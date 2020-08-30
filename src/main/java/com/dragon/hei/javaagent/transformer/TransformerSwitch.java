package com.dragon.hei.javaagent.transformer;

import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author: lilong
 **/
public class TransformerSwitch {

    private static final boolean switch_open = Boolean.TRUE;

    private static final List<String> packageScan = Arrays.asList("com/dragon/hei");

    /***
     *
     * @param className
     * @return true 需要转换；false 不需要
     */
    public static boolean transform(String className){

        if(!switch_open) {
            return false;
        }

        for (String pack : packageScan) {
            if (null != className && className.indexOf(pack) != -1) {
                return true;
            }
        }
        return false;
    }
}
