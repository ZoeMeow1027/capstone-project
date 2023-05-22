package io.zoemeow.pbl6.phonestoremanager.controller;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

import io.zoemeow.pbl6.phonestoremanager.model.RequestException;
import io.zoemeow.pbl6.phonestoremanager.model.RequestResult;

public class BasicAPIRequestController {
    private final Boolean ignoreSSL = true;

    public RequestResult getRequest(String uri, Map<String, String> parameters, Map<String, String> header) {
        RequestResult result = new RequestResult();

        try {
            URIBuilder uriBuilder = new URIBuilder(uri);
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

    public RequestResult postRequest(String uri, Map<String, String> parameters,
            Map<String, String> header, String jsonString) {
        RequestResult result = new RequestResult();

        try {
            URIBuilder uriBuilder = new URIBuilder(uri);
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

    public RequestResult getUserInformation(Map<String, String> header, ArrayList<Integer> allowedUserType) throws Exception {
        RequestResult reqResult = getRequest("https://localhost:7053/api/account/my", null, header);
        if (!reqResult.getIsSuccessfulRequest()) {
            // TODO: Check if not successful request here!
        } else if (reqResult.getStatusCode() != 200) {
            throw new Exception(String.format("API was returned with code %d.", reqResult.getStatusCode()));
        } else if (allowedUserType != null) {
            if (!allowedUserType
                    .contains(reqResult.getData().get("data").getAsJsonObject().get("usertype").getAsInt())) {
                throw new Exception("This user isn't have enough permission to do that!");
            }
        }

        return reqResult;
    }
}
