package com.example.comp6442_group_assignment.FakeServerStuff;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FakeClient {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        InputStreamReader inputStreamReader = null;
        OutputStreamWriter outputStreamWriter = null;
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            socket = new Socket("localhost", 6060);
            inputStreamReader = new InputStreamReader(socket.getInputStream());
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());

            bufferedReader = new BufferedReader(inputStreamReader);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            Scanner scanner = new Scanner(System.in); // for user input

            while (true) {
                String input = scanner.nextLine();
                bufferedWriter.write(input);
                bufferedWriter.newLine();
                bufferedWriter.flush();

                String receive = bufferedReader.readLine();
                while (bufferedReader.ready()) {
                    receive += "\n";
                    receive += bufferedReader.readLine();
                }
                System.out.println("Server: " + receive);
                if (input.equals("exit")) {
                    break;
                }
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
