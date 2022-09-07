package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;

import java.util.Set;

/**
 * Description: SetMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 13:06
 */
@Getter
public class SetMemoryInfo extends KeyMemoryInfo {

    private final int elementSize;

    @Override
    public String type() {
        return "set";
    }

    public SetMemoryInfo(String key, Set<String> members, Long ttl) {
        super(key, ttl);
        this.elementSize = members.size();
        for (String member : members) {
            valueByteSize.addAndGet(member.getBytes().length);
        }
    }

    @Override
    public String toString() {
        return String.format("%s, elementSize = %s", super.toString(), elementSize);
    }
}
