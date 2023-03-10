# 1  redis的五种基础数据结构

**一、String（字符串）**
在 Redis 中，字符串可以被修改，称为简单动态字符串 (SDS) 或字符串，但它的结构更像数组列表，维护字节数组并在其中预分配空间。减少频繁的内存分配。
Redis 的内存分配机制如下:

- 当字符串长度小于 1MB 时，每次扩容会使现有空间翻倍。
- 如果字符串长度超过 1MB，则每次只扩展 1MB 空间。
- 这确保了足够的内存空间，并且不会浪费内存。字符串的最大长度是 512MB.

```c++
struck SDS{T capacity;       //数组容量
  T len;            //实际长度
  byte flages;  //标志位,低三位表示类型
  byte[] content;   //数组内容
}
```


上面是字符串的基本结构，其中 content 保存字符串的内容，0x\0 作为结束字符不计算在 len中。

capacity 和 len 属性都是泛型的，为什么不直接使用 int 呢? Redis 中有很多优化方案。为了更合理地使用内存，不同长度的字符串用不同的数据类型表示。此外，len 在创建字符串时与capacity 一样大，因此不会产生多余的空间。所以 String 值可以是字符串、数字(整数、浮点数)或二进制。
应用场景：
存储key-value键值对

**二、list(列表)**
当数据量较小时，它的底层存储结构是一个连续的内存，称为 ziplist(压缩列表)，它将所有的元素彼此相邻地存储，分配的是一个连续的内存;当数据量较大时，将其转换为快速列表结构。

然而，简单的链表也有缺陷。链表前后的指针 prev 和 next 将占用更多的内存，浪费更多的空间，并加剧内存碎片。redis 3.2 之后，采用了 ziplist + 链表 的混合结构，称为 quicklist (快速链表)。
应用场景：

由于 list 是按插入顺序排序的列表，因此有许多应用场景。例如:
消息队列: lpop 和 rpush (反之亦然，lpush 和 rpop) 可以实现队列的功能
像朋友圈的列表、评论列表和排行榜: lpush 命令和 lrange 命令可以实现最新列表的功能，每次通过 lpush 命令插入新的元素到列表中，再通过 lrange 命令读取最新的元素列表。

**三、hash （字典）**
数组 + 列表结构，当发生散列冲突时将元素添加到列表中。值得注意的是，Redis Hash 值只能是字符串。
Hash 和 String 都可以用来存储用户信息，但不同点在于 Hash 可以分别存储用户信息的每个字段。存储所有用户信息的序列化字符串。如果要修改用户字段，必须先查询所有用户信息字符串，解析为对应的用户信息对象，修改后再序列化为字符串。但是，哈希只可以修改某个字段，从而节省网络流量。但是，哈希的内存占用比 String 大，这是哈希的缺点。
应用场景：
购物车:hset [key] [field] [value] 命令可以实现用户 Id、商品 Id 为字段、商品数量为值，正好构成了购物车的三个要素。
散列类型 (键、字段、值) 的结构类似于对象 (对象id、属性、值) 的结构，也可以用来存储对象。

**四、set(集合)**
它内部的键值对是无序的、唯一 的。它的内部实现相当于一个特殊的字典，字典中所有的value都是一个值 NULL。当集合中最后一个元素被移除之后，数据结构被自动删除，内存被回收。
应用场景：
散列类型(键、字段、值)的结构类似于对象(对象id、属性、值)的结构，也可以用来存储对象。

收集朋友，追随者，粉丝和感兴趣的人:
1)使用 sinter 命令可以获取用户 A 和 B 的共同好友;
sismember 命令可以判断 A 是否是 B 的朋友;
scard 命令可以获取好友数量;
4)注意时，smove 命令可以将 B 从 A 的fans转移到 A 的朋友集
首页展示随机：美团首页有很多推荐商家，但是并不能全部展示，set 类型适合存放所有需要展示的内容，而 srandmember 命令则可以从中随机获取几个。
存储某活动中中奖的用户 ID ，因为有去重功能，可以保证同一个用户不会中奖两次。

**五、zset(有序集合)**
zset 也称为 SortedSet。一方面，它是一个集合，它保证了内部值的唯一性。另一方面，它可以为每个值分配一个分数，表示该值的排序权重。它的内部实现使用一种称为“跳转列表”的数据结构。
应用场景：
zset 可以用作排序，但与 list 不同的是，它可以实现动态排序。例如，zset 可以用来存储风扇列表。value 为球迷的用户ID, score 为接下来的时间。
zset 还可以用来存储学生的成绩，其中 value 是学生的ID, score 是他的考试成绩。我们可以按分数给他打分。

来源参考知乎《Redis 5种数据结构 及使用场景分析》





# redis 的内存淘汰策略

**定期删除**
Redis会将每个设置了过期日期的键放入一个单独的字典中，并定期遍历字典以删除过期的键。默认情况下，Redis每秒执行10次过期扫描(每100ms一次)。过期扫描不是遍历过期字典中的所有键，而是使用一个简单的贪婪策略。从过期字典中随机抽取20个键;从20个密钥中删除过期的密钥。
3.如果过期密钥的比例超过1/4，请重复执行步骤1。
redis 默认每 100ms 随机抽取一些设置了过期时间的密钥，检查密钥是否过期，如果过期则删除密钥。注意，这是一个随机选择。为什么随机?如果你认为redis节省了数十万个键，并每100ms迭代所有设置过期时间的键，这将给CPU带来很大的负载。

**惰性删除**
当客户端访问密钥时，redis检查key的过期日期并删除它而不返回任何内容。
定期删除可能会导致许多过期的key在到期时无法删除。这就是惰性删除的用武之地。如果您有一个过期的key没有通过常规删除删除，并且仍然在内存中，它将不会通过redis删除，直到您的系统检查该密key。这被称为延迟删除，这意味着当您主动检查过期的密钥时，如果您发现key已经过期，您将立即删除它而不返回任何内容。

总结:定期删除是集中处理，延迟删除是分散处理。