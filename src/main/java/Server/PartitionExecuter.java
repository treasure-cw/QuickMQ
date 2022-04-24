package Server;

import Clients.Producer.ProducerRecord;
import Common.CommonFunction;
import Common.SteamController;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class PartitionExecuter implements Runnable{
    private final Socket socket;
    private int abstractSize;
    private final PartitionController partitionController;

    public PartitionExecuter(Socket socket, PartitionController partitionController) {
        this.socket = socket;
        this.partitionController = partitionController;
    }

    public int getAbstractSize() {
        return abstractSize;
    }

    public void setAbstractSize(int abstractSize){
        this.abstractSize = abstractSize;
    }

    public void addAbstractSize(){
        this.setAbstractSize(this.getAbstractSize()+1);
    }

    public Socket getSocket() {
        return socket;
    }

    public PartitionController getPartitionController() {
        return partitionController;
    }

    @Override
    public void run() {
        String basePath = CommonFunction.getBasicFilePath();
        String baseType = CommonFunction.getTextFileType();
        String path = basePath
                + this.getPartitionController().getTopic()
                + "_"
                + this.getPartitionController().getPartition().toString()
                + baseType;
        try {
            InputStream inputStream = this.getSocket().getInputStream();
            OutputStream outputStream = this.getSocket().getOutputStream();

            FileInputStream fileInputStream = SteamController.getFileInputByPath(path);
            FileOutputStream fileOutputStream = SteamController.getFileOutputByPath(path);
            //read from stream
            //write to file
            ObjectInputStream objectInputStreamFromSteam = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStreamToFile = SteamController.getObjectOutputByFileOutputSteam(fileOutputStream);
            //read from file
            //write to steam
            ObjectInputStream objectInputStreamFromFile = SteamController.getObjectInputByFileInputSteam(fileInputStream);
            ObjectOutputStream objectOutputStreamToSteam = new ObjectOutputStream(outputStream);

            //noinspection InfiniteLoopStatement
            while(true) {
                Integer tagNum;
                while (true) {
                    try {
                        tagNum = (Integer) objectInputStreamFromSteam.readObject();
                        break;
                    } catch (Exception e) {
                        Thread.sleep(1000);
                    }
                }
                String info = Objects.equals(tagNum, CommonFunction.getPushProducerRecordQuery()) ? "producer push query" : "consumer pull query";
                System.out.println(info);
                //push query type;
                if (Objects.equals(CommonFunction.getPushProducerRecordQuery(), tagNum)) {
                    ProducerRecord record = (ProducerRecord) objectInputStreamFromSteam.readObject();
                    objectOutputStreamToFile.writeObject(record);
                    //add partition's offset
                    this.getPartitionController().addLastRndOffset();

                    if (this.getPartitionController().getLeader()){
                        //
                        for(Socket socket : this.getPartitionController().getPartitionFollowerConnectPool().getFollowerSocket()){
                            ObjectOutputStream o = SteamController.getObjectOutputSteamByOutputSteam(socket.getOutputStream());
                            o.writeObject(CommonFunction.getPushProducerRecordQuery());
                            o.writeObject(record);
                        }
                    } else{
                        //ack
                        ObjectOutputStream o = SteamController.getObjectOutputSteamByOutputSteam(
                                this.getPartitionController().getPartitionFollowerConnectPool().getLeaderSocket().getOutputStream());
                        o.writeObject(CommonFunction.getFollowerAckQuery());
                        o.writeObject(this.getPartitionController().getLastEndOffset());
                    }
//                    Jedis redis = null;
//                    for (int i = 0; i < 5; i++) {
//                        redis = RedisConnectPool.getConnection();
//                        if(redis != null){
//                            break;
//                        }
//                    }
//                    if(redis == null){
//                        throw new IllegalStateException("Redis Connection Exception:Can't get a redis connection.Exception from QuickMQ.Server.PartitionReceiver");
//
//                    }else {
//                        while(1 == RedisOperation.getClock(redis, "test")){
//                            this.addAbstractSize();
//                        }
//                        RedisOperation.releaseLock(redis, "test");
//                        RedisConnectPool.releaseConnection(redis);
//                    }
                }
                //pull query type
                else if (Objects.equals(CommonFunction.getPullConsumerRecordQuery(), tagNum)) {
                    Integer offsetNum = (Integer) objectInputStreamFromSteam.readObject();
                    if (offsetNum < this.getPartitionController().getHighWater()) {
                        ProducerRecord record = (ProducerRecord) objectInputStreamFromFile.readObject();
                        objectOutputStreamToSteam.writeObject(record);
                    } else {
                        throw new IllegalStateException("QuickMQ Server Exception:The pull query's offset is bigger than server's HW QuickMQ.Server.PartitionReceiver.Exception from QuickMQ.Server.PartitionReceiver");
                    }
                }
                else if (Objects.equals(CommonFunction.getFollowerAckQuery(), tagNum)) {
                    Integer lastEndOffsetNum = (Integer) objectInputStreamFromSteam.readObject();
                }
                //exception
                else {
                    throw new IllegalStateException("QuickMQ Server Exception:Can't recognize the type of query.Exception from QuickMQ.Server.PartitionReceiver");
                }
                while(inputStream.available()==0){
                    Thread.sleep(10);
                }
                if(inputStream.available()>0){
                    inputStream.skip(4);
                }
            }
        } catch (ClassNotFoundException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
