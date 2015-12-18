package com.wusyx.spider.teach;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.HashSet;

/**
 * Created by xiao on 2015/12/16.
 */
public class GuavaTest {

    @Test
    public void hashSetTest(){
        // 集合的合集，交集，差集
        HashSet<Integer> setA = Sets.newHashSet();
        HashSet<Integer> setB = Sets.newHashSet(4, 5, 6, 7, 8);

        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println("union:");
        for (Integer integer : union)
            System.out.println(integer);

        Sets.SetView<Integer> difference = Sets.difference(setA, setB);
        System.out.println("difference:");
        for (Integer integer : difference)
            System.out.println(integer);

        Sets.SetView<Integer> difference1 = Sets.difference(setB, setA);
        System.out.println("difference1:");
        for (Integer integer : difference1)
            System.out.println(integer);

        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        System.out.println("intersection:");
        for (Integer integer : intersection)
            System.out.println(integer);
    }
}
