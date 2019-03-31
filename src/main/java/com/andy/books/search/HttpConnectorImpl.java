package com.andy.books.search;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpConnectorImpl implements HttpConnector {

    private Logger logger = LogManager.getLogger();

    @Override
    public String get(String url) {
        String jsonResponse = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            HttpEntity entity1 = response.getEntity();

            jsonResponse = EntityUtils.toString(entity1);
        } catch (IOException e) {
            logger.error("Failed to get data from " + url);
        }

        return jsonResponse;
    }
}
