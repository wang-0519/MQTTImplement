package MQTTMessage;

import MQTTMessage.impl.ConnMessage;
import client.ClientInformation;
import org.junit.Test;

/**
 * Created by admin on 2020/2/13.
 */
public class ConnMessageTest {

    /**
     * 连接报文测试
     * 获取连接报文
     */
    @Test
    public void connMessTest(){
        ClientInformation ci = new ClientInformation();
        ci.setId("123");
        ci.setUserName("hello");
        ci.setPassword(null);
        ci.setWillRetain(false);
        ci.setWillQos(0);
        ci.setWillTopic(null);
        ci.setWillMessage(null);
        ci.setCleanSession(false);
        ci.setKeepAlive(3000);
        AbstractMess connMess = new ConnMessage(ci.getId(), ci.getUserName(), ci.getPassword(),ci.isWillRetain(), ci.getWillQos(),
                ci.getWillTopic(), ci.getWillMessage(), ci.isCleanSession(), ci.getKeepAlive());
        byte[] mess = connMess.getUBytes();
        for(int i = 0; i < mess.length; i++){
            System.out.print((int)mess[i] + " ");
        }
        byte[] bytes = {16, 22, 0, 4, 77, 81, 84, 84, 4, -128, 117, 48, 0, 3, 49, 50, 51, 0, 5, 104, 101, 108, 108, 111};
    }
}
