package MQTTMessage;

import MQTTMessage.impl.DisconnectMessage;
import MQTTMessage.impl.PingreqMessage;
import org.junit.Test;

/**
 * Created by admin on 2020/2/17.
 */
public class PingreqTest {

    @Test
    public void getPingreqMess(){
        AbstractMess pingMess = new PingreqMessage();
        byte[] mess = pingMess.getUBytes();
        for(int i = 0; i < mess.length; i++){
            System.out.print((int)mess[i] + " ");
        }
        System.out.println();
        System.out.println(pingMess.getClass().toString());
        if(pingMess.getClass() == PingreqMessage.class){
            pingMess = new DisconnectMessage();
            System.out.println(pingMess.getClass().toString());
        }


        //-64   1 1000000   1 0111111  1 1000000
    }
}
