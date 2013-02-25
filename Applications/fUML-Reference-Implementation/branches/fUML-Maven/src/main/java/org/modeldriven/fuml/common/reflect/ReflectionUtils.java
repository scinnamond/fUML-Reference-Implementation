package org.modeldriven.fuml.common.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReflectionUtils {
    private static Log log = LogFactory.getLog(ReflectionUtils.class);

    public static Method findMethod(Class<?> target, String name, Class<?> arg) throws NoSuchMethodException {
        return findMethod(target.getMethods(), name, arg);
    }

    public static Method getMethod(Class<?> target, String name, Class<?> arg) throws NoSuchMethodException {
        Method method = findMethod(target.getMethods(), name, arg);
        if (method == null)
            throw new NoSuchMethodException(target.getName() + "." + name);
        return method;
    }

    public static Method findMethod(Method[] methods, String name, Class<?> arg) {
        for (int i = 0; i < methods.length; i++) {
            if (!methods[i].getName().equals(name))
                continue;
            Class<?>[] types = methods[i].getParameterTypes();
            if (types != null && types.length == 1) {
                if (types[0].isAssignableFrom(arg)) {
                    return methods[i];
                }
                else if (types[0].isPrimitive() || arg.isPrimitive()) {
                	// accommodate conditions where code generators use either primitive type
                	// wrapper classes or primitive types themselves
                	// rather than their respective wrapper classes in the generated code.
                	// This may be for efficiency or other reasons. Yet the source UML model uses UML
                	// primitive types which are [Integer,String,Boolean,...] and don't of course
                	// specify any notion of wrapper classes or otherwise. Code generators
                	// should therefore be allowed to interpret the UML types as they wish. When 
                	// determining a method signature isAssignableFrom above will fail when comparing
                	// primitive type classes and their respective wrapper, so below these are
                	// manually compared. 
                	if ((Integer.class.equals(types[0]) && int.class.equals(arg)) ||
                		(int.class.equals(types[0]) && Integer.class.equals(arg))) {
                		return methods[i];
                	}
                	else if ((Boolean.class.equals(types[0]) && boolean.class.equals(arg)) ||
                    	(boolean.class.equals(types[0]) && Boolean.class.equals(arg))) {
                    	return methods[i];
                    }
                }
            }
        }
        return null;
    }
    
    public static Object instanceForName(String qualifiedName) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException,
            InstantiationException {
    	Object[] args = new Object[0];
    	Class<?>[] types = new Class<?>[0];
        return instanceForName(qualifiedName, args, types);
    }

    public static Object instanceForName(String qualifiedName, Object[] args, Class<?>[] types) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException,
            InstantiationException {
        Class<?> targetClass = Class.forName(qualifiedName);
        int mods = targetClass.getModifiers();
        if (Modifier.isAbstract(mods))
            throw new RuntimeException("attempt to instantiate abstract class, " + qualifiedName);
        if (!Modifier.isPublic(mods))
            throw new RuntimeException("attempt to instantiate non-public class, " + qualifiedName);
        Constructor<?> defaultConstructor = targetClass.getConstructor(types);
        Object object = defaultConstructor.newInstance(args);
        return object;
    }
    
    public static Object invokePublicGetterOrField(Object target, String propertyName) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
	    String methodName = "get" + propertyName.substring(0, 1).toUpperCase()
	        + propertyName.substring(1);
		Object[] args = { };
		try {
		    Method getter = target.getClass().getMethod(methodName, new Class[] { });
		    return getter.invoke(target, args);
		} catch (NoSuchMethodException e) {
		    try {
		        Field field = target.getClass().getField(propertyName);
		        field.get(target);
		    } catch (NoSuchFieldException e2) {
		        String msg = "no (" + target.getClass().getName()
	                + ") getter method named '" + methodName 
	                + "' or public field found for primitive feature " 
	                + target.getClass().getName() + "."
	                + propertyName;
		        log.warn(msg);
		    }
		}
		return null;
	}
    
    
    public static void invokePublicSetterOrField(Object target, String propertyName, Class<?> javaType, Object value) 
        throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        String methodName = "set" + propertyName.substring(0, 1).toUpperCase()
            + propertyName.substring(1);
		Object[] args = { value };
		try {
		    Method setter = target.getClass().getMethod(methodName, new Class[] { javaType });
		    setter.invoke(target, args);
		} catch (NoSuchMethodException e) {
		    try {
		        Field field = target.getClass().getField(propertyName);
		        field.set(target, value);
		    } catch (NoSuchFieldException e2) {
		        String msg = "no (" + target.getClass().getName()
		                + ") setter method named '"+methodName+"' or public field found for primitive feature " + "<"
		                + javaType.getName() + "> " + target.getClass().getName() + "."
		                + propertyName;
		        log.warn(msg);
		    }
		}
    }
}
