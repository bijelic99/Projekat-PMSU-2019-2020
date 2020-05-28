package com.ftn.mailClient.navigationRouter;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.contactsActivity.ContactsActivity;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.activities.foldersActivity.FoldersActivity;

public interface NavigationRouter {
    public static boolean routeFromMenuItem(Context context, @NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.emails: {
                Intent intent = new Intent(context, EmailsActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.contacts:{
                Intent intent = new Intent(context, ContactsActivity.class);
                context.startActivity(intent);
                return true;
            }
            case R.id.folders:{
                Intent intent = new Intent(context, FoldersActivity.class);
                context.startActivity(intent);
                return true;
            }
            default: return false;
        }
    }
}
