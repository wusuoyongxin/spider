package com.wusyx.spider.teach;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.*;

/**
 * Created by Wu on 2016-1-14-0014.
 */
public class TestVectorException {

    static volatile List list = new CopyOnWriteArrayList();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        Thread thread1 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = list.iterator();
                while(iterator.hasNext()) {
                    Integer integer = iterator.next();
                    System.out.println(Thread.currentThread().getName() + ",读取:" + integer);
                }
            };
        };

        Thread thread2 = new Thread(){
            public void run() {
                Iterator<Integer> iterator = list.iterator();
                while(iterator.hasNext()){
                    Integer integer = iterator.next();
                    if(integer==2){
                        list.remove(integer);
                        System.out.println(Thread.currentThread().getName() + ",remove:" + integer);
                    }
                }

            };
        };

        thread2.start();
        thread1.start();
    }
}
