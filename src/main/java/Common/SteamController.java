package Common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * a class of steam controller that contents some IO steam pools <br>
 * can build and manage some IO steam pools <br>
 * @author wangchen
 * @version 1.0.0
 */
public class SteamController {
    private static final Map<String, FileInputStream> FileInput = new HashMap<>();
    private static final Map<String, FileOutputStream> FileOutput = new HashMap<>();

    private static final Map<FileInputStream, ObjectInputStream> ObjectInput = new HashMap<>();
    private static final Map<FileOutputStream, ObjectOutputStream> ObjectOutput = new HashMap<>();

    private static final Map<InputStream, ObjectInputStream> ObjectInputSteam = new HashMap<>();
    private static final Map<OutputStream, ObjectOutputStream> ObjectOutputSteam = new HashMap<>();

    /**
     * function to insert FileInputSteam to steam pool
     * @param path a path of a file
     * @param fileInputStream a new FileInputSteam of a file
     */
    public static void insertFileInput(String path, FileInputStream fileInputStream){
        FileInput.put(path, fileInputStream);
    }

    /**
     * function to insert FileOutputSteam to steam pool
     * @param path a path of a file
     * @param fileOutputStream a new FileOutputSteam of a file
     */
    public static void insertFileOutput(String path, FileOutputStream fileOutputStream){
        FileOutput.put(path, fileOutputStream);
    }

    /**
     * function to insert ObjectInputSteam to steam pool
     * @param fileInputStream a FileInputSteam
     * @param objectInputStream a new ObjectInputSteam of a FileInputSteam
     */
    public static void insertObjectInput(FileInputStream fileInputStream, ObjectInputStream objectInputStream){
        ObjectInput.put(fileInputStream, objectInputStream);
    }

    /**
     * function to insert ObjectOutputSteam to steam pool
     * @param fileOutputStream a FileOutputSteam
     * @param objectOutputStream a new ObjectOutputSteam of a FileOutputSteam
     */
    public static void insertObjectOutput(FileOutputStream fileOutputStream, ObjectOutputStream objectOutputStream){
        ObjectOutput.put(fileOutputStream, objectOutputStream);
    }

    /**
     * function to insert ObjectInputSteam to steam pool for consumer
     * @param inputStream an InputSteam
     * @param objectInputStream a new ObjectInputSteam of an InputSteam
     */
    public static void insertObjectInputSteam(InputStream inputStream, ObjectInputStream objectInputStream){
        ObjectInputSteam.put(inputStream, objectInputStream);
    }

    public static void insertObjectOutputSteam(OutputStream outputStream, ObjectOutputStream objectOutputStream){
        ObjectOutputSteam.put(outputStream, objectOutputStream);
    }
    /**
     * function to get FileInputSteam by identity path of file
     * @param path the path of file
     * @return class of FileInputSteam data means identity file's FileInputSteam
     * @throws FileNotFoundException
     */
    public static FileInputStream getFileInputByPath(String path) throws FileNotFoundException {
        if(FileInput.get(path) != null) {
            return FileInput.get(path);
        }
        insertFileInput(path, new FileInputStream(path));
        return FileInput.get(path);
    }

    /**
     * function to get FileOutputSteam by identity path of file
     * @param path the path of file
     * @return class of FileOutputSteam data means identity file's FileOutputSteam
     * @throws FileNotFoundException
     */
    public static FileOutputStream getFileOutputByPath(String path) throws FileNotFoundException {
        if(FileOutput.get(path) != null) {
            return FileOutput.get(path);
        }
        insertFileOutput(path, new FileOutputStream(path));
        return FileOutput.get(path);
    }

    /**
     * function to get ObjectInputStream by identity FileInputStream
     * @param fileInputStream identity FileInputSteam
     * @return class of ObjectInputStream data means identity FileInputStream's ObjectInputStream
     * @throws IOException
     */
    public static ObjectInputStream getObjectInputByFileInputSteam(FileInputStream fileInputStream) throws IOException {
        if(ObjectInput.get(fileInputStream) != null){
            return ObjectInput.get(fileInputStream);
        }
        insertObjectInput(fileInputStream, new ObjectInputStream(fileInputStream));
        return ObjectInput.get(fileInputStream);
    }

    /**
     * function to get ObjectOutputStream by identity FileOutputStream
     * @param fileOutputStream identity FileOutputStream
     * @return class of ObjectOutputStream data means identity FileOutputStream's ObjectOutputStream
     * @throws IOException
     */
    public static ObjectOutputStream getObjectOutputByFileOutputSteam(FileOutputStream fileOutputStream) throws IOException {
        if(ObjectOutput.get(fileOutputStream) != null){
            return ObjectOutput.get(fileOutputStream);
        }
        insertObjectOutput(fileOutputStream, new ObjectOutputStream(fileOutputStream));
        return ObjectOutput.get(fileOutputStream);
    }

    /**
     * function to get ObjectInputStream by identity InputStream
     * @param inputStream identity InputStream
     * @return class of ObjectInputStream data means identity InputStream's ObjectInputStream
     * @throws IOException
     */
    public static ObjectInputStream getObjectInputSteamByInputSteam(InputStream inputStream) throws IOException {
        if(ObjectInputSteam.get(inputStream) != null){
            return ObjectInputSteam.get(inputStream);
        }
        insertObjectInputSteam(inputStream, new ObjectInputStream(inputStream));
        return ObjectInputSteam.get(inputStream);
    }

    public static ObjectOutputStream getObjectOutputSteamByOutputSteam(OutputStream outputStream) throws IOException {
        if(ObjectOutputSteam.get(outputStream) != null){
            return ObjectOutputSteam.get(outputStream);
        }
        insertObjectOutputSteam(outputStream, new ObjectOutputStream(outputStream));
        return ObjectOutputSteam.get(outputStream);
    }
}
