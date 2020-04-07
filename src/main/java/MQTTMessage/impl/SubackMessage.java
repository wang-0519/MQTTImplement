package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import client.TopicInformation;
import com.sun.org.apache.xpath.internal.operations.Bool;
import helperClass.Translater;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

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
            mess_identify = Arrays.copyOfRange(uBytes, sign, sign + 2);
            sign += 2;

            int i = 0;
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
