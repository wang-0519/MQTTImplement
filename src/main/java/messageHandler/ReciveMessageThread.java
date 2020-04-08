package messageHandler;

import client.ClientInformation;
import client.HelpMess;
import MQTTMessage.AbstractMess;
import MQTTMessage.impl.*;
import client.Message;
import client.TopicInformation;
import helperClass.BytesHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by admin on 2020/2/28.
 */
public class ReciveMessageThread implements Runnable{

    //客户端信息
    private ClientInformation client = null;

    //发送消息的线程
    private SendMessageThread sendThread = null;

    //socket输入流
    private BufferedInputStream bi = null;

    //运行标志
    private boolean sign = true;

    /**
     * 构造器
     * @param thread
     * @param is
     */
    public ReciveMessageThread(SendMessageThread thread, InputStream is, ClientInformation client){
        this.sendThread = thread;
        this.bi = new BufferedInputStream(is);
        this.client = client;
    }

    @Override
    public void run() {
        while(sign){
            try{
                int temp = 0;
                byte[] type = new byte[1];
                byte[] buffer = null;
                while((temp = bi.read()) != -1){
                    type[0] = (byte)temp;
                    buffer = type;
                    int count = 0;
                    byte[] remainlen = new byte[4];
                    temp = bi.read();
                    while(temp != -1 && BytesHandler.and((byte)temp,"10000000") != 0){
                        remainlen[count] = (byte)temp;
                        count++;
                        temp = bi.read();
                    }
                    remainlen[count] = (byte)temp;
                    buffer = BytesHandler.connAll(buffer, Arrays.copyOfRange(remainlen, 0, count + 1));
                    int remainLen = BytesHandler.analysisRemainLen(Arrays.copyOfRange(remainlen, 0, count+1));
                    count = 0;
                    byte[] bytes = new byte[remainLen];
                    while((count = bi.read(bytes)) < remainLen){
                        buffer = BytesHandler.connAll(buffer, Arrays.copyOfRange(bytes, 0, count));
                        remainLen -= count;
                        bytes = new byte[remainLen];
                    }
                    if(count == remainLen){
                        buffer = BytesHandler.connAll(buffer, bytes);
                    }
                    handleMess(buffer);
                }
            }catch (IOException e){
                setSign(false);
                client.setState(ClientInformation.CONN_STATE.CONN_ERROR);
            }
        }
    }

    /**
     * 处理接收到的消息
     * @param bytes
     */
    private void handleMess(byte[] bytes){
        AbstractMess message = null;
        int temp = BytesHandler.getTypeOfMessage(bytes[0]);
        switch (temp){
            case 2:
                //connack
                message = new ConnAckMessage(bytes);
                if( message.analysisMess() ){
                    //报文正确，连接成功
                    client.setState(ClientInformation.CONN_STATE.CONN);
                    sendThread.delete(1);
                } else {
                    //连接错误
                    HelpMess errorMess = new HelpMess();
                    errorMess.setId(client.getId());
                    errorMess.setType(HelpMess.HELP_MESS_TYPE.ERROR);
                    errorMess.setErrorMessage(message.getOther_mess().get("errorMessage"));
                    MessageObservable.getInstance().notifyObserver(errorMess);
                }
                break;
            case 3:
                //publish
                message = new PublishMessage(bytes);
                if( message.analysisMess() ){

                    HelpMess helpMess = new HelpMess();
                    helpMess.setId(client.getId());
                    helpMess.setType(HelpMess.HELP_MESS_TYPE.RECIVE);
                    helpMess.setTopic(message.getOther_mess().get("topic"));
                    Message help = new Message(message.getOther_mess().get("message"));
                    help.setQos(message.getOther_mess().get("Qos"));
                    client.getTopic(message.getOther_mess().get("topic")).addMessage(help);
                    helpMess.setMessage(help);
                    MessageObservable.getInstance().notifyObserver(helpMess);

                    if(message.getOther_mess().get("Qos").equals("Qos1")){
                        sendThread.send(new PubackMessage(message.getMess_identify()));
                    }
                    if(message.getOther_mess().get("Qos").equals("Qos2")){
                        sendThread.send(new PubrecMessage(message.getMess_identify()));
                    }
                }
                break;
            case 4:
                //puback
                message = new PubackMessage(bytes, 1);
                if( message.analysisMess() ){
                    sendThread.delete(message.getMess_identify());
                }
                break;
            case 5:
                //pubrec
                message = new PubrecMessage(bytes, 1);
                if( message.analysisMess() ){
                    sendThread.send(new PubrelMessage(message.getMess_identify()));
                    sendThread.delete(message.getMess_identify());
                }
                break;
            case 6:
                //pubrel
                message = new PubrelMessage(bytes, 1);
                if( message.analysisMess() ){
                    sendThread.send(new PubcompMessage(message.getMess_identify()));
                    sendThread.delete(message.getMess_identify());
                }
                break;
            case 7:
                //pubcomp
                message = new PubcompMessage(bytes, 1);
                if( message.analysisMess() ){
                    sendThread.delete(message.getMess_identify());
                }
                break;
            case 9:
                //suback
                message = new SubackMessage(bytes);
                if(message.analysisMess()){
                    ArrayList<TopicInformation> topics = ((SubscribeMessage)(sendThread.getMessByIdentify(message.getMess_identify()))).getTopics();
                    ArrayList<Integer> topicIsSub = ((SubackMessage)message).getTopicsIsSub();
                    if(topicIsSub.size() == topics.size()){
                        for(int i = 0; i < topicIsSub.size(); i++){
                            topics.get(i).setQos(topicIsSub.get(i));
                        }
                    }
                    client.updateTopicInformation(topics);
                    sendThread.delete(message.getMess_identify());
                }
                break;
            case 11:
                //unsuback
                message = new UnsubackMessage(bytes);
                if(message.analysisMess()){
                    ArrayList<TopicInformation> topics = ((UnsubscribeMessage)(sendThread.getMessByIdentify(message.getMess_identify()))).getTopics();
                    sendThread.delete(message.getMess_identify());
                }
                break;
            case 13:
                //pingresq
                message = new PingrespMessage(bytes);
                sendThread.delete(12);
                break;
            default:
                //报错
                System.out.println("报文类型错误");
        }
        if(message != null){
            client.addHistoryMessage(message);
        }
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    /**
     * 关闭
     */
    public void closeInputStream(){
        try{
            setSign(false);
            bi.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
