/**
 * 最简单的 IOC 容器只需以下几步即可实现：
 *
 * 加载 xml 配置文件，遍历其中的标签
 * 获取标签中的 id 和 class 属性，加载 class 属性对应的类，并创建 bean
 * 遍历标签中的标签，获取属性值，并将属性值填充到 bean 中
 * 将 bean 注册到 bean 容器中
 */
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
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
