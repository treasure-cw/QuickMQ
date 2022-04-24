package Clients.Producer;

import java.io.Serializable;

/**
 * a class of producer record <br>
 * can build a new producer record entity record in some ways <br>
 * @author wangchen
 * @version 1.0.0
 */
public class ProducerRecord<K, V> implements Serializable {
    // definite serializable ID
    private static final long serializableUID = 12345678L;

    private final String topic;
    private final Integer partition;
    private final K key;
    private final V value;
    private final Long sequence;

    /**
     * definite partition,key,sequence <br>
     * constructor function with topic,partition,key,value,sequence
     * @param topic the definite topic which producer push information to
     * @param partition the definite partition which producer push to in topic
     * @param value the producer record's definite value(the basic information)
     * @param key the producer record's definite key
     * @param sequence the sequence of one producer record
     */
    public ProducerRecord(String topic, Integer partition, K key, V value, Long sequence){
        if(topic == null){
            throw new IllegalArgumentException("主题不合法：主题不能为空");
        }
        if(partition != null && partition < 0){
            throw new IllegalArgumentException("分区不合法：分区不能小于0");
        }
        if(sequence != null && sequence < 0){
            throw new IllegalArgumentException("次序不合法：次序不能小于0");
        }
        this.topic = topic;
        this.partition = partition;
        this.key = key;
        this.value = value;
        this.sequence = sequence;
    }
    /**
     * definite partition
     */
    public ProducerRecord(String topic, Integer partition, K key, V value){
        this(topic, partition, key, value, null);
    }
    public ProducerRecord(String topic, Integer partition, V value, Long sequence){
        this(topic, partition, null, value, sequence);
    }
    public ProducerRecord(String topic, Integer partition, V value){
        this(topic, partition, null, value, null);
    }
    /**
     * without definite partition
     */
    public ProducerRecord(String topic, K key, V value, Long sequence){
        this(topic, null, key, value, sequence);
    }
    public ProducerRecord(String topic, K key, V value){
        this(topic, null, key, value, null);
    }
    public ProducerRecord(String topic, V value, Long sequence){
        this(topic, null, null, value, sequence);
    }
    public ProducerRecord(String topic, V value){
        this(topic, null, null, value, null);
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
     * the function of getting total producer record hashcode which is used to get partition
     * @return int data means this entity's hashcode
     */
    @Override
    public int hashCode() {
        int result = topic != null ? topic.hashCode() : 0;
        result = 31 * result + (partition != null ? partition.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        return result;
    }

    /**
     * the function of getting producer record's String information
     * @return String data means this entity's information
     */
    @Override
    public String toString() {
        return "ProducerRecord{" +
                "topic='" + topic + '\'' +
                ", partition=" + partition +
                ", key=" + key +
                ", value=" + value +
                ", sequence=" + sequence +
                '}';
    }
}
