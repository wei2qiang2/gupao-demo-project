# ActiveMQ

流量削峰：秒杀通过异步实现排队的形式

ConnectionFactory 通过工厂创建连接
Connection 	连接
Session	
Producer	Destination 	发送
Consumer	Destination		接受


JMS + ACtiveMQ 实例

JMS的基本基本功能

	point-point(消息传递域:P2P)：每个消息只能有一个消费者
	pub/sub(发布订阅，类似于QQ群)，每个消息有多个消费者，生产者和消费者有时间的相关性。