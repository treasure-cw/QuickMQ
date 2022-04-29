package Version1;

import Clients.Producer.Producer;
import Clients.Producer.ProducerRecord;
import Clients.Producer.QuickMQProducer;
import Common.ConfigMaker;

import java.util.Properties;

public class ProducerTest {
    public static void main(String[] args) throws InterruptedException {
        Properties properties=new Properties();
        properties.put("IP","127.0.0.1");
        properties.put("PORT",1234);
        properties.put("TOPIC","Test");
        ConfigMaker configMaker=new ConfigMaker(properties);
        Producer producer=new QuickMQProducer(configMaker);
        ProducerRecord<String,String> producerRecord= new ProducerRecord<String,String>("Test", 0, "TestKey", "Hello QuickMQ", 0L);
        producer.send(producerRecord);
        producer.send(producerRecord);
        producer.send(producerRecord);
        Thread.sleep(5000);
        producer.send(producerRecord);
    }
}
