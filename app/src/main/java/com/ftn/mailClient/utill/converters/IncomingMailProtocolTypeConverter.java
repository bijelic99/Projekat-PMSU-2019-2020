package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.IncomingMailProtocol;
import org.jetbrains.annotations.NotNull;

public interface IncomingMailProtocolTypeConverter {
    @TypeConverter
    static IncomingMailProtocol fromString(String value){
        return IncomingMailProtocol.valueOf(value);
    }

    @TypeConverter
    static String toString(IncomingMailProtocol value){
        return value != null ? value.toString() : null;
    }
}
