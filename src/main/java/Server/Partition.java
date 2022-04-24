package Server;

/**
 * @author wangchen
 * @version 1.0.0
 */
public interface Partition {
    int getHighWaterFromRedis();
}
