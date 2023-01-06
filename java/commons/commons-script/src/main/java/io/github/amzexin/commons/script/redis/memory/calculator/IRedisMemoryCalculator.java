package io.github.amzexin.commons.script.redis.memory.calculator;

import io.github.amzexin.commons.script.redis.memory.info.DBMemoryInfo;
import io.github.amzexin.commons.script.redis.memory.info.KeyMemoryInfo;
import io.github.amzexin.commons.script.redis.memory.info.RedisMemoryInfo;

public interface IRedisMemoryCalculator {

    RedisMemoryInfo calculate();

    DBMemoryInfo calculate(int dbIndex);

    KeyMemoryInfo calculateString(String key);

    KeyMemoryInfo calculateList(String key);

    KeyMemoryInfo calculateSet(String key);

    KeyMemoryInfo calculateZSet(String key);

    KeyMemoryInfo calculateHash(String key);

}
