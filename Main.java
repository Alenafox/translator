package com.company;

/*
Создать консольное приложение для перевода текста
Запросить у пользователя имя файла и направление перевода
Вариант: передать эти сведения как параметры командной строки String[] args
Обратиться к API переводчика, сделать запрос методом POST
Полученный ответ десереализовать из JSON в объект,
Вывести результат перевода и статус запроса (успешно или нет)
Справочник по API https://docs.microsoft.com/ru-ru/azure/cognitive-services/translator/reference/v3-0-reference
*/

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {  // вместо перехвата исключений используем throws
        String text = "";
        String API_URL = "https://api.cognitive.microsofttranslator.com/translate?api-version=3.0&";
        try {
            File file = new File("text.txt");
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            //text = "[{'Text':'";
            while (line != null) {
                //System.out.println(line);
                text = text + line;
                line = reader.readLine();
            }
            //text = "'}]";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson g = new Gson();
        String POSTData = g.toJson(text);

        System.out.print("Введите язык: ");
        Scanner in = new Scanner(System.in);
        String lang= in.nextLine();

        URL url = new URL(API_URL+"to="+lang);
        System.out.println(url);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String region = "westeurope", key = "82e622ccc27b4ad0af0918182329a742";
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        urlConnection.setRequestProperty("Ocp-Apim-Subscription-Region", region);
        urlConnection.setRequestProperty("Content-Type", POSTData);

        urlConnection.setDoOutput(true);
        OutputStream out = urlConnection.getOutputStream();
        out.write(POSTData.getBytes()); // преобразуем строку в байты и пишем в поток

        Scanner inn = new Scanner(urlConnection.getInputStream());
        if (inn.hasNext()) {
            System.out.println(inn.nextLine());
        } else System.out.println("No output returned");
        urlConnection.disconnect();
    }
}
