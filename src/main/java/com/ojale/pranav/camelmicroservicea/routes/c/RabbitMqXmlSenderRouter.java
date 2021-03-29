package com.ojale.pranav.camelmicroservicea.routes.c;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class RabbitMqXmlSenderRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:files/xml")
                .log("${body}")
                .to("rabbitmq://localhost:5672/pranavoj-xml?queue=my-rabbitmq-xml-queue&autoDelete=false");
    }
}
