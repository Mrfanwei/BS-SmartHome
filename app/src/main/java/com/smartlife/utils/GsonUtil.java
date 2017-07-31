package com.smartlife.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * 描述：Gson解析的工具类
 * 作者：傅健
 * 创建时间：2017/7/31 15:38
 */
public class GsonUtil {

    private static final Gson gson = new Gson();

    /**
     * 解析json数据
     *
     * @param json  json数据
     * @param clazz 映射的类
     * @param <T>   泛型
     * @return T类元素
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 解析jsonArray，这里使用报错类型转换异常的话，使用下面的
     *
     * @param json json数据
     * @param <T>  泛型
     * @return ArrayList
     */
    public static <T> ArrayList<T> json2BeanArray(String json) {
        ArrayList<T> arrayList;
        arrayList = gson.fromJson(json, new TypeToken<ArrayList<T>>() {
        }.getType());
        return arrayList;
    }

    /**
     * 解析jsonArray，有人推荐这种方式更好
     *
     * @param json  json数据
     * @param clazz 映射的类
     * @param <T>   泛型
     * @return ArrayList
     */
    public static <T> ArrayList<T> json2BeanArrayPlus(String json, Class<T> clazz) {
        ArrayList<T> list = new ArrayList<>();
        try {
            JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement element : jsonArray) {
                list.add(gson.fromJson(element, clazz));
            }
            return list;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
