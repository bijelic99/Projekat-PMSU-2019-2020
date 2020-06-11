package com.ftn.mailClient.utill.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.ftn.mailClient.model.Folder;

import java.io.IOException;

public class ParentFolderDeserializer extends StdDeserializer<Folder> {

    protected ParentFolderDeserializer(Class<?> vc) {
        super(vc);
    }

    protected ParentFolderDeserializer(JavaType valueType) {
        super(valueType);
    }

    @Override
    public Folder deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Long parentFolderId = p.getLongValue();
        Folder parentFolder = new Folder();
        parentFolder.setId(parentFolderId);
        return parentFolder;
    }
}
