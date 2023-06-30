package io.zoemeow.pbl6.phonestoremanager.repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.core.io.Resource;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.zoemeow.pbl6.phonestoremanager.model.bean.RequestResult;

public class RequestRepository {
    private final Boolean ignoreSSL = true;

    private String getBaseURL() {
        final String defaultBaseUrl = "http://localhost:5000";

        String baseUrl = System.getenv("API_BASEURL");
        return (baseUrl != null) ? baseUrl : defaultBaseUrl;
    }

    public RequestResult<JsonObject> getRequestWithResult(String uri, Map<String, String> parameters, Map<String, String> header) {
        final HttpClientResponseHandler<RequestResult<JsonObject>> responseHandler = new HttpClientResponseHandler<RequestResult<JsonObject>>() {
            @Override
            public RequestResult<JsonObject> handleResponse(ClassicHttpResponse response)
                    throws HttpException, IOException {
                RequestResult<JsonObject> result = new RequestResult<JsonObject>();

                try {
                    result.setStatusCode(response.getCode());
                    result.setMessage("Successful!");
                    result.setIsSuccessfulRequest(true);
                    if (!(result.getStatusCode() >= HttpStatus.SC_SUCCESS
                            && result.getStatusCode() < HttpStatus.SC_REDIRECTION)) {
                        throw new Exception(String.format("API was returned with code %d.", result.getStatusCode()));
                    }

                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();
                    result.setData(jObject);
                } catch (Exception ex) {
                    result.setIsSuccessfulRequest(false);
                    result.setMessage(ex.getMessage());
                    result.setData(null);
                }
                return result;
            }
        };

        RequestResult<JsonObject> result = new RequestResult<JsonObject>();
        try {
            CloseableHttpClient httpClient = createHttpClient();
            HttpGet httpGet = new HttpGet(uriBuilder(uri, parameters));
            if (header != null) {
                for (String key : header.keySet()) {
                    httpGet.addHeader(key, header.get(key));
                }
            }

            result = httpClient.execute(httpGet, responseHandler);
        } catch (Exception ex) {
            result.setIsSuccessfulRequest(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    public RequestResult<JsonObject> getRequestWithResult(String baseUri, String path, Map<String, String> parameters, Map<String, String> header) {
        final HttpClientResponseHandler<RequestResult<JsonObject>> responseHandler = new HttpClientResponseHandler<RequestResult<JsonObject>>() {
            @Override
            public RequestResult<JsonObject> handleResponse(ClassicHttpResponse response)
                    throws HttpException, IOException {
                RequestResult<JsonObject> result = new RequestResult<JsonObject>();

                try {
                    result.setStatusCode(response.getCode());
                    result.setMessage("Successful!");
                    result.setIsSuccessfulRequest(true);
                    if (!(result.getStatusCode() >= HttpStatus.SC_SUCCESS
                            && result.getStatusCode() < HttpStatus.SC_REDIRECTION)) {
                        throw new Exception(String.format("API was returned with code %d.", result.getStatusCode()));
                    }

                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();
                    result.setData(jObject);
                } catch (Exception ex) {
                    result.setIsSuccessfulRequest(false);
                    result.setMessage(ex.getMessage());
                    result.setData(null);
                }
                return result;
            }
        };

        RequestResult<JsonObject> result = new RequestResult<JsonObject>();
        try {
            CloseableHttpClient httpClient = createHttpClient();
            HttpGet httpGet = new HttpGet(uriBuilder(String.format("%s%s", baseUri, path), parameters));
            if (header != null) {
                for (String key : header.keySet()) {
                    httpGet.addHeader(key, header.get(key));
                }
            }

            result = httpClient.execute(httpGet, responseHandler);
        } catch (Exception ex) {
            result.setIsSuccessfulRequest(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    public RequestResult<JsonObject> postRequestWithResult(String uri, Map<String, String> parameters,
            Map<String, String> header, String jsonString) {
        final HttpClientResponseHandler<RequestResult<JsonObject>> responseHandler = new HttpClientResponseHandler<RequestResult<JsonObject>>() {
            @Override
            public RequestResult<JsonObject> handleResponse(ClassicHttpResponse response)
                    throws HttpException, IOException {
                RequestResult<JsonObject> result = new RequestResult<JsonObject>();

                try {
                    result.setStatusCode(response.getCode());
                    result.setMessage("Successful!");
                    result.setIsSuccessfulRequest(true);
                    if (!(result.getStatusCode() >= HttpStatus.SC_SUCCESS
                            && result.getStatusCode() < HttpStatus.SC_REDIRECTION)) {
                        throw new Exception(String.format("API was returned with code %d.", result.getStatusCode()));
                    }

                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();
                    result.setData(jObject);
                } catch (Exception ex) {
                    result.setIsSuccessfulRequest(false);
                    result.setMessage(ex.getMessage());
                    result.setData(null);
                }
                return result;
            }
        };

        RequestResult<JsonObject> result = new RequestResult<JsonObject>();
        try {
            CloseableHttpClient httpClient = createHttpClient();

            HttpPost httpPost = new HttpPost(uriBuilder(uri, parameters));
            if (header != null) {
                for (String key : header.keySet()) {
                    httpPost.addHeader(key, header.get(key));
                }
            }
            httpPost.addHeader("Content-Type", "application/json; charset=UTF-8");

            if (jsonString != null) {
                HttpEntity jsonparam = new StringEntity(jsonString, Charset.forName("UTF-8"));
                httpPost.setEntity(jsonparam);
            }

            result = httpClient.execute(httpPost, responseHandler);
        } catch (Exception ex) {
            result.setIsSuccessfulRequest(false);
            result.setMessage(ex.getMessage());
        }
        return result;
    }

    public byte[] getRequestToImage(String uri, Map<String, String> parameters, Map<String, String> header) throws Exception {
        final HttpClientResponseHandler<byte[]> responseHandler = new HttpClientResponseHandler<byte[]>() {
            @Override
            public byte[] handleResponse(ClassicHttpResponse response)
                    throws HttpException, IOException {
                if (response.getCode() != 200) {
                    throw new IOException("No image here!");
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                response.getEntity().writeTo(baos);
                return baos.toByteArray();
            }
        };

        CloseableHttpClient httpClient = createHttpClient();
        HttpGet httpGet = new HttpGet(uriBuilder(uri, parameters));
        if (header != null) {
            for (String key : header.keySet()) {
                httpGet.addHeader(key, header.get(key));
            }
        }
        return httpClient.execute(httpGet, responseHandler);
    }

    public RequestResult<JsonObject> postRequestFromImage(
        String uri, Map<String, String> parameters, Map<String, String> header, Resource resource, Map<String, String> body
    ) {
        final HttpClientResponseHandler<RequestResult<JsonObject>> responseHandler = new HttpClientResponseHandler<RequestResult<JsonObject>>() {
            @Override
            public RequestResult<JsonObject> handleResponse(ClassicHttpResponse response)
                    throws HttpException, IOException {
                RequestResult<JsonObject> result = new RequestResult<JsonObject>();

                try {
                    result.setStatusCode(response.getCode());
                    result.setMessage("Successful!");
                    result.setIsSuccessfulRequest(true);
                    if (!(result.getStatusCode() >= HttpStatus.SC_SUCCESS
                            && result.getStatusCode() < HttpStatus.SC_REDIRECTION)) {
                        throw new Exception(String.format("API was returned with code %d.", result.getStatusCode()));
                    }

                    String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JsonObject jObject = JsonParser.parseString(responseString).getAsJsonObject();
                    result.setData(jObject);
                } catch (Exception ex) {
                    result.setIsSuccessfulRequest(false);
                    result.setMessage(ex.getMessage());
                    result.setData(null);
                }
                return result;
            }
        };

        RequestResult<JsonObject> result = new RequestResult<JsonObject>();
        try {
            CloseableHttpClient httpClient = createHttpClient();

            HttpPost httpPost = new HttpPost(uriBuilder(uri, parameters));
            if (header != null) {
                for (String key : header.keySet()) {
                    httpPost.addHeader(key, header.get(key));
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
            httpPost.setEntity(multipart);

            result = httpClient.execute(httpPost, responseHandler);
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

    private URI uriBuilder(String uri, Map<String, String> parameters) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(getBaseURL() + uri);
        if (parameters != null) {
            for (String parItem : parameters.keySet()) {
                uriBuilder.addParameter(parItem, parameters.get(parItem));
            }
        }
        return uriBuilder.build();
    }
}
