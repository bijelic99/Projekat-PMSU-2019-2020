package com.ftn.mailClient.repository.asyncTasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.webkit.MimeTypeMap;
import androidx.annotation.RequiresApi;
import com.ftn.mailClient.dao.AccountDao;
import com.ftn.mailClient.dao.FolderDao;
import com.ftn.mailClient.dao.MessageDao;
import com.ftn.mailClient.database.LocalDatabase;
import com.ftn.mailClient.model.Account;
import com.ftn.mailClient.model.Attachment;
import com.ftn.mailClient.model.Contact;
import com.ftn.mailClient.model.Message;
import com.ftn.mailClient.model.linkingClasses.FolderMessage;
import com.ftn.mailClient.retrofit.MessageApi;
import com.ftn.mailClient.retrofit.RetrofitClient;
import com.ftn.mailClient.utill.OnPostExecuteFunctionFunctionalInterface;
import retrofit2.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class MessageAsyncTasks {
    public static class SendMessageAsyncTask extends LocalDatabaseCallbackAsyncTask<Bundle, Void, Boolean>{
        private Long accountId;
        private ContentResolver contentResolver;

        public SendMessageAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long accountId, ContentResolver contentResolver) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.accountId = accountId;
            this.contentResolver = contentResolver;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Boolean doInBackground(Bundle... bundles) {
            MessageDao messageDao = localDatabase.messageDao();
            FolderDao folderDao = localDatabase.folderDao();
            AccountDao accountDao = localDatabase.accountDao();
            MessageApi messageApi = RetrofitClient.getApi(MessageApi.class);

            Bundle messageBundle = bundles[0];

            Message message = new Message();
            message.setAccount(accountId);
            Account account = accountDao.getAccountById(accountId);
            Contact from = new Contact();
            from.setEmail(account.getUsername());
            message.setFrom(from);
            if(messageBundle.containsKey("toList")){
                message.setTo((List<Contact>) messageBundle.getSerializable("toList"));
            }
            if(messageBundle.containsKey("ccList")){
                message.setCc((List<Contact>) messageBundle.getSerializable("ccList"));
            }
            if(messageBundle.containsKey("bccList")){
                message.setBcc((List<Contact>) messageBundle.getSerializable("bccList"));
            }
            if(messageBundle.containsKey("subject")){
                message.setSubject(messageBundle.getString("subject"));
            }
            if(messageBundle.containsKey("contents")){
                message.setContent(messageBundle.getString("contents"));
            }
            if(messageBundle.containsKey("attachments")){
                List<Uri> uriList = (List<Uri>) messageBundle.getSerializable("attachments");
                Message finalMessage = message;
                uriList.forEach(uri -> {
                    String base64 = "";
                    try {
                        String filename = "";
                        Cursor cursor = null;
                        try {
                            cursor = contentResolver.query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        }finally {
                            if(cursor != null) cursor.close();
                        }
                        InputStream inputStream = contentResolver.openInputStream(uri);
                        ByteBuffer byteBuffer = ByteBuffer.allocate(inputStream.available());
                        while (inputStream.available()>0){
                            byteBuffer.put((byte) inputStream.read());
                        }
                        base64 = Base64.getEncoder().encodeToString(byteBuffer.array());
                        Attachment attachment = new Attachment();
                        attachment.setName(filename);
                        String type = contentResolver.getType(uri);
                        attachment.setType(type);
                        attachment.setBase64Data(base64);
                        finalMessage.getAttachments().add(attachment);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("attachment", e.getMessage());
                    }

                });
            }

            try {
                Response<Message> messageResponse = messageApi.sendMessage(message).execute();
                if(messageResponse.isSuccessful()){
                    message = messageResponse.body();

                    messageDao.insert(message);
                    Long sentFolderId = folderDao.getSentFolderId(accountId);
                    if(message != null && sentFolderId != null) {
                        folderDao.insertMessagesToFolder(new FolderMessage(sentFolderId, message.getId()));
                        return true;
                    }


                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static class AddToDraftsAsyncTask extends LocalDatabaseCallbackAsyncTask<Bundle, Void, Boolean>{
        Long accountId;
        ContentResolver contentResolver;

        public AddToDraftsAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long accountId, ContentResolver contentResolver) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.accountId = accountId;
            this.contentResolver = contentResolver;
        }

        @Override
        protected Boolean doInBackground(Bundle... bundles) {
            MessageDao messageDao = localDatabase.messageDao();
            FolderDao folderDao = localDatabase.folderDao();
            AccountDao accountDao = localDatabase.accountDao();
            MessageApi messageApi = RetrofitClient.getApi(MessageApi.class);

            Bundle messageBundle = bundles[0];

            Message message = new Message();
            message.setAccount(accountId);
            Account account = accountDao.getAccountById(accountId);
            Contact from = new Contact();
            from.setEmail(account.getUsername());
            message.setFrom(from);
            if(messageBundle.containsKey("toList")){
                message.setTo((List<Contact>) messageBundle.getSerializable("toList"));
            }
            if(messageBundle.containsKey("ccList")){
                message.setCc((List<Contact>) messageBundle.getSerializable("ccList"));
            }
            if(messageBundle.containsKey("bccList")){
                message.setBcc((List<Contact>) messageBundle.getSerializable("bccList"));
            }
            if(messageBundle.containsKey("subject")){
                message.setSubject(messageBundle.getString("subject"));
            }
            if(messageBundle.containsKey("contents")){
                message.setContent(messageBundle.getString("contents"));
            }
            if(messageBundle.containsKey("attachments")){
                List<Uri> uriList = (List<Uri>) messageBundle.getSerializable("attachments");
                Message finalMessage = message;
                uriList.forEach(uri -> {
                    String base64 = "";
                    try {
                        String filename = "";
                        Cursor cursor = null;
                        try {
                            cursor = contentResolver.query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        }finally {
                            if(cursor != null) cursor.close();
                        }
                        InputStream inputStream = contentResolver.openInputStream(uri);
                        ByteBuffer byteBuffer = ByteBuffer.allocate(inputStream.available());
                        while (inputStream.available()>0){
                            byteBuffer.put((byte) inputStream.read());
                        }
                        base64 = Base64.getEncoder().encodeToString(byteBuffer.array());
                        Attachment attachment = new Attachment();
                        attachment.setName(filename);
                        String type = contentResolver.getType(uri);
                        attachment.setType(type);
                        attachment.setBase64Data(base64);
                        finalMessage.getAttachments().add(attachment);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("attachment", e.getMessage());
                    }

                });
            }

            try {
                Response<Message> messageResponse = messageApi.addToDrafts(accountId, message).execute();
                if(messageResponse.isSuccessful()){
                    message = messageResponse.body();

                    messageDao.insert(message);
                    Long draftsFolderId = folderDao.getDraftsFolderId(accountId);
                    if(message != null && draftsFolderId != null) {
                        folderDao.insertMessagesToFolder(new FolderMessage(draftsFolderId, message.getId()));
                        return true;
                    }


                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
