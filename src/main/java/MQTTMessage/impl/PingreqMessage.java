package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;

/**
 * Created by admin on 2020/2/17.
 */
public class PingreqMessage extends AbstractMess{

    public PingreqMessage(){
        try{
            typeOfMess = BytesHandler.or(typeOfMess, "11000000");
            remainLength = BytesHandler.editRemainLen(0);
            fixedHeader = BytesHandler.connAll(typeOfMess, remainLength);
            uBytes = fixedHeader;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
