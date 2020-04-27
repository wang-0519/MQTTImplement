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
        topic1.setTopicName("123");
        topic1.setQos(0);
        topic2.setTopicName("hello/123/123");
        topic2.setQos(0);
        System.out.println(topic1.isTopicFilter(topic2.getTopicName()));
    }
}
