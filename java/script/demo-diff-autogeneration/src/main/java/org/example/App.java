package org.example;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ZipUtil;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        String script = "/Users/lizexin/projects/cloud-iot/iot-ota/iot-ota-console-api/src/main/resources/diff/adiff_3.5_lnx";
        String oldOriginZip = "/Users/lizexin/temp/diff-autogeneration/EC200NCNGBR03A06M16_OCPU_427.zip";
        String newOriginZip = "/Users/lizexin/temp/diff-autogeneration/EC200NCNGBR03A06M16_OCPU_428.zip";

        if (args != null && args.length == 3) {
            script = FileUtil.getAbsolutePath(args[0]);
            oldOriginZip = FileUtil.getAbsolutePath(args[1]);
            newOriginZip = FileUtil.getAbsolutePath(args[2]);
        }

        if (!script.startsWith("/")) {
            script = "./" + script;
        }

        String jarAbsolutPath = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String jarDir = jarAbsolutPath.substring(0, jarAbsolutPath.lastIndexOf("/"));
        String diffBin = jarDir + "/" + System.currentTimeMillis() + ".bin";

        File oldDir = ZipUtil.unzip(oldOriginZip, jarDir + "/old");
        File newDir = ZipUtil.unzip(newOriginZip, jarDir + "/new");

        String cmd = String.format("%s -p %s %s %s -a1 customer_app %s %s -l fs",
                script, oldDir.getAbsolutePath() + "/system.img", newDir.getAbsolutePath() + "/system.img",
                diffBin, oldDir.getAbsolutePath() + "/customer_app.bin", newDir.getAbsolutePath() + "/customer_app.bin"
        );
        System.out.println("开始执行命令: " + cmd);

        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor(3, TimeUnit.MINUTES);
            System.out.println("命令执行完成");
            printStream("stderr", process.getErrorStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void printStream(String name, InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        String str = IoUtil.read(new BufferedReader(new InputStreamReader(inputStream)));
        if (str.isEmpty()) {
            return;
        }
        System.out.println(name + ": " + str);
    }
}
