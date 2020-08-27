package com.dragon.hei.javaagent.enhancer;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;
import java.util.Arrays;

public class TimeCostEnhancer implements ClassEnhancer {

    @Override
    public byte[] enhance(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws Exception{
        if(!"com/dragon/hei/wsth/promote/aviator/rule/DefaultRuleChain".equals(className)){
            System.out.println("TimeCostEnhancer：不匹配：" + className+"："+classBeingRedefined);
            return null;
        }
        className = className.replace("/", ".");
        System.out.println("TimeCostEnhancer：开始：" + className+"："+classBeingRedefined);

        ClassPool classPool = ClassPool.getDefault();
        System.out.println("TimeCostEnhancer：ClassPool：" + classPool);
        CtClass clazz = classPool.get(className);
        System.out.println("TimeCostEnhancer：CtClass：" + clazz);
        CtMethod method = clazz.getDeclaredMethod("matchRule");

        System.out.println("TimeCostEnhancer:原类的method：" + method.getName());
        method.insertBefore("System.out.println(\"TimeCostEnhancer：修改DefaultRuleChain：首行打印开始时间：\" + System.currentTimeMillis());");
        byte[] bytes = clazz.toBytecode();
        clazz.detach();
        return bytes;

    }
}
