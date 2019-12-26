package com.pracitve.parker.practivezookeeperadvanced.lock.watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created Date by 2019/12/26 0026.
 *
 * @author Parker
 */
public class CallBackWatcher implements Watcher, AsyncCallback.StringCallback, AsyncCallback.ChildrenCallback, AsyncCallback.StatCallback {

    private ZooKeeper zk;
    private String lockName;
    private String threadName;
    private String pathName;
    private CountDownLatch latch = new CountDownLatch(1);

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * 尝试锁
     */
    public void tryLock(){

        try {
            System.out.println(threadName+" 尝试锁... ");
            zk.create("/"+lockName,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL,this,"ctx");
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放锁
     */
    public void unLock(){
        try {
            zk.delete(pathName,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }finally {
            latch.countDown();
        }

    }

    /**
     * exits watcher 观察
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/",false,this,"ctx");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    /**
     * create 回调
     * @param i
     * @param s
     * @param o
     * @param name
     */
    @Override
    public void processResult(int i, String s, Object o, String name) {
        if(name != null){
            pathName = name;
            zk.getChildren("/",false,this,"ctx");
        }
    }

    /**
     * getChildren 回调
     * @param i
     * @param s
     * @param o
     * @param list
     */
    @Override
    public void processResult(int i, String s, Object o, List<String> list) {
        if(list != null && !list.isEmpty()){

            Map<String,Integer> map = new HashMap<>(list.size());

            // 顺序
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return Integer.compare(Integer.parseInt(o1.replace(lockName,"")),Integer.parseInt(o2.replace(lockName,"")));

                }
            });
            for (int j = 0; j < list.size(); j++) {
                map.put(list.get(j),j);
            }

            Integer num = map.get(pathName.replace("/",""));
            if(num == 0){
                System.out.println(threadName+" "+pathName+" i am first");
                try {
                    zk.setData("/",threadName.getBytes(),-1);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }else{
                zk.exists("/"+list.get(num-1),this,this,"ctx");
            }
        }
    }

    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        // 暂无处理
    }
}
