package com.ojale.pranav.camelmicroservicea.routes.patters;

import com.ojale.pranav.camelmicroservicea.routes.model.CurrencyExchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EipPatternsRouter extends RouteBuilder {

    @Autowired
    private SplitterComponent splitterComponent;

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
        /*from("file:files/csv")
                .unmarshal().csv()
                .split(body())
                .to("rabbitmq://localhost:5672/pranavoj?queue=my-rabbitmq-queue&autoDelete=false");*/
                //.to("log:split-files");

        /*from("file:files/csv")
                .convertBodyTo(String.class)
                //.split(body(),",")
                .split(method(splitterComponent))
                .to("rabbitmq://localhost:5672/pranavoj?queue=my-rabbitmq-queue&autoDelete=false");*/

        // Aggregation Pattern
        /*from("file:files/aggregate-json")
                .unmarshal().json(JsonLibrary.Jackson, CurrencyExchange.class)
                .aggregate(simple("${body.to}"), new ArrayListAggregationStrategy())
                .completionSize(3)
                //.completionTimeout(10000l)
                .to("log:aggregate-json");*/

        // Routing Slip pattern

        String routingSlip = "direct:endPoint1,direct:endPoint2";
        //String routingSlip = "direct:endPoint1,direct:endPoint2,direct:endPoint3";

        from("timer:routingSlip?period=10000")
                .transform().constant("My Message is Hardcoded")
                .routingSlip(simple(routingSlip));

        from("direct:endPoint1")
                .to("log:directEndPoint1");

        from("direct:endPoint2")
                .to("log:directEndPoint2");

        from("direct:endPoint3")
                .to("log:directEndPoint3");
    }


}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return Arrays.asList("ABC", "DEF", "PQR");
    }
}
