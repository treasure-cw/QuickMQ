package Common.Net.Server;

import Server.PartitionController;
import Server.PartitionExecuter;

import java.io.IOException;
import java.net.Socket;

/**
 * a class of socket pool for server <br>
 * can build multi threads for clients(producer or consumer) <br>
 * @author wangchen
 * @version 1.0.0
 */
public class PartitionAcceptSocketConnect {
    private PartitionController partitionController;

    /**
     * constructor function with partition controller
     * @param partitionController partition identity
     */
    public PartitionAcceptSocketConnect(PartitionController partitionController) {
        this.partitionController = partitionController;
    }

    /**
     * partition controller getter
     * @return class of partitionController data means this identity's partition controller
     */
    public PartitionController getPartitionController() {
        return partitionController;
    }

    /**
     * partition controller setter <br>
     * may to use <br>
     */
    public void setPartitionController(PartitionController partitionController) {
        this.partitionController = partitionController;
    }

    /**
     * a function to start QuickMQ Server <br>
     * it can accept new socket connection request and make new thead to resolve <br>
     */
    public void startServer() {
        // show server running tag in terminal
        System.out.println("*****QuickMQ Server is running now*****");

        // loop for main area
        // noinspection InfiniteLoopStatement
        while(true) {
            // try statement
            try {
                // accept new socket connection request
                Socket socket = this.getPartitionController().getServerSocket().accept();
                // make a new thread to resolve
                new Thread(new PartitionExecuter(socket, this.getPartitionController())).start();
            }
            // catch statement
            catch (IOException e){
                e.printStackTrace();
                // show the type of exception and the location of exception
                throw new IllegalStateException("" +
                        "QuickMQ server Exception: Accept area trouble,accept disabled or new thread exception " +
                        "from QuickMQ.Common.Server.PartitionAcceptSocketConnect");
            }
        }
    }
}
