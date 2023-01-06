package io.github.amzexin.commons.script.redis.connect;

import lombok.Data;

/**
 * Description: JumpProperties
 *
 * @author Lizexin
 * @date 2022-09-07 11:47
 */
@Data
public class SSHTunnel {
    /**
     * SSH server host
     */
    private String host;
    /**
     * SSH server port
     */
    private int port;
    /**
     * local port
     */
    private int lport;
    /**
     * Login username of the ssh server.
     */
    private String username;
    /**
     * Login password of the ssh server.
     */
    private String password;

    public boolean isEmpty() {
        return host == null || host.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty() || port <= 0 || lport <= 0;
    }
}
