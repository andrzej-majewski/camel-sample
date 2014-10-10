package org.camel.sample;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.direct.DirectEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.main.Main;

/**
 * Created by amajewski on 06/10/2014.
 */
public class MainExample {


    public static void main(String[] args) throws Exception {
        MainExample example = new MainExample();
        example.boot();
    }

    public void boot() throws Exception {

        CamelContext context = new DefaultCamelContext();

        SampleRouteBuilder simpleRouteBuilder = new SampleRouteBuilder();

        // add routes
        context.addRoutes(simpleRouteBuilder);

        ProducerTemplate template = context.createProducerTemplate();
        Endpoint direct = context.getEndpoint("direct:transformBody");

        context.start();

        int x = 0;
        int eventCount = 2000000;
        StopWatch stopWatch = StopWatch.getInstance();

        try {
            stopWatch.start();

            for (; x < eventCount; x++) {

                template.sendBody(direct, new Integer(x));
            }

            stopWatch.stop();
            System.out.print(stopWatch.getTime() + " for [" + eventCount + "] events.");

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
