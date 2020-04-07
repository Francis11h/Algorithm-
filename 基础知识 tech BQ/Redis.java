Redis



------
NoSQL
------ 
NoSQL = Not only SQL 不仅仅是SQL  只非关系型数据库
不依赖业务逻辑方式存储 仅仅以 <Key,Value> 模式存储 
不支持 事务ACID

适用场景: 
	1. 对数据高并发读写
	2. 海量数据的读写
	3. 对数据高可扩展性的

不适用:
	1. 需要事务支持
	2. 基于sql结构化查询的 处理复杂的关系的 需要即席查询的



-----------------
redis 基本数据类型
-----------------  

string, list, set, zset（有序set）, hash 五种





--------------------
为什么 Redis是单线程的
--------------------
https://cloud.tencent.com/developer/article/1120615

高性能的 服务器 一定由多线程来实现的吗?
多线程 一定比 单线程 效率高吗?

redis 核心就是 如果我的数据全都在"内存"里, 我单线程的去操作 就是效率最高的

多线程的本质就是 CPU 模拟出来多个线程的情况

这种模拟出来的情况就有一个代价,就是上下文的切换 Context-Switch

对于一个内存的系统来说 它没有上下文的切换就是效率最高的
redis 用 单个CPU 绑定一块内存的数据,然后针对这块内存的数据进行多次读写的时候,都是在一个CPU上完成的,所以它是单线程处理这个事。
这个方案就是最佳方案




因为一次CPU上下文的切换大概在 1500ns 左右。

从内存中读取 1MB 的连续数据,耗时大约为 250us,假设1MB的数据由多个线程读取了1000次,那么就有1000次时间上下文的切换，

那么就有1500ns * 1000 = 1500us <----> 我单线程的读完1MB数据才250us ,你光时间上下文的切换就用了1500us了,我还不算你每次读一点数据 的时间，



那什么时候用多线程的方案呢 : 下层的存储等慢速的情况。比如磁盘







--------------------------
为什么高并发使用redis不用mysql
--------------------------
因为 redis 快。。。 快 快
在高并发的业务场景下，数据库大多数情况都是用户并发访问最薄弱的环节。所以，就需要使用redis做一个缓冲操作，让请求先访问到redis，而不是直接访问MySQL等数据库。



----------------------------------------------------------------
数据库和缓存更新，就容易出现缓存(Redis)和数据库（MySQL）间的数据一致性问题。


1.方案：设置缓存过期时间

从理论上来说，给缓存设置过期时间，是保证最终一致性的解决方案。
所有的写操作以数据库为准，只要到达缓存过期时间，则后面的读请求自然会从数据库中读取新值然后回填缓存。

2.方案：采用延时双删 (业务双写) 策略

在写库前后都进行redis.del(key)操作，并且设定合理的超时时间。

public void write(String key,Object data) {
	redis.delKey(key);		//1. 先删缓存 
	db.updateData(data); 	//2. 再更新数据库
	Thread.sleep(500);		//3. 再休眠0.5秒
	redis.delKey(key);		//4. 再次删缓存
}


3.方案： 异步更新缓存
4.加锁

	

-------------------- 
简述Redis的数据淘汰机制
--------------------

volatile-lru 从已设置过期时间的数据集中挑选最近最少使用的数据淘汰
volatile-ttl 从已设置过期时间的数据集中挑选将要过期的数据淘汰
volatile-random从已设置过期时间的数据集中任意选择数据淘汰
allkeys-lru从所有数据集中挑选最近最少使用的数据淘汰
allkeys-random从所有数据集中任意选择数据进行淘汰
noeviction禁止驱逐数据


--------------------------
Redis怎样防止异常数据不丢失？
--------------------------
RDB 持久化
	将某个时间点的所有数据都存放到硬盘上。
	可以将快照复制到其它服务器从而创建具有相同数据的服务器副本。
	如果系统发生故障，将会丢失最后一次创建快照之后的数据。
	如果数据量很大，保存快照的时间会很长。
AOF 持久化
	将写命令添加到 AOF 文件（Append Only File）的末尾。
	使用 AOF 持久化需要设置同步选项，从而确保写命令同步到磁盘文件上的时机。这是因为对文件进行写入并不会马上将内容同步到磁盘上，而是先存储到缓冲区，然后由操作系统决定什么时候同步到磁盘。有以下同步选项：
	选项同步频率always每个写命令都同步everysec每秒同步一次no让操作系统来决定何时同步
	always 选项会严重减低服务器的性能；
	everysec 选项比较合适，可以保证系统崩溃时只会丢失一秒左右的数据，并且 Redis 每秒执行一次同步对服务器性能几乎没有任何影响；
	no 选项并不能给服务器性能带来多大的提升，而且也会增加系统崩溃时数据丢失的数量
	随着服务器写请求的增多，AOF 文件会越来越大。Redis 提供了一种将 AOF 重写的特性，能够去除 AOF 文件中的冗余写命令。




--------------------------
缓存穿透，缓存雪崩以及缓存击穿
--------------------------

缓存穿透：就是客户持续向服务器发起对不存在服务器中数据的请求。客户先在Redis中查询，查询不到后去数据库中查询。

缓存击穿：就是一个很热门的数据，突然失效，大量请求到服务器数据库中

缓存雪崩：就是大量数据同一时间失效。

	打个比方，你是个很有钱的人，开满了百度云，腾讯视频各种杂七杂八的会员，但是你就是没有netflix的会员，
	然后你把这些账号和密码发布到一个你自己做的网站上，然后你有一个朋友每过十秒钟就查询你的网站，
	发现你的网站没有Netflix的会员后打电话向你要。
	你就相当于是个数据库，网站就是Redis。这就是缓存穿透。
	大家都喜欢看腾讯视频上的《水果传》，但是你的会员突然到期了，大家在你的网站上看不到腾讯视频的账号，纷纷打电话向你询问，这就是缓存击穿
	你的各种会员突然同一时间都失效了，那这就是缓存雪崩了。



缓存穿透：
	1.接口层增加校验，对传参进行个校验，比如说我们的id是从1开始的，那么id<=0的直接拦截；
	2.缓存中取不到的数据，在数据库中也没有取到，这时可以将key-value对写为key-null，这样可以防止攻击用户反复用同一个id暴力攻击

缓存击穿：
	最好的办法就是设置热点数据永不过期，拿到刚才的比方里，那就是你买腾讯一个永久会员


缓存雪崩：
	1.缓存数据的过期时间设置随机，防止同一时间大量数据过期现象发生。
	2.如果缓存数据库是分布式部署，将热点数据均匀分布在不同搞得缓存数据库中。




















