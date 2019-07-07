438. Find All Anagrams in a String

Given a string s and a non-empty string p, find all the start indices of p‘s anagrams in s.

Input:
s: "cbaebabacd" p: "abc"

Output:
[0, 6]

Explanation:
The substring with start index = 0 is "cba", which is an anagram of "abc".
The substring with start index = 6 is "bac", which is an anagram of "abc".


Input:
s: "abab" p: "ab"

Output:
[0, 1, 2]

Explanation:
The substring with start index = 0 is "ab", which is an anagram of "ab".
The substring with start index = 1 is "ba", which is an anagram of "ab".
The substring with start index = 2 is "ab", which is an anagram of "ab".


Related Topics is "Hash Table"


 "cbaebabacd"
  i 

sliding window

先建个 Map 给 p的 哪个字符 出现多少次 算明白了

然后就走s, 前面先减, 直到 map中每个字符计数都是0 就match

class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (s == null || s.length() == 0) return ans;
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < p.length(); i++) {
            char ch = p.charAt(i);
            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }

        int match = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (map.containsKey(ch)) {
                map.put(ch, map.get(ch) - 1);
                if (map.get(ch) == 0) {
                    match++;
                }
            }

            if (i >= p.length()) {
                char charToRemove = s.charAt(i - p.length());
                if (map.containsKey(charToRemove)) {
                    map.put(charToRemove, map.get(charToRemove) + 1);
                    if (map.get(charToRemove) == 1) {
                        match--;
                    }
                }
            }

            if (match == map.size()) {
                ans.add(i - p.length() + 1);
            }
        }
        return ans;
    }
}










愚钝的做法 dfs给p的全排列找全 然后每次一个个比

class Solution {
    Set<String> set = new HashSet<>();

    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new ArrayList<>();
        if (s == null || s.length() == 0) return ans;

        List<String> anagrams = permutation(p); 
        for (String anagram : anagrams) {
            set.add(anagram);
        }

        for (int i = 0; i < s.length() - p.length() + 1; i++) {
            if (set.contains(s.substring(i, i + p.length()))) {
                ans.add(i);
            }
        }

        return ans;
    }

    public List<String> permutation(String pp) {
        List<String> ans = new ArrayList<>();
        char[] p = pp.toCharArray();
        Arrays.sort(p);                                  
        dfs(0, p, new boolean[p.length], new StringBuilder(), ans);
        return ans;
    }

    private void dfs(int level, char[] p, boolean[] visited, StringBuilder cur, List<String> ans) {
        if (level == p.length) {
            ans.add(cur.toString());
            return;
        }
        for (int i = 0; i < p.length; i++) {
            if (!visited[i]) {
                if (i > 0 && p[i - 1] == p[i] && !visited[i - 1]) continue;
                visited[i] = true;
                cur.append(p[i]);
                dfs(level + 1, p, visited, cur, ans);
                cur.deleteCharAt(cur.length() - 1);
                visited[i] = false;
            }
        }
    }
}









//如何求给定一个String 的全排列




class Solution {
    public List<String> permuteUnique(String pp) {
        List<String> ans = new ArrayList<>();
        char[] nums = pp.toCharArray();
        Arrays.sort(nums);                                  
        dfs(0, nums, new boolean[nums.length], new StringBuilder, ans);
        return ans;
    }
    private void dfs(int level, char[] nums, boolean[] visited, StringBuilder cur, List<String> ans) {
        if (level == nums.length) {
            ans.add(cur.toString());
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!visited[i]) {
                //关键
                if (i > 0 && nums[i - 1] == nums[i] && !visited[i - 1]) continue;
                visited[i] = true;
                cur.append(nums[i]);
                dfs(level + 1, nums, visited, cur, ans);
                cur.deleteCharAt(cur.length() - 1);
                visited[i] = false;
            }
        }
    }
}







