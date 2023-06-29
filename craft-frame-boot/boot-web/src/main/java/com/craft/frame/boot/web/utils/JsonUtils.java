package com.craft.frame.boot.web.utils;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author DURR
 * @desc 描述
 * @date 2023/6/25 20:27
 */
public class JsonUtils extends JSONObject {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static <T> T toObj(String str, Class<T> clz) {
        try {
            return JSON_MAPPER.readValue(str, clz);
        } catch (JsonProcessingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String toStr(Object t) {
        try {
            return JSON_MAPPER.writeValueAsString(t);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
