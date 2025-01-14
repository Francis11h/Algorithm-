33. Search in Rotated Sorted Array

Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4

Input: nums = [4,5,6,7,0,1,2], target = 3
Output: -1

O(logn)


nums = [4,5,6,7,0,1,2]


1.determine which part the Mid is in
	就是先判断A[mid]与A[left]的大小，要是A[mid]大，就保证在xxxxoooo的x里面，
    * 因为 left 到 mid 是单增
    * 而mid到right可能单减或先增后减
    所以 先比 nums[left] < nums[mid], 要是A[mid]大，就保证在xxxxoooo的x里面.


2.determine what range Target is in
	这时判断 target >= A[left] && target < A[mid],
	既保证了每次缩小范围时，右边界缩小，即每次把mid值赋给right；
    *否则，左边界缩小。




最基本的 二分 是比 nums[mid] 和 target 然后决定, 把 mid 赋值给 left / right 哪个


但是本题直接比 nums[mid] 和 target 并不能确定target是在哪一边


[2, 3, 4, 5, 0, 1]

一般情况下 num[mid] > target, 右边的不要了 right = mid。

比如 nums[mid] = 4 > target = 1 要是直接不要右边的 数组变为 ---> [2, 3, 4] 就丢了


因为我们不确定 target 是 在  xxxxxMidxxxooooo 中的哪里。 
如果 target 在 xxxx里 (xxTxxMidxxxooooo) 则需要保留左边的
如果 target 在 oooo里 (xxxxxMidxxxooToo) 则需要保留右边的

所以相当于多了情况---> 分4种了

如果mid在右边递增的序列里 : xxxxxooMidooooo

如果 target 在 oooo里 (xxxxxooMidooToo) 则需要保留右边的
如果 target 在 xxxx里 (xxTxxooMidooooo) 则需要保留左边的







nums[mid]直接等于target                        return mid

nums[mid]在左半边的递增区域 xxxxxx

        a. target 在 left 和 mid 之间          把 mid 赋值给 right  

        b. 不在之间                            把 mid 赋值给 left 

nums[mid]在右半边的递增区域 oooooo

        a. target 在 mid 和 right 之间         把 mid 赋值给 left 

        b. 不在之间                            把 mid 赋值给 right 




class Solution {
    public int search(int[] nums, int target) {
        if (nums == null || nums.length == 0)
            return -1;
        int left = 0, right = nums.length - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target)
                return mid;

            if (nums[left] < nums[mid]) {
                if (target >= nums[left] && target < nums[mid])         //这里的等号很关键。target >= nums[left]
                    right = mid;
                else 
                    left = mid;
            } else {
                if (target > nums[mid] && target <= nums[right])        //这里的等号很关键。 target <= nums[right] 
                    left = mid;
                else 
                    right = mid;
            }
        }
        if (nums[left] == target)
            return left;
        if (nums[right] == target)
            return right;
        return -1;
    }
}