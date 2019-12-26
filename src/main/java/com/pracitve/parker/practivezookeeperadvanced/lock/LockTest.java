package com.pracitve.parker.practivezookeeperadvanced.lock;

import com.pracitve.parker.practivezookeeperadvanced.lock.conf.ZkConf;
import com.pracitve.parker.practivezookeeperadvanced.lock.thread.Run;
import com.pracitve.parker.practivezookeeperadvanced.lock.threadpool.NameableThreadFactory;
import com.pracitve.parker.practivezookeeperadvanced.lock.util.ZkMain;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(JUnit4.class)
public class LockTest {

    private AtomicInteger integer;

    private static ZkConf zkConf = new ZkConf();

    private static ZooKeeper zk;

    private static String[] user = {
            "雍山梅","茹文瑶","郁茵茵","白紫山","宁北辰","庄菲菲","居南晴","李玉华","孙淼淼","马妙海","菱含雁","乌思宸","满若云","璩微熹","通思懿","逯灵慧","辛梓馨","糜绢子","晏雅洁","郁思雅","宓洁静","郏荌荌","訾暖暖","万川暮","游梦桃","濮启颜","茹忆寒","庄北晶","后芷文","孟曦秀","瞿依瑶","徐梦琪","阎迎荷","晏妙竹","任子宁","能雨柏","那优悦","菱雅隽","茹锦洁","孔菱凡","陆天晴","易淑婉","宋水格","阙笑雯","简优美","勾书文","邵雁丝","弘水彤","国丝柳","耿芮欢","薛之槐","宫又菡","牛湛娟","江灵波","史好慕","方雅安","寿听寒","訾仪琳","贺雪枫","吕妍茜","扶寻菡","劳月华","唐白曼","籍玲丽","桂珺琪","辛玉颖","隆笑白","空婉清","惠月明","詹伊颜","叶晗晗","康煊悦","游小莉","何八雪","充恺玲","崔学英","宓一芳","于元风","宦玉瑾","郝瑞芝","熊采春","康晏然","敖宣蓉","伊芬馥","冀嘉颖","萧迷伤","乔瑜英","苏傲霜","闻心菱","侯颜英","谢芳懿","钱湘君","融美如","庄胜花","后米雪","符梦影","钱逸丽","康绪婷","弘桂芝","怀冬卉"
    };

    private List<String> okNames = new ArrayList<>();
    private List<String> errorNames = new ArrayList<>();


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
    public void lock(){

        // 设置秒杀数量 为 50件
        integer = new AtomicInteger(50);

        int listSize = 100;

        // 虚拟10000 用户强
        List<String> list = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            Random random = new Random();
            // 0-99
            int num = random.nextInt(100);

            Random random2 = new Random();
            // 0-99
            int num2 = random2.nextInt(1000);

            list.add(user[num]+"-"+num2);
        }

        System.out.println("人数："+list.size());

        ExecutorService executorService = Executors.newCachedThreadPool(new NameableThreadFactory("测试线程池"));

        // 创建 100 个线程 并且同步抢单
        for (int i = 0; i < list.size(); i++) {
            Run run = new Run();
            run.setZk(zk)
               .setName(list.get(i))
               .setAtomicInteger(integer)
               .setOkNames(okNames)
               .setErrorNames(errorNames);

            executorService.submit(run);
        }

        executorService.shutdown();

        // while循环 延长时间
        boolean flag = true;
        while (flag){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int ok = okNames.size();
            int error = errorNames.size();
            if((ok+error) == user.length){
                flag = false;
            }
        }


        System.out.println("抢购成功("+okNames.size()+")："+okNames.toString());
        System.out.println();
        System.out.println("抢购失败("+errorNames.size()+")"+errorNames.toString());


    }









}
