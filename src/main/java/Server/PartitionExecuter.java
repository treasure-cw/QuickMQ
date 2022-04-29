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

    public void renewHighWater(int a){

    }
    /**
     * function to receive request and resolve it
     */
    @Override
    public void run() {
        // get base path
        String basePath = CommonFunction.getBasicFilePath();
        // get base file type
        String baseType = CommonFunction.getTextFileType();
        // build path
        String path = basePath
                + this.getPartitionController().getTopic()
                + "_"
                + this.getPartitionController().getPartition().toString()
                + baseType;
        //try statement
        try {
            // get inputSteam from socket
            InputStream inputStream = this.getSocket().getInputStream();
            // get outputSteam from socket
            OutputStream outputStream = this.getSocket().getOutputStream();

            // get fileInputSteam from SteamController by path
            FileInputStream fileInputStream = SteamController.getFileInputByPath(path);
            // get fileOutputSteam from SteamController by path
            FileOutputStream fileOutputStream = SteamController.getFileOutputByPath(path);

            //read from stream
            //write to file
            ObjectInputStream objectInputStreamFromFile = SteamController.getObjectInputByFileInputSteam(fileInputStream);
            ObjectInputStream objectInputStreamFromSteam = new ObjectInputStream(inputStream);
            //read from file
            //write to steam
            ObjectOutputStream objectOutputStreamToFile = SteamController.getObjectOutputByFileOutputSteam(fileOutputStream);
            ObjectOutputStream objectOutputStreamToSteam = new ObjectOutputStream(outputStream);

            //noinspection InfiniteLoopStatement
            while(true) {
                // request type tag
                Integer tagNum;
                while (true) {
                    try {
                        // get tag
                        tagNum = (Integer) objectInputStreamFromSteam.readObject();
                        break;
                    } catch (Exception e) {
                        Thread.sleep(1000);
                    }
                }
                // push query type;
                if (Objects.equals(CommonFunction.getPushProducerRecordQuery(), tagNum)) {
                    // read record from IO
                    ProducerRecord record = (ProducerRecord) objectInputStreamFromSteam.readObject();
                    // write it into file
                    objectOutputStreamToFile.writeObject(record);
                    // add partition's offset
                    this.getPartitionController().addLastEndOffset();

                    // this partition is leader
                    if (this.getPartitionController().getLeader()){
                        // write record to all fellows
                        for(Socket socket : this.getPartitionController().getPartitionFollowerConnectPool().getFollowerSocket()){
                            ObjectOutputStream o = SteamController.getObjectOutputSteamByOutputSteam(socket.getOutputStream());
                            o.writeObject(CommonFunction.getPushProducerRecordQuery());
                            o.writeObject(record);
                        }
                    }
                    // this partition is fellow
                    else{
                        // write ack to leader
                        ObjectOutputStream o = SteamController.getObjectOutputSteamByOutputSteam(
                                this.getPartitionController().getPartitionFollowerConnectPool().getLeaderSocket().getOutputStream());
                        o.writeObject(CommonFunction.getFollowerAckQuery());
                        o.writeObject(this.getPartitionController().getLastEndOffset());
                    }
                }
                //pull query type
                else if (Objects.equals(CommonFunction.getPullConsumerRecordQuery(), tagNum)) {
                    // get offset
                    Integer offsetNum = (Integer) objectInputStreamFromSteam.readObject();
                    // this request is redis keep alive request
                    if(offsetNum == -1){
                        Integer aliveTag = 1;
                        // tell redis i'm alive
                        objectOutputStreamToSteam.writeObject(aliveTag);
                    }
                    // this request is pull request
                    else {
                        // if offset less than highWater
                        if (offsetNum < this.getPartitionController().getHighWater()) {
                            // read record from file
                            ProducerRecord record = (ProducerRecord) objectInputStreamFromFile.readObject();
                            // write record to consumer
                            objectOutputStreamToSteam.writeObject(record);
                        } else {
                            throw new IllegalStateException("QuickMQ Server Exception:The pull query's offset is bigger than server's HW QuickMQ.Server.PartitionReceiver.Exception from QuickMQ.Server.PartitionReceiver");
                        }
                    }
                }
                // ack type
                else if (Objects.equals(CommonFunction.getFollowerAckQuery(), tagNum)) {
                    Integer lastEndOffsetNum = (Integer) objectInputStreamFromSteam.readObject();
                    this.renewHighWater(lastEndOffsetNum);
                }
                //exception
                else {
                    throw new IllegalStateException("QuickMQ Server Exception:Can't recognize the type of query.Exception from QuickMQ.Server.PartitionReceiver");
                }
                // wait IO Steam
                while(inputStream.available()==0){
                    Thread.sleep(10);
                }
                // skip Object head
                if(inputStream.available()>0){
                    inputStream.skip(4);
                }
            }
        } catch (ClassNotFoundException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
