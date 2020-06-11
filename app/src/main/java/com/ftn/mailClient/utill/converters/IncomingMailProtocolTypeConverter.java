package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.IncomingMailProtocol;
import org.jetbrains.annotations.NotNull;

public class IncomingMailProtocolTypeConverter {
    @TypeConverter
    public IncomingMailProtocol fromString(String value){
        return IncomingMailProtocol.valueOf(value);
    }

    @TypeConverter
    public String toString(IncomingMailProtocol value){
        return value != null ? value.toString() : null;
    }
}
