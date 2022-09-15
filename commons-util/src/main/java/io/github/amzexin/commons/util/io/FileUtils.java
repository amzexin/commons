package io.github.amzexin.commons.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Description: FileUtils
 *
 * @author Lizexin
 */
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 获取文件流
     * 加载与jar同目录下的文件: filePath写文件件名即可
     *
     * @param filePath 文件路径（相对路径和绝对路径）
     * @return InputStream
     * @throws FileNotFoundException 文件不存在
     */
    public static InputStream getInputStream(String filePath) throws FileNotFoundException {
        URL fileUrl = FileUtils.class.getClassLoader().getResource(filePath);
        if (fileUrl != null) {
            log.info("Starting with file at {}, file normal {}", fileUrl.toExternalForm(), fileUrl);
            return FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        }
        log.warn("No file has been found in the bundled resources. Scanning filesystem...");
        File file = new File(filePath);
        if (file.exists()) {
            log.info("Loading external file. Url = {}.", file.getAbsolutePath());
            return new FileInputStream(file);
        }
        throw new FileNotFoundException("The file does not exist. Url = " + file.getAbsolutePath());
    }

    /**
     * 获取文件内容
     *
     * @param filePath 文件路径（相对路径和绝对路径）
     * @return 文件内容
     * @throws IOException 内容读取出现异常
     */
    public static String getFileContent(String filePath) throws IOException {
        InputStream inputStream = getInputStream(filePath);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer, 0, inputStream.available());
        inputStream.close();
        return new String(buffer);
    }

    /**
     * 递归遍历某目录，并对该目录下的所有文件执行固定的操作
     *
     * @param file             需要迭代搜索的目录
     * @param excludeDirectory 需要过滤的目录
     * @param consumer         处理方法
     */
    public static void recursiveFileConsumer(File file, Set<String> excludeDirectory, Consumer<File> consumer) {
        if (!file.exists()) {
            log.info("{} isn't exists", file.getPath());
            return;
        }

        if (file.isFile()) {
            if (!file.canRead()) {
                log.info("{} can't read", file.getPath());
                return;
            }
            consumer.accept(file);
            return;
        }

        if (file.isDirectory()) {
            if (excludeDirectory.contains(file.getName())) {
                return;
            }

            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return;
            }
            log.info("开始遍历 {}", file.getPath());
            for (File subFile : files) {
                recursiveFileConsumer(subFile, excludeDirectory, consumer);
            }
        }
    }

    private FileUtils() {
    }
}
