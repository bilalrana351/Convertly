package com.example.convertlyapp;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// This will be the class that will send the images to the python flask server and will then get the converted document as the result of the flask operation
public class SendAndGetImages {
    // TODO : Have to implement this
    // This will be the arraylist of the filePaths of the images in the local pc, these will be passed in as a json object in python and then the python will convert the images to a word
    // Initialize an arraylist of String variables to Send to the python server
    protected static ArrayList<String> imagePaths = new ArrayList<>();

    // This will be the output file
    protected static File outputFile;

    public static void addPath(String path){
        imagePaths.add(path);
    }

    // This will be the method that will send in the present images path to the python server, and will also receive the translated word document
    public static File sendImages(){
        // This will be the method that will send the images to the python server
                try {
            // Create a Url object with the url of your flask server
            URL url = new URL("http://localhost:5000/sendImages"); // replace with your server's URL

            // Open a connection to the server
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set the request method to POST
            conn.setRequestMethod("POST");

            // Enable input and output streams
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Set the headers
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");

            // Create a JSONObject and put the ArrayList of paths into it
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("imagePaths", imagePaths);

            // Write the JSON object to the output stream of the HttpURLConnection
            try(OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonParam.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Get the response code to ensure the request was successful
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK){
                outputFile = new File("output.docx");

                // This will open the input stream
                InputStream inputStream = conn.getInputStream();

                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) != -1){
                    fileOutputStream.write(buffer, 0, length);
                }

                // We will close the input and the output streams
                inputStream.close();
                fileOutputStream.close();
            }

            // Disconnect the connection
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Now we would return the file that was written
        return outputFile;
    }

    // This will be the method that will be used to reset the images paths
    public static void resetImagesPath(){
        imagePaths.clear();
    }
}
