package org.test.consumer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.jms.dsl.Jms;
import org.test.consumer.handler.JMSMessageConsumerHandler;

import javax.jms.ConnectionFactory;

@Configuration
@Import(value = { MessageConsumerJMSBrokerConfiguration.class })
public class MessageConsumerJMSConfiguration {
    @Autowired
    private DirectChannel inputChannel;


    @Value("${queuename}")
    private String queueName;

    @Bean
    @ServiceActivator(inputChannel = "inputChannel")
    public JMSMessageConsumerHandler jmsMessageConsumerHandler()
    {
        return new JMSMessageConsumerHandler();
    }

    @Bean
    public IntegrationFlow jmsMessageConsumerFlow(ActiveMQConnectionFactory connectionFactory)
    {
        return IntegrationFlows
                .from(Jms.messageDrivenChannelAdapter(connectionFactory)
                        .destination(queueName))
                .channel("inputChannel")
                .get();
    }

}
