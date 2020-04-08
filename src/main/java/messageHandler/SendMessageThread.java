package messageHandler;

import MQTTMessage.AbstractMess;
import client.ClientInformation;
import helperClass.BytesHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 处理发送的消息
 * Created by admin on 2020/2/27.
 */
public class SendMessageThread extends Thread{

    //等待回复的消息
    private MessageQueue sended = null;
    //等待发送
    private MessageQueue waitSend = null;
    //客户端信息
    private ClientInformation client = null;

    //输出流
    private OutputStream os = null;

    //线程池
    private ThreadPoolExecutor threadPool = null;

    //锁
    private final byte[] by = new byte[0];

    //运行标志,通过改变此变量停止线程
    private boolean sign = true;

    /**
     * 构造器
     * @param ous
     */
    public SendMessageThread(OutputStream ous, ClientInformation client){
        os = ous;
        this.client = client;
        waitSend = new MessageQueue();
        sended = new MessageQueue();
        try{
            threadPool = new ThreadPoolExecutor(5,10,100, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.sign = true;
        try{
            while(sign){
                if(waitSend.size() != 0){
                    //发送未发送的消息
                    while(waitSend.size() != 0){
                        os.write(waitSend.getNextMessage().getUBytes());
                        os.flush();
                        addMessageToSended(waitSend.getNextMessage());
                        waitSend.deleteNext();
                    }
                }
                synchronized (by){
                    by.wait();
                }
            }
        }catch (IOException io){
            io.printStackTrace();
          setSign(false);
          client.setState(ClientInformation.CONN_STATE.CONN_ERROR);
        } catch (Exception e){
            e.printStackTrace();
            setSign(false);
        }
    }

    /**
     * 发送报文
     * @param mess
     */
    public void send(AbstractMess mess){
        client.addHistoryMessage(mess);
        waitSend.addMess(mess);
        synchronized (by){
            by.notify();
        }
        if(sign){
            System.out.println("客户端存活");
        }

    }

    /**
     * 添加报文到已发送队列,并开启重发线程
     * @param mess
     */
    private void addMessageToSended(AbstractMess mess){
        if(sended.getMessDeque().contains(mess)){
            return;
        }
        int k = BytesHandler.getTypeOfMessage(mess.getFixedHeader()[0]);
        if( k == 1 || k == 5 || k == 6 || k == 8 || k == 10){
            sended.addMess(mess);
            try{
                ReSendMessage send = new ReSendMessage(mess,this, 3, 10000);
                threadPool.execute(send);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if( k == 3 && (mess.getOther_mess().get("Qos").charAt(3) - '0') != 0){
            sended.addMess(mess);
            try{
                ReSendMessage send = new ReSendMessage(mess,this, 3, 10000);
                threadPool.execute(send);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if( k == 12){
            sended.addMess(mess);
        }
    }

    /**
     * 收到回复后根据报文类型删除队列中的消息
     * 适用于connack，ping
     * @param typeOfMess
     */
    public void delete(int typeOfMess){
        sended.deleteMess(typeOfMess);
    }

    /**
     * 收到回复后根据报文标识符删除队列中的消息
     * 适用于 发布，订阅等含有报文标识符的报文
     * @param identify
     */
    public void delete(byte[] identify){
        sended.deleteMess(identify);
    }

    /**
     * 关闭输出流
     */
    public void closeOutputStream(){
        try{
            setSign(false);
            synchronized (by){
                by.notify();
            }
            threadPool.shutdown();
            os.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    synchronized public boolean contains(AbstractMess mess){
        if(sended.contains(mess)){
            for(AbstractMess absmess : sended.getMessDeque()){
                System.out.println(Arrays.toString(absmess.getUBytes()));
            }
        }
        return sended.contains(mess);
    }

    //关闭线程
    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public void addMessageToQueue(AbstractMess message){
        client.addHistoryMessage(message);
        waitSend.addMess(message);
    }

    public AbstractMess getMessByIdentify(byte[] identify){
        return sended.findMessByIdentify(identify);
    }
}
