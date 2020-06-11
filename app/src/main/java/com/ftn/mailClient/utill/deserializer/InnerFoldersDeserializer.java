package com.ftn.mailClient.utill.deserializer;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.ftn.mailClient.model.Folder;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class InnerFoldersDeserializer extends StdDeserializer<Set<Folder>> {
    public InnerFoldersDeserializer(Class<?> vc) {
        super(vc);
    }

    public InnerFoldersDeserializer(JavaType valueType) {
        super(valueType);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Set<Folder> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashSet<Long> ids = objectMapper.readValue(p, objectMapper.getTypeFactory().constructCollectionType(HashSet.class, Long.class));
        return ids.stream().map(aLong -> {
            Folder folder = new Folder();
            folder.setId(aLong);
            return folder;
        }).collect(Collectors.toSet());
    }
}
