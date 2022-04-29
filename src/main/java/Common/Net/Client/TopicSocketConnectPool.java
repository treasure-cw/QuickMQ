package Common.Net.Client;

import Common.MateData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * a class of socket pool for client which inherits form interface SocketConnectPool <br>
 * can build a new QuickMQ-Client socket pool entity <br>
 * can alter the content of QuickMQ-Client socket pool entity <br>
 * @author wangchen
 * @version 1.0.0
 */
public class TopicSocketConnectPool implements SocketConnectPool {
    private final String topic;
    private final Map<Integer, Socket> partitionSockets;

    /**
     * constructor function with definite topic and mate data
     * @param topic the socket pool of definite topic for client(producer or consumer)
     * @param mateData the mate data of broker group
     */
    public TopicSocketConnectPool(String topic, MateData mateData){
        this.topic=topic;
        this.partitionSockets = new HashMap<>();
        this.initSocketConnectPool(mateData);
    }

    /**
     * initialize the socket pool for client(producer or consumer) <br>
     * @param mateData the mate data of broker group
     */
    @Override
    public void initSocketConnectPool(@NotNull MateData mateData) {
        // get a list that can judge if the partition is alive
        List<Boolean> isAlive = mateData.getTopicPartitionAliveList().get(this.getTopic());
        // get a list of IP for definite topic
        List<String> ips = mateData.getTopicPartitionIPList().get(this.getTopic());
        // get a list of port for definite topic
        List<Integer> ports = mateData.getTopicPartitionPortList().get(this.getTopic());
        // get a list of reflection from index to partition
        List<Integer> partitions = mateData.getTopicPartitionList().get(this.getTopic());
        int index = 0;

        // build socket connection and put it into socket pool
        for(String IP : ips) {
            // partitions[index] (one partition) alive condition
            if (isAlive.get(index)) {
                // try statement
                try {
                    // build socket connection [IP:port]
                    Socket socketClint = new Socket(IP, ports.get(index));
                    // put it into socket pool
                    this.getPartitionSockets().put(partitions.get(index), socketClint);
                }
                // catch statement
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            index++;
        }
    }

    /**
     * alter the socket pool for client(producer or consumer)
     * @param mateData the mate data of broker group
     */
    @Override
    public void flushSocketConnectPool(@NotNull MateData mateData) {
        // initialize index,in loop area it will begin at zero
        int index = -1;
        // get a list of reflection from index to partition
        List<Integer> partitions = mateData.getTopicPartitionList().get(this.getTopic());


        for (Boolean isA : mateData.getTopicPartitionAliveList().get(this.getTopic())) {
            int correctTag = 0;
            index++;
            // partition alive condition
            if (isA) {
                // can't find socket in socket pool or this socket is disabled
                if (this.getPartitionSockets().get(partitions.get(index)) == null || !this.getPartitionSockets().get(partitions.get(index)).isConnected()) {
                    // remove this socket
                    this.getPartitionSockets().remove(partitions.get(index));
                    // try statement
                    try {
                        // build a new socket and put it into socket pool
                        this.getPartitionSockets().put(partitions.get(index),
                                new Socket(mateData.getTopicPartitionIPList().get(this.getTopic()).get(index),
                                        mateData.getTopicPartitionPortList().get(this.getTopic()).get(index))
                        );
                    }
                    // catch statement
                    catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        //TODO
                        System.out.println("TODO");
                    }
                }
            }
            // partition disabled condition
            else {
                // can find socket in socket pool, but it isn't closed
                if (this.getPartitionSockets().get(partitions.get(index)) != null && !this.getPartitionSockets().get(partitions.get(index)).isClosed()) {
                    // try statement
                    try {
                        // close this socket
                        this.getPartitionSockets().get(partitions.get(index)).close();
                        // successful tag
                        correctTag = 1;
                    }
                    // catch statement
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    // finally statement
                    finally {
                        // if closing is not successful
                        if (correctTag == 0) {
                            // remove this socket,keep service alive
                            this.getPartitionSockets().remove(partitions.get(index));
                        }
                    }
                }
            }
        }
    }

    /**
     * topic getter
     * @return String data means the name of topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * socket pool getter
     * @return Map data with key(integer)-value(socket) means socket pool
     */
    public Map<Integer, Socket> getPartitionSockets() {
        return partitionSockets;
    }
}
