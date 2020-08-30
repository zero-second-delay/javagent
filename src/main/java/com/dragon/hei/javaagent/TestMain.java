package com.dragon.hei.javaagent;

import com.dragon.hei.javaagent.enhancer.ClassEnhancer;
import com.dragon.hei.javaagent.enhancer.javassist.LogEnhancer;
import com.dragon.hei.javaagent.enhancer.javassist.TimeCostEnhancer;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] args) {
//        getMethodParamNames();
        aa();

    }

    private static void aa(){
        try {
            List<ClassEnhancer> enhancers = new ArrayList<ClassEnhancer>();

            enhancers.add(new LogEnhancer());
            enhancers.add(new TimeCostEnhancer());

            String classname = MethodTimePreMain.class.getName();
            byte[] classBytes = ClassPool.getDefault().get(classname).toBytecode();
            for (ClassEnhancer enhancer : enhancers) {
                classBytes = enhancer.enhance(null, classname, null, null, classBytes);
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }


    private static String getMethodParamNames(){
        StringBuilder paramNames = new StringBuilder();
        try {
            ClassPool classPool = ClassPool.getDefault();
            Class clz = MethodTimePreMain.class;

            CtClass ctClass = classPool.get(clz.getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod("premain");

            MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            System.out.println(attribute);

            CtClass[] paramTypes = ctMethod.getParameterTypes();

            int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramTypes.length; i++) {
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
