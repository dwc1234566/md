# 1   容器接口



##                     1.1 BeanFactory  和ApplicationContext的区别

-  Spring 框架带有两个 IOC 容器—— BeanFactory和ApplicationContext。BeanFactory是 IOC 容器的最基本版本，ApplicationContext扩展了BeanFactory的特性。 

-  Spring容器最基本的接口就是BeanFactory。BeanFactory负责配置、创建、管理Bean，它有一个子接口ApplicationContext，也被称为Spring上下文，容器同时还管理着Bean和Bean之间的依赖关系。

-  spring Ioc容器的实现，从根源上是beanfactory，但真正可以作为一个可以独立使用的ioc容器还是DefaultListableBeanFactory，因此可以这么说，DefaultListableBeanFactory 是整个spring ioc的始祖。

![image-20230307151406345](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230307151406345.png)





## 1.2**BeanFactory**的作用



