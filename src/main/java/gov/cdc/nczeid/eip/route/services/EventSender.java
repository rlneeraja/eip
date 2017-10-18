package gov.cdc.nczeid.eip.route.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String queueName, String guid) {
        this.rabbitTemplate.convertAndSend(queueName,guid);
    }
}
