package io.github.amzexin.commons.test.jdk.java.lang.ref;

import io.github.amzexin.commons.util.lang.SleepUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.ref.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: ReferenceTest
 * blog：https://www.cnblogs.com/skywang12345/p/3154474.html
 *
 * @author Lizexin
 * @date 2022-09-21 11:38
 */
@Slf4j
public class ReferenceTest {

    /**
     * 强引用：没有引用的地方，才释放
     */
    @Test
    public void testStrongReference() {
        Obj obj = new Obj();

        log.info("gc is start !!!!!!!!!!!!!!!!!!!! Obj obj = new Obj();");
        System.gc();

        obj = null;
        SleepUtils.sleep(1000);

        log.info("gc is start !!!!!!!!!!!!!!!!!!!! obj = null;");
        System.gc();

        log.info("gc is end !!!!!!!!!!!!!!!!!!!!");
        SleepUtils.sleep(1000);
    }

    /**
     * 软引用：内存不足时，会释放
     */
    @Test
    public void testSoftReference() {
        // 运行该方法之前，需要将Xmx设置为10m
        int MB = 1024 * 1024;
        SoftReference<Obj> softReference = new SoftReference<>(new Obj());
        log.info("gc is start !!!!!!!!!!!!!!!!!!!! softReference.get() = {}", softReference.get());
        try {
            byte[] bytes = new byte[6 * MB];
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        log.info("gc is end !!!!!!!!!!!!!!!!!!!! softReference.get() = {}", softReference.get());
        SleepUtils.sleep(1000);
    }

    /**
     * 弱引用：GC线程扫描到，就释放
     */
    @Test
    public void testWeakReference() {
        WeakReference<Obj> weakReference = new WeakReference<>(new Obj());
        log.info("gc is start !!!!!!!!!!!!!!!!!!!! weakReference.get() = {}", weakReference.get());
        System.gc();
        SleepUtils.sleep(1000);
        log.info("gc is end !!!!!!!!!!!!!!!!!!!! weakReference.get() = {}", weakReference.get());
    }

    /**
     * 虚引用：Unknown，被真正gc之后会放入队列中
     */
    @Test
    public void testPhantomReference() {
        ReferenceQueue<Obj> referenceQueue = new ReferenceQueue<>();
        PhantomReference<Obj> phantomReference = new PhantomReference<>(new Obj(), referenceQueue);

        // 不一定什么时候被回收
        Reference<? extends Obj> reference;
        while ((reference = referenceQueue.poll()) == null) {
            log.info("gc is start !!!!!!!!!!!!!!!!!!!! phantomReference.get() = {}, referenceQueue.poll() is null", phantomReference.get());
            System.gc();
            SleepUtils.sleep(1000);
        }

        log.info("gc is end !!!!!!!!!!!!!!!!!!!! phantomReference.get() = {}, referenceQueue.poll() = {}", phantomReference.get(), reference);
    }

    private static class Obj {
        private final String name = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Obj{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            log.info("{} is gc!!!!!!!!!!!!!!!!!!!", name);
        }
    }
}
