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
        if(obj == null){
            return false;
        }
        if(obj.getClass() != getClass()){
            return false;
        }
        TopicInformation info = (TopicInformation)obj;
        return this.topicName.equals(info.getTopicName()) && this.tpoicType == info.getTpoicType();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = (topicName == null ? 0 : topicName.hashCode()) + tpoicType.hashCode();
        return hash;
    }

    /**
     * 主题过滤器匹配
     * @param tn
     * @return
     */
    public boolean isTopicFilter(String tn){
        if(tpoicType == TOPICTYPE.PUBLISH){
            return false;
        }
        String[] names = topicName.split("/");
        String[] name = tn.split("/");
        if(names.length > name.length){
            return false;
        }
        for(int i = 0; i < names.length; i++){
            if(names[i].equals("#")){
                return true;
            }
            if(names[i].equals("+")){
                i++;
            }else if( !names[i].equals(name[i]) ){
                return false;
            } else {
                i++;
            }
        }
        return true;
    }
}
