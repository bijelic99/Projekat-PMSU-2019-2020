package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Condition;
import com.ftn.mailClient.model.Operation;

public interface OperationTypeConverter {
    @TypeConverter
    static Operation fromString(String value){
        return Operation.valueOf(value);
    }

    @TypeConverter
    static String toString(Operation value){
        return value != null ? value.toString() : null;
    }
}
