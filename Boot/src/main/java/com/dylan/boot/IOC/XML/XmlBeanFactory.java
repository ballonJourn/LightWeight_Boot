package com.dylan.boot.IOC.XML;

import com.dylan.boot.AOP.AspectJAwareAdvisorAutoProxyCreator;
import com.dylan.boot.IOC.BeanDefinition;
import com.dylan.boot.IOC.BeanPostProcessor;
import com.dylan.boot.IOC.BeanReference;
import com.dylan.boot.IOC.Factory.BeanFactory;
import com.dylan.boot.IOC.Factory.BeanFactoryAware;
import com.dylan.boot.IOC.PropertyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * BeanFactory 作用 1) 通过实例化 XmlBeanDefinitionReader 来将 bean的id，class和property(不包括value)加入BeanDefinitions 加个Xml指名是读取Xml配置的
 *
 * 2） put 到 Map里面
 *
 * 3） 初始化AOP接口 BeanPostProcors
 *
 * 重点：beanDefinition转化为beanPostProcessor 使用 newInstance，
 * 您提到的原理涉及 Java 类型系统和多态性。
 * 让我解释一下为什么之前的代码会导致 ClassCastException，以及为什么使用 newInstance() 方法可以解决这个问题。
 *
 * 为什么会出现 ClassCastException： 当您尝试将一个对象从一个类转换为另一个类时，Java 运行时会检查这个转换是否合法。
 * 如果目标类不是源类的子类或实现的接口，会抛出 ClassCastException。
 * 在您的代码中，尝试将 BeanDefinition 转换为 BeanPostProcessor，但它们是不相关的类，所以出现了 ClassCastException。
 *
 * 为什么 newInstance() 可以解决问题： newInstance() 方法用于创建类的新实例，返回的是一个 Object 引用。
 * 这个实例是在运行时动态创建的，并且其具体类型是您创建实例的类。
 * 这意味着您可以在运行时创建一个与 BeanDefinition 相同类的对象，然后检查它是否是 BeanPostProcessor 的实例，
 * 因为您创建的实例与 BeanPostProcessor 相关。
 */
public class XmlBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap;

    private List<String> beanDefinitionNames ;

    private List<BeanPostProcessor> beanPostProcessors ;

    private XmlBeanDefinitionReader beanDefinitionReader;

//    private BeanDefinition beanDefinition ;

    public List<String> getBeanDefinitionNames() {
        return beanDefinitionNames;
    }

    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }

    /**
     * 错误提示 "Private field 'registry' is never assigned" 表示您的私有字段 'registry' 从未被分配值。这可能会导致 NullPointerException，因为您尝试在一个未初始化的字段上调用方法。
     *
     * 要解决这个问题，您需要确保在使用 'registry' 字段之前对其进行初始化。
     */
    public XmlBeanFactory(String location) throws Exception{
        this.beanDefinitionReader = new XmlBeanDefinitionReader();
        beanDefinitionMap = new HashMap<>();
        beanDefinitionNames = new ArrayList<>();
        beanPostProcessors = new ArrayList<>();
        loadBeanDefinitions(location);
    }
    //BeanFactory不屑于读取配置，因此这个继续转交给Reader  这里的Reader是当前class的全局对象，一改全改
    public void loadBeanDefinitions(String location) throws Exception{
        beanDefinitionReader.loadBeanDefinitions(location);// 已经读取完毕 xml 配置文件中的属性
        // beanDefinitionReader 里面的BD包含了BD 和 PropertyValue（value可能是ref  BeanReference），这里将其提取到Map里面
        registerBeanDefinition();
        // Todo
        // 找到所有实现了 BeanPostProcessor 的类
        registerBeanProcessor();

    }

    public void registerBeanDefinition(){
//        System.out.println("this is registerBD");
        /**
         * entrySet() 是一个常用于 Java 集合框架中的方法，它通常用于获取一个集合中包含的所有键值对（entries）的集合。
         *
         * 这个方法在 Map 接口和实现了 Map 接口的类中经常出现。
         *
         * 在这里我们获取所有bean的map
         */

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionReader.getRegistry().entrySet()) {
            String name = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            beanDefinitionMap.put(name, beanDefinition);
            beanDefinitionNames.add(name);
//            System.out.println(beanDefinition.getBeanClass());
            //输出
            // logInterceptor
            //helloServiceAspect
            //car
            //autoProxyCreator
            //wheel
            //helloService

        }

    }

    public void registerBeanProcessor() throws Exception{

        //这里，getBeanForType第一次使用getBean，但是BeanPostProcessor是空的，不调用initPost函数，因此这时候还没栈溢出问题
        List beanPs = getBeansForType(BeanPostProcessor.class);
//        System.out.println(2222);

        //      输出 空
        for (Object bean : beanPs) {
//            System.out.println("-------------------------------XmlBeanFac.registerBeanProcessor------------------------------");
//            System.out.println(bean.getBeanClass());
            addBeanPostProcessor((BeanPostProcessor) bean);
//            System.out.println(2222);
        }
//        for (BeanPostProcessor bean : beans) {
//            addBeanPostProcessor((BeanPostProcessor) bean);
//        }
    }

    public void addBeanPostProcessor(BeanPostProcessor bean){
        beanPostProcessors.add(bean);
//        System.out.println(22);
//      输出 空
    }

    public List getBeansForType(Class<?> type) throws Exception{
        List beanPs = new ArrayList();
        //Todo 看看怎么拿的 getBeanForType, 主要是这一个东西怎么用 type.isAssignableFrom
        /**
         * type.isAssignableFrom(cls) 是Java中的一个方法，用于检查一个类型 type 是否可以分配给另一个类型 cls。
         *
         * 具体来说，它用于检查 cls 是否是 type 的超类或超接口，或者是否与 type 类型相同，
         *
         * 或者是否可以通过引用赋值将 type 类型的对象赋给 cls 类型的变量。
         */
        for (String beanDefinitionName : beanDefinitionNames) {

//            System.out.println(2222);
            if(type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass()))
            {
                System.out.println(beanDefinitionMap.get(beanDefinitionName).getBeanClass());
//                System.out.println(2222);
//                System.out.println(beanDefinitionMap.get(beanDefinitionName).getBeanClass());
//                输出： class com.dylan.boot.AOP.AspectJAwareAdvisorAutoProxyCreator
                //public class AspectJAwareAdvisorAutoProxyCreator  implements BeanPostProcessor

//                beans.add智能传入beanDefinition，因此beans不能用List<BeanPostProcessor> 来实现
                //这里局部变量和全局变量冲突了  他吗的  换成 beanPs
//                BeanDefinition s = beanDefinitionMap.get(beanDefinitionName);
//                Object beanP = s.getBeanClass().newInstance();

//                BeanDefinition beanP = beanDefinitionMap.get(beanDefinitionName);
//                System.out.println(beanP.getBeanClass());
//                System.out.println(111);
//                System.out.println(beanP.getClass());
                /**
                 * 虽然 AspectJAwareAdvisorAutoProxyCreator 类型实现了 BeanPostProcessor 接口，但在 beanDefinitionMap 中，它被存储为 BeanDefinition 对象，
                 *
                 * 因此 getClass() 方法会返回 BeanDefinition 的类。
                 *
                 * 要获得对象的实际类型，您需要使用 instanceof 运算符，或者可以在实际使用对象时根据需要进行类型转换。
                 */
                beanPs.add(getBean(beanDefinitionName));
//                beanDefinitionMap.get(beanDefinitionName).getBeanClassName();
            };
        }

        return beanPs;
    }



    @Override
    public Object getBean(String beanId) throws Exception{

        BeanDefinition beanDefinition = beanDefinitionMap.get(beanId);
        System.out.println(beanId);
//        System.out.println(beanId);
//        System.out.println("-----------XmlBeanFactory.getBean---------------------");
        if(null == beanDefinition){
            throw new IllegalArgumentException("no bean with name " + "");
        }
        //重点：这里有问题，每次get都是空，导致循环判断，只要bean不是空就不会再继续执行 initbean 了
        Object bean = beanDefinitionMap.get(beanId).getBean();
//        System.out.println(beanDefinitionMap.get(beanId).getBean());
        System.out.println(3333);

//------如果还未实例化(意思是property是ref类型，只有name和未实例化的bd)，则执行以下------------------------------------------------------------------------------------------------
        if(null == bean){
            System.out.println("-----------XmlBeanFactory.getBean---------------------");
            /**
             *  Car car = (Car) factory.getBean("car");
             *  输出 -----------XmlBeanFactory.getBean---------------------
             * -----------XmlBeanFactory.getBean---------------------
             *
             * Wheel wheel = (Wheel) factory.getBean("wheel");
             *输出 -----------XmlBeanFactory.getBean---------------------
             *
             * Wheel wheel = (Wheel) factory.getBean("wheel");
             * Car car = (Car) factory.getBean("car");
             * 输出 -----------XmlBeanFactory.getBean---------------------
             * -----------XmlBeanFactory.getBean---------------------
             *
             *
             */
//            System.out.println(33);
//            System.out.println(beanDefinition.getClass());
            bean = createBean(beanDefinition);
            System.out.println(213);
//            System.out.println(bean.getClass());
            /**
             * 这时候Spring IOC容器已经完成对Bean实例对象的创建和属性的依赖注入
             *
             * initializeBean() 开始对 BeanPostProcessor 后置处理器的调用
             */
//            beanDefinitionMap.put(beanId, bean);

            bean = initializeBean(bean, beanId);

            //代码这里都没有到达过，怪不得设置不了beanDefinition
//            System.out.println(224);
//            System.out.println(beanDefinitionMap.get(beanId).getBean());
//            beanDefinitionMap.get(beanId).setBean(bean);
//            System.out.println(beanDefinitionMap.get(beanId).getBean());
            beanDefinition.setBean(bean);
            beanDefinitionMap.put(beanId, beanDefinition);

            //代码这里都没有到达过，怪不得设置不了beanDefinition
//            System.out.println(beanDefinition.getBean());
//            System.out.println(2333);
//            System.out.println(beanDefinitionMap.get(beanId).getBean());



        }
        return bean;


    }
    public Object createBean(BeanDefinition bd) throws Exception{
//------通过反射实例化bean对象-------------------------------------------------
/**
 * 步骤:
 *  1) 添加新的空bean对象：Object bean = beanClass.newInstance();-------------------------------------------------
 *
 *  2) bean.getClass().de (className) -----------------------------------------
 *
 *  3) declaredField.set 将bean相关Field访问权限设为可访问
 */
//        System.out.println(bd.getBeanClassName());
        Object bean = bd.getBeanClass().newInstance();
        applyPropertyValue(bean, bd);
        return bean;

    }
//------实现反射(不会，多看)--------------------------------------------------------------------------------------------------
    private void applyPropertyValue(Object bean, BeanDefinition bd) throws  Exception{
        //以前是这样实现的，简易IOC
//        Field declaredField = bean.getClass().getDeclaredField(bd.getBeanClassName());
//        declaredField.setAccessible(true);
//
//        declaredField.set(bean, bd.getPropertyValues());
        if(bean instanceof BeanFactoryAware){
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        for (PropertyValue propertyValue : bd.getPropertyValues().getPropertyValueList()) {
            Object value = propertyValue.getValue();
            /**
             * 检查变量 value 是否是 BeanReference 类的实例类
             * 如果 value 是 BeanReference 类的实例，表达式将返回 true
             *
             * 因此，反射可以忽视xml配置的bean顺序，让bean_1即使在依赖的bean_2之前被读取，也能注入bean_2的value
             *
             */
            if(value instanceof BeanReference){
//                System.out.println(((BeanReference) value).getName());
//
//                System.out.println("-----------XmlBeanFactory.applyPropertyValue---------------------");
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }
            /**
             * try: 新方法
             * catch: 旧的field
             */
            try {
                //?????????????????????????????????????
//                System.out.println("-------------------------------xmlBF.applyPropertyValue-------------------------------");
//                System.out.println(propertyValue.getName());
                Method declaredMethod = bean.getClass().getDeclaredMethod("set" +
                        propertyValue.getName().substring(0, 1).toUpperCase() +
                        propertyValue.getName().substring(1), value.getClass());
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(bean, value);
            }catch (NoSuchMethodException e){
//                TODO:
                // 操作属性
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                try {
                    int val = Integer.valueOf((String) value); // 看 value 是否能转换成 int 类型
                    declaredField.set(bean, val);
                } catch (Exception error) {
                    declaredField.set(bean, value);
                }
            }
        }

    }
    //getBean().initializeBean()
    public Object initializeBean(Object bean, String beanId) throws Exception{
//        System.out.println(beanId);

//        System.out.println("-------------------------------XmlBeanFac.initializeBean------------------------------");
//        System.out.println("-------------------------------AOP入口-------------------------------");
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            System.out.println(321);
//            System.out.println(bean);
//            System.out.println("-------------------------------XmlBeanFac.initializeBean------------------------------");
//            System.out.println("-------------------------------AOP入口-------------------------------");
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanId);
//            System.out.println(bean);
        }
//        System.out.println(2222);
//        System.out.println(bean);
        //处理之后，bean变成null了
//
        //这里一直在循环调用导致stackoverflow
        //1) 首先 initbean里面的postProcessAfterInitialization调用 xmlBeanFactory.getBeansForType，
        //2) 接着 xmlBeanFactory.getBeansForType，有 beanPs.add(getBean(beanDefinitionName));
        //3) 最后，getBean()里面有个initbean  导致闭环
        //4(( 解决循环的关键是判断getBean时候有没有已经初始化了的bean
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            System.out.println(338);
            //一开始拿的是空beanDefinition，因此beanfactory是空，但是也导致后面的一系列问题
//            AspectJAwareAdvisorAutoProxyCreator ss = (AspectJAwareAdvisorAutoProxyCreator) beanPostProcessor;
//            ss.setBeanFactory(this);

//            ------------------------------------------------------------------------------------------------
//            XmlBeanFactory factory = new XmlBeanFactory(location); 初始化过程中，beanPostProcessor是空
//            System.out.println(2222); 初始化未输出2222

            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanId);
        }
        return bean;
    }


}
