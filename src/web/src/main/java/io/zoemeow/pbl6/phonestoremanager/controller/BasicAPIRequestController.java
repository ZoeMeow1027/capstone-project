package io.zoemeow.pbl6.phonestoremanager.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BasicAPIRequestController {
    private final Boolean ignoreSSL = true;

    public JsonObject getRequest(String uri, Map<String, String> parameters, Map<String, String> header)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException,
            URISyntaxException {
        URIBuilder builder = new URIBuilder(uri);
        if (parameters != null) {
            for (String parItem : parameters.keySet()) {
                builder.addParameter(parItem, parameters.get(parItem));
            }
        }

        HttpGet httpGet = new HttpGet(builder.build());
        if (header != null) {
            for (String key : header.keySet()) {
                httpGet.addHeader(key, header.get(key));
            }
        }

        CloseableHttpClient httpClient = createHttpClient();

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        try {
            String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();
            return jObject;
        } catch (Exception ex) {
            return null;
        }
    }

    public JsonObject postRequest(String uri, String jsonString, Map<String, String> header) throws URISyntaxException,
            KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        URIBuilder builder = new URIBuilder(uri);

        HttpPost httppost = new HttpPost(builder.build());
        if (header != null) {
            for (String key : header.keySet()) {
                httppost.addHeader(key, header.get(key));
            }
        }
        StringEntity jsonparam = new StringEntity(jsonString);
        httppost.setEntity(jsonparam);

        CloseableHttpClient httpClient = createHttpClient();
        CloseableHttpResponse httpResponse = httpClient.execute(httppost);

        try {
            String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();
            return jObject;
        } catch (Exception ex) {
            return null;
        }
    }

    private CloseableHttpClient createHttpClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        CloseableHttpClient httpClient = null;

        if (ignoreSSL) {
            httpClient = HttpClients.custom()
                    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                                    .setSslContext(SSLContextBuilder.create()
                                            .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                                            .build())
                                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                    .build())
                            .build())
                    .build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        return httpClient;
    }
}
