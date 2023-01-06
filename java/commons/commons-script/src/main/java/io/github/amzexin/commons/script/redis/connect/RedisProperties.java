package io.github.amzexin.commons.script.redis.connect;

import lombok.Data;

/**
 * Description: RedisProperties
 *
 * @author Lizexin
 * @date 2022-09-07 11:45
 */
@Data
public class RedisProperties {
    /**
     * SSH tunnel
     */
    private SSHTunnel sshTunnel;
    /**
     * Redis server host.
     */
    private String host;
    /**
     * Redis server port.
     */
    private int port;
    /**
     * Login username of the redis server.
     */
    private String username;
    /**
     * Login password of the redis server.
     */
    private String password;

}
