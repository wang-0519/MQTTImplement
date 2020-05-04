package messageHandler;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by admin on 2020/2/5.
 */
public class MessageQueue {

    /**
     * 发送消息队列，需要确认的消息入队列，确认消息到达之后删除
     * 如需重发按顺序发送报文
     */
    private ArrayList<AbstractMess> messDeque = null;

    /**
     * 构造器
     */
    public MessageQueue(){
        messDeque = new ArrayList<>();
    }

    /**
     * 添加报文对象到队列中
     * @param mess
     */
    public void addMess(AbstractMess mess){
        messDeque.add(mess);
    }

    /**
     * 接收到确认报文后删除队列中的消息信息
     * 适用于有报文标识符的报文  发布、订阅报文
     * @param identify
     */
    public void deleteMess(byte[] identify){
        int i = 0;
        while(i < messDeque.size() && !Arrays.equals(messDeque.get(i).getMessIdentify(),identify)){
            i++;
        }
        if(i < messDeque.size()){
            messDeque.remove(i);
        }
    }

    public AbstractMess findMessByIdentify(byte[] identify){
        int i = 0;
        while(i < messDeque.size() && !Arrays.equals(messDeque.get(i).getMessIdentify(),identify)){
            i++;
        }
        if(i < messDeque.size()){
            return messDeque.get(i);
        }
        return null;
    }

    /**
     * 删除队列中的消息，消息类型编号，
     * 适用于连接报文，ping报文
     * @param typeOfMess
     */
    public void deleteMess(int typeOfMess){
        int i = 0;
        while(i < messDeque.size() && BytesHandler.getTypeOfMessage(messDeque.get(i).getTypeOfMess()[0]) != typeOfMess){
            i++;
        }
        if(i < messDeque.size()){
            messDeque.remove(i);
        }
    }

    /**
     * 获取消息队列
     * @return
     */
    public ArrayList<AbstractMess> getMessDeque(){
        return messDeque;
    }

    /**
     * 获取队列容量大小
     * @return
     */
    public int size(){
        return messDeque.size();
    }

    /**
     * 获取第一个报文
     * @return
     */
    public AbstractMess getNextMessage(){
        return messDeque.get(0);
    }

    /**
     * 删除第一个报文
     */
    public void deleteNext(){
        messDeque.remove(0);
    }

    /**
     *
     * @param mess
     * @return
     */
    public boolean contains(AbstractMess mess){
        return messDeque.contains(mess);
    }
}
