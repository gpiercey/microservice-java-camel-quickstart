package com.piercey.processors;

import com.piercey.commons.exceptions.HttpException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.health.HealthCheck;
import org.apache.camel.health.HealthCheckHelper;

import java.util.Collection;

public class HealthProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws HttpException {
        final Collection<HealthCheck.Result> results = HealthCheckHelper.invokeReadiness(exchange.getContext());
        final int httpStatus = results.stream().anyMatch(o -> o.getState() == HealthCheck.State.UP) ? 200 : 503;
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatus);
        exchange.getMessage().setBody(httpStatus == 200 ? results.iterator().next().getDetails() : "");
    }
}