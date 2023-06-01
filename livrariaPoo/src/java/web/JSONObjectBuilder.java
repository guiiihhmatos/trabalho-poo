package web;

import org.json.JSONObject;

public class JSONObjectBuilder {

    private JSONObject jsonObject;

    public JSONObjectBuilder() {
        jsonObject = new JSONObject();
    }
    
    public static JSONObject createJSONObject(String json) {
        return new JSONObject(json);
    }

    public static JSONObject createJSONObject(Object bean) {
        return new JSONObject(bean);
    }

    public JSONObjectBuilder withProperty(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    // Outros métodos para configurar o objeto JSON, se necessário

    public JSONObject build() {
        return jsonObject;
    }
}
