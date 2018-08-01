/**
 * @brief 【】
 *
 * @<b>文件名</b>    : ReflectUtils
 * @n@n<b>作  者</b>  : 陈希
 * @n@n<b>创建时间</b>: 2012-7-11 上午12:17:05 
 */
package com.nd.weather.widget;

import java.lang.reflect.Method;

public class ReflectUtils
{
	public static Method getMethod(Class<?> clazz, String methodName, final Class<?>[] classes)
			throws Exception {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException e) {
			try {
				method = clazz.getMethod(methodName, classes);
			} catch (NoSuchMethodException ex) {
				if (clazz.getSuperclass() == null) {
					return method;
				} else {
					method = getMethod(clazz.getSuperclass(), methodName, classes);
				}
			}
		}
		return method;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	public final static Object invokeMethod(Object owner, String methodName, Class<?>[] argsClass,
			Object[] args) throws Exception {

		Class<?> ownerClass = owner.getClass();
		Method method = ownerClass.getMethod(methodName, argsClass);
		method.setAccessible(true);
		return method.invoke(owner, args);
	}
}
