package com.dylan.boot.IOC;

import com.dylan.boot.IOC.XML.XmlBeanDefinitionReader;
import com.dylan.boot.IOC.XML.XmlBeanFactory;
import com.dylan.boot.Model.*;
import org.springframework.core.io.ClassPathResource;

        public class IOCtest {
            public static void main(String[] args) throws Exception{
                /**
                 * 加载xml文件路径方法一:
                 * XmlBeanDefinitionReader.class.getClassLoader().getResource("ioc.xml").getFile();
                 *
                 * 方法二：ClassPathResource classPathResource = new ClassPathResource("static/test.xml");
                 */
                String location = XmlBeanDefinitionReader.class.getClassLoader().getResource("ioc.xml").getFile();
//        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader();
                XmlBeanFactory factory = new XmlBeanFactory(location);
//                Wheel wheel = (Wheel) factory.getBean("wheel");
//                System.out.println(wheel + " | " + wheel.getBrand());

//                System.out.println(factory.getBean("car").getClass());
//                Car car = (Car) factory.getBean("car");
//                System.out.println(car + " | " + car.getMoney());

//                LogInterceptor ss = (LogInterceptor) factory.getBean("logInterceptor");
                HelloService hello = (HelloService) factory.getBean("helloService");
//                System.out.println(hello + " | " );
                hello.sayHelloWorld();

//------System.out.println(location);-------------------------------------------------------
//------成功输出:/D:/GitFiles/LightWeight_Boot/Boot/target/classes/ioc.xml-------------------------------------------------------
//        factory.loadBeanDefinitions(location);

//---------------------------------------------------------------------------------------------------------------------
//        ClassPathResource classPathResource = new ClassPathResource("ioc.xml");
//
//        String location = classPathResource.getURL().getPath();
//---------------------------------------------------------------------------------------------------------------------

/**
 * AOP 测试
 */
//                car.

    }
}
