package MQTTMessage;

import MQTTMessage.impl.PubackMessage;
import helperClass.PackageIdentify;
import org.junit.Test;

/**
 * Created by admin on 2020/2/16.
 */
public class PubackMessageTest {

    /**
     * 获取二进制Puback报文
     */
    @Test
    public void getPublackMess(){
        AbstractMess pubackMess = new PubackMessage(PackageIdentify.getIdentify());
        byte[] mess = pubackMess.getUBytes();
        for(int i = 0; i < mess.length; i++){
            System.out.print((int)mess[i] + " ");
        }
        System.out.println();

        AbstractMess pubAck = new PubackMessage(mess, 1);
        pubAck.analysisMess();
        for(int i = 0; i < pubAck.getMessIdentify().length; i++){
            System.out.print((int)pubAck.getMessIdentify()[i] + " ");
        }
    }
}
