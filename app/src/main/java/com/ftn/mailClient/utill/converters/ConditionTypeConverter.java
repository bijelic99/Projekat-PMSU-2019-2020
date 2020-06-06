package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Condition;
import com.ftn.mailClient.model.IncomingMailProtocol;

public interface ConditionTypeConverter {
    @TypeConverter
    static Condition fromString(String value){
        return Condition.valueOf(value);
    }

    @TypeConverter
    static String toString(Condition value){
        return value != null ? value.toString() : null;
    }
}
