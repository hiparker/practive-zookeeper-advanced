package com.pracitve.parker.practivezookeeperadvanced.lock.watcher;

import lombok.extern.log4j.Log4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * 默认 watcher
 * @date 2019年12月22日16:09:24
 * @author parker
 */
@Log4j
public class DefaultWatcher implements Watcher {

    private CountDownLatch latch;

    public DefaultWatcher() {
        super();
    }

    public DefaultWatcher(CountDownLatch latch) {
        super();
        this.latch = latch;
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        switch (watchedEvent.getState()) {
            case Unknown:
                break;
            case Disconnected:
                break;
            case NoSyncConnected:
                break;
            case SyncConnected:
                log.info(" zookeeper is start ...");
                latch.countDown();
                break;
            case AuthFailed:
                break;
            case ConnectedReadOnly:
                break;
            case SaslAuthenticated:
                break;
            case Expired:
                break;
        }

    }
}
