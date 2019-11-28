package com.pingplusplus.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.*;
import com.pingplusplus.model.*;
import com.pingplusplus.serializer.*;
import com.pingplusplus.util.PingppSignature;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * extends the abstract class when you need requset anything from ping++
 */
public abstract class APIResource extends PingppObject {
    /**
     * URLEncoder charset
     */
    public static final String CHARSET = "UTF-8";

    private static final String REQUEST_TIME_KEY = "Pingplusplus-Request-Timestamp";

    public static int CONNECT_TIMEOUT = 30;
    public static int READ_TIMEOUT = 80;
    public static int RETRY_MAX = 1;

    /**
     * Http requset method
     */
    protected enum RequestMethod {
        GET, POST, DELETE, PUT
    }

    /**
     * Gson object use to transform json string to resource object
     */
    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Charge.class, new ChargeDeserializer())
            .registerTypeAdapter(RedEnvelope.class, new RedEnvelopeDeserializer())
            .registerTypeAdapter(Transfer.class, new TransferDeserializer())
            .registerTypeAdapter(ChargeRefundCollection.class, new ChargeRefundCollectionDeserializer())
            .registerTypeAdapter(EventData.class, new EventDataDeserializer())
            .registerTypeAdapter(PingppRawJsonObject.class, new PingppRawJsonObjectDeserializer())
            .registerTypeAdapter(SubApp.class, new SubAppDeserializer())
            .create();

    public static Gson getGson() {
        try {
            Class<?> klass = Class.forName("com.pingplusplus.net.AppBasedResource");
            Field field = klass.getField("GSON");
            return (Gson) field.get(klass);
        } catch (ClassNotFoundException e) {
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return GSON;
    }

    public static Class<?> getSelfClass() {
        return APIResource.class;
    }

    /**
     * @param clazz
     * @return className
     */
    protected static String className(Class<?> clazz) {
        String className = clazz.getSimpleName().toLowerCase().replace("$", " ");

        switch (className) {
            case "redenvelope":
                return "red_envelope";
            case "batchrefund":
                return "batch_refund";
            case "batchtransfer":
                return "batch_transfer";
            case "customs":
                return "custom";
            case "cardinfo":
                return "card_info";
            case "assettransaction":
                return "asset_transaction";
            case "balancebonus":
                return "balance_bonuse";
            case "balancetransfer":
                return "balance_transfer";
            case "balancetransaction":
                return "balance_transaction";
            case "coupontemplate":
                return "coupon_template";
            case "batchwithdrawal":
                return "batch_withdrawal";
            case "transactionstatistics":
                return "transaction_statistics";
            case "settleaccount":
                return "settle_account";
            case "subapp":
                return "sub_app";
            case "royalty":
                return "royaltie";
            case "royaltysettlement":
                return "royalty_settlement";
            case "royaltytransaction":
                return "royalty_transaction";
            case "royaltytemplate":
                return "royalty_template";
            case "balancesettlement":
                return "balance_settlement";
            case "subbank":
                return "sub_bank";
            case "splitreceiver":
                return "split_receiver";
            case "splitprofit":
                return "split_profit";
            case "profittransaction":
                return "profit_transaction";
            case "userpic":
                return "users/upload_pic";
            case "contact":
                return "sub_apps/contact";
            default:
                return className;
        }
    }

    /**
     * @param clazz
     * @return singleClassURL
     */
    protected static String singleClassURL(Class<?> clazz) throws InvalidRequestException {
        String className = null;
        Class<?> klass = getSelfClass();
        if (!klass.getSimpleName().equalsIgnoreCase("APIResource")) {
            try {
                Method method = klass.getMethod("className", Class.class);
                className = (String)method.invoke(klass, clazz);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (className == null) {
            className = className(clazz);
        }

        return String.format("%s/v1/%s", Pingpp.getApiBase(), className);
    }

    /**
     * @param clazz
     * @return classURL
     */
    protected static String classURL(Class<?> clazz) throws InvalidRequestException {
        return String.format("%ss", singleClassURL(clazz));
    }

    /**
     * @param clazz
     * @param id
     * @return instanceURL
     * @throws InvalidRequestException
     */
    protected static String instanceURL(Class<?> clazz, String id) throws InvalidRequestException {
        try {
            return String.format("%s/%s", classURL(clazz), urlEncode(id));
        } catch (UnsupportedEncodingException e) {
            throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
        }
    }

    protected static String apiBasePrefixedURL(String url) {
        return String.format("%s%s", Pingpp.getApiBase(), url);
    }

    /**
     * @param str
     * @return urlEncodedString
     * @throws UnsupportedEncodingException
     */
    protected static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, CHARSET);
        }
    }

    /**
     * @param k
     * @param v
     * @return urlEncodedString
     * @throws UnsupportedEncodingException
     */
    private static String urlEncodePair(String k, String v)
            throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    /**
     * @param apiKey
     * @return headers
     */
    static Map<String, String> getHeaders(String apiKey) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Charset", CHARSET);
        headers.put("User-Agent",
                String.format("Pingpp/v1 JavaBindings/%s", Pingpp.VERSION));

        if (apiKey == null) {
            apiKey = Pingpp.apiKey;
        }

        headers.put("Authorization", String.format("Bearer %s", apiKey));
        headers.put("Accept-Language", Pingpp.AcceptLanguage);

        // debug headers
        String[] propertyNames = {"os.name", "os.version", "os.arch",
                "java.version", "java.vendor", "java.vm.version",
                "java.vm.vendor"};
        Map<String, String> propertyMap = new HashMap<String, String>();
        for (String propertyName : propertyNames) {
            propertyMap.put(propertyName, System.getProperty(propertyName));
        }
        propertyMap.put("bindings.version", Pingpp.VERSION);
        propertyMap.put("lang", "Java");
        propertyMap.put("publisher", "Pingpp");
        headers.put("X-Pingpp-Client-User-Agent", getGson().toJson(propertyMap));
        if (Pingpp.apiVersion != null) {
            headers.put("Pingplusplus-Version", Pingpp.apiVersion);
        }
        return headers;
    }

    /**
     * @param url
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     */
    private static HttpURLConnection createPingppConnection(
            String url, String apiKey) throws IOException {
        URL pingppURL = new URL(url);
        HttpURLConnection conn;
        if (pingppURL.getProtocol().equals("https")) {
            conn = (HttpsURLConnection) pingppURL.openConnection();
        } else {
            conn = (HttpURLConnection) pingppURL.openConnection();
        }

        conn.setConnectTimeout(CONNECT_TIMEOUT * 1000);
        conn.setReadTimeout(READ_TIMEOUT * 1000);
        conn.setUseCaches(false);
        for (Map.Entry<String, String> header : getHeaders(apiKey).entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        return conn;
    }

    /**
     * @throws APIConnectionException
     */
    private static void throwInvalidCertificateException() throws APIConnectionException {
        throw new APIConnectionException("Invalid server certificate. You tried to connect to a server that has a revoked SSL certificate, which means we cannot securely send data to that server. ");
    }

    /**
     * @param url
     * @param query
     * @return formatedURL
     */
    private static String formatURL(String url, String query) {
        if (query == null || query.isEmpty()) {
            return url;
        } else {
            // In some cases, URL can already contain a question mark (eg, upcoming invoice lines)
            String separator = url.contains("?") ? "&" : "?";
            return String.format("%s%s%s", url, separator, query);
        }
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     * @throws APIConnectionException
     */
    private static HttpURLConnection createGetConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        String getURL = formatURL(url, query);

        HttpURLConnection conn = createPingppConnection(getURL,
                apiKey);
        conn.setRequestMethod("GET");

        String requestTime = currentTimeString();
        String stringToBeSigned = getRequestURIFromURL(conn.getURL()) + requestTime;
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);
        String signature = generateSign(stringToBeSigned);
        if (signature != null) {
            conn.setRequestProperty("Pingplusplus-Signature", signature);
        }

        return conn;
    }

    private static HttpURLConnection createDeleteConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        String getURL = formatURL(url, query);
        HttpURLConnection conn = createPingppConnection(getURL,
                apiKey);
        conn.setRequestMethod("DELETE");

        String requestTime = currentTimeString();
        String stringToBeSigned = getRequestURIFromURL(conn.getURL()) + requestTime;
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);
        String signature = generateSign(stringToBeSigned);
        if (signature != null) {
            conn.setRequestProperty("Pingplusplus-Signature", signature);
        }

        return conn;
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     * @throws APIConnectionException
     */
    private static HttpURLConnection createPostConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        HttpURLConnection conn = createPingppConnection(url,
                apiKey);

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", String.format(
                "application/json;charset=%s", CHARSET));

        String stringToBeSigned = query;
        stringToBeSigned += getRequestURIFromURL(conn.getURL());
        String requestTime = currentTimeString();
        stringToBeSigned += requestTime;

        String signature = generateSign(stringToBeSigned);
        if (signature != null) {
            conn.setRequestProperty("Pingplusplus-Signature", signature);
        }
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    /**
     * @param url
     * @param query
     * @param apiKey
     * @return HttpURLConnection
     * @throws IOException
     * @throws APIConnectionException
     */
    private static HttpURLConnection createPutConnection(
            String url, String query, String apiKey) throws IOException, APIConnectionException {
        HttpURLConnection conn = createPingppConnection(url,
                apiKey);

        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", String.format(
                "application/json;charset=%s", CHARSET));

        String stringToBeSigned = query;
        stringToBeSigned += getRequestURIFromURL(conn.getURL());
        String requestTime = currentTimeString();
        stringToBeSigned += requestTime;

        String signature = generateSign(stringToBeSigned);
        if (signature != null) {
            conn.setRequestProperty("Pingplusplus-Signature", signature);
        }
        conn.setRequestProperty(REQUEST_TIME_KEY, requestTime);

        OutputStream output = null;
        try {
            output = conn.getOutputStream();
            output.write(query.getBytes(CHARSET));
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return conn;
    }

    /**
     * @param params
     * @return queryString
     * @throws UnsupportedEncodingException
     * @throws InvalidRequestException
     */
    private static String createQuery(Map<String, Object> params)
            throws UnsupportedEncodingException, InvalidRequestException {
        Map<String, String> flatParams = flattenParams(params);
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : flatParams.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(),
                    entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    /**
     * @param params
     * @return JSONString
     */
    private static String createJSONString(Map<String, Object> params) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(params);
    }

    /**
     * @param params
     * @return flattenParams
     * @throws InvalidRequestException
     */
    private static Map<String, String> flattenParams(Map<String, Object> params)
            throws InvalidRequestException {
        if (params == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> flatParams = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map<?, ?>) {
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                Map<?, ?> nestedMap = (Map<?, ?>) value;
                for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
                    flatNestedMap.put(
                            String.format("%s[%s]", key, nestedEntry.getKey()),
                            nestedEntry.getValue());
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if (value instanceof ArrayList<?>) {
                ArrayList<?> ar = (ArrayList<?>) value;
                Map<String, Object> flatNestedMap = new HashMap<String, Object>();
                int size = ar.size();
                for (int i = 0; i < size; i++) {
                    flatNestedMap.put(String.format("%s[%d]", key, i), ar.get(i));
                }
                flatParams.putAll(flattenParams(flatNestedMap));
            } else if ("".equals(value)) {
                throw new InvalidRequestException("You cannot set '" + key + "' to an empty string. " +
                        "We interpret empty strings as null in requests. " +
                        "You may set '" + key + "' to null to delete the property.",
                        key, null);
            } else if (value == null) {
                flatParams.put(key, "");
            } else {
                flatParams.put(key, value.toString());
            }
        }
        return flatParams;
    }


    // represents Errors returned as JSON
    private static class ErrorContainer {
        private APIResource.Error error;
    }

    /**
     *
     */
    private static class Error {
        @SuppressWarnings("unused")
        String type;

        String message;

        String code;

        String param;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            if (null != type && !type.isEmpty()) {
                sb.append("Error type: " + type + "\n");
            }
            if (null != message && !message.isEmpty()) {
                sb.append("\t Error message: " + message + "\n");
            }
            if (null != code && !code.isEmpty()) {
                sb.append("\t Error code: " + code + "\n");
            }

            return sb.toString();
        }
    }

    /**
     * @param responseStream
     * @return responseString
     * @throws IOException
     */
    private static String getResponseBody(InputStream responseStream)
            throws IOException {
        //\A is the beginning of
        // the stream boundary
        String rBody = new Scanner(responseStream, CHARSET)
                .useDelimiter("\\A")
                .next(); //
        responseStream.close();
        return rBody;
    }

    /**
     * @param method
     * @param url
     * @param query
     * @param apiKey
     * @return PingppResponse
     * @throws APIConnectionException
     */
    private static PingppResponse makeURLConnectionRequest(
            APIResource.RequestMethod method, String url, String query,
            String apiKey) throws APIConnectionException {
        HttpURLConnection conn = null;
        try {
            switch (method) {
                case GET:
                    conn = createGetConnection(url, query, apiKey);
                    break;
                case POST:
                    conn = createPostConnection(url, query, apiKey);
                    break;
                case DELETE:
                    conn = createDeleteConnection(url, query, apiKey);
                    break;
                case PUT:
                    conn = createPutConnection(url, query, apiKey);
                    break;
                default:
                    throw new APIConnectionException(
                            String.format("Unrecognized HTTP method %s. ", method));
            }
            // trigger the request
            int rCode = conn.getResponseCode();
            String rBody = null;
            Map<String, List<String>> headers;

            if (rCode >= 200 && rCode < 300) {
                rBody = getResponseBody(conn.getInputStream());
            } else {
                rBody = getResponseBody(conn.getErrorStream());
            }
            headers = conn.getHeaderFields();
            return new PingppResponse(rCode, rBody, headers);

        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format(
                            "IOException during API request to Pingpp (%s): %s "
                                    + "Please check your internet connection and try again. If this problem persists,"
                                    + "you should check Pingpp's service status at https://pingxx.com/status.",
                            Pingpp.getApiBase(), e.getMessage()), e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * @param method
     * @param url
     * @param params
     * @param clazz
     * @param <T>
     * @return PingppObject
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     */
    protected static <T> T request(APIResource.RequestMethod method, String url, Map<String, Object> params, Class<T> clazz) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        return request(method, url, null, params, clazz);
    }

    /**
     * @param method
     * @param url
     * @param apiKey
     * @param params
     * @param clazz
     * @param <T>
     * @return PingppObject
     * @throws AuthenticationException
     * @throws InvalidRequestException
     * @throws APIConnectionException
     * @throws APIException
     * @throws ChannelException
     * @throws RateLimitException
     */
    protected static <T> T request(APIResource.RequestMethod method, String url, String apiKey, Map<String, Object> params, Class<T> clazz) throws AuthenticationException,
            InvalidRequestException, APIConnectionException,
            APIException, ChannelException, RateLimitException {
        apiKey = apiKey != null ? apiKey : Pingpp.apiKey;
        if ((apiKey == null || apiKey.length() == 0)) {
            throw new AuthenticationException(
                    "No API key provided. (HINT: set your API key using 'Pingpp.apiKey = <API-KEY>'. "
                            + "You can generate API keys from the Pingpp web interface. "
                            + "See https://pingxx.com for details.");
        }

        String query = null;
        switch (method) {
            case GET:
            case DELETE:
                try {
                    query = createQuery(params);
                } catch (UnsupportedEncodingException e) {
                    throw new InvalidRequestException("Unable to encode parameters to " + CHARSET, null, e);
                }
                break;
            case POST:
            case PUT:
                query = createJSONString(params);
                break;
        }

        PingppResponse response;
        int retryCount = 0;
        while(true) {
            try {
                // HTTPSURLConnection verifies SSL cert by default
                response = makeURLConnectionRequest(method, url, query, apiKey);
                if (Pingpp.DEBUG) {
                    System.out.println(getGson().toJson(response));
                }

                int rCode = response.getResponseCode();
                String rBody = response.getResponseBody();
                if (rCode < 200 || rCode >= 300) {
                    handleAPIError(rBody, rCode);
                }
                return getGson().fromJson(rBody, clazz);
            } catch (ClassCastException ce) {
                throw ce;
            } catch (ConnectException e) {
                if(retryCount < RETRY_MAX) {
                    retryCount++;
                } else {
                    throw new APIConnectionException(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 错误处理
     *
     * @param rBody
     * @param rCode
     * @throws InvalidRequestException
     * @throws AuthenticationException
     * @throws APIException
     */
    private static void handleAPIError(String rBody, int rCode)
            throws InvalidRequestException, AuthenticationException,
            APIException, ChannelException, RateLimitException, ConnectException {
        Error error = null;
        try {
            error = getGson().fromJson(rBody,
                    ErrorContainer.class).error;
        } catch (JsonSyntaxException e) {
            error = new Error();
            error.message = rBody;
            error.code = String.valueOf(rCode);
        }

        switch (rCode) {
            case 400:
                throw new InvalidRequestException(error.toString(), error.param, null);
            case 404:
                throw new InvalidRequestException(error.toString(), error.param, null);
            case 403:
            case 429:
                throw new RateLimitException(error.toString(), null);
            case 402:
                throw new ChannelException(error.toString(), error.param, null);
            case 401:
                throw new AuthenticationException(error.toString());
            case 502:
                throw new ConnectException(error.toString());
            default:
                throw new APIException(error.toString(), null);
        }
    }

    /**
     * 生成请求签名
     *
     * @param data
     */

    private static String generateSign(String data)
            throws IOException {
        if (Pingpp.privateKey == null) {
            if (Pingpp.privateKeyPath == null) {
                return null;
            }
            FileInputStream inputStream = new FileInputStream(Pingpp.privateKeyPath);
            byte[] keyBytes = new byte[inputStream.available()];
            inputStream.read(keyBytes);
            inputStream.close();
            Pingpp.privateKey = new String(keyBytes, CHARSET);
        }

        return PingppSignature.sign(data, Pingpp.privateKey, CHARSET);
    }

    private static String currentTimeString() {
        Integer requestTime = (int) (System.currentTimeMillis() / 1000);
        return requestTime.toString();
    }

    private static String getRequestURIFromURL(URL url) {
        String path = url.getPath();
        String query = url.getQuery();
        if (query == null) {
            return path;
        }
        return path + "?" + query;
    }
}
