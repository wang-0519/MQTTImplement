package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;

import java.util.Arrays;

/**
 * Qos2第二步回应
 * Created by admin on 2020/2/16.
 */
public class PubrelMessage extends AbstractMess {

    /**
     * 构造函数
     * 输入需要回复的报文标识符
     * @param identify
     */
    public PubrelMessage(byte[] identify){
        messIdentify = identify;
        editVariableHeader();
        editFixedHeader();
        super.editUBytes();
    }

    /**
     * 需要解析时传入二进制报文
     * @param bytes
     * @param sign
     */
    public PubrelMessage(byte[] bytes, int sign){
        uBytes = bytes;
    }

    /**
     * 编辑固定报头
     * 剩余长度为2
     */
    private void editFixedHeader(){
        typeOfMess = BytesHandler.or(typeOfMess,"01100010");
        remainLength = BytesHandler.editRemainLen(2);
        fixedHeader = BytesHandler.connAll(typeOfMess, remainLength);
    }

    /**
     * 编辑可变报头
     * 可变报头只包含取消确认的报文的报文标识符
     */
    private void editVariableHeader(){
        variableHeader = messIdentify;
    }

    @Override
    public boolean analysisMess() {
        if(super.analysisMess()){
            messIdentify = Arrays.copyOfRange(uBytes, 1 + remainLength.length, uBytes.length);
            variableHeader = messIdentify;
        }
        return true;
    }
}
