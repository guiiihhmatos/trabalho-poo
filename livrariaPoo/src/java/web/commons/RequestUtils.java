package web.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;
import exceptions.HttpErrorCodeException;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.HttpServletRequest;

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
    
    public static String makeRequest(String pUrl, String payload, String method) throws IOException, Exception {
        String result = null;
        InputStream errorStream = null;
        Integer statusCode = null;
        try {
            URL url = new URL(pUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(payload.getBytes("UTF-8"));
            outputStream.close();

            statusCode = connection.getResponseCode();

            errorStream = connection.getErrorStream();
            
            if(errorStream != null){
            	String contentType = connection.getContentType();
            	 if (contentType.contains("text/html")) {
                 	htmlContent(errorStream, statusCode);
                 }else {
                	 handleHttpError(errorStream, statusCode);
                 }
            } else{
                result = handleHttpSuccess(connection.getInputStream());
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof HttpErrorCodeException){
                throw e;
            }
            if (errorStream != null) {
                handleHttpError(errorStream, statusCode);
            } else {
                final Map retorno = jsonb.fromJson(e.getMessage(), Map.class);
                throw new Exception(retorno.get("message").toString());
            }
        }
        return result;
    }

	private static void htmlContent(InputStream errorStream, Integer statusCode) throws IOException, HttpErrorCodeException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
		String line;
		StringBuilder errorMessage = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			errorMessage.append(line);
		}
		reader.close();

		// Extrair o conteúdo do corpo dentro da tag <body>
		String htmlContent = errorMessage.toString();
		//Document doc = Jsoup.parse(htmlContent);
		//Element bodyElement = doc.body();
		//String bodyContent = bodyElement.html();

		System.out.println("Conteúdo do html: " + htmlContent);
		throw new HttpErrorCodeException(htmlContent);
	}

    private static String handleHttpSuccess(InputStream inputStream) throws IOException {
        StringBuilder responseContent = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();
        return responseContent.toString();
    }

    private static void handleHttpError(InputStream errorStream, Integer responseCode) throws IOException, Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream));
        String line;
        StringBuilder errorMessage = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            errorMessage.append(line);
        }
        reader.close();
        System.out.println(errorMessage.toString());
        String errorMsg = errorMessage.toString();
        System.out.println(errorMsg);
        final Map retorno = jsonb.fromJson(errorMsg, Map.class);
        String result = retorno.get("message").toString();
        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            // Código 404 - Recurso não encontrado
            result = "O recurso solicitado não foi encontrado no serviço REST.";
        } else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR && (result == null || result.trim().isEmpty()) ) {
            // Código 500 - Erro interno do servidor
            result = "O serviço REST encontrou um erro interno.";
        } else {
            // Outros códigos de resposta HTTP e mensagens em branco
            if (result == null || result.trim().isEmpty()) {
                result = "A chamada ao serviço REST retornou um código HTTP: " + responseCode;
            }
        }
        throw new HttpErrorCodeException(result);
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