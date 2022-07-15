package com.amzexin.util.common;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;

/**
 * Description: FileUtil
 *
 * @author Lizexin
 * @date 2022-07-15 15:29
 */
@Slf4j
public class FileUtil {

    /**
     * 获取文件流（支持相对路径和绝对路径）
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getInputStream(String filePath) throws FileNotFoundException {
        URL fileUrl = FileUtil.class.getClassLoader().getResource(filePath);
        if (fileUrl != null) {
            log.info("Starting with file at {}, file normal {}", fileUrl.toExternalForm(), fileUrl);
            return FileUtil.class.getClassLoader().getResourceAsStream(filePath);
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
     * 获取文件内容（支持相对路径和绝对路径）
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getFileContent(String filePath) throws IOException {
        InputStream inputStream = getInputStream(filePath);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer, 0, inputStream.available());
        inputStream.close();
        return new String(buffer);
    }

    private FileUtil() {
    }
}
