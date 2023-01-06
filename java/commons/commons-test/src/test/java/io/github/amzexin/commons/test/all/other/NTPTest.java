package io.github.amzexin.commons.test.all.other;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;

/**
 * Description: NTPTest
 *
 * @author Lizexin
 * @date 2022-04-21 11:45
 */
public class NTPTest {

    public static void main(String[] args) throws IOException {
        String timeServerUrl = "ntp1.aliyun.com";
        timeServerUrl = "time.nist.gov";
        InetAddress inetAddress = InetAddress.getByName(timeServerUrl);

        NTPUDPClient ntpudpClient = new NTPUDPClient();
        TimeInfo timeInfo = ntpudpClient.getTime(inetAddress);
        TimeStamp timeStamp = timeInfo.getMessage().getReceiveTimeStamp();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(simpleDateFormat.format(timeStamp.getDate()));

    }
}
