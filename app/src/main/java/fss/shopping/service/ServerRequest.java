package fss.shopping.service;

import android.util.Log;

import java.io.IOException;
import java.net.CookieHandler;
import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.prefs.Preferences;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Alex on 26.01.2018.
 * Requests to server
 */
public class ServerRequest {

    // private static final String HOST = "http://192.168.42.239:8080/";
    private static final String HOST = "http://192.168.0.80:8080/";

    static OkHttpClient client;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor());
        builder.addInterceptor(new ReceivedCookiesInterceptor());
        client = builder.build();
    }

    public static void registration(Callback callback, String name, String surname, String password1,
                                    String password2, String email, boolean terms) {
       // OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("firstName", name)
                .add("lastName", surname)
                .add("password", password1)
                .add("confirmPassword", password2)
                .add("email", email)
                .add("confirmEmail", email)
                .add("terms", String.valueOf(terms))
                .build();

        Request registrationRequest = new Request.Builder()
                .url(HOST + "registration")
                .post(requestBody)
                .build();
        Call call = client.newCall(registrationRequest);
        call.enqueue(callback);
    }

    public static void login(Callback callback, String email, String password) {
        //OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", email)
                .add("password", password)
                .build();
        Request registrationRequest = new Request.Builder()
                .url(HOST + "login")
                .post(requestBody)
                .build();
        Call call = client.newCall(registrationRequest);
        call.enqueue(callback);
    }

    public static void write(Callback callback)
    {
        //OkHttpClient client = new OkHttpClient();
        Request registrationRequest = new Request.Builder()
                .url(HOST + "add")
                .build();
        Call call = client.newCall(registrationRequest);
        call.enqueue(callback);
    }

    public static void read(Callback callback)
    {

        Request registrationRequest = new Request.Builder()
                .url(HOST + "get")
                .build();
        Call call = client.newCall(registrationRequest);
        call.enqueue(callback);
    }

    public static void check(Callback callback)
    {
        //OkHttpClient client = new OkHttpClient();
        Request registrationRequest = new Request.Builder()
                .url(HOST + "check")
                .build();
        Call call = client.newCall(registrationRequest);
        call.enqueue(callback);
    }

    static HashSet<String> COOKIES = new HashSet<>();

    static Lock lock = new ReentrantLock();

    static void setCookies(HashSet<String> cookies)
    {
        lock.lock();
        COOKIES = cookies;
        lock.unlock();
    }

    static HashSet<String> getCookies()
    {
        lock.lock();
        try {
            return COOKIES;
        } finally {
            lock.unlock();
        }
    }

    static class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }

                setCookies(cookies);
            }

            return originalResponse;
        }
    }

    static class AddCookiesInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = getCookies();
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie); // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }

            return chain.proceed(builder.build());
        }
    }
}
