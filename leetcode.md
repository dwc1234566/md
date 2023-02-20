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
                o.remove(s.charAt(i-1));
            }
            while(r < n-1 && !o.contains(s.charAt(r+1))){
                o.add(s.charAt(r+1));
                r++;
            }
            res = Math.max(res,r-i+1);
        }

         return res;
    }
}
```



# *4 . 寻找两个正序数组的中位数*

 给定两个大小分别为 `m` 和 `n` 的正序（从小到大）数组 `nums1` 和 `nums2`。请你找出并返回这两个正序数组的 **中位数** 。











































# 数据库 mysql



## 1.* 组合两个表*

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





## *2 第二高的薪水*

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





## *3 第N高的薪水*

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

