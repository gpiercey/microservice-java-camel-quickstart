package com.piercey.processors;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.piercey.ApplicationRule;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HealthCheckTest {
    @ClassRule
    public static ApplicationRule application = new ApplicationRule();

    @Test
    public void testHealthAlive() throws Exception {
        final HttpResponse<com.mashape.unirest.http.JsonNode> o =
                Unirest.get("http://127.0.0.1:8080/api/health")
                        .asJson();
        assertNotNull(o);
        assertEquals(200, o.getStatus());
    }
}