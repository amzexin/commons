package io.github.amzexin.commons.script.redis.memory.info;

import lombok.Getter;

import java.util.List;

/**
 * Description: ListMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 13:06
 */
@Getter
public class ListMemoryInfo extends KeyMemoryInfo {

    private final int elementSize;

    @Override
    public String type() {
        return "list";
    }

    public ListMemoryInfo(String key, List<String> values, Long ttl) {
        super(key, ttl);
        this.elementSize = values.size();
        for (String value : values) {
            valueByteSize.addAndGet(value.getBytes().length);
        }
    }

    @Override
    public String toString() {
        return String.format("%s, elementSize = %s", super.toString(), elementSize);
    }
}
