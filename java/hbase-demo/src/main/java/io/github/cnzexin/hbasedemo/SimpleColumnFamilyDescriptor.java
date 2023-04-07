package io.github.cnzexin.hbasedemo;

import lombok.Data;

/**
 * SimpleColumnFamilyDescriptor
 *
 * @author Zexin Li
 * @date 2023-04-07 19:50
 */
@Data
public class SimpleColumnFamilyDescriptor {
    /**
     * 列族名称
     */
    private String name;
    /**
     * 有效时间，单位：秒
     */
    private int timeToLive;
    /**
     * 压缩类型
     */
    private String compressionType;
    /**
     * 历史版本数
     */
    private int historyVersionCount;
}
