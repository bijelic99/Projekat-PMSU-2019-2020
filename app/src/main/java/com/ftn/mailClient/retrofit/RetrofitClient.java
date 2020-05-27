package com.ftn.mailClient.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient <T extends Api>{

    private static final String BASE_URL = "http://192.168.1.250:8080/api/";
    private Retrofit retrofit;
    private Class<T> tClass;

    private RetrofitClient(){
        this.retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
        .build();
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
