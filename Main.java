package com.company;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;
import org.jooq.tools.json.ParseException;
import org.junit.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {  // вместо перехвата исключений используем throws
        String API_URL = "https://api.cognitive.microsofttranslator.com/translate";
        String text = "[{u0027Textu0027:u0027";
        try {
            File file = new File("text.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                text = text + line;
                line = reader.readLine();
            }
            text = text + "u0027}]";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson g = new Gson();
        String POSTData = text.replaceAll("u0027", "\'");
        System.out.println(POSTData);

        System.out.print("Введите язык: ");
        Scanner in = new Scanner(System.in);
        String lang= in.nextLine();

        URL url = new URL(API_URL+"?api-version=3.0&to="+lang);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String region = "westeurope", key = "82e622ccc27b4ad0af0918182329a742" ,content = "application/json";
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Region", region);
        urlConnection.setRequestProperty("Content-Type", content);

        urlConnection.setDoOutput(true); // setting POST method
        // creating stream for writing request
        OutputStream out = urlConnection.getOutputStream();
        out.write(POSTData.getBytes()); // преобразуем строку в байты и пишем в поток

        Scanner inn = new Scanner(urlConnection.getInputStream());
        JSONParser parser = new JSONParser();
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        if (inn.hasNext()) {
            String str = inn.nextLine();
            int len = str.length();
            str = str.substring(1, len-1);

            TranslateResponce p = g.fromJson(str, TranslateResponce.class);
            String out_text = p.translations.get(0).text;
            writer.println(out_text);

        } else System.out.println("No output returned");
        urlConnection.disconnect();
        writer.close();
    }
}
