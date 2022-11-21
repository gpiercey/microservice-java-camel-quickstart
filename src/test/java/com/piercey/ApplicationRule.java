package com.piercey;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationRule extends ExternalResource {
    private final Application application = new Application();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void before() throws Throwable {
        super.before();
        application.startup(new String[]{}, true);
        Thread.sleep(2000);
    }

    @Override
    protected void after() {
        super.after();
        try {
            application.shutdown();
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}