package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;

import java.util.Arrays;

/**
 * Qos2 第三步回应
 * Created by admin on 2020/2/16.
 */
public class PubcompMessage extends AbstractMess {

    /**
     * 构造函数
     * 输入需要回复的报文标识符
     * @param identify
     */
    public PubcompMessage(byte[] identify){
        mess_identify = identify;
        editVariableHeader();
        editFixedHeader();
        super.editUBytes();
    }

    /**
     * 需要解析报文时传入二进制报文
     * @param bytes
     * @param sign
     */
    public PubcompMessage(byte[] bytes, int sign){
        uBytes = bytes;
    }

    /**
     * 编辑固定报头
     * 剩余长度为2
     */
    private void editFixedHeader(){
        typeOfMess = BytesHandler.or(typeOfMess,"01110000");
        remainLength = BytesHandler.editRemainLen(2);
        fixedHeader = BytesHandler.connAll(typeOfMess, remainLength);
    }

    /**
     * 编辑可变报头
     * 可变报头只包含取消确认的报文的报文标识符
     */
    private void editVariableHeader(){
        variableHeader = mess_identify;
    }

    @Override
    public boolean analysisMess() {
        if(super.analysisMess()){
            mess_identify = Arrays.copyOfRange(uBytes, 1 + remainLength.length, uBytes.length);
            variableHeader = mess_identify;
        }
        return true;
    }
}
