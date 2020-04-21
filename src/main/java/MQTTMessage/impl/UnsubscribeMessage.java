package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import client.TopicInformation;
import helperClass.BytesHandler;
import helperClass.PackageIdentify;
import helperClass.Translater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by admin on 2020/2/17.
 */
public class UnsubscribeMessage extends AbstractMess{

    private ArrayList<TopicInformation> topics = null;

    public UnsubscribeMessage(ArrayList<TopicInformation> topics){
        try{
            mess_identify = PackageIdentify.getIdentify();
            this.topics = topics;
            int i = editPackage();
            editVariableHeader();
            this.mess.put("remainLen", "" + (i+2));
            editFixedHeader(i + 2);
        }catch(Exception e){
            e.printStackTrace();
        }
        super.editUBytes();
    }

    /**
     * 编辑固定报头
     * @param remainLen
     */
    private void editFixedHeader(int remainLen){
        typeOfMess = BytesHandler.or(typeOfMess, "10100010");
        remainLength = BytesHandler.editRemainLen(remainLen);
        fixedHeader = BytesHandler.connAll(typeOfMess,remainLength);
    }

    /**
     * 编辑可变报头
     */
    private void editVariableHeader(){
        variableHeader = mess_identify;
    }

    /**
     * 编辑有效载荷
     * @return
     */
    private int editPackage(){
        ArrayList<byte[]> arr = new ArrayList<>();
        try{
            for(int i = 0; i < topics.size(); i++){
                arr.add(BytesHandler.editStrWithLen(topics.get(i).getTopicName()));
                arr.add(Translater.intToBin((topics.get(i).getQos()),8));
            }
            for(int i = 0; i < arr.size(); i++){
                packageValue = BytesHandler.connAll(packageValue,arr.get(i));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return packageValue.length;
    }

    public ArrayList<TopicInformation> getTopics() {
        return topics;
    }
}
