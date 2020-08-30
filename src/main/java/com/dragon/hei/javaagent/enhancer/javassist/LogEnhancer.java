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
        FileUtil.createClassFile("DefaultRuleChain-0", classfileBuffer);
        CtMethod oldMethod = clazz.getDeclaredMethod("matchRule");

        String oldMethodName = oldMethod.getName();
        String newMethodName = oldMethodName + "$impl_log";

        CtMethod newMethod = CtNewMethod.copy(oldMethod, oldMethodName, clazz, null);
        oldMethod.setName(newMethodName);

        //TODO：打印参数对象，需要获取本地变量参数名
        String paramNames = getMethodParamNames(newMethod);

        StringBuilder newMethodBody = new StringBuilder();
        newMethodBody.append("{\n");
                //.append("\nSystem.out.println(\" param:\" + $1 + \", \" + $2);")
        newMethodBody.append("\nSystem.out.println(\"[Function][Begin] " + className + "."+ oldMethodName + " Param: \" + $1 + \", \" + $2);\n");
        newMethodBody.append("Object $$result = " + newMethodName + "($$);");
        if(CtClass.voidType != newMethod.getReturnType()) {
            newMethodBody.append("\nSystem.out.println(\"[Function][End] " + className + "." + oldMethodName + " Return: \" + $$result);");
        }else{
            newMethodBody.append("\nSystem.out.println(\"[Function][End] " + className + "." + oldMethodName + "\");");
        }
        newMethodBody.append("\nreturn $$result;");
        newMethodBody.append("}");
        System.out.println("返回值类型：：："+newMethod.getReturnType().getName());
        newMethod.setBody(newMethodBody.toString());

        clazz.addMethod(newMethod);
        classfileBuffer = clazz.toBytecode();
        FileUtil.createClassFile("DefaultRuleChain-1", classfileBuffer);
        return classfileBuffer;
    }

    private String getMethodParamNames(CtMethod method){
        StringBuilder paramNames = new StringBuilder();
        try {
            MethodInfo methodInfo = method.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            System.out.println(attribute);

            CtClass[] paramTypes = method.getParameterTypes();


            int pos = Modifier.isStatic(method.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramTypes.length; i++) {
                System.out.println("mmmmname"+paramTypes[i].getName());
                String paramName = attribute.variableName(i + pos);
                if(null != paramName){
                    paramNames.append(paramName + ".toString()");
                }
                if (i < paramTypes.length - 1) {
                    paramNames.append(",");
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
        System.out.println(paramNames);
        return paramNames.toString();
    }
}
