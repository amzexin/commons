package jdktest.java.util.concurrent;

import io.github.amzexin.commons.logback.TraceIdUtils;
import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Description: ConcurrentHashMapForeachTest
 * 使用Map（ConcurrentHashMap、HashMap），进行foreach，并在此期间不断添加元素
 * HashMap会抛出ConcurrentModificationException
 * ConcurrentHashMap不会
 *
 * @author Lizexin
 * @date 2022-09-22 11:43
 */
@Slf4j
public class ConcurrentHashMapForeachTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private Map<String, String> map = new ConcurrentHashMap<>();

    private void put() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        TraceIdUtils.setupTraceId("put " + i);
                        map.put(i + "", i + "");
                        log.info("put {}", i);
                        if (i > 5) {
                            SleepUtils.sleep(1000);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    private void remove() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        TraceIdUtils.setupTraceId("remove " + i);
                        map.remove(i + "");
                        log.info("remove {}", i);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    private void mapForeach() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    map.forEach(new BiConsumer<String, String>() {
                        @Override
                        public void accept(String key, String value) {
                            log.info("map.forEach ===> key = {}, value = {}", key, value);
                            SleepUtils.sleep(1000);
                        }
                    });
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    private void keysForeach() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Set<String> keys = map.keySet();
                    for (String key : keys) {
                        log.info("keys.forEach key = {}, value = {}", key, map.get(key));
                        SleepUtils.sleep(1000);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    private void valuesForeach() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Collection<String> values = map.values();
                    for (String value : values) {
                        log.info("values.forEach value = {}", value);
                        SleepUtils.sleep(1000);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }


    @Test
    public void test20220922_1146() throws IOException {
        put();
        // remove();
        SleepUtils.sleep(1000);
        mapForeach();
        keysForeach();
        valuesForeach();
        System.in.read();
    }
}
