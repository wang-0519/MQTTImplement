package MQTTMessage.impl;

import MQTTMessage.AbstractMess;
import helperClass.BytesHandler;
import helperClass.Translater;

/**
 * 请求连接报文
 * Created by admin on 2020/2/4.
 */
public class ConnMessage extends AbstractMess {

    //客户端id，唯一标识
    private String client_id = null;


    public ConnMessage(String client_id){
        this.client_id = client_id;
    }

    /**
     *
     * @param client_id
     * @param userName
     * @param password
     * @param willRetain
     * @param willQoS
     * @param willTopic   遗嘱话题
     * @param willMessage   遗嘱消息  此时遗嘱标志为1
     * @param cleanSession
     * @param keepAlive
     */
    public ConnMessage(String client_id, String userName, String password, boolean willRetain, int willQoS, String willTopic, String willMessage, boolean cleanSession, int keepAlive){
        mess.put("clientId", client_id);
        mess.put("willRetain", String.valueOf(willRetain));
        mess.put("willQos", String.valueOf(willQoS));
        mess.put("cleanSession", String.valueOf(cleanSession));
        mess.put("keepAlive", String.valueOf(keepAlive));

        this.client_id = client_id;
        boolean signOfName = false, signOfPass = false, willFlag = false;
        if(userName != null){
            signOfName = true;
            mess.put("userName",userName);
        }
        if(password != null){
            signOfPass = true;
            mess.put("password",password);
        }
        if(willTopic != null){
            willFlag = true;
            mess.put("willTopic",willTopic);
            mess.put("willMessage",willMessage);
        }
        int k = editPackage(signOfName, signOfPass, willFlag);
        editVariableHeader(signOfName, signOfPass, willRetain, willQoS, willFlag, cleanSession, keepAlive);
        mess.put("remainLen", "" + (k+10));
        editFixedHeader(k + 10);
        super.editUBytes();
    }

    /**
     * 编辑固定报头，由于需要确定剩余长度，应当在
     * editVariableHeader
     * editPackage之后调用
     */
    private void editFixedHeader(int remainLen){
        typeOfMess = BytesHandler.or(typeOfMess,"00010000");
        remainLength = BytesHandler.editRemainLen(remainLen);
        fixedHeader = BytesHandler.connAll(typeOfMess , remainLength);
    }


    /**
     * 编辑可变报头
     * 传入连接标志位
     * @param userName
     * @param password
     * @param willRetain
     * @param willQoS
     * @param willFlag
     * @param cleanSession
     * @param keepAlive
     */
    private void editVariableHeader(boolean userName, boolean password, boolean willRetain, int willQoS, boolean willFlag, boolean cleanSession, int keepAlive){
        try{
            byte[] length = Translater.intToBin(4,16);
            byte[] MQTTSign = Translater.strToBin("MQTT");
            byte[] level = Translater.intToBin(4,8);

            byte[] signOfConn = new byte[1];
            if(userName){
                signOfConn = BytesHandler.or(signOfConn,"10000000");
            }
            if(password){
                signOfConn = BytesHandler.or(signOfConn, "01000000");
            }
            if(willFlag){
                signOfConn = BytesHandler.or(signOfConn, "00000100");
                if(willRetain){
                    signOfConn = BytesHandler.or(signOfConn, "00100000");
                }
                if(willQoS == 1){
                    signOfConn = BytesHandler.or(signOfConn, "00001000");
                } else if(willQoS == 2){
                    signOfConn = BytesHandler.or(signOfConn, "00010000");
                }
            }
            if(cleanSession){
                signOfConn = BytesHandler.or(signOfConn, "00000010");
            }

            byte[] keep = Translater.intToBin(keepAlive,16);

            variableHeader = BytesHandler.connAll(length, MQTTSign, level, signOfConn, keep);
        }catch (Exception e){
            System.out.print("ConnMessage.editVariableHeader==>");
            e.printStackTrace();
        }
    }

    /**
     * 编辑有效载荷
     * @param userName
     * @param password
     * @param willFlag
     */
    private int editPackage(boolean userName, boolean password, boolean willFlag){
        int count = 0;
        try{
            byte[] uClientId = BytesHandler.editStrWithLen(client_id);
            count += uClientId.length;

            byte[] uUserName = null, uPassword = null, uTopic = null, uMess = null;
            if(userName){
                uUserName = BytesHandler.editStrWithLen(mess.get("userName"));
                count += uUserName.length;
            }
            if (password) {
                uPassword = BytesHandler.editStrWithLen(mess.get("password"));
                count += uPassword.length;
            }
            if(willFlag){
                uTopic = BytesHandler.editStrWithLen(mess.get("willTopic"));
                count += uTopic.length;
                uMess = BytesHandler.editStrWithLen(mess.get("willMessage"));
                count += uMess.length;
            }

            packageValue = BytesHandler.connAll(uClientId, uTopic, uMess, uUserName, uPassword);
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
