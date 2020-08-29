package com.dragon.hei.javaagent.enhancer.javassist;

import com.dragon.hei.javaagent.enhancer.ClassEnhancer;
import javassist.ClassPool;
import javassist.CtClass;

import java.security.ProtectionDomain;

public abstract class JavassistClassEnhancer implements ClassEnhancer {

    @Override
    public byte[] enhance(ClassLoader loader,
                   String className,
                   Class<?> classBeingRedefined,
                   ProtectionDomain protectionDomain,
                   byte[] classfileBuffer) throws Throwable{

        if(!transformSwitch(className)){
            return null;
        }

        className = className.replace("/", ".");
        ClassPool classPool = ClassPool.getDefault();
        CtClass clazz = classPool.get(className);
        if(clazz.isFrozen()){
            // 解冻，防止其他增强器报错"class is frozen"
            clazz.defrost();
        }

        return transform(className, classfileBuffer, clazz, classPool);
    }

    /***
     * 开关
     * @param className
     * @return
     */
    protected abstract boolean transformSwitch(String className);

    /***
     * 子类覆盖的核心方法
     * <p>注意：子类尽量不要使用CtClass.detach否则会导致后面的增强器丢失之前已增强的特性</p>
     * @param className
     * @param clazz
     * @param classPool
     * @return
     * @throws Throwable
     */
    protected abstract byte[] transform(String className, byte[] classfileBuffer, CtClass clazz, ClassPool classPool) throws Throwable;
}