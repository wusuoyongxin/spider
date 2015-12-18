package com.wusyx.spider.teach;

import org.junit.Test;


/**
 * Created by xiao on 2015/12/16.
 */
public class OtherTest {

    /**
     * 求素数
     */
    @Test
    public void  sushuTest (){
        for (int i = 2; i <= 200; i++) {
            int k = 0;
            for (int j = 2; j <= i/2; j++) {
                if (i % j == 0){
                    k++;
                    break;
                }
            }
            if (k == 0){
                System.out.println("素数："+ i);
            }
        }
    }

    @Test
    public void sushuTest2(){
        System.out.println(Math.sqrt(5));
        System.out.println(5/2);
    }
}
