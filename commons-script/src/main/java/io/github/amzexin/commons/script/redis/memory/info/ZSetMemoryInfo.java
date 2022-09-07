package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: ZSetMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 13:06
 */
@Getter
public class ZSetMemoryInfo extends KeyMemoryInfo {

    private final Map<String, Long> elementByteSizeMap = new ConcurrentHashMap<>();

    public void add(String member) {
        // +8 是因为score固定double类型，所以表示占用8字节
        long memberByteSize = member.getBytes().length + 8L;
        Long oldMemberByteSize = elementByteSizeMap.put(member, memberByteSize);

        valueByteSize.addAndGet(memberByteSize);

        // 如果以前添加过，要删除掉旧值的大小
        if (oldMemberByteSize != null) {
            valueByteSize.addAndGet(-oldMemberByteSize);
        }
    }

    @Override
    public String toString() {
        return String.format("%s, elementSize = %s", super.toString(), elementByteSizeMap.size());
    }

    @Override
    public String type() {
        return "zset";
    }

    public ZSetMemoryInfo(String key, Long ttl) {
        super(key, ttl);
    }

}
