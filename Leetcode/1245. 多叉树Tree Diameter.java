1245. Tree Diameter

Given an undirected tree, return its diameter: the number of edges in a longest path in that tree.

The tree is given as an array of edges where edges[i] = [u, v] is a bidirectional edge between nodes u and v.  Each node has labels in the set {0, 1, ..., edges.length}.



Input: edges = [[0,1],[1,2],[2,3],[1,4],[4,5]]
Output: 4
Explanation: 
A longest path of the tree is the path 3 - 2 - 1 - 4 - 5.





首先 怎么 用 edge list 表示 tree ---> 建树 ---> 因为 可以确定 构成 所以 edgelist.length = vertex - 1

用 临界表 就行



class Solution {

    int diameter = 0;

    public int treeDiameter(int[][] edges) {
        if (edges == null) return 0;
        int n = edges.length;
        List<Integer>[] tree = new LinkedList[n + 1]; // vertex - 1 = edge --> cause it is a tree
        for (int i = 0; i <= n; i++) {
            tree[i] = new LinkedList<>();
        }
        for (int[] edge : edges) {
            int a = edge[0], b = edge[1];
            tree[a].add(b);
            tree[b].add(a);
        }
        depth(0, -1, tree);
        return diameter;
    }
    
    // 二叉树的时候 是 dia = left + right, 现在 dia = maxDepth1st + maxDepth2nd 即找第一第二长的
    // 需要 return int 因为 我们需要知道子节点的 深度 才能算 父节点
    // 同时还是 return depth 之前 更新 diameter
    // 同时 还得保证 不能重复计算 root 的 adj list 可能包含 其 父亲节点， 即已经走过的节点，应该去掉
    // 怎么去 用set 表示 已走过 可以， 但是 更牛逼的办法是 传 parent 进去 即可 因为是 tree 只可能有一个parent
    private int depth(int root, int parent, List<Integer>[] tree) {
        int D1st = 0, D2nd = 0;
        for (int child : tree[root]) {
            if (child != parent) {
                int depthOfChild = depth(child, root, tree);
                // 新来的如果是 前二深 就记录
                if (depthOfChild > D1st) {
                    D2nd = D1st;
                    D1st = depthOfChild;
                } else if (depthOfChild > D2nd) {
                    D2nd = depthOfChild;
                }
            }
        }
        int longestPathThroughRoot = D1st + D2nd;
        diameter = Math.max(diameter, longestPathThroughRoot);
        // return the root's depth = deepest depth of child + 1
        return D1st + 1;
    }
}


JAVA 中变量不能以数字开头   一开始写的是  int 1st = 0, 2nd = 0; 这是很荒唐的。。
                         应改为      int D1st = 0, D2nd = 0;

因为这是为了制作编译器的方便，如果你学过编译原理的话，你会知道如果开始第一个字符可以是数字，那会是相当痛苦的，这会增加此法分析的难度，所以java语言变量拒绝使用数字开头这种方式。





不是 那么优秀的解法 用了 set 去重 

class Solution {

    int diameter = 0;

    public int treeDiameter(int[][] edges) {
        if (edges == null) return 0;
        int n = edges.length;
        List<Integer>[] tree = new LinkedList[n + 1]; // vertex - 1 = edge --> cause it is a tree
        for (int i = 0; i <= n; i++) {
            tree[i] = new LinkedList<>();
        }
        for (int[] edge : edges) {
            int a = edge[0], b = edge[1];
            tree[a].add(b);
            tree[b].add(a);
        }
        Set<Integer> visited = new HashSet<>();
        depth(0, visited, tree);
        return diameter;
    }
    
    private int depth(int root, Set<Integer> visited, List<Integer>[] tree) {
        visited.add(root);
        int D1st = 0, D2nd = 0;
        for (int child : tree[root]) {
            if (!visited.contains(child)) {
                int depthOfChild = depth(child, visited, tree);

                if (depthOfChild > D1st) {
                    D2nd = D1st;
                    D1st = depthOfChild;
                } else if (depthOfChild > D2nd) {
                    D2nd = depthOfChild;
                }
            }
        }
        int longestPathThroughRoot = D1st + D2nd;
        diameter = Math.max(diameter, longestPathThroughRoot);
        // return the root's depth = deepest depth of child + 1
        return D1st + 1;
    }
}

