package com.ftn.mailClient.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.ftn.mailClient.R;
import com.ftn.mailClient.dao.*;
import com.ftn.mailClient.model.*;

@Database(entities = {Account.class, Attachment.class, Contact.class, Folder.class, Message.class, Photo.class, Rule.class, Tag.class, User.class}, version = 1, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase instance;

    public abstract AccountDao accountDao();
    public abstract AttachmentDao attachmentDao();
    public abstract ContactDao contactDao();
    public abstract FolderDao folderDao();
    public abstract MessageDao messageDao();
    public abstract PhotoDao photoDao();
    public abstract RuleDao ruleDao();
    public abstract TagDao tagDao();
    public abstract UserDao userDao();

    public static synchronized LocalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "local_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
