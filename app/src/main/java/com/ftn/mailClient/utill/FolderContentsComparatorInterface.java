package com.ftn.mailClient.utill;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.ftn.mailClient.model.Folder;
import com.ftn.mailClient.model.Message;

public interface FolderContentsComparatorInterface {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int folderContentsComparator(Object o1, Object o2){
        if(o1.getClass() == Message.class){
            if(o2.getClass() == Message.class){
                Message m1 = (Message) o1;
                Message m2 = (Message) o2;
                return m1.getDateTime().isAfter(m2.getDateTime()) ? -1 : m2.getDateTime().isAfter(m1.getDateTime()) ? 1 : 0;
            }
            else if(o2.getClass() == Folder.class){
                return 1;
            }
            else return 0;
        }
        else if(o1.getClass() == Folder.class){
            if(o2.getClass() == Message.class){
                return -1;
            }
            else if(o2.getClass() == Folder.class){
                Folder f1 = (Folder) o1;
                Folder f2 = (Folder) o2;
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
            else return 0;
        }
        else return 0;
    }
}
