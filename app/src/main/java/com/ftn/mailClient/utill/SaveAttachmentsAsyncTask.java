package com.ftn.mailClient.utill;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.ftn.mailClient.model.Attachment;

import java.io.FileOutputStream;
import java.util.Base64;

public class SaveAttachmentsAsyncTask extends CallbackAsyncTask<Attachment, Void, Integer> {
    private Context context;
    public SaveAttachmentsAsyncTask(OnPostExecuteFunctionFunctionalInterface<Integer> onPostExecuteFunctionFunctionalInterface, Context context) {
        super(onPostExecuteFunctionFunctionalInterface);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Integer doInBackground(Attachment... attachments) {
        Integer numberOfSuccessfulSaves = 0;
        for(Attachment attachment : attachments){
            FileOutputStream fileOutputStream = null;
            try {
                if(attachment != null && attachment.getBase64Data() != null){
                    fileOutputStream = context.openFileOutput(attachment.getName(), Context.MODE_PRIVATE);
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
