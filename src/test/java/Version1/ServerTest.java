package Version1;

import Common.Net.Server.PartitionAcceptSocketConnect;
import Server.Partition;
import Server.PartitionController;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerTest {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        PartitionController partition = new PartitionController("TOPIC",0,true,1,1,serverSocket,null);
        PartitionAcceptSocketConnect partitionAcceptSocketConnect = new PartitionAcceptSocketConnect(partition);
        partitionAcceptSocketConnect.startServer();
    }
}
