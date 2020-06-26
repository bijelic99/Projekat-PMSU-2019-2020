package com.ftn.mailClient.authorization;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.ftn.mailClient.R;
import com.ftn.mailClient.model.User;
import com.ftn.mailClient.repository.Repository;
import com.ftn.mailClient.repository.UserRepository;

public class UserAccountInfo {
    private static UserAccountInfo userAccountInfo = null;

    private String token;
    private Boolean isLoggedIn;
    private Long userId;
    private Long selectedAccountId;

    private UserAccountInfo() {
        this.token = null;
        this.isLoggedIn = false;
        this.userId = null;
        this.selectedAccountId = null;
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

    public Long getSelectedAccountId() {
        return selectedAccountId;
    }

    public void setSelectedAccountId(Long selectedAccountId, Application application) {
        this.selectedAccountId = selectedAccountId;

        SharedPreferences.Editor editor = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE).edit();
        editor.putLong(application.getString(R.string.user_account_id), selectedAccountId);
        editor.commit();
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
            if(sharedPreferences.contains(context.getString(R.string.user_account_id))){
                this.selectedAccountId = sharedPreferences.getLong(context.getString(R.string.user_account_id), -55L);
            }
            return true;
        }
        return false;
    }

    public LiveData<Boolean> logOut(Application application){
        SharedPreferences.Editor editor = application.getSharedPreferences(application.getString(R.string.user_details_file_key), Context.MODE_PRIVATE).edit();

        UserRepository userRepository = new UserRepository(application);

        editor.remove(application.getString(R.string.user_id));
        editor.remove(application.getString(R.string.user_token));
        editor.remove(application.getString(R.string.user_id));

        editor.commit();

        return userRepository.logout();


    }

    public static UserAccountInfo getUserAccountInfo(){
        if(userAccountInfo == null) userAccountInfo = new UserAccountInfo();
        return userAccountInfo;
    }
}
