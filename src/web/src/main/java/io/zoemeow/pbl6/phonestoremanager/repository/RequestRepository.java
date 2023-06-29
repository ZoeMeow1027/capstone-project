package io.zoemeow.pbl6.phonestoremanager.repository;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.core.io.Resource;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;
import io.zoemeow.pbl6.phonestoremanager.model.exceptions.RequestException;

public class RequestRepository {
    private final Boolean ignoreSSL = true;

    private String getBaseURL() {
        final String defaultBaseUrl = "http://localhost:5000";

        String baseUrl = System.getenv("API_BASEURL");
        return (baseUrl != null) ? baseUrl : defaultBaseUrl;
    }

    public RequestResult<JsonObject> getRequestWithResult(String uri, Map<String, String> parameters, Map<String, String> header) {
        RequestResult<JsonObject> result = new RequestResult<JsonObject>();

        try {
            URIBuilder uriBuilder = new URIBuilder(getBaseURL() + uri);
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

    public RequestResult<JsonObject> getRequestWithResult(String baseUri, String uri, Map<String, String> parameters, Map<String, String> header) {
        RequestResult<JsonObject> result = new RequestResult<JsonObject>();

        try {
            URIBuilder uriBuilder = new URIBuilder(baseUri + uri);
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

    public RequestResult<JsonObject> postRequestWithResult(String uri, Map<String, String> parameters,
            Map<String, String> header, String jsonString) {
        RequestResult<JsonObject> result = new RequestResult<JsonObject>();

        try {
            URIBuilder uriBuilder = new URIBuilder(getBaseURL() + uri);
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

            if (jsonString != null) {
                HttpEntity jsonparam = new StringEntity(jsonString, Charset.forName("UTF-8"));
                httppost.setEntity(jsonparam);
            }

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

    public byte[] getRequestToImage(String url, Map<String, String> parameters, Map<String, String> header) throws Exception {
        URIBuilder uriBuilder = new URIBuilder(getBaseURL() + url);
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
            throw new Exception("No image here!");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        httpResponse.getEntity().writeTo(baos);
        return baos.toByteArray();
    }

    public RequestResult<JsonObject> postRequestFromImage(
        String url, Map<String, String> parameters, Map<String, String> header, Resource resource, Map<String, String> body
    ) {
        RequestResult<JsonObject> result = new RequestResult<JsonObject>();

        try {
            URIBuilder uriBuilder = new URIBuilder(getBaseURL() + url);
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

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.LEGACY);
            builder.setContentType(ContentType.MULTIPART_FORM_DATA);
            builder.addBinaryBody("file", resource.getInputStream(), ContentType.APPLICATION_OCTET_STREAM, resource.getFilename());
            if (body != null) {
                for (var dataItem: body.keySet()) {
                    builder.addTextBody(dataItem, body.get(dataItem));
                }
            }
            HttpEntity multipart = builder.build();
            httppost.setEntity(multipart);
        
            CloseableHttpClient httpClient = createHttpClient();
            CloseableHttpResponse httpResponse = httpClient.execute(httppost);

            JsonObject jObject = null;
            try {
                String responseString = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                jObject = JsonParser.parseString(responseString).getAsJsonObject();

                result.setData(jObject);
            } catch (Exception ex) {
                result.setData(null);
                // Remove in stable
                // result.setMessage(ex.getMessage());
            }

            if (httpResponse.getCode() != 200)
                throw new RequestException(
                        uriBuilder.build().toString(),
                        httpResponse.getCode(),
                        jObject != null ? jObject.get("msg").getAsString() : result.getMessage() // "Unknown error"
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
}
