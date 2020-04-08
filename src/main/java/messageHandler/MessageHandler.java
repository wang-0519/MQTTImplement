package messageHandler;

import MQTTMessage.AbstractMess;
import client.ClientInformation;
import client.HelpMess;

import java.net.Socket;

/**
 * Created by admin on 2020/2/21.
 */
public class MessageHandler {

    //客户端信息
    private ClientInformation client = null;

    //java Socket对象
    private Socket socket = null;

    /**
     * 各类线程
     */
    private SendMessageThread sendThread = null;
    private ReciveMessageThread reciveThread = null;
    private PingReqThread pingReqThread = null;


    //默认连接地址
    private String addr = "127.0.0.1";
    private int port = 1883;

    /**
     * 构造器  传入socket获取的输入输出流
     * @param ci  客户端信息
     */
    public MessageHandler(ClientInformation ci){
        try{
            this.client = ci;

            //连接服务器
            if(client.getAddr() != null){
                String[] strs = client.getAddr().split(":");
                addr = strs[0];
                port = Integer.valueOf(strs[1]);
            }
            socket = new Socket(addr,port);

            //socket连接成功
            //启动各类线程
            client.setState(ClientInformation.CONN_STATE.SOCKET_CONNED);
            sendThread = new SendMessageThread(socket.getOutputStream(), client);
            sendThread.start();
            reciveThread = new ReciveMessageThread(sendThread, socket.getInputStream(), client);
            new Thread(reciveThread).start();
            pingReqThread = new PingReqThread(sendThread, client);
            new Thread(pingReqThread).start();
        }catch (Exception exception){
            client.setState(ClientInformation.CONN_STATE.CONN_ERROR);
            HelpMess errorMess = new HelpMess();
            errorMess.setType(HelpMess.HELP_MESS_TYPE.ERROR);
            errorMess.setErrorMessage("网络连接错误！");
            MessageObservable.getInstance().notifyObserver(errorMess);
        }
    }

    /**
     * 发送报文
     * @param mess
     */
    public void send(AbstractMess mess){
        sendThread.send(mess);
    }


    /**
     * 关闭输入输出流
     */
    public void close(){
        try{
            reciveThread.closeInputStream();
            pingReqThread.close();
            sendThread.closeOutputStream();
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //添加消息到待发送队列
    public void addMessageToQueue(AbstractMess message){
        sendThread.addMessageToQueue(message);
    }
}
