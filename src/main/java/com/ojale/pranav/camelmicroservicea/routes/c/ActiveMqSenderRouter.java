package com.ojale.pranav.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // timer
        from("timer:active-mq-timer?period=10000")
                .transform().constant("My message for Active MQ")
                .log("${body}")
                .to("rabbitmq://localhost:5672/pranavoj?queue=my-rabbitmq-queue&autoDelete=false");
                //.to("rabbitmq:pranavoj?queue=my-rabbitmq-queue");
        // queue
    }
}
