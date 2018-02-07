package fss.shopping.service;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alex on 07.02.2018.
 */

public class ClientHolder {
    private static final String TAG = ClientHolder.class.getName();
    private static final ClientHolder INSTANCE = new ClientHolder();

    private final OkHttpClient client;
    private final HashSet<String> cookies = new HashSet<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private ClientHolder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor());
        builder.addInterceptor(new ReceivedCookiesInterceptor());
        client = builder.build();
    }

    private OkHttpClient getClient() {
        return client;
    }

    public static OkHttpClient getDefaultClient() {
        return INSTANCE.getClient();
    }

    public static ClientHolder getInstance() {
        return INSTANCE;
    }

    private void updateCookies(HashSet<String> cookies) {
        lock.readLock().lock();
        try {
            if (this.cookies.equals(cookies))
                return;
        } finally {
            lock.readLock().unlock();
        }

        lock.writeLock().lock();
        try {
            if (this.cookies.equals(cookies))
                return;
            Log.i(TAG, "Cookies updating.\nold Cookies: " + this.cookies.toString() +
                    "\nNew cookies: " + cookies.toString());
            this.cookies.clear();
            this.cookies.addAll(cookies);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clearCookies() {
        lock.writeLock().lock();
        try {
            Log.i(TAG, "Clearing application cookies");
            this.cookies.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private HashSet<String> getCookies() {
        lock.readLock().lock();
        try {
            return new HashSet<>(cookies);
        } finally {
            lock.readLock().unlock();
        }
    }

    class ReceivedCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();
                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);
                }
                updateCookies(cookies);
            }
            return originalResponse;
        }
    }

    class AddCookiesInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = getCookies();
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
            }
            return chain.proceed(builder.build());
        }
    }
}
