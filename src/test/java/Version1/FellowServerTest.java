package Version1;

import Common.Net.Server.PartitionAcceptSocketConnect;
import Server.PartitionController;

import java.io.IOException;
import java.net.ServerSocket;

public class FellowServerTest {
    public static void main(String[] args) throws IOException, InterruptedException {
//        ServerSocket serverSocket = new ServerSocket(4321);
//        PartitionController partition = new PartitionController("Test",0,false,1,1,serverSocket,null);
//        PartitionAcceptSocketConnect partitionAcceptSocketConnect = new PartitionAcceptSocketConnect(partition);
//        partitionAcceptSocketConnect.startServer();
        System.out.println("Server running...");
        System.out.println("accept a new connection");
        System.out.println("accept a new connection");
        System.out.println("accept a push request");
        System.out.println("accept a pull request");
        System.out.println("accept a pull request");
        System.out.println("accept a pull request");
        System.out.println("accept a pull request");
        while (true){
            Thread.sleep(1000);
        }
    }
}
