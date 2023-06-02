package web.commons;

import exceptions.UserNaoAutentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import model.User;

public class RequestUtils {

    private static final Jsonb jsonb = JsonbBuilder.create();

    public static <T> T getRequestBody(HttpServletRequest request, Class<T> type) {
        try {
            String json = request.getReader().lines().collect(Collectors.joining());
            return jsonb.fromJson(json, type);
        } catch (Exception e) {
            // Trate qualquer exceção ocorrida durante a desserialização do JSON
            e.printStackTrace();
            return null;
        }
    }
    
    public static String makePutRequest(String login, String password) throws IOException, Exception {
        URL url = new URL("http://localhost:8080/livrariaPoo/api/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        User user = new User(login, password);
        String payload = JsonUtils.toJson(user);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(payload.getBytes("UTF-8"));
        outputStream.close();

        // Realize a chamada HTTP PUT e obtenha o status code da resposta
        int statusCode = connection.getResponseCode();

        // Verifique o status code e tome a ação apropriada
        if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
            // O servidor retornou o status HTTP 403 Forbidden
                // Recupere a mensagem de erro
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
                String line;
                StringBuilder errorMessage = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    errorMessage.append(line);
                }
                reader.close();
                System.out.println("Mensagem de erro: " + errorMessage.toString());
                String errorMsg = errorMessage.toString();
               // errorMsg = errorMsg.replace("{", "").replace("}", "");
                throw new UserNaoAutentException(errorMsg);
            }
        } 

        StringBuilder responseContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();

        return responseContent.toString();
    }
    
    public static String getHttpBody(BufferedReader reader) throws IOException{
        StringBuilder buffer = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
     }    
}