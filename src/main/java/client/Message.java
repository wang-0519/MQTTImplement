package client;

import MQTTMessage.AbstractMess;
import helperClass.TimeGetter;

import java.io.Serializable;

/**
 * 消息，消息内容及时间
 * Created by admin on 2020/3/22.
 */
public class Message implements Serializable{
    private String message = null;
    private String time = null;
    private int qos = 0;

    public Message(String mess){
        message = mess;
        time = TimeGetter.getTime();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public void setQos(String qos){
        this.qos = qos.charAt(3) - '0';
    }
}
