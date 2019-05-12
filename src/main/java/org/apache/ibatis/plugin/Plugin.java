/**
 *    Copyright 2009-2019 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.reflection.ExceptionUtil;

/**
 * @author Clinton Begin
 */
public class Plugin implements InvocationHandler {

  private final Object target;
  private final Interceptor interceptor;
  private final Map<Class<?>, Set<Method>> signatureMap;

  private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
    this.target = target;
    this.interceptor = interceptor;
    this.signatureMap = signatureMap;
  }

  /**
   * 生成目标对象的代理对象
   * @param target      目标对象
   * @param interceptor 拦截器
   * @return
   */
  public static Object wrap(Object target, Interceptor interceptor) {
    //获取Interceptor上定义的所有方法签名
    Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
    //获取被代理对象的类
    Class<?> type = target.getClass();
    //得到所有被注解的接口
    Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
    //有被@Intercepts注解中定义的接口，那么为之生成代理
    if (interfaces.length > 0) {
      return Proxy.newProxyInstance(
          type.getClassLoader(),
          interfaces,
          new Plugin(target, interceptor, signatureMap));
    }
    //不存在被拦截的类 返回原Target。
    return target;
  }

  /**
   * 使用JDBC的动态代理 为目标接口生成代理之后，最终执行的都是invoke方法
   * @param proxy
   * @param method
   * @param args
   * @return
   * @throws Throwable
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      // 通过method对应的Class，获取该Class中有哪些方法签名
      Set<Method> methods = signatureMap.get(method.getDeclaringClass());
      // 判断是否存在签名的拦截方法
      if (methods != null && methods.contains(method)) {
        //存在签名的拦截方法，插件的intercept方法被调用，然后返回结果
        //这里创建了一个 Invocation 对象
        return interceptor.intercept(new Invocation(target, method, args));
      }
      // 不存在，通过反射调用要执行的方法
      return method.invoke(target, args);
    } catch (Exception e) {
      throw ExceptionUtil.unwrapThrowable(e);
    }
  }

  private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
    //获取@Intercepts注解
    Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
    // 判断是否获取到@Intercepts注解
    if (interceptsAnnotation == null) {
      //没有拿到@Intercepts注解 抛出异常
      throw new PluginException("No @Intercepts annotation was found in interceptor " + interceptor.getClass().getName());
    }
    //获取@Intercepts注解下的所有@Signature注解
    Signature[] sigs = interceptsAnnotation.value();
    Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
    for (Signature sig : sigs) {
      //获取其type属性（表示具体某个接口）
      //若sig.type()对应的value为空，会将第二个参数的返回值存入并返回
      Set<Method> methods = signatureMap.computeIfAbsent(sig.type(), k -> new HashSet<>());
      try {
        //再根据method与args两个属性去type下找方法签名一致的方法
        Method method = sig.type().getMethod(sig.method(), sig.args());
        // 将method添加到 Set<Method> 中
        methods.add(method);
      } catch (NoSuchMethodException e) {
        //没有方法签名一致的就抛出异常，此签名的方法在该接口下找不到
        throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method() + ". Cause: " + e, e);
      }
    }
    return signatureMap;
  }

  private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
    // 用来存储所有的签名接口
    Set<Class<?>> interfaces = new HashSet<>();
    while (type != null) {
      for (Class<?> c : type.getInterfaces()) {
        // 如果方法签名映射中有这个接口，添加到interfaces中
        if (signatureMap.containsKey(c)) {
          interfaces.add(c);
        }
      }
      type = type.getSuperclass();
    }
    // 转换成数组返回
    return interfaces.toArray(new Class<?>[interfaces.size()]);
  }

}
