/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web.commons;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.List;

public class JsonUtils {

    private static final Jsonb jsonb = JsonbBuilder.create();

    public static String toJson(Object object) {
        return jsonb.toJson(object);
    }
    
     public static String toJson(List<?> list) {
        return jsonb.toJson(list);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return jsonb.fromJson(json, type);
    }
}