package com.dragon.hei.javaagent;


import com.dragon.hei.javaagent.enhancer.ClassEnhancer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyClassTransformer implements ClassFileTransformer {


    private List<ClassEnhancer> enhancers;

    public MyClassTransformer(){
        this(null);
    }

    public MyClassTransformer(ClassEnhancer...enhancer){

        enhancers = null == enhancers ? new ArrayList<ClassEnhancer>() : enhancers;
        if(null != enhancer){
            enhancers.addAll(Arrays.asList(enhancer));
        }
    }

    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {

        if(!TransformerSwitch.transform(className)){
            return null;
        }

        for(ClassEnhancer enhancer : enhancers){
            try {
                classfileBuffer = enhancer.enhance(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            }catch (Throwable e){
                e.printStackTrace();
            }
        }

        return classfileBuffer;
    }

}
