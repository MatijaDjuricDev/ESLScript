package rs.netplanet.Services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import rs.netplanet.Models.ItemModel;

public class BowtzAPIService {

    int lastRequestId;
    File lastRequestIdFile = new File("requestId.txt");
    File requestLogFile = new File("requestLog.txt");
    String url = "http://49.13.217.99:8085/api/v1/item/sync";
    String secret, storeCodes, scenarioKey;
    BufferedWriter bufferedWriter, lastRequestWriter;

    public BowtzAPIService(String secret, String storeCodes, String scenarioKey) {

        this.secret = secret;
        this.storeCodes = storeCodes;
        this.scenarioKey = scenarioKey;

        if (!requestLogFile.exists())
            try {
                requestLogFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        if (!lastRequestIdFile.exists()) {
            try {
                lastRequestIdFile.createNewFile();
                lastRequestId = 800000;
                lastRequestWriter = new BufferedWriter(new FileWriter(lastRequestIdFile));
                lastRequestWriter.write(String.valueOf(lastRequestId));
                lastRequestWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            readLastRequestId();
        }
    }

    void readLastRequestId() {
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(lastRequestIdFile)));
            String row = bufferedReader.readLine();
            bufferedReader.close();
            if (row != null)
                lastRequestId = Integer.valueOf(row);
            else
                lastRequestId = 80000;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void writeNewRequestId() {
        try {
            lastRequestWriter = new BufferedWriter(new FileWriter(lastRequestIdFile));
            lastRequestWriter.write(String.valueOf(lastRequestId));
            lastRequestWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    JSONObject makeRequestObject(String scenarioKey, String secret, String storeCodes) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("scenarioKey", scenarioKey);
        requestObject.put("secret", secret);
        requestObject.put("storeCodes", storeCodes);
        requestObject.put("requestId", String.valueOf(++lastRequestId));

        return requestObject;
    }

    public boolean sendWholeFile(File file, FileParser fileParser) {

        readLastRequestId();

        JSONObject requestObject = makeRequestObject(scenarioKey, secret, storeCodes);

        JSONArray itemsJson = new JSONArray();

        List<ItemModel> itemsList = fileParser.parseToItemsList(file);

        int itemCount = 1;

        String response;

        for (ItemModel itemModel : itemsList) {
            if (itemCount > 10000) {
                // send last request
                requestObject.put("data", itemsJson);
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                    bufferedWriter.write((new Date()).toString() + "\n" + requestObject.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                response = sendRequest(requestObject);
                itemCount = 1;
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                    bufferedWriter.write((new Date()).toString() + response);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemsJson = new JSONArray();
                // make new request
                requestObject = makeRequestObject(scenarioKey, secret, storeCodes);
            }

            // load items into request
            itemsJson.put(itemModel.parseToJSON());

            itemCount++;
        }

        // if any left, send last request
        if (itemsJson.length() > 0) {
            requestObject.put("data", itemsJson);
            response = sendRequest(requestObject);

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                bufferedWriter.write((new Date()).toString() + response);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeNewRequestId();

        return true;
    }

    String sendRequest(JSONObject requJsonObject) {

        try {

            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            OutputStream outputStream = urlConnection.getOutputStream();
            byte[] input = requJsonObject.toString().getBytes("utf-8");
            outputStream.write(input, 0, input.length);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String row = "";

            while ((row = bufferedReader.readLine()) != null) {
                stringBuilder.append(row.trim());
            }

            return stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public boolean sendItems(List<ItemModel> differentItems) {
        readLastRequestId();

        JSONObject requestObject = makeRequestObject(scenarioKey, secret, storeCodes);

        JSONArray itemsJson = new JSONArray();

        int itemCount = 1;

        String response;

        for (ItemModel itemModel : differentItems) {
            if (itemCount > 10000) {
                // send last request
                requestObject.put("data", itemsJson);
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                    bufferedWriter.write((new Date()).toString() + "\n" + requestObject.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                response = sendRequest(requestObject);
                itemCount = 1;
                try {
                    bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                    bufferedWriter.write((new Date()).toString() + response);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                itemsJson = new JSONArray();
                // make new request
                requestObject = makeRequestObject(scenarioKey, secret, storeCodes);
            }

            // load items into request
            itemsJson.put(itemModel.parseToJSON());

            itemCount++;
        }

        // if any left, send last request
        if (itemsJson.length() > 0) {

            requestObject.put("data", itemsJson);

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                bufferedWriter.write((new Date()).toString() + "\n" + requestObject.toString());
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            response = sendRequest(requestObject);

            try {
                bufferedWriter = new BufferedWriter(new FileWriter(requestLogFile, true));
                bufferedWriter.write((new Date()).toString() + response);
                bufferedWriter.newLine();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        writeNewRequestId();

        System.out.println("Transfer done!");

        return true;
    }
}
