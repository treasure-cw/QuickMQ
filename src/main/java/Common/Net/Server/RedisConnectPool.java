package Common.Net.Server;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

public class RedisConnectPool {
    private static final List<Jedis> freeRedisConnection = new ArrayList<>();
    private static final List<Jedis> busyRedisConnection = new ArrayList<>();

    /**
     * function to initialize RedisConnectPool
     */
    public static void setFreeRedisConnection(int size) {
        for (int i = 0; i < size; i++) {
            getFreeRedisConnection().add(new Jedis("localhost"));
        }
    }


    public static List<Jedis> getFreeRedisConnection() {
        return freeRedisConnection;
    }


    public static List<Jedis> getBusyRedisConnection() {
        return busyRedisConnection;
    }

    /**
     * function to get redis connection
     */
    public static synchronized Jedis getConnection(){
        if(getFreeRedisConnection().size() == 0){
            return null;
        }
        else{
            Jedis redis = getFreeRedisConnection().get(0);
            getFreeRedisConnection().remove(0);
            getBusyRedisConnection().add(redis);
            return redis;
        }
    }

    /**
     * function to release redis connection
     */
    public static synchronized Boolean releaseConnection(Jedis redis){
        for (int i = 0; i < getBusyRedisConnection().size(); i++) {
            if(redis.equals(getBusyRedisConnection().get(i))){
                getBusyRedisConnection().remove(i);
                getFreeRedisConnection().add(redis);
                return true;
            }
        }
        return false;
    }

}
