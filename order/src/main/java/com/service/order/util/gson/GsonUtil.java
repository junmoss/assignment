package com.service.order.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.service.order.dto.product.ProductDto;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

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

    public static List<ProductDto> parseJsonStrToProducts(String jsonListStr) {
        try {
            return Arrays.asList(gson.fromJson(jsonListStr, ProductDto[].class));
        } catch (JsonParseException jsonParseException) {
            log.error(jsonParseException.getMessage());
            return null;
        }
    }
}
