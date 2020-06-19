package com.ftn.mailClient.utill;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.ftn.mailClient.model.Attachment;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public interface UriToAttachmentConverterInterface {
    @RequiresApi(api = Build.VERSION_CODES.O)
    static List<Attachment> uriToAttachment(ContentResolver contentResolver, Uri... uris){
        List<Attachment> attachments = new ArrayList<>();
        for(Uri uri : uris){
            try {
                Attachment attachment = new Attachment();
                String base64 = "";
                String filename = "";
                Cursor cursor = null;
                try {
                    cursor = contentResolver.query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        filename = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
                finally {
                    if(cursor != null) cursor.close();
                }
                InputStream inputStream = contentResolver.openInputStream(uri);
                ByteBuffer byteBuffer = ByteBuffer.allocate(inputStream.available());
                while (inputStream.available()>0){
                    byteBuffer.put((byte) inputStream.read());
                }
                base64 = Base64.getEncoder().encodeToString(byteBuffer.array());
                attachment.setBase64Data(base64);
                attachment.setType(contentResolver.getType(uri));
                attachment.setName(filename);
                attachments.add(attachment);
            }
            catch (Exception e){
                Log.e("attachment", e.getMessage());
            }
        }

        return attachments;
    }
}
