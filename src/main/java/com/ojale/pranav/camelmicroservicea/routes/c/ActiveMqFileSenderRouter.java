package com.ojale.pranav.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqFileSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:files/json")
                .log("${body}")
                .to("rabbitmq://localhost:5672/pranavoj?queue=my-rabbitmq-queue&autoDelete=false");
    }
}
