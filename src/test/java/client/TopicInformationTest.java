package client;

import org.junit.Test;

/**
 * Created by admin on 2020/3/27.
 */
public class TopicInformationTest {

    @Test
    public void test(){
        TopicInformation topic1 = new TopicInformation();
        TopicInformation topic2 = new TopicInformation();
        topic1.setTopicName("hello");
        topic1.setQos(0);
        topic2.setTopicName("hello");
        topic2.setQos(0);

    }
}
