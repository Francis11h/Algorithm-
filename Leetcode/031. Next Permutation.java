
031. Next Permutation
    同 第 556. Next Greater Element III


Implement next permutation, which rearranges numbers into the lexicographically next greater permutation of numbers.

If such arrangement is not possible, it must rearrange it as the lowest possible order (ie, sorted in ascending order).

The replacement must be in-place and use only constant extra memory.

Here are some examples. Inputs are in the left-hand column and its corresponding outputs are in the right-hand column.

1,2,3 → 1,3,2
3,2,1 → 1,2,3
1,1,5 → 1,5,1




I donot think any one can understand this solution without seeing an example, here is an example:

2,3,6,5,4,1

Solution:
    Step1, from right to left, find the first number which not increase in a ascending order. In this case which is 3.
    Step2, here we can have two situations:

        We cannot find the number, all the numbers increasing in a ascending order. This means this permutation is the last permutation, we need to rotate back to the first permutation. 
        So we reverse the whole array, for example, 6,5,4,3,2,1 we turn it to 1,2,3,4,5,6.

        We can find the number, then the next step, we will start from right most to leftward, try to find the first number which is larger than 3, in this case it is 4.
        Then we swap 3 and 4, the list turn to 2,4,6,5,3,1.
        Last, we reverse numbers on the right of 4, we finally get 2,4,1,3,5,6.

Time complexity is: O(3*n)=O(n).





class Solution {
    public void nextPermutation(int[] nums) {
        int n = nums.length - 1;
        
        int pivot = 0, index = -1;
        for (int i = n; i > 0; i--) {
            if (nums[i] > nums[i - 1]) {
                index = i - 1;
                pivot = nums[i - 1];
                break;
            }
        }
        
        if (index == -1) {
            reverse(nums, 0, n);
            return;
        }
        
        for (int i = n; i > index; i--) {
            if (nums[i] > pivot) {
                swap(nums, index, i);
                reverse(nums, index + 1, n);
                break;
            }
        }
    }
    private void reverse (int[] nums, int s, int e) {
      while (s < e) {
        swap(nums, s, e);
        s++;
        e--;
      }
    }
    
    private void swap (int[] nums, int s, int e) {
        int t = nums[s];
        nums[s] = nums[e];
        nums[e] = t;     
    }
}




//2,3,6,5,4,1

  // pivot











556. Next Greater Element III
class Solution {
    public int nextGreaterElement(int n) {
        // change int to string then change it to char array
        char[] nums = String.valueOf(n).toCharArray();
        // step 1 find the first not ascending number from right -> left
        int index = -1;
        for (int i = nums.length - 1; i > 0; i--) {
            if (nums[i] > nums[i - 1]) {
                index = i - 1;
                break;
            }
        }
        // the given is the largest permutation 
        if (index == -1) return -1;
        // step 2 swap this first not ascending number with the number just larger than it from right to it
        for (int i = nums.length - 1; i > index; i--) {
            if (nums[i] > nums[index]) {
                swap(nums, i, index);
                break;
            }
        }
        // step 3 reverse the sting from index -> right
        reverse(nums, index + 1, nums.length - 1);    
        
        //not overflow
        long ans = Long.parseLong(String.valueOf(nums));
        
        return ans > Integer.MAX_VALUE ? -1 : (int) ans;
    }
    
    private void reverse(char[] nums, int i, int j) {
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }
    
    private void swap(char[] nums, int i, int j) {
        char temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}

