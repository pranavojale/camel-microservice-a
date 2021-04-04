package com.ojale.pranav.camelmicroservicea.routes.patters;

import com.ojale.pranav.camelmicroservicea.routes.model.CurrencyExchange;
import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class EipPatternsRouter extends RouteBuilder {

    @Autowired
    private SplitterComponent splitterComponent;

    @Autowired
    private DynamicRouterBean dynamicRouterBean;

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

        /*from("timer:routingSlip?period=10000")
                .transform().constant("My Message is Hardcoded")
                .routingSlip(simple(routingSlip));*/

        // Dynamic Routing

        // Step 1, Step 2, Step 3

        from("timer:dynamicRouting?period={{timePeriod}}")
                .transform().constant("My Message is Hardcoded")
                .dynamicRouter(method(dynamicRouterBean, "decideTheNextEndpoint"));

        // Endpoint 1
        // Endpoint 2
        // Endpoint 3

        from("direct:endPoint1")
                .to("{{endpoint-for-logging-1}}");

        from("direct:endPoint2")
                .to("{{endpoint-for-logging-2}}");

        from("direct:endPoint3")
                .to("{{endpoint-for-logging-3}}");
    }


}

@Component
class SplitterComponent{
    public List<String> splitInput(String body){
        return Arrays.asList("ABC", "DEF", "PQR");
    }
}

@Component
class DynamicRouterBean{
    Logger LOGGER = LoggerFactory.getLogger(DynamicRouterBean.class);

    int invocations;

    public String decideTheNextEndpoint(@ExchangeProperties Map<String, String> properties,
                                        @Headers Map<String, String> headers,
                                        @Body String body){
        LOGGER.info("{} {} {}", properties, headers, body);

        invocations++;

        if(invocations%3 == 0){
            return "direct:endPoint1";
        }
        if(invocations%3 == 1){
            return "direct:endPoint2, direct:endPoint3";
        }

        return null;
    }
}
