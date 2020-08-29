package com.dragon.hei.javaagent.enhancer.javassist;


import com.dragon.hei.javaagent.FileUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class LogEnhancer extends JavassistClassEnhancer {

    @Override
    protected boolean transformSwitch(String className){
        if(!"com/dragon/hei/wsth/promote/aviator/rule/DefaultRuleChain".equals(className)){
            return false;
        }
        return true;
    }

    @Override
    protected byte[] transform(String className, byte[] classfileBuffer, CtClass clazz, ClassPool classPool) throws Throwable {
        FileUtil.createClassFile("DefaultRuleChain2", classfileBuffer);
        CtMethod oldMethod = clazz.getDeclaredMethod("matchRule");

        String oldMethodName = oldMethod.getName();
        String newMethodName = oldMethodName + "$impl2";
        CtMethod newMethod = CtNewMethod.copy(oldMethod, oldMethodName, clazz, null);
        oldMethod.setName(newMethodName);

        //TODO：打印参数对象，需要获取本地变量参数名
        MethodInfo methodInfo = newMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        System.out.println(attribute);
        int pos = Modifier.isStatic(newMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < newMethod.getParameterTypes().length; i++) {
            System.out.print(attribute.variableName(i + pos));
            if (i < newMethod.getParameterTypes().length - 1) {
                System.out.print(",");
            }
        }

        StringBuilder newMethodBody = new StringBuilder();
        newMethodBody.append("{\n")
                .append("\nSystem.out.println(\"[Function][Begin] " + className +"."+oldMethodName+"$$"+"\");\n")
                .append(newMethodName + "($$);")
                .append("\nSystem.out.println(\"[Function][End] " + className +"."+ oldMethodName + "\");")
                .append("}");

        newMethod.setBody(newMethodBody.toString());
        clazz.addMethod(newMethod);
        classfileBuffer = clazz.toBytecode();
        FileUtil.createClassFile("DefaultRuleChain3", classfileBuffer);
        return classfileBuffer;
    }
}
