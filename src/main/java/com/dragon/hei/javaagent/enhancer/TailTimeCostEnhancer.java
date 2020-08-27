package com.dragon.hei.javaagent.enhancer;


import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.security.ProtectionDomain;

public class TailTimeCostEnhancer implements ClassEnhancer {

    @Override
    public byte[] enhance(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) {
        if(!"com/dragon/hei/wsth/promote/aviator/rule/DefaultRuleChain".equals(className)){
            return null;
        }
        try {
            final ClassPool classPool = ClassPool.getDefault();
            final CtClass clazz = classPool.get("com/dragon/hei/wsth/promote/aviator/rule/DefaultRuleChain");
            CtMethod method = clazz.getDeclaredMethod("matchRule");

            System.out.println("TimeCostEnhancer:原类的method：" + method.getName());
            method.insertAfter("System.out.println(\"TimeCostEnhancer：修改DefaultRuleChain：尾行打印结束时间：\" + System.currentTimeMillis());");
            byte[] bytes = clazz.toBytecode();
            clazz.detach();

            return bytes;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
