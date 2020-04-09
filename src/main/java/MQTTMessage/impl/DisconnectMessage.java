package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;

/**
 * 断开连接
 * Created by admin on 2020/2/17.
 */
public class DisconnectMessage extends AbstractMess {

    public DisconnectMessage(){
        typeOfMess = BytesHandler.or(typeOfMess, "11100000");
        remainLength = BytesHandler.editRemainLen(0);
        fixedHeader = BytesHandler.connAll(typeOfMess, remainLength);
        uBytes = fixedHeader;
    }
}
