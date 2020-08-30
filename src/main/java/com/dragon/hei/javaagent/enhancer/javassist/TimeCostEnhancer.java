package com.dragon.hei.javaagent.enhancer.javassist;


import com.dragon.hei.javaagent.utils.FileUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

public class TimeCostEnhancer extends JavassistClassEnhancer {

    @Override
    protected boolean transformSwitch(String className){
        if(!"com/dragon/hei/wsth/promote/aviator/rule/DefaultRuleChain".equals(className)){
            return false;
        }
        return true;
    }

    @Override
    protected byte[] transform(String className, byte[] classfileBuffer, CtClass clazz, ClassPool classPool) throws Throwable {
        FileUtil.createClassFile("DefaultRuleChain-2", classfileBuffer);
        CtMethod oldMethod = clazz.getDeclaredMethod("matchRule");

        String oldMethodName = oldMethod.getName();
        String newMethodName = oldMethod.getName() + "$impl_timeCost";
        oldMethod.setName(newMethodName);

        CtMethod newMethod = CtNewMethod.copy(oldMethod, oldMethodName, clazz, null);

        StringBuilder newMethodBody = new StringBuilder();
        newMethodBody.append("{")
                .append("long startTime_$$ = System.currentTimeMillis();\n")
                //.append("Object $$result = " + newMethodName + "($$);\n")
                .append(newMethodName + "($$);\n")
                .append("System.out.println(\"[ExecuteTime] " + className + "." +oldMethodName + " cost \" + (System.currentTimeMillis()-startTime_$$) + \" ms\");")
                //.append("\nreturn $$result;")
                .append("}");

        newMethod.setBody(newMethodBody.toString());
        clazz.addMethod(newMethod);
        classfileBuffer = clazz.toBytecode();
        FileUtil.createClassFile("DefaultRuleChain-3", classfileBuffer);
        return classfileBuffer;
    }
}
