package com.ftn.mailClient.utill.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ftn.mailClient.model.Folder;

import java.io.IOException;

public class ParentFolderSerializer extends StdSerializer<Folder> {
    public ParentFolderSerializer(Class<Folder> t) {
        super(t);
    }

    public ParentFolderSerializer(JavaType type) {
        super(type);
    }

    @Override
    public void serialize(Folder value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(value.getId());
    }
}
