package com.ftn.mailClient.utill;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.ftn.mailClient.model.Attachment;

import java.util.ArrayList;
import java.util.List;

public class LoadAttachmentsAsyncTask extends CallbackAsyncTask<Uri, Void, List<Attachment>> {
    private ContentResolver contentResolver;
    public LoadAttachmentsAsyncTask(OnPostExecuteFunctionFunctionalInterface<List<Attachment>> onPostExecuteFunctionFunctionalInterface, ContentResolver contentResolver) {
        super(onPostExecuteFunctionFunctionalInterface);
        this.contentResolver = contentResolver;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected List<Attachment> doInBackground(Uri... uris) {
        return UriToAttachmentConverterInterface.uriToAttachment(contentResolver, uris);
    }
}
