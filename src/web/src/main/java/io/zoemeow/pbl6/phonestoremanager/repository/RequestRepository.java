package io.zoemeow.pbl6.phonestoremanager.repository;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.zoemeow.pbl6.phonestoremanager.model.RequestException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;

@Repository
public class RequestRepository {
    private static final Boolean ignoreSSL = true;
    
    @Value("${serverapi.baseurl}")
    private static String baseUrl = "https://127.0.0.1:7053";

    public static RequestResult<JsonObject> getRequest(String uri, Map<String, String> parameters, Map<String, String> header) {
        RequestResult<JsonObject> result = new RequestResult<JsonObject>();

        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl + uri);
            if (parameters != null) {
                for (String parItem : parameters.keySet()) {
                    uriBuilder.addParameter(parItem, parameters.get(parItem));
                }
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            if (header != null) {
                for (String key : header.keySet()) {
                    httpGet.addHeader(key, header.get(key));
                }
            }

            CloseableHttpClient httpClient = createHttpClient();
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            if (httpResponse.getCode() != 200)
                throw new RequestException(
                        uriBuilder.build().toString(),
                        httpResponse.getCode(),
                        "");
            result.setStatusCode(httpResponse.getCode());
            result.setIsSuccessfulRequest(true);
            result.setMessage("Successful");

            try {
                String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();

                result.setData(jObject);
            } catch (Exception ex) {
                result.setData(null);
            }
        } catch (RequestException rEx) {
            result.setStatusCode(rEx.getStatusCode());
            result.setMessage(rEx.getMessage());
            result.setIsSuccessfulRequest(true);
        } catch (Exception ex) {
            result.setIsSuccessfulRequest(false);
            result.setMessage(ex.getMessage());
        }

        return result;
    }

    public static RequestResult<JsonObject> postRequest(String uri, Map<String, String> parameters,
            Map<String, String> header, String jsonString) {
        RequestResult<JsonObject> result = new RequestResult<JsonObject>();

        try {
            URIBuilder uriBuilder = new URIBuilder(baseUrl + uri);
            if (parameters != null) {
                for (String parItem : parameters.keySet()) {
                    uriBuilder.addParameter(parItem, parameters.get(parItem));
                }
            }

            HttpPost httppost = new HttpPost(uriBuilder.build());
            if (header != null) {
                for (String key : header.keySet()) {
                    httppost.addHeader(key, header.get(key));
                }
            }
            httppost.addHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity jsonparam = new StringEntity(jsonString);
            httppost.setEntity(jsonparam);

            CloseableHttpClient httpClient = createHttpClient();
            CloseableHttpResponse httpResponse = httpClient.execute(httppost);

            JsonObject jObject = null;
            try {
                String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                jObject = JsonParser.parseString(responseString).getAsJsonObject();

                result.setData(jObject);
            } catch (Exception ex) {
                result.setData(null);
            }

            if (httpResponse.getCode() != 200)
                throw new RequestException(
                        uriBuilder.build().toString(),
                        httpResponse.getCode(),
                        jObject != null ? jObject.get("msg").getAsString() : "Unknown error"
                        );
            result.setStatusCode(httpResponse.getCode());
            result.setIsSuccessfulRequest(true);
            result.setMessage("Successful");
        } catch (RequestException rEx) {
            result.setStatusCode(rEx.getStatusCode());
            result.setMessage(rEx.getMessage());
            result.setIsSuccessfulRequest(true);
        } catch (Exception ex) {
            result.setIsSuccessfulRequest(false);
            result.setMessage(ex.getMessage());
        }

        return result;
    }

    private static CloseableHttpClient createHttpClient()
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
