package com.honvay.cola.cloud.framework.util;


import com.google.common.collect.Maps;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;

/**
 * ReflectionUtils:反射工具类.
 *
 * @author YRain
 */
public abstract class ReflectionUtils {

    public enum Accessible {
        yes, no
    }

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    public static String getParams(Method method, Object[] args) throws RuntimeException {
        Map<String, Object> params = Maps.newHashMap();
        String[] fieldNames = ReflectionUtils.getMethodParamNames(method);
        int i = 0;
        for (String fieldName : fieldNames) {
            params.put(fieldName, Objects.toString(args[i]));
            i++;
        }
        return params.toString();
    }

    public static String[] getMethodParamNames(Method method) throws RuntimeException {
        ClassPool classPool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(method.getDeclaringClass());
        classPool.insertClassPath(classPath);
        try {
            CtClass ctClass = classPool.get(method.getDeclaringClass().getName());
            CtMethod ctMethod = ctClass.getDeclaredMethod(method.getName(), classPool.get(getParamTypesNames(method.getParameterTypes())));
            return getMethodParamNames(ctMethod);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static String[] getParamTypesNames(Class<?>... paramTypes) {
        String[] paramTypeNames = new String[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypeNames[i] = paramTypes[i].getName();
        }
        return paramTypeNames;
    }


    private static String[] getMethodParamNames(CtMethod ctMethod) throws RuntimeException {
        CtClass ctClass = ctMethod.getDeclaringClass();
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new RuntimeException(ctClass.getName());
        }
        try {
            String[] paramNames = new String[ctMethod.getParameterTypes().length];
            int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = attr.variableName(i + pos);
            }
            return paramNames;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
