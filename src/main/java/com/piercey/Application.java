package com.piercey;

import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class Application {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Main camel = new Main();
    private Thread thread;

    public static void main(String[] args) throws Exception {
        final Application application = new Application();
        application.startup(args, false);
    }

    public void startup(String[] args, boolean threading) throws Exception {
        if (threading) {
            thread = new Thread(() -> runApp(args));
            thread.start();
            waitUntil(camel::isStarted);
        } else {
            runApp(args);
        }
    }

    public void shutdown() throws Exception {
        camel.stop();
        waitUntil(camel::isStopped);
    }

    private void runApp(String[] args) {
        try {
            camel.addMainListener(new ApplicationListener());
            camel.configure().addRoutesBuilder(new ApplicationRoutes());
            camel.configure().setLoadTypeConverters(true);
            camel.run(args);
        } catch (Exception e) {
            logger.error("application failed to start", e);
        }
    }

    private void waitUntil(Callable<Boolean> callable) throws Exception {
        while (!callable.call()) {
            Thread.yield();
        }
    }

    //    public static void main(String[] args) throws Exception {
//        final Main camel = new Main();
//        camel.start();
//
//        final PropertiesComponent component = new PropertiesComponent();
//        component.setLocation("classpath:application.properties");
//
//        camel.getCamelContext().addRoutes(new ApplicationRoutes());
//        camel.getCamelContext().addComponent("properties", (Component) component);
//        camel.run();
//    }

}