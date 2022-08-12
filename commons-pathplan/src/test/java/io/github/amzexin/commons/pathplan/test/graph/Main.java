package io.github.amzexin.commons.pathplan.test.graph;

import java.util.Set;
import java.util.TreeSet;

/**
 * Description:
 *
 * @author Vincent
 * @date 2020-01-18 16:49
 */
public class Main {

    public static void main(String[] args) {
        Set<String> objects = new TreeSet<>();

        objects.add("A_11-2");
        objects.add("A_11-1");
        objects.add("A_12-2");

        System.out.println(objects.toString());

        double sqrt = Math.sqrt(Math.pow(-42.58488513727855 - (-41.18301117267325), 2) + Math.pow(-21.93510016230548 - (-22.19179124288499), 2));
        System.out.println(sqrt);

    }
}
