package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Description: KeyMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 16:49
 */
@Getter
public abstract class KeyMemoryInfo {

    protected final String key;

    protected final Long ttl;

    protected final AtomicLong keyByteSize;

    protected final AtomicLong valueByteSize;

    public abstract String type();

    public long getKeyByteSize() {
        return keyByteSize.get();
    }

    public long getTotalByteSize() {
        return keyByteSize.get() + valueByteSize.get();
    }

    public String getHumanByteSize(long byteSize) {
        return ByteUtils.byteHumanize(byteSize);
    }

    protected KeyMemoryInfo(String key, Long ttl) {
        this.key = key;
        this.keyByteSize = new AtomicLong(key.getBytes().length);
        this.valueByteSize = new AtomicLong();
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return String.format("key(%s): %s, totalByteSize: %s, ttl: %ss, keyByteSize: %s, valueByteSize: %s",
                type(),
                key,
                getHumanByteSize(getTotalByteSize()),
                ttl,
                getHumanByteSize(keyByteSize.get()),
                getHumanByteSize(valueByteSize.get()));
    }
}
