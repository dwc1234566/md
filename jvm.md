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

    只有所有的gc root对象那个都不通过强引用引用该对象，该对象才能被垃圾回收

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
- 收集垃圾时，必须stop the world，使用复制算法。
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
-  是一种以获得最短回收停顿时间为目标的收集器，**标记清除算法，运作过程：初始标记，并发标记，重新标记，并发清除**，收集结束会产生大量空间碎片。
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



### 9）  JDK8 u60回收巨型对象

- 一个大对象大于redin的一半时，称之为巨型对象
- G1不会对巨型对象进行拷贝
- 回收时被优先考虑
- G1会跟踪老年代所有incoming引用，这样老年代incoming引用为0的巨型对象就可以在新生代垃圾回收时处理掉













## 6 垃圾回收调优



### 1）  调优领域

- 内存
- 锁竞争
- cpu占用
- io







### 2）  确定目标

- 低延迟还是高吞吐量，选择合适的垃圾货收器
- CMS,G1,ZGC
- ParallelGC





### 3） 最快的GC，是不发生GC

- 查看FullGC前后的内存占用
  - 数据是不是太多？  是不是代码有问题
  - 数据表示是不是太臃肿？
    - 对象图
    - 对象大小
  - 是否存在内存泄漏？





### 4）  新生代调优

- 新生代的特点
  - 所有的new操作的内存分配非常廉价
    - TLAB thread-local allocation buffer
  - 死亡对象的回收代价是0
  - 大部分对象用过即死
  - Minor GC的时间远远低于Full Gc



- 并不是新生代空间越大越好

  - -Xmn

  - 为新生代（nursery）设置堆的初始大小和最大大小（以字节为单位）。附加字母`k`或`K`表示千字节，`m`或`M`表示兆字节，`g`或`G`表示千兆字节。堆的年轻代区域用于new对象。GC 在这个区域比在其他区域更频繁地执行。如果新生代的大小太小，则会执行大量次要垃圾回收。如果大小太大，则只执行完整的垃圾收集，这可能需要很长时间才能完成。**Oracle 建议您保持年轻代的大小大于整个堆大小的 25% 且小于 50%**。以下示例显示如何使用各种单位将年轻代的初始大小和最大大小设置为 256 MB：

    ```cmd
    -Xmn256m
    -Xmn262144k
    -Xmn268435456
    ```

-    新生代能容纳所有【并发量*（请求-响应）】的数据

- 幸存区大到能保留【当前活跃对象+需要晋升对象】

- 晋升阈值配置得当，让长时间存活的对象尽快晋升

  - **`-XX:MaxTenuringThreshold=threshold`**

    设置用于自适应 GC 大小调整的最大使用期阈值。最大值为 15。并行（吞吐量）收集器的默认值为 15，CMS 收集器的默认值为 6。

    以下示例显示如何将最大任期阈值设置为 10：

    ```cmd
    -XX:MaxTenuringThreshold=10
    ```

  - 

  - ```
    -XX:+PrintTenuringDistribution
    ```

    启用终身年龄信息的打印。以下是输出示例：

    ```cmd
    Desired survivor size 48286924 bytes, new threshold 10 (max 10)
    - age 1: 28992024 bytes, 28992024 total
    - age 2: 1366864 bytes, 30358888 total
    - age 3: 1425912 bytes, 31784800 total
    ...
    ```







### 5）  老年代调优

以CMS为例

- CMS的老年代内存越大越好

- 先尝试不做调优，如果没有Full GC那么已经  ，否则先尝试调优新生代

- 观察发生Full GC时老年代的占用，将老年代内存预设调大1/4~1/3

  - ```
    -XX:CMSInitiatingOccupancyFraction=percent
    ```

    设置开始 CMS 收集周期的老年代占用百分比（0 到 100）。默认值设置为 -1。任何负值（包括默认值）都意味着该选项`-XX:CMSTriggerRatio`用于定义初始占用率的值。

    以下示例显示如何将占用率设置为 20%：

    ```cmd
    -XX:CMSInitiatingOccupancyFraction=20
    ```









# 类加载与字节码技术







## 1 类文件结构

 

### 1）  魔术

-  0~3个字节，表示它是否是【class】类型文件
  - ![image-20230302141340105](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302141340105.png)





### 2）  版本

- 4~7个字节，表示类的版本00 34 （52）表示java8
  - ![image-20230302141519108](C:/Users/86172/AppData/Roaming/Typora/typora-user-images/image-20230302141519108.png)





### 3）  常量池

-  8~9字节，表示常量池长度，00 23 （35）表示常量池有#1~#34项，注意#0项不计入，也没有值
  - ![image-20230302142102755](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302142102755.png)





### 4）  访问标识与继承信息







### 5）  Field信息







### 6）  Method信息







### 7）  附加属性











## 2 字节码指令



### 1） javap工具

- javap工具可以反编译class字节码文件

  - ```cmd
    javap -v Main.class
    ```

  - 原始的main.java

    ```java
    package com.example.demo01;
    
    public class Main {
        public static void main(String[] args) {
            String a ="a";
            String b = "b";
            String c = "ab";
            String x = a+b;
        }
    }
    ```

    - 编译后的字节码

    - ```cmd
        Last modified 2023-2-27; size 484 bytes
        MD5 checksum 10b994507e45d6bac9a4769df80cee5c
        Compiled from "Main.java"
      public class com.example.demo01.Main
        minor version: 0
        major version: 52
        flags: ACC_PUBLIC, ACC_SUPER
      Constant pool:
         #1 = Methodref          #10.#19        // java/lang/Object."<init>":()V
         #2 = String             #20            // a
         #3 = String             #21            // b
         #4 = String             #22            // ab
         #5 = Class              #23            // java/lang/StringBuilder
         #6 = Methodref          #5.#19         // java/lang/StringBuilder."<init>":()V
         #7 = Methodref          #5.#24         // java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
         #8 = Methodref          #5.#25         // java/lang/StringBuilder.toString:()Ljava/lang/String;
         #9 = Class              #26            // com/example/demo01/Main
        #10 = Class              #27            // java/lang/Object
        #11 = Utf8               <init>
        #12 = Utf8               ()V
        #13 = Utf8               Code
        #14 = Utf8               LineNumberTable
        #15 = Utf8               main
        #16 = Utf8               ([Ljava/lang/String;)V
        #17 = Utf8               SourceFile
        #18 = Utf8               Main.java
        #19 = NameAndType        #11:#12        // "<init>":()V
        #20 = Utf8               a
        #21 = Utf8               b
        #22 = Utf8               ab
        #23 = Utf8               java/lang/StringBuilder
        #24 = NameAndType        #28:#29        // append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        #25 = NameAndType        #30:#31        // toString:()Ljava/lang/String;
        #26 = Utf8               com/example/demo01/Main
        #27 = Utf8               java/lang/Object
        #28 = Utf8               append
        #29 = Utf8               (Ljava/lang/String;)Ljava/lang/StringBuilder;
        #30 = Utf8               toString
        #31 = Utf8               ()Ljava/lang/String;
      {
        public com.example.demo01.Main();
          descriptor: ()V
          flags: ACC_PUBLIC
          Code:
            stack=1, locals=1, args_size=1
               0: aload_0
               1: invokespecial #1                  // Method java/lang/Object."<init>":()V
               4: return
            LineNumberTable:
              line 3: 0
      
        public static void main(java.lang.String[]);
          descriptor: ([Ljava/lang/String;)V
          flags: ACC_PUBLIC, ACC_STATIC
          Code:
            stack=2, locals=5, args_size=1
               0: ldc           #2                  // String a
               2: astore_1
               3: l
               // String b
               5: astore_2
               6: ldc           #4                  // String ab
               8: astore_3
               9: new           #5                  // class java/lang/StringBuilder
              12: dup
              13: invokespecial #6                  // Method java/lang/StringBuilder."<init>":()V
              16: aload_1
              17: invokevirtual #7                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
              20: aload_2
              21: invokevirtual #7                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
              24: invokevirtual #8                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
              27: astore        4
              29: return
            LineNumberTable:
              line 5: 0
              line 6: 3
              line 7: 6
              line 8: 9
              line 9: 29
      }
      SourceFile: "Main.java"
      
      ```

      

### 2） 图解执行流程

####    1）  原始java代码

```java
package com.example.demo01;

public class h {
    public static void main(String[] args) {
        int a = 10;
        int b = Short.MAX_VALUE +1;
        int c = a+ b;
        System.out.println(c);
    }
}
```

#### 2） 编译后的字节码文件

```cmd
  Last modified 2023-3-2; size 439 bytes
  MD5 checksum b2bafa9e87bd2a14095eba3a57321c19
  Compiled from "h.java"
public class com.example.demo01.h
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #7.#16         // java/lang/Object."<init>":()V
   #2 = Class              #17            // java/lang/Short
   #3 = Integer            32768
   #4 = Fieldref           #18.#19        // java/lang/System.out:Ljava/io/PrintStream;
   #5 = Methodref          #20.#21        // java/io/PrintStream.println:(I)V
   #6 = Class              #22            // com/example/demo01/h
   #7 = Class              #23            // java/lang/Object
   #8 = Utf8               <init>
   #9 = Utf8               ()V
  #10 = Utf8               Code
  #11 = Utf8               LineNumberTable
  #12 = Utf8               main
  #13 = Utf8               ([Ljava/lang/String;)V
  #14 = Utf8               SourceFile
  #15 = Utf8               h.java
  #16 = NameAndType        #8:#9          // "<init>":()V
  #17 = Utf8               java/lang/Short
  #18 = Class              #24            // java/lang/System
  #19 = NameAndType        #25:#26        // out:Ljava/io/PrintStream;
  #20 = Class              #27            // java/io/PrintStream
  #21 = NameAndType        #28:#29        // println:(I)V
  #22 = Utf8               com/example/demo01/h
  #23 = Utf8               java/lang/Object
  #24 = Utf8               java/lang/System
  #25 = Utf8               out
  #26 = Utf8               Ljava/io/PrintStream;
  #27 = Utf8               java/io/PrintStream
  #28 = Utf8               println
  #29 = Utf8               (I)V
{
  public com.example.demo01.h();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 3: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=4, args_size=1
         0: bipush        10
         2: istore_1
         3: ldc           #3                  // int 32768
         5: istore_2
         6: iload_1
         7: iload_2
         8: iadd
         9: istore_3
        10: getstatic     #4                  // Field java/lang/System.out:Ljava/io/PrintStream;
        13: iload_3
        14: invokevirtual #5                  // Method java/io/PrintStream.println:(I)V
        17: return
      LineNumberTable:
        line 5: 0
        line 6: 3
        line 7: 6
        line 8: 10
        line 9: 17
}
SourceFile: "h.java"

```



#### 3） 常量池载入运行时常量池

- 大于Short.MAX_VALUE时会存入常量池

![image-20230302150041936](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302150041936.png)



#### 4）  字节码载入方法区

![image-20230302150310342](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302150310342.png)

#### 5）  main线程开始运行，分配栈帧内存

- stack = 2 局部操作栈的最大深度  locals = 4 局部变量表的长度
- ![image-20230302150509086](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302150509086.png)



#### 6） 执行引擎开始执行字节码

- bipush 10 将一个byte压入操作栈（其长度回补其四个字节）
- sipush将一个short压入操作栈
- ldc将一个int压入操作栈
- ldc2—w将一个long压入操作栈（分两次压入）
- 这里小的数字都是和字节码指令一起，超过short范围的数字存入常量池

![image-20230302150858308](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302150858308.png)



- istor_1将栈顶数据弹出，存在局部变量表slot1
- ![image-20230302151118316](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302151118316.png)

 **iload**

- 将局部变量表中的数据压入栈
- ![image-20230302151415018](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302151415018.png)





**getstatic #4**

- 在常量池中找#4对应的引用，找到堆中的对象
- ![image-20230302151610768](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302151610768.png)





**invokevirtual #5**

- 找到常量池#5项
- 定位到方法区java/io/PrintStream.println:(I)v方法
- 生成新的栈帧
- 传递参数，执行新栈帧中的字节码
- ![image-20230302152002546](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230302152002546.png)

- 执行完毕，弹出栈桢



### 3）练习分析

```java
public class h {
    public static void main(String[] args) {
        int a = 10;
        int b = a++ + ++a + a--;
        System.out.println(a);
        System.out.println(b);
    }
}
```

最终输出

```
11
34
```

**字节码文件**

- ```cmd
    Last modified 2023-3-2; size 428 bytes
    MD5 checksum eaebf53d9c10d6da788e6772b1a8413f
    Compiled from "h.java"
  public class com.example.demo01.h
    minor version: 0
    major version: 52
    flags: ACC_PUBLIC, ACC_SUPER
  Constant pool:
     #1 = Methodref          #5.#14         // java/lang/Object."<init>":()V
     #2 = Fieldref           #15.#16        // java/lang/System.out:Ljava/io/PrintStream;
     #3 = Methodref          #17.#18        // java/io/PrintStream.println:(I)V
     #4 = Class              #19            // com/example/demo01/h
     #5 = Class              #20            // java/lang/Object
     #6 = Utf8               <init>
     #7 = Utf8               ()V
     #8 = Utf8               Code
     #9 = Utf8               LineNumberTable
    #10 = Utf8               main
    #11 = Utf8               ([Ljava/lang/String;)V
    #12 = Utf8               SourceFile
    #13 = Utf8               h.java
    #14 = NameAndType        #6:#7          // "<init>":()V
    #15 = Class              #21            // java/lang/System
    #16 = NameAndType        #22:#23        // out:Ljava/io/PrintStream;
    #17 = Class              #24            // java/io/PrintStream
    #18 = NameAndType        #25:#26        // println:(I)V
    #19 = Utf8               com/example/demo01/h
    #20 = Utf8               java/lang/Object
    #21 = Utf8               java/lang/System
    #22 = Utf8               out
    #23 = Utf8               Ljava/io/PrintStream;
    #24 = Utf8               java/io/PrintStream
    #25 = Utf8               println
    #26 = Utf8               (I)V
  {
    public com.example.demo01.h();
      descriptor: ()V
      flags: ACC_PUBLIC
      Code:
        stack=1, locals=1, args_size=1
           0: aload_0
           1: invokespecial #1                  // Method java/lang/Object."<init>":()V
           4: return
        LineNumberTable:
          line 3: 0
  
    public static void main(java.lang.String[]);
      descriptor: ([Ljava/lang/String;)V
      flags: ACC_PUBLIC, ACC_STATIC
      Code:
        stack=2, locals=3, args_size=1
           0: bipush        10
           2: istore_1
           3: iload_1         //压入栈
           4: iinc          1, 1     //自增1
           7: iinc          1, 1    //自增一
          10: iload_1               //压入栈
          11: iadd                  
          12: iload_1
          13: iinc          1, -1
          16: iadd
          17: istore_2
          18: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
          21: iload_1
          22: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
          25: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
          28: iload_2
          29: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
          32: return
        LineNumberTable:
          line 5: 0
          line 6: 3
          line 7: 18
          line 8: 25
          line 9: 32
  }
  SourceFile: "h.java"
  
  ```

  





### 4）  条件判断指令

| **指令** | **助记符** | **含义**         |
| -------- | ---------- | ---------------- |
| 0x99     | ifeq       | 判断是否 == 0    |
| 0x9a     | ifne       | 判断是否 != 0    |
| 0x9b     | iflt       | 判断是否 < 0     |
|          |            |                  |
| 0x9d     | ifgt       | 判断是否 > 0     |
| 0x9e     | ifle       | 判断是否 <= 0    |
| 0x9f     | if_icmpeq  | 两个int 是否 ==  |
| 0xa0     | if_icmpne  | 两个int 是否 !=  |
| 0xa1     | if_icmplt  | 两个int 是否 <   |
| 0xa2     | if_icmpge  | 两个int 是否 >=  |
| 0xa3     | if_icmpgt  | 两个int 是否 >   |
| 0xa4     | if_icmple  | 两个int 是否 <=  |
| 0xa5     | if_acmpeq  | 两个引用是否 ==  |
| 0xa6     | if_acmpne  | 两个引用是否 !=  |
| 0xc6     | ifnull     | 判断是否 == null |
| 0xc7     | ifnonnull  | 判断是否 != null |

**几点说明：**

- byte，short，char 都会按int比较，因为操作数栈都是4字节
- goto 用来进行跳转到指定行号的字节码





### 5）  循环控制指令

**例如下面while循环**

- ```java
  public class h {
      public static void main(String[] args) {
          int a = 0;
          while (a < 10){
              a++;
          }
      }
  }
  ```

- 字节码是：

  ```cmd
           0: iconst_0      //0赋值给a
           1: istore_1      //加载到局部变量表
           2: iload_1       //压入栈
           3: bipush        10      //10压入栈
           5: if_icmpge     14      //如果满足跳动14
           8: iinc          1, 1    //自增一
          11: goto          2        //跳转到2
          14: return
  
  ```

  

### 6）  练习 结果分析

```java
public class h {
    public static void main(String[] args) {
        int i = 0;
        int x =0;
        while (i < 10){
            x = x++;
            i++;
        }
        System.out.println(x);
    }
}
```

运行结果为0

**下面从字节码来分析**

```cmd
          0: iconst_0
         1: istore_1        //0赋值局部变量表 i
         2: iconst_0      
         3: istore_2      //0赋值局部变量表 x
         4: iload_1        //i压入操作栈
         5: bipush        10      //10压入栈
         7: if_icmpge     21        //对比成功 跳到21
        10: iload_2                  //x压入栈
        11: iinc          2, 1      //局部变量表x自增
        14: istore_2                //栈中x弹出，覆盖局部变量表
        15: iinc          1, 1      //i自增
        18: goto          4         //跳到4
        21: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
        24: iload_2
        25: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
        28: return

```





### 7）  构造方法

####     1 ）cinit()v

- ```java
  public class x {
      static int i = 10;
  
      static {
          i = 20;
      }
  
      static {
          i = 30;
      }
  }
  ```

**i最终会赋值为30**

- 编译器会按照从上至下的顺序，收集所有static静态代码块和静态成员变量复制的代码，合并为一个特殊的方法

  ```
  <cinit>()V
  ```

- 该方法会在类加载的初始化阶段被调用

```cmd
        0: bipush        10
         2: putstatic     #2                  // Field i:I
         5: bipush        20
         7: putstatic     #2                  // Field i:I
        10: bipush        30
        12: putstatic     #2                  // Field i:I
```



#### 2） init

- 编译器会按照从上至下的顺序，收集所有{}代码块和成员变量复制的代码，形成新的构造方法，但原始的构造方法的代码总在最后







### 8） 方法调用

**看一下几种方法调用对应的字节码指令**

```java
public class demo01 {
    public demo01(){}

    private void test1(){}

    private final void test2(){}

    public void test3(){}

    public static void test4(){}


    public static void main(String[] args) {
        demo01 d = new demo01();
        d.test1();
        d.test2();
        d.test3();
        d.test4();
        demo01.test4();
    }

}
```

**字节码文件**

```cmd
         0: new           #2                  // class com/example/classma/demo01  分配成功将引用放入操作数栈
         3: dup                               //将操作数栈引用复制一份
         4: invokespecial #3                  // Method "<init>":()V   //构造方法invokespecial
         7: astore_1
         8: aload_1
         9: invokespecial #4                  // Method test1:()V    //私有 invokespecial
        12: aload_1
        13: invokespecial #5                  // Method test2:()V   //final修饰的也是 invokespecial
        16: aload_1
        17: invokevirtual #6                  // Method test3:()V   //public方法invokevirtual  动态绑定
        20: aload_1
        21: pop
        22: invokestatic  #7                  // Method test4:()V   //静态方法 invokestatic
        25: invokestatic  #7                  // Method test4:()V
        28: return

```





### 9） 多态的原理

- 当执行invokevirtual指令时
  1. 先通过栈帧中的对象引用找到对象
  2. 分析对象头，找到对象的实际class
  3. Class结构中有vtable(虚方法表），它在类加载的链接阶段就已经根据方法的重写规则生成好的
  4. 查表得到方法的具体地址
  5. 执行方法的字节码
