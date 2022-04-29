package Common;

import org.jetbrains.annotations.NotNull;

/**
 * @author wangchen
 * @version 1.0.0
 */
public class OffSet {
    private final String topic;
    private final Integer partition;
    private Integer offSet;

    /**
     * constructor function with topic,partition and offset
     * @param topic name of topic that consumer will pull from
     * @param partition number of partition that consumer will pull from
     * @param offSet number of offset that consumer will pull at
     */
    public OffSet(@NotNull String topic, @NotNull Integer partition, Integer offSet) {
        this.topic = topic;
        this.partition = partition;
        this.offSet = offSet;
    }

    /**
     * constructor function without offset(default 0)
     * @param topic name of topic that consumer will pull from
     * @param partition number of partition that consumer will pull from
     */
    public OffSet(String topic, Integer partition) {
        this(topic, partition, 0);
    }

    /**
     * topic getter
     * @return String data means the name of topic that consumer will pull from
     */
    public String getTopic() {
        return topic;
    }

    /**
     * partition getter
     * @return Integer data means the number of partition that consumer will pull from
     */
    public Integer getPartition() {
        return partition;
    }

    /**
     * offset getter
     * @return Integer data means number of offset that consumer will pull at
     */
    public Integer getOffSet() {
        return offSet;
    }

    /**
     * function to add onr for the number of offset
     */
    public void plusSelf(){
        this.offSet++;
    }

    /**
     * function to sub one for the number of offset
     */
    public void subSelf(){
        this.offSet--;
    }

    /**
     * function to make the number of offset to zero
     */
    public void selfToZero() {
        this.offSet = 0;
    }
}
