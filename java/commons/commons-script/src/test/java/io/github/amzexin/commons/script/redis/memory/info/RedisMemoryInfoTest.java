package io.github.amzexin.commons.script.redis.memory.info;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * Description: RedisMemoryInfoTest
 *
 * @author Lizexin
 * @date 2022-09-08 11:30
 */
@Slf4j
public class RedisMemoryInfoTest {

    @Test
    public void testToString() {
        ListMemoryInfo listMemoryInfo = new ListMemoryInfo("list", Arrays.asList("value1", "value2"), -1L);
        SetMemoryInfo setMemoryInfo = new SetMemoryInfo("set", Collections.singleton("member1"), -1L);
        ZSetMemoryInfo zSetMemoryInfo = new ZSetMemoryInfo("zset", -1L);
        zSetMemoryInfo.add("member1");
        HashMemoryInfo hashMemoryInfo = new HashMemoryInfo("hash", -1L);
        hashMemoryInfo.put("hKey", "hValue");
        StringMemoryInfo stringMemoryInfo = new StringMemoryInfo("key", "value", -1L);

        DBMemoryInfo dbMemoryInfo0 = new DBMemoryInfo(0);
        dbMemoryInfo0.addKeyMemoryInfo(stringMemoryInfo);
        dbMemoryInfo0.addKeyMemoryInfo(listMemoryInfo);
        dbMemoryInfo0.addKeyMemoryInfo(setMemoryInfo);
        dbMemoryInfo0.addKeyMemoryInfo(zSetMemoryInfo);
        dbMemoryInfo0.addKeyMemoryInfo(hashMemoryInfo);

        DBMemoryInfo dbMemoryInfo1 = new DBMemoryInfo(1);
        dbMemoryInfo1.addKeyMemoryInfo(stringMemoryInfo);

        RedisMemoryInfo redisMemoryInfo = new RedisMemoryInfo();
        redisMemoryInfo.addDBMemoryInfo(dbMemoryInfo0);
        redisMemoryInfo.addDBMemoryInfo(dbMemoryInfo1);

        System.out.println(redisMemoryInfo);
    }
}
