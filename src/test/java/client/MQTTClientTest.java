package client;

import org.junit.Test;

import java.util.UUID;

/**
 * Created by admin on 2020/2/27.
 */
public class MQTTClientTest {

    @Test
    public void runClient(){
        ClientInformation ci = new ClientInformation();
        ci.setId(UUID.randomUUID().toString());
        ci.setUserName("hello");
        ci.setAddr("192.168.1.8:1883");
        ci.setPassword(null);
        ci.setWillRetain(false);
        ci.setWillQos(0);
        ci.setWillTopic(null);
        ci.setWillMessage(null);
        ci.setCleanSession(false);
        ci.setKeepAlive(10);
        ci.setAutoConn(true);
        MQTTClient client = new MQTTClient(ci);
        TopicInformation ti = new TopicInformation();
        ti.setTopicName("hello/hello/hello");
        ti.setTpoicType(TopicInformation.TOPICTYPE.PUBLISH);
        client.getClient().addTopic(ti);
        Message mes = new Message("hello");
        client.publish(ti, mes);
        while (true){

        }
    }

    @Test
    public void updateClient(){
        ClientInformation ci = new ClientInformation();
        ci.setName("hello");
        MQTTClient client = new MQTTClient(ci);
        ClientInformation clientInformation = new ClientInformation();
        clientInformation.setName("123456");
        client.setClient(clientInformation);
    }
}
