package com.pracitve.parker.practivezookeeperadvanced.zookeeper.util;

import com.pracitve.parker.practivezookeeperadvanced.zookeeper.conf.ZkConf;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.watcher.DefaultWatcher;
import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper 主注册方法
 * @date 2019年12月22日15:52:48
 * @author parker
 */
@Log4j
public class ZkMain {

    //@Autowired
    private static ZkConf zkConf = new ZkConf();

    private static ZooKeeper zk;

    private static CountDownLatch latch = new CountDownLatch(1);

    private static DefaultWatcher defaultWatcher = new DefaultWatcher(latch);

    public  ZooKeeper getZk(){
        try {
            zk = new ZooKeeper(zkConf.getZookeeperAddress(),zkConf.getZookeeperSessionTimeout(),defaultWatcher);
            latch.await();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return zk;
    }

    public  ZooKeeper getZk(String name){
        try {
            zk = new ZooKeeper(zkConf.getZookeeperAddress()+"/"+name,zkConf.getZookeeperSessionTimeout(),defaultWatcher);
            latch.await();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return zk;
    }



}
