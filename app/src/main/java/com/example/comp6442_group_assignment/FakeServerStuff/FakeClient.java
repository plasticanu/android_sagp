package com.example.comp6442_group_assignment.FakeServerStuff;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Fake client used for simulating a data stream between the server and another client.
 */
public class FakeClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            socket = new Socket("localhost", 6070);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in); // for user input

            while (true) {
                String input = scanner.nextLine(); // request value will be manually entered here, but automatically generated in the actual client
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
        } catch (IOException e) {
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
