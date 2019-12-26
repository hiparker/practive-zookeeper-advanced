package com.pracitve.parker.practivezookeeperadvanced.lock.conf;

import lombok.Data;
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
        this.zookeeperAddress = "192.0.0.109:2181,192.0.0.110:2181,192.0.0.111:2181";
        this.zookeeperSessionTimeout = 300000;
        this.zookeeperName = "lock";
    }

    public ZkConf(String zookeeperAddress, Integer zookeeperSessionTimeout, String zookeeperName) {
        this.zookeeperAddress = zookeeperAddress;
        this.zookeeperSessionTimeout = zookeeperSessionTimeout;
        this.zookeeperName = zookeeperName;
    }
}
