package org.camel.sample;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.camel.sample.SampleRouteBuilder;
import org.junit.Test;

/**
 * Test class uses the DataSet component to load test a route.
 */
public class DirectSampleRouteBuilderLoadTest extends CamelTestSupport {

    private static int NUMBER_OF_MESSAGES = 100000;

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {

        SimpleRegistry registry = new SimpleRegistry();
        return new DefaultCamelContext(registry);
    }


    protected RouteBuilder[] createRouteBuilders() throws Exception {
        RouteBuilder[] routeBuilders = new RouteBuilder[1];

        SampleRouteBuilder simpleRouteBuilder = new SampleRouteBuilder();
        simpleRouteBuilder.setSourceUri("direct:transformBody");
        simpleRouteBuilder.setTargetUri("mock:expectedOutput");


        routeBuilders[0] = simpleRouteBuilder;
        return routeBuilders;
    }

    @Test
    public void testPayloadsTransformedInExpectedTime() throws Exception {
        // A DataSetEndpoint is a sub-class of MockEndpoint that sets up expectations based on
        // the messages created, and the size property on the object.
        // All that is needed for us to test this route is to assert that the endpoint was satisfied.

        long startTime = System.currentTimeMillis();

        context.start();

        MockEndpoint expectedOutput = getMockEndpoint("mock:expectedOutput");
        expectedOutput.setResultWaitTime(200000);
        expectedOutput.setExpectedMessageCount(NUMBER_OF_MESSAGES);

        for (long i = 0; i < NUMBER_OF_MESSAGES; i++) {
            template.sendBody("direct:transformBody", new Long(i));
        }

        expectedOutput.assertIsSatisfied();

        long executionTime = System.currentTimeMillis() - startTime;

        System.out.println("Submitted "+NUMBER_OF_MESSAGES+" messages in " + executionTime+ " ms");

        context.stop();
    }
}
