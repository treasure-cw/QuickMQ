package Common;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * a class of some common function that server and client entity can(may) use <br>
 * all member variable and member function are static <br>
 * @author wangchen
 * @version 1.0.0
 */
public class CommonFunction {
    // make producer's push request tag
    private static final Integer PUSH_PRODUCER_RECORD_QUERY = 101;
    // make consumer's pull request tag
    private static final Integer PULL_CONSUMER_RECORD_QUERY = 102;
    // make follower's ack request tag
    private static final Integer FOLLOWER_ACK_QUERY = 103;

    private static final String ALL_TOPIC_NAMES = "ALL_TOPIC_NAMES";

    private static final String BASIC_FILE_PATH = "D:\\QuickMQ\\src\\main\\java\\Data\\";

    private static final String INDEX_FILE_TYPE = ".index";

    private static final String LOG_FILE_TYPE = ".log";

    private static final String TEXT_FILE_TYPE = ".txt";
    /**
     * producer's push request tag getter
     * @return Integer data means the producer's push request tag
     */
    public static Integer getPushProducerRecordQuery() {
        return PUSH_PRODUCER_RECORD_QUERY;
    }

    /**
     * consumer's pull request tag getter
     * @return Integer data means the consumer's pull request tag
     */
    public static Integer getPullConsumerRecordQuery() {
        return PULL_CONSUMER_RECORD_QUERY;
    }

    /**
     * consumer's pull request tag getter
     * @return Integer data means the follower's ack request tag
     */
    public static Integer getFollowerAckQuery() {
        return FOLLOWER_ACK_QUERY;
    }

    public static String getAllTopicNames(){
        return ALL_TOPIC_NAMES;
    }

    public static String getBasicFilePath() {
        return BASIC_FILE_PATH;
    }

    public static String getIndexFileType(){
        return INDEX_FILE_TYPE;
    }

    public static String getLogFileType(){
        return LOG_FILE_TYPE;
    }

    public static String getTextFileType(){
        return TEXT_FILE_TYPE;
    }
    /**
     * function to change type(int) data to type(byte[]) data
     * @param n the int data which will change to byte array
     * @return byte array data change from int
     * @deprecated all rebuild to object,no use anymore
     */
    public static byte[] toLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * function to change type(byte[]) data to type(int) data
     * @param b the byte array data which will change to int
     * @return int data change from byte array
     * @deprecated all rebuild to object,no use anymore
     */
    public static int toInt(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }
}
