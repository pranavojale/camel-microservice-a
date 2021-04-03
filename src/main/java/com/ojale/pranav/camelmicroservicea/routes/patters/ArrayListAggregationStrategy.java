package com.ojale.pranav.camelmicroservicea.routes.patters;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

import java.util.ArrayList;

public class ArrayListAggregationStrategy implements AggregationStrategy {
    // 1, 2, 3

    // null, 1  => [1]
    // [1], 2   => [1,2]
    // [1,2], 3 => [1,2,3]

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Object newBody = newExchange.getIn().getBody();

        ArrayList<Object> list = null;

        if(oldExchange == null){
            list = new ArrayList<>();
            list.add(newBody);
            newExchange.getIn().setBody(list);

            return newExchange;
        }else{
            list = oldExchange.getIn().getBody(ArrayList.class);
            list.add(newBody);

            return oldExchange;
        }
    }
}
