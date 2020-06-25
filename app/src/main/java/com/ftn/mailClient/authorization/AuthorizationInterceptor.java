package com.ftn.mailClient.authorization;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthorizationInterceptor implements Interceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder rb = request.newBuilder();
        UserAccountInfo u = UserAccountInfo.getUserAccountInfo();
        if(u.getLoggedIn()){
            rb.addHeader(AUTHORIZATION_HEADER, "Bearer " + u.getToken());
        }
        Response response = chain.proceed(rb.build());

        return response;
    }
}
