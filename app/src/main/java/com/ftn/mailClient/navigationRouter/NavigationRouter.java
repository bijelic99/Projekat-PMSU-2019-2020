package com.ftn.mailClient.navigationRouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import androidx.fragment.app.FragmentActivity;
import com.ftn.mailClient.R;
import com.ftn.mailClient.activities.ProfileActivity;
import com.ftn.mailClient.activities.SettingsActivity;
import com.ftn.mailClient.activities.contactsActivity.ContactsActivity;
import com.ftn.mailClient.activities.emailsActivity.EmailsActivity;
import com.ftn.mailClient.activities.foldersActivity.FoldersActivity;
import com.ftn.mailClient.dialogs.ChangeAccountDialog;

public interface NavigationRouter {
    public static boolean routeFromMenuItem(Context context, @NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.profile:{
                Intent intent = new Intent(context, ProfileActivity.class);
                context.startActivity(intent);
                return true;
            }
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
            case R.id.change_account:{
                ChangeAccountDialog changeAccountDialog = new ChangeAccountDialog();
                FragmentActivity activity = (FragmentActivity) context;
                changeAccountDialog.show(activity.getSupportFragmentManager(), "Change account dialog");
                return true;
            }
            case R.id.settings:{
                Intent set = new Intent(context, SettingsActivity.class);
                context.startActivity(set);
                return true;
            }
            default: return false;
        }
    }
}
