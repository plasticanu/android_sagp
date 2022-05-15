package com.example.comp6442_group_assignment.FakeServerStuff;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * This class is used to generate the DataStream file later will be input to the Server by Fake client.
 * Normally rerun this would not cause trouble but not recommended for sure.
 */
public class DataStreamFileGenerator {
    @SuppressLint("DefaultLocale")
    public static void main(String[] args) throws IOException {
        String filePath = "app/src/main/assets/DataStream";
        File file = new File(filePath);
        if (file.createNewFile()) {
            System.out.println("File is created!");
        } else {
            System.out.println("File already exists.");
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

        for (int i = 4; i > 0; i--) {
            if (i == 2) { continue; }

            outputStreamWriter.write("li user" + i + " qwerty\n");
            for (int j = 0; j < 100; j++) {
                outputStreamWriter.write("cp testing content" + j + "\n");
                for (int k = 0; k < 5; k++) {
                    outputStreamWriter.write("lp " + String.format("%08d", 5 * j + k) + "\n");
                }
                for (int k = 0; k < 4; k++) {
                    outputStreamWriter.write("cm " + String.format("%08d", (4 * j + k)) + " testing comment" + (4 * j + k) + "\n");
                }
            }
            outputStreamWriter.write("lo\n");
            outputStreamWriter.flush();
        }
        outputStreamWriter.close();
    }
}
