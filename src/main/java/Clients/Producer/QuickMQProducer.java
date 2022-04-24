package Clients.Producer;

import Common.CommonFunction;
import Common.ConfigMaker;
import Common.MateData;
import Common.Net.Client.TopicSocketConnectPool;
import org.jetbrains.annotations.NotNull;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Future;

/**
 * a class of true QuickMQ producer which inherits form interface producer <br>
 * can build a new QuickMQ producer entity in some ways <br>
 * can push information from server(broker-topic-partition) <br>
 * @author wangchen
 * @version 1.0.0
 */
public class QuickMQProducer implements Producer{
    private MateData mateData;
    private TopicSocketConnectPool topicSocketConnectPool;

    /**
     * constructor function with mate data and socket Pool
     * @param mateData mate date（the basic information of the chosen partition in broker）
     * @param topicSocketConnectPool  socket connection pool which producer connect partitions(brokers)
     */
    public QuickMQProducer(MateData mateData, TopicSocketConnectPool topicSocketConnectPool) {
        this.mateData = mateData;
        this.topicSocketConnectPool = topicSocketConnectPool;
    }

    /**
     * constructor function with config maker
     * @param configMaker config for producer which is built by pros or without mate data and socket pool
     */
    public QuickMQProducer(@NotNull ConfigMaker configMaker){
        this(configMaker.ConfigGetMataData(), configMaker.ConfigGetSocketConnectPool());
    }

    /**
     * mate data setter
     */
    public void setMateData(MateData mateData) {
        this.mateData = mateData;
    }

    /**
     * socket pool setter
     */
    public void setTopicSocketConnectPool(TopicSocketConnectPool topicSocketConnectPool) {
        this.topicSocketConnectPool = topicSocketConnectPool;
    }

    /**
     * function to send producer record to topic
     * @param producerRecord the producer record which will push to topic
     * @return future<MateData>
     */
    @Override
    public Future<MateData> send(@NotNull ProducerRecord producerRecord) {
        // the partition which producer push to
        int partition;
        // producerRecord has the definite partition
        if(producerRecord.getPartition() != null){
            partition = producerRecord.getPartition();
        }
        // without definite partition
        else{
            // producerRecord has the definite key
            if(producerRecord.getKey() != null){
                //get partition by hashing its own key
                partition = producerRecord.getKey().hashCode() % mateData.getTopicPartitionIPList().get(producerRecord.getTopic()).size();
            }
            // without definite key
            else{
                // get partition by hashing its total record
                partition = producerRecord.hashCode() % mateData.getTopicPartitionIPList().get(producerRecord.getTopic()).size();
            }
        }

        // if the partition is disabled,throw new exception and warn users
        if(!mateData.getTopicPartitionAliveList().get(producerRecord.getTopic()).get(partition)){
            throw new IllegalArgumentException(
                    String.format("分区不可用：分区：%d，IP：%s 暂时不可用，请检查",
                            partition, mateData.getTopicPartitionIPList().get(producerRecord.getTopic()).get(partition)));
        }
        // try statement
        try {
            // get producer socket client form socket pool by definite partition
            Socket producerClient = this.topicSocketConnectPool.getPartitionSockets().get(partition);

            // build the output steam(ObjectOutputSteam)
            ObjectOutputStream outputStream = new ObjectOutputStream(producerClient.getOutputStream());

            // get push tag from common function area
            Integer integer = CommonFunction.getPushProducerRecordQuery();

            // push tag
            outputStream.writeObject(integer);
            // push producer record
            outputStream.writeObject(producerRecord);

            outputStream.flush();
        }
        // catch statement
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
