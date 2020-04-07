package MQTTMessage;

import MQTTMessage.impl.PubackMessage;
import MQTTMessage.impl.PublishMessage;
import org.junit.Test;

/**
 * Created by admin on 2020/2/15.
 */
public class PublishMessageTest {

    /**
     * 获取Publish二进制报文
     */
    @Test
    public void getPublishUbytes(){

        /**编辑二进制publish报文
         *  58 00111010
          */
        String topic = "hello";
        String mess = "hello";
        AbstractMess publish = new PublishMessage(1,false,topic,mess);
        byte[] ubytes = publish.getUBytes();
        for(int i = 0; i < ubytes.length; i++){
            System.out.print((int)ubytes[i] + " ");
        }

        //解析二进制报文
        AbstractMess publish2 = new PublishMessage(ubytes);
        if(publish2.analysisMess()){
            System.out.print("\n" + publish2.getOther_mess().toString());
        }
    }
}
