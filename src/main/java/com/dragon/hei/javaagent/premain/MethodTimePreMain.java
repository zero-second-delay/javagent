package com.dragon.hei.javaagent.premain;

import com.dragon.hei.javaagent.enhancer.ClassEnhancer;
import com.dragon.hei.javaagent.enhancer.javassist.LogEnhancer;
import com.dragon.hei.javaagent.enhancer.javassist.TimeCostEnhancer;
import com.dragon.hei.javaagent.transformer.MyClassTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class MethodTimePreMain {

    public static void premain(String agentArgs, Instrumentation inst) {

        //System.out.println("自定义premain：" + agentArgs);

        inst.addTransformer(myTransformer(), true);
    }

    private static ClassFileTransformer myTransformer(){

        ClassEnhancer timeCostEnhancer = new TimeCostEnhancer();
        ClassEnhancer logEnhancer = new LogEnhancer();
        return new MyClassTransformer(logEnhancer, timeCostEnhancer);
    }
}
