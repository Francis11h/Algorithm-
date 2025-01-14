13. Roman to Integer


Input: "IX"
Output: 9


Input: "LVIII"
Output: 58
Explanation: L = 50, V= 5, III = 3.

Input: "MCMXCIV"
Output: 1994
Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.


其实转换就一条 挨个遍历字符串 当前位比后面小就减去这位 否则就加。


class Solution {
    public int romanToInt(String s) {
        if (s == null || s.length() == 0) return 0;
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        
        int ans = 0;
        int now = map.get(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            int next = map.get(s.charAt(i));
            if (now < next) {
                ans -= now;
            } else {
                ans += now;
            }
            now = next;
        }
        ans += now;
        return ans;
    }
}