package client;

/**
 * 话题信息， 包含一个 Message消息列表
 * Created by admin on 2020/3/22.
 */
import java.io.Serializable;
import java.util.ArrayList;

public class TopicInformation implements Serializable {

    /**
     * topic类型枚举
     * PUBLISH  发布
     * SUBSCRIBE  订阅
     */
    public static enum TOPICTYPE{PUBLISH, SUBSCRIBE}

    //topic 名称及类型
    private String topicName = null;
    private TOPICTYPE tpoicType = TOPICTYPE.SUBSCRIBE;
    private int Qos = 0;

    //是否有新消息
    boolean signOfNew = false;

    //消息列表
    private ArrayList<Message> messages = null;

    public TopicInformation(){
        messages = new ArrayList<>();
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public TOPICTYPE getTpoicType() {
        return tpoicType;
    }

    public void setTpoicType(TOPICTYPE tpoicType) {
        this.tpoicType = tpoicType;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        messages.add(message);
        if(tpoicType == TOPICTYPE.SUBSCRIBE){
            this.signOfNew = true;
        }
    }

    public int getQos() {
        return Qos;
    }

    public void setQos(int qos) {
        Qos = qos;
    }

    public void setQos(String qos) {
        this.Qos = qos.charAt(3) - '0';
    }

    /**
     * 判断是否有新消息
     * @return
     */
    public boolean hasNew(){
        return signOfNew;
    }

    /**
     * 设置新消息标志
     * @param temp
     */
    public void setNew(boolean temp){
        signOfNew = temp;
    }

    @Override
    public boolean equals(Object obj) {
        TopicInformation info = (TopicInformation)obj;
        return this.topicName.equals(info.getTopicName()) && this.tpoicType == info.getTpoicType();
    }
}
