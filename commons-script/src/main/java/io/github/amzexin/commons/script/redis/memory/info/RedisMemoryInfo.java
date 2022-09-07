package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description: RedisMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 14:26
 */
@Getter
public class RedisMemoryInfo {

    private final AtomicLong totalByteSize = new AtomicLong();

    private final Map<Integer, DBMemoryInfo> dbMemoryInfos = new ConcurrentHashMap<>();

    public long getTotalByteSize() {
        return totalByteSize.get();
    }

    public String getHumanTotalByteSize() {
        return ByteUtils.byteHumanize(totalByteSize.get());
    }

    public void addDBMemoryInfo(DBMemoryInfo dbMemoryInfo) {
        DBMemoryInfo oldMemoryInfo = dbMemoryInfos.put(dbMemoryInfo.getIndex(), dbMemoryInfo);
        totalByteSize.addAndGet(dbMemoryInfo.getTotalByteSize());
        if (oldMemoryInfo != null) {
            totalByteSize.addAndGet(oldMemoryInfo.getTotalByteSize());
        }
    }

    @Override
    public String toString() {
        String humanTotalByteSize = getHumanTotalByteSize();
        StringBuilder result = new StringBuilder(String.format("redis (%s)\n", humanTotalByteSize.isEmpty() ? "0B" : getHumanTotalByteSize()));
        if (dbMemoryInfos.isEmpty()) {
            return result.toString();
        }

        dbMemoryInfos.forEach((key, value) -> {
            result.append(value.toString()).append("\n");
        });
        return result.toString();
    }
}
