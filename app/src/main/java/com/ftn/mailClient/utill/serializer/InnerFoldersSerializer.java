package com.ftn.mailClient.utill.serializer;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ftn.mailClient.model.Folder;

import java.io.IOException;

public class InnerFoldersSerializer extends StdSerializer<Folder> {
    public InnerFoldersSerializer(Class<Folder> t) {
        super(t);
    }

    public InnerFoldersSerializer(JavaType type) {
        super(type);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void serialize(Folder value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeArray(value.getFolders().stream().map(folder -> folder.getId()).mapToLong(id -> id).toArray(), 0, value.getFolders().size());
    }
}
