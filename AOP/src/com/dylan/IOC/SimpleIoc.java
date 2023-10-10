package com.dylan.IOC;
/**
 * 最简单的 IOC 容器只需以下几步即可实现（version 1.0实现了容器管理这一特性）：
 *
 * 1)
 *  1.1 加载 xml 配置文件，遍历其中的标签
 *  1.2 获取标签中的 id 和 class 属性，加载 class 属性对应的类，并创建 bean
 *  1.3 遍历标签中的标签，获取属性值，并将属性值填充到 bean 中(如果值为空则填充引用ref这个bean)
 * 2)
 *  registerBean中使用beanMap.put()将 bean 注册到 bean 容器(可以理解为winforms中的拖拽模板)中
 */
//Java中的IOC（Inversion of Control，控制反转）是一种设计原则和编程模式，用于管理应用程序组件之间的依赖关系。在传统的编程中，应用程序通常会负责自己的组件的创建和管理，而在IOC中，这种控制权被反转了，交由容器来管理。
//
//以下是IOC的关键概念和特点：
//
//依赖注入（Dependency Injection）： IOC的核心是依赖注入，它是指将一个组件所需的依赖关系（例如其他对象或服务）从外部注入到组件中，而不是组件自己创建或获取这些依赖关系。这样可以实现组件之间的松耦合，使得组件更容易被重用和测试。
//
//容器管理： 在IOC中，通常有一个容器（例如Spring容器）负责创建、配置和管理应用程序中的组件。容器负责跟踪组件之间的依赖关系，并在需要时注入依赖。
//
//配置文件或注解： 依赖关系可以通过配置文件（例如XML配置文件）或注解（例如Spring的@Component注解）来定义。这些配置描述了组件之间的关系，容器根据配置来实现依赖注入。
//
//解耦： IOC有助于解耦应用程序的各个部分，使得组件更加独立和可维护。这意味着你可以更容易地替换
/**
 *  version 1.0 代码结构做一个简要介绍：
 *
 * SimpleIOC：IOC 的实现类，实现了上面所说的几个步骤
 * SimpleIOCTest：IOC 的测试类
 * Car：IOC 测试使用的 bean
 * Wheel：测试使用的 bean
 * ioc.xml：bean 配置文件
 */
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
/**
 * 工厂模式生成 Bean
 * 请注意 factory-bean 和 FactoryBean 的区别。
 * 这节说的是前者，指的是静态工厂或实例工厂，而后者是 Spring 中的特殊接口，代表一类特殊的 Bean，下面一节会介绍 FactoryBean。

 设计模式里，工厂方法模式分静态工厂和实例工厂，我们分别看看 Spring 中怎么配置这两个，来个代码示例就什么都清楚了。

 */
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleIoc {
    private HashMap<String, Object> beanMap = new HashMap<>();
    public SimpleIoc(String location)throws Exception{
        loadBeans(location);
    }

    public Object getBean(String name) {
        Object bean = beanMap.get(name);
        if (null == bean) {
            throw new IllegalArgumentException("There is no bean with name:" + name);
        }
        return bean;
    }

    private void loadBeans(String location) throws Exception{

        // * 1.加载xml配置文件，遍历其中的标签
//        构建builder，通过传入fileStream遍历element
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        FileInputStream inputStream = new FileInputStream(location);

        Document document = documentBuilder.parse(inputStream);

        //获取<beans>下的所有<bean>
        Element root = document.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        //获取<bean>的所有Attribute,这里是id和class
        for(int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);

            //将node转化为Element
            if(node instanceof Element){
                Element beanEle = (Element) node;
//                System.out.println(beanEle.getAttribute("class"));
                //相关xml内容为：  <bean id="wheel" class="com.dylan.IOC.Car">
                String id = beanEle.getAttribute("id");
                String className = beanEle.getAttribute("class");

//--------------加载beanclass--------------------------------------------------------------------------------
                Class beanClass = null;
                //这里有个异常Unhandled exception: java.lang.ClassNotFoundException
                //使用try
                try{beanClass = Class.forName(className);}catch (Exception exception){}

//--------------创建bean------------------------------------------------------------------------
                Object bean = beanClass.newInstance();

//--------------遍历<property>标签------------------------------------------------------------------------
                NodeList propertyNodes = beanEle.getElementsByTagName("property");
//                System.out.println(propertyNodes.getLength()); 输出 5 2
//--------------这里是重点，为bean设置特性内容-----------------------------------------------------------------------
//--------------犯了低级错误，循环都搞错sb------------------------------------------------------------------------
                /**
                 * for(int j = 0; i < propertyNodes.getLength(); i++){
                 */

                for(int j = 0; j < propertyNodes.getLength(); j++){
                    Node propertyNode = propertyNodes.item(j);
                    //一样将node转化为Element
                    if(propertyNode instanceof Element){
                        Element propertyEle = (Element) propertyNode;
//                        for(int j = 0; i < propertyNodes.getLength(); i++){ 这一行导致
//                        System.out.println(propertyEle.getAttribute("name"));
//                        System.out.println(2); 输出为空
                        String name = propertyEle.getAttribute("name");

                        String value = propertyEle.getAttribute("value");

//----------------------利用反射(运行时数据修改)将bean相关Field访问权限设为可访问-------------------------------------------------------
                        Field declaredField = bean.getClass().getDeclaredField(name);
                        declaredField.setAccessible(true);

                        // 将属性值填充到相关字段中,declaredField已经是绑定当前遍历的name了
//                            declaredField.set(bean, value);
                        //但是如果是成员字段导致value是空的怎么办？因此declaredField.set要设置ref保证不报错
                        if(value != null && value.length() > 0){
                            declaredField.set(bean, value);
                        }else{
                            String ref = propertyEle.getAttribute("ref");
//                            System.out.println(2); 循环到wheel的时候未执行此语句 因为110行首先set了
                            if (null == ref || 0 == ref.length()) {
                                throw new IllegalArgumentException("ref config error");
                            }
                            // 将引用Object填充到相关字段中, 因此ioc.xml要按照顺序先把ref声明，再到car声明
                            declaredField.set(bean, getBean(ref));
                        }


                    }
                }
                // 将bean注册到bean容器中
                registerBean(id, bean);


            }
        }


    }

    private void registerBean(String id, Object bean) {
        beanMap.put(id, bean);
    }
}
