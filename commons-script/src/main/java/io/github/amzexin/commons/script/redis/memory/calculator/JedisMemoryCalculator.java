package io.github.amzexin.commons.script.redis.memory.calculator;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.script.redis.memory.info.*;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: JedisMemoryCalculateAdptor
 *
 * @author Lizexin
 * @date 2022-09-07 13:33
 */
@Slf4j
public class JedisMemoryCalculator implements IRedisMemoryCalculator {

    protected Jedis jedis;

    private int maxScanCount = 1000;

    public JedisMemoryCalculator(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public RedisMemoryInfo calculate() {
        RedisMemoryInfo redisMemoryInfo = new RedisMemoryInfo();
        for (int i = 0; i < 16; i++) {
            DBMemoryInfo dbMemoryInfo = calculate(i);
            redisMemoryInfo.addDBMemoryInfo(dbMemoryInfo);
            log.info("{}", dbMemoryInfo.toString());
        }
        return redisMemoryInfo;
    }

    @Override
    public DBMemoryInfo calculate(int dbIndex) {
        TraceIdUtils.setupTraceId("db " + dbIndex);
        DBMemoryInfo dbMemoryInfo = new DBMemoryInfo(dbIndex);
        jedis.select(dbIndex);

        ScanParams scanParams = new ScanParams();
        scanParams.count(maxScanCount);
        String cursor = ScanParams.SCAN_POINTER_START;
        int curCount = 0;

        while (true) {
            log.info("dbindex = {}, cursor = {}, curCount = {}", dbIndex, cursor, curCount);
            // 使用scan命令获取数据，使用cursor游标记录位置，下次循环使用
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            keys.forEach(key -> {
                String keyType = jedis.type(key);
                switch (keyType) {
                    case "none":
                        break;
                    case "string":
                        dbMemoryInfo.addKeyMemoryInfo(calculateString(key));
                        break;
                    case "list":
                        dbMemoryInfo.addKeyMemoryInfo(calculateList(key));
                        break;
                    case "set":
                        dbMemoryInfo.addKeyMemoryInfo(calculateSet(key));
                        break;
                    case "zset":
                        dbMemoryInfo.addKeyMemoryInfo(calculateZSet(key));
                        break;
                    case "hash":
                        dbMemoryInfo.addKeyMemoryInfo(calculateHash(key));
                        break;
                    default:
                        log.warn("keyType: {}, 未知", keyType);
                        break;
                }
            });

            cursor = scanResult.getCursor();
            curCount += keys.size();
            if ("0".equals(cursor)) {
                // 返回0 说明遍历完成
                break;
            }

            SleepUtils.sleep(500);
        }

        TraceIdUtils.clearTraceId();
        return dbMemoryInfo;
    }

    @Override
    public KeyMemoryInfo calculateString(String key) {
        String value = jedis.get(key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        Long ttl = jedis.ttl(key);
        return new StringMemoryInfo(key, value, ttl);
    }

    @Override
    public KeyMemoryInfo calculateList(String key) {
        List<String> values = jedis.lrange(key, 0, -1);
        Long ttl = jedis.ttl(key);
        return new ListMemoryInfo(key, values, ttl);
    }

    @Override
    public KeyMemoryInfo calculateSet(String key) {
        Set<String> members = jedis.smembers(key);
        Long ttl = jedis.ttl(key);
        return new SetMemoryInfo(key, members, ttl);
    }

    @Override
    public KeyMemoryInfo calculateZSet(String key) {
        Long ttl = jedis.ttl(key);
        ZSetMemoryInfo memoryInfo = new ZSetMemoryInfo(key, ttl);

        ScanParams scanParams = new ScanParams();
        scanParams.count(maxScanCount);
        String cursor = ScanParams.SCAN_POINTER_START;
        int curCount = 0;

        while (true) {
            ScanResult<Tuple> scanResult = jedis.zscan(key, cursor, scanParams);

            List<Tuple> values = scanResult.getResult();
            for (int i = 0; i < values.size(); i++) {
                memoryInfo.add(values.get(i).getElement());
            }

            cursor = scanResult.getCursor();
            curCount += values.size();
            if ("0".equals(cursor)) {
                // 返回0 说明遍历完成
                break;
            }

            log.info("zset key = {}, cursor = {}, curCount = {}", key, cursor, curCount);
            SleepUtils.sleep(500);
        }

        return memoryInfo;
    }

    @Override
    public KeyMemoryInfo calculateHash(String key) {
        Long ttl = jedis.ttl(key);
        HashMemoryInfo memoryInfo = new HashMemoryInfo(key, ttl);

        ScanParams scanParams = new ScanParams();
        scanParams.count(maxScanCount);
        String cursor = ScanParams.SCAN_POINTER_START;
        int curCount = 0;

        while (true) {
            ScanResult<Map.Entry<String, String>> scanResult = jedis.hscan(key, cursor, scanParams);

            List<Map.Entry<String, String>> values = scanResult.getResult();
            for (int i = 0; i < values.size(); i++) {
                Map.Entry<String, String> entry = values.get(i);
                memoryInfo.put(entry.getKey(), entry.getValue());
            }

            cursor = scanResult.getCursor();
            curCount += values.size();
            if ("0".equals(cursor)) {
                // 返回0 说明遍历完成
                break;
            }

            log.info("hash key = {}, cursor = {}, curCount = {}", key, cursor, curCount);
            SleepUtils.sleep(500);
        }

        return memoryInfo;
    }

}
