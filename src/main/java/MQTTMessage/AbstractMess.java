package MQTTMessage;

import helperClass.BytesHandler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by admin on 2020/2/4.
 */
public abstract class AbstractMess implements Serializable{

    //utf-8二进制报文
    protected byte[] uBytes = null;
    //报文标识符
    protected byte[] messIdentify = null;
    /**
     * 报文的各类信息
     * message 报文
     * remainLength  剩余长度
     * topic 主题
     * Qos 质量属性
     */
    protected HashMap<String,String> mess = null;

    //固定报头内容
    protected byte[] fixedHeader = null;
    //报文类型 + 标志位
    protected byte[] typeOfMess = new byte[1];
    //剩余长度  字节数
    protected byte[] remainLength = null;

    public byte[] getTypeOfMess() {
        return typeOfMess;
    }

    //可变报头内容
    protected byte[] variableHeader = null;

    //负载
    protected byte[] packageValue = null;

    //消息方向
    private String messDir = "C->S";

    /**
     * 初始化参数
     */
    public AbstractMess(){
        typeOfMess[0] = 0;
        mess = new HashMap<>();
    }

    /**
     * 获取二进制报文
     * @return
     */
    public byte[] getUBytes(){
        return uBytes;
    };


    /**
     * 编辑二进制报文
     */
    public void editUBytes(){
        uBytes = BytesHandler.connAll(fixedHeader, variableHeader, packageValue);
    }

    /**
     * 获取报文标识符
     * @return
     */
    public byte[] getMessIdentify(){
        return messIdentify;
    }

    /**
     * 获取报文信息
     * @return
     */
    public String getMessage() {
        return mess.get("message");
    }

    /**
     * 获取HashMap
     * @return
     */
    public HashMap<String,String> getOtherMess(){
        return mess;
    }

    /**
     * 二进制报文信息解析，解析为HashMap
     */
    public boolean analysisMess(){
        typeOfMess = Arrays.copyOfRange(uBytes,0,1);
        int i = 1;
        while(BytesHandler.and(uBytes[i],"10000000") != 0){
            i++;
        }
        remainLength = Arrays.copyOfRange(uBytes,1,i+1);
        mess.put("remainLength","" + BytesHandler.analysisRemainLen(remainLength));
        fixedHeader = BytesHandler.connAll(typeOfMess, remainLength);
        return true;
    }

    /**
     * 获取固定报头
     * @return
     */
    public byte[] getFixedHeader() {
        return fixedHeader;
    }

    public byte[] getVariableHeader() {
        return variableHeader;
    }

    public byte[] getPackageValue() {
        return packageValue;
    }

    public String getMessDir() {
        return messDir;
    }

    public void setMessDir(String messDir) {
        this.messDir = messDir;
    }
}
