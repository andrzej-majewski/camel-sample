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
public class DataSetSampleRouteBuilderLoadTest extends CamelTestSupport {

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        final int testBatchSize = 100000;

        InputDataSet inputDataSet = new InputDataSet();
        inputDataSet.setSize(testBatchSize);


        SimpleRegistry registry = new SimpleRegistry();
        registry.put("input", inputDataSet);

        return new DefaultCamelContext(registry);
    }


    protected RouteBuilder[] createRouteBuilders() throws Exception{
        RouteBuilder[] routeBuilders = new RouteBuilder[2];

        SampleRouteBuilder simpleRouteBuilder = new SampleRouteBuilder();
        simpleRouteBuilder.setSourceUri("seda:transformBody?concurrentConsumers=1");
        simpleRouteBuilder.setTargetUri("mock:expectedOutput");


        routeBuilders[0]= new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("dataset:input?produceDelay=-1&preloadSize=5000").to("seda:transformBody");
            }
        };
        routeBuilders[1]= simpleRouteBuilder;
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
        expectedOutput.setExpectedMessageCount(100000);
        expectedOutput.assertIsSatisfied();

        long executionTime = System.currentTimeMillis() -startTime;

        System.out.println("execution time" + executionTime);

        context.stop();
    }
}
