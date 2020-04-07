package client;

import MQTTMessage.AbstractMess;
import MQTTMessage.impl.*;
import messageHandler.AbstractMessageObserver;
import messageHandler.MessageHandler;
import messageHandler.MessageObservable;

import java.util.ArrayList;

/**
 * Created by admin on 2020/2/4.
 */
public class MQTTClient{

    //客户端信息
    private ClientInformation client = null;

    //消息处理器
    private MessageHandler handler = null;

    /**
     * 创建客户端，如果需要自动连接发送连接报文
     * @param client
     */
    public MQTTClient(ClientInformation client){
        this.client = client;
        if(client.isAutoConn()){
            connect();
        }
    }

    //获取客户端信息
    public ClientInformation getClient() {
        return client;
    }

    public void setClient(ClientInformation client) {
        this.client = client;
    }

    /**
     * 发送连接报文
     */
    public void connect(){
        if(client.getState() == ClientInformation.CONN_STATE.NO_CONN || client.getState() == ClientInformation.CONN_STATE.CONN_ERROR){
            handler = new MessageHandler(client);
        }
        ConnMessage conn = new ConnMessage(client.getId(), client.getUserName(), client.getPassword(), client.isWillRetain(),
                client.getWillQos(), client.getWillTopic(), client.getWillMessage(), client.isCleanSession(),
                client.getKeepAlive());
        if(client.getState() == ClientInformation.CONN_STATE.SOCKET_CONNED){
            handler.send(conn);
        }
    }

    /**
     * 发布报文
     */
    public void publish(int qos, boolean retain, String topic, String mess){
        AbstractMess message= new PublishMessage(qos, retain, topic, mess);
        Message publishMessage = new Message(mess);
        publishMessage.setQos(qos);
        client.getTopic(topic).addMessage(publishMessage);
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            handler.send(message);
        } else if(client.getState() == ClientInformation.CONN_STATE.SOCKET_CONNED){
            handler.addMessageToQueue(message);
        }
    }


    /**
     * 订阅主题
     */
    public void subscribe(ArrayList<TopicInformation> topic){
        AbstractMess message = new SubscribeMessage(topic);
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            handler.send(message);
            client.addTopicInformation(topic);
        }
    }


    /**
     * 取消订阅
     */
    public void unSubscribe(ArrayList<TopicInformation> topic){
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            client.deleteTopicInformation(topic);
            AbstractMess message = new UnsubscribeMessage(topic);
            handler.send(message);
        }
    }


    /**
     * 断开连接
     */
    public void disConnect(){
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            AbstractMess message = new DisconnectMessage();
            handler.send(message);
            client.setState(ClientInformation.CONN_STATE.SOCKET_CONNED);
        }
    }

    /**
     * 关闭服务器
     */
    public void close(){
        if(client.getState() == ClientInformation.CONN_STATE.SOCKET_CONNED){
            handler.close();
            client.setState(ClientInformation.CONN_STATE.NO_CONN);
        }
    }

    /**
     * 添加观察者, 用来向Android项目传递消息
     * @param observer
     */
    public void addObserver(AbstractMessageObserver observer){
        MessageObservable.getInstance().addObserver(observer);
    }
}
