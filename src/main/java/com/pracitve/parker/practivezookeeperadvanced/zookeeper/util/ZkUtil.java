package com.pracitve.parker.practivezookeeperadvanced.zookeeper.util;

import com.pracitve.parker.practivezookeeperadvanced.zookeeper.conf.ZkConf;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.entity.ZkData;
import com.pracitve.parker.practivezookeeperadvanced.zookeeper.watcher.CallBackWatcher;
import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * zookeeper 工具类
 * @date 2019年12月22日16:10:06
 * @author parker
 */
@Log4j
public class ZkUtil {

    //@Autowired
    private static ZkConf zkConf = new ZkConf();

    private static ZooKeeper zk;

    public  void before(){
        zk = new ZkMain().getZk();
        CountDownLatch latchT = new CountDownLatch(1);
        try {
            // 如果 zk 没有 当前 zookeeper 名字 则创建
            zk.exists("/"+zkConf.getZookeeperName(),new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    // 如果 zk 路劲不存在 则自动创建一个
                    try {
                        String path = watchedEvent.getPath();
                        if(StringUtils.isEmpty(path)){
                            zk.create("/"+zkConf.getZookeeperName(), "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                        }
                        //关闭当先zk
                        zk.close();
                        //获取新路劲下zk
                        zk = new ZkMain().getZk(zkConf.getZookeeperName());
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        latchT.countDown();
                    }
                }
            });
            latchT.await();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void after(){
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    /**
     * 新增数据
     * @param path
     * @param data
     * @param acl
     * @param createMode
     * @return
     * @throws Exception
     */
    public String create(String path, byte[] data, List<ACL > acl, CreateMode createMode) throws Exception{
        return zk.create(path,data, acl, createMode);
    }

    /**
     * 新增数据
     * @param path
     * @param data
     * @param acl
     * @param createMode
     * @param cb
     * @param ctx
     * @throws Exception
     */
    public void create(String path, byte[] data, List<ACL> acl, CreateMode createMode, AsyncCallback.StringCallback cb, Object ctx) throws Exception{
        zk.create(path,data, acl, createMode,cb,ctx);
    }

    /**
     * 修改数据
     * @param path
     * @param data
     * @param version
     * @return
     * @throws Exception
     */
    public Stat setData(String path, byte[] data, int version) throws Exception{
        return zk.setData(path,data,version);
    }

    /**
     * 修改数据
     * @param path
     * @param data
     * @param version
     * @param cb
     * @param ctx
     * @throws Exception
     */
    public void setData(String path, byte[] data, int version, AsyncCallback.StatCallback cb, Object ctx) throws Exception{
        zk.setData(path,data,version,cb,ctx);
    }




}
