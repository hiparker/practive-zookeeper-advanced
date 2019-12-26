package com.pracitve.parker.practivezookeeperadvanced.zookeeper.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ZkConf {

    //@Value("zookeeper.address")
    private String zookeeperAddress;
    //@Value("zookeeper.sessiontimeout")
    private Integer zookeeperSessionTimeout;
    //@Value("zookeeper.name")
    private String zookeeperName;

    public ZkConf() {
        super();
        this.zookeeperAddress = "172.16.165.22:2181,172.16.165.21:2181,172.16.165.20:2181";
        this.zookeeperSessionTimeout = 30000;
        this.zookeeperName = "config";
    }

    public ZkConf(String zookeeperAddress, Integer zookeeperSessionTimeout, String zookeeperName) {
        this.zookeeperAddress = zookeeperAddress;
        this.zookeeperSessionTimeout = zookeeperSessionTimeout;
        this.zookeeperName = zookeeperName;
    }
}
