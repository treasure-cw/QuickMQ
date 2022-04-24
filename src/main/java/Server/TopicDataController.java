package Server;

import Common.CommonFunction;
import Common.MateData;
import Common.RedisOperation;
import redis.clients.jedis.Jedis;

import java.util.*;

public class TopicDataController {
    public static Set<String> getAllTopicName(Jedis redis){
        return RedisOperation.getSetValues(redis, CommonFunction.getAllTopicNames());
    }
    public static List<String> getTopicPartitionIP(Jedis redis, String topic){
        return RedisOperation.getListValues(redis, topic);
    }
    public static Map<String, List<String>> getAllTopicPartitionIP(Jedis redis){
        Set<String> topics = getAllTopicName(redis);
        Map<String, List<String>> ret = new HashMap<>();
        for(String topic : topics){
            ret.put(topic, getTopicPartitionIP(redis, topic));
        }
        return ret;
    }
    public static List<String> getAllPartitionLAndF(Jedis redis, String topic, Integer partition){
        return RedisOperation.getListValues(redis, topic + "_" + partition.toString() + "_" + "LAndF");
    }

    public static String getPartitionNumber(Jedis redis, String topic, String IP){
        return RedisOperation.getKey(redis, topic + "_" + IP);
    }

    public static String getPartitionLeaderIP(Jedis redis, String topic, Integer partition){
        return RedisOperation.getKey(redis, topic + "_" + partition.toString());
    }

    public static MateData getMateDataFromBrokers(Jedis redis){
        Map<String, List<String>> md = getAllTopicPartitionIP(redis);

        Map<String, List<String>> topicPartitionIPList = new HashMap<>();
        Map<String, List<Integer>> topicPartitionPortList = new HashMap<>();
        Map<String, List<Integer>> topicPartitionList = new HashMap<>();
        Map<String, List<Boolean>> topicPartitionAliveList = new HashMap<>();

        for(String topic : md.keySet()){
            List<String> IPs = md.get(topic);
            List<String> IPList = new ArrayList<>();
            List<Integer> PortList = new ArrayList<>();
            List<Integer> PartitionList = new ArrayList<>();
            List<Boolean> AliveList = new ArrayList<>();
            for(String IP : IPs){
                String[] totalIP = IP.split(":");
                IPList.add(totalIP[0]);
                PortList.add(Integer.valueOf(totalIP[1]));
                PartitionList.add(Integer.valueOf(getPartitionNumber(redis, topic, IP)));
                AliveList.add(true);
            }
            topicPartitionIPList.put(topic, IPList);
            topicPartitionPortList.put(topic, PortList);
            topicPartitionList.put(topic, PartitionList);
            topicPartitionAliveList.put(topic, AliveList);
        }

        return new MateData(topicPartitionIPList, topicPartitionPortList, topicPartitionList, topicPartitionAliveList);
    }
}
