package org.camel.sample;

import org.apache.camel.Exchange;
import org.apache.camel.Endpoint;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.direct.DirectEndpoint;

/**
 * Route builder uses allows injecting source uri and target uri. In this way you can test the route with amq
 * or in memory synchronous queue direct aor asynchronous queue seda.
 */
public class SampleRouteBuilder extends RouteBuilder {

    //private Endpoint source = new DirectEndpoint("direct:transformBody");

    @Override
    public void configure() throws Exception {

        from("direct:transformBody")
                .process(new TransformerProcessor())
                ;

    }

    private class TransformerProcessor implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            exchange.getIn().setBody(String.valueOf(exchange.getIn().getBody(Integer.class)));

        }
    }
}
