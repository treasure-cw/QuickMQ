package Version1;

import Clients.Consumer.Consumer;
import Clients.Consumer.ConsumerRecord;
import Clients.Consumer.QuickMQConsumer;
import Common.ConfigMaker;
import Common.OffSet;

import java.util.Properties;

public class ConsumerTest {
    public static void main(String[] args) {
        Properties properties=new Properties();
        properties.put("IP","127.0.0.1");
        properties.put("PORT",1234);
        properties.put("TOPIC","WANG");
        ConfigMaker configMaker=new ConfigMaker(properties);
        Consumer consumer = new QuickMQConsumer(configMaker);
        OffSet offSet =new OffSet("TOPIC", 0);
        ConsumerRecord<String, String> consumerRecord = consumer.pull(offSet);
        System.out.println(consumerRecord.toString());
        ConsumerRecord<String, String> consumerRecord1 = consumer.pull(offSet);
        System.out.println(consumerRecord1.toString());
    }
}
