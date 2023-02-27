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

 贪心算法

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







# 6  三树之和

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

