

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

1.服务集成 RPC 后，服务（这里的服务就是图中的 Provider，服务提供者）启动后会通过 Register（注册）模块，把服务的唯一 ID 和 IP 地址，端口信息等注册到 RPC 框架注册中心（图中的 Registry 部分）。
2.当调用者（Consumer）想要调用服务的时候，通过 Provider 注册时的的服务唯一 ID 去注册中心查找在线可供调用的服务，返回一个 IP 列表（3.notify 部分）。
3.第三步 Consumer 根据一定的策略，比如随机 or 轮训从 Registry 返回的可用 IP 列表真正调用服务（4.invoke）。
4。最后是统计功能，RPC 框架都提供监控功能，监控服务健康状况，控制服务线上扩展和上下线（5.count）



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

1.ABA问题。因为CAS需要在操作值的时候，检查值有没有发生变化，如果没有发生变化则更新，但是如果一个值原来是A，变成了B，又变成了A，那么使用CAS进行检查时会发现它的值没有发生变化，但是实际上却变化了，ABA问题解决的思路就是使用版本号。在变量前面追加上版本号，每次变量更新的时候把版本号加1，那么A->B->A就会变成1A->2B->3A.从jdk1.5开始，JDK的Atomic包里提供了一个类AtomicStampedReference来解决ABA问题。

2.循环时间长开销大。自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。

3.只能保证一个共享变量的原子操作。不能操作一个代码块。





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

优点：轻量级锁，省去了线程状态切换的开销，休眠到运行

缺点：自选增加了新的内存开销

如果线程执行周期很长，如io密集型操作，就可以用非自旋锁





# 10 合适的线程数量是多少？  CPU核心数和线程数的关系？**

![image-20230129192040742](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230129192040742.png)

### CPU密集型

CPU密集型也叫计算密集型，指的是系统的硬盘、内存性能相对CPU要好很多，此时，系统运作CPU读写IO(硬盘/内存)时，IO可以在很短的时间内完成，而CPU还有许多运算要处理，因此，CPU负载很高。

CPU密集表示该任务需要大量的运算，而没有阻塞，CPU一直全速运行。CPU密集任务只有在真正的多核CPU上才可能得到加速（通过多线程），而在单核CPU上，无论你开几个模拟的多线程该任务都不可能得到加速，因为CPU总的运算能力就只有这么多。

CPU使用率较高（例如:计算圆周率、对视频进行高清解码、矩阵运算等情况）的情况下，通常，线程数只需要设置为CPU核心数的线程个数就可以了。 这一情况多出现在一些业务复杂的计算和逻辑处理过程中。比如说，现在的一些机器学习和深度学习的模型训练和推理任务，包含了大量的矩阵运算。

CPU密集型核心线程数 = CPU核数 + 1

### IO密集型

IO密集型指的是系统的CPU性能相对硬盘、内存要好很多，此时，系统运作，大部分的状况是CPU在等IO (硬盘/内存) 的读写操作，因此，CPU负载并不高。

密集型的程序一般在达到性能极限时，CPU占用率仍然较低。这可能是因为任务本身需要大量I/O操作，而程序的逻辑做得不是很好，没有充分利用处理器能力。

CPU 使用率较低，程序中会存在大量的 I/O 操作占用时间，导致线程空余时间很多，通常就需要开CPU核心数数倍的线程。

其计算公式为：IO密集型核心线程数 = CPU核数 / （1-阻塞系数）。

当线程进行 I/O 操作 CPU 空闲时，启用其他线程继续使用 CPU，以提高 CPU 的使用率。例如：数据库交互，文件上传下载，网络传输等。

### CPU密集型与IO密集型任务的使用说明

当线程等待时间所占比例越高，需要越多线程，启用其他线程继续使用CPU，以此提高CPU的利用率；
当线程CPU时间所占比例越高，需要越少的线程，通常线程数和CPU核数一致即可，这一类型在开发中主要出现在一些计算业务频繁的逻辑中。

### CPU密集型任务与IO密集型任务的区别

计算密集型任务的特点是要进行大量的计算，消耗CPU资源，全靠CPU的运算能力。这种计算密集型任务虽然也可以用多任务完成，但是任务越多，花在任务切换的时间就越多，CPU执行任务的效率就越低，所以，要最高效地利用CPU，计算密集型任务同时进行的数量应当等于CPU的核心数，避免线程或进程的切换。计算密集型任务由于主要消耗CPU资源，因此，代码运行效率至关重要。Python这样的脚本语言运行效率很低，完全不适合计算密集型任务。对于计算密集型任务，最好用C语言编写。IO密集型任务的特点是CPU消耗很少，任务的大部分时间都在等待IO操作完成（因为IO的速度远远低于CPU和内存的速度）。涉及到网络、磁盘IO的任务都是IO密集型任务，

对于IO密集型任务，线程数越多，CPU效率越高，但也有一个限度。




# *11 CAS是一种什么样的同步机制？*

   jdk5之前都是通过 synchronized或者lock来保证同步，从而达到线程安全的目的。但是synchronized或lock方案属于互斥锁方案，比较重量级，加锁、释放锁都会引起性能损耗的问题。

![image-20230130125256426](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230130125256426.png)





# 12.*线程加锁的方式，synchronized和lock的区别*





