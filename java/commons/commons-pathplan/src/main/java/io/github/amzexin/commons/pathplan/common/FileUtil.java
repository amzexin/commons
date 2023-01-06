package io.github.amzexin.commons.pathplan.common;

import java.io.*;

/**
 * Description:
 *
 * @author Vincent
 * @date 2020-01-18 14:01
 */
public class FileUtil {

    public static String readJsonFile(String fileName) {
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private FileUtil() {
    }
}
