package com.ftn.mailClient.authorization;


import android.content.Context;
import android.content.SharedPreferences;
import com.ftn.mailClient.R;

public class UserAccountInfo {
    private static UserAccountInfo userAccountInfo = null;

    private String token;
    private Boolean isLoggedIn;
    private Long userId;

    private UserAccountInfo() {
        this.token = null;
        this.isLoggedIn = false;
        this.userId = null;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void login(String token, Long userId, Context context){

        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.user_details_file_key), Context.MODE_PRIVATE).edit();
        editor.putLong(context.getString(R.string.user_id), userId);
        editor.putString(context.getString(R.string.user_token), token);
        editor.commit();

        this.token = token;
        this.userId = userId;
        this.isLoggedIn = true;
    }

    public Boolean loginIfAvailable(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_details_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.contains(context.getString(R.string.user_id)) && sharedPreferences.contains(context.getString(R.string.user_token))){
            this.token = sharedPreferences.getString(context.getString(R.string.user_token), null);
            this.userId = sharedPreferences.getLong(context.getString(R.string.user_id), -55L);
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    public void logOut(Context context){
        //TODO Impl odjavu
    }

    public static UserAccountInfo getUserAccountInfo(){
        if(userAccountInfo == null) userAccountInfo = new UserAccountInfo();
        return userAccountInfo;
    }
}
