package com.pracitve.parker.practivezookeeperadvanced.zookeeper.watcher;

import com.pracitve.parker.practivezookeeperadvanced.zookeeper.entity.ZkData;
import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * 获取值 watcher
 * @date 2019年12月22日16:59:14
 * @author parker
 */
@Data
@Log4j
public class CallBackWatcher implements Watcher, AsyncCallback.StatCallback, AsyncCallback.DataCallback {

    private ZooKeeper zk;
    private String path;
    private ZkData zkData;
    private CountDownLatch latch = new CountDownLatch(1);

    /**
     * 任务锁
     */
    public ZkData await(String path){
        ZkData zkData = new ZkData();
        this.setPath(path);
        this.setLatch(latch);
        this.setZkData(zkData);
        this.setZk(zk);
        zk.exists("/"+path,this,this,"Exists");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkData;
    }

    /**
     * 任务锁
     */
    public ZkData await(String path,ZooKeeper zk){
        ZkData zkData = new ZkData();
        this.setPath(path);
        this.setLatch(latch);
        this.setZkData(zkData);
        this.setZk(zk);
        zk.exists("/"+path,this,this,"Exists");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkData;
    }

    /**
     * DataCallback
     * @param i
     * @param s
     * @param o
     * @param bytes
     * @param stat
     */
    @Override
    public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
        if(bytes != null){
            zkData.setValue(new String(bytes));
            latch.countDown();
        }
    }

    /**
     * StatCallback
     * @param i
     * @param s
     * @param o
     * @param stat
     */
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if(stat != null){
            zk.getData("/"+path,this,this,"StatCallback");
        }
    }

    /**
     * watcher
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                zk.getData("/"+path,this,this,"StatCallback");
                break;
            case NodeDeleted:
                zkData.setValue(null);
                latch= new CountDownLatch(1);
                break;
            case NodeDataChanged:
                zk.getData("/"+path,this,this,"StatCallback");
                break;
            case NodeChildrenChanged:
                break;
        }

    }


}
