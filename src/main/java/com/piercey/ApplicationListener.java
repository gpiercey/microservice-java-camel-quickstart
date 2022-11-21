package com.piercey;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jasypt.JasyptPropertiesParser;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.health.HealthCheckRegistry;
import org.apache.camel.impl.health.DefaultHealthCheckRegistry;
import org.apache.camel.impl.health.RoutesHealthCheckRepository;
import org.apache.camel.main.BaseMainSupport;
import org.apache.camel.main.MainListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationListener extends MainListenerSupport {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String locations = "/etc/configmap/application.properties,classpath:application.properties";
    private final String defaultLocation = "/etc/configmap/application.properties";

    @Override
    public void beforeInitialize(BaseMainSupport main) {
        main.setDefaultPropertyPlaceholderLocation(defaultLocation);
        main.setPropertyPlaceholderLocations(locations);
        initializeJasyptPropertyDecryptor(main);
        initializeHealthCheckRegistry(main.getCamelContext());
    }

    @Override
    public void beforeConfigure(BaseMainSupport main) {
    }

    @Override
    public void afterConfigure(BaseMainSupport main) {
    }

    @Override
    public void beforeStart(BaseMainSupport main) {
    }

    @Override
    public void afterStart(BaseMainSupport main) {
    }

    @Override
    public void beforeStop(BaseMainSupport main) {
    }

    @Override
    public void afterStop(BaseMainSupport main) {
    }

    private void initializeJasyptPropertyDecryptor(BaseMainSupport main) {
        if (System.getenv().containsKey("CRYPTO_KEY")) {
            final JasyptPropertiesParser parser = new JasyptPropertiesParser();
            parser.setPassword(System.getenv("CRYPTO_KEY"));
            // TODO: env is weak, use AWS SSM or similar to improve security here

            final PropertiesComponent component = new PropertiesComponent();
            component.setLocation(locations);
            component.setIgnoreMissingLocation(true);
            component.setPropertiesParser(parser);

            main.getCamelContext().setPropertiesComponent(component);

        } else {
            logger.warn("crypto key undefined, properties won't be decrypted");
        }
    }

    private void initializeHealthCheckRegistry(CamelContext camelContext) {
        final DefaultHealthCheckRegistry registry = new DefaultHealthCheckRegistry();
        registry.register(new RoutesHealthCheckRepository());
        camelContext.setExtension(HealthCheckRegistry.class, registry);
    }
}