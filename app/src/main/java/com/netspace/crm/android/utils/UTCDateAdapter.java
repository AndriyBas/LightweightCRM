package com.netspace.crm.android.utils;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

/**
 * serialization and deserialization realized the same way as in DefaultDateTypeAdapter, but
 * with adding another date formatter. This realization parse date from response to UTC timezone.
 */
public class UTCDateAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {

    public static final String TAG = UTCDateAdapter.class.getSimpleName();

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();

        try {
            return DateUtils.parseDateUTC(date);
        } catch (ParseException e) {
            Log.d(TAG, "Parse date exception");
            return null;
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return src == null ? null : new JsonPrimitive(DateUtils.formatDateUTC(src));
    }
}