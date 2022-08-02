package io.github.amzexin.commons.unittest.other;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Description: ExceptionTest
 *
 * @author Lizexin
 * @date 2022-06-29 15:43
 */
@Slf4j
public class MqttExceptionTest {

    @Test
    public void run() {
        try {
            readMqttWireMessage();
        } catch (IOException e) {
            // 最终会在这个catch块中出现
            log.error(e.getMessage(), e);
        }
    }

    public void readMqttWireMessage() throws IOException {
        try {
            readByte();
        } catch (SocketTimeoutException e) {
            // 这个方法没有catch IOException, 会继续往上抛
            log.error(e.getMessage(), e);
        }
    }

    public final void readByte() throws IOException {
        throw new EOFException();
    }
}
