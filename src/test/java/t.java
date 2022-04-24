import Clients.Producer.ProducerRecord;
import Common.CommonFunction;

import java.io.*;
import java.net.Socket;

public class t {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream =new FileInputStream("D:\\QuickMQ\\src\\main\\java\\Data\\TOPIC_0.txt");
        //FileOutputStream fileOutputStream =new FileOutputStream("D:\\QuickMQ\\src\\main\\java\\Data\\TOPIC_0.txt");

        //read from file
        //write to steam
        //ObjectInputStream objectInputStreamFromFile = new ObjectInputStream(fileInputStream);

        ObjectInputStream ObjectInputStream = new ObjectInputStream(fileInputStream);
        FileInputStream fileInputStream1 =new FileInputStream("D:\\QuickMQ\\src\\main\\java\\Data\\TOPIC_0.txt");
        ObjectInputStream ObjectInputStream1 = new ObjectInputStream(fileInputStream1);
        ProducerRecord producerRecord= (ProducerRecord) ObjectInputStream.readObject();

        ProducerRecord producerRecord1= (ProducerRecord) ObjectInputStream1.readObject();
        System.out.println(producerRecord.toString());
        System.out.println(producerRecord1.toString());
    }
}
