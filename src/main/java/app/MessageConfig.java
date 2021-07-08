package app;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
	 static final String topicExchangeName = "spring-boot-exchange";

	    static final String queueName = "spring-boot";

	    @Bean
	    Queue queue() {
	        return new Queue(queueName, false);
	    }

	    @Bean
	    TopicExchange exchange() {
	        return new TopicExchange(topicExchangeName);
	    }

	    @Bean
	    Binding binding(Queue queue, TopicExchange exchange) {
	        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
	    }
	    
	    @Bean
	    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
	        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
	        return rabbitTemplate;
	    }

	    @Bean
	    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
	        return new Jackson2JsonMessageConverter();
	    }

}
