package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description: DBMemoryInfo TODO 待优化 toString
 *
 * @author Lizexin
 * @date 2022-09-07 14:27
 */
@Getter
public class DBMemoryInfo {

    private final int index;

    private final AtomicLong totalByteSize = new AtomicLong();

    private final Map<String, Long> keyByteSizeMap = new ConcurrentHashMap<>();

    private final Map<String, KeyMemoryInfo> keyByteSizeTopNumMemoryInfoMap = new ConcurrentHashMap<>();

    @Setter
    private int topNum = 20;

    public long getTotalByteSize() {
        return totalByteSize.get();
    }

    public String getHumanTotalByteSize() {
        return ByteUtils.byteHumanize(totalByteSize.get());
    }

    public void addKeyMemoryInfo(KeyMemoryInfo keyMemoryInfo) {
        if (keyMemoryInfo == null) {
            return;
        }

        Long oldTotalByteSize = keyByteSizeMap.put(keyMemoryInfo.getKey(), keyMemoryInfo.getTotalByteSize());
        totalByteSize.addAndGet(keyMemoryInfo.getTotalByteSize());

        // 说明之前添加过，删除旧值的byteSize
        if (oldTotalByteSize != null) {
            totalByteSize.addAndGet(-oldTotalByteSize);
        }

        keyByteSizeTopNumMemoryInfoMap.put(keyMemoryInfo.getKey(), keyMemoryInfo);
        if (keyByteSizeTopNumMemoryInfoMap.size() > topNum) {
            KeyMemoryInfo minMemoryUsageKey = keyByteSizeTopNumMemoryInfoMap.values().stream().min(Comparator.comparingLong(KeyMemoryInfo::getTotalByteSize)).get();
            keyByteSizeTopNumMemoryInfoMap.remove(minMemoryUsageKey.getKey());
        }
    }

    public DBMemoryInfo(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        String humanTotalByteSize = getHumanTotalByteSize();
        StringBuilder result = new StringBuilder(String.format("db %s (%s), keySize: %s", index, humanTotalByteSize.isEmpty() ? "0B" : getHumanTotalByteSize(), keyByteSizeMap.size()));
        if (topNum <= 0 || keyByteSizeTopNumMemoryInfoMap.isEmpty()) {
            return result.toString();
        }
        result.append(String.format("\n\tkey memory usage top%s ↓↓↓", topNum));
        keyByteSizeTopNumMemoryInfoMap.values().stream()
                .sorted((o1, o2) -> Long.compare(o2.getTotalByteSize(), o1.getTotalByteSize()))
                .forEach(value -> {
                    result.append(String.format("\n\t%s", value));
                });
        return result.toString();
    }
}
