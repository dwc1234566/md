# 1.两数之和

![image-20230215210417233](https://gitee.com/dwc12/image/raw/master/typoraImage/image-20230215210417233.png)

`

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

