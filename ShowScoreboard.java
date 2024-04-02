package startscreen;

import javax.swing.table.DefaultTableModel;
import java.io.*;

public class ShowScoreboard {
    public static void readScoreboard(DefaultTableModel model, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    String difficulty = parts[2].trim();
                    String mode = parts[3].trim();
                    addData(model, name, score, difficulty, mode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addData(DefaultTableModel model, String name, int score, String difficulty, String mode) {
        int rank = 1;
        if (model.getRowCount() > 0) {
            rank = (int) model.getValueAt(model.getRowCount() - 1, 0) + 1;
        }
        model.addRow(new Object[]{rank, name, score, difficulty, mode});
    }

    public class ClearScoreboard {
        public static void clear(String filePath) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // 파일 내용을 모두 지움
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
