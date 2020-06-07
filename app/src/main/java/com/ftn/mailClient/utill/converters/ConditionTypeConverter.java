package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Condition;
import com.ftn.mailClient.model.IncomingMailProtocol;

public class ConditionTypeConverter {
    @TypeConverter
    public static Condition fromString(String value){
        return Condition.valueOf(value);
    }

    @TypeConverter
    public static String toString(Condition value){
        return value != null ? value.toString() : null;
    }
}
