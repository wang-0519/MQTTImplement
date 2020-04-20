package client;

import MQTTMessage.AbstractMess;
import helperClass.TimeGetter;
import helperClass.Translater;

import java.io.Serializable;

/**
 * 消息，消息内容及时间
 * Created by admin on 2020/3/22.
 */
public class Message implements Serializable{
    private byte[] bytes = null;
    private String message = null;
    private String time = null;
    private int qos = 0;
    private boolean isRetain = false;

    public Message(String mess){
        message = mess;
        bytes = Translater.strToBin(mess);
        time = TimeGetter.getTime();
    }

    public Message(byte[] bytes){
        this.bytes = bytes;
        message = Translater.binToString(bytes);
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

    public boolean isRetain() {
        return isRetain;
    }

    public void setRetain(boolean retain) {
        isRetain = retain;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
