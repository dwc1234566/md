# 1.两数之和

给定一个整数数组 `nums` 和一个整数目标值 `target`，请你在该数组中找出 **和为目标值** *`target`* 的那 **两个** 整数，并返回它们的数组下标。你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。

你可以按任意顺序返回答案。

![image-20230215210417233](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215210417233.png)

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();  
        for(int i = 0; i< nums.length; i++) {
            if(map.containsKey(target - nums[i])) {
                return new int[] {map.get(target-nums[i]),i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
```

`

# *2 两数相加*

 给你两个 **非空** 的链表，表示两个非负的整数。它们每位数字都是按照 **逆序** 的方式存储的，并且每个节点只能存储 **一位** 数字。

请你将两个数相加，并以相同形式返回一个表示和的链表。

你可以假设除了数字 0 之外，这两个数都不会以 0 开头。

思路

- 将两个链表看成是相同长度的进行遍历，如果一个链表较短则在前面补 000，比如 987 + 23 = 987 + 023 = 1010
- 每一位计算的同时需要考虑上一位的进位问题，而当前位计算结束后同样需要更新进位值
- 如果两个链表全部遍历完毕后，进位值为 111，则在新链表最前方添加节点 111
- 小技巧：对于链表问题，返回结果为头结点时，通常需要先初始化一个预先指针 pre，该指针的下一个节点指向真正的头结点 head。
- 使用预先指针的目的在于链表初始化时无可用节点值，而且链表构造过程需要指针移动，进而会导致头指针丢失，无法返回结果。

 

![image-20230216200334296](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230216200334296.png)

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
      ListNode prev =  new ListNode(0);
      ListNode cur = prev; //当前指针
      int curry = 0;  //上次相加的进位树
      while(l1 != null || l2 != null){
         int x = l1 != null ? l1.val : 0; //取得当前l1节点的值
         int y = l2 != null ? l2.val : 0;//取得当前l2节点的值
         int sum = x + y +curry;   //算出当前位的和
         curry = sum / 10;    //取进位值
         sum = sum % 10;   
         cur.next = new ListNode(sum);
         cur = cur.next;  //移动指针
         if(l1 != null){
             l1 = l1.next;
         }
         if(l2 != null){
            l2 = l2.next;
         }

      }
      if(curry == 1){
          cur.next = new ListNode(curry);
      }
      
      return prev.next;
    }
}
```



# *3 无重复字符的最长子串*

​     给定一个字符串 `s` ，请你找出其中不含有重复字符的 **最长子串** 的长度。

![image-20230217165348184](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230217165348184.png)

这道题主要用到思路是：滑动窗口

什么是滑动窗口？

其实就是一个队列,比如例题中的 abcabcbb，进入这个队列（窗口）为 abc 满足题目要求，当再进入 a，队列变成了 abca，这时候不满足要求。所以，我们要移动这个队列！

如何移动？

我们只要把队列的左边的元素移出就行了，直到满足题目要求！

一直维持这样的队列，找出队列出现最长的长度时候，求出解！

时间复杂度：O(n)

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> o = new HashSet<Character>();
        int r = -1 ,res = 0;
        int n = s.length();
        for(int i =0; i<n;i++){
            if(i != 0){
                o.remove(s.charAt(i-1));  //窗口移动
            }
            while(r < n-1 && !o.contains(s.charAt(r+1))){    //小于数组长度，和set里面没有该字母
                o.add(s.charAt(r+1));
                r++;
            }
            res = Math.max(res,r-i+1);  //当前窗口宽度与前面最大的窗口宽度相比，找到最大的
        }
         return res;
    }
}
```



# *4 . 寻找两个正序数组的中位数*

 给定两个大小分别为 `m` 和 `n` 的正序（从小到大）数组 `nums1` 和 `nums2`。请你找出并返回这两个正序数组的 **中位数** 。









# *5 整数转罗马数字*

罗马数字包含以下七种字符： `I`， `V`， `X`， `L`，`C`，`D` 和 `M`。

```
字符          数值
I             1
V             5
X             10
L             50
C             100
D             500
M             1000
```

例如， 罗马数字 2 写做 `II` ，即为两个并列的 1。12 写做 `XII` ，即为 `X` + `II` 。 27 写做 `XXVII`, 即为 `XX` + `V` + `II` 。

通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 `IIII`，而是 `IV`。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 `IX`。这个特殊的规则只适用于以下六种情况：

- `I` 可以放在 `V` (5) 和 `X` (10) 的左边，来表示 4 和 9。
- `X` 可以放在 `L` (50) 和 `C` (100) 的左边，来表示 40 和 90。 
- `C` 可以放在 `D` (500) 和 `M` (1000) 的左边，来表示 400 和 900。

给你一个整数，将其转为罗马数字。

 

**示例 1:**

```
输入: num = 3
输出: "III"
```

**示例 2:**

```
输入: num = 4
输出: "IV"
```

**示例 3:**

```
输入: num = 9
输出: "IX"
```

**示例 4:**

```
输入: num = 58
输出: "LVIII"
解释: L = 50, V = 5, III = 3.
```

**示例 5:**

```
输入: num = 1994
输出: "MCMXCIV"
解释: M = 1000, CM = 900, XC = 90, IV = 4.
```

 贪心算法  从大的开始向后找

```java
class Solution {
    public String intToRoman(int num) {
 int[] nums = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] romans = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nums.length; i++) {
            while (num >= nums[i]){
                sb.append(romans[i]);
                num -= nums[i];
            }
        }
        return sb.toString();
    }
}
```







# 6  三数之和

给你一个整数数组 `nums` ，判断是否存在三元组 `[nums[i], nums[j], nums[k]]` 满足 `i != j`、`i != k` 且 `j != k` ，同时还满足 `nums[i] + nums[j] + nums[k] == 0` 。请

你返回所有和为 `0` 且不重复的三元组。

**注意：**答案中不可以包含重复的三元组。

 

 

**示例 1：**

```
输入：nums = [-1,0,1,2,-1,-4]
输出：[[-1,-1,2],[-1,0,1]]
解释：
nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
注意，输出的顺序和三元组的顺序并不重要。
```

**示例 2：**

```
输入：nums = [0,1,1]
输出：[]
解释：唯一可能的三元组和不为 0 。
```

**示例 3：**

```
输入：nums = [0,0,0]
输出：[[0,0,0]]
解释：唯一可能的三元组和为 0 。
```

排序单层循环加双指针

```java
class Solution {
    /**
     * 双指针法
     /
    public List<List<Integer>> threeSum(int[] nums) {
        ArrayList<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);  //先对数组进行排序
        int len = nums.length;
        if (nums.length == 0 || len <3)
            return res;   //长度为0 或者小于3 直接返回空
        for (int i = 0; i < len; i++) {
            if (nums[i] > 0)  break;  //i大于0 三数之和肯定大于0
            if (i > 0 && nums[i] == nums[i-1]) continue;  //去重
            int l = i+1; 
            int r = len-1;
            while (l <r){
                 int sum = nums[i] + nums[l] + nums[r];
                if(sum == 0){
                    res.add(Arrays.asList(nums[i],nums[l],nums[r]));
                    while (l<r && nums[l] == nums[l+1]) l++; // 去重
                    while (l<r && nums[r] == nums[r-1]) r--; // 去重
                    l++;
                    r--;
                }
                else if (sum < 0) l++;
                else if (sum > 0) r--;
            }
        }
        return res;
    }
}
```





# 7 整数反转

给你一个 32 位的有符号整数 `x` ，返回将 `x` 中的数字部分反转后的结果。

如果反转后整数超过 32 位的有符号整数的范围 `[−231, 231 − 1]` ，就返回 0。

**假设环境不允许存储 64 位整数（有符号或无符号）。**

 

**示例 1：**

```
输入：x = 123
输出：321
```

**示例 2：**

```
输入：x = -123
输出：-321
```

**示例 3：**

```
输入：x = 120
输出：21
```

**示例 4：**

```
输入：x = 0
输出：0
```

```java
class Solution {
    public int reverse(int x) {
         int res = 0;
        while (x != 0){
            int temp = x % 10;
            int pre = res;
            res = res*10 +temp;
            //如果不等于 则表示数据溢出了
            if (pre != res/10){
                return 0;
            }
            x = x/10;
        }
        return res;
    }
}
```



# 8  四数之和

给你一个由 `n` 个整数组成的数组 `nums` ，和一个目标值 `target` 。请你找出并返回满足下述全部条件且**不重复**的四元组 `[nums[a], nums[b], nums[c], nums[d]]` （若两个四元组元素一一对应，则认为两个四元组重复）：

- `0 <= a, b, c, d < n`
- `a`、`b`、`c` 和 `d` **互不相同**
- `nums[a] + nums[b] + nums[c] + nums[d] == target`

你可以按 **任意顺序** 返回答案 。

 

**示例 1：**

```
输入：nums = [1,0,-1,0,-2,2], target = 0
输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
```

**示例 2：**

```
输入：nums = [2,2,2,2,2], target = 8
输出：[[2,2,2,2]]
```

 

**提示：**

- `1 <= nums.length <= 200`

- `-109 <= nums[i] <= 109`

- `-109 <= target <= 109`

  **思路**  双重循环+双指针

  - 在确定第一个数之后，如果 nums[i]+nums[i+1]+nums[i+2]+nums[i+3]>target，说明此时剩下的三个数无论取什么值，四数之和一定大于 target，因此退出第一重循环；

  - 在确定第一个数之后，如果 nums[i]+nums[n−3]+nums[n−2]+nums[n−1]<target，说明此时剩下的三个数无论取什么值，四数之和一定小于 target，因此第一重循环直接进入下一轮，枚举 nums[i+1]；

  - 在确定前两个数之后，如果 nums[i]+nums[j]+nums[j+1]+nums[j+2]>target，说明此时剩下的两个数无论取什么值，四数之和一定大于 target，因此退出第二重循环；

  - 在确定前两个数之后，如果 nums[i]+nums[j]+nums[n−2]+nums[n−1]<target，说明此时剩下的两个数无论取什么值，四数之和一定小于 target，因此第二重循环直接进入下一轮，枚举 nums[j+1]。

    

```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
     List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length < 4) {
            return res;
        }
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n - 3; i++) {
            if (i >0 && nums[i] == nums[i-1])  continue;   //去重
            if((nums[i]+nums[i+1]+nums[i+2]+nums[i+3]) > target)  break;  //如果最近的四个数相加都大于目标值，则以后每次相加都大于目标值break
            if((nums[i]+nums[n-3]+nums[n-2]+nums[n-1]) < target)  continue; //说明此时剩下的三个数无论取什么值，四数之和一定小于 target\textit{target}target，因此第一重循环直接进入下一轮，枚举 nums[i+1]\textit{nums}[i+1]nums[i+1]
            for (int j = i+1; j < n-2 ; j++) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
                    break;
                }
                if ((long) nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) {
                    continue;
                }
                int left = j+1, right = n-1;
                while (left < right){
                    int sum = nums[i] + nums[j] + nums[left] + nums[right];
                    if (sum == target){
                        res.add(Arrays.asList(nums[i] , nums[j] ,nums[left] , nums[right]));
                        while (left < right && nums[j] == nums[j+1]){
                            left++;
                        }
                        left++;
                        while (left < right && nums[right] == nums[right-1]){
                            right--;
                        }
                        right--;
                    } else if (sum < target) {
                        left++;
                    }else {
                        right--;
                    }
                }

            }

        }
        return res;
    }
}
```



# 9  最长回文子串

给你一个字符串 `s`，找到 `s` 中最长的回文子串。

如果字符串的反序与原始字符串相同，则该字符串称为回文字符串。

 

**示例 1：**

```
输入：s = "babad"
输出："bab"
解释："aba" 同样是符合题意的答案。
```

**示例 2：**

```
输入：s = "cbbd"
输出："bb"



```

**中心扩展算法**
思路与算法

我们仔细观察一下方法一中的状态转移方程：


找出其中的状态转移链：

- P(i,j)←P(i+1,j−1)←P(i+2,j−2)←⋯←某一边界情况 P(i, j) \leftarrow P(i+1, j-1) \leftarrow P(i+2, j-2) \leftarrow \cdots \leftarrow \text{某一边界情况}
  P(i,j)←P(i+1,j−1)←P(i+2,j−2)←⋯←某一边界情况
  可以发现，所有的状态在转移的时候的可能性都是唯一的。也就是说，我们可以从每一种边界情况开始「扩展」，也可以得出所有的状态对应的答案。

- 边界情况即为子串长度为 111 或 222 的情况。我们枚举每一种边界情况，并从对应的子串开始不断地向两边扩展。如果两边的字母相同，我们就可以继续扩展，例如从 P(i+1,j−1)P(i+1,j-1)P(i+1,j−1) 扩展到 P(i,j)P(i,j)P(i,j)；如果两边的字母不同，我们就可以停止扩展，因为在这之后的子串都不能是回文串了。

- 聪明的读者此时应该可以发现，「边界情况」对应的子串实际上就是我们「扩展」出的回文串的「回文中心」。方法二的本质即为：我们枚举所有的「回文中心」并尝试「扩展」，直到无法扩展为止，此时的回文串长度即为此「回文中心」下的最长回文串长度。我们对所有的长度求出最大值，即可得到最终的答案。


```java

class Solution {
   public String longestPalindrome(String s) {
        if (s ==null || s.length() <1){
            return "";
        }
        int end =0 , start = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = getLength(s,i,i);
            int len2 = getLength(s,i,i+1);
            int len = Math.max(len1,len2);
            if (len > end - start){
                start = i - (len - 1) /2;
                end = i + len/2;
            }
        }

        return s.substring(start,end+1);
    }


    public int getLength(String s, int left,int right){   //返回当前为中心扩散的最大长度
        while (left >= 0 && right <= s.length()-1 && s.charAt(left) == s.charAt(right)){
            left--;
            right++;
        }
        return right - left -1;
    }
}
```





# 10  删除链表的倒数第 N 个结点

给你一个链表，删除链表的倒数第 `n` 个结点，并且返回链表的头结点。

 

**示例 1：**

![img](https://gitee.com/dwc12/image/raw/master/typoraImage/remove_ex1.jpg)

```
输入：head = [1,2,3,4,5], n = 2
输出：[1,2,3,5]
```

**示例 2：**

```
输入：head = [1], n = 1
输出：[]
```

**示例 3：**

```
输入：head = [1,2], n = 1
输出：[1]
```

**提示：**

- 链表中结点的数目为 `sz`
- `1 <= sz <= 30`
- `0 <= Node.val <= 100`
- `1 <= n <= sz`

**进阶：**你能尝试使用一趟扫描实现吗？

**计算链表长度法**

 

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode temp = new ListNode(0,head);
        int l = getlength(head);
        ListNode cur = temp;
        for(int i = 1; i < l-n+1;i++){    //第l-n+1个节点即为要删除的节点
            cur = cur.next;
        } 
        cur.next = cur.next.next;
        ListNode res = temp.next;
        return res;
    }

    public int getlength(ListNode head) {  //获取链表长度
        int length = 0;
        while (head != null) {
            ++length;
            head = head.next;
        }
        return length;
    }
}
```



**栈**

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
        public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0,head);
        Deque<ListNode> stack = new LinkedList<>();
        ListNode cur = dummy;
        while (cur != null){
            stack.push(cur);
            cur = cur.next;
        }
        for (int i = 0; i < n; i++) {
            stack.pop();
        }
        ListNode prev = stack.peek();
        prev.next = prev.next.next;
        ListNode res = dummy.next;
        return res;
    }

}
```









# 11 用两个栈实现队列

用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 `appendTail` 和 `deleteHead` ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，`deleteHead` 操作返回 -1 )

 

**示例 1：**

```
输入：
["CQueue","appendTail","deleteHead","deleteHead","deleteHead"]
[[],[3],[],[],[]]
输出：[null,null,3,-1,-1]
```

**示例 2：**

```
输入：
["CQueue","deleteHead","appendTail","appendTail","deleteHead","deleteHead"]
[[],[],[5],[2],[],[]]
输出：[null,-1,null,null,5,2]
```

**提示：**

- `1 <= values <= 10000`
- 最多会对` appendTail、deleteHead `进行` 10000` 次调用



```java
class CQueue {

    Deque<Integer> input;
    Deque<Integer> output;


    public CQueue() {
        this.input = new LinkedList<>();
        this.output = new LinkedList<>();
    }

    public void appendTail(int value) {
          input.push(value);
    }

    public int deleteHead() {
        if (!output.isEmpty()){
            return output.pop();
        }else {
            while (!input.isEmpty()){
                output.push(input.pop());
            }
            return output.isEmpty() ? -1 : output.pop();
        }
    }
}

```



# 12   包含min函数的栈

定义栈的数据结构，请在该类型中实现一个能够得到栈的最小元素的 min 函数在该栈中，调用 min、push 及 pop 的时间复杂度都是 O(1)。

**示例:**

```
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.min();   --> 返回 -3.
minStack.pop();
minStack.top();      --> 返回 0.
minStack.min();   --> 返回 -2.
```

**提示：**

1. 各函数的调用总次数不超过 20000 次

**min可以用辅助栈**

```java
class MinStack {

    Stack<Integer> stack;
    Stack<Integer>  min;
    /** initialize your data structure here. */
    public MinStack() {
       this.stack = new Stack<>();
       this.min = new Stack<>();
       min.push(Integer.MAX_VALUE);
    }

    public void push(int x) {
        stack.push(x);
        min.push(Math.min(min.peek(),x));

    }

    public void pop() {
        if (stack.isEmpty()){
            return;
        }else {
            stack.pop();
            min.pop();
        }
    }

    public int top() {
        if (stack.isEmpty()){
            return 0;
        }else {
            return stack.peek();
        }
    }

    public int min() {
      return min.peek();
    }
}
```









# 13  从尾到头打印链表

输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。

**示例 1：**

```
输入：head = [1,3,2]
输出：[2,3,1]
```

 

**限制：**

```
0 <= 链表长度 <= 10000
```



**方法一**

两次遍历链表，第一次获得链表长度，后面倒着往返回数组里面填入即可

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
     public int[] reversePrint(ListNode head) {
         int length = getLength(head);
         int[] res = new int[length];
         for (int i = length -1 ; i > -1; i--) {
             res[i] = head.val;
             head = head.next;
         }
         return res;
     }
     public int getLength(ListNode head){
         int l = 0;
         ListNode pre = new ListNode(0,head);
         while (pre.next !=null){
             l++;
             pre = pre.next;
         }
         return l;
     }

}
```

**方法二递归法**

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    int len = 0;
    int[] res;
    public int[] reversePrint(ListNode head) {
       if (head == null){
           res = new int[len];       //递归到末尾首先 new一个新数组
       }else {
           len++;                   //不为空 len++
           reversePrint(head.next);    //递归下一个节点
           res[res.length - len--] = head.val;      //本节点放入数组，len减一
       }
       return res;
     }

}
```







# 14  反转链表

定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。

 

**示例:**

```
输入: 1->2->3->4->5->NULL
输出: 5->4->3->2->1->NULL
```

 

**限制：**

```
0 <= 节点个数 <= 5000
```



**双指针法**

```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode cur = head, pre = null;
        while(cur != null) {
            ListNode tmp = cur.next; // 暂存后继节点 cur.next
            cur.next = pre;          // 修改 next 引用指向
            pre = cur;               // pre 暂存 cur
            cur = tmp;               // cur 访问下一节点
        }
        return pre;
    }
}
```







# 15    复杂链表的复制

请实现 `copyRandomList` 函数，复制一个复杂链表。在复杂链表中，每个节点除了有一个 `next` 指针指向下一个节点，还有一个 `random` 指针指向链表中的任意节点或者 `null`。

 

**示例 1：**

![img](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2020/01/09/e1.png)

```
输入：head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
输出：[[7,null],[13,0],[11,4],[10,2],[1,0]]
```

**示例 2：**

![img](https://gitee.com/dwc12/image/raw/master/typoraImage/e2.png)

```
输入：head = [[1,1],[2,1]]
输出：[[1,1],[2,1]]
```

**示例 3：**

**![img](https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2020/01/09/e3.png)**

```
输入：head = [[3,null],[3,0],[3,null]]
输出：[[3,null],[3,0],[3,null]]
```

**示例 4：**

```
输入：head = []
输出：[]
解释：给定的链表为空（空指针），因此返回 null。
```

 

**提示：**

- `-10000 <= Node.val <= 10000`
- `Node.random` 为空（null）或指向链表中的节点。
- 节点数目不超过 1000 。

**哈希表法**

```java
/*
// Definition for a Node.
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
*/
class Solution {
    public Node copyRandomList(Node head) {
       if (head == null) {
            return head;
        }
     //map中存的是(原节点，拷贝节点)的一个映射
        Map<Node, Node> map = new HashMap<>();
        for (Node cur = head; cur != null; cur = cur.next) {
            map.put(cur, new Node(cur.val));
        }
        //将拷贝的新的节点组织成一个链表
        for (Node cur = head; cur != null; cur = cur.next) {
            map.get(cur).next = map.get(cur.next);
            map.get(cur).random = map.get(cur.random);
        }
        return map.get(head);
    }
}
```





# 16 替换空格

请实现一个函数，把字符串 `s` 中的每个空格替换成"%20"。

 

**示例 1：**

```
输入：s = "We are happy."
输出："We%20are%20happy."
```

```java
class Solution {
    public String replaceSpace(String s) {
        StringBuilder res = new StringBuilder();
        for(Character c : s.toCharArray())
        {
            if(c == ' ') res.append("%20");
            else res.append(c);
        }
        return res.toString();
    }
}

```





# 17 左旋转字符串

字符串的左旋转操作是把字符串前面的若干个字符转移到字符串的尾部。请定义一个函数实现字符串左旋转操作的功能。比如，输入字符串"abcdefg"和数字2，该函数将返回左旋转两位得到的结果"cdefgab"。

 

**示例 1：**

```
输入: s = "abcdefg", k = 2
输出: "cdefgab"
```

**示例 2：**

```
输入: s = "lrloseumgh", k = 6
输出: "umghlrlose"
```

```java
class Solution {
    public String reverseLeftWords(String s, int n) {
         int len = s.length();
        char[] str = new char[len];
        for (int i = 0 ; i < s.length() ; i ++) {
            int idx = ( i - n + len) % len ;
            str[idx] = s.charAt(i);
        }
        return new String(str);
    }
}
```





# 18 数组中重复的数字

找出数组中重复的数字。


在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。

**示例 1：**

```
输入：
[2, 3, 1, 0, 2, 5, 3]
输出：2 或 3 
```



**方法一   哈希法**

出现多次在对应的位置加一

```java
class Solution {
    public int findRepeatNumber(int[] nums) {
        int[] arr =new int[nums.length];
        for(int i = 0 ; i <arr.length;i++){
            arr[nums[i]]++;
            if(arr[nums[i]] > 1)   return nums[i];
        }
        return -1;
    }
}
```

**排序法**

```java
class Solution {
    public int findRepeatNumber(int[] nums) {
       Arrays.sort(nums);
       for(int i = 0 ;i< nums.length;i++){
           if(nums[i] == nums[i+1]){
               return nums[i];
           }
       }
       return -1;
    }
}
```



# 19  在排序数组中查找数字 I

统计一个数字在排序数组中出现的次数。

 

**示例 1:**

```
输入: nums = [5,7,7,8,8,10], target = 8
输出: 2
```

**示例 2:**

```
输入: nums = [5,7,7,8,8,10], target = 6
输出: 0
```



**暴力查找**

```java
class Solution {
    public int search(int[] nums, int target) {
         int res = 0;
         for(int i = 0; i < nums.length ; i++){
             if(nums[i] == target){
                 res++;
             }
             if(i < nums.length-1 && nums[i+1] != nums[i] && res >0){  
                 break;
             }
         }
         return res;
    }
}
```

**二分查找**

```java
class Solution {
    public int search(int[] nums, int target) {
        int leftIdx = binarySearch(nums, target, true);   //查找第一次出现target的下标
        int rightIdx = binarySearch(nums, target, false) - 1;   //查找最后一次出现target的下标
        if (leftIdx <= rightIdx && rightIdx < nums.length && nums[leftIdx] == target && nums[rightIdx] == target) {
            return rightIdx - leftIdx + 1;
        } 
        return 0;
    }

    public int binarySearch(int[] nums, int target, boolean lower) {
        int left = 0, right = nums.length - 1, ans = nums.length;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (nums[mid] > target || (lower && nums[mid] >= target)) {
                right = mid - 1;
                ans = mid;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }
}

```



# 20  0～n-1中缺失的数字

一个长度为n-1的递增排序数组中的所有数字都是唯一的，并且每个数字都在范围0～n-1之内。在范围0～n-1内的n个数字中有且只有一个数字不在该数组中，请找出这个数字。

 

**示例 1:**

```
输入: [0,1,3]
输出: 2
```

**示例 2:**

```
输入: [0,1,2,3,4,5,6,7,9]
输出: 8
```



**二分查找**

```java
class Solution {
   public int missingNumber(int[] nums) {
       int l = 0;
       int r = nums.length - 1;
       while(l <= r){            
           int mid = l + (r - l) / 2;
           if(nums[mid] == mid){    //如果等于说明前面还没有发生错位
               l = mid + 1;  
           }else{                      //如果没有则前面已经发生错位了
               r = mid - 1;
           }
       }
       return l;
   }
}
```



# 21   二维数组中的查找

在一个 n * m 的二维数组中，每一行都按照从左到右 **非递减** 的顺序排序，每一列都按照从上到下 **非递减** 的顺序排序。请完成一个高效的函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。

 

**示例:**

现有矩阵 matrix 如下：

```
[
  [1,   4,  7, 11, 15],
  [2,   5,  8, 12, 19],
  [3,   6,  9, 16, 22],
  [10, 13, 14, 17, 24],
  [18, 21, 23, 26, 30]
]
```

给定 target = `5`，返回 `true`。

给定 target = `20`，返回 `false`。



**暴力破解**

```java
class Solution {   //空间复杂度  O（n2）
    
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
          for(int i =0;i<matrix.length;i++){
              for(int j = 0 ; j<matrix[i].length;j++){
                  if(matrix[i][j] == target)  return true;
              }
          }
          return false;
    }
}
```

**二叉搜索树**

![image-20230308090730538](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230308090730538.png)

```java
class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
         int i = matrix.length - 1 , j =0;
         while(i >= 0 && j < matrix[0].length){
             if(matrix[i][j] < target) j++;
             else if(matrix[i][j] > target) i--;
             else return true;
         }
         return false;
    }
}
```





# 22   剑指 Offer 11. 旋转数组的最小数字

把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。

给你一个可能存在 **重复** 元素值的数组 `numbers` ，它原来是一个升序排列的数组，并按上述情形进行了一次旋转。请返回旋转数组的**最小元素**。例如，数组 `[3,4,5,1,2]` 为 `[1,2,3,4,5]` 的一次旋转，该数组的最小值为 1。 

注意，数组 `[a[0], a[1], a[2], ..., a[n-1]]` 旋转一次 的结果为数组 `[a[n-1], a[0], a[1], a[2], ..., a[n-2]]` 。

 

**示例 1：**

```
输入：numbers = [3,4,5,1,2]
输出：1
```

**示例 2：**

```
输入：numbers = [2,2,2,0,1]
输出：0
```

 

**提示：**

- `n == numbers.length`
- `1 <= n <= 5000`
- `-5000 <= numbers[i] <= 5000`
- `numbers` 原来是一个升序排序的数组，并进行了 `1` 至 `n` 次旋转



**二分法**

```java
class Solution {
    public int minArray(int[] numbers) {
         int left = 0;
         int right = numbers.length -1;
        if(right == 0){
            return numbers[0];
        }
        while(left < right){
            int mid = left + (right - left) /2;
            if(numbers[mid] < numbers[right]){    //mid<right   分界点肯定在mid左边或者mid
                right = mid;       
            }else if(numbers[mid] > numbers[right]){    //mid>right  分界点肯定在mid右边
                left = mid +1;
            }else{     //如果重复左移
                right--;
            }
        }
       return numbers[left];
    }
}
```



# 23剑指 Offer 50. 第一个只出现一次的字符

在字符串 s 中找出第一个只出现一次的字符。如果没有，返回一个单空格。 s 只包含小写字母。

**示例 1:**

```
输入：s = "abaccdeff"
输出：'b'
```

**示例 2:**

```
输入：s = "" 
输出：' '
```

 

**限制：**

```
0 <= s 的长度 <= 50000
```





**哈希映射**

```java
class Solution {
    public char firstUniqChar(String s) {
      Map<Character, Integer> map = new HashMap<>();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            map.put(ch,map.getOrDefault(ch,0) +1);
        }
        for (int i = 0; i < n; i++) {
            if (map.get( s.charAt(i)) == 1){
                return s.charAt(i);
            }
        }
        return ' ';
    }
}
```





















































# 数据库 mysql



# 1.* 组合两个表*

 表: `Person`

```
+-------------+---------+
| 列名         | 类型     |
+-------------+---------+
| PersonId    | int     |
| FirstName   | varchar |
| LastName    | varchar |
+-------------+---------+
personId 是该表的主键列。
该表包含一些人的 ID 和他们的姓和名的信息。
```

 

表: `Address`

```
+-------------+---------+
| 列名         | 类型    |
+-------------+---------+
| AddressId   | int     |
| PersonId    | int     |
| City        | varchar |
| State       | varchar |
+-------------+---------+
addressId 是该表的主键列。
该表的每一行都包含一个 ID = PersonId 的人的城市和州的信息。
```

编写一个SQL查询来报告 `Person` 表中每个人的姓、名、城市和州。如果 `personId` 的地址不在 `Address` 表中，则报告为空  `null` 。



![image-20230220210133125](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230220210133125.png)

```mysql
select firstName,lastName,city,state

from Person P left join Address a 

on P.personId = a.personId
```





# *2 第二高的薪水*

`Employee` 表：

```
+-------------+------+
| Column Name | Type |
+-------------+------+
| id          | int  |
| salary      | int  |
+-------------+------+
id 是这个表的主键。
表的每一行包含员工的工资信息。
```

 编写一个 SQL 查询，获取并返回 `Employee` 表中第二高的薪水 。如果不存在第二高的薪水，查询应该返回 `null` 。

查询结果如下例所示。

![image-20230220211538553](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230220211538553.png)

#### 方法一：使用子查询和 `LIMIT` 子句

```mysql
select (select distinct salary  from Employee
         order by salary desc 
         limit 1 OFFSET 1 ) as SecondHighestSalary 
```

#### 方法二：使用 `IFNULL` 和 `LIMIT` 子句

```mysql
SELECT
    IFNULL(
      (SELECT DISTINCT Salary
       FROM Employee
       ORDER BY Salary DESC
        LIMIT 1 OFFSET 1),
    NULL) AS SecondHighestSalary
```





# *3 第N高的薪水*

表: `Employee`

```
+-------------+------+
| Column Name | Type |
+-------------+------+
| id          | int  |
| salary      | int  |
+-------------+------+
Id是该表的主键列。
该表的每一行都包含有关员工工资的信息。
```

编写一个SQL查询来报告 `Employee` 表中第 `n` 高的工资。如果没有第 `n` 个最高工资，查询应该报告为 `null` 。

查询结果格式如下所示。

![image-20230220212653350](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230220212653350.png)

```mysql
CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
   set N := N-1;
  RETURN (
      # Write your MySQL query statement below.
      SELECT (
          SELECT distinct salary from Employee
          order by salary desc
          limit N,1
      ) as getNthHighestSalary
  );
END
```



# *4 分数排名*

表: `Scores`

```
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| score       | decimal |
+-------------+---------+
Id是该表的主键。
该表的每一行都包含了一场比赛的分数。Score是一个有两位小数点的浮点值。
```

编写 SQL 查询对分数进行排序。排名按以下规则计算:

- 分数应按从高到低排列。
- 如果两个分数相等，那么两个分数的排名应该相同。
- 在排名相同的分数后，排名数应该是下一个连续的整数。换句话说，排名之间不应该有空缺的数字。

按 `score` 降序返回结果表。

![image-20230221214129359](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230221214129359.png)

```mysql
select a.score as score,
(select count(distinct b.score) from Scores b where b.score >= a.score ) as 'rank'
from Scores a
order by a.score desc

```

最后的结果包含两个部分，第一部分是降序排列的分数，第二部分是每个分数对应的排名。

第一部分不难写：

```mysql
select a.Score as Score
from Scores a
order by a.Score DESC
```

比较难的是第二部分。假设现在给你一个分数X，如何算出它的排名Rank呢？ 我们可以先提取出大于等于X的所有分数集合H，将H去重后的元素个数就是X的排名。比如你考了99分，但最高的就只有99分，那么去重之后集合H里就只有99一个元素，个数为1，因此你的Rank为1。 先提取集合H：

```mysql
select b.Score from Scores b where b.Score >= X;
```

我们要的是集合H去重之后的元素个数，因此升级为：

```mysql
select count(distinct b.Score) from Scores b where b.Score >= X as Rank;
```

而从结果的角度来看，第二部分的Rank是对应第一部分的分数来的，所以这里的X就是上面的a.Score，把两部分结合在一起为：

```mysql
select a.Score as Score,
(select count(distinct b.Score) from Scores b where b.Score >= a.Score) as Rank
from Scores a
```





# *5 连续出现的数字*

```
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| num         | varchar |
+-------------+---------+
id 是这个表的主键。
```

编写一个 SQL 查询，查找所有至少连续出现三次的数字。

返回的结果表中的数据可以按 **任意顺序** 排列。

查询结果格式如下面的例子所示：

![image-20230221215636537](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230221215636537.png)

```mysql
select distinct l1.Num as ConsecutiveNums
from Logs l1,Logs l2,Logs l3
where l1.Id = l2.id -1 and l2.Id = l3.Id -1 
and l1.Num = l2.Num AND l2.Num = l3.Num
```



# *6 超过经理收入的员工*

表：`Employee` 

```
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
| salary      | int     |
| managerId   | int     |
+-------------+---------+
Id是该表的主键。
该表的每一行都表示雇员的ID、姓名、工资和经理的ID。
```

 编写一个SQL查询来查找收入比经理高的员工。

以 **任意顺序** 返回结果表。

![image-20230221220334773](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230221220334773.png)

```mysql
select a.name as Employee
from Employee a,
Employee b
where a.managerId = b.id and a.salary > b.salary
```



## *7 查找重复的电子邮箱*

编写一个 SQL 查询，查找 `Person` 表中所有重复的电子邮箱。

**示例：**

```
+----+---------+
| Id | Email   |
+----+---------+
| 1  | a@b.com |
| 2  | c@d.com |
| 3  | a@b.com |
+----+---------+
```

根据以上输入，你的查询应返回以下结果：

```
+---------+
| Email   |
+---------+
| a@b.com |
+---------+
```

**说明：**所有电子邮箱都是小写字母。

```mysql
select distinct a.Email
from Person a, Person b
where a.Email like b.Email and a.Id != b.Id
```

方法2

```mysql
select Email from
(
  select Email, count(Email) as num
  from Person
  group by Email
) as statistic
where num > 1
```

向 GROUP BY 添加条件的一种更常用的方法是使用 HAVING 子句，该子句更为简单高效。所以我们可以将上面的解决方案重写为：

```mysql
select Email
from Person
group by Email
having count(Email) > 1;
```





## *8 部门工资最高的员工*

表： `Employee`

```
+--------------+---------+
| 列名          | 类型    |
+--------------+---------+
| id           | int     |
| name         | varchar |
| salary       | int     |
| departmentId | int     |
+--------------+---------+
id是此表的主键列。
departmentId是Department表中ID的外键。
此表的每一行都表示员工的ID、姓名和工资。它还包含他们所在部门的ID。
```

表： `Department`

```
+-------------+---------+
| 列名         | 类型    |
+-------------+---------+
| id          | int     |
| name        | varchar |
+-------------+---------+
id是此表的主键列。
此表的每一行都表示一个部门的ID及其名称。
```

 编写SQL查询以查找每个部门中薪资最高的员工。
按 **任意顺序** 返回结果表。

查询结果格式如下例所示。

 

**示例 1:**

```
输入：
Employee 表:
+----+-------+--------+--------------+
| id | name  | salary | departmentId |
+----+-------+--------+--------------+
| 1  | Joe   | 70000  | 1            |
| 2  | Jim   | 90000  | 1            |
| 3  | Henry | 80000  | 2            |
| 4  | Sam   | 60000  | 2            |
| 5  | Max   | 90000  | 1            |
+----+-------+--------+--------------+
Department 表:
+----+-------+
| id | name  |
+----+-------+
| 1  | IT    |
| 2  | Sales |
+----+-------+
输出：
+------------+----------+--------+
| Department | Employee | Salary |
+------------+----------+--------+
| IT         | Jim      | 90000  |
| Sales      | Henry    | 80000  |
| IT         | Max      | 90000  |
+------------+----------+--------+
解释：Max 和 Jim 在 IT 部门的工资都是最高的，Henry 在销售部的工资最高。
```

```mysql
select d.name as Department ,e.name as Employee,e.salary as salary
from Employee e join Department d on e.departmentId = d.id 
where (e.departmentId,salary) in
      (select departmentId ,max(salary)
       from Employee
       group by departmentId
        )
```



# *9 部门工资前三高的所有员工*

表: `Employee`

```
+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| id           | int     |
| name         | varchar |
| salary       | int     |
| departmentId | int     |
+--------------+---------+
Id是该表的主键列。
departmentId是Department表中ID的外键。
该表的每一行都表示员工的ID、姓名和工资。它还包含了他们部门的ID。
```

 

表: `Department`

```
+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
+-------------+---------+
Id是该表的主键列。
该表的每一行表示部门ID和部门名。
```

公司的主管们感兴趣的是公司每个部门中谁赚的钱最多。一个部门的 **高收入者** 是指一个员工的工资在该部门的 **不同** 工资中 **排名前三** 。

编写一个SQL查询，找出每个部门中 **收入高的员工** 。

以 **任意顺序** 返回结果表。

查询结果格式如下所示。

```
输入: 
Employee 表:
+----+-------+--------+--------------+
| id | name  | salary | departmentId |
+----+-------+--------+--------------+
| 1  | Joe   | 85000  | 1            |
| 2  | Henry | 80000  | 2            |
| 3  | Sam   | 60000  | 2            |
| 4  | Max   | 90000  | 1            |
| 5  | Janet | 69000  | 1            |
| 6  | Randy | 85000  | 1            |
| 7  | Will  | 70000  | 1            |
+----+-------+--------+--------------+
Department  表:
+----+-------+
| id | name  |
+----+-------+
| 1  | IT    |
| 2  | Sales |
+----+-------+
输出: 
+------------+----------+--------+
| Department | Employee | Salary |
+------------+----------+--------+
| IT         | Max      | 90000  |
| IT         | Joe      | 85000  |
| IT         | Randy    | 85000  |
| IT         | Will     | 70000  |
| Sales      | Henry    | 80000  |
| Sales      | Sam      | 60000  |
+------------+----------+--------+
解释:
在IT部门:
- Max的工资最高
- 兰迪和乔都赚取第二高的独特的薪水
- 威尔的薪水是第三高的

在销售部:
- 亨利的工资最高
- 山姆的薪水第二高
- 没有第三高的工资，因为只有两名员工
```

```mysql

select d.name as  Department,e.name as Employee,e.salary as salary
from Employee e join Department d 
on e.departmentId = d.id 
 where 3  > (select count(distinct e1.salary)
             from Employee e1    
             where e1.salary > e.salary 
              and e.departmentId = e1.departmentId
             )
```





# *10 上升的温度*

```
+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| recordDate    | date    |
| temperature   | int     |
+---------------+---------+
id 是这个表的主键
该表包含特定日期的温度信息
```

 

编写一个 SQL 查询，来查找与之前（昨天的）日期相比温度更高的所有日期的 `id` 。

返回结果 **不要求顺序** 。

查询结果格式如下例。	

```
输入：
Weather 表：
+----+------------+-------------+
| id | recordDate | Temperature |
+----+------------+-------------+
| 1  | 2015-01-01 | 10          |
| 2  | 2015-01-02 | 25          |
| 3  | 2015-01-03 | 20          |
| 4  | 2015-01-04 | 30          |
+----+------------+-------------+
输出：
+----+
| id |
+----+
| 2  |
| 4  |
+----+
解释：
2015-01-02 的温度比前一天高（10 -> 25）
2015-01-04 的温度比前一天高（20 -> 30）
```

```mysql
SELECT nw.id 
FROM Weather w,Weather nw
WHERE DATEDIFF(nw.recordDate,w.recordDate)=1 
AND w.Temperature < nw.Temperature
```





## *11 有趣的电影*

某城市开了一家新的电影院，吸引了很多人过来看电影。该电影院特别注意用户体验，专门有个 LED显示板做电影推荐，上面公布着影评和相关电影描述。

作为该电影院的信息部主管，您需要编写一个 SQL查询，找出所有影片描述为**非** `boring` (不无聊) 的并且 **id 为奇数** 的影片，结果请按等级 `rating` 排列。

 

例如，下表 `cinema`:

```
+---------+-----------+--------------+-----------+
|   id    | movie     |  description |  rating   |
+---------+-----------+--------------+-----------+
|   1     | War       |   great 3D   |   8.9     |
|   2     | Science   |   fiction    |   8.5     |
|   3     | irish     |   boring     |   6.2     |
|   4     | Ice song  |   Fantacy    |   8.6     |
|   5     | House card|   Interesting|   9.1     |
+---------+-----------+--------------+-----------+
```

对于上面的例子，则正确的输出是为：

```
+---------+-----------+--------------+-----------+
|   id    | movie     |  description |  rating   |
+---------+-----------+--------------+-----------+
|   5     | House card|   Interesting|   9.1     |
|   1     | War       |   great 3D   |   8.9     |
+---------+-----------+--------------+-----------+
```

```mysql
select * from cinema
where description != 'boring' and id % 2 !=0
order by rating  desc


或者
select *
from cinema
where mod(id, 2) = 1 and description != 'boring'
order by rating DESC
```

