package com.pracitve.parker.practivezookeeperadvanced.lock.thread;

import com.pracitve.parker.practivezookeeperadvanced.lock.watcher.CallBackWatcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created Date by 2019/12/26 0026.
 *
 * @author Parker
 */
public class Run implements Runnable{

    private String name;
    private ZooKeeper zk;
    private List<String> okNames = new ArrayList<>();
    private List<String> errorNames = new ArrayList<>();
    private AtomicInteger atomicInteger;

    public ZooKeeper getZk() {
        return zk;
    }

    public Run setZk(ZooKeeper zk) {
        this.zk = zk;
        return this;
    }

    public List<String> getOkNames() {
        return okNames;
    }

    public Run setOkNames(List<String> okNames) {
        this.okNames = okNames;
        return this;
    }

    public List<String> getErrorNames() {
        return errorNames;
    }

    public Run setErrorNames(List<String> errorNames) {
        this.errorNames = errorNames;
        return this;
    }

    public AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public Run setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
        return this;
    }

    public Run setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        CallBackWatcher lock = new CallBackWatcher();
        lock.setLockName("lock");
        lock.setThreadName(name);
        lock.setZk(zk);

        try {
            // 尝试锁
            lock.tryLock();

            // 工作

            // 如果 秒杀数 >= 0 则可以继续秒杀
            if(atomicInteger.decrementAndGet() >= 0){

                // 如果该线程 已秒杀成功 则不允许第二次秒杀
                if(okNames.indexOf(lock.getThreadName()) == -1){
                    okNames.add(lock.getThreadName());
                }else{
                    errorNames.add(lock.getThreadName());
                }

            }else{
                errorNames.add(lock.getThreadName());
            }

            // 睡 20毫秒 模仿真实环境
            Thread.sleep(20);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            // 释放锁
            lock.unLock();
        }

    }


}
