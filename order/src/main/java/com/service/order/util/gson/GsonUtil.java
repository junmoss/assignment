package com.service.order.util.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.service.order.dto.product.ProductDto;
import com.service.order.error.exception.parsing.ParsingServiceException;
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
            throw new ParsingServiceException("데이터 파싱 처리 작업에 실패하였습니다.");
        }
    }
}
