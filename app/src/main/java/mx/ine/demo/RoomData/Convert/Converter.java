package mx.ine.demo.RoomData.Convert;

import android.text.TextUtils;

import androidx.room.TypeConverter;

public class Converter {

    @TypeConverter
    public static String[] fromString(String value) {
        return value != null && value.trim().length() > 0 ? TextUtils.split(value, ";;") : null;
    }

    @TypeConverter
    public static String fromArray(String[] value) {
        return value != null && value.length > 0 ? TextUtils.join(";;", value) : null;
    }
}
