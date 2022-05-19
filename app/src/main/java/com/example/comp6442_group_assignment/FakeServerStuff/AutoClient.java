package com.example.comp6442_group_assignment.FakeServerStuff;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class AutoClient {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        InputStreamReader textStreamReader = null;
        BufferedReader textBufferedReader = null;

        try {
            socket = new Socket("localhost", 6080);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            String address = "app/src/main/assets/DataStream";
            File file = new File(address);

            textStreamReader = new InputStreamReader(new FileInputStream(file));
            textBufferedReader = new BufferedReader(textStreamReader);

            while (true) {
                int sleepTime = new Random().nextInt(5000) + 5000;
                Thread.sleep(sleepTime); // sleep for a random time between 5 and 10 seconds

                String input = textBufferedReader.readLine(); // read request command line from file
                if (input == null) { continue; }

                bufferedWriter.write(input);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String receive = bufferedReader.readLine();
                while (bufferedReader.ready()) { // This will handle the case where post content contains multiple lines
                    receive += "\n";
                    receive += bufferedReader.readLine();
                }
                System.out.println("Server: " + receive);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }
    }
}
