package com.service.product.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GsonUtil {
    public static final Gson gson = new GsonBuilder().create();

    public static <T> String parseObjToStr(T obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T parseStrToObj(String jsonStr, Class<T> clz) {
        try {
            return gson.fromJson(jsonStr, clz);
        } catch (Exception e) {
            return null;
        }
    }
}
