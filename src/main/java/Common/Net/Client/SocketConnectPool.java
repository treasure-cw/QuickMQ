package Common.Net.Client;

import Common.MateData;

/**
 * an interface of socket pool entity
 * @author wangchen
 * @version 1.0.0
 */
public interface SocketConnectPool {
    /**
     * initialize the socket pool for client(producer or consumer) <br>
     * abstract function <br>
     * must be rewritten <br>
     * @param mateData the mate data of broker group
     */
    void initSocketConnectPool(MateData mateData);

    /**
     * alter the socket pool for client(producer or consumer) <br>
     * abstract function <br>
     * must be rewritten <br>
     * @param mateData the mate data of broker group
     */
    void flushSocketConnectPool(MateData mateData);
}
