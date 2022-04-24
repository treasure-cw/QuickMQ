package Clients.Producer;

import Common.MateData;

import java.util.concurrent.Future;

/**
 * an interface of producer entity
 * @author wangchen
 * @version 1.0.0
 */
public interface Producer<K,V> {
    /**
     * function to send producer record to topic <br>
     * abstract function <br>
     * must be rewritten <br>
     * @param producerRecord the producer record which will push to topic
     * @return future<MateData>
     */
    Future<MateData> send(ProducerRecord<K, V> producerRecord);
}
