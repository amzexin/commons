package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: HashMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 13:06
 */
@Getter
public class HashMemoryInfo extends KeyMemoryInfo {

    private final Map<String, Long> elementByteSizeMap = new ConcurrentHashMap<>();

    public void put(String hKey, String hValue) {
        long hKeyByteSize = hKey.getBytes().length;
        long hValueByteSize = hValue.getBytes().length;
        Long oldTotalByteSize = elementByteSizeMap.put(hKey, hKeyByteSize + hValueByteSize);

        valueByteSize.addAndGet(hKeyByteSize);
        valueByteSize.addAndGet(hValueByteSize);

        // 如果以前添加过，要删除掉旧值的大小
        if (oldTotalByteSize != null) {
            valueByteSize.addAndGet(-oldTotalByteSize);
        }
    }

    @Override
    public String type() {
        return "hash";
    }

    public HashMemoryInfo(String key, Long ttl) {
        super(key, ttl);
    }

    @Override
    public String toString() {
        return String.format("%s, elementSize = %s", super.toString(), elementByteSizeMap.size());
    }
}
