#  jvm的基本结构

JVM由三个主要的子系统构成

1. 类加载器子系统  （即将class文件加载到虚拟机内存）
   - **启动类加载器**：负责加载JRE的核心类库，如jre目标下的rt.jar,charsets.jar等
   - **扩展类加载器**：负责加载JRE扩展目录ext中JAR类包
   - **系统类加载器**：负责加载ClassPath路径下的类包
   - **用户自定义加载器**：负责加载用户自定义路径下的类包
2. 运行时数据区（内存结构）
3. 执行引擎

![image-20230225165659938](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230225165659938.png)

1. **本地方法栈(线程私有)：登记native方法，在Execution Engine执行时加载本地方法库
2. 程序计数器（线程私有）：就是一个指针，指向方法区中的方法字节码（用来存储指向下一条指令的地址,也即将要执行的指令代码），由执行引擎读取下一条指令，是一个非常小的内存空间，几乎可以忽略不记。
3. 方法区(线程共享)：类的所有字段和方法字节码，以及一些特殊方法如构造函数，接口代码也在此定义。简单说，所有定义的方法的信息都保存在该区域，静态变量+常量+类信息(构造方法/接口定义)+运行时常量池都存在方法区中，虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却有一个别名叫做 Non-Heap（非堆），目的应该是与 Java 堆区分开来。
4. Java栈（线程私有）： Java线程执行方法的内存模型，一个线程对应一个栈，每个方法在执行的同时都会创建一个栈帧（用于存储局部变量表，操作数栈，动态链接，方法出口等信息）不存在垃圾回收问题，只要线程一结束该栈就释放，生命周期和线程一致
5. JVM对该区域（栈）规范了两种异常：

        1) 线程请求的栈深度大于虚拟机栈所允许的深度，将抛出StackOverFlowError异常
        
        2) 若虚拟机栈可动态扩展，当无法申请到足够内存空间时将抛出OutOfMemoryError，通过jvm参数–Xss指定栈空间，空间大小决定函数调用的深度


# 运行时数据区

##    1   程序计数器（寄存器）

###      1） 做用：  记住下一条jvm指令的地址

![image-20230225174838603](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230225174838603.png)

###   2）    **特点：**

- ​     时线程私有的    每个线程都有自己的程序计数器
- ​    唯一一个不存在内存溢出的区





## 2     jvm虚拟机栈

  线程运行时需要的内存空间。

  栈里存储栈帧，当运行方法时对应的栈帧（每个方法运行时需要的内存）入栈，方法执行完后出栈

![image-20230225180355502](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230225180355502.png)

### 1）**特点：**

- 每个线程只能有一个活动的栈帧（栈顶部的栈帧），对应着当前正在执行的方法
- 每个线程运行时需要的内存，称为虚拟机栈
- 栈帧 -------每个方法运行时需要的内存



###   2）**问题：**

-   不需要垃圾回收
-  栈内存不是越高越好，栈内存越高线程越少
-  如果变量没有逃离方法的作用范围，那么它是线程安全的

**栈内存溢出 ** （StackOverFlowError)

-   栈帧过多导致栈内存溢出
- 栈帧过大，也会导致栈内存溢出



**线程运行诊断**

1.cpu占用过高

  top: 查看进程状态  定位到进程

 ps H -eo pid.tid,%cpu   :   查看线程，进程，cpu占用率

 ps H -eo pid.tid,%cpu | grep pid ： 过滤想要看的线程

jstack  <pid>  : 查看当前进程对应虚拟机里的线程信息

​                          对比十六进制线程id找到相应的信息









## 3 本地方法栈 （native method stacks)

对非java方法运行时提供的空间





## 4  堆（heap）

 

### 1）  **定义**

-  通过new创建的对象都会使用堆内存

- 是线程共享的，堆中的对象都要考虑线程安全问题

- 有垃圾回收机制

- 对于大多数应用来说，Java 堆是 Java 虚拟机所管理的内存中最大的一块。Java 堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例以及数组都要在堆上分配内存。根据 Java 虚拟机规范的规定，Java 堆可以处于物理上不连续的内存空间中，只要逻辑上是连续的即可，如果在堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出 OutOfMemoryError 异常。

  

### 2）**堆内存溢出** （OutOfMemoryError)

​    不断创建新的对象使用，最终导致堆内存溢出

### 3）**堆内存诊断**

1. jps工具
   - 查看当前系统有哪些java线程
2. jmap工具
   -  查看堆内存占用情况         -heap <进程id>

   3.jconsole工具

-  图形化界面，多功能检测工具，可以连续监测

  

### 4） **案例**

- 垃圾回收后，内存占用依然很高

   可以用jsisualvm工具排查具体对象





## 5  方法区（Method Area）

### 1） **定义 ** 

方法区与 Java 堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。根据 Java 虚拟机规范的规定，当方法区无法满足内存分配需求时，将抛出 OutOfMemoryError 异常。

![image-20230227185129544](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230227185129544.png)



### 2）  **内存溢出**

![image-20230227185729369](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230227185729369.png)

一次性加载的类太多则会导致方法去内存溢出  MetaSpace

**场景**

- 在开发中 spring和mybatis在运行过程中会加载很多类，有可能会造成方法去内存溢出





## 6 运行时常量池

### 1） **定义**

- 常量池就是一张表，虚拟机指令根据这张表找到要执行的方法，类名，方法名，参数类型，字面量等信息
- 运行时常量池，常量池是*.class文件中的，当该类被加载，它的常量池信息就会放入运行时常量池，并把里面的符号地址变为真实地址。
- 运行时常量池，存在于方法区





## 7 StringTable （串池）

![image-20230227194858337](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230227194858337.png)

​     答案： false，true，true，false 如果最后两行换位置则为 true

### 1） **特性**

- 常量池中的字符串仅是符号，第一次用到时才变为对象
- 利用串池机制，来避免重复创建字符串对象
- 字符串拼接的原理是StringBuilder（1.8）
- 字符串常量拼接的原理是编译器优化
- 可以使用intern方法，主动将传池中还没有的对象放入串池
  - 1.8 将对象放入串池，没有则放入，有则不放入
  - 1.6  将对象放入串池，没有则将对象复制一份，放入串池，有则不放入



### 2）**位置**

   jdk1.8之后将stringtable放入了堆中，增加了垃圾回收的效率

​    1.6 串池存在于永久代



### 3） **垃圾回收机制**

   当stringtable内存不足时会触发gc垃圾回收





### 4） **性能调优**   		

- ​    stringtable底层是hashtable表实现的如果桶的个数很多那么存储的效率也会变高

​           调优参数  -XX: StringTableSize = "??"  设置桶的个数

-   考虑将字符串对象是否入池   如果有大量的字符串对象，并且有重复的可以考虑让对象入池会节省大量空间





## 8  直接内存  （Direct 	Memory）

### 1）  **定义**

-  常见于NIO操作，用于数据缓冲区
- 分配回收成本较高，但读写性能高
- 不受jvm内存回收管理

普通文件读写过程

![image-20230228154457048](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228154457048.png)

直接缓冲区 ,在操作系统层面划一块缓冲区。java代码可以直接访问，读写速度得到了成倍的提升。

```java
ByteBuffer.allocateDirect(_1MB)
```

![image-20230228154527857](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228154527857.png)







### 2） **分配和回收原理**

- 使用了Unsafe对象完成直接内存的分配和回收，并且回收需要调用freememory方法
- ByteBuffer的实现类内部，使用了Cleaner(虚引用)来检测ByteBuffer对象，一旦ByterBuffer对象被垃圾回收，那么就有ReferenceHandler线程通过Cleaner的clean方法调用freeMemory来释放直接内存





# 垃圾回收







## 1 如何判断对象可以回收

### 1） **引用计数法**

   如果对象引用多一个，计数就会加一当引用数为0时，被垃圾回收。

会造成循环引用，内存溢出

![image-20230228161249734](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228161249734.png)

### 2）  **可达性分析算法**

- java虚拟机中的垃圾回收器采用可达性分析来探索所有存活的对象
- 扫描堆中的对象，看看能否沿着gc root对象为起点的引用链找到该对象，找不到，表示可以回收
- 哪些对象可以作为gc root？
  -  系统类对象，本地方法对象，线程类对象，被加锁的对象（Busy Mointor），栈帧内使用的变量，被引用的对象





### 3）  **四种引用**

-   强引用

    只有所有的gc root对喜爱那个都不通过强引用引用该对象，该对象才能被垃圾回收

- 弱引用

  -    不管内存够不够，都会回收弱引用对象   引用的对象被回收后会进入引用队列
  -    可以配合引用队列来释放弱引用自身

  

- 软引用

  -   垃圾回收时内存不够会回收软引用的对象，内存够则不会回收。 引用的对象被回收后会进入引用队列

  -    可以配合引用队列来释放软引用自身

- 虚引用

  -  ![image-20230228170221419](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228170221419.png)
  - 当虚引用引用的对象被回收时，其进入引用队列，被遍历回收
  - 必须配合引用队列使用，主要配合ByteBuffer使用，被引用对象回收时，会将虚引用入队，有Reference Handler线程调用虚引用相关的方法释放直接内存





## 2  垃圾回收算法

 





### 1） **标记清除   Mark Sweep**

- 首先将gc root找不到的对象啊进行标记，然后将该对象的起始和终止地址记录到空闲列表。分配新对象时，则会去空闲列表去找，如果有足够的内存空间就进行内存分配。
- 标记清除的速度是很快的，但是容易产生内存碎片

![image-20230228185059746](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228185059746.png)

  

### 2）  **标记整理  Mark Compact**

- 标记整理是在标记清除的基础上对清除后的空间进行整理，时空间更加连续

- 这样就不会有很多的内存碎片

- 相对于标记清除效率变低

  ![image-20230228190020481](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228190020481.png)





### 3）  **复制算法**

-  不会有内存碎片
- 需要占用双倍的内存空间

![image-20230228190451977](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230228190451977.png)







##  3 分代垃圾回收机制

### 1）  **内存划分**

-   长时间使用的对象放在老年代
- 使用完就丢弃的对象放在新生代
- 新生代的垃圾回收比较频繁
- 老年代垃圾回收的频率低，堆内存不足时才会清理

![image-20230301110526318](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301110526318.png)

- 对象首先分配在伊甸园区
- 伊甸园空间不足时，出发minor gc，伊甸园和from存活的对象使用复制算法到to中，存活的对象年龄加一并且from和to交换位置
- minor gc 会引发stop the world，暂停其他用户线程，等待垃圾回收结束，用户线程才恢复运行
- 当对象年龄超过阈值时，会晋升至老年代，最大寿命是15（4bit）
- 当老年代空间不足，会先尝试minor gc，如果之后空间仍然不足，那么会触发full gc，stw的时间更长





### 2）  **相关VM参数**

![image-20230301112807092](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301112807092.png)





## 4  垃圾回收器



### 1）   **串行**

- 单线程
- 堆内存较小时，适合个人电脑单核cpu
- 打开串行垃圾回收器 ![](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301115326532.png)
- ![image-20230301115646007](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301115646007.png)

### 2）  **吞吐量优先**

- 多线程
- 适合堆内存较大的场景，需要多核cpu支持
- 让单位时间内，STW时间最短 
- 1.8 默认开启吞吐量优先    标记整理算法
- ![image-20230301115803834](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301115803834.png)

### 3）  **响应时间优先**

- 多线程
- 适合堆内存较大的场景，需要多核cpu支持
- 注重的时，尽可能让单次gc时stop the world时间变短

-   开启响应时间优先的虚拟机参数    标记清除算法，如果产生的内存碎片太多导致并发失败，则会退化为seriaold做一次串行的老年代垃圾回收
- ![image-20230301120618150](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301120618150.png)



## 5 G1

![image-20230301133629776](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301133629776.png)



### 1）  ***G1回收阶段***

- 刚开始是新生代的垃圾收集（Young Collection），经过一段时间老年代的内存超过了阈值，会在新生代垃圾收集时进行并发标记（Young Collection + Concurrent Mark）。最后进行混合收集，**会对新生代**，**幸存区，老年代**都进行垃圾收集（Mixed   Collection）
- 这是一个循环的过程
-  ![image-20230301162528431](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301162528431.png)





### 2）  **新生代收集阶段**

- 会SWT
- ![image-20230301163322906](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301163322906.png)

### 3）  **（Young Collection + Concurrent Mark）**

- 在Young GC时会进行GC Root初始标记
- 老年代占用堆空间达到阈值时，进行并发标记（不会SWT），有下面JVM参数决定阈值
- -XX:InItiatingHeapOccupancyPercent = Percent(默认45%)
- ![image-20230301164150451](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301164150451.png)



### 4）  **Mixed Collection**

- 会对 E,S,O进行全面的回收
- 最终标记（Remark）会STW
- 拷贝存活会(Evacuation）STW
- -XX:MaxGCPauseMillis=ms   //期望垃圾回收的最大时间
- 优先回收空间占用较大的区域
- ![image-20230301164519315](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301164519315.png)



### 5）  Full GC

-  SerialGC
  - 新生代内存不足发生垃圾收集 minor gc
  - 老年代内存不足发生垃圾收集  full gc
- ParallelGC
  - 新生代内存不足发生垃圾收集 minor gc
  - 老年代内存不足发生垃圾收集  full gc
- CMS
  - 新生代内存不足发生垃圾收集 minor gc
  - 老年代内存不足，并发回收失败之后，才会采用full gc
- G1
  - 新生代内存不足发生垃圾收集 minor gc
  - 老年代内存不足，会进行并发标记，当并发处理跟不上垃圾产生的速度才会触发full gc

​		

### 6）  Young Collection的跨代引用

-   将老年代的区域划分成卡表（每张卡512k），当新生代的GC Root存在与老年代的卡中时，将该卡标记成脏卡
- 这样做gc root 遍历的时候就不用遍历整个老年代，减少范围，提高效率
- 新生代有Remembered Set记录都有哪些脏卡
- 在引用变更时通过post—write barrier+dirty  card queue
- concurrent refinement threads更新Remember Set
- ![image-20230301171226267](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230301171226267.png)

### 7）  Remark

-  为了防止并发标记过后一些对象被误删除
- 这时候就需要对 **对象**进一步检查**Remark**
- 当对象的引用发生生改变时，JVM就会给该对象加入写屏障
- 写屏障将该对象加入一个队列当中，使该对象状态变为正在处理状态
- Remark重新检查队列中的对象并判断
- per-write barrier + satb——mark——queue





### 8）  JDK8 u20字符串去重

- 优点：节省大量的内存

- 缺点：略微多占用了cpu时间，新生代回收时间略微增加

- -XX：UserStringDeduplication

- ```java
  String s1 = new String("hello"); //char[]{'h','e','l','l','0'}
  String s2 = new String("hello"); //char[]{'h','e','l','l','0'}
  ```

-   将所有新分配的字符串放入一个队列
- 当新生代回收时，G1并发检查是否有字符串重复
- 如果他们值一样，让他们引用同一个char[]
- 注意，与string.intern()不一样
  - string.intern()关注的是字符串对象
  - 而字符串去重关注的是char[]
  - 在JVM内部，使用了不同的字符串表
