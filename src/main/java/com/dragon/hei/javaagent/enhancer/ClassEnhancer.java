package com.dragon.hei.javaagent.enhancer;

import java.security.ProtectionDomain;

public interface ClassEnhancer {
    byte[] enhance(ClassLoader loader,
                   String className,
                   Class<?> classBeingRedefined,
                   ProtectionDomain protectionDomain,
                   byte[] classfileBuffer) throws Exception;
}