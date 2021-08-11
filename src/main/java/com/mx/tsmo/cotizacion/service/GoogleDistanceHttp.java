package com.mx.tsmo.cotizacion.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@Slf4j
public class GoogleDistanceHttp {

    private HttpClient httpClient;
    private HttpGet get;
    private HttpPost post;
    private HttpResponse response;
    private HttpRequest request;
    private String resource;

    public GoogleDistanceHttp() {
        this.httpClient = HttpClients.createDefault();
        this.get = null;
        this.resource = null;
    }

    public String GET(String url) {
        this.get = new HttpGet(url);
        try {
            this.response = this.httpClient.execute(this.get);
            this.resource = EntityUtils.toString(this.response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return this.resource;
    }
}
