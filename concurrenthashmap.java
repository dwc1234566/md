 private static final int DEFAULT_CAPACITY = 16;  //默认初始容量
 private static final int MAXIMUM_CAPACITY = 1 << 30; //最大表容量
 private static final float LOAD_FACTOR = 0.75f;    //默认加载因子
 
 
     /**
     * 表初始化和大小调整控件。当为负值时，表正在初始化或调整大小:
	   -1表示初始化，否则-(1 +活动的调整大小线程数 扩容)。
	   否则，当table为空时，保存创建时要使用的初始表大小，默认为0。
	   初始化后，保存下一个要调整表大小的元素计数值。
	   当为正数时，如果未初始化代表数组大小，如果已经初始化，代表了数组的扩容阈值
     */
    private transient volatile int sizeCtl;
 
 
 
 //创建一个新的空映射，其初始表大小可容纳指定数量的元素，而不需要动态调整大小。
 形参: initialCapacity——实现执行内部大小调整来容纳这么多元素。 
 抛出: 如果元素的初始容量为负
 
 此时数组并未初始化
    public ConcurrentHashMap(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException();
        int cap = ((initialCapacity >= (MAXIMUM_CAPACITY >>> 1)) ?
                   MAXIMUM_CAPACITY :
                   tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1));   //返回一个传入值最接近的2的幂次方数
        this.sizeCtl = cap;
    }
	
	
//当第一次put时初始化map

    public V put(K key, V value) {
        return putVal(key, value, false);
    }
	
	
    final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException(); //不允许空值
        int hash = spread(key.hashCode());   //基于键获取hashcode 做了扰动算法   正数链表节点，负数树节点    104行
        int binCount = 0;    //记录某个桶上的元素个数
		//遍历数组
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)   //如果为空 初始化数组 
                tab = initTable();    //119行
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {    //如果当前位置没有值 cas插入当前位置
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            else if ((fh = f.hash) == MOVED)   //static final int MOVED  = -1; hash for forwarding nodes 表示当前正在扩容 迁移到当前节点 
                tab = helpTransfer(tab, f);   //多线程协助扩容              //497行
            else {                      //不为空 插入到链表
                V oldVal = null;
                synchronized (f) {         //加锁，只锁当前要插入的节点 粒度很低
                    if (tabAt(tab, i) == f) {      //防止变成树后元素改变
                        if (fh >= 0) {        //hsah值  代表链表
                            binCount = 1;
                            for (Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash &&
                                    ((ek = e.key) == key ||
                                     (ek != null && key.equals(ek)))) {    //如果插入值和当前值一样  覆盖当前值 break
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                Node<K,V> pred = e;       
                                if ((e = e.next) == null) { 	 //最终添加到节点尾部 
                                    pred.next = new Node<K,V>(hash, key,
                                                              value, null);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof TreeBin) {   //如果时树的根节点 插入到树中
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                    }
                }
                if (binCount != 0) {   //看看链表的长度，是否需要扩容
                    if (binCount >= TREEIFY_THRESHOLD) //大于8 变成树
                        treeifyBin(tab, i);   //143 行
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);   //维护集合长度，判断是否需要扩容   put一个数据就需要加一     184行
        return null;
    }
	
	
     static final int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;   // static final int HASH_BITS = 0x7fffffff 正数
    }
	
	
	
	
	
	
	
	
	
	 private final Node<K,V>[] initTable() {
        Node<K,V>[] tab; int sc;
        while ((tab = table) == null || tab.length == 0) {  //判断是否为空
            if ((sc = sizeCtl) < 0)    //正在初始化或扩容
                Thread.yield(); // lost initialization race; just spin     当前线程礼让
            else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {   //用cas尝试将sizectl改为-1 表示自己正在进行初始化
                try {
                    if ((tab = table) == null || tab.length == 0) {   //双重校验
                        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;    //是否手动设置数组初始大小 
                        @SuppressWarnings("unchecked")
                        Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];   
                        table = tab = nt;
                        sc = n - (n >>> 2);    //0.75n 扩容阈值
                    }
                } finally {
                    sizeCtl = sc;
                }
                break;
            }
        }
        return tab;
    }
	
	
	
	
	private final void treeifyBin(Node<K,V>[] tab, int index) {
        Node<K,V> b; int n, sc;
        if (tab != null) {
            if ((n = tab.length) < MIN_TREEIFY_CAPACITY)   //如果小于64   用扩容的方式避免树化操作
                tryPresize(n << 1);
            else if ((b = tabAt(tab, index)) != null && b.hash >= 0) {
                synchronized (b) {
                    if (tabAt(tab, index) == b) {
                        TreeNode<K,V> hd = null, tl = null;
                        for (Node<K,V> e = b; e != null; e = e.next) {
                            TreeNode<K,V> p =
                                new TreeNode<K,V>(e.hash, e.key, e.val,
                                                  null, null);
                            if ((p.prev = tl) == null)
                                hd = p;
                            else
                                tl.next = p;
                            tl = p;
                        }
                        setTabAt(tab, index, new TreeBin<K,V>(hd));
                    }
                }
            }
        }
    }
	
	
	
	

	
	     /**
     * Adds to count, and if table is too small and not already
     * resizing, initiates transfer. If already resizing, helps
     * perform transfer if work is available.  Rechecks occupancy
     * after a transfer to see if another resize is already needed
     * because resizings are lagging additions.
     *
     * @param x the count to add
     * @param check if <0, don't check resize, if <= 1 only check if uncontended    binCount，binCount 默认是0，只有 hash 冲突了才会大于 1.且他的大小是链表的长度（如果不是红黑数结构的话）。
     */
    private final void addCount(long x, int check) {      //集合的长度  当 counterCells 不是 null，就遍历元素，并和 baseCount 累加。
        CounterCell[] as; long b, s;
        if ((as = counterCells) != null ||    
            !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)) {   //cas添加失败 需要添加到countercells数组中  
            CounterCell a; long v; int m;
            boolean uncontended = true;
			 // 如果计数盒子是空（尚未出现并发）
             // 如果随机取余一个数组位置为空 或者
            // 修改这个槽位的变量失败（出现并发了）
             // 执行 fullAddCount 方法。并结束
            if (as == null || (m = as.length - 1) < 0 ||
                (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
                !(uncontended =
                  U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
                fullAddCount(x, uncontended);    //229
                return;
            }
            if (check <= 1)
                return;
            s = sumCount();
        }
        if (check >= 0) {  // 如果需要检查,检查是否需要扩容，在 putVal 方法调用时，默认就是要检查的
            Node<K,V>[] tab, nt; int n, sc;
			// 如果map.size() 大于 sizeCtl（达到扩容阈值需要扩容） 且
        // table 不是空；且 table 的长度小于 1 << 30。（可以扩容）
            while (s >= (long)(sc = sizeCtl) && (tab = table) != null &&
                   (n = tab.length) < MAXIMUM_CAPACITY) {
                int rs = resizeStamp(n);    // 根据 length 得到一个标识
                if (sc < 0) {
				// 如果 sc 的低 16 位不等于 标识符（校验异常 sizeCtl 变化了）
                // 如果 sc == 标识符 + 1 
				      （扩容结束了，不再有线程进行扩容）（默认第一个线程设置 sc ==rs 左移 16 位 + 2，当第一个线程结束扩容了，就会将 sc 减一。这个时候，sc 就等于 rs + 1）
                // 如果 sc == 标识符 + 65535（帮助线程数已经达到最大）
                // 如果 nextTable == null（结束扩容了）
                // 如果 transferIndex <= 0 (转移状态变化了)
                // 结束循环
                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                        sc == rs + MAX_RESIZERS || (nt = nextTable) == null ||
                        transferIndex <= 0)
                        break;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                        transfer(tab, nt);     //协助扩容
                }
                else if (U.compareAndSwapInt(this, SIZECTL, sc,
                                             (rs << RESIZE_STAMP_SHIFT) + 2))   //会将sizectl 变成负数
                    transfer(tab, null);   //扩容     322行
                s = sumCount();
            }
        }
    } 
	
	
	
	
	private final void fullAddCount(long x, boolean wasUncontended) {
        int h;
        if ((h = ThreadLocalRandom.getProbe()) == 0) {
            ThreadLocalRandom.localInit();      // force initialization   拿到当前线程对应的随机值 ，为了计算角标
            h = ThreadLocalRandom.getProbe();
            wasUncontended = true;
        }
        boolean collide = false;                // 如果最后一个槽非空则为True
        for (;;) {
            CounterCell[] as; CounterCell a; int n; long v;
            if ((as = counterCells) != null && (n = as.length) > 0) {
                if ((a = as[(n - 1) & h]) == null) {
                    if (cellsBusy == 0) {            // 尝试连接新的cell
                        CounterCell r = new CounterCell(x); // Optimistic create
                        if (cellsBusy == 0 &&
                            U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                            boolean created = false;
                            try {               //重新检查
                                CounterCell[] rs; int m, j;
                                if ((rs = counterCells) != null &&
                                    (m = rs.length) > 0 &&
                                    rs[j = (m - 1) & h] == null) {
                                    rs[j] = r;
                                    created = true;
                                }
                            } finally {
                                cellsBusy = 0;
                            }
                            if (created)
                                break;
                            continue;           // 槽现在非空
                        }
                    }
                    collide = false;
                }
                else if (!wasUncontended)       // 如果cas失败
                    wasUncontended = true;      // 重新散列循环
                else if (U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))
                    break;
                else if (counterCells != as || n >= NCPU)
                    collide = false;            // At max size or stale
                else if (!collide)
                    collide = true;
                else if (cellsBusy == 0 &&
                         U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                    try {
                        if (counterCells == as) {// 扩容counterCells
                            CounterCell[] rs = new CounterCell[n << 1];
                            for (int i = 0; i < n; ++i)
                                rs[i] = as[i];
                            counterCells = rs;
                        }
                    } finally {
                        cellsBusy = 0;
                    }
                    collide = false;
                    continue;                   // Retry with expanded table
                }
                h = ThreadLocalRandom.advanceProbe(h);
            }
            else if (cellsBusy == 0 && counterCells == as &&
                     U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                boolean init = false;
                try {                           // Initialize table
                    if (counterCells == as) {
                        CounterCell[] rs = new CounterCell[2];
                        rs[h & 1] = new CounterCell(x);
                        counterCells = rs;
                        init = true;
                    }
                } finally {
                    cellsBusy = 0;
                }
                if (init)
                    break;
            }
            else if (U.compareAndSwapLong(this, BASECOUNT, v = baseCount, v + x))
                break;                          // Fall back on using base
        }
    }
	
	
	
	//扩容
	private final void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
        int n = tab.length, stride;
        if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE)     // stride代表了每个线程负责迁移的数据量
            stride = MIN_TRANSFER_STRIDE; // subdivide range
        if (nextTab == null) {            // initiating
            try {
                @SuppressWarnings("unchecked")
                Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n << 1];   //新建一个原来集合两倍的集合
                nextTab = nt;
            } catch (Throwable ex) {      // try to cope with OOME
                sizeCtl = Integer.MAX_VALUE;
                return;
            }
            nextTable = nextTab;    //赋值给成员变量
            transferIndex = n;      //记录原来集合成员个数
        } 
        int nextn = nextTab.length;    
        ForwardingNode<K,V> fwd = new ForwardingNode<K,V>(nextTab);
        boolean advance = true;
        boolean finishing = false; // to ensure sweep before committing nextTab
		  //通过for自循环处理每个槽位中的链表元素，默认advace为真，通过CAS设置transferIndex属性值，
		  //并初始化i和bound值，i指当前处理的槽位序号，bound指需要处理的槽位边界，先处理槽位15的节点
        for (int i = 0, bound = 0;;) {
            Node<K,V> f; int fh;
 
            // 这个循环使用CAS不断尝试为当前线程分配任务
 
            // 直到分配成功或任务队列已经被全部分配完毕
 
            // 如果当前线程已经被分配过bucket区域
 
            // 那么会通过--i指向下一个待处理bucket然后退出该循
            while (advance) {
                int nextIndex, nextBound;
				//--i表示下一个待处理的bucket，如果它>=bound,表示当前线程已经分配过bucket区域
                if (--i >= bound || finishing)
                    advance = false;
				//表示所有bucket已经被分配完毕 给nextIndex赋予初始值 = 16
                else if ((nextIndex = transferIndex) <= 0) {
                    i = -1;
                    advance = false;
                }
				//通过cas来修改TRANSFERINDEX,为当前线程分配任务，处理的节点区间为(nextBound,nextIndex)->(0,15)
                else if (U.compareAndSwapInt
                         (this, TRANSFERINDEX, nextIndex,
                          nextBound = (nextIndex > stride ?
                                       nextIndex - stride : 0))) {
                    bound = nextBound;
                    i = nextIndex - 1;
                    advance = false;
                }
            }
			//如果已经迁移完，也就是当前线程已经处理完所有负责的bucket
            if (i < 0 || i >= n || i + n >= nextn) {
                int sc;
				//如果完成了扩容
                if (finishing) {
					//删除成员变量
                    nextTable = null;
					
                    //更新table数组
                    table = nextTab;
					//更新阈值
                    sizeCtl = (n << 1) - (n >>> 1);
                    return;
                }
				
				
				// sizeCtl 在迁移前会设置为 (rs << RESIZE_STAMP_SHIFT) + 2 (详细介绍点击这里)
 
                // 然后，每增加一个线程参与迁移就会将 sizeCtl 加 1，
 
                // 这里使用 CAS 操作对 sizeCtl 的低16位进行减 1，代表做完了属于自己的任务
				
                if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
					
					//第一个扩容的线程，执行transfer方法之前，会设置 sizeCtl = (resizeStamp(n) << RESIZE_STAMP_SHIFT) + 2)
                    //后续帮其扩容的线程，执行transfer方法之前，会设置 sizeCtl = sizeCtl+1
                    //每一个退出transfer的方法的线程，退出之前，会设置 sizeCtl = sizeCtl-1
                    //那么最后一个线程退出时：必然有
                    //sc == (resizeStamp(n) << RESIZE_STAMP_SHIFT) + 2)，即 (sc - 2) == resizeStamp(n) << RESIZE_STAMP_SHIFT
                    // 如果 sc - 2 不等于标识符左移 16 位。如果他们相等了，说明没有线程在帮助他们扩容了。也就是说，扩容结束了。

                    if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
                        return;
                    finishing = advance = true;
                    i = n; // recheck before commit
                }
            }
			
			//没有元素直接设为fwd节点
            else if ((f = tabAt(tab, i)) == null)
                advance = casTabAt(tab, i, null, fwd);
			
			//如果已经被迁移，跳过
            else if ((fh = f.hash) == MOVED)
                advance = true; // already processed
            else {
				//和put一个锁对象，保证迁移的时候不会放入元素
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        Node<K,V> ln, hn;
                        if (fh >= 0) {
                            int runBit = fh & n;
                            Node<K,V> lastRun = f;
                            for (Node<K,V> p = f.next; p != null; p = p.next) {
                                int b = p.hash & n;
                                if (b != runBit) {
                                    runBit = b;
                                    lastRun = p;
                                }
                            }
                            if (runBit == 0) {
                                ln = lastRun;
                                hn = null;
                            }
                            else {
                                hn = lastRun;
                                ln = null;
                            }
                            for (Node<K,V> p = f; p != lastRun; p = p.next) {
                                int ph = p.hash; K pk = p.key; V pv = p.val;
                                if ((ph & n) == 0)
                                    ln = new Node<K,V>(ph, pk, pv, ln);
                                else
                                    hn = new Node<K,V>(ph, pk, pv, hn);
                            }
                            setTabAt(nextTab, i, ln);
                            setTabAt(nextTab, i + n, hn);
                            setTabAt(tab, i, fwd);
                            advance = true;
                        }
                        else if (f instanceof TreeBin) {
                            TreeBin<K,V> t = (TreeBin<K,V>)f;
                            TreeNode<K,V> lo = null, loTail = null;
                            TreeNode<K,V> hi = null, hiTail = null;
                            int lc = 0, hc = 0;
                            for (Node<K,V> e = t.first; e != null; e = e.next) {
                                int h = e.hash;
                                TreeNode<K,V> p = new TreeNode<K,V>
                                    (h, e.key, e.val, null, null);
                                if ((h & n) == 0) {
                                    if ((p.prev = loTail) == null)
                                        lo = p;
                                    else
                                        loTail.next = p;
                                    loTail = p;
                                    ++lc;
                                }
                                else {
                                    if ((p.prev = hiTail) == null)
                                        hi = p;
                                    else
                                        hiTail.next = p;
                                    hiTail = p;
                                    ++hc;
                                }
                            }
                            ln = (lc <= UNTREEIFY_THRESHOLD) ? untreeify(lo) :
                                (hc != 0) ? new TreeBin<K,V>(lo) : t;
                            hn = (hc <= UNTREEIFY_THRESHOLD) ? untreeify(hi) :
                                (lc != 0) ? new TreeBin<K,V>(hi) : t;
                            setTabAt(nextTab, i, ln);
                            setTabAt(nextTab, i + n, hn);
                            setTabAt(tab, i, fwd);
                            advance = true;
                        }
                    }
                }
            }
        }
    }
	
	
	
	final Node<K,V>[] helpTransfer(Node<K,V>[] tab, Node<K,V> f) {
        Node<K,V>[] nextTab; int sc;
        if (tab != null && (f instanceof ForwardingNode) &&
            (nextTab = ((ForwardingNode<K,V>)f).nextTable) != null) {
            int rs = resizeStamp(tab.length);
            while (nextTab == nextTable && table == tab &&
                   (sc = sizeCtl) < 0) {
                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||    //判断是否完成扩容或者协助线程是否达到最大值
                    sc == rs + MAX_RESIZERS || transferIndex <= 0)
                    break;
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                    transfer(tab, nextTab);    
                    break;
                }
            }
            return nextTab;
        }
        return table;
    }