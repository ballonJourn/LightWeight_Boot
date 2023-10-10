
#### 最简单的 IOC 容器只需以下几步即可实现（IOC这个目录实现了容器管理这一特性）：

1 )
 * 加载 xml 配置文件，遍历其中的标签

 * 获取标签中的 id 和 class 属性，加载 class 属性对应的类，并创建 bean

 * 遍历标签中的标签，获取属性值，并将属性值填充到 bean 中(如果值为空则填充引用ref这个bean)

2 )
* registerBean中使用beanMap.put()将 bean 注册到 bean 容器(可以理解为winforms中的拖拽模板)中
