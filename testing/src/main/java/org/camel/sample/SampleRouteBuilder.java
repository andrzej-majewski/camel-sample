package org.camel.sample;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * Route builder uses allows injecting source uri and target uri. In this way you can test the route with amq
 * or in memory synchronous queue direct aor asynchronous queue seda.
 */
public class SampleRouteBuilder extends RouteBuilder {
    private String sourceUri;
    private String targetUri;

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }

    @Override
    public void configure() throws Exception {

        from(sourceUri)
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        Long body = exchange.getIn().getBody(Long.class);
                        //String convertedBody = String.valueOf(body);
                        exchange.getIn().setBody(String.valueOf(body));
                    }
                })//.log(LoggingLevel.INFO,"${body}")

                .to(targetUri);

    }
}
