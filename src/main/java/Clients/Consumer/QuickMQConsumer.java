package Clients.Consumer;

import Clients.Producer.ProducerRecord;
import Common.*;
import Common.Net.Client.TopicSocketConnectPool;
import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * a class of true QuickMQ consumer which inherits form interface consumer <br>
 * can build a new QuickMQ consumer entity in some ways <br>
 * can pull information from server(broker-topic-partition) <br>
 * @author wangchen
 * @version 1.0.0
 */
public class QuickMQConsumer<K,V> implements Consumer{
    private final MateData mateData;
    private final TopicSocketConnectPool topicSocketConnectPool;

    /**
     * constructor function with mate data and socket Pool
     * @param mateData mate date（the basic information of the chosen partition in broker）
     * @param topicSocketConnectPool socket connection pool which consumer connects
     */
    public QuickMQConsumer(MateData mateData, TopicSocketConnectPool topicSocketConnectPool) {
        this.mateData = mateData;
        this.topicSocketConnectPool = topicSocketConnectPool;
    }

    /**
     * constructor function with config maker
     * @param  configMaker  config for consumer which is built by pros or without mate data and socket pool
     */
    public QuickMQConsumer(@NotNull ConfigMaker configMaker){
        this(configMaker.ConfigGetMataData(), configMaker.ConfigGetSocketConnectPool());
    }

    /**
     * mate data getter
     * @return class of mateData data
     */
    public MateData getMateData() {
        return mateData;
    }

    /**
     * socket pool getter
     * @return class of topicSocketConnectPool data
     */
    public TopicSocketConnectPool getTopicSocketConnectPool() {
        return topicSocketConnectPool;
    }

    /**
     * function to pull consumer record from topic
     * @param offSet the information to identify which topic and partition and offset the consumer wants to pull
     * @return class of consumerRecord means the information of this consumer pull
     */
    @Override
    public ConsumerRecord pull(@NotNull OffSet offSet) {
        // the partition which consumer pull from
        // get from offset
        Integer partition = offSet.getPartition();
        // try statement
        try {
            // get consumer socket client form socket pool by definite partition
            Socket consumerClient = this.getTopicSocketConnectPool().getPartitionSockets().get(partition);
            if(consumerClient == null){
                throw new IllegalStateException("");
            }
            // build or get input steam(ObjectInputSteam)
            ObjectInputStream objectInputStream = SteamController.getObjectInputSteamByInputSteam(consumerClient.getInputStream());
            if(objectInputStream == null){
                throw new IllegalStateException("");
            }
            // build the output steam(ObjectOutputSteam)
            ObjectOutputStream outputStream = new ObjectOutputStream(consumerClient.getOutputStream());
            // get pullTag from common function area
            Integer tag = CommonFunction.getPullConsumerRecordQuery();
            // get Offset's offset
            Integer off = offSet.getOffSet();

            // push pullTag
            outputStream.writeObject(tag);
            // push offset
            outputStream.writeObject(off);
            // pull producer record
            ProducerRecord producerRecord = (ProducerRecord) objectInputStream.readObject();

            //change to consumer record
            ConsumerRecord<K, V> consumerRecord = new ConsumerRecord<>(producerRecord);
            //return to user
            return consumerRecord;
        }
        // catch statement
        catch (Exception e){
            e.printStackTrace();
        }
        // if catch exception,return null
        return null;
    }
}
