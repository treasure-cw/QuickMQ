import Common.ConfigMaker;
import Clients.Producer.Producer;
import Clients.Producer.ProducerRecord;
import Clients.Producer.QuickMQProducer;

import java.util.*;

public class MyTest {
    public static void main(String[] args) {
        Properties properties=new Properties();
        properties.put("IP","127.0.0.1");
        properties.put("PORT",1234);
        properties.put("TOPIC","WANG");
        ConfigMaker configMaker=new ConfigMaker(properties);
        Producer producer=new QuickMQProducer(configMaker);
        ProducerRecord<String,String> producerRecord= new ProducerRecord<>("WANG", 0, "TEST", "HELLO_WORLD", 0L);
        producer.send(producerRecord);
        producer.send(producerRecord);
        producer.send(producerRecord);
    }
}
