package com.piercey;

import com.piercey.processors.ExceptionProcessor;
import com.piercey.processors.HealthProcessor;
import com.piercey.processors.RestProcessor;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class ApplicationRoutes extends RouteBuilder {
    private final ExceptionProcessor exceptionProcessor = new ExceptionProcessor();
    private final HealthProcessor healthProcessor = new HealthProcessor();
    private final RestProcessor restProcessor = new RestProcessor();

    @PropertyInject("api.base.path:/api")
    private String basePath;

    @PropertyInject("api.enable.cors:false")
    private boolean enableCors;

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("netty-http")
                .bindingMode(RestBindingMode.auto)
                .host("{{api.host:0.0.0.0}}")
                .port("{{api.port:8080}}")
                .enableCORS(enableCors);

        onException(Exception.class)
                .handled(true)
                .to("direct:exceptionProcessor");

        rest(basePath).produces("application/json")
                .get("/health").to("direct:healthProcessor")
                .get("/samples").to("direct:restProcessor")
                .get("/sample/{id}").to("direct:restProcessor")
                .post("/sample").to("direct:restProcessor")
                .patch("/sample/{id}").to("direct:restProcessor")
                .delete("/sample/{id}").to("direct:restProcessor");

        from("direct:exceptionProcessor")
                .process(exceptionProcessor);

        from("direct:healthProcessor")
                .process(healthProcessor);

        from("direct:restProcessor")
                .process(restProcessor);
    }
}