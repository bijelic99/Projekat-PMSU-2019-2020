package com.ftn.mailClient.utill.converters;

import androidx.room.TypeConverter;
import com.ftn.mailClient.model.Condition;
import com.ftn.mailClient.model.Operation;

public class OperationTypeConverter {
    @TypeConverter
    public Operation fromString(String value){
        return Operation.valueOf(value);
    }

    @TypeConverter
    public String toString(Operation value){
        return value != null ? value.toString() : null;
    }
}
