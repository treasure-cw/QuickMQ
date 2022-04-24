package Common;

import Common.Net.Client.TopicSocketConnectPool;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * a class of config maker that build basic config for client to start <br>
 * can build basic config like mate data and socket pool for client(producer or consumer) <br>
 * @author wangchen
 * @version 1.0.0
 */
public class ConfigMaker {
    private final String serverIP;
    private final Integer serverPort;
    private final String topic;

    /**
     * constructor function with pros
     * @param properties the basic information to build client identity(producer or consumer)
     */
    public ConfigMaker(@NotNull Properties properties) {
        this.serverIP = (String) properties.get("IP");
        this.serverPort = (Integer) properties.get("PORT");
        this.topic = (String) properties.get("TOPIC");
    }

    /**
     * function to get basic mate data
     */
    public MateData ConfigGetMataData(){
        // build a list of IP for definite topic
        Map<String, List<String>> topicPartitionIPList;
        topicPartitionIPList = new HashMap<>();
        List<String> list = new ArrayList<String>(){{add(getServerIP());}};
        topicPartitionIPList.put(this.getTopic(), list);

        // build a list of port for definite topic
        Map<String, List<Integer>> topicPartitionHostList;
        topicPartitionHostList = new HashMap<>();
        List<Integer> list1 =new ArrayList<Integer>(){{add(getServerPort());}};
        topicPartitionHostList.put(this.getTopic(),list1);

        // build a list of reflection from index to partition
        Map<String, List<Integer>> topicPartitionList;
        topicPartitionList = new HashMap<>();
        List<Integer> list2 = new ArrayList<Integer>(){{add(0);}};
        topicPartitionList.put(this.getTopic(), list2);

        // build a list that can judge if the partition is alive
        Map<String, List<Boolean>> topicPartitionAliveList;
        topicPartitionAliveList = new HashMap<>();
        List<Boolean> list3 = new ArrayList<Boolean>(){{add(true);}};
        topicPartitionAliveList.put(this.getTopic(), list3);

        //build a new mate date with above information and return it
        return new MateData(topicPartitionIPList,topicPartitionHostList,topicPartitionList,topicPartitionAliveList);
    }

    /**
     * function to get basic socket pool
     */
    public TopicSocketConnectPool ConfigGetSocketConnectPool(){
        //build a new socket pool with basic mate data and return it
        return new TopicSocketConnectPool(topic, this.ConfigGetMataData());
    }

    /**
     * IP getter
     * @return String data means IP
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * port getter
     * @return Integer data means the number of port
     */
    public Integer getServerPort() {
        return serverPort;
    }

    /**
     * topic getter
     * @return String data means the name of topic
     */
    public String getTopic() {
        return topic;
    }
}
