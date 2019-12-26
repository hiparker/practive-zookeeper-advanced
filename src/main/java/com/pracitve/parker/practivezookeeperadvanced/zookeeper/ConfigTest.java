package com.pracitve.parker.practivezookeeperadvanced.zookeeper;

import com.pracitve.parker.practivezookeeperadvanced.PractiveZookeeperAdvancedApplication;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.conf.ZkConf;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.entity.ZkData;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.util.ZkMain;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.util.ZkUtil;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.watcher.CallBackWatcher;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.concurrent.CountDownLatch;


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes={PractiveZookeeperAdvancedApplication.class})
@RunWith(JUnit4.class)
public class ConfigTest {

    private static ZkConf zkConf = new ZkConf();

    private static ZooKeeper zk;


    @Before
    public void before(){
        zk = new ZkMain().getZk(zkConf.getZookeeperName());
    }

    @After
    public void after(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void main() {
        CallBackWatcher callBackWatcher = new CallBackWatcher();
        ZkData zkData = callBackWatcher.await("xxoo",zk);
        while (true){

            if(StringUtils.isEmpty(zkData.getValue())){
                System.out.println("zkData undefined ......");
                zkData = callBackWatcher.await("xxoo",zk);
            }else{
                System.out.println(zkData.getValue());
            }

            // 睡 200 毫秒
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
