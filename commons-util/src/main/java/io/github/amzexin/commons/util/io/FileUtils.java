package io.github.amzexin.commons.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * Description: FileUtils
 *
 * @author Lizexin
 */
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 获取文件流
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

    private FileUtils() {
    }
}
