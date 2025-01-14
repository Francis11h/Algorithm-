146. LRU Cache

Least Recently Used 最近最少使用(把这个pop出)

we need to implement the structure in O(1). do both operations in O(1) time complexity


It should support the following operations: get and put.

get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.

获取数据get(key) : 如果缓存中存在key，则获取其数据值（通常是正数），否则返回-1。

写入数据set(key, value) : 如果key还没有在缓存中，则写入其数据值。
当缓存达到上限，它应该在写入新数据之前删除 最近最少 使用的数据 来腾出空闲位置。


LRUCache cache = new LRUCache( 2 /* capacity */ );

cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // returns 1
cache.put(3, 3);    // evicts key 2
cache.get(2);       // returns -1 (not found)
cache.put(4, 4);    // evicts key 1
cache.get(1);       // returns -1 (not found)
cache.get(3);       // returns 3
cache.get(4);       // returns 4









Map中存的是 <Integer 值, ListNode 链表节点>

每次删都从tail删, 每次使用(添加) 都把对应的Node放到head。
=====
capacity = 2

用得多               用的少
head                tail
--------------------->
cache.put(2, 2);
[2, 2]-->[1, 1]                             

cache.get(1);       // returns 1
[1, 1]-->[2, 2]                            

cache.put(3, 3);    // evicts key 2
[1, 1]
[3, 3]-->[1, 1] 

cache.get(2);---> returns -1 (not found)

cache.put(4, 4);    // evicts key 1
[3, 3]
[4, 4]--->[3, 3]

cache.get(1);---> returns -1 (not found)

cache.get(3);       // returns 3
[3, 3]--->[4, 4]

cache.get(4);       // returns 4
[4, 4]--->[3, 3]


        [key, value]----->

[]---->[]---->[]

方法一:
双向链表 + HashMap

class ListNode {
    int key;
    int val;
    ListNode prev;
    ListNode next;
    ListNode (int key, int val) {
        this.key = key;
        this.val = val;
        this.prev = this.next = null;
    }
}

class LRUCache {
    Map<Integer, ListNode> map;
    int capacity;
    ListNode head;
    ListNode tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        map = new HashMap<>();
        head = new ListNode(0, 0);
        tail = head;
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        ListNode current = map.get(key);

        //先切割原有的关系
        if (current.next == null) {                 //如果 current 是尾部节点 tail的赋值就要变了
            current.prev.next = null;
            tail = tail.prev;
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }
        //再移动到头部
        moveToFront(current);
        return map.get(key).val;
    }

    public void put(int key, int value) {
        if (get(key) != -1) {                   //这里是 get 不是 map.get。 这里的get是上面写的方法
            map.get(key).val = value;
            return;
        }
        if (map.size() == capacity) {
            //移除尾部元素对应的
            map.remove(tail.key);
            tail.prev.next = null;               //这里决定了我们要用双链表, 要是用singly 这里要遍历到tail 会是O(n) 因为没有 prev这个属性！！
            tail = tail.prev;
        }

        ListNode newNode = new ListNode(key, value);
        map.put(key, newNode);
        moveToFront(newNode);
    }

    //把新插入的节点 移动到头节点
    public void moveToFront(ListNode node) {
        //如果 head 没有指向 代表 该链表中没有节点 tail也就没有指向 新加入节点 tail也需要赋值
        if (head.next == null) {
            head.next = node;
            node.prev = head;
            tail = node;
        } else {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
    }
}



head          node
             [    ]

head               tail         node
[   ]---->[  ]---->[ ]         [    ]
     <----    <----





One advantage of double linked list is that the node can remove itself without other reference.
(enables us to quickly move node)
In addition, it takes constant time to add and remove from the head or tail.


S : O(capacity), hashmap and linkedlist




更加简洁的代码 把tail也设为一个dummy 这样子get()中 切割原有的关系时 就没有 cur.next = null 的特殊情况了

class Node {
    int key;
    int val;
    Node prev;
    Node next;
    public Node(int key, int val) {
        this.key = key;
        this.val = val;
        prev = null;
        next = null;
    }
}

class LRUCache {
    Map<Integer, Node> map;
    int capacity;
    Node head;
    Node tail;
    
    public LRUCache(int capacity) {
        map = new HashMap<>();
        this.capacity = capacity;
        head = new Node(0, 0);
        tail = new Node(0, 0);
    }
    
    private void moveToFront(Node node) {
        //whole list is empty initialize head & tail
        if (head.next == null) {
            head.next = node;
            node.prev = head;
            tail.prev = node;
            node.next = tail;
        } else {
            node.prev = head;
            node.next = head.next;
            node.next.prev = node;
            head.next = node;
        }
        
    }
    
    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }
        //found remove its relationship within list
        Node cur = map.get(key);
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        
        moveToFront(cur);    
        return cur.val;
    }
    
    public void put(int key, int value) {
        if (get(key) != -1) {
            map.get(key).val = value;
            return;
        }
        
        if (map.size() == capacity) {
            map.remove(tail.prev.key);
            tail.prev.prev.next = tail;
            tail.prev = tail.prev.prev;
        }
        
        Node node = new Node(key, value);
        map.put(key, node);
        moveToFront(node);
    }
}












我就想用单链表试试


class ListNode {
    int key;
    int val;
    ListNode next;
    ListNode (int key, int val) {
        this.key = key;
        this.val = val;
        this.next = null;
    }
}

class LRUCache {
    //需要使用一个hash table来存储目前节点指向的前一个节点(模拟双链表的prev)是什么
    Map<Integer, ListNode> keyToPrev;
    int capacity, size;
    ListNode dummy;
    ListNode tail;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        keyToPrev = new HashMap<>();
        dummy = new ListNode(0, 0);
        tail = dummy;
    }

    public int get(int key) {
        if (!keyToPrev.containsKey(key)) {
            return -1;
        }
        moveToTail(key);
        return tail.val;
    }

    public void put(int key, int value) {
        if (get(key) != -1) {                   //这里是 get 不是 map.get。 这里的get是上面写的方法
            ListNode prev = keyToPrev.get(key);
            prev.next.val = value;
            return;
        }
        if (size < capacity) {
            size++;
            ListNode cur = new ListNode(key, value);
            tail.next = cur;
            keyToPrev.put(key, tail);
            
            tail = cur;
            return;
        }
        
        // replace the first node with new key, value
        ListNode first = dummy.next;
        keyToPrev.remove(first.key);
        
        first.key = key;
        first.val = value;
        keyToPrev.put(key, dummy);
        
        moveToTail(key);
    }

    public void moveToTail(int key) {
        ListNode prev = keyToPrev.get(key);
        ListNode cur = prev.next;

        if (tail == cur) {
            return;
        }

        prev.next = prev.next.next;
        tail.next = cur;
        if (prev.next != null) {
            keyToPrev.put(prev.next.key, prev);
        }
        keyToPrev.put(cur.key, tail);
        tail = cur;
    }
}


                      tail  
prev ----> cur -----> qwer












2019.11.27

试想 Node 不存 key 只存 value, 那么当后面 map.remove(special key) 的时候 我们无法从 特定node中知道该node的key
所以 node必须存key 就是为了通过特定的node来寻找到特定的key 以便在map中删除


错误代码 因为node中没有存key 就走不通了 
class Node {
    int val;
    Node next, prev;
    Node (int val) {
        this.val = val;
    }
}

class LRUCache {
    Map<Integer, Node> map;
    int capacity;
    Node head;          // head: points to the most recently used element
    Node tail;          // tail: points to the least recently used element
    public LRUCache(int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
        this.head = new Node(0);
        this.tail = new Node(0);
    }
    
    // 1. get the value of the key 2. return -1 if the key not exists 3.move the node to the head
    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        // delete the node from the list
        node.prev.next = node.next;
        node.next.prev = node.prev;
        //move the node to the head
        moveToHead(node);
        return node.val;
    }
    public void put(int key, int value) {
        if (get(key) != -1) {
            map.get(key).val = value;
            return;
        }
        if (map.size() == capacity) {
            tail.prev = tail.prev.prev;
            tail.prev.next = tail;
            map.remove()            //
        }
        Node node = new Node(value);
        map.put(key, node);
        moveToHead(node);
    }
    
    private void moveToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next = node;
        node.next.prev = node;
    }
}



修改了 Node 其中加上对应value的key
但还是 错误的代码 因为  初始化 没有考虑 就是 最开始 head tail 的指针 都没有初始化
class Node {
    int key,val;
    Node next, prev;
    Node (int key, int val) {
        this.key = key;
        this.val = val;
    }
}

class LRUCache {

    Map<Integer, Node> map;
    int capacity;
    Node head;          // head: points to the most recently used element
    Node tail;          // tail: points to the least recently used element
    public LRUCache(int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        //////////////////这里少了初始化//////。。。。。。。。。。
    }
    
    // 1. get the value of the key 2. return -1 if the key not exists 3.move the node to the head
    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        // delete the node from the list
        node.prev.next = node.next;
        node.next.prev = node.prev;
        //move the node to the head
        moveToHead(node);
        return node.val;
    }
    
    public void put(int key, int value) {
        if (get(key) != -1) {
            map.get(key).val = value;
            return;
        }
        if (map.size() == capacity) {
            map.remove(tail.prev.key);       // 通过特定value 瞬间找key 因为本身node就存了 key
            tail.prev = tail.prev.prev;
            tail.prev.next = tail;
                  
        }
        Node node = new Node(key, value);
        map.put(key, node);
        moveToHead(node);
    }
    
    private void moveToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next = node;
        node.next.prev = node;
    }
}





正确的代码
class Node {
    int key,val;
    Node next, prev;
    Node (int key, int val) {
        this.key = key;
        this.val = val;
    }
}
class LRUCache {
    // cause we need to get a value in O(1) and add a key in O(1), we need to maintain a HashMap
    // but just use a int to represent the value, we cant do the LRU operations, so we need a new class Node
    // a Node must have 1. value 2. next ptr 3.prev ptr 
    // all of the nodes will form a linked list, which has the head and tail, where we can do offer and pop 
    Map<Integer, Node> map;
    int capacity;
    Node head;          // head: points to the most recently used element
    Node tail;          // tail: points to the least recently used element
    public LRUCache(int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }
    
    // 1. get the value of the key 2. return -1 if the key not exists 3.move the node to the head
    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        // delete the node from the list
        node.prev.next = node.next;
        node.next.prev = node.prev;
        //move the node to the head
        moveToHead(node);
        return node.val;
    }
    
    // 1. search whether there existed this key  by using get()
    // 2. if exists, update the val + move the node to the head 
    // 3. if not exists, insert a new node at tail + move the new node to the head points to.
    // 3.1 if the capacity is full, evicts the node which the tail points to.
    public void put(int key, int value) {
        if (get(key) != -1) {
            map.get(key).val = value;
            return;
        }
        if (map.size() == capacity) {
            map.remove(tail.prev.key);       // 通过特定value 瞬间找key 因为本身node就存了 key
            tail.prev = tail.prev.prev;
            tail.prev.next = tail;
                  
        }
        Node node = new Node(key, value);
        map.put(key, node);
        moveToHead(node);
    }
    
    private void moveToHead(Node node) {
        // if (head.next != null) {
            node.prev = head;
            node.next = head.next;
            head.next = node;
            node.next.prev = node;
         // } else {
         //    head.next = node;
         //   node.prev = head;
         //  tail.prev = node;
         //  node.next = tail;
         // } 
    }
}







2019.11.29
更精简的代码
确的代码
class Node {
    int key,val;
    Node next, prev;
    Node (int key, int val) {
        this.key = key;
        this.val = val;
    }
}

class LRUCache {
    // cause we need to get a value in O(1) and add a key in O(1), we need to maintain a HashMap
    // but just use a int to represent the value, we cant do the LRU operations, so we need a new class Node
    // a Node must have 1. value 2. key(for us to delete on map) 3. next ptr 4.prev ptr 
    // all of the nodes will form a linked list, which has the head and tail, where we can do offer and pop 
    Map<Integer, Node> map;
    int capacity;
    Node head;          // head: points to the most recently used element
    Node tail;          // tail: points to the least recently used element
    public LRUCache(int capacity) {
        this.map = new HashMap<>();
        this.capacity = capacity;
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }
    
    // 1. get the value of the key 2. return -1 if the key not exists 3.move the node to the head
    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        Node node = map.get(key);
        
        remove(node);
        moveToHead(node);
        return node.val;
    }
    
    // 1. search whether there existed this key  by using get()
    // 2. if exists, update the val + move the node to the head 
    // 3. if not exists, insert a new node at tail + move the new node to the head points to.
    // 3.1 if the capacity is full, evicts the node which the tail points to.
    public void put(int key, int value) {
        if (get(key) != -1) {
            map.get(key).val = value;
            return;
        }
        if (map.size() == capacity) {
            map.remove(tail.prev.key);       // 通过特定value 瞬间找key 因为本身node就存了 key
            remove(tail.prev);
        }

        Node node = new Node(key, value);
        map.put(key, node);
        moveToHead(node);
    }

    // delete the node from the list
    private void remove(Node node) {
        if (node == null) return;
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }
    //move the node to the head
    private void moveToHead(Node node) {
        if (node == null) return;
        node.prev = head;
        node.next = head.next;
        head.next = node;
        node.next.prev = node;
    }
}







There is a structure called ordered dictionary, it combines behind both hashmap and linked list. 
In Python this structure is called OrderedDict and in Java "LinkedHashMap".

class LRUCache extends LinkedHashMap<Integer, Integer>{
    private int capacity;
    
    public LRUCache(int capacity) {
        super(capacity, 0.75F, true);
        this.capacity = capacity;
    }

    public int get(int key) {
        return super.getOrDefault(key, -1);
    }

    public void put(int key, int value) {
        super.put(key, value);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
        return size() > capacity; 
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */





补充 关于 Cache 的面试点

什么是缓存 Cache 及其作用
缓存可以提高页面加载的速度, 并可以减少 服务器和数据库 的负载

数据库缓存(1.从网络获取内容较慢,我们可以用数据库进行缓存)
数据库分片均匀分布的读取是最好的。但是热门数据会让读取分布不均匀，这样就会造成瓶颈，如果在数据库前加个缓存，就会抹平不均匀的负载和突发流量对数据库的影响。

客户端缓存
Web 服务器缓存
数据库缓存
应用缓存
	基于内存的缓存比如 Memcached 和 Redis 是应用程序和数据存储之间的一种键值存储
	由于数据保存在 RAM 中，它比存储在磁盘上的典型数据库要快多了。RAM 比磁盘限制更多，所以例如 least recently used (LRU) 的缓存无效算法可以将「热门数据」放在 RAM 中，而对一些比较「冷门」的数据不做处理。


	有多个缓存级别，分为两大类：数据库查询和对象：
		行级别
		查询级别
		完整的可序列化对象
		完全渲染的 HTML
何时更新缓存


缓存的缺点：

请求的数据如果不在缓存中就需要经过三个/多个步骤来获取数据，这会导致明显的延迟。
如果数据库中的数据更新了会导致缓存中的数据过时。这个问题需要通过设置 "TTL" 强制更新缓存或者直写模式来缓解这种情况








