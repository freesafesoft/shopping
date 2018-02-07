package fss.shopping.service;

import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * Created by Alex on 07.02.2018.
 */

public abstract class BaseRequest implements Callback {
    //public static final String HOST = "http://192.168.42.239:8080/";
    public static final String HOST = "http://192.168.0.80:8080/";
    protected final OkHttpClient client = ClientHolder.getDefaultClient();

    public abstract void process();
}
