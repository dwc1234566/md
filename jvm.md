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

 

### 1）  魔树

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







### 10） 异常处理

#### 1） **try-catch**

```java
public class demo03 {
    public static void main(String[] args) {
        int i = 0;
        try {
          i = 10;
        }catch (Exception e){
            i  = 20;
        }
    }
}
```

**字节码文件**

```java
 public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=3, args_size=1
         0: iconst_0
         1: istore_1
         2: bipush        10
         4: istore_1
         5: goto          12  //没有异常直接return
         8: astore_2
         9: bipush        20
        11: istore_1
        12: return
      Exception table:             //异常表
         from    to  target type
             2     5     8   Class java/lang/Exception   //检测2至4行 如果发生的异常与该异常匹配则进入target
      LineNumberTable:
        line 5: 0
        line 7: 2
        line 10: 5
        line 8: 8
        line 9: 9
        line 11: 12
      StackMapTable: number_of_entries = 2
        frame_type = 255 /* full_frame */
          offset_delta = 8
          locals = [ class "[Ljava/lang/String;", int ]
          stack = [ class java/lang/Exception ]
        frame_type = 3 /* same */
}

```







#### 2） try-catc-finally

```java
public class demo04 {
    public static void main(String[] args) {
        int i = 10;
        try {
            i = 20;
        }catch (Exception e){
            i = 30;
        }finally {
            i = 40;
        }
    }
}
```

**字节码**

```java
 public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=4, args_size=1
         0: bipush        10
         2: istore_1       
         3: bipush        20
         5: istore_1     
         6: bipush        40
         8: istore_1        
         9: goto          28
        12: astore_2
        13: bipush        30
        15: istore_1       
        16: bipush        40
        18: istore_1
        19: goto          28
        22: astore_3
        23: bipush        40
        25: istore_1
        26: aload_3
        27: athrow
        28: return
      Exception table:
         from    to  target type
             3     6    12   Class java/lang/Exception
             3     6    22   any             //对应着其他异常例如error
            12    16    22   any                  //对应着其他异常例如error
      LineNumberTable:
        line 5: 0
        line 7: 3
        line 11: 6
        line 12: 9
        line 8: 12
        line 9: 13
        line 11: 16
        line 12: 19
        line 11: 22
        line 12: 26
        line 13: 28
      StackMapTable: number_of_entries = 3
        frame_type = 255 /* full_frame */
          offset_delta = 12
          locals = [ class "[Ljava/lang/String;", int ]
          stack = [ class java/lang/Exception ]
        frame_type = 73 /* same_locals_1_stack_item */
          stack = [ class java/lang/Throwable ]
        frame_type = 5 /* same */
}

```

- 可以看到finall中的代码被复制了三份，分别放入try，catch，及catch剩余的异常类型保证finall中的代码一定会执行









### 11） 练习 - finally面试题

####    finally出现了return

- ​    看下面代码

- ```java
  public class demo05 {
      public static void main(String[] args) {
          int res = test();
          System.out.println(res);
      }
  
      public static int test(){
          try {
              return 10;
          }finally {
              return 20;
          }
      }
  }
  ```

- 最终输出 20 为什么呢？ 我们看下字节码

- ```java
   public static void main(java.lang.String[]);
      descriptor: ([Ljava/lang/String;)V
      flags: ACC_PUBLIC, ACC_STATIC
      Code:
        stack=2, locals=2, args_size=1
           0: invokestatic  #2                  // Method test:()I
           3: istore_1
           4: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
           7: iload_1
           8: invokevirtual #4                  // Method java/io/PrintStream.println:(I)V
          11: return
        LineNumberTable:
          line 5: 0
          line 6: 4
          line 7: 11
    
    public static int test();
      descriptor: ()I
      flags: ACC_PUBLIC, ACC_STATIC
      Code:
        stack=1, locals=2, args_size=0
           0: bipush        10
           2: istore_0                    //将10放入局部变量表第0个槽位
           3: bipush        20
           5: ireturn                     //返回栈顶值20
           6: astore_1                     
           7: bipush        20            //20放入栈顶
           9: ireturn                       //依然返回20
        Exception table:
           from    to  target type
               0     3     6   any
        LineNumberTable:
          line 12: 0
          line 14: 3
        StackMapTable: number_of_entries = 1
          frame_type = 70 /* same_locals_1_stack_item */
            stack = [ class java/lang/Throwable ]
  }
  
  ```

  - 由于finall中的ireturn被插入了所有可能的流程，因此返回结果肯定以finall的为准

  - 至于字节码第2行，似乎没啥用，看下个例子

  - 跟上例中的finally相比，发现没有athrow了，这告诉我们  如果在finall中出现了return，会吞掉异常😱😱😱，可以试一下下面代码

  - ```java
    public class demo06 {
        public static void main(String[] args) {
            int i = test();
            System.out.println(i);
        }
    
    
        public static int test(){
            int i =10;
            try {
                i = i/0;
            }finally {
                System.out.println("异常");
                return i;
            }
        }
    
    }
    ```

    - 这个代码就不会抛出异常，因为在处理any异常时最终有return忽略了异常





### 12） synchronized

-   我们是如何保证枷锁之后出现异常也能解锁的呢

- ```java
  public class demo07 {
      public static void main(String[] args) {
          Object o = new Object();
          synchronized (o){
              System.out.println("ok");
          }
      }
  }
  ```

  

- 看下字节码

- ```java
  
    public static void main(java.lang.String[]);
      descriptor: ([Ljava/lang/String;)V
      flags: ACC_PUBLIC, ACC_STATIC
      Code:
        stack=2, locals=4, args_size=1
           0: new           #2                  // class java/lang/Object
           3: dup                               //将对象的引用复制一份
           4: invokespecial #1                  // Method java/lang/Object."<init>":()V  初始化方法
           7: astore_1                           //0引用->0
           8: aload_1                           //o放入栈顶
           9: dup                               //复制o引用 一份加锁 一份解锁
          10: astore_2                          //复制的o引用放入slot2
          11: monitorenter                      //加锁  monitorexit（o引用）
          12: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
          15: ldc           #4                  // String ok
          17: invokevirtual #5                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
          20: aload_2
          21: monitorexit                       //解锁 解锁monitorexit（o引用）
          22: goto          30                  //返回
          25: astore_3                          //any-> slot3
          26: aload_2                           //o引用入栈
          27: monitorexit                       //解锁monitorexit（o引用）
          28: aload_3
          29: athrow                            //抛出any
          30: return
        Exception table:
           from    to  target type
              12    22    25   any         //12-21 发生异常  跳转25
              25    28    25   any           //25-27 发生异常  跳转25
        LineNumberTable:
          line 5: 0
          line 6: 8
          line 7: 12
          line 8: 20
          line 9: 30
        StackMapTable: number_of_entries = 2
          frame_type = 255 /* full_frame */
            offset_delta = 25
            locals = [ class "[Ljava/lang/String;", class java/lang/Object, class java/lang/Object ]
            stack = [ class java/lang/Throwable ]
          frame_type = 250 /* chop */
            offset_delta = 4
  }
  
  ```

  

- 方法级别的synchronized不会再字节码中有所体现









## 3  编译期处理



**所谓语法糖，其实就是指java编译器把*.java源码编译为*.class字节码的过程中，自动生成和转换一些代码，主要是为了减轻程序员的负担，算是java编译器给我们的一个额外福利**





### 3.1  默认构造器

```java
public class h{

}
```

编译成class后的代码

```java
 public class h{
   //这个无参构造是编译器给我们加上的
    public h(){
       super();
    }
 }
```





### 3.2 自动装箱和拆箱

```java
public class Candy1 {
    public static void main(String[] args) {
        Integer x = 1;
        int y =x;
    }
}
```

编译成class后的代码

```java
public class Candy1 {
    public Candy1() {
    }

    public static void main(String[] var0) {
        Integer var1 = Integer.valueOf(1);
        int var2 = var1.intValue();
    }
}
```







### 3.3  泛型集合取值



泛型是在JDK5 开始加入的新特性，但在编译泛型代码后会执行泛型擦除的动作，即泛型信息在编译成字节码之后就丢失了，实际上类型当作了Object类型来处理 	

 

```java
public class Candy2 {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        l.add(10);     //  实际上调用了list.add(Obiect e)
        Integer integer = l.get(0); //实际上是Obiect O = l.get(0)
    }
}
```

字节码文件

```java
  Last modified 2023-3-3; size 516 bytes
  MD5 checksum 30e51efbaf040d90c03e3cbbeddf25ef
  Compiled from "Candy2.java"
public class com.example.candy.Candy2
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
   #1 = Methodref          #9.#18         // java/lang/Object."<init>":()V
   #2 = Class              #19            // java/util/ArrayList
   #3 = Methodref          #2.#18         // java/util/ArrayList."<init>":()V
   #4 = Methodref          #7.#20         // java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
   #5 = InterfaceMethodref #21.#22        // java/util/List.add:(Ljava/lang/Object;)Z
   #6 = InterfaceMethodref #21.#23        // java/util/List.get:(I)Ljava/lang/Object;
   #7 = Class              #24            // java/lang/Integer
   #8 = Class              #25            // com/example/candy/Candy2
   #9 = Class              #26            // java/lang/Object
  #10 = Utf8               <init>
  #11 = Utf8               ()V
  #12 = Utf8               Code
  #13 = Utf8               LineNumberTable
  #14 = Utf8               main
  #15 = Utf8               ([Ljava/lang/String;)V
  #16 = Utf8               SourceFile
  #17 = Utf8               Candy2.java
  #18 = NameAndType        #10:#11        // "<init>":()V
  #19 = Utf8               java/util/ArrayList
  #20 = NameAndType        #27:#28        // valueOf:(I)Ljava/lang/Integer;
  #21 = Class              #29            // java/util/List
  #22 = NameAndType        #30:#31        // add:(Ljava/lang/Object;)Z
  #23 = NameAndType        #32:#33        // get:(I)Ljava/lang/Object;
  #24 = Utf8               java/lang/Integer
  #25 = Utf8               com/example/candy/Candy2
  #26 = Utf8               java/lang/Object
  #27 = Utf8               valueOf
  #28 = Utf8               (I)Ljava/lang/Integer;
  #29 = Utf8               java/util/List
  #30 = Utf8               add
  #31 = Utf8               (Ljava/lang/Object;)Z
  #32 = Utf8               get
  #33 = Utf8               (I)Ljava/lang/Object;
{
  public com.example.candy.Candy2();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 6: 0

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
         0: new           #2                  // class java/util/ArrayList
         3: dup
         4: invokespecial #3                  // Method java/util/ArrayList."<init>":()V
         7: astore_1
         8: aload_1
         9: bipush        10
        11: invokestatic  #4                  // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;  泛型包装
        14: invokeinterface #5,  2            // InterfaceMethod java/util/List.add:(Ljava/lang/Object;)Z
        19: pop
        20: aload_1
        21: iconst_0
        22: invokeinterface #6,  2            // InterfaceMethod java/util/List.get:(I)Ljava/lang/Object;
        27: checkcast     #7                  // class java/lang/Integer   泛型转换
        30: astore_2
        31: return
      LineNumberTable:
        line 8: 0
        line 9: 8
        line 10: 20
        line 11: 31
}

```

- 类型擦除的是字节码上的信息，可是看到LocalVariableTypeTable仍然保留了方法参数泛型信息





### 3.4  可变参数

```java
public void tset(String... args){

}
//会被编译为  
public void tset(String[] args){

}
//数组长度为传入参数的数量
```





### 3.5  foreach循环

```java
public class Candy3 {
    public static void main(String[] args) {
        int[] array = {0,1,2,3,4,5};  
        for (int i : array) {
            System.out.println(i);
        }
    }
}
```

字节码文件

```java
public class Candy3 {
    public Candy3() {
    }

    public static void main(String[] var0) {
        int[] var1 = new int[]{0, 1, 2, 3, 4, 5};  //新建数组语法糖
        int[] var2 = var1;
        int var3 = var1.length;

        for(int var4 = 0; var4 < var3; ++var4) {  //遍历语法糖
            int var5 = var2[var4];
            System.out.println(var5);
        }

    }
}
```

集合遍历呢

```java
public class Candy4 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        for (Integer integer : list) {
            System.out.println(integer);
        }
    }
}
```

字节码

```java
public class Candy4 {
    public Candy4() {
    }

    public static void main(String[] var0) {
        ArrayList var1 = new ArrayList();
        var1.add(0);
        var1.add(1);
        var1.add(2);
        var1.add(3);
        var1.add(4);
        Iterator var2 = var1.iterator();   //实际编译为Iterator遍历

        while(var2.hasNext()) {
            Integer var3 = (Integer)var2.next();
            System.out.println(var3);
        }

    }
}
```







### 3.6  switch 字符串

从jdk1.7开始switch可以用于字符串和枚举类，这个功能其实也是语法糖，例如：

```java
public class Candy5 {

    public void choose(String str){
        switch (str){
            case "hello" :{
                System.out.println("h");
            }
            case "world" :{
                System.out.println("w");
            }
        }
    }
}
```

会被编译器转换为

![image-20230303113345102](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230303113345102.png)

- 可以看到，执行了两遍switch，第一遍是根据字符串的hashcode和equals将字符串的抓换响应byte类型，第二遍才是利用byte执行进行比较
- 为什么第一遍时必须比较hashcode，又利用equals比较呢？hashcode是为了提高效率，减少可能的比较；而equals是为了防止hashcode冲突，例如BM和c，这两个字符串的hashcode值都是2123







### 3.7 switch枚举

```java
enum Sex {
    MALE,
    FEMALE
}
public class Candy6 {
   public static void fool(Sex sex){
       switch (sex){
           case MALE:
               System.out.println("男");
               break;
           case FEMALE:
               System.out.println("女");
               break;
       }
   }
}
```

转换后的代码

![image-20230303141415711](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230303141415711.png)







### 3.8 枚举类字节码

```java
enum Sex {
    MALE,
    FEMALE
}
```



字节码

```java
  Last modified 2023-3-3; size 848 bytes
  MD5 checksum bbe69fa903f3a77c0653be35dfa25036
  Compiled from "Candy6.java"
final class com.example.candy.Sex extends java.lang.Enum<com.example.candy.Sex>
  minor version: 0
  major version: 52
  flags: ACC_FINAL, ACC_SUPER, ACC_ENUM
Constant pool:
   #1 = Fieldref           #4.#32         // com/example/candy/Sex.$VALUES:[Lcom/example/candy/Sex;
   #2 = Methodref          #33.#34        // "[Lcom/example/candy/Sex;".clone:()Ljava/lang/Object;
   #3 = Class              #17            // "[Lcom/example/candy/Sex;"
   #4 = Class              #35            // com/example/candy/Sex
   #5 = Methodref          #12.#36        // java/lang/Enum.valueOf:(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
   #6 = Methodref          #12.#37        // java/lang/Enum."<init>":(Ljava/lang/String;I)V
   #7 = String             #13            // MALE
   #8 = Methodref          #4.#37         // com/example/candy/Sex."<init>":(Ljava/lang/String;I)V
   #9 = Fieldref           #4.#38         // com/example/candy/Sex.MALE:Lcom/example/candy/Sex;
  #10 = String             #15            // FEMALE
  #11 = Fieldref           #4.#39         // com/example/candy/Sex.FEMALE:Lcom/example/candy/Sex;
  #12 = Class              #40            // java/lang/Enum
  #13 = Utf8               MALE
  #14 = Utf8               Lcom/example/candy/Sex;
  #15 = Utf8               FEMALE
  #16 = Utf8               $VALUES
  #17 = Utf8               [Lcom/example/candy/Sex;
  #18 = Utf8               values
  #19 = Utf8               ()[Lcom/example/candy/Sex;
  #20 = Utf8               Code
  #21 = Utf8               LineNumberTable
  #22 = Utf8               valueOf
  #23 = Utf8               (Ljava/lang/String;)Lcom/example/candy/Sex;
  #24 = Utf8               <init>
  #25 = Utf8               (Ljava/lang/String;I)V
  #26 = Utf8               Signature
  #27 = Utf8               ()V
  #28 = Utf8               <clinit>
  #29 = Utf8               Ljava/lang/Enum<Lcom/example/candy/Sex;>;
  #30 = Utf8               SourceFile
  #31 = Utf8               Candy6.java
  #32 = NameAndType        #16:#17        // $VALUES:[Lcom/example/candy/Sex;
  #33 = Class              #17            // "[Lcom/example/candy/Sex;"
  #34 = NameAndType        #41:#42        // clone:()Ljava/lang/Object;
  #35 = Utf8               com/example/candy/Sex
  #36 = NameAndType        #22:#43        // valueOf:(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
  #37 = NameAndType        #24:#25        // "<init>":(Ljava/lang/String;I)V
  #38 = NameAndType        #13:#14        // MALE:Lcom/example/candy/Sex;
  #39 = NameAndType        #15:#14        // FEMALE:Lcom/example/candy/Sex;
  #40 = Utf8               java/lang/Enum
  #41 = Utf8               clone
  #42 = Utf8               ()Ljava/lang/Object;
  #43 = Utf8               (Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
{
  public static final com.example.candy.Sex MALE;
    descriptor: Lcom/example/candy/Sex;
    flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL, ACC_ENUM

  public static final com.example.candy.Sex FEMALE;
    descriptor: Lcom/example/candy/Sex;     
    flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL, ACC_ENUM

  public static com.example.candy.Sex[] values();
    descriptor: ()[Lcom/example/candy/Sex;
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=0, args_size=0
         0: getstatic     #1                  // Field $VALUES:[Lcom/example/candy/Sex;
         3: invokevirtual #2                  // Method "[Lcom/example/candy/Sex;".clone:()Ljava/lang/Object;
         6: checkcast     #3                  // class "[Lcom/example/candy/Sex;"
         9: areturn
      LineNumberTable:
        line 3: 0

  public static com.example.candy.Sex valueOf(java.lang.String);
    descriptor: (Ljava/lang/String;)Lcom/example/candy/Sex;
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: ldc           #4                  // class com/example/candy/Sex
         2: aload_0
         3: invokestatic  #5                  // Method java/lang/Enum.valueOf:(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
         6: checkcast     #4                  // class com/example/candy/Sex
         9: areturn
      LineNumberTable:
        line 3: 0

  static {};
    descriptor: ()V
    flags: ACC_STATIC
    Code:
      stack=4, locals=0, args_size=0
         0: new           #4                  // class com/example/candy/Sex
         3: dup
         4: ldc           #7                  // String MALE
         6: iconst_0
         7: invokespecial #8                  // Method "<init>":(Ljava/lang/String;I)V
        10: putstatic     #9                  // Field MALE:Lcom/example/candy/Sex;
        13: new           #4                  // class com/example/candy/Sex
        16: dup
        17: ldc           #10                 // String FEMALE
        19: iconst_1
        20: invokespecial #8                  // Method "<init>":(Ljava/lang/String;I)V
        23: putstatic     #11                 // Field FEMALE:Lcom/example/candy/Sex;
        26: iconst_2
        27: anewarray     #4                  // class com/example/candy/Sex
        30: dup
        31: iconst_0
        32: getstatic     #9                  // Field MALE:Lcom/example/candy/Sex;
        35: aastore
        36: dup
        37: iconst_1
        38: getstatic     #11                 // Field FEMALE:Lcom/example/candy/Sex;
        41: aastore
        42: putstatic     #1                  // Field $VALUES:[Lcom/example/candy/Sex;
        45: return
      LineNumberTable:
        line 4: 0
        line 5: 13
        line 3: 26
}
Signature: #29             
```









### 3.9  try-with-resources

JDK1.7 新增了对需要关闭的资源处理的特殊语法try-with-resources

```java
try(资源变量 = 创建资源对象){

}catch(){

}
```

- 其中资源对象需要实现AutoCloseable接口，例如 InputStream  OutputStream 等接口都实现了AutoCloseable，使用try-with-resources可以不用写finally语句块，编译器会帮助生成关闭资源代码，例如：

- ```java
  public class Candy7 {
      public Candy7() {
      }
  
      public static void main(String[] var0) {
          try {
              FileInputStream var1 = new FileInputStream("D:\\新建文件夹\\a.txt");
              Throwable var2 = null;
  
              try {
                  System.out.println(var1);
              } catch (Throwable var12) {
                  var2 = var12;
                  throw var12;
              } finally {     //可以看到在编译期间编译器自动生成了用于关流的finally代码块
                  if (var1 != null) {   //判断资源不为空
                      if (var2 != null) {     //如果有异常
                          try {
                              var1.close();
                          } catch (Throwable var11) {
                              //如果close出现异常，作为被压制异常添加
                              var2.addSuppressed(var11);
                          }
                      } else {
                          var1.close();  //关闭资源
                      }
                  }
  
              }
  
          } catch (IOException var14) {
              throw new RuntimeException(var14);
          }
      }
  }
  ```

  



### 3.10   方法重写时的桥接方法

**我们都知道，方法重写时分为两种情况：**

- 父类子类返回值完全一致

- 子类返回值可以是父类返回值的子类

- ```java
  public class Candy8 {
  
      public Number m(){
          return 1;
      }
  }
  
  class b extends Candy8{
      @Override
      public Integer m() {
          return 1;
      }
  ```

  ![image-20230303150331124](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230303150331124.png)









### 3.11   匿名内部类

**源代码**

```java
public class Candy9 {
    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        };
    }
}
```



**编译后的文件**

- ```java
  //会额外生成类 
  final class Candy9$1 implements Runnable {
      Candy9$1() {
      }
  
      public void run() {
          System.out.println("ok");
      }
  }
  ```

- 外部的类事实上使用了新生成的类



**如果方法有传入的参数**

```java
public class Candy10 {
    public static void test(final int x) {
       Runnable r = new Runnable() {
           @Override
           public void run() {
               System.out.println("ok"+x);
           }
       };
    }
}
```

**生成的类**

```java
package com.example.candy;
 //会多一个参数
final class Candy10$1 implements Runnable {
    Candy10$1(int var1) {
        this.val$x = var1;
    }

    public void run() {
        System.out.println("ok" + this.val$x);
    }
}
```

注意

  这同时解释了为什么匿名内部类引用局部变量时，局部变量必须是final的；因为在创建时Candy10$1对喜爱那个是，将x的值赋给了Candy10$1对象的valx属性，所以x不应该在发生变化了，如果变化，那么valx属性没有机会再跟着一起变化











## 4   类加载阶段



### 4.1 加载

- 将类的字节码载入方法区中，内部采用c++的instanceKlass描述java类，它的重要filed有：
  - _java_mirror即java类的镜像，例如对String来说，就是String.class，作用是吧Klass暴露给java使用，存在于堆中
  - _super即父类
  - _constants即常量池
  - _class_loader即类加载器
  - _vtable虚方法表
  - _itable接口方法表
- 如果这个类的父类还没有加载完成，先加载父类
- 加载和链接可能是交替运行的

------

![image-20230303194307269](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230303194307269.png)





### 4.2 链接

- **验证**：验证字节码文件是否符合JVM规范，安全性检查。
- **准备** ：为static变量分配空间，设置默认值
  - static变量存储与_java_mirror末尾
  - static变量分配空间和赋值时两个步骤，分配空间在准备阶段完成，赋值在初始化阶段完成
  - 如果static变量是final的基本类型，那么编译阶段就确定了，赋值在准备阶段完成
  - 如果static变量是final的，但属于引用类型，那么赋值也会在初始化阶段完成

- **解析**

  将常量池中的符号解析为直接引用 ，知道类属性方法的具体位置





### 4.3 初始化



**<cinit>()V方法**

初始化即调用<cinit>()V，虚拟机会保证这个类的构造方法的线程安全







**发生的时机**

- 概括地说，**类初始化是懒惰的**

- main方法所在的类，总是被首先初始化

- 首次访问这个类的静态变量或者静态方法时

- 子类初始化，如果父类没有初始化，会引发父类初始化

- 子类访问父类静态变量，只会触发父类初始化

- Class.forName

- new 会导致初始化

  **不会导致初始化的情况**

- 访问类的static final静态常量（基本类型，字符串）时不会触发初始化
- 类对象.class不会触发初始化
- 创建该类的数组时不会触发初始化
- 类加载器loadcass不会触发初始化
- Class.forName第二个参数为false时不会触发



### 4.5   练习

**下面属性哪个会导致E的初始化**

```java
public class load1 {
    public static void main(String[] args) {
        System.out.println(E.a);
        System.out.println(E.b);
        System.out.println(E.c);
        System.out.println(E.d);
    }
}
class E{
    public static final int a = 10;
    public static final String b = "hello";
    public static final Integer c = 20;
    public static final String  d = new String("herrr");
    static {
        System.out.println("E INIT");
    }
}
```

- 答案是a，b不会导致初始化，c，d会导致初始化



**典型应用：完成懒惰初始化单例模式**

```java
class Singleten{
    private Singleten(){}

    private static class LazyHolder{
        private static final Singleten SINGLETEN = new Singleten();
    }

    public Singleten getInstance(){
        return LazyHolder.SINGLETEN;
    }
}
```

- 以上的实现特点是
- 懒惰实例化
- 初始化时线程安全是有保障的





## 5 类加载器

**以JDK8为例**

**名称**                                                      **加载哪些类**                                               **说明** 

------

Bootstrap  ClassLoader                    JAVA_home/jre/lib                             无法直接访问

------

Extension  ClassLoader                    JAVA_HOME/jre/lib/ext                      上级为Bootstrap  ，显示为null

------

Application ClassLoader                    classpath                                              上级为Extension  

------

自定义类加载器                                  自定义                                                      上级为Application 







###                                             5.1 启动类加载器

用Bootstrap  类加载器加载类

```java

public class F {
    static {
        System.out.println("f init");
    }
}

```

执行

```java
public class load3 {
    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> aClass = Class.forName("com.example.load.F");
        System.out.println(aClass.getClassLoader());   //appclassloader      extclassloader
    }
}
```

![image-20230304105157876](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230304105157876.png)







###                                             5.2   扩展类加载器









































###                                                        5.3   双亲委派模式



































# 内存模型





很多人将【java内存结构】和【java内存模型】分不清楚，java内存模型是JAVA Memory Model（JMM）的意思

简单的来说JMM定义了一套在多线程读写共享数据时，对数据可见性，有序性，和原子性的规则和保障







## 1     原子性

  怎么保证多线程下对对共享变量的操作是原子性的呢



我们可以使用synchronized（同步关键字）

**语法**

```java
synchronzied (对象){
   要作为原子操作的代码
}
```

**用synchronized解决并发问题**

```java
   static int i = 0;
    static Object o = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() ->{
            for (int i1 = 0; i1 < 5000; i1++) {
                synchronized (o){
                    i++;
                }
            }
        }).start();
        new Thread(() -> {
            for (int i1 = 0; i1 < 4500; i1++) {
                synchronized (o){
                    i--;
                }
            }
        }).start();
        Thread.sleep(5000);
        System.out.println(i);
    }
```

、







##                                           2   可见性







###                                               2.1  退不出的循环

**有下面一段代码**

```java
 static  boolean run = true;

    public static void main(String[] args) throws InterruptedException {
          Thread t = new Thread(()->{
              while (run){

              }
          });

          t.start();
          Thread.sleep(1000);
          run = false;
    }
```

- 我们会发现这段代码是停不下来的，，，为什么呢？

1. 初始状态，t线程从主存读取到了run的值存储在工作内存中
2. ![](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305145307636.png)
3. 因为t线程频繁的从主存中获取值，JIT编译器会将run的值缓存至自己的工作内存中，减少对主存中run的访问，提高效率
4. ![image-20230305145640343](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305145640343.png)

​     main线程修改了run的值但是t线程读的仍然是缓存中的值







### 2.2   解决方法

volatile关键字

takeyi用来修饰成员变量和静态成员变量，它可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，线程操作volatile变量都是直接操作主存

上述例子我们发现 run变量加上volatile之后，程序在一秒后退出





### 2.3  volatile

前面例子体现的就是可见性，他保证的是在多个线程之间，一个线程对volatil变量的修改对另一个线程不可见，不能保证原子性，仅用在一个写线程，多个读线程的情况：

![image-20230305153523873](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305153523873.png)



**注意**

synchronized关键字语句块既可以保证原子性，也可以保证代码块的变量的可见性。但缺点是，synchronized是属于重量级操作，性能相对较低

如果前面例子死循环中加入system.out.println()会发现即使不加volatile修饰符，线程t也能看到run变量的修改，想一想为什么？



因为println的底层是synchroinzed修饰的

```java
    public void println(boolean x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }
```







## 3   有序性





### 3.1   诡异的结果

![image-20230305154311889](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305154311889.png)



**执行上面方法会有几种结果呢**

![image-20230305154410844](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305154410844.png)

**为什么结果中会出现0呢？是因为num和ready的赋值顺序发生了改变， 这种现象叫做指令重排，是JIT编译器在运行时做的优化**







### 3.2     解决方法

可以用volatile关键字修饰该变量，就可以禁止指令重排。

这体现了volatile的有序性







### 3.3   有序性的理解







同一个线程内，JVM在不影响正确性的前提下，可以调整语句的执行顺序，

```java
static int i;
static int j;


//在某个线程内执行如下赋值操作
i = .....;   //较为耗时的操作
j = ....;
```

可以看到无论是给 i赋值，还是先给 j赋值，对最终结果不会产生影响，所以上面的代码可能发生指令重排

```java
j = ....;
i = .....;   //较为耗时的操作
```

这种特性称之为【指令重排】，多线程下【指令重排】会影响正确性，例如著名的double-checked locking模式实现单例

```java
    final class  SingleTon{
        private SingleTon(){}
        private static SingleTon INSTANCE =null;
        public static SingleTon getInstance(){
            //doucle-check 
            if (INSTANCE == null){
                synchronized (SingleTon.class){
                         if (INSTANCE == null){
                             INSTANCE = new SingleTon();
                         }
                }
            }
            return INSTANCE;
        }
        
     }
```

以上的特点是：

- 懒惰实例化
- 首次调用getInstance时才使用synchronized加锁，后续使用无需加锁

但在多线程环境下，上面代码是有问题的， INSTANCE = new SingleTon();对应的字节码为：

```cmd
        17: new           #3                  // class sssss/SingleTon
        20: dup
        21: invokespecial #4                  // Method "<init>":()V
        24: putstatic     #2                  // Field INSTANCE:Lsssss/SingleTon;

```

其中21 和 24 代码时不固定的，也许jvm会优化为：先将引用地址赋值给INSTANCE变量后，在执行构造方法，如果两个线程t1，t2按如下时间顺序执行：

![image-20230305161120662](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305161120662.png)

这是t1还未将构造方法执行完毕，那么t2拿到的将是一个未初始化完成的单例，虽然这种情况发生的概率很小，但是为了杜绝此类情况的发生我们可以用volatil修饰INSTANC变量







### *3.4    happens-before*

hhappens-before规定了哪些写操作对其他线程的读操作可见，它是可见性和有序性的一套规则总结：

- 线程解锁m之前对变量的写，对于接下来对m加锁的其他线程变量的读可见

```java
static int x;
static Object m = new Object();

new Thread(() -> {
   synchronized(m){
      x = 10;
   }

},"t1").start();

new Thread(() -> {
   synchronized(m){
       System.out.println(x);
   }

},"t1").start();

```



- 线程对volatile变量的写，对接下来线程对该变量的读可见

- ```java
  volatile static int x;
  
  new Thread(() -> {
    x = 10;
  },"t1").start();
  
  
  new Thread(() -> {
    System.out.println(x);
  },"t2").start();
  ```

  

- 线程start前对变量的写，对该线程开始后的读可见

  ```java
   static int x;
    
   x = 0;
   
   new Thread(() -> {
    System.out.println(x);
  },"t2").start();
  ```

- 线程结束前对变量的写，对其他线程得知他结束后的读可见

```java
 static int x;
 Thread t1 = new Thread(() ->{
     x = 10;
 },"t1");
 t1.start();
 
 t1.join();
 System.out.println(x);
```

- 线程t1打断t2（interrupt）前对变量的写，对于其他线程得知t2被打断后对变量的读可见

![image-20230305162913958](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230305162913958.png)

- 对变量默认值（0，false，null）的写，对其他线程可见
- 具有传递性，如果 x  hb>y , y hb>z  则  x hb->z







##                                   4   CAS与原子类



###                                                 4.1  CAS

CAS即Compare and Sweep，它体现的是一种乐观锁思想，比如多个线程对一个共享整型变量+1操作：

```java
//需要不断尝试
while(true){
   int 旧值 = 共享变量;   //比如拿到了当前值 0
   int 结果 = 旧值+1;  
  
  if(compareAndSweep(旧值，结果)){
     //成功 退出循环
  }

}
```

获取共享变量时，为了保证变量的可见性，需要使用volatile修饰。结合CAS和volatile可以实现无锁并发，适用于竞争激烈，多核CPU场景下：

- 因为没有使用synchronized，所以线程不会陷入阻塞，这是效率提升的因素之一
- 但如果竞争激烈，可能会引起频繁自旋，反而效率会受影响

CAS依赖于一个unsafe类来调用操作系统底层的CAS指令，下面使用UnSafe对象进行线程安全保护的一个例子：

```java
class DataContainer{
    private volatile int data;
    static final Unsafe UNSAFE ;
    static final long  DATA_OFFSET;
    //初始化UNSAFE 和DATA_OFFSET
    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE= (Unsafe)theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            //data属性在DataContainer中的偏移量，用于Unsafe直接访问该对象
            DATA_OFFSET = UNSAFE.objectFieldOffset(DataContainer.class.getDeclaredField("data"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    public DataContainer(int i) {
        this.data = i;
    }
    public void increase(){
        int oldValue;
        while (true){
            oldValue = data;
            if (UNSAFE.compareAndSwapInt(this,DATA_OFFSET,oldValue,oldValue+1)){
                return;
            }

        }
    }
    public void decrease(){
        int oldValue;
        while (true){
            oldValue = data;
            if (UNSAFE.compareAndSwapInt(this,DATA_OFFSET,oldValue,oldValue-1)){
                return;
            }
        }
    }

    public int getData(){
        return this.data;
    }

}
```







###                                                        4.2   乐观锁与悲观锁













###                                                            4.3  原子操作类











## 5   synchronized优化



Java HotSpot虚拟机中，每个对象都有对象头（包含class指针和Mark  Word）。Mark Word 平时存储这个对象的   

**哈希码、分代年龄、**，当加锁时，这些信息就根据情况被替换为**标记位、线程锁记录指针、重量级锁指针、线程id等内容**



### 

###                                         5.1 轻量级锁

 如果一个对象虽然有多线程访问，但多线程访问的时间是错开的，那么可以使用轻量级锁来优化。这就好比：

![image-20230306104340530](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306104340530.png)

假设有两个同步代码块，利用同一个对象加锁：

```java
static Object obj = new Object();
public static void method1(){
    synchronized(obj){
       method()2;
    }
}


public static void method2(){
    synchronized(obj){
      
    }
}
```

**每个线程的栈帧都包含一个锁记录的结构，内部都可以存储锁对象的Mark word**

![image-20230306141727551](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306141727551.png)





###                                                       5.2    锁膨胀

如果尝试加轻量级锁的过程中，CAS操作无法成功，这时一种情况就是有其他线程为此对象加上了轻量级锁（有竞争），这是需要进行锁膨胀，将轻量级锁改为重量级锁。

```java
static Object obj = new Object();
public static void method1(){
        synchronized(obj){
          //同步块
        }
}
```

![image-20230306142525034](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306142525034.png)







###                                                    5.3  重量锁

重量级锁竞争的时候，还可以使用自旋来优化，弱国当前线程自选成功，这时当前线程就可以避免阻塞。

在java6之后自旋锁是自适应的，比如对象刚刚的一次自旋操作成功过，那么认为这次自旋成功的可能性更高，就会多自旋几次；

![image-20230306143007972](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306143007972.png)

![image-20230306143026855](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306143026855.png)

![image-20230306143227609](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306143227609.png)







###                                                      5.4   偏向锁

**什么是偏向锁**

 所谓的偏向，就是偏心，即锁会偏向于当前已经占有锁的线程 。大部分情况是没有竞争的（某个同步块大多数情况都不会出现多线程同时竞争锁），所以可以通过偏向来提高性能。即在无竞争时，之前获得锁的线程再次获得锁时，会判断是否偏向锁指向我，那么该线程将不用再次获得锁，直接就可以进入同步块。偏向锁的实施就是将对象头Mark的标记设置为偏向，并将线程ID写入对象头Mark ，当其他线程请求相同的锁时，偏向模式结束


**偏向锁的获取和撤销逻辑**
1.首先获取锁 对象的 Markword，判断是否处于可偏向状态。（biased_lock=1、且 ThreadId 为空）

2.如果是可偏向状态，则通过 CAS 操作，把当前线程的 ID 写入到 MarkWorda) 如果 cas 成功，那么 markword 就会变成这样。 表示已经获得了锁对象的偏向锁，接着执行同步代码块b) 如果 cas 失败，说明有其他线程已经获得了偏向锁，这种情况说明当前锁存在竞争，需要撤销已获得偏向锁的线程，并且把它持有的锁升级为轻量级锁（这个操作需要等到全局安全点，也就是没有线程在执行字节码）才能执行
3.如果是已偏向状态，需要检查 markword 中存储的ThreadID 是否等于当前线程的 ThreadIDa) 如果相等，不需要再次获得锁，可直接执行同步代码块b) 如果不相等，说明当前锁偏向于其他线程，需要撤销偏向锁并升级到轻量级锁
![image-20230306144306795](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230306144306795.png)









###                                                       5.5  其他优化



#### 1    减少上锁时间













#### 2  减少锁的粒度











#### 3   锁粗化











#### 4   锁消除











#### 5   读写分离
