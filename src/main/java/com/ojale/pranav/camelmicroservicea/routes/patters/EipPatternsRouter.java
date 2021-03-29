package com.ojale.pranav.camelmicroservicea.routes.patters;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class EipPatternsRouter extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Pipeline Pattern
        // Content Based Routing - choice()

        // Multicast Pattern
        /*from("timer:multicast?period=10000")
                .multicast()
                .to("log:something1", "log:something2", "log:something3");
        */
        // Splitter Pattern
        from("file:files/csv")
                .unmarshal().csv()
                .split(body())
                .to("rabbitmq://localhost:5672/pranavoj?queue=my-rabbitmq-queue&autoDelete=false");
                //.to("log:split-files");
    }
}
