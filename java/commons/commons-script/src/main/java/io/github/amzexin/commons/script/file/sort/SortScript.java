package io.github.amzexin.commons.script.file.sort;

import io.github.amzexin.commons.util.io.FileUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.TreeSet;

/**
 * SortScript
 *
 * @author zexin
 */
public class SortScript {

    public static void main(String[] args) throws IOException {
        String filePath = "/Users/lizexin/Desktop/xxx";
        String prefix = "xxx";
        Set<Integer> nums = new TreeSet<>();

        InputStream inputStream = FileUtils.getInputStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            nums.add(Integer.parseInt(str.substring(prefix.length())));
        }

        Integer lastNum = null;
        for (Integer num : nums) {
            if (lastNum != null && num - lastNum != 1) {
                System.out.println(prefix + (num - 1));
            }
            lastNum = num;
        }


    }
}
