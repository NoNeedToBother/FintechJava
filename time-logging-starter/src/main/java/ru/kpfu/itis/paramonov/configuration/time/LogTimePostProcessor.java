package ru.kpfu.itis.paramonov.configuration.time;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LogTimePostProcessor implements BeanPostProcessor {

    private final Map<String, Class<?>> beans = new HashMap<>();

    private final Map<String, List<Method>> methods = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        if (clazz.isAnnotationPresent(LogTime.class)) {
            beans.put(beanName, clazz);
        }
        else {
            List<Method> annotatedMethods = Arrays.stream(clazz.getMethods())
                    .filter(method -> method.isAnnotationPresent(LogTime.class))
                    .toList();
            if (!annotatedMethods.isEmpty()) {
                methods.put(beanName, annotatedMethods);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beans.get(beanName) != null) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice((MethodInterceptor) this::handleMethodTimeLogging);
            return proxyFactory.getProxy();
        } else if (methods.containsKey(beanName)) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice((MethodInterceptor) invocation -> {
                Method thisMethod = invocation.getMethod();
                for (Method method : methods.get(beanName)) {
                    if (checkMethodSignatureIdentity(method, thisMethod)) {
                        return handleMethodTimeLogging(invocation);
                    }
                }
                return invocation.proceed();
            });
            return proxyFactory.getProxy();
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    private Object handleMethodTimeLogging(MethodInvocation invocation) throws Throwable {
        String name = invocation.getMethod().getName();
        Class<?> clazz = invocation.getMethod().getDeclaringClass();
        Long start = System.nanoTime();
        Object result = invocation.proceed();
        Long finish = System.nanoTime();
        log.info("Method {} from class {} was invoked, execution time: {} ns", name, clazz.getCanonicalName(), finish - start);
        return result;
    }

    private boolean checkMethodSignatureIdentity(Method method1, Method method2) {
        if (!method1.getName().equals(method2.getName())) {
            return false;
        }
        Class<?>[] params1 = method1.getParameterTypes();
        Class<?>[] params2 = method2.getParameterTypes();
        return Arrays.equals(params1, params2);
    }
}
