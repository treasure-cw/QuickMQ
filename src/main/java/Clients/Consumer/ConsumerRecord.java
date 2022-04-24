package Clients.Consumer;

import Clients.Producer.ProducerRecord;
import org.jetbrains.annotations.NotNull;

/**
 * a class of consumer record <br>
 * can build a new consumer record entity record in some ways <br>
 * @author wangchen
 * @version 1.0.0
 */
public class ConsumerRecord<K, V> {
    private final String topic;
    private final Integer partition;
    private final K key;
    private final V value;
    private final Long sequence;

    /**
     * constructor function with topic,partition,key,value,sequence
     * @param topic the definite topic which consumer pull information from
     * @param partition the definite partition which consumer pull in topic
     * @param key the consumer record's definite key
     * @param value the consumer record's definite value(the basic information)
     * @param sequence the sequence of one consumer record
     */
    public ConsumerRecord(String topic, Integer partition, K key, V value, Long sequence) {
        this.topic = topic;
        this.partition = partition;
        this.key = key;
        this.value = value;
        this.sequence = sequence;
    }

    /**
     * constructor function with producer record which used in changing producer record to consumer record
     * @param producerRecord a producer record
     */
    public ConsumerRecord(@NotNull ProducerRecord<K, V> producerRecord){
        this(producerRecord.getTopic(),
                producerRecord.getPartition(),
                producerRecord.getKey(),
                producerRecord.getValue(),
                producerRecord.getSequence());
    }

    /**
     * a series getter function
     */
    public String getTopic() {
        return topic;
    }

    public Integer getPartition() {
        return partition;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public Long getSequence() {
        return sequence;
    }

    /**
     * the function of getting consumer record's String information
     * @return String data shows this entity's information
     */
    @Override
    public String toString() {
        return "ConsumerRecord{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", key=" + key +
                ", value=" + value +
                ", sequence=" + sequence +
                '}';
    }
}
