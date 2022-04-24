package Common;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisOperation {
    public static Long getClock(Jedis redis, String key){
        return redis.setnx(key, "lock");
    }

    public static Long releaseLock(Jedis redis, String key){
        return redis.del(key);
    }

    public static String getKey(Jedis redis, String key){
        return redis.get(key);
    }

    public static Long setKey(Jedis redis, String key, String value){
        return redis.setnx(key, value);
    }

    public static Set<String> getSetValues(Jedis redis, String key){
        return redis.smembers(key);
    }

    public static List<String> getListValues(Jedis redis, String key){
        return redis.lrange(key, 0 ,10);
    }

    public static Map<String, String> getHashTableKeysAndValues(Jedis redis, String name){
        return redis.hgetAll(name);
    }
}
