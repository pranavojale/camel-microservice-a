package com.ojale.pranav.camelmicroservicea.routes.b;

import org.apache.camel.Body;
import org.apache.camel.ExchangeProperties;
import org.apache.camel.Headers;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class MyFileRouter extends RouteBuilder {
    /*
    Headers             {CamelFileAbsolute=false, CamelFileAbsolutePath=E:\IntelliJ_Workspace\Apache_Camel\camel-microservice-a\files\input\1000.json, CamelFileLastModified=1617014103247, CamelFileLength=76, CamelFileName=1000.json, CamelFileNameConsumed=1000.json, CamelFileNameOnly=1000.json, CamelFileParent=files\input, CamelFilePath=files\input\1000.json, CamelFileRelativePath=1000.json}
     */
    // https://camel.apache.org/components/latest/languages/simple-language.html
    // https://camel.apache.org/components/latest/languages/file-language.html

    @Autowired
    private DeciderBean deciderBean;

    @Override
    public void configure() throws Exception {

        // Pipeline pattern
        from("file:files/input")
                //.pipeline()
                .routeId("Files-Input-Route")
                .transform().body(String.class)
                .choice() // Content Based Routing
                    .when(simple("${file:ext} ends with 'xml'"))
                        .log("XML FILE")
                    .when(simple("${file:ext} == 'csv'"))
                        .log("CSV FILE")
                    //.when(simple("${body} contains 'USD'"))
                    .when(method(deciderBean, "isThisConditionMet"))
                        .log("Not an XML FILE but contains USD")
                    .otherwise()
                        .log("Not an XML FILE or a JSON FILE")
                .end()
                //.log("${body}")
                //.log("${messageHistory} ${headers.CamelFileAbsolute} ${file:absolute.path}")
                //.to("direct://log-file-values")
                .to("file:files/output");

        // Reusable End Point
        from("direct:log-file-values")
                .log("${messageHistory} ${file:absolute.path}")
                .log("${file:name} ${file:name.ext}")
                .log("${file:size} ${file:modified}")
                .log("${routeId} ${camelId} ${body}");
    }
}

@Component
class DeciderBean {

    Logger LOGGER = LoggerFactory.getLogger(DeciderBean.class);

    public boolean isThisConditionMet(@Body String body,
                                      @Headers Map<String, String> headers,
                                      @ExchangeProperties Map<String, String> exchangeProperties){
        LOGGER.info("DeciderBean {} {} {}", body, headers, exchangeProperties);
        return true;
    }
}
