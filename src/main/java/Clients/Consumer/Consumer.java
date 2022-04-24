package Clients.Consumer;

import Common.OffSet;

/**
 * an interface of consumer entity
 * @author wangchen
 * @version 1.0.0
 */
public interface Consumer<K, V> {
    /**
     * function to pull consumer record from topic <br>
     * abstract function <br>
     * must be rewritten <br>
     * @param offSet the information to identify which topic and partition and offset the consumer wants to pull
     * @return consumer record
     */
    ConsumerRecord pull(OffSet offSet);
}
