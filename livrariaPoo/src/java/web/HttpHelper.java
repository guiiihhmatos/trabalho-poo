/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;

public class HttpHelper {
    public static String makePutRequest(String login, String password) throws IOException, Exception {
        URL url = new URL("http://localhost:8080/livrariaPoo/api/session");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        String payload = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";

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
                errorMsg = errorMsg.replace("{", "").replace("}", "");
                throw new UsuarioNaoAutenticadoException(errorMsg);
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

