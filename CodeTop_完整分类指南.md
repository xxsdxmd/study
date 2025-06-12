# 🎯 CodeTop 算法分类精进指南

> **基于真实面试数据的系统化刷题方案**  
> 涵盖 400+ 高频题目，23 大核心题型，配套模板代码与训练路径

## 📊 数据概览

- **题目总数**: 400 道
- **核心分类**: 24 个
- **数据来源**: CodeTop 面试真题统计
- **更新频率**: 实时同步最新面试趋势

## 📚 分类目录

1. [滑动窗口](#滑动窗口) - *6 题*
2. [双指针](#双指针) - *10 题*
3. [哈希表](#哈希表) - *7 题*
4. [二分搜索](#二分搜索) - *16 题*
5. [栈/单调结构](#栈单调结构) - *17 题*
6. [字符串处理](#字符串处理) - *17 题*
7. [链表](#链表) - *22 题*
8. [树·BFS层序](#树BFS层序) - *8 题*
9. [树·DFS/递归DP](#树DFS递归DP) - *29 题*
10. [动态规划](#动态规划) - *36 题*
11. [回溯&组合](#回溯&组合) - *15 题*
12. [贪心](#贪心) - *13 题*
13. [设计题](#设计题) - *14 题*
14. [堆&优先队列](#堆&优先队列) - *8 题*
15. [图论&并查集](#图论&并查集) - *5 题*
16. [排序](#排序) - *13 题*
17. [模拟](#模拟) - *22 题*
18. [数学&位运算](#数学&位运算) - *17 题*
19. [前缀和](#前缀和) - *4 题*
20. [Trie字典树](#Trie字典树) - *1 题*
21. [分治](#分治) - *11 题*
22. [线段树](#线段树) - *1 题*
23. [拓扑排序](#拓扑排序) - *3 题*

---

## 滑动窗口

### 💡 核心思想
解决子串/子数组的长度、数量等线性扫描问题

### 🔍 识别关键词
`子串` | `子数组` | `最长` | `最短` | `不重复` | `包含`

### 📝 核心模板
```java
// 1. 定长滑动窗口模板
public int fixedWindow(int[] nums, int k) {
    if (nums.length < k) return 0;
    
    int[] cnt = new int[1001];  // 根据数据范围调整
    int left = 0, best = Integer.MIN_VALUE;
    
    for (int right = 0; right < nums.length; right++) {
        cnt[nums[right]]++;  // 右边界进入窗口
        
        if (right - left + 1 == k) {  // 窗口达到固定大小
            // 这里处理窗口内的逻辑，更新best
            best = Math.max(best, windowValue(cnt));
            
            cnt[nums[left++]]--;  // 左边界移出窗口
        }
    }
    return best;
}

// 2. 不定长滑动窗口 - 求最小
public int minWindow(String s, String t) {
    if (s.length() == 0 || t.length() == 0) return 0;
    
    int[] need = new int[128], window = new int[128];
    int needCount = 0;
    
    // 统计目标字符
    for (char c : t.toCharArray()) {
        if (need[c]++ == 0) needCount++;
    }
    
    int left = 0, validCount = 0;
    int best = Integer.MAX_VALUE;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        window[c]++;
        if (window[c] == need[c]) validCount++;
        
        // 尝试收缩窗口 - 当满足条件时求最小
        while (validCount == needCount) {
            best = Math.min(best, right - left + 1);
            
            char d = s.charAt(left++);
            if (window[d]-- == need[d]) validCount--;
        }
    }
    return best == Integer.MAX_VALUE ? 0 : best;
}

// 3. 不定长滑动窗口 - 求最大
public int maxWindow(String s) {
    if ("".equals(s)) return 0;
    
    int[] cnt = new int[128];
    int left = 0, best = Integer.MIN_VALUE;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        cnt[c]++;
        
        // 当窗口不满足条件时收缩 - 保持条件下求最大
        while (cnt[c] > 1) {  // 这里的条件根据具体题目调整
            cnt[s.charAt(left++)]--;
        }
        
        best = Math.max(best, right - left + 1);
    }
    return best == Integer.MIN_VALUE ? 0 : best;
}
```

### 🎯 训练建议
1. 先掌握定长窗口，理解窗口进入和移出的时机
2. 不定长求最小：满足条件时尽量收缩窗口
3. 不定长求最大：不满足条件时才收缩窗口
4. 重点理解while循环的收缩条件设置
5. 建议练习题：定长(239,567) 求最小(76,209) 求最大(3,424)

### 📋 题目清单 (6 题)

**🔥 高频必刷**

- `3` **无重复字符的最长子串** - *Medium* `频率:937`
- `76` **最小覆盖子串** - *Hard* `频率:110`

**📚 进阶练习**

- `209` 长度最小的子数组 - *Medium* `频率:63`

**🎓 扩展了解**

- `1004` 最大连续1的个数 III - *Medium* `频率:24`
- `567` 字符串的排列 - *Medium* `频率:20`
- `438` 找到字符串中所有字母异位词 - *Medium* `频率:15`

---

## 双指针

### 💡 核心思想
通过两个指针的移动来解决数组/字符串问题

### 🔍 识别关键词
`两数之和` | `三数之和` | `回文` | `去重` | `合并`

### 📝 核心模板
```java
// 对撞双指针
public int[] twoSum(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left < right) {
        int sum = nums[left] + nums[right];
        if (sum == target) return new int[]{left, right};
        else if (sum < target) left++;
        else right--;
    }
    return new int[]{};
}

// 快慢指针
public ListNode detectCycle(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
        slow = slow.next;
        fast = fast.next.next;
        if (slow == fast) {
            ListNode ptr = head;
            while (ptr != slow) {
                ptr = ptr.next;
                slow = slow.next;
            }
            return ptr;
        }
    }
    return null;
}
```

### 🎯 训练建议
1. 熟练掌握对撞指针和快慢指针两种模式
2. 练习排序数组的双指针技巧
3. 掌握去重和边界处理
4. 建议练习题：15, 16, 18, 11, 42

### 📋 题目清单 (10 题)

**🔥 高频必刷**

- `15` **三数之和** - *Medium* `频率:411`
- `88` **合并两个有序数组** - *Easy* `频率:251`

**📚 进阶练习**

- `283` 移动零 - *Easy* `频率:57`

**🎓 扩展了解**

- `11` 盛最多水的容器 - *Medium* `频率:49`
- `16` 最接近的三数之和 - *Medium* `频率:45`
- `26` 删除排序数组中的重复项 - *Easy* `频率:43`
- `75` 颜色分类 - *Medium* `频率:40`
- `18` 四数之和 - *Medium* `频率:19`

---

## 哈希表

### 💡 核心思想
利用哈希表的快速查找特性解决问题

### 🔍 识别关键词
`两数之和` | `频次` | `去重` | `映射`

### 📝 核心模板
```java
// 哈希表统计
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return new int[]{};
}

// 前缀和 + 哈希表
public int subarraySum(int[] nums, int k) {
    Map<Integer, Integer> map = new HashMap<>();
    map.put(0, 1);
    int sum = 0, count = 0;
    for (int num : nums) {
        sum += num;
        count += map.getOrDefault(sum - k, 0);
        map.put(sum, map.getOrDefault(sum, 0) + 1);
    }
    return count;
}
```

### 🎯 训练建议
1. 理解空间换时间的思想
2. 熟练使用HashMap和HashSet
3. 掌握前缀和+哈希的组合技巧
4. 建议练习题：1, 49, 128, 560, 454

### 📋 题目清单 (7 题)

**🔥 高频必刷**

- `1` **两数之和** - *Easy* `频率:277`
- `41` **缺失的第一个正数** - *Hard* `频率:108`

**📚 进阶练习**

- `128` 最长连续序列 - *Medium* `频率:78`

**🎓 扩展了解**

- `149` 直线上最多的点数 - *Hard* `频率:11`
- `340` 至多包含 K 个不同字符的最长子串 - *Hard* `频率:11`
- `36` 有效的数独 - *Medium* `频率:10`
- `409` 最长回文串 - *Easy* `频率:8`

---

## 二分搜索

### 💡 核心思想
在有序或部分有序的数据结构中快速定位

### 🔍 识别关键词
`有序` | `旋转` | `峰值` | `搜索` | `第k个`

### 📝 核心模板
```java
// 标准二分搜索
public int binarySearch(int[] nums, int target) {
    int left = 0, right = nums.length - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (nums[mid] == target) return mid;
        else if (nums[mid] < target) left = mid + 1;
        else right = mid - 1;
    }
    return -1;
}

// 二分答案模板
public int searchAnswer(int[] nums, int target) {
    int left = 0, right = 1000000;
    while (left < right) {
        int mid = left + (right - left) / 2;
        if (check(mid, nums, target)) right = mid;
        else left = mid + 1;
    }
    return left;
}
```

### 🎯 训练建议
1. 掌握三种二分写法：闭区间、左闭右开、开区间
2. 理解二分答案的适用场景
3. 练习旋转数组和矩阵中的二分
4. 建议练习题：33, 34, 153, 162, 278

### 📋 题目清单 (16 题)

**🔥 高频必刷**

- `33` **搜索旋转排序数组** - *Medium* `频率:270`
- `704` **二分查找** - *Easy* `频率:134`
- `69` **x 的平方根** - *Easy* `频率:125`

**📚 进阶练习**

- `34` 在排序数组中查找元素的第一个和最后一个位置 - *Medium* `频率:87`
- `162` 寻找峰值 - *Medium* `频率:75`
- `153` 寻找旋转排序数组中的最小值 - *Medium* `频率:53`

**🎓 扩展了解**

- `74` 搜索二维矩阵 - *Medium* `频率:44`
- `287` 寻找重复数 - *Medium* `频率:30`
- `349` 两个数组的交集 - *Easy* `频率:23`
- `1044` 最长重复子串 - *Hard* `频率:19`
- `154` 寻找旋转排序数组中的最小值 II - *Hard* `频率:18`

---

## 栈/单调结构

### 💡 核心思想
利用栈的LIFO特性解决括号、表达式、单调性问题

### 🔍 识别关键词
`括号` | `表达式` | `单调` | `下一个更大` | `柱状图`

### 📝 核心模板
```java
// 单调栈找下一个更大元素
public int[] nextGreaterElements(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Deque<Integer> stack = new ArrayDeque<>();
    
    for (int i = 0; i < 2 * n; i++) {
        while (!stack.isEmpty() && nums[stack.peek()] < nums[i % n]) {
            result[stack.pop()] = nums[i % n];
        }
        if (i < n) stack.push(i);
    }
    return result;
}

// 括号匹配
public boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    Map<Character, Character> map = Map.of(')', '(', '}', '{', ']', '[');
    
    for (char c : s.toCharArray()) {
        if (map.containsKey(c)) {
            if (stack.isEmpty() || stack.pop() != map.get(c)) return false;
        } else {
            stack.push(c);
        }
    }
    return stack.isEmpty();
}
```

### 🎯 训练建议
1. 理解单调栈维护单调性的原理
2. 掌握括号匹配的经典应用
3. 练习柱状图、雨水等面积计算问题
4. 建议练习题：20, 42, 84, 496, 739

### 📋 题目清单 (17 题)

**🔥 高频必刷**

- `20` **有效的括号** - *Easy* `频率:250`
- `42` **接雨水** - *Hard* `频率:173`
- `32` **最长有效括号** - *Hard* `频率:120`

**📚 进阶练习**

- `227` 基本计算器 II - *Medium* `频率:66`
- `739` 每日温度 - *Medium* `频率:56`
- `224` 基本计算器 - *Hard* `频率:55`

---

## 字符串处理

### 💡 核心思想
字符串的搜索、匹配、转换和处理

### 🔍 识别关键词
`回文` | `子串` | `匹配` | `转换` | `编码` | `解码`

### 📝 核心模板
```java
// 字符串处理通用模板
public String processString(String s) {
    StringBuilder sb = new StringBuilder();
    int n = s.length();
    
    for (int i = 0; i < n; i++) {
        char c = s.charAt(i);
        // 具体处理逻辑
        sb.append(c);
    }
    return sb.toString();
}

// KMP模式匹配
public int strStr(String haystack, String needle) {
    if (needle.isEmpty()) return 0;
    
    int[] lps = buildLPS(needle);
    int i = 0, j = 0;
    
    while (i < haystack.length()) {
        if (haystack.charAt(i) == needle.charAt(j)) {
            i++; j++;
        }
        
        if (j == needle.length()) return i - j;
        else if (i < haystack.length() && haystack.charAt(i) != needle.charAt(j)) {
            if (j != 0) j = lps[j - 1];
            else i++;
        }
    }
    return -1;
}
```

### 🎯 训练建议
1. 掌握StringBuilder的使用
2. 理解KMP算法的原理
3. 练习字符串的各种变换操作
4. 建议练习题：5, 415, 28, 151, 394

### 📋 题目清单 (17 题)

**🔥 高频必刷**

- `5` **最长回文子串** - *Medium* `频率:286`
- `165` **比较版本号** - *Medium* `频率:130`
- `8` **字符串转换整数 (atoi)** - *Medium* `频率:123`
- `43` **字符串相乘** - *Medium* `频率:108`

**📚 进阶练习**

- `151` 翻转字符串里的单词 - *Medium* `频率:98`
- `394` 字符串解码 - *Medium* `频率:83`
- `14` 最长公共前缀 - *Easy* `频率:75`
- `468` 验证IP地址 - *Medium* `频率:54`

**🎓 扩展了解**

- `125` 验证回文串 - *Easy* `频率:36`
- `344` 反转字符串 - *Easy* `频率:23`
- `459` 重复的子字符串 - *Easy* `频率:21`
- `647` 回文子串 - *Medium* `频率:19`
- `6` Z 字形变换 - *Medium* `频率:17`

---

## 链表

### 💡 核心思想
链表的遍历、反转、合并等操作

### 🔍 识别关键词
`反转` | `合并` | `环` | `相交` | `删除`

### 📝 核心模板
```java
// 反转链表
public ListNode reverseList(ListNode head) {
    ListNode prev = null, curr = head;
    while (curr != null) {
        ListNode next = curr.next;
        curr.next = prev;
        prev = curr;
        curr = next;
    }
    return prev;
}

// 合并两个有序链表
public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;
    
    while (l1 != null && l2 != null) {
        if (l1.val <= l2.val) {
            curr.next = l1;
            l1 = l1.next;
        } else {
            curr.next = l2;
            l2 = l2.next;
        }
        curr = curr.next;
    }
    curr.next = l1 != null ? l1 : l2;
    return dummy.next;
}
```

### 🎯 训练建议
1. 熟练使用虚拟头节点简化边界处理
2. 掌握快慢指针检测环和找中点
3. 练习递归和迭代两种反转方式
4. 建议练习题：206, 92, 25, 141, 142

### 📋 题目清单 (22 题)

**🔥 高频必刷**

- `206` **反转链表** - *Easy* `频率:677`
- `25` **K 个一组翻转链表** - *Hard* `频率:437`
- `21` **合并两个有序链表** - *Easy* `频率:294`
- `92` **反转链表 II** - *Medium* `频率:235`
- `141` **环形链表** - *Easy* `频率:235`
- `143` **重排链表** - *Medium* `频率:204`
- `160` **相交链表** - *Easy* `频率:190`
- `142` **环形链表 II** - *Medium* `频率:163`
- `19` **删除链表的倒数第N个节点** - *Medium* `频率:158`
- `82` **删除排序链表中的重复元素 II** - *Medium* `频率:154`
- `2` **两数相加** - *Medium* `频率:123`
- `剑指 Offer 22` **链表中倒数第k个节点** - *Easy* `频率:102`

**📚 进阶练习**

- `234` 回文链表 - *Easy* `频率:76`
- `83` 删除排序链表中的重复元素 - *Easy* `频率:63`
- `24` 两两交换链表中的节点 - *Medium* `频率:61`
- `138` 复制带随机指针的链表 - *Medium* `频率:54`

**🎓 扩展了解**

- `61` 旋转链表 - *Medium* `频率:46`
- `445` 两数相加 II - *Medium* `频率:35`
- `328` 奇偶链表 - *Medium* `频率:33`
- `86` 分隔链表 - *Medium* `频率:19`
- `109` 有序链表转换二叉搜索树 - *Medium* `频率:13`

---

## 树·BFS层序

### 💡 核心思想
二叉树的层序遍历和相关变形

### 🔍 识别关键词
`层序` | `广度优先` | `层数` | `右视图` | `锯齿形`

### 📝 核心模板
```java
// 层序遍历
public List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> result = new ArrayList<>();
    if (root == null) return result;
    
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    
    while (!queue.isEmpty()) {
        int size = queue.size();
        List<Integer> level = new ArrayList<>();
        
        for (int i = 0; i < size; i++) {
            TreeNode node = queue.poll();
            level.add(node.val);
            
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        result.add(level);
    }
    return result;
}
```

### 🎯 训练建议
1. 理解BFS按层处理的关键思想
2. 掌握队列大小控制层级的技巧
3. 练习各种层序遍历的变形
4. 建议练习题：102, 103, 107, 199, 515

### 📋 题目清单 (8 题)

**🔥 高频必刷**

- `102` **二叉树的层序遍历** - *Medium* `频率:281`
- `103` **二叉树的锯齿形层次遍历** - *Medium* `频率:241`
- `199` **二叉树的右视图** - *Medium* `频率:142`

**📚 进阶练习**

- `662` 二叉树最大宽度 - *Medium* `频率:71`

**🎓 扩展了解**

- `958` 二叉树的完全性检验 - *Medium* `频率:46`
- `107` 二叉树的层次遍历 II - *Medium* `频率:13`
- `429` N叉树的层序遍历 - *Medium* `频率:11`
- `559` N叉树的最大深度 - *Easy* `频率:8`

---

## 树·DFS/递归DP

### 💡 核心思想
二叉树的深度优先遍历和递归动态规划

### 🔍 识别关键词
`深度优先` | `递归` | `路径` | `祖先` | `最大值`

### 📝 核心模板
```java
// DFS遍历
public void dfs(TreeNode root, List<Integer> result) {
    if (root == null) return;
    
    result.add(root.val);  // 前序
    dfs(root.left, result);
    // result.add(root.val);  // 中序
    dfs(root.right, result);
    // result.add(root.val);  // 后序
}

// 树形DP
public int maxPathSum(TreeNode root) {
    int[] maxSum = {Integer.MIN_VALUE};
    maxPathSumHelper(root, maxSum);
    return maxSum[0];
}

private int maxPathSumHelper(TreeNode node, int[] maxSum) {
    if (node == null) return 0;
    
    int leftMax = Math.max(0, maxPathSumHelper(node.left, maxSum));
    int rightMax = Math.max(0, maxPathSumHelper(node.right, maxSum));
    
    maxSum[0] = Math.max(maxSum[0], node.val + leftMax + rightMax);
    return node.val + Math.max(leftMax, rightMax);
}
```

### 🎯 训练建议
1. 熟练掌握前中后序遍历的递归写法
2. 理解树形DP的状态定义和转移
3. 练习路径问题和最值问题
4. 建议练习题：104, 124, 236, 543, 687

### 📋 题目清单 (29 题)

**🔥 高频必刷**

- `236` **二叉树的最近公共祖先** - *Medium* `频率:238`
- `124` **二叉树中的最大路径和** - *Hard* `频率:164`
- `94` **二叉树的中序遍历** - *Easy* `频率:138`
- `105` **从前序与中序遍历序列构造二叉树** - *Medium* `频率:101`

**📚 进阶练习**

- `129` 求根到叶子节点数字之和 - *Medium* `频率:94`
- `101` 对称二叉树 - *Easy* `频率:89`
- `104` 二叉树的最大深度 - *Easy* `频率:86`
- `144` 二叉树的前序遍历 - *Easy* `频率:84`
- `110` 平衡二叉树 - *Easy* `频率:82`
- `98` 验证二叉搜索树 - *Medium* `频率:78`
- `543` 二叉树的直径 - *Easy* `频率:78`
- `113` 路径总和 II - *Medium* `频率:73`
- `112` 路径总和 - *Easy* `频率:68`
- `226` 翻转二叉树 - *Easy* `频率:62`

---

## 动态规划

### 💡 核心思想
通过状态转移解决最优化问题

### 🔍 识别关键词
`最优` | `状态` | `转移` | `子问题` | `重叠`

### 📝 核心模板
```java
// 一维DP
public int rob(int[] nums) {
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];
    
    int prev2 = nums[0];
    int prev1 = Math.max(nums[0], nums[1]);
    
    for (int i = 2; i < nums.length; i++) {
        int curr = Math.max(prev1, prev2 + nums[i]);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}

// 二维DP
public int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];
    
    // 初始化
    for (int i = 0; i < m; i++) dp[i][0] = 1;
    for (int j = 0; j < n; j++) dp[0][j] = 1;
    
    // 状态转移
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = dp[i-1][j] + dp[i][j-1];
        }
    }
    return dp[m-1][n-1];
}
```

### 🎯 训练建议
1. 明确状态定义是DP的关键
2. 掌握初始化和边界处理
3. 练习空间优化技巧
4. 建议练习题：70, 198, 300, 72, 322

### 📋 题目清单 (36 题)

**🔥 高频必刷**

- `300` **最长上升子序列** - *Medium* `频率:221`
- `72` **编辑距离** - *Hard* `频率:172`
- `1143` **最长公共子序列** - *Medium* `频率:159`
- `70` **爬楼梯** - *Easy* `频率:119`
- `322` **零钱兑换** - *Medium* `频率:112`

**📚 进阶练习**

- `64` 最小路径和 - *Medium* `频率:80`
- `221` 最大正方形 - *Medium* `频率:79`
- `62` 不同路径 - *Medium* `频率:71`
- `152` 乘积最大子数组 - *Medium* `频率:70`
- `198` 打家劫舍 - *Medium* `频率:68`
- `718` 最长重复子数组 - *Medium* `频率:64`
- `139` 单词拆分 - *Medium* `频率:63`

---

## 回溯&组合

### 💡 核心思想
通过试探和回退生成所有可能的解

### 🔍 识别关键词
`全排列` | `组合` | `子集` | `N皇后` | `括号生成`

### 📝 核心模板
```java
// 回溯模板
public List<List<Integer>> permute(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    backtrack(result, new ArrayList<>(), nums, new boolean[nums.length]);
    return result;
}

private void backtrack(List<List<Integer>> result, List<Integer> current, 
                      int[] nums, boolean[] used) {
    if (current.size() == nums.length) {
        result.add(new ArrayList<>(current));
        return;
    }
    
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        
        current.add(nums[i]);
        used[i] = true;
        backtrack(result, current, nums, used);
        current.remove(current.size() - 1);
        used[i] = false;
    }
}
```

### 🎯 训练建议
1. 理解回溯的试探-回退机制
2. 掌握剪枝优化提高效率
3. 练习不同的约束条件
4. 建议练习题：46, 77, 78, 39, 51

### 📋 题目清单 (15 题)

**🔥 高频必刷**

- `46` **全排列** - *Medium* `频率:257`
- `93` **复原IP地址** - *Medium* `频率:163`
- `22` **括号生成** - *Medium* `频率:128`

**📚 进阶练习**

- `78` 子集 - *Medium* `频率:98`
- `39` 组合总和 - *Medium* `频率:82`
- `79` 单词搜索 - *Medium* `频率:51`
- `47` 全排列 II - *Medium* `频率:50`

**🎓 扩展了解**

- `40` 组合总和 II - *Medium* `频率:45`
- `51` N皇后 - *Hard* `频率:23`
- `37` 解数独 - *Hard* `频率:20`
- `301` 删除无效的括号 - *Hard* `频率:18`
- `17` 电话号码的字母组合 - *Medium* `频率:17`

---

## 贪心

### 💡 核心思想
通过局部最优选择达到全局最优

### 🔍 识别关键词
`局部最优` | `全局最优` | `跳跃` | `区间` | `股票`

### 📝 核心模板
```java
// 跳跃游戏
public int jump(int[] nums) {
    int jumps = 0, currentEnd = 0, farthest = 0;
    
    for (int i = 0; i < nums.length - 1; i++) {
        farthest = Math.max(farthest, i + nums[i]);
        
        if (i == currentEnd) {
            jumps++;
            currentEnd = farthest;
        }
    }
    return jumps;
}

// 区间调度
public int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);
    int count = 0, end = intervals[0][1];
    
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] >= end) {
            end = intervals[i][1];
        } else {
            count++;
        }
    }
    return count;
}
```

### 🎯 训练建议
1. 理解贪心选择的正确性证明
2. 掌握排序在贪心中的应用
3. 练习区间问题的贪心策略
4. 建议练习题：55, 45, 435, 452, 122

### 📋 题目清单 (13 题)

**🔥 高频必刷**

- `121` **买卖股票的最佳时机** - *Easy* `频率:248`

**📚 进阶练习**

- `122` 买卖股票的最佳时机 II - *Easy* `频率:78`
- `402` 移掉K位数字 - *Medium* `频率:50`

**🎓 扩展了解**

- `55` 跳跃游戏 - *Medium* `频率:46`
- `135` 分发糖果 - *Hard* `频率:36`
- `45` 跳跃游戏 II - *Medium* `频率:35`
- `678` 有效的括号字符串 - *Medium* `频率:29`
- `134` 加油站 - *Medium* `频率:25`

---

## 设计题

### 💡 核心思想
设计特定功能的数据结构

### 🔍 识别关键词
`LRU` | `LFU` | `Trie` | `栈` | `队列`

### 📝 核心模板
```java
// LRU缓存设计
class LRUCache {
    private final int capacity;
    private final Map<Integer, Node> cache;
    private final Node head, tail;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.head = new Node();
        this.tail = new Node();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) return -1;
        
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            moveToHead(node);
        } else {
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            addToHead(newNode);
            
            if (cache.size() > capacity) {
                Node tail = removeTail();
                cache.remove(tail.key);
            }
        }
    }
    
    // 双向链表操作方法...
}
```

### 🎯 训练建议
1. 理解题目要求的时间复杂度
2. 选择合适的底层数据结构
3. 注意线程安全和边界情况
4. 建议练习题：146, 208, 155, 232, 295

### 📋 题目清单 (14 题)

**🔥 高频必刷**

- `146` **LRU缓存机制** - *Medium* `频率:752`
- `232` **用栈实现队列** - *Easy* `频率:131`

**📚 进阶练习**

- `155` 最小栈 - *Easy* `频率:93`
- `297` 二叉树的序列化与反序列化 - *Hard* `频率:56`
- `460` LFU缓存 - *Hard* `频率:53`

**🎓 扩展了解**

- `225` 用队列实现栈 - *Easy* `频率:30`
- `380` 常数时间插入、删除和获取随机元素 - *Medium* `频率:17`
- `622` 设计循环队列 - *Medium* `频率:17`
- `706` 设计哈希映射 - *Easy* `频率:14`
- `1206` 设计跳表 - *Hard* `频率:9`

---

## 堆&优先队列

### 💡 核心思想
利用堆的性质解决TopK、中位数等问题

### 🔍 识别关键词
`第K个` | `最大值` | `最小值` | `中位数` | `TopK`

### 📝 核心模板
```java
// 堆的基本使用
public int findKthLargest(int[] nums, int k) {
    PriorityQueue<Integer> minHeap = new PriorityQueue<>();
    
    for (int num : nums) {
        minHeap.offer(num);
        if (minHeap.size() > k) {
            minHeap.poll();
        }
    }
    return minHeap.peek();
}

// 合并K个有序链表
public ListNode mergeKLists(ListNode[] lists) {
    PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> a.val - b.val);
    
    for (ListNode list : lists) {
        if (list != null) heap.offer(list);
    }
    
    ListNode dummy = new ListNode(0);
    ListNode curr = dummy;
    
    while (!heap.isEmpty()) {
        ListNode node = heap.poll();
        curr.next = node;
        curr = curr.next;
        
        if (node.next != null) {
            heap.offer(node.next);
        }
    }
    return dummy.next;
}
```

### 🎯 训练建议
1. 理解堆的基本性质和操作
2. 掌握PriorityQueue的使用
3. 练习TopK问题的解决思路
4. 建议练习题：215, 23, 295, 347, 692

### 📋 题目清单 (8 题)

**🔥 高频必刷**

- `239` **滑动窗口最大值** - *Hard* `频率:124`

**🎓 扩展了解**

- `347` 前 K 个高频元素 - *Medium* `频率:33`
- `295` 数据流的中位数 - *Hard* `频率:32`
- `264` 丑数 II - *Medium* `频率:23`
- `253` 会议室 II - *Medium* `频率:23`
- `378` 有序矩阵中第K小的元素 - *Medium* `频率:20`

---

## 图论&并查集

### 💡 核心思想
图的遍历、拓扑排序、连通性问题

### 🔍 识别关键词
`岛屿` | `连通` | `路径` | `拓扑` | `环检测`

### 📝 核心模板
```java
// DFS/BFS遍历图
public int numIslands(char[][] grid) {
    int count = 0;
    for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid[0].length; j++) {
            if (grid[i][j] == '1') {
                dfs(grid, i, j);
                count++;
            }
        }
    }
    return count;
}

private void dfs(char[][] grid, int i, int j) {
    if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') {
        return;
    }
    grid[i][j] = '0';
    dfs(grid, i + 1, j);
    dfs(grid, i - 1, j);
    dfs(grid, i, j + 1);
    dfs(grid, i, j - 1);
}

// 并查集
class UnionFind {
    private int[] parent, rank;
    
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }
    
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    public void union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX != rootY) {
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }
}
```

### 🎯 训练建议
1. 熟练掌握DFS和BFS的图遍历
2. 理解拓扑排序的应用场景
3. 掌握并查集的路径压缩优化
4. 建议练习题：200, 207, 547, 684, 695

### 📋 题目清单 (5 题)

**🔥 高频必刷**

- `200` **岛屿数量** - *Medium* `频率:267`

**📚 进阶练习**

- `695` 岛屿的最大面积 - *Medium* `频率:73`

**🎓 扩展了解**

- `130` 被围绕的区域 - *Medium* `频率:15`
- `547` 省份数量（原朋友圈） - *Medium* `频率:13`
- `133` 克隆图 - *Medium* `频率:10`

---

## 排序

### 💡 核心思想
各种排序算法的实现和应用

### 🔍 识别关键词
`排序` | `快排` | `归并` | `堆排` | `桶排`

### 📝 核心模板
```java
// 快速排序
public void quickSort(int[] nums, int left, int right) {
    if (left < right) {
        int pivot = partition(nums, left, right);
        quickSort(nums, left, pivot - 1);
        quickSort(nums, pivot + 1, right);
    }
}

private int partition(int[] nums, int left, int right) {
    int pivot = nums[right];
    int i = left - 1;
    
    for (int j = left; j < right; j++) {
        if (nums[j] <= pivot) {
            i++;
            swap(nums, i, j);
        }
    }
    swap(nums, i + 1, right);
    return i + 1;
}

// 归并排序
public void mergeSort(int[] nums, int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(nums, left, mid);
        mergeSort(nums, mid + 1, right);
        merge(nums, left, mid, right);
    }
}

private void merge(int[] nums, int left, int mid, int right) {
    int[] temp = new int[right - left + 1];
    int i = left, j = mid + 1, k = 0;
    
    while (i <= mid && j <= right) {
        temp[k++] = nums[i] <= nums[j] ? nums[i++] : nums[j++];
    }
    while (i <= mid) temp[k++] = nums[i++];
    while (j <= right) temp[k++] = nums[j++];
    
    for (int l = 0; l < temp.length; l++) {
        nums[left + l] = temp[l];
    }
}
```

### 🎯 训练建议
1. 手写各种排序算法
2. 理解排序算法的时间复杂度
3. 掌握排序的稳定性概念
4. 建议练习题：912, 148, 75, 215, 347

### 📋 题目清单 (13 题)

**🔥 高频必刷**

- `56` **合并区间** - *Medium* `频率:193`
- `148` **排序链表** - *Medium* `频率:129`

**📚 进阶练习**

- `179` 最大数 - *Medium* `频率:67`

**🎓 扩展了解**

- `977` 有序数组的平方 - *Easy* `频率:15`
- `692` 前K个高频单词 - *Medium* `频率:15`
- `354` 俄罗斯套娃信封问题 - *Hard* `频率:13`
- `905` 按奇偶排序数组 - *Easy* `频率:10`
- `164` 最大间距 - *Hard* `频率:10`

---

## 模拟

### 💡 核心思想
按照题目描述模拟执行过程

### 🔍 识别关键词
`螺旋` | `旋转` | `模拟` | `矩阵` | `遍历`

### 📝 核心模板
```java
// 螺旋矩阵
public List<Integer> spiralOrder(int[][] matrix) {
    List<Integer> result = new ArrayList<>();
    if (matrix.length == 0) return result;
    
    int top = 0, bottom = matrix.length - 1;
    int left = 0, right = matrix[0].length - 1;
    
    while (top <= bottom && left <= right) {
        // 右
        for (int j = left; j <= right; j++) {
            result.add(matrix[top][j]);
        }
        top++;
        
        // 下
        for (int i = top; i <= bottom; i++) {
            result.add(matrix[i][right]);
        }
        right--;
        
        // 左
        if (top <= bottom) {
            for (int j = right; j >= left; j--) {
                result.add(matrix[bottom][j]);
            }
            bottom--;
        }
        
        // 上
        if (left <= right) {
            for (int i = bottom; i >= top; i--) {
                result.add(matrix[i][left]);
            }
            left++;
        }
    }
    return result;
}
```

### 🎯 训练建议
1. 仔细理解题目的模拟过程
2. 注意边界条件的处理
3. 练习方向数组的使用
4. 建议练习题：54, 48, 59, 289, 73

### 📋 题目清单 (22 题)

**🔥 高频必刷**

- `54` **螺旋矩阵** - *Medium* `频率:223`
- `415` **字符串相加** - *Easy* `频率:199`
- `31` **下一个排列** - *Medium* `频率:126`

**📚 进阶练习**

- `48` 旋转图像 - *Medium* `频率:80`

---

## 数学&位运算

### 💡 核心思想
数学计算和位运算技巧

### 🔍 识别关键词
`位运算` | `数学` | `进制` | `GCD` | `质数`

### 📝 核心模板
```java
// 位运算基本操作
public int singleNumber(int[] nums) {
    int result = 0;
    for (int num : nums) {
        result ^= num;  // 异或运算
    }
    return result;
}

// 快速幂
public double myPow(double x, int n) {
    if (n == 0) return 1.0;
    
    long N = Math.abs((long) n);
    double result = 1.0;
    double currentPower = x;
    
    while (N > 0) {
        if (N % 2 == 1) {
            result *= currentPower;
        }
        currentPower *= currentPower;
        N /= 2;
    }
    
    return n < 0 ? 1.0 / result : result;
}

// GCD算法
public int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}
```

### 🎯 训练建议
1. 掌握常用位运算技巧
2. 理解快速幂的原理
3. 熟悉基本数学概念
4. 建议练习题：136, 190, 50, 172, 268

### 📋 题目清单 (17 题)

---

## 前缀和

### 📋 题目清单 (4 题)

**📚 进阶练习**

- `560` 和为K的子数组 - *Medium* `频率:66`

**🎓 扩展了解**

- `525` 连续数组 - *Medium* `频率:14`
- `974` 和可被 K 整除的子数组 - *Medium* `频率:11`
- `523` 连续的子数组和 - *Medium* `频率:11`

---

## Trie字典树

### 📋 题目清单 (1 题)

**🎓 扩展了解**

- `208` 实现 Trie (前缀树) - *Medium* `频率:33`

---

## 分治

### 📋 题目清单 (11 题)

**🔥 高频必刷**

- `215` **数组中的第K个最大元素** - *Medium* `频率:522`
- `53` **最大子数组和** - *Medium* `频率:341`
- `23` **合并K个排序链表** - *Hard* `频率:216`
- `4` **寻找两个正序数组的中位数** - *Hard* `频率:146`

**📚 进阶练习**

- `240` 搜索二维矩阵 II - *Medium* `频率:78`
- `169` 多数元素 - *Easy* `频率:65`

**🎓 扩展了解**

- `395` 至少有K个重复字符的最长子串 - *Medium* `频率:23`
- `426` 将二叉搜索树转化为排序的双向链表 - *Medium* `频率:18`
- `617` 合并二叉树 - *Easy* `频率:13`
- `912` 排序数组 - *Medium* `频率:11`
- `312` 戳气球 - *Hard* `频率:10`

---

## 线段树

### 📋 题目清单 (1 题)

**🎓 扩展了解**

- `315` 计算右侧小于当前元素的个数 - *Hard* `频率:12`

---

## 拓扑排序

### 📋 题目清单 (3 题)

**📚 进阶练习**

- `207` 课程表 - *Medium* `频率:53`

**🎓 扩展了解**

- `329` 矩阵中的最长递增路径 - *Hard* `频率:37`
- `210` 课程表 II - *Medium* `频率:26`

---

## 🚀 学习建议

### 📅 学习计划

**第1-2周**: 基础数据结构 (滑动窗口、双指针、哈希表、栈)
**第3-4周**: 搜索与排序 (二分搜索、排序算法、堆)
**第5-6周**: 字符串与数学 (字符串处理、数学位运算、模拟)
**第7-8周**: 树与递归 (BFS、DFS、树形DP)
**第9-10周**: 图与连通性 (图论、并查集)
**第11-12周**: 高级算法 (动态规划、回溯、贪心)
**第13-14周**: 系统设计 (LRU、Trie等设计题)
**第15-16周**: 链表专项训练

### 🎯 刷题策略

1. **优先高频题**: 先刷频率>100的题目，确保核心考点不丢分
2. **理解模板**: 每类题型都要熟练掌握通用模板，举一反三
3. **定期复习**: 建议每周回顾一次之前做过的题目
4. **限时练习**: 模拟面试环境，培养在压力下的代码能力
5. **总结归纳**: 每做完一类题型，总结该类型的解题思路

### 📝 面试技巧

- **理解题意**: 先确保完全理解题目要求，必要时举例说明
- **分析复杂度**: 在写代码前分析时间和空间复杂度
- **代码规范**: 注意变量命名、注释和边界处理
- **测试验证**: 用简单例子验证代码逻辑
- **优化讨论**: 主动讨论可能的优化方案

---

*📝 本指南基于CodeTop真实面试数据整理，持续更新中...*
*💡 建议结合LeetCode官方题解和讨论区深入学习*
