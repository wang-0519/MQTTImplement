package MQTTMessage.impl;

import MQTTMessage.AbstractMess;

/**
 * 回应连接报文
 * Created by admin on 2020/2/13.
 */
public class ConnAckMessage extends AbstractMess{

    /**
     * 传入获取的ConnAck二进制报文
     * @param bytes
     */
    public ConnAckMessage(byte[] bytes){
        uBytes = bytes;
    }


    public boolean analysisMess() {
        super.analysisMess();
        if((int)(uBytes[3]) != 0){
            System.out.print("连接失败！");
            return false;
        }
        switch ((int)(uBytes[3])){
            case 0:
                return true;
            case 1:
                mess.put("errorMessage", "服务端不支持所请求的协议级别");
                break;
            case 2:
                mess.put("errorMessage", "客户端标识不合法");
                break;
            case 3:
                mess.put("errorMessage", "网络以连接，MQTT服务不可用");
                break;
            case 4:
                mess.put("errorMessage", "用户名或密码格式不正确");
                break;
            case 5:
                mess.put("errorMessage", "客户端未授权");
                break;
            default:
                mess.put("erroeMessage", "未知错误");
                break;
        }
        return false;
    }
}
