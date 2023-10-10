package com.dylan.boot.IOC.XML;

import com.dylan.boot.IOC.BeanDefinition;
import com.dylan.boot.IOC.BeanDefinitionReader;
import com.dylan.boot.IOC.BeanReference;
import com.dylan.boot.IOC.PropertyValue;
import org.springframework.context.annotation.Bean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class XmlBeanDefinitionReader implements BeanDefinitionReader {
    /**
     * 1) 复现SimpleIoc的功能：根据location读取并解析xml文件
     *
     * 2) 注册xml文件里的bean到beanMap
     */

    /**
     * 错误提示 "Private field 'registry' is never assigned" 表示您的私有字段 'registry' 从未被分配值。这可能会导致 NullPointerException，因为您尝试在一个未初始化的字段上调用方法。
     *
     * 要解决这个问题，您需要确保在使用 'registry' 字段之前对其进行初始化。
     */
    private Map<String, BeanDefinition> registry ;

    public XmlBeanDefinitionReader(){
        registry = new HashMap<>();
    }

    public Map<String, BeanDefinition> getRegistry() {
        return registry;
    }


    @Override
    public void loadBeanDefinitions(String location) throws Exception{
//------用document读取xml  几个对象 1 Docu 2 factory.  3 File 4 docu.  只需要打完这几个单词就能完成下面几行------------------------------------------------------------------------------------------------------------
//------加载xml配置文件，遍历其中的标签------------------------------------------------------------------------------------------------------------
//------构建builder，通过传入fileStream遍历element------------------------------------------------------
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        FileInputStream fileInputStream = new FileInputStream(location);
        Document document = documentBuilder.parse(fileInputStream);


        Element root = document.getDocumentElement();
//------获取<beans>下的所有<bean>------------------------------------------------------------------------
        //root传入的是<beans>根
        parseBeanDefinitions(root);
    }
    public void parseBeanDefinitions(Element root) throws Exception{
        if(root!= null ){
            NodeList nodes = root.getChildNodes();
//----------得到<bean>------------------------------------------------------------------------------------------------
            for(int i = 0; i < nodes.getLength(); i++){
                //将node转化为Element才能使用getTag
                Node node = nodes.item(i);
                if(node instanceof Element){
                    Element eleNode = (Element) node;
                    //避免太多缩进，使用函数,此处传入的是<bean>
                    parseBeanDefinition(eleNode);
                }
            }
        }
    }
    public void parseBeanDefinition(Element beanEle) throws  Exception{
        String id = beanEle.getAttribute("id");
        String className = beanEle.getAttribute("class");

//------加载beanclass,因为要的是传入beanDefinition，所以这里不再使用newInstance------------------------------------------------------------------------
//        Class beanClass = null;
//        beanClass = Class.forName(className);
//        Object bean = beanClass.newInstance();
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(className);

        processProperty(beanEle, beanDefinition);

        /**
         * System.out.println(id);
 *         System.out.println(className);
         *         System.out.println(beanDefinition);
         *         System.out.println(beanDefinition.getBeanClassName());
         *         System.out.println(beanDefinition.getPropertyValues());
         *         输出 wheel com...wheel
         */



        registry.put(id, beanDefinition);

        /**
         * System.out.println(id);
         * 无输出 registry.put(id, beanDefinition) 有问题，应该是beanDefinition出错
         */

    }

//------创建bean以及用property实例化bean组件值-----------------------------------------------------------------------------------
//------NodeList propertyNodes = ele.getElementsByTagName("property");
    public void processProperty(Element beanEle, BeanDefinition beanDefinition){
        NodeList propertyNodes = beanEle.getElementsByTagName("property");
        for(int i = 0; i < propertyNodes.getLength(); i++){
            Node propertyNode = propertyNodes.item(i);
            if(propertyNode instanceof Element){
                Element propertyEle = (Element) propertyNode;


                String name = propertyEle.getAttribute("name");
                //这里value是String, 但是在PropertyValue里面value是Object  这里需要String因为能用length()
                String value = propertyEle.getAttribute("value");
//                System.out.println(name);
//                System.out.println("-----------XmlBeanDefinitionReader.processProperty---------------------");

//--------------为什么不能像简易IOC那样，直接利用反射(运行时数据修改)将bean相关Field访问权限设为可访问-------------------------------------------------------
                if(value != null && value.length() > 0){
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name,value));
                }else{
                    String ref = propertyEle.getAttribute("ref");
                    if(ref == null || ref.length() == 0){
                        throw new IllegalArgumentException("ref is not found");
                    }
                    /**
                     * BeanReference 对象保存的是 bean 配置中 ref 属性 的 空Object bean，
                     *
                     * 等 XmlBeanFactory使用getBean时候如果发现是这个空Object bean， 就会使用反射实例化这个Object bean
                     *
                     * 因此无论xml的配置顺序如何 都可以成功实例化
                     *
                     * 在后续 BeanFactory 实例化 bean 时，会根据 BeanReference 保存的ref去实例化 bean 所依赖的其他 bean。
                     */
//--------------通过BeanReference添加其他bean，比如wheel----------------------------------------------------------------------
                    BeanReference beanReference = new BeanReference(ref);
                    //这样难道不会得到一个name是ref,但是value是空的Object吗？
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));


                }
            }
        }
    }

}
