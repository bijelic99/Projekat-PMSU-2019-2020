package com.ftn.mailClient.retrofit;

import com.ftn.mailClient.authorization.AuthorizationInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient <T extends Api>{

    public static final String BASE_URL = "http://192.168.0.11:8080/";

    private Class<T> tClass;

    private static Retrofit retrofit;

    private RetrofitClient(){
        if(retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .addInterceptor(new AuthorizationInterceptor())
                    .build();

            this.retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
    }

    /**
     * Pravi retrofit objekat
     * @param apiClass klasa Api-ja koji zelimo da napravimo
     */
    public RetrofitClient(Class<T> apiClass){
        this();
        this.tClass = apiClass;
    }

    public T getApi(){
        return retrofit.create(tClass);
    }

    public static <A extends Api> A getApi(Class<A> apiClass){
        RetrofitClient retrofitClient = new RetrofitClient<A>(apiClass);
        return (A) retrofitClient.getApi();
    }
}
