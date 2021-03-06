package com.ftn.mailClient.repository.asyncTasks;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
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

import java.io.*;
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
            if(messageBundle.containsKey("attachments_uri")){
                List<Uri> uriList = (List<Uri>) messageBundle.getSerializable("attachments");
                Message finalMessage = message;
                if(uriList != null)
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
            if(messageBundle.containsKey("attachments_raw")){
                List<Attachment> attachments = (ArrayList<Attachment>) messageBundle.getSerializable("attachments_raw");
                message.setAttachments(attachments);
            }

            try {
                Response<Message> messageResponse = messageApi.sendMessage(message).execute();
                if(messageResponse.isSuccessful()){
                    message = messageResponse.body();

                    messageDao.insert(message);
                    return true;


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

    public static class SaveMessageAttachmentsAsyncTask extends LocalDatabaseCallbackAsyncTask<Long, Void, Integer>{
        Context context;
        public SaveMessageAttachmentsAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Integer> onPostExecuteFunctionFunctionalInterface, Context context) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.context = context;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Integer doInBackground(Long... longs) {
            MessageDao messageDao = localDatabase.messageDao();
            Long messageId = longs[0];
            Message message = messageDao.getMessageByIdNonLive(messageId);

            Integer numberOfSuccessfulSaves = 0;
            for(Attachment attachment : message.getAttachments()){
                File file = new File(context.getExternalFilesDir(null), attachment.getName());
                FileOutputStream fileOutputStream = null;
                try {
                    if(attachment != null && attachment.getBase64Data() != null){
                        file.createNewFile();
                        fileOutputStream = new FileOutputStream(file, true);

                        byte[] decStr = Base64.getDecoder().decode(attachment.getBase64Data());

                        fileOutputStream.write(decStr);
                        fileOutputStream.flush();
                        fileOutputStream.close();



                        numberOfSuccessfulSaves++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    if(fileOutputStream != null) fileOutputStream = null;
                }
            }
            return numberOfSuccessfulSaves;
        }
    }

    public static class DeleteMessageAsyncTask extends LocalDatabaseCallbackAsyncTask<Void, Void, Boolean>{
        private Long messageId;

        public DeleteMessageAsyncTask(LocalDatabase localDatabase, OnPostExecuteFunctionFunctionalInterface<Boolean> onPostExecuteFunctionFunctionalInterface, Long messageId) {
            super(localDatabase, onPostExecuteFunctionFunctionalInterface);
            this.messageId = messageId;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MessageApi messageApi = RetrofitClient.getApi(MessageApi.class);
            MessageDao messageDao = localDatabase.messageDao();
            FolderDao folderDao = localDatabase.folderDao();

            folderDao.deleteFromFolderMessageByMessageId(messageId);
            messageDao.deleteMessageById(messageId);

            try {
                Response<Void> response = messageApi.deleteMessage(messageId).execute();
                if(response.isSuccessful()){
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
