package com.dylan.boot.AOP;

import com.dylan.boot.IOC.BeanDefinition;
import com.dylan.boot.IOC.BeanPostProcessor;
import com.dylan.boot.IOC.Factory.BeanFactory;
import com.dylan.boot.IOC.Factory.BeanFactoryAware;
import com.dylan.boot.IOC.XML.XmlBeanFactory;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 确保在创建 AspectJAwareAdvisorAutoProxyCreator 对象时，正确设置了 BeanFactory，并且 setBeanFactory 方法被正确调用以将 xmlBeanFactory 初始化为正确的值。
 *
 * 如果您的代码逻辑是在 XmlBeanFactory 中创建 AspectJAwareAdvisorAutoProxyCreator 的实例，请确保在创建实例后立即调用 setBeanFactory 方法。
 *
 * 下面这段代码看起来像是一个用于处理 AspectJ 切面表达式的工具类，它可以根据给定的切面表达式来匹配类和方法。
 *
 * 这种工具在 AOP 框架中经常用于确定切入点（pointcut）和通知（advice）的匹配条件。
 */

public class AspectJAwareAdvisorAutoProxyCreator  implements BeanPostProcessor, BeanFactoryAware {
    private XmlBeanFactory xmlBeanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
//        System.out.println("--------------------------------AAAAPC.postPAI--------------------------------");
        System.out.println(bean.getClass());
        System.out.println(39);
        if (bean instanceof AspectJExpressionPointcutAdvisor) {
            return bean;
        }
//        if (bean instanceof MethodMatcher) {
//            return bean;
//        }
        /**
         * 这一行问题重点，重点，找了十年（LogInterceptor要跳过防止oom）
         */
        if(bean instanceof  MethodInterceptor){
            return bean;
        }
//------ 1.  从 BeanFactory 查找 AspectJExpressionPointcutAdvisor 类型的对象------------------------
//        System.out.println(xmlBeanFactory);
//        System.out.println(9000);
        /**
         *  获取这个 bean 的 advice
         *  Create proxy if we have this advice.
         */
        List<AspectJExpressionPointcutAdvisor> advisors = xmlBeanFactory.getBeansForType(AspectJExpressionPointcutAdvisor.class);
//        List<AspectJExpressionPointcutAdvisor> advisors = new ArrayList<>();
//        advisors.add(this);
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {

//            System.out.println("2302302032");
//            System.out.println(advisor.getPropertyValues().getPropertyValueList());
//            System.out.println(advisor.get());

//---------- 2. 使用 Pointcut 对象匹配matchers方法如果成功就切入当前 bean 对象------------------------------------------------
//            System.out.println(bean.getClass());
//            System.out.println(advisor.getClass());
//            输出： class com.dylan.boot.Model.HelloServiceImpl
//class com.dylan.boot.AOP.AspectJExpressionPointcutAdvisor

//            System.out.println(advisor.getPointcut().getClassFilter());
//            System.out.println(222);
//            输出：com.dylan.boot.AOP.AspectJExpressionPointcut@2e5d6d97
//                 222
            //Todo 现在先把结果搞出来，之后再去匹配expression
            //advisor.getPointcut()== 获得<property name="advice" ref="logInterceptor"/>，
            // getClassFilter().matchers== <property name="expression" value="execution(* com.dylan.boot.Model.*.sayHelloWorld(..))"/>
            // 连起来就是构造了 pointcut 的 Advise

            //根据切点表达式检查传递的类是否符合条件
            if (advisor.getPointcut().getClassFilter().matchers(bean.getClass())) {

//            if(true){
//                /**
//                 * spring 源码：
//                 *    Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
//                 *    proxyFactory.addAdvisors(advisors);
//                 *    proxyFactory.setTargetSource(targetSource);
//                 *    customizeProxyFactory(proxyFactory);
//                 */
////                System.out.println(advisor.getPointcut().getClassFilter().matchers(bean.getClass()));
//                System.out.println("AAAAPC 90");

                ProxyFactory proxyFactory = new ProxyFactory();
                TargetSource targetSource = new TargetSource(bean, bean.getClass(), bean.getClass().getInterfaces());


                //设置拦截器，这里的getAdvice应该得到<bean id="logInterceptor" class="com.dylan.boot.Model.LogInterceptor"/>
                proxyFactory.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());

                //这里应该是把上一步拿到的advice分配给method，如何分配就是pointcut的问题了（也就是expression）
                //之前有用过是判断advisor是不是符合targetsource，这里是为advice添加pointcut
                proxyFactory.setMethodMatcher((MethodMatcher) advisor.getPointcut());

//-------------- 3. 使用JdkDynamicAopProxy生成代理对象，并返回-----------------------------------------------
                proxyFactory.setTargetSource(targetSource);
                return proxyFactory.getProxy();

            }
        }
            return bean;
    }
//
//


// 失败方法尝试1：
//------------------------------------------------------------------------------------------------------------------------------------------------------------
//        AspectJExpressionPointcutAdvisor realAdvisor = null;
//
//        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
//
//
//        for (String name : xmlBeanFactory.getBeanDefinitionNames()) {
//
//            //这里有问题，每次gean的时候调用getAdvisor逻辑，就会导致循环，因此不能使用getBean，最好使用map
//            Map<String, BeanDefinition> bpMap = xmlBeanFactory.getBeanDefinitionMap();
//            BeanDefinition beanP = bpMap.get(name);
//
//            Object beanWillBeAdvisor = beanP.getBeanClass().newInstance();
////            System.out.println(beanP.getClass());
//            System.out.println(AopProxyUtils.ultimateTargetClass(beanWillBeAdvisor));
//            if(beanWillBeAdvisor instanceof AspectJExpressionPointcutAdvisor && targetClass.equals(AopProxyUtils.ultimateTargetClass(beanWillBeAdvisor))){
//                realAdvisor = (AspectJExpressionPointcutAdvisor) beanWillBeAdvisor;
//                System.out.println(bean.getClass());
//                System.out.println(realAdvisor.getClass());
//                System.out.println(2323);
//
//                break;
//            }
//        }


//-----------------------------------------------------------------------------------------------------------------------
//---------- 2. 匹配代理失败则直接返回bean-----------------------------------------------
//            return bean;
//        }


//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
//        if (bean != null) {
//
//            return wrapIfNecessary(bean, beanName);
//            /**
//             * protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, @Nullable TargetSource targetSource)
//             * {
//             *
//             * 		    if( isSupportedBeanName(beanClass, beanName) ){
//             * 				return PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS;}
//             * 			else {
//             * 		        return	DO_NOT_PROXY;}
//             *        }
//             */
////            protected static final Object[] PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS = new Object[0];
//
//        }
//        return bean;
//    }
//    public Object wrapIfNecessary(Object bean, String beanName){
//
//        Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
//        if (specificInterceptors != DO_NOT_PROXY) {
//            Object proxy = createProxy(
//                    bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
//            return proxy;
//        }
//        return bean;
//    }
//
//    protected Object[] getAdvicesAndAdvisorsForBean(
//            Class<?> beanClass, String beanName, @Nullable TargetSource targetSource) {
//
//        ////首先调用 findEligibleAdvisors(beanClass, beanName) 方法来查找适用于给定 Bean 的所有候选 Advisor。
//        List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);
//        //然后，它检查是否有可应用的 Advisor。如果没有找到可应用的 Advisor，则返回一个特殊的常量 DO_NOT_PROXY，表示不需要代理该 Bean。
//        if (advisors.isEmpty()) {
//            return DO_NOT_PROXY;
//        }
//        ////将找到的 Advisor 转换为数组并返回，表示需要为该 Bean 创建代理，并将这些 Advisor 应用于代理。
//        return advisors.toArray();
//    }
//    protected Object createProxy(Class<?> beanClass, @Nullable String beanName,
//                                 @Nullable Object[] specificInterceptors, TargetSource targetSource) {
//
//        return buildProxy(beanClass, beanName, specificInterceptors, targetSource, false);
//    }
//    private Object buildProxy(Class<?> beanClass, @Nullable String beanName,
//                              @Nullable Object[] specificInterceptors, TargetSource targetSource, boolean classOnly) {
//
//        if (this.beanFactory instanceof ConfigurableListableBeanFactory clbf) {
//            AutoProxyUtils.exposeTargetClass(clbf, beanName, beanClass);
//        }
//
//        ProxyFactory proxyFactory = new ProxyFactory();
//        proxyFactory.copyFrom(this);
//
//        //proxyFactory.isProxyTargetClass() true表示要使用目标类创建代理，以确定是否明确要求使用目标类来创建代理。
//        if (proxyFactory.isProxyTargetClass()) {
//            // Explicit handling of JDK proxy targets and lambdas (for introduction advice scenarios)
//            // 进一步检查目标类是否是代理类（Proxy.isProxyClass(beanClass)）或Lambda类（ClassUtils.isLambdaClass(beanClass)
//            // 这是为了处理一些特殊情况，例如，当目标类本身已经是代理类或Lambda类时，Spring AOP需要特殊处理。
//            if (Proxy.isProxyClass(beanClass) || ClassUtils.isLambdaClass(beanClass)) {
//                // Must allow for introductions; can't just set interfaces to the proxy's interfaces only.
//                for (Class<?> ifc : beanClass.getInterfaces()) {
//                    //添加接口到代理类：代码会遍历目标类实现的接口，并将这些接口添加到 ProxyFactory 中。
//                    proxyFactory.addInterface(ifc);
//                }
//            }
//        }
//        else {
//            // No proxyTargetClass flag enforced, let's apply our default checks...
//            // isProxyTargetClass() 返回 false，表示没有明确要求使用目标类来创建代理.shouldProxyTargetClass 检查：首先，代码检查 shouldProxyTargetClass 方法，该方法根据目标类的类型和配置来确定是否应该使用目标类。
//            if (shouldProxyTargetClass(beanClass, beanName)) {
//                proxyFactory.setProxyTargetClass(true);
//            }
//            else {
//                // 调用 evaluateProxyInterfaces 方法，该方法用于评估是否应该使用目标类的接口来创建代理。
//                // 这是Spring AOP的默认行为，它将根据目标类的接口来创建代理。
//                evaluateProxyInterfaces(beanClass, proxyFactory);
//            }
//        }
//
//        Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
//        proxyFactory.addAdvisors(advisors);
//        proxyFactory.setTargetSource(targetSource);
//        customizeProxyFactory(proxyFactory);
//
//        proxyFactory.setFrozen(this.freezeProxy);
//        if (advisorsPreFiltered()) {
//            proxyFactory.setPreFiltered(true);
//        }
//
//        // Use original ClassLoader if bean class not locally loaded in overriding class loader
//        ClassLoader classLoader = getProxyClassLoader();
//        if (classLoader instanceof SmartClassLoader smartClassLoader && classLoader != beanClass.getClassLoader()) {
//            classLoader = smartClassLoader.getOriginalClassLoader();
//        }
//        return (classOnly ? proxyFactory.getProxyClass(classLoader) : proxyFactory.getProxy(classLoader));
//    }
//    protected Advisor[] buildAdvisors(@Nullable String beanName, @Nullable Object[] specificInterceptors) {
//        // Handle prototypes correctly...
//        Advisor[] commonInterceptors = resolveInterceptorNames();
//
//        List<Object> allInterceptors = new ArrayList<>();
//        if (specificInterceptors != null) {
//            if (specificInterceptors.length > 0) {
//                // specificInterceptors may equal PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS
//                allInterceptors.addAll(Arrays.asList(specificInterceptors));
//            }
//            if (commonInterceptors.length > 0) {
//                if (this.applyCommonInterceptorsFirst) {
//                    allInterceptors.addAll(0, Arrays.asList(commonInterceptors));
//                }
//                else {
//                    allInterceptors.addAll(Arrays.asList(commonInterceptors));
//                }
//            }
//        }
//
//        Advisor[] advisors = new Advisor[allInterceptors.size()];
//        for (int i = 0; i < allInterceptors.size(); i++) {
//            advisors[i] = this.advisorAdapterRegistry.wrap(allInterceptors.get(i));
//        }
//        return advisors;
//    }
//
//
//    protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
//        //首先调用 findCandidateAdvisors() 方法，从容器中获取所有候选的 Advisor。
//        List<AspectJExpressionPointcutAdvisor> candidateAdvisors = findCandidateAdvisors();
//
//        //然后调用 findAdvisorsThatCanApply 方法，筛选出那些可以应用于给定 Bean 的 Advisor。
//        List<AspectJExpressionPointcutAdvisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
//
//        // 接着调用 extendAdvisors 方法，用于扩展 Advisor 列表（通常不会改变 Advisor 列表）
//        extendAdvisors(eligibleAdvisors);
//        // 同时对符合条件的 Advisor 进行排序，并返回结果。
//        if (!eligibleAdvisors.isEmpty()) {
//            eligibleAdvisors = sortAdvisors(eligibleAdvisors);
//        }
//        return eligibleAdvisors;
//    }
//    protected List<AspectJExpressionPointcutAdvisor> findCandidateAdvisors() throws Exception {
//        List<AspectJExpressionPointcutAdvisor> advisorss = xmlBeanFactory.getBeansForType(AspectJExpressionPointcutAdvisor.class);
//
//        return advisorss;
//    }
//
//    public static List<AspectJExpressionPointcutAdvisor> findAdvisorsThatCanApply(List<AspectJExpressionPointcutAdvisor> candidateAdvisors, Class<?> beanClass) {
////        if (candidateAdvisors.isEmpty()) {
////            return candidateAdvisors;
////        }
//
//        List<AspectJExpressionPointcutAdvisor> eligibleAdvisors = new ArrayList<>();
//        for (AspectJExpressionPointcutAdvisor candidate : candidateAdvisors) {
////            if (candidate instanceof IntroductionAdvisor && canApply(candidate, clazz)) {
////                eligibleAdvisors.add(candidate);
////            }
//            //Advisor candidate
//            if ( canApply(candidate.getPointcut(), beanClass)) {
//                eligibleAdvisors.add(candidate);
//            }
//        }
//
////        boolean hasIntroductions = !eligibleAdvisors.isEmpty();
////        for (Advisor candidate : candidateAdvisors) {
////            if (candidate instanceof IntroductionAdvisor) {
////                // already processed
////                continue;
////            }
////            if (canApply(candidate, clazz, hasIntroductions)) {
////                eligibleAdvisors.add(candidate);
////            }
////        }
//        return eligibleAdvisors;
//    }
//    public static boolean canApply(AspectJExpressionPointcut pointcut, Class<?> targetClass) {
//        Assert.notNull(pointcut, "Pointcut must not be null");
//
//        // 首先排除不是这个类的
//        if (!pointcut.getClassFilter().matches(targetClass)) {
//            return false;
//        }
//        //然后开始切入不同的方法,比如不只有sayHello，还有sayGooble
//        // <property name="expression" value="execution(* com.dylan.boot.Model.*.sayHelloWorld(..))"/>
//        // 我们暂时直接返回true
//        return true;
////        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
////        if (methodMatcher == MethodMatcher.TRUE) {
////            // No need to iterate the methods if we're matching any method anyway...
////            return true;
////        }
////
////        IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
////        if (methodMatcher instanceof IntroductionAwareMethodMatcher iamm) {
////            introductionAwareMethodMatcher = iamm;
////        }
////
////        Set<Class<?>> classes = new LinkedHashSet<>();
////        if (!Proxy.isProxyClass(targetClass)) {
////            classes.add(ClassUtils.getUserClass(targetClass));
////        }
////        classes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
////
////        for (Class<?> clazz : classes) {
////            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
////            for (Method method : methods) {
////                if (introductionAwareMethodMatcher != null ?
////                        introductionAwareMethodMatcher.matches(method, targetClass, hasIntroductions) :
////                        methodMatcher.matches(method, targetClass)) {
////                    return true;
////                }
////            }
////        }
////
////        return false;
//    }
//    @Nullable
//    protected static final Object[] DO_NOT_PROXY = null;
//
//    protected static final Object[] PROXY_WITHOUT_ADDITIONAL_INTERCEPTORS = new Object[0];

        @Override
        public void setBeanFactory (BeanFactory beanFactory){
            xmlBeanFactory = (XmlBeanFactory) beanFactory;
        }

    }

