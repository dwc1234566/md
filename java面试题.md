

# ***1.为什么string设计为不可变***   （不考虑反射特殊场景）

   ![](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124172328117.png)

**string被final修饰，不可继承**

**string的value是不可变的**

![image-20230124172708557](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124172708557.png)

**一 ，多个相同的字符串共同指向一个值，节省空间。**

二，用作HashMap的key，HashCode不变。

三，缓存HashCode，创建了string对象hash不变。用的时候，不用重新计算。

![image-20230124173354785](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124173354785.png)

三，线程安全，不可变被多个线程共享进行同步操作。





# *2.如何设计一个类似于Dubbo的RPC框架*

**什么是rpc框架**

![image-20230124190532097](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124190532097.png)

![image-20230212133822553](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230212133822553.png)

`1.服务集成 RPC 后，服务（这里的服务就是图中的 Provider，服务提供者）启动后会通过 Register（注册）模块，把服务的唯一 ID 和 IP 地址，端口信息等注册到 RPC 框架注册中心（图中的 Registry 部分）。`
`2.当调用者（Consumer）想要调用服务的时候，通过 Provider 注册时的的服务唯一 ID 去注册中心查找在线可供调用的服务，返回一个 IP 列表（3.notify 部分）。`
`3.第三步 Consumer 根据一定的策略，比如随机 or 轮训从 Registry 返回的可用 IP 列表真正调用服务（4.invoke）。`
`4。最后是统计功能，RPC 框架都提供监控功能，监控服务健康状况，控制服务线上扩展和上下线（5.count）`



# *3.为什么阿里巴巴不建议使用Executors创建线程池*



![image-20230124191224318](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124191224318.png)

```
newFixedThreadPool用的没有上限的queue，如果任务的处理速度比较慢，当队列占用内存过多有可能发生oom
```

![image-20230124191548144](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124191548144.png)

# *4.根据实际需要，定制自己的线程池 new ThreadPoolExecutor*

 ![image-20230124192317624](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124192317624.png)

​      线程工厂，和拒绝执行策略。当线程池慢后，执行决绝策略。

![image-20230124192926637](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124192926637.png)





# *5.  CAS有什么缺点*

![image-20230124193542234](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230124193542234.png)

`1.ABA问题。因为CAS需要在操作值的时候，检查值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是A，变成了B，又变成了A，那么使用CAS进行检查时会发现它的值没有发生变化，但是实际上却变化了，ABA问题解决的思路就是使用版本号。在变量前面追加上版本号，每次变量更新的时候把版本号加1，那么A->B->A就会变成1A->2B->3A.从jdk1.5开始，JDK的Atomic包里提供了一个类AtomicStampedReference来解决ABA问题。`

`2.循环时间长开销大。自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。`

`3.只能保证一个共享变量的原子操作。不能操作一个代码块。`





# 6.*你知道线程池线程复用的原理吗*

![image-20230125172511381](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230125172511381.png)

`**当任务提交之后，线程池首先会检查当前线程数，如果当前的线程数小于核心线程数（corePoolSize），则新建线程并执行任务。`

`当提交的任务不断增加，创建的线程数等于核心线程数（corePoolSize），新增的任务会被添加到 workQueue 任务队列中，等待核心线程执行完当前任务后，重新从 workQueue 中获取任务执行。`

`当任务数量达到了 workQueue 的最大容量，但是当前线程数小于最大线程数（maximumPoolSize），线程池会在核心线程数（corePoolSize）的基础上继续创非核心建线程来执行任务。`

`当任务继续增加，线程池的线程数达到最大线程数（maximumPoolSize），这个时候线程池就会采用拒绝策略来拒绝这些任务。`



# 7.*阻塞和非阻塞队列的并发安全原理是什么*

![image-20230125190910212](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230125190910212.png) 

阻塞队列ArrayBlockingQueue利用了可重入锁。

```java
/**
 * Inserts the specified element at the tail of this queue, waiting
 * for space to become available if the queue is full.
 *
 * @throws InterruptedException {@inheritDoc}
 * @throws NullPointerException {@inheritDoc}
 */
public void put(E e) throws InterruptedException {
    checkNotNull(e);
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        while (count == items.length)
            notFull.await();
        enqueue(e);
    } finally {
        lock.unlock();
    }
}
```

并发队列ConcurrentLinkedQueue，使用了cas，乐观锁原理



# *8.你对“非公平锁”了解吗，为什么会有非公平锁*

![image-20230129190214040](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230129190214040.png)



# *9 你对自旋锁了解吗？ 优点和缺点分别是什么？*



![image-20230129191147157](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230129191147157.png)

`优点：轻量级锁，省去了线程状态切换的开销，休眠到运行`

`缺点：自选增加了新的内存开销`

`如果线程执行周期很长，如io密集型操作，就可以用非自旋锁`





# 10 合适的线程数量是多少？  CPU核心数和线程数的关系？**

![image-20230129192040742](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230129192040742.png)

### `CPU密集型`

`CPU密集型也叫计算密集型，指的是系统的硬盘、内存性能相对CPU要好很多，此时，系统运作CPU读写IO(硬盘/内存)时，IO可以在很短的时间内完成，而CPU还有许多运算要处理，因此，CPU负载很高。`

`CPU密集表示该任务需要大量的运算，而没有阻塞，CPU一直全速运行。CPU密集任务只有在真正的多核CPU上才可能得到加速（通过多线程），而在单核CPU上，无论你开几个模拟的多线程该任务都不可能得到加速，因为CPU总的运算能力就只有这么多。`

`CPU使用率较高（例如:计算圆周率、对视频进行高清解码、矩阵运算等情况）的情况下，通常，线程数只需要设置为CPU核心数的线程个数就可以了。 这一情况多出现在一些业务复杂的计算和逻辑处理过程中。比如说，现在的一些机器学习和深度学习的模型训练和推理任务，包含了大量的矩阵运算。`

`CPU密集型核心线程数 = CPU核数 + 1`

### `IO密集型`

`IO密集型指的是系统的CPU性能相对硬盘、内存要好很多，此时，系统运作，大部分的状况是CPU在等IO (硬盘/内存) 的读写操作，因此，CPU负载并不高。`

`密集型的程序一般在达到性能极限时，CPU占用率仍然较低。这可能是因为任务本身需要大量I/O操作，而程序的逻辑做得不是很好，没有充分利用处理器能力。`

`CPU 使用率较低，程序中会存在大量的 I/O 操作占用时间，导致线程空余时间很多，通常就需要开CPU核心数数倍的线程。`

`其计算公式为：IO密集型核心线程数 = CPU核数 / （1-阻塞系数）。`

`当线程进行 I/O 操作 CPU 空闲时，启用其他线程继续使用 CPU，以提高 CPU 的使用率。例如：数据库交互，文件上传下载，网络传输等。`

### `CPU密集型与IO密集型任务的使用说明`

`当线程等待时间所占比例越高，需要越多线程，启用其他线程继续使用CPU，以此提高CPU的利用率；`
`当线程CPU时间所占比例越高，需要越少的线程，通常线程数和CPU核数一致即可，这一类型在开发中主要出现在一些计算业务频繁的逻辑中。`

### `CPU密集型任务与IO密集型任务的区别`

`计算密集型任务的特点是要进行大量的计算，消耗CPU资源，全靠CPU的运算能力。这种计算密集型任务虽然也可以用多任务完成，但是任务越多，花在任务切换的时间就越多，CPU执行任务的效率就越低，所以，要最高效地利用CPU，计算密集型任务同时进行的数量应当等于CPU的核心数，避免线程或进程的切换。计算密集型任务由于主要消耗CPU资源，因此，代码运行效率至关重要。Python这样的脚本语言运行效率很低，完全不适合计算密集型任务。对于计算密集型任务，最好用C语言编写。IO密集型任务的特点是CPU消耗很少，任务的大部分时间都在等待IO操作完成（因为IO的速度远远低于CPU和内存的速度）。涉及到网络、磁盘IO的任务都是IO密集型任务，`

`对于IO密集型任务，线程数越多，CPU效率越高，但也有一个限度。`




# *11 CAS是一种什么样的同步机制？*

   jdk5之前都是通过 synchronized或者lock来保证同步，从而达到线程安全的目的。但是synchronized或lock方案属于互斥锁方案，比较重量级，加锁、释放锁都会引起性能损耗的问题。

![image-20230130125256426](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230130125256426.png)





# 12.*线程加锁的方式，synchronized和lock的区别*



![image-20230215195143316](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215195143316.png)

#### synchronized和lock的区别： synchronized编码更简单，锁机制是有jvm维护，在竞争不激烈的情况下性能更好。Lock功能更加强大灵活，竞争激烈时性能更好。

- 性能不一样：    synchronized会根据锁竞争情况 从偏向锁->轻量级锁 -> 重量级锁 升级，编程更简单

- 锁机制不一样：synchronized是在jvm层面实现的，系统会监控锁的释放与否。lock是jdk代码实现的，需要手动释放，在finall块中释放。可以采用非阻塞的方式获取锁
- synchronized可以用到代码块上，方法上。lock只能写在代码里，不能直接修改方法。

#### Lock支持的功能

- 公平锁 ：Synchronized是非公平锁，Lock支持公平锁，默认非公平锁

- 可中断锁：Reentranlock 提供了lockinterruptibly（）的功能，可中断争夺锁的操作，强锁的时候会检查是否被中断，中断直接抛出异常，退出抢锁。

- 快速反馈锁： Reentranlock 提供了trylock（）和trylock（trytimes）的功能，不等待或限定时间获取锁，更灵活。可以避免死锁的发生。

  ![image-20230215201439468](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215201439468.png)

# *13 如何实现不可变类*

![image-20230215201602576](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215201602576.png)

```java
public final class test {

    private final String name;

    private final Integer age;

    public test(String name, Integer age) {
        this.name = name;
        this.age = age;
    }


    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
```



record是jdk16新特性





# *14 抽象类和接口的区别，类可以继承多个类吗，接口可以继承多个接口吗，类可以实现多个接口吗？

![image-20230215204914013](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215204914013.png)



# *15 描述动态代理的实现方式，分别说出相应的优缺点*

​      ![image-20230215205245384](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215205245384.png)



# *16 现成的生命周期*

  ![image-20230216190256170](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230216190256170.png)





# 17 .*讲讲java的反射机制*

####   什么是反射：

        Reflection(反射) 是 Java 程序开发语言的特征之一，它允许运行中的 Java 程序对自身进行检查。被private封装的资源只能类内部访问，外部是不行的，但反射能直接操作类私有属性。反射可以在运行时获取一个类的所有信息，（包括成员变量，成员方法，构造器等），并且可以操纵类的字段、方法、构造器等部分。要想解剖一个类，必须先要获取到该类的字节码文件对象。而解剖使用的就是Class类中的方法。所以先要获取到每一个字节码文件对应的Class类型的对象。
     反射就是把java类中的各种成分映射成一个个的Java对象。
         例如：一个类有：成员变量、方法、构造方法、包等等信息，利用反射技术可以对一个类进行解剖，把一个个组成部分映射成一个个对象。（其实：一个类中这些成员方法、构造方法、在加入类中都有一个类来描述）
            加载的时候：Class对象的由来是将 .class 文件读入内存，并为之创建一个Class对象。

 


#### 获取类

对应的字节码的对象（三种）
① 调用某个类的对象的getClass()方法，即：对象.getClass()；

```java
Person p = new Person();
Class clazz = p.getClass();
```

​        注意：此处使用的是Object类中的getClass()方法，因为所有类都继承Object类，所以调用Object类中的getClass()方法来获取。

② 调用类的class属性类获取该类对应的Class对象，即：类名.class

```java
Class clazz = Person.class;
```

③ 使用Class类中的forName()静态方法（最安全，性能最好）即：Class.forName(“类的全路径”)

```java
Class clazz = Class.forName("类的全路径");
```

​       注意：在运行期间，一个类，只有一个Class对象产生。

**![image-20230216193913411](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230216193913411.png)**



# *18 简述java内存区域*

  Java [内存](https://so.csdn.net/so/search?q=内存&spm=1001.2101.3001.7020)区域主要分为 Java堆，虚拟机栈，方法区，本地方法栈，程序计数器，这些都是虚拟的！不存在的，因为 Java 虚拟机本身就是虚拟的一个机器，但是真正在运行的时候虚拟机为了追求更高的速度，会把这些区域尽可能的分配在硬件的寄存器或缓存上，因为这样运行速度更快。

![image-20230219204333760](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230219204333760.png)

##### 程序计数器

程序计数器是一块较小的内存空间，它的作用可以看做是当前线程所执行的字节码的行号指示器。在虚拟机的概念模型里，字节码解释器工作时就是通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能都需要依赖这个计数器来完成。因此，为了线程切换后能恢复到正确的执行位置，每条线程都需要有一个独立的程序计数器，各条线程之间的计数器互不影响，独立存储，我们称这类内存区域为“线程私有”的内存。

如果线程正在执行的是一个 Java 方法，这个计数器记录的是正在执行的虚拟机字节码指令的地址；如果正在执行的是 natvie 方法，这个计数器值则为空（Undefined）。此内存区域是唯一一个在 Java 虚拟机规范中没有规定任何 OutOfMemoryError 情况的区域。

##### Java 虚拟机栈

与程序计数器一样，Java 虚拟机栈也是线程私有的，它的生命周期与线程相同。虚拟机栈描述的是 Java 方法执行的内存模型：每个方法被执行的时候都会同时创建一个栈帧用于存储局部变量表、操作栈、动态链接、方法出口等信息。每一个方法被调用直至执行完成的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。

我们常说的栈其实指的是“局部变量表”局部变量表存放了编译期可知的各种基本数据类型（boolean、byte、char、short、int、float、long、double）、对象引用（它可能是一个指向对象起始地址的引用指针，也可能指向一个代表对象的句柄）和 returnAddress 类型（指向了一条字节码指令的地址）。其中64 位长度的 long 和 double 类型的数据会占用 2 个局部变量空间（Slot），其余的数据类型只占用 1 个。

局部变量表所需的内存空间在编译期间完成分配，当进入一个方法时，这个方法需要在帧中分配多大的局部变量空间是完全确定的，在方法运行期间不会改变局部变量表的大小。

在 Java 虚拟机规范中，对这个区域规定了两种异常状况：如果线程请求的栈深度大于虚拟机所允许的深度，将抛出 StackOverflowError 异常；当无法申请到足够的内存时会抛出 OutOfMemoryError 异常。

##### 本地方法栈

本地方法栈与虚拟机栈所发挥的作用是非常相似的，其区别不过是虚拟机栈为虚拟机执行 Java 方法（也就是字节码）服务，而本地方法栈则是为虚拟机使用到的 native 方法服务。与虚拟机栈一样，本地方法栈区域也会抛出 StackOverflowError 和 OutOfMemoryError 异常。

##### Java 堆

对于大多数应用来说，Java 堆是 Java 虚拟机所管理的内存中最大的一块。Java 堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例以及数组都要在堆上分配内存。根据 Java 虚拟机规范的规定，Java 堆可以处于物理上不连续的内存空间中，只要逻辑上是连续的即可，如果在堆中没有内存完成实例分配，并且堆也无法再扩展时，将会抛出 OutOfMemoryError 异常。

##### 方法区

方法区与 Java 堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。根据 Java 虚拟机规范的规定，当方法区无法满足内存分配需求时，将抛出 OutOfMemoryError 异常。

##### 运行时常量池

运行时常量池是方法区的一部分。Class 文件中除了有类的版本、字段、方法、接口等描述等信息外，还有一项信息是常量池，用于存放编译期生成的各种字面量和符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。既然运行时常量池是方法区的一部分，自然会受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 异常。到这里为止，已经介绍了 Java 内存模型和 Java 内存区域并介绍了它们之间的区别。更重要的是搞清楚了在多线程情况下如何才能保证线程安全，那就是要保证 3 个特性，原子性、可见性、有序性。


# 19 *java对象创建过程*

   **java创建对象一般分为五个步骤：**

**（1）类加载检查**
**Java虚拟机（jvm）在读取一条new指令时候，首先检查能否在常量池中定位到这个类的符号引用，并且检查这个符号引用代表的类是否被加载、解析和初始化。如果没有，则会先执行相应的类加载过程。**

**（2）内存分配**
**在通过（1）后，则开始为新生的对象分配内存。该对象所需的内存大小在类加载完成后便可确定，因此为每个对象分配的内存大小是确定的。而分配方式主要有两种，分别为：**

**1.指针碰撞**

**应用场合：堆内存规整（通俗的说就是用过的内存被整齐充分的利用，用过的内存放在一边，没有用过的放在另外一边，而中间利用一个分界值指针对这两边的内存进行分界，从而掌握内存分配情况）。**

**即在开辟内存空间时候，将分界值指针往没用过的内存方向移动向应大小位置即可）。**

**将堆内存这样划分的代表的GC收集器算法有：Serial，ParNew**

**2.空闲列表**

**应用场合;堆内存不规整（虚拟机维护一个可以记录内存块是否可以用的列表来了解内存分配情况）**

**即在开辟内存空间时候，找到一块足够大的内存块分配给该对象即可，同时更新记录列表。**

**将堆内存这样划分的代表的GC收集器算法有：CMS**

**（3）初始化默认值**
**第（2）步完成后，紧接着，虚拟机需要将分配到的内存空间都进行初始化（即给一些默认值），这将做是为了保证对象实例的字段在Java代码中可以在不赋初值的情况下使用。程序可以访问到这些字段对用数据类型的默认值。**

**（4）设置对象头**
**初始化（3）完成后，虚拟机对对象进行一些简单设置，如标记该对象是哪个类的实例，这个对象的hash码，该对象所处的年龄段等等（这些可以理解为对象实例的基本信息）。这些信息被写在对象头中。jvm根据当前的运行状态，会给出不同的设置方式。**

**（5）执行初始化方法**
**在（4）完成后，最后执行由开发人员编写的对象的初始化方法，把对象按照开发人员的设计进行初始化，一个对象便创建出来了。**
