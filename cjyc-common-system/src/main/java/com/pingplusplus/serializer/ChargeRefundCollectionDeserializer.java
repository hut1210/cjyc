package com.pingplusplus.serializer;

import com.google.gson.*;
import com.pingplusplus.model.ChargeRefundCollection;

import java.lang.reflect.Type;

public class ChargeRefundCollectionDeserializer implements JsonDeserializer<ChargeRefundCollection> {

    public ChargeRefundCollection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return gson.fromJson(json, typeOfT);
    }
}
