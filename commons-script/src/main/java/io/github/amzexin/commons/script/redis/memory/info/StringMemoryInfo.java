package io.github.amzexin.commons.script.redis.memory.info;

/**
 * Description: StringMemoryInfo
 *
 * @author Lizexin
 * @date 2022-09-07 13:06
 */
public class StringMemoryInfo extends KeyMemoryInfo {

    @Override
    public String type() {
        return "string";
    }

    public StringMemoryInfo(String key, String value, Long ttl) {
        super(key, ttl);
        this.valueByteSize.addAndGet(value.getBytes().length);
    }
}
