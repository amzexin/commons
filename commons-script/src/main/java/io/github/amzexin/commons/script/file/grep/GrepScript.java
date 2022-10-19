package io.github.amzexin.commons.script.file.grep;

import io.github.amzexin.commons.util.io.FileUtils;

import java.io.*;
import java.util.*;

/**
 * Description: GrepScript
 *
 * @author Lizexin
 * @date 2022-09-15 11:46
 */
public class GrepScript {

    public static void main(String[] args) throws IOException {
        String directory = "/Users/xxxx";
        String patternFile = "patterns.txt";

        // 获取要匹配内容的集合
        Map<String, List<String>> result = new HashMap<>();
        InputStream patternFileInputStream = FileUtils.getInputStream(patternFile);
        new BufferedReader(new InputStreamReader(patternFileInputStream)).lines().forEach(x -> {
            result.put(x, new ArrayList<>());
        });
        Set<String> patterns = result.keySet();

        // 要排除的目录
        Set<String> excludeDirectory = new HashSet<>(Arrays.asList(".idea", "logs", "target", ".git", "test"));

        // 递归搜索文件并收集结果
        FileUtils.recursiveFileConsumer(new File(directory), excludeDirectory, file -> {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String text;
                int lineNum = 0;
                while ((text = br.readLine()) != null) {
                    lineNum++;
                    for (String pattern : patterns) {
                        if (text.contains(pattern)) {
                            result.get(pattern).add(file.getPath() + " " + lineNum + " " + text);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // 打印结果
        result.forEach((key, contexts) -> {
            if (contexts.isEmpty()) {
                return;
            }

            System.out.println(key);
            for (String text : contexts) {
                System.out.println("\t" + text);
            }
        });

    }
}
