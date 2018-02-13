package fss.shopping.web.service;

import fss.shopping.web.ConnectionManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Alex on 07.02.2018.
 */

public abstract class AbstractRequest implements Callback {
    public static final String SESSION = "JSESSIONID";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String HOST = "http://192.168.42.100:8080/";
    //public static final String HOST = "http://192.168.0.80:8080/";

    private final Call call;

    public AbstractRequest(RequestBody requestBody) {
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        requestBuilder.url(HOST + getRequestUrl());
        if (requestBody != null)
            requestBuilder.post(requestBody);
        call = ConnectionManager.getDefaultClient().newCall(requestBuilder.build());
    }

    protected abstract String getRequestUrl();

    public void process() {
        call.enqueue(this);
    }

    public void cancelCall() {
        call.cancel();
    }

    public static abstract class Builder {
        protected FormBody.Builder builder = new FormBody.Builder();

        protected void add(String name, String value) {
            builder.add(name, value);
        }

        public abstract AbstractRequest build();
    }
}
