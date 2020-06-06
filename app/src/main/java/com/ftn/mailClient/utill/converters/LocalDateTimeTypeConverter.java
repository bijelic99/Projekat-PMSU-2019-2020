package com.ftn.mailClient.utill.converters;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface LocalDateTimeTypeConverter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    static LocalDateTime fromTimestamp(Long timeStamp){
        return timeStamp != null ? LocalDateTime.ofEpochSecond(timeStamp, 0, ZoneOffset.UTC) : null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    static Long toTimestamp(LocalDateTime dateTime){
        return dateTime != null ?   dateTime.toEpochSecond(ZoneOffset.UTC): null;
    }
}
