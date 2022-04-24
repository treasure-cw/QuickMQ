import Clients.Producer.ProducerRecord;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MyTestForSocket {


    public static void main(String[] args){
        try {
            ServerSocket socketServer = new ServerSocket(1234);
            Socket socket = socketServer.accept();
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            while(true) {
                byte[] bytes = new byte[4];
               // socket.getInputStream().read(bytes);
               // socket.getInputStream();
//                System.out.println(toInt(bytes));
                System.out.println(socket.getInputStream().available());
                if(socket.getInputStream().available()<=0){
                    System.out.println("没有数据了");
                    break;
                }
                ProducerRecord record = (ProducerRecord) objectInputStream.readObject();
                //System.out.println(socket.isConnected());
                if(socket.getInputStream().available()>0)
                    socket.getInputStream().skip(4);
                System.out.println(record.toString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static int toInt(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }
}
