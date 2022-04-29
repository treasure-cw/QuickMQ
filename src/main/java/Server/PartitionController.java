package Server;

import Common.Net.Server.PartitionFollowerAndLeaderConnectPool;

import java.net.ServerSocket;

public class PartitionController implements Partition{
    private final String topic;
    private final Integer partition;
    private Boolean isLeader;
    private Integer lastEndOffset;
    private Integer highWater;
    private ServerSocket serverSocket;
    private PartitionFollowerAndLeaderConnectPool partitionFollowerConnectPool;

    public PartitionController(String topic, Integer partition, Boolean isLeader, Integer lastEndOffset, Integer highWater, ServerSocket serverSocket, PartitionFollowerAndLeaderConnectPool partitionFollowerConnectPool) {
        this.topic = topic;
        this.partition = partition;
        this.isLeader = isLeader;
        this.lastEndOffset = lastEndOffset;
        this.highWater = highWater;
        this.serverSocket = serverSocket;
        this.partitionFollowerConnectPool = partitionFollowerConnectPool;
    }

    public PartitionController(String topic, Integer index, Boolean isLeader, ServerSocket serverSocket, PartitionFollowerAndLeaderConnectPool partitionFollowerConnectPool) {
        this(topic, index, isLeader, 0, 0, serverSocket, partitionFollowerConnectPool);
    }

    public String getTopic() {
        return topic;
    }

    public Integer getPartition() {
        return partition;
    }

    public Boolean getLeader() {
        return isLeader;
    }

    public Integer getLastEndOffset() {
        return lastEndOffset;
    }

    public Integer getHighWater() {
        return highWater;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public PartitionFollowerAndLeaderConnectPool getPartitionFollowerConnectPool() {
        return partitionFollowerConnectPool;
    }

    public void setPartitionFollowerConnectPool(PartitionFollowerAndLeaderConnectPool partitionFollowerConnectPool) {
        this.partitionFollowerConnectPool = partitionFollowerConnectPool;
    }

    public void setLeader(Boolean leader) {
        isLeader = leader;
    }

    public synchronized void setLastEndOffset(Integer lastEndOffset) {
        this.lastEndOffset = lastEndOffset;
    }

    public void setHighWater(Integer highWater) {
        this.highWater = highWater;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public synchronized void addLastEndOffset(){
        this.setLastEndOffset(this.getLastEndOffset() + 1);
    }

    public synchronized void manageHighWater(Integer lastEndOffset){
        this.setHighWater(Math.max(this.getHighWater(), lastEndOffset));
    }
    @Override
    public int getHighWaterFromRedis() {
        return 0;
    }
}
