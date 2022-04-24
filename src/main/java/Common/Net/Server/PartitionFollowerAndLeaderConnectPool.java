package Common.Net.Server;

import Server.PartitionController;
import Server.TopicDataController;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PartitionFollowerAndLeaderConnectPool {
    private List<Socket> followerSocket;
    private Socket leaderSocket;

    public List<Socket> getFollowerSocket() {
        return followerSocket;
    }

    public void setFollowerSocket(List<Socket> followerSocket) {
        this.followerSocket = followerSocket;
    }

    public Socket getLeaderSocket() {
        return leaderSocket;
    }

    public void setLeaderSocket(Socket leaderSocket) {
        this.leaderSocket = leaderSocket;
    }

    private List<Socket> getFollowerConnection(Jedis redis, String topic, Integer partition) throws IOException {
        List<String> IPs = TopicDataController.getAllPartitionLAndF(redis, topic, partition);
        List<Socket> ret = new ArrayList<>();
        for(String IP : IPs){
            String[] totalIP = IP.split(":");
            ret.add(new Socket(totalIP[0], Integer.parseInt(totalIP[1])));
        }
        return ret;
    }

    public Socket getLeaderConnection(Jedis redis, String topic, Integer partition) throws IOException {
        String[] totalIP = TopicDataController.getPartitionLeaderIP(redis, topic, partition).split(":");
        return new Socket(totalIP[0], Integer.parseInt(totalIP[1]));
    }

    public PartitionFollowerAndLeaderConnectPool(Jedis redis, PartitionController partitionController) throws IOException {
        if(partitionController.getLeader()) {
            this.setFollowerSocket(this.getFollowerConnection(redis, partitionController.getTopic(), partitionController.getPartition()));
        }else{
            this.setLeaderSocket(this.getLeaderConnection(redis, partitionController.getTopic(), partitionController.getPartition()));
        }
    }

}
