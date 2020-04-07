package MQTTMessage;

import MQTTMessage.impl.SubscribeMessage;
import client.TopicInformation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2020/2/16.
 */
public class SubcribeMessageTest {

    @Test
    public void getSubcribeMess(){
        ArrayList<TopicInformation> topics = new ArrayList<>();
        TopicInformation topic1 = new TopicInformation();
        topic1.setTopicName("hello");
        topic1.setTpoicType(TopicInformation.TOPICTYPE.PUBLISH);
        topic1.setQos(0);
        topics.add(topic1);

        TopicInformation topic2 = new TopicInformation();
        topic2.setTopicName("hello");
        topic2.setTpoicType(TopicInformation.TOPICTYPE.SUBSCRIBE);
        topic2.setQos(0);
        topics.add(topic2);

        AbstractMess subMess = new SubscribeMessage(topics);
        byte[] mess = subMess.getUBytes();
        for(int i = 0; i < mess.length; i++){
            System.out.print((int)mess[i] + " ");
        }
    }
}
