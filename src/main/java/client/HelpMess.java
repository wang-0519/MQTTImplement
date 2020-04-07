package client;

import java.io.Serializable;

/**
 * 接收到消息时，将消息传递给观察者
 * 单例模式，保存客户端id，话题名称， 消息内容
 * Created by admin on 2020/3/25.
 */
public class HelpMess implements Serializable{
    //是否未错误信息
    private boolean isError = false;

    //客户端id
    private String id = null;
    //话题名称
    private String topic = null;
    //报文
    private Message message = null;

    //错误信息
    private String errorMessage = null;

    static private HelpMess instance = new HelpMess();

    private HelpMess(){

    }

    public static HelpMess getIntance(){
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = new Message(message);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
