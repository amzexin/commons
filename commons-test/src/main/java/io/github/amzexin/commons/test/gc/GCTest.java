package io.github.amzexin.commons.test.gc;

import java.util.Arrays;

/**
 * Description: GCTest
 * 测试方法：
 * 1. 通过开发工具打一个jar
 * 2. java -Xms4m -Xmx32m -jar commons-test.jar
 * <p>
 * JVM参数说明：
 * -Xms 最小堆内存, 默认是服务器的1/64
 * -Xmx 最大堆内存, 默认是服务器的1/4
 * -XX:+HeapDumpOnOutOfMemoryError 在OOM时，产生进程的dump文件
 * -XX:HeapDumpPath 指定dump文件的生成路径，若不指定则默认在进程的工作目录生成dump文件；还可以指定dump文件的名称
 * -Xloggc 指定gc日志文件路径（包括文件名）
 * -XX:OnOutOfMemoryError 可以指定shell命令、shell脚本、其他脚本等，在OOM时执行一些额外的操作（实践证明，是先产生dump文件，再执行配置）
 * -XX:+ExitOnOutOfMemoryError，OOM后，应用程序直接退出
 *
 * @author Lizexin
 * @date 2022-08-22 14:42
 */
public class GCTest {

    private final static int MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[][] bytes = new byte[2][];
        int index = 0;
        while (true) {
            if (index == bytes.length - 1) {
                System.out.println(String.format("copy %s MB", index));
                bytes = Arrays.copyOf(bytes, bytes.length * 2);
            }
            System.out.println(String.format("%sMB", index));
            bytes[index++] = new byte[MB];
            System.gc();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
