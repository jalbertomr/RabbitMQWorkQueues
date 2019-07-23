package com.bext;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {
	public final static String TASK_QUEUE_NAME = "task-queue";
	
	public static void main(String[] args) throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
		System.out.println("Esperando por mensajes... CTRL+C para salir.");
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), "UTF-8");
			System.out.println("[x] recibido: '" + message +"'");
			try {
			  doWork(message);	
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println("[x] Hecho.");
			}
		};
		boolean autoAck = true; // acknowledgment 
		channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, consumerTag->{});
	}
	
	private static void doWork(String task) throws InterruptedException {
		for (char ch: task.toCharArray()) {
			if ( ch == '.') Thread.sleep(1000);
		}
	}
}
