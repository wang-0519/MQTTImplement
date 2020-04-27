package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;
import helperClass.PackageIdentify;
import helperClass.Translater;

import java.util.Arrays;

/**
 * 发布报文
 * Created by admin on 2020/2/15.
 */
public class PublishMessage extends AbstractMess {


    /**
     * 发布报文
     * @param qos   qos服务质量
     * @param retain   保留标志位
     * @param topic    主题
     * @param mess     发布的报文
     */
    public PublishMessage(int qos, boolean retain, String topic, String mess){
        this(qos, retain, topic, Translater.strToBin(mess));
    }

    public PublishMessage(int qos, boolean retain, String topic, byte[] pack){
        this.mess.put("message", Translater.binToString(pack));
        this.mess.put("Qos","Qos" + qos);
        this.mess.put("topic",topic);
        if(qos > 0 ){
            this.mess_identify = PackageIdentify.getIdentify();
        }
        int i = editVariableHeader(topic, qos);
        packageValue = pack;
        this.mess.put("remainLength", "" + (i + pack.length));
        editFixedHeader(qos, retain, i + pack.length);
        super.editUBytes();
    }

    public PublishMessage(byte[] bytes){
        uBytes = bytes;
    }

    /**
     * 固定报头
     * @param qos  qos质量
     * @param retain  消息保留标志
     * @param remainLen  剩余长度
     */
    private void editFixedHeader(int qos, boolean retain, int remainLen){
        int temp = retain ? 1 : 0;
        typeOfMess = BytesHandler.or(typeOfMess, "00110" + qos/2 + qos%2 + temp);
        remainLength = BytesHandler.editRemainLen(remainLen);
        fixedHeader = BytesHandler.connAll(typeOfMess,remainLength);
    }

    /**
     * 编辑可变报头
     * @param topic  主题
     * @param qos    qos质量服务，当qos > 0 时可变报头中加入报文标识符
     * @return    返回可变报头长度
     */
    private int editVariableHeader(String topic, int qos){
        byte[] uTopic = BytesHandler.editStrWithLen(topic);
        if(qos > 0){
            variableHeader = BytesHandler.connAll(uTopic, getMess_identify());
        } else {
            variableHeader = uTopic;
        }
        return variableHeader.length;
    }

    /**
     * 编辑有效载荷
     * @param mess   报文，字符串
     * @return   返回报文长度
     */
    private int editPackage(String mess){
        packageValue = Translater.strToBin(mess);
        return packageValue.length;
    }

    /**
     * 分析二进制报文
     * 将信息存入map中  Qos 服务质量
     *                  topic 话题
     *                  message  消息
     * @return
     */
    @Override
    public boolean analysisMess() {
        super.analysisMess();

        // 除2为右移一位操作
        mess.put("Qos","Qos" + BytesHandler.and(typeOfMess[0],"00000110")/2);

        int sign = remainLength.length + 1;
        int k = Translater.binToInt(Arrays.copyOfRange(uBytes,sign,sign + 2));
        variableHeader = Arrays.copyOfRange(uBytes, sign, sign + 2 + k);
        sign += 2;
        mess.put("topic", Translater.binToString(Arrays.copyOfRange(uBytes, sign, sign + k)));
        sign += k;
        if(!mess.get("Qos").equals("Qos0")){
            mess_identify = Arrays.copyOfRange(uBytes, sign, sign + 2);
            variableHeader = BytesHandler.connAll(variableHeader, Arrays.copyOfRange(uBytes, sign, sign + 2));
            sign += 2;
        }

        packageValue = Arrays.copyOfRange(uBytes, sign, uBytes.length);
        mess.put("message", Translater.binToString(Arrays.copyOfRange(uBytes, sign, uBytes.length)));

        return true;
    }
}
