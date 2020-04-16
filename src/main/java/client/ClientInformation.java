package client;

import MQTTMessage.AbstractMess;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 客户端信息
 * Created by admin on 2020/3/22.
 */
public class ClientInformation implements Serializable {

    public static enum CONN_STATE{CONN, NO_CONN, CONN_ERROR, SOCKET_CONNED} //连接， 未连接， 连接错误, socket已连接
    private CONN_STATE state = CONN_STATE.NO_CONN;

    private String id;  //客户端id
    private String name;  //客户端名称
    private String addr;  //连接地址
    private String userName; //用户名
    private String password;  //密码
    private int reconnPeriod;  //重连延时  毫秒
    private int connTimeout;  //连接超时 毫秒
    private int keepAlive;   //ping延时 秒
    private String willTopic;  //遗嘱主题
    private int willQos;    //遗嘱消息Qos
    private String willMessage;   //遗嘱消息
    private boolean reschedulePing;   //刷新ping
    private boolean cleanSession;   //清理会话
    private boolean autoConn;     //自动连接
    private boolean mqttVersion;   //兼容 MQTT 3.1.1
    private boolean queueQos0;   //队列传出Qos0消息
    private boolean willRetain;   //遗嘱保留

    //话题信息
    private ArrayList<TopicInformation> topicInformation = null;

    //历史报文
    private ArrayList<AbstractMess> historyMessage = null;

    //是否有新消息
    private boolean signOfNew = false;

    public ClientInformation(){
        topicInformation = new ArrayList<>();
        historyMessage = new ArrayList<>();
    }

    /**
     * 获取某一话题对象
     */
    public TopicInformation getTopic(String topicName, TopicInformation.TOPICTYPE type){
        int i = 0;
        while (i < topicInformation.size() && (! topicInformation.get(i).getTopicName().equals(topicName) || type != topicInformation.get(i).getTpoicType())){
            i++;
        }
        if(i < topicInformation.size()){
            return topicInformation.get(i);
        }
        return null;
    }

    /**
     * 添加话题信息
     * @param ti
     */
    public void addTopic(TopicInformation ti){
        int i = 0;
        while(i < topicInformation.size() && !topicInformation.get(i).equals(ti)){
            i++;
        }
        if(i < topicInformation.size()){
            topicInformation.get(i).setQos(ti.getQos());
        } else {
            topicInformation.add(ti);
        }
    }

    /**
     * 添加一组话题
     * @param topicInformation
     */
    public void addTopicInformation(ArrayList<TopicInformation> topicInformation) {
        for(TopicInformation ti : topicInformation){
            addTopic(ti);
        }
    }

    /**
     * 跟新话题信息
     * @param topics
     */
    public void updateTopicInformation(ArrayList<TopicInformation> topics){
        for(TopicInformation ti : topics){
            int i = 0;
            while(i < topicInformation.size() && !ti.equals(topicInformation.get(i))){
                i++;
            }
            if(i < topicInformation.size()){
                topicInformation.get(i).setQos(ti.getQos());
            }
        }
    }

    /**
     *删除一组话题
     * @param topicInformation
     */
    public void deleteTopicInformation(ArrayList<TopicInformation> topicInformation){
        for(TopicInformation ti : topicInformation){
            deleteTopic(ti);
        }
    }

    /**
     * 删除单个话题
     * @param ti
     */
    public void deleteTopic(TopicInformation ti){
        deleteTopic(ti.getTopicName(), ti.getTpoicType());
    }

    /**
     * 删除对应话题信息
     * @param topicName
     */
    public boolean deleteTopic(String topicName, TopicInformation.TOPICTYPE type){
        if(getTopic(topicName, type) == null){
            return false;
        }else {
            topicInformation.remove(getTopic(topicName, type));
            return true;
        }
    }

    /**
     * 清空历史二进制报文
     */
    public void clearHistory(){
        historyMessage.clear();
    }

    /**
     * 判断是否有新消息
     * @return
     */
    public boolean hasNew(){
        return signOfNew;
    }

    /**
     * 设置存在新消息
     * @param topicInformation
     */
    public void setNewFalse(TopicInformation topicInformation){
        getTopic(topicInformation.getTopicName(), topicInformation.getTpoicType()).setNew(false);
        flushNew();
    }

    /**
     * 刷新是否有新消息
     */
    public void flushNew(){
        int i = 0;
        while(i < topicInformation.size() && !topicInformation.get(i).hasNew()){
            i++;
        }
        if(i < topicInformation.size()){
            this.signOfNew = true;
        } else {
            this.signOfNew = false;
        }
    }

    public CONN_STATE getState() {
        return state;
    }

    public void setState(CONN_STATE state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<AbstractMess> getHistoryMessage() {
        return historyMessage;
    }

    public void addHistoryMessage(AbstractMess historyMessage) {
        this.historyMessage.add(historyMessage);
    }

    public ArrayList<TopicInformation> getTopicInformation() {
        return topicInformation;
    }

    public void setTopicInformation(ArrayList<TopicInformation> topicInformation) {
        this.topicInformation = topicInformation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReconnPeriod() {
        return reconnPeriod;
    }

    public void setReconnPeriod(int reconnPeriod) {
        this.reconnPeriod = reconnPeriod;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(int keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public void setWillTopic(String willTopic) {
        this.willTopic = willTopic;
    }

    public int getWillQos() {
        return willQos;
    }

    public void setWillQos(int willQos) {
        this.willQos = willQos;
    }

    public void setWillQos(String willQos) {
        this.willQos = willQos.charAt(3) - '0';
    }

    public String getWillMessage() {
        return willMessage;
    }

    public void setWillMessage(String willMessage) {
        this.willMessage = willMessage;
    }

    public boolean isReschedulePing() {
        return reschedulePing;
    }

    public void setReschedulePing(boolean reschedulePing) {
        this.reschedulePing = reschedulePing;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    public boolean isAutoConn() {
        return autoConn;
    }

    public void setAutoConn(boolean autoConn) {
        this.autoConn = autoConn;
    }

    public boolean isMqttVersion() {
        return mqttVersion;
    }

    public void setMqttVersion(boolean mqttVersion) {
        this.mqttVersion = mqttVersion;
    }

    public boolean isQueueQos0() {
        return queueQos0;
    }

    public void setQueueQos0(boolean queueQos0) {
        this.queueQos0 = queueQos0;
    }

    public boolean isWillRetain() {
        return willRetain;
    }

    public void setWillRetain(boolean willRetain) {
        this.willRetain = willRetain;
    }
}
