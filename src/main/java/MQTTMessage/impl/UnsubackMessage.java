package MQTTMessage.impl;

import MQTTMessage.AbstractMess;

import java.util.Arrays;

/**
 * Created by admin on 2020/2/20.
 */
public class UnsubackMessage extends AbstractMess {


    public UnsubackMessage(byte[] bytes){
        uBytes = bytes;
    }

    @Override
    public boolean analysisMess() {
        if(super.analysisMess()){
            int sign = 1 + remainLength.length;
            mess_identify = Arrays.copyOfRange(uBytes, sign, uBytes.length);
            variableHeader = mess_identify;
        }
        return true;
    }
}
