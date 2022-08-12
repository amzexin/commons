package io.github.amzexin.commons.pathplan.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: index min heap
 *
 * @author lizexin
 * @date 2019-12-31 01:12
 */
public class IndexMinHeap<K, T extends Comparable<T>> {

    /**
     * 原始数据 key:index; value:element
     */
    private Map<K, T> data;

    /**
     * 数组实现的最小索引堆
     */
    private Map<Integer, K> indexes;

    /**
     * 通过【原始数据key】获取对应【最小索引堆的位置】
     */
    private Map<K, Integer> reverse;

    private int capacity;

    private int count;

    public void put(K key, T element) {
        if (count >= capacity) {
            throw new IllegalStateException("index min heap is full");
        }
        if (data.containsKey(key)) {
            update(key, element);
        } else {
            insert(key, element);
        }
    }

    public T pop() {

        if (count == 0) {
            throw new IllegalStateException("index min heap is empty");
        }

        T min = data.remove(indexes.get(0));
        reverse.remove(indexes.get(0));

        indexes.put(0, indexes.get(count - 1));
        indexes.put(count - 1, null);
        reverse.put(indexes.get(0), 0);
        count--;

        shiftDown(0);

        return min;
    }

    public K popKey() {

        if (count == 0) {
            throw new IllegalStateException("index min heap is empty");
        }

        K key = indexes.get(0);

        data.remove(indexes.get(0));
        reverse.remove(indexes.get(0));

        indexes.put(0, indexes.get(count - 1));
        indexes.put(count - 1, null);
        reverse.put(indexes.get(0), 0);
        count--;

        shiftDown(0);

        return key;
    }

    public T top() {
        return count == 0 ? null : data.get(indexes.get(0));
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public boolean containsIndex(K key) {
        return data.containsKey(key);
    }

    public T get(K key) {
        return data.get(key);
    }

    private void insert(K key, T element) {
        data.put(key, element);
        indexes.put(count, key);
        reverse.put(key, count);
        count++;

        shiftUp(count - 1);
    }

    private void update(K key, T element) {
        data.put(key, element);

        shiftUp(reverse.get(key));
        shiftDown(reverse.get(key));
    }

    public IndexMinHeap(int capacity) {
        this.capacity = capacity;
        this.count = 0;
        this.data = new HashMap<>(capacity);
        this.indexes = new HashMap<>(capacity);
        this.reverse = new HashMap<>(capacity);
    }

    private void shiftUp(int indexesIndex) {
        while (indexesIndex > 0) {
            int son = (indexesIndex - 1) / 2;
            if (data.get(indexes.get(son)).compareTo(data.get(indexes.get(indexesIndex))) > 0) {
                swapIndex(son, indexesIndex);
            }
            indexesIndex = son;
        }
    }

    private void shiftDown(int indexesIndex) {

        while (2 * indexesIndex + 1 < count) {
            int minIndex = 2 * indexesIndex + 1;

            if (minIndex + 1 < count) {
                minIndex = data.get(indexes.get(minIndex)).compareTo(data.get(indexes.get(minIndex + 1))) < 0 ? minIndex : minIndex + 1;
            }

            swapIndex(indexesIndex, minIndex);

            indexesIndex = minIndex;
        }
    }

    private void swapIndex(int indexesIndex1, int indexesIndex2) {
        reverse.put(indexes.get(indexesIndex1), indexesIndex2);
        reverse.put(indexes.get(indexesIndex2), indexesIndex1);

        K temp = indexes.get(indexesIndex1);
        indexes.put(indexesIndex1, indexes.get(indexesIndex2));
        indexes.put(indexesIndex2, temp);
    }
}