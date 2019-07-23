package com.bext;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class NewTask {
	private final static String TASK_QUEUE_NAME = "task-queue";
	
	public static void main(String[] args) throws Exception{
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try(Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()) {
			boolean durable = true;
			channel.queueDeclare(TASK_QUEUE_NAME, durable, false,false, null);
			String message = String.join(" ", args[0]);
			channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
			System.out.println(" [x] enviado '" + message + "'");
		}
	}
}
