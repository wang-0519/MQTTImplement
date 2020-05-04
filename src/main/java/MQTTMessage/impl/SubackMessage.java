package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.Translater;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by admin on 2020/2/20.
 */
public class SubackMessage extends AbstractMess {

    private ArrayList<Integer> topicsIsSub = new ArrayList();

    public SubackMessage(byte[] bytes){
        uBytes = bytes;
        topicsIsSub = new ArrayList<>();
    }

    @Override
    public boolean analysisMess() {
        if(super.analysisMess()) {
            int sign = 1 + remainLength.length;
            messIdentify = Arrays.copyOfRange(uBytes, sign, sign + 2);
            variableHeader = messIdentify;
            sign += 2;

            packageValue = Arrays.copyOfRange(uBytes, sign, uBytes.length);
            try {
                while (sign < uBytes.length) {
                    topicsIsSub.add(Translater.binToInt(uBytes[sign++]));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public ArrayList<Integer> getTopicsIsSub() {
        return topicsIsSub;
    }
}
