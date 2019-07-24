package com.example.mvvm_jetpack_lib.utils.net;


import com.example.mvvm_jetpack_lib.ConstantKt;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Data：2018/10/25-11:51
 * Author: cwh
 */
public class RetrofitUtils {
    private static volatile Retrofit mRetrofit = null;
    private static volatile OkHttpClient okHttpClient = null;
    private static ArrayList<Interceptor> interceptors = new ArrayList<>();

    private static CookieJar mCookieJar=null;

    private RetrofitUtils() {
    }

    /**
     * 添加全局拦截器，需要在OkHttpClient创建前调用，建议在application 中调用
     *
     * @param interceptor
     */
    public static void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }


    /**
     * 添加全局拦截器，需要在OkHttpClient创建前调用，建议在application 中调用
     *
     * @param interceptorArrayList
     */
    public static void addInterceptors(ArrayList<Interceptor> interceptorArrayList) {
        interceptors.addAll(interceptorArrayList);
    }


    public static void addCookJar(CookieJar cookieJar){
        mCookieJar=cookieJar;
    }


    private static OkHttpClient newOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (RetrofitUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient().newBuilder()
                            .connectTimeout(ConstantKt.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(ConstantKt.READ_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(ConstantKt.WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true);
//                        .addInterceptor(new ChangeBaseUrlInterceptor())
                    for (Interceptor interceptor : interceptors) {
                        okHttpClientBuilder.addInterceptor(interceptor);
                    }
                    if(mCookieJar!=null){
                        okHttpClientBuilder.cookieJar(mCookieJar);
                    }


                    //添加显示请求和返回信息的logInterceptor
                    okHttpClientBuilder.addInterceptor(new LoggingInterceptor.Builder()
                            .loggable(ConstantKt.isShowLog)
                            .setLevel(Level.BASIC)
                            //add token header
                            //.addHeader(Constant.HEADER, SPUtils.getToken(UWApplication.getApplication()))
                            .log(Platform.INFO)
                            .tag("MVVM")
                            .request("request")
                            .response("response")
                            .build());

                    //处理https协议
                    SSLContext sc;
                    TrustAllManager tm = new TrustAllManager();
                    try {
                        sc = SSLContext.getInstance("TLS");
                        sc.init(null, new TrustManager[]{tm}, new SecureRandom());
                    } catch (Exception e) {
                        e.printStackTrace();
                        sc = null;
                    }
                    if (sc != null) {
                        okHttpClient = okHttpClientBuilder.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()), tm)
                                .hostnameVerifier(new HostnameVerifier() {
                                    @Override
                                    public boolean verify(String hostname, SSLSession session) {
                                        return true;
                                    }
                                })
                                .build();
                    } else {
                        okHttpClient = okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                                .build();
                    }
                }
            }
        }

        return okHttpClient;
    }


    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class Tls12SocketFactory extends SSLSocketFactory {

        private final String[] TLS_SUPPORT_VERSION = {"TLSv1", "TLSv1.1", "TLSv1.2"};

        final SSLSocketFactory delegate;

        public Tls12SocketFactory(SSLSocketFactory delegate) {
            this.delegate = delegate;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return patch(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return patch(delegate.createSocket(address, port, localAddress, localPort));
        }

        private Socket patch(Socket s) {
            if (s instanceof SSLSocket) {
                ((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);
            }
            return s;

        }
    }

    /**
     * 创建Retrofit实例
     *
     * @return
     */
    private static Retrofit newRetrofitInstance() {
        if (mRetrofit == null) {
            synchronized (RetrofitUtils.class) {
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(ConstantKt.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(newOkHttpClient())
                            .build();
                }
            }
        }
        return mRetrofit;
    }


    public static <T> T createServiceInstance(Class<T> clazz) {
        return newRetrofitInstance().create(clazz);
    }
}
