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
    public void publish(TopicInformation topic, Message mess){
        AbstractMess publishMessage= new PublishMessage(mess.getQos(), mess.isRetain(), topic.getTopicName(), mess.getBytes());
        client.getTopic(topic.getTopicName(), topic.getTpoicType()).addMessage(mess);
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            handler.send(publishMessage);
        } else if(client.getState() == ClientInformation.CONN_STATE.SOCKET_CONNED){
            handler.addMessageToQueue(publishMessage);
        }
    }


    /**
     * 订阅主题
     */
    public void subscribe(ArrayList<TopicInformation> topic){
        client.addTopicInformation(topic);
        AbstractMess message = new SubscribeMessage(topic);
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            handler.send(message);
        } else if(client.getState() == ClientInformation.CONN_STATE.SOCKET_CONNED){
            handler.addMessageToQueue(message);
        }
    }


    /**
     * 取消订阅
     */
    public void unSubscribe(ArrayList<TopicInformation> topic){
        client.deleteTopicInformation(topic);
        AbstractMess message = new UnsubscribeMessage(topic);
        if(client.getState() == ClientInformation.CONN_STATE.CONN){
            handler.send(message);
        } else if(client.getState() == ClientInformation.CONN_STATE.SOCKET_CONNED){
            handler.addMessageToQueue(message);
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
     * 清空二进制历史报文
     */
    public void clearHistory(){
        client.clearHistory();
    }
}
