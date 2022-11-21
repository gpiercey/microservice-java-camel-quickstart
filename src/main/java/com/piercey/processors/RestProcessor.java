package com.piercey.processors;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.piercey.commons.ExecutionTimer;
import com.piercey.commons.JsonSerializer;
import com.piercey.commons.exceptions.HttpException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JsonSerializer serializer = new JsonSerializer();

    @Override
    public void process(Exchange exchange) throws HttpException {
        try (ExecutionTimer t = new ExecutionTimer(logger, extractRequestDetails(exchange))) {
            final String method = exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class);
            switch (method) {
                case "GET" -> {
                    if (exchange.getIn().getHeaders().containsKey("id")) { // <- convention... change as desired
                        readOne(exchange);
                    } else {
                        readAll(exchange);
                    }
                }
                case "POST" -> create(exchange);
                case "PATCH" -> update(exchange);
                case "DELETE" -> delete(exchange);
            }
        }
    }

    private void create(Exchange exchange) throws HttpException {
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 201);
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getMessage().setBody(JsonNodeFactory.instance.objectNode());
    }

    private void readAll(Exchange exchange) throws HttpException {
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getMessage().setBody(JsonNodeFactory.instance.arrayNode());
    }

    private void readOne(Exchange exchange) throws HttpException {
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getMessage().setBody(JsonNodeFactory.instance.objectNode());
    }

    private void update(Exchange exchange) throws HttpException {
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getMessage().setBody(JsonNodeFactory.instance.objectNode());
    }

    private void delete(Exchange exchange) throws HttpException {
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        exchange.getMessage().setHeader(Exchange.CONTENT_TYPE, "application/json");
        exchange.getMessage().setBody(JsonNodeFactory.instance.objectNode());
    }

    private String extractRequestDetails(Exchange exchange) {
        final String method = exchange.getIn().getHeader(Exchange.HTTP_METHOD, String.class);
        final String resourcePath = exchange.getIn().getHeader(Exchange.HTTP_URI, String.class);
        final String queryString = exchange.getIn().getHeader(Exchange.HTTP_QUERY, "", String.class);
        final String userAgent = exchange.getIn().getHeader("user-agent", "unknown", String.class);

        final String sourceIp = exchange.getIn()
                .getHeader("CamelNettyRemoteAddress", "unknown", String.class)
                .replaceAll("/", "");

        return sourceIp + " (" + userAgent + ") " + method + ":" + resourcePath
                + (!queryString.isEmpty() ? "?" + queryString : "");
    }
}