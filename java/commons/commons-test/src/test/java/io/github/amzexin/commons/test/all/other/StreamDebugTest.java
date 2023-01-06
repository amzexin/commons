package io.github.amzexin.commons.test.all.other;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: StreamDebugTest
 *
 * @author Lizexin
 * @date 2022-08-18 15:12
 */
public class StreamDebugTest {

    @Test
    public void test() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new Random().nextInt(100) + "");
        }

        Set<String> result = list.stream()
                .filter(s -> Integer.parseInt(s) % 5 == 0)
                .map(s -> Integer.parseInt(s) * 3 + "")
                .limit(8)
                .sorted(Comparator.comparingInt(Integer::parseInt))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        System.out.println(result);
    }
}
