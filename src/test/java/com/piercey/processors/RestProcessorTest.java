package com.piercey.processors;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.piercey.ApplicationRule;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RestProcessorTest {
    @ClassRule
    public static ApplicationRule application = new ApplicationRule();

    @Test
    public void testGetAll() throws Exception {
        final HttpResponse<com.mashape.unirest.http.JsonNode> o =
                Unirest.get("http://127.0.0.1:8080/api/samples")
                        .asJson();
        assertNotNull(o);
        assertEquals(200, o.getStatus());
        assertEquals("[]", o.getBody().toString());
    }

    @Test
    public void testGetOne() throws Exception {
        final HttpResponse<com.mashape.unirest.http.JsonNode> o =
                Unirest.get("http://127.0.0.1:8080/api/sample/1")
                        .asJson();
        assertNotNull(o);
        assertEquals(200, o.getStatus());
        assertEquals("{}", o.getBody().toString());
    }

    @Test
    public void testPost() throws Exception {
        final HttpResponse<com.mashape.unirest.http.JsonNode> o =
                Unirest.post("http://127.0.0.1:8080/api/sample")
                        .asJson();
        assertNotNull(o);
        assertEquals(201, o.getStatus());
        assertEquals("{}", o.getBody().toString());
    }

    @Test
    public void testPatch() throws Exception {
        final HttpResponse<com.mashape.unirest.http.JsonNode> o =
                Unirest.patch("http://127.0.0.1:8080/api/sample/1")
                        .asJson();
        assertNotNull(o);
        assertEquals(200, o.getStatus());
        assertEquals("{}", o.getBody().toString());
    }

    @Test
    public void testDelete() throws Exception {
        final HttpResponse<com.mashape.unirest.http.JsonNode> o =
                Unirest.delete("http://127.0.0.1:8080/api/sample/1")
                        .asJson();
        assertNotNull(o);
        assertEquals(200, o.getStatus());
        assertEquals("{}", o.getBody().toString());
    }
}