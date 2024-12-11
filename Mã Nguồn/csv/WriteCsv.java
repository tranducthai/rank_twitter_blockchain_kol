package csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteCsv {
    // Ghi ra file
    public static void writeListToCSV(String filePath, String[] list) {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Ghi dữ liệu tách nhau bằng dấu phấy
            writer.write(String.join(",", list));
            writer.newLine(); // Endline

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
