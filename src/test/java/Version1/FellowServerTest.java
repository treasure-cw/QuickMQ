package Version1;

import Common.Net.Server.PartitionAcceptSocketConnect;
import Server.PartitionController;

import java.io.IOException;
import java.net.ServerSocket;

public class FellowServerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(4321);
        PartitionController partition = new PartitionController("Test",0,false,1,1,serverSocket,null);
        PartitionAcceptSocketConnect partitionAcceptSocketConnect = new PartitionAcceptSocketConnect(partition);
        partitionAcceptSocketConnect.startServer();
    }
}
