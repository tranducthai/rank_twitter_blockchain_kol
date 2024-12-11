package csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadCsv {
    public static List<String> readCsvToList(String filePath, String column) { // column A : 1, B : 2, C : 3.....
        List<String> data = new ArrayList<>();
        int columnIndex = Character.toUpperCase(column.charAt(0)) - 'A'; // Tính chỉ số cột từ ký tự

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Tách dòng theo dấu phẩy
                if (columnIndex < values.length) {
                    data.add(values[columnIndex].trim()); // Thêm giá trị từ cột chỉ định
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}