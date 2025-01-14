210. Course Schedule II


class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List[] graph = new ArrayList[numCourses];
        int[] indegree = new int[numCourses];
        int[] ans = new int[numCourses];
        
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] pre : prerequisites) {
            int from = pre[1], to = pre[0];
            // add edge for our directed graph
            // there are no duplicate edges in the input prerequisites.
            graph[from].add(to);
            indegree[to]++;
        }
        // do bfs
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }
        
        int index = 0;
        while (!queue.isEmpty()) {
            int from = queue.poll();
            // 这里 很 tricky 每次 修改ans的一个下标 
            ans[index++] = from;
            
            for (int k = 0; k < graph[from].size(); k++) {
                int to = (int) graph[from].get(k);
                indegree[to]--;
                if (indegree[to] == 0) {
                    queue.offer(to);
                }
            }
        }
        return index == numCourses ? ans : new int[0];
    } 
}







//临接表 表示出来图 5ms
class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        ArrayList[] graph = new ArrayList[numCourses];
        int[] indegree = new int[numCourses];
        int[] ans = new int[numCourses];

        //initialize the graph
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<Integer>();
        }

        // find the # of each course's pre and build the graph 
        for (int i = 0; i < prerequisites.length; i++) {
            indegree[prerequisites[i][0]]++;
            graph[prerequisites[i][1]].add(prerequisites[i][0]);
        }
        Queue<Integer> queue = new LinkedList<>();
        //find the course without pre
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        int index = 0;
        //remove these course and find the new course with 0 indegree
        while (!queue.isEmpty()) {
            int tmp = queue.poll();
            ans[index++] = tmp;
            int n = graph[tmp].size();
            for (int k = 0; k < n; k++) {
                int i = (int) graph[tmp].get(k);        //object -> int
                indegree[i]--;
                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }
        }
        return (index == numCourses) ? ans : new int[0];    //new empty array
    }
}
Time 
O(V+E)
Space
O(V+E)





这么建图 就不用 再转 object to int

List<Integer>[] graph 即 这里直接规定了 List里面存的是 Integer



class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<Integer>[] graph = new ArrayList[numCourses];
        int[] indegree = new int[numCourses];
        int[] ans = new int[numCourses];
        
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int[] pre : prerequisites) {
            int from = pre[1], to = pre[0];
            // add edge for our directed graph
            // there are no duplicate edges in the input prerequisites.
            graph[from].add(to);
            indegree[to]++;
        }
        // do bfs
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }
        
        int index = 0;
        while (!queue.isEmpty()) {
            int from = queue.poll();
            ans[index++] = from;
            
            for (int k = 0; k < graph[from].size(); k++) {
                int to = graph[from].get(k);
                indegree[to]--;
                if (indegree[to] == 0) {
                    queue.offer(to);
                }
            }
        }
        return index == numCourses ? ans : new int[0];
    } 
}












//不把图表示出来   34ms 反而慢
// 慢的原因是 每次while 要遍历一遍prerequisites，这个可能很长，冗余
class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] indegree = new int[numCourses];
        int[] ans = new int[numCourses];

        // find the # of each course's pre and build the graph 
        for (int i = 0; i < prerequisites.length; i++) {
            indegree[prerequisites[i][0]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        //find the course without pre
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        int index = 0;
        //remove these course and find the new course with 0 indegree
        while (!queue.isEmpty()) {
            int tmp = queue.poll();
            ans[index++] = tmp;

            for (int i = 0; i < prerequisites.length; i++) {
                if (prerequisites[i][1] == tmp) {   //有 从tmp指向i的边 => tmp是i的先修课
                    indegree[prerequisites[i][0]]--;
                    if (indegree[prerequisites[i][0]] == 0) {
                        queue.offer(prerequisites[i][0]);
                    }
                }
            }
        }
        return (index == numCourses) ? ans : new int[0];    //new empty array
    }
}

Time complexity: O(V^2)
outer for loop for each course: O(V)
inner for loop for all the neighbors by visiting the adjacency list O(V): at most connect to V - 1 courses
O(V*(V - 1)) = O(V^2)

Space complexity: O(V)
