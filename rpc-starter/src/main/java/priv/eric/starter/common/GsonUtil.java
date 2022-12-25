package priv.eric.starter.common;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import priv.eric.starter.entity.ServiceInstanceInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class GsonUtil {

    private static final Gson DEFAULT_GSON;
    private static final Gson SNAKE_GSON;

    static {
        DEFAULT_GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
        SNAKE_GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    /**
     * 判断字符串是否是json
     *
     * @return json
     */
    public static boolean isJson(String json) {
        try {
            return JsonParser.parseString(json).isJsonObject();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对象转json字符串
     *
     * @param bean 对象
     * @return json字符串
     */
    public static String toString(Object bean) {
        return DEFAULT_GSON.toJson(bean);
    }

    /**
     * json字符串转对象
     *
     * @param jsonStr json字符串
     * @param clazz   对象类型
     * @param <T>     泛型
     * @return 对象
     */
    public static <T> T toBean(String jsonStr, Class<T> clazz) {
        return DEFAULT_GSON.fromJson(jsonStr, clazz);
    }

    /**
     * 创建json
     *
     * @return JsonBuilder
     */
    public static JsonBuilder create() {
        return new JsonBuilder();
    }

    private static <T> T map2Object(Map<String, Object> map, Class<T> clazz) throws Exception {
        if (map == null) {
            throw new NullPointerException("转换map为空!");
        }
        T t = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(t, map.get(field.getName()));
            }
        }
        return t;
    }

    public static void main(String[] args) {
        String s = GsonUtil.create()
                .put("hello", new ServiceInstanceInfo())
                .put("你好", "你好")
                .put("1", 12345)
                .toString();

        System.out.println(s);

    }

    private static class JsonBuilder {
        private final Map<String, Object> map = new HashMap<>();

        public JsonBuilder put(String key, Object value) {
            map.put(key, value);
            return this;
        }

        public JsonBuilder putAll(Map<String, Object> param) {
            map.putAll(param);
            return this;
        }

        public Map<String, Object> toMap() {
            return this.map;
        }

        public <T> T toBean(Class<T> clazz) throws Exception {
            return map2Object(this.map, clazz);
        }

        public String toString() {
            return GsonUtil.toString(this.map);
        }
    }

}
