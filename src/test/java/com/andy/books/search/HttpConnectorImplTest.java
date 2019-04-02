package com.andy.books.search;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpConnectorImplTest {

    private HttpConnector httpConnector;
    @Mock
    private CloseableHttpClient closeableHttpClient;
    @Mock
    private CloseableHttpResponse response;

    @Test
    public void shouldReturnEmptyResponse() throws IOException {
        httpConnector = new FakeHttpConnector(closeableHttpClient);
        when(closeableHttpClient.execute(any())).thenThrow(IOException.class);

        String response = httpConnector.get(new URL("https://www.google.co,uk"));

        assertEquals("", response);
    }

    @Test
    public void shouldReturnResponse() throws IOException, URISyntaxException {
        when(closeableHttpClient.execute(any())).thenReturn(response);
        when(response.getEntity()).thenReturn(createEntityFromJsonFile());

        httpConnector = new FakeHttpConnector(closeableHttpClient);

        String response = httpConnector.get(new URL("https://www.google.co,uk"));

        assertEquals("{\n" +
                "  \"key1\": \"value1\",\n" +
                "  \"key2\": \"value2\"\n" +
                "}\n", response);
    }

    private BasicHttpEntity createEntityFromJsonFile() throws FileNotFoundException, URISyntaxException {
        URL resource = getClass().getResource("/simple.json");
        FileInputStream fileInputStream = new FileInputStream(new File(resource.toURI()));

        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(fileInputStream);
        return entity;
    }

    class FakeHttpConnector extends HttpConnectorImpl {
        private CloseableHttpClient httpClient;

        public FakeHttpConnector(CloseableHttpClient httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        protected CloseableHttpClient getHttpClient() {
            return httpClient;
        }
    }
}