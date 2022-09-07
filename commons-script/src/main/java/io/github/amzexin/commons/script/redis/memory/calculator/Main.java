package io.github.amzexin.commons.script.redis.memory.calculator;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.github.amzexin.commons.script.redis.connect.RedisProperties;
import io.github.amzexin.commons.script.redis.connect.SSHTunnel;
import io.github.amzexin.commons.script.redis.memory.info.RedisMemoryInfo;
import io.github.amzexin.commons.util.io.FileUtils;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * Description: 通过扫描redis所有db，获取内存使用情况。
 * 最终效果可以通运行io.github.amzexin.commons.script.redis.memory.info.RedisMemoryInfoTest.testToString()方法查看
 *
 * @author Lizexin
 * @date 2022-09-07 10:54
 */
@Slf4j
public class Main {

    /**
     * 远程端口映射到本地端口
     * 相关博客：<a herf="https://juejin.cn/post/6965773279906758670">本地连接数据库</a>
     *
     * @param redisProperties
     * @throws JSchException
     */
    private static void remotePortMappingLocalPort(RedisProperties redisProperties) throws JSchException {
        if (redisProperties.getSshTunnel() == null) {
            throw new RuntimeException("SshTunnel is null");
        }
        JSch jSch = new JSch();
        SSHTunnel sshTunnel = redisProperties.getSshTunnel();
        Session session = jSch.getSession(sshTunnel.getUsername(), sshTunnel.getHost(), sshTunnel.getPort());
        session.setPassword(sshTunnel.getPassword());
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        // 映射到本地的端口,这里是什么，下面的jedis也要配置什么
        int assinged_port = session.setPortForwardingL(sshTunnel.getLport(), redisProperties.getHost(), redisProperties.getPort());
        session.setTimeout(5_000);
        log.info("The ssh connection is OK.");
    }

    private static RedisProperties loadRedisProperties(String redisConfPath) throws IOException {
        Properties properties = new Properties();
        properties.load(FileUtils.getInputStream(redisConfPath));

        SSHTunnel sshTunnel = new SSHTunnel();
        sshTunnel.setHost(properties.getProperty("ssh.host"));
        sshTunnel.setPort(Integer.parseInt(properties.getProperty("ssh.port", "0")));
        sshTunnel.setUsername(properties.getProperty("ssh.username"));
        sshTunnel.setPassword(properties.getProperty("ssh.password"));
        sshTunnel.setLport(Integer.parseInt(properties.getProperty("ssh.lport", "0")));

        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost(properties.getProperty("redis.host"));
        redisProperties.setPort(Integer.parseInt(properties.getProperty("redis.port", "0")));
        redisProperties.setUsername(properties.getProperty("redis.username"));
        redisProperties.setPassword(properties.getProperty("redis.password"));
        if (!sshTunnel.isEmpty()) {
            redisProperties.setSshTunnel(sshTunnel);
        }

        return redisProperties;
    }

    /**
     * start script: nohup java -jar commons-script-jar-with-dependencies.jar redis.properties > /dev/null 2>&1 &
     *
     * @param args
     * @throws JSchException
     * @throws IOException
     */
    public static void main(String[] args) throws JSchException, IOException {
        log.info("args = {}", Arrays.toString(args));

        String redisConfPath = "/Users/lizexin/amzexin/technology/projects-github/commons/commons-script/target/redis.properties";

        if (args != null) {
            if (args.length >= 1) {
                redisConfPath = args[0];
            }
        }

        RedisProperties redisProperties = loadRedisProperties(redisConfPath);
        String redisHost;
        int redisPort;
        if (redisProperties.getSshTunnel() != null) {
            // 通过ssh连接redis，并将映射到本地端口
            remotePortMappingLocalPort(redisProperties);
            redisHost = "127.0.0.1";
            redisPort = redisProperties.getSshTunnel().getLport();
        } else {
            // 直连redis
            redisHost = redisProperties.getHost();
            redisPort = redisProperties.getPort();
        }

        log.info("redisHost = {}, redisPort = {}", redisHost, redisPort);

        Jedis jedis = null;
        try {
            jedis = new Jedis(redisHost, redisPort);
            if (!redisProperties.getPassword().isEmpty()) {
                jedis.auth(redisProperties.getPassword());
            }
            JedisMemoryCalculator calculator = new JedisMemoryCalculator(jedis);
            RedisMemoryInfo redisMemoryInfo = calculator.calculate();
            log.info("{}", redisMemoryInfo.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

    }

}