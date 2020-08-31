package com.dragon.hei.javaagent.enhancer.asm;

import com.dragon.hei.javaagent.enhancer.ClassEnhancer;

import java.security.ProtectionDomain;

public class ASMClassEnhancer implements ClassEnhancer {

    @Override
    public byte[] enhance(ClassLoader loader,
                          String className,
                          Class<?> classBeingRedefined,
                          ProtectionDomain protectionDomain,
                          byte[] classfileBuffer) throws Throwable {
        return new byte[0];
    }
}
