package com.piercey.processors;

import com.piercey.commons.exceptions.HttpException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionProcessor implements Processor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws Exception {
        final Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);

        final int httpStatus = e instanceof HttpException ? ((HttpException) e).getHttpStatus() : 500;
        final String httpMessage = e.getMessage();

        if (httpStatus >= 500) {
            logger.error("{}: {}", httpStatus, httpMessage, e);
        } else {
            if (e instanceof HttpException && ((HttpException) e).hasCustomData()) {
                logger.warn("{}: {}: customStatus={} customMessage={}", httpStatus, httpMessage, ((HttpException) e).getCustomStatus(), ((HttpException) e).getCustomMessage());
            } else {
                logger.warn("{}: {}", httpStatus, httpMessage);
            }
        }

        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, httpStatus);
        exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_TEXT, httpMessage);
    }
}