package io.github.amzexin.commons.pathplan.test.common;

import io.github.amzexin.commons.pathplan.common.IndexMinHeap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * Description:
 *
 * @author Vincent
 * @date 2019-12-31 02:16
 */
@Slf4j
public class IndexMinHeapTest {

    @Test
    public void case1() {
        IndexMinHeap<Integer, Integer> minHeap = new IndexMinHeap<>(11);
        minHeap.put(1, 1);
        minHeap.put(2, 5);
        minHeap.put(3, 9);
        minHeap.put(4, 3);
        minHeap.put(5, 4);
        minHeap.put(6, 5);
        minHeap.put(7, 8);
        minHeap.put(8, 6);
        minHeap.put(9, 6);
        minHeap.put(10, 4);
        minHeap.put(11, 6);

        // key => 1 4 6 2 5 7 8 9 10 11
        System.out.println();

        // element = 1
        System.out.println(minHeap.pop());

        // key = 4, 5, 6, 2, 10, 3, 7, 8, 9, 11
        System.out.println();

        minHeap.put(2, 10);

        // key = 4, 5, 6, 9, 10, 3, 7, 8, 2, 11
        System.out.println();
    }
}
