package Common;

import java.util.List;
import java.util.Map;

/**
 * a class of mate data that contents basic information of broker group <br>
 * can build a new mate data identity <br>
 * @author wangchen
 * @version 1.0.0
 */
public class MateData {
    private final Map<String, List<String>> topicPartitionIPList;
    private final Map<String, List<Integer>> topicPartitionPortList;
    private final Map<String, List<Integer>> topicPartitionList;
    private final Map<String, List<Boolean>> topicPartitionAliveList;
    private final Boolean isSendAlready;

    /**
     *  constructor function with basic information
     * @param topicPartitionIPList a list of IP for definite topic
     * @param topicPartitionPortList a list of port for definite topic
     * @param topicPartitionList a list of reflection from index to partition
     * @param topicPartitionAliveList a list that can judge if the partition is alive
     * @param isSendAlready if the information from producer is sent to partition,true for yes false for no
     */
    public MateData(Map<String, List<String>> topicPartitionIPList,
                    Map<String, List<Integer>> topicPartitionPortList,
                    Map<String, List<Integer>> topicPartitionList,
                    Map<String, List<Boolean>> topicPartitionAliveList,
                    Boolean isSendAlready) {
        this.topicPartitionIPList = topicPartitionIPList;
        this.topicPartitionPortList = topicPartitionPortList;
        this.topicPartitionList = topicPartitionList;
        this.topicPartitionAliveList = topicPartitionAliveList;
        this.isSendAlready = isSendAlready;
    }

    /**
     * constructor function without isSendAlready
     * @param topicPartitionIPList a list of IP for definite topic
     * @param topicPartitionPortList a list of port for definite topic
     * @param topicPartitionList a list of reflection from index to partition
     * @param topicPartitionAliveList a list that can judge if the partition is alive
     */
    public MateData(Map<String, List<String>> topicPartitionIPList,
                    Map<String, List<Integer>> topicPartitionPortList,
                    Map<String, List<Integer>> topicPartitionList,
                    Map<String, List<Boolean>> topicPartitionAliveList){
        this(topicPartitionIPList, topicPartitionPortList, topicPartitionList, topicPartitionAliveList, true);
    }

    /**
     * IP list getter
     * @return Map data with key(String)-value(List 'String') means IP list
     */
    public Map<String, List<String>> getTopicPartitionIPList() {
        return topicPartitionIPList;
    }

    /**
     * port list getter
     * @return Map data with key(String)-value(List 'Integer') means port list
     */
    public Map<String, List<Integer>> getTopicPartitionPortList() {
        return topicPartitionPortList;
    }

    /**
     * alive list getter
     * @return Map data with key(String)-value(List 'Boolean') means alive list
     */
    public Map<String, List<Boolean>> getTopicPartitionAliveList() {
        return topicPartitionAliveList;
    }

    /**
     * reflection of index to partition list getter
     * @return Map data with key(String)-value(List 'Integer') means reflection of index to partition list
     */
    public Map<String, List<Integer>> getTopicPartitionList() {
        return topicPartitionList;
    }

    /**
     * isSendAlready getter
     * @return Boolean data means the clue of isSendAlready ture for yes,false for no
     */
    public Boolean getSendAlready() {
        return isSendAlready;
    }
}
