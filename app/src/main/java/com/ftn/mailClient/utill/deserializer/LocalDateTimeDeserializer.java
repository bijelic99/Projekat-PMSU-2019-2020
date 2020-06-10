package com.ftn.mailClient.utill.deserializer;

import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    protected LocalDateTimeDeserializer(Class<LocalDateTime> vc) {
        super(vc);
    }
    public LocalDateTimeDeserializer(){
        this(null);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String strDateTime = p.getText();
        try {
            return LocalDateTime.parse(strDateTime, dateTimeFormatter);
        }
        catch (Exception e){
            Log.e("deserialization-error", e.getMessage());
        }
        return null;
    }
}
