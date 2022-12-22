package priv.eric.starter.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    public String toString(Object bean) {
        return gson.toJson(bean);
    }

    public <T> T toBean(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }

}
