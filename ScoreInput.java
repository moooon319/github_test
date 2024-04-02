package startscreen;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

public class ScoreInput extends JFrame {
    private DefaultTableModel model;
    private JTable scoreboard;

    private String currentDifficulty;
    private String currentMode;

    public ScoreInput(String name, int score, String difficulty, String mode) {
        setTitle("Scoreboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300); // 너비를 조정하여 추가 열에 충분한 공간을 확보합니다.
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        // JTable에 사용할 모델 생성
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // 랭킹, 이름, 점수, 난이도, 모드 컬럼 추가
        String[] columnNames = {"Rank", "Name", "Score", "Difficulty", "Mode"};
        for (String columnName : columnNames) {
            model.addColumn(columnName);
        }

        // JTable 생성 및 각 컬럼 너비 설정
        scoreboard = new JTable(model);
        scoreboard.getColumnModel().getColumn(0).setPreferredWidth(10); // 랭킹 열의 너비를 설정
        scoreboard.getColumnModel().getColumn(1).setPreferredWidth(150); // 이름 열의 너비를 설정
        scoreboard.getColumnModel().getColumn(2).setPreferredWidth(50); // Score 열의 너비를 설정
        scoreboard.getColumnModel().getColumn(3).setPreferredWidth(80); // Difficulty 열의 너비를 설정
        scoreboard.getColumnModel().getColumn(4).setPreferredWidth(80); // Mode 열의 너비를 설정

        // 가운데 정렬을 위한 셀 렌더러 설정
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // 셀 내용을 가운데 정렬로 설정
        for (int i = 0; i < model.getColumnCount(); i++) {
            scoreboard.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        //스코어보드를 패널에 넣고, 프레임 가운데 배치
        JScrollPane scrollPane = new JScrollPane(scoreboard);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        // 파일로부터 데이터를 읽어와 스코어보드에 추가
        addDataFromFile("scoreboard.txt");

        // 현재 플레이어의 정보를 추가
        addDataDescending(name, score, difficulty, mode);

        // 스코어보드를 파일에 저장
        saveDataToFile("scoreboard.txt");

        // 이름과 점수를 파란색으로 강조
        highlightNameAndScore(name, score);

        // 현재 게임 난이도와 모드 설정
        currentDifficulty = difficulty;
        currentMode = mode;
    }


    // 게임 난이도 변경 메서드
    public void changeDifficulty(String newDifficulty) {
        currentDifficulty = newDifficulty;
        // 변경된 난이도에 따라 스코어보드를 업데이트하는 로직 추가
        updateScoreboard();
    }

    // 게임 모드 변경 메서드
    public void changeMode(String newMode) {
        currentMode = newMode;
        // 변경된 모드에 따라 스코어보드를 업데이트하는 로직 추가
        updateScoreboard();
    }

    // 스코어보드 업데이트 메서드
    private void updateScoreboard() {
        // 모든 행을 선택 해제
        scoreboard.clearSelection();
        // 현재 난이도와 모드에 맞는 행만 선택
        for (int i = 0; i < model.getRowCount(); i++) {
            String difficulty = (String) model.getValueAt(i, 3);
            String mode = (String) model.getValueAt(i, 4);
            if (difficulty.equals(currentDifficulty) && mode.equals(currentMode)) {
                scoreboard.addRowSelectionInterval(i, i);
            }
        }
    }

    // 데이터를 테이블에 추가하는 메서드
    private void addData(String name, int score, String difficulty, String mode) {
        int rank = 1; //테이블 데이터 없을 때는 1등으로 들어감
        // 테이블의 마지막 행의 순위를 가져와서 그보다 1 높은 순위를 계산
        if (model.getRowCount() > 0) {
            rank = (int) model.getValueAt(model.getRowCount() - 1, 0) + 1;
        }
        // 테이블에 새로운 행 추가
        model.addRow(new Object[]{rank, name, score, difficulty, mode});
    }

    // 파일로부터 데이터를 읽어와 테이블에 추가하는 메서드
    private void addDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    String difficulty = parts[2].trim();
                    String mode = parts[3].trim();
                    addData(name, score, difficulty, mode);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 테이블의 데이터를 파일에 저장하는 메서드
    private void saveDataToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                String name = (String) model.getValueAt(i, 1);
                int score = (int) model.getValueAt(i, 2);
                String difficulty = (String) model.getValueAt(i, 3);
                String mode = (String) model.getValueAt(i, 4);
                writer.write(name + "," + score + "," + difficulty + "," + mode); // 각 데이터를 쉼표로 구분하여 저장
                writer.newLine(); // 다음 데이터를 위해 개행 추가
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 이름과 점수를 파란색으로 강조하는 메서드
    private void highlightNameAndScore(String name, int score) {
        Timer timer = new Timer(500, new ActionListener() {
            boolean flag = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (flag) {
                    scoreboard.setSelectionForeground(Color.BLUE);
                } else {
                    scoreboard.setSelectionForeground(scoreboard.getForeground());
                }
                flag = !flag;
            }
        });
        timer.setRepeats(true);
        timer.start();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 1).equals(name) && (int) model.getValueAt(i, 2) == score) {
                scoreboard.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    // 새로운 데이터를 삽입할 위치를 찾고, 내림차순으로 유지하면서 테이블에 추가하는 메서드
    private void addDataDescending(String name, int score, String difficulty, String mode) {
        boolean playerFound = false;
        int row = 0;

        // 기존 데이터와 새로운 데이터를 비교하여 적절한 위치를 찾습니다.
        while (row < model.getRowCount() && score <= (int) model.getValueAt(row, 2)) {
            if (name.equals((String) model.getValueAt(row, 1))) {
                // 같은 이름을 가진 플레이어의 기록을 찾았을 경우
                playerFound = true;
                break;
            }
            row++;
        }

        // 같은 이름을 가진 플레이어의 기록이 없는 경우 새로운 행을 추가합니다.
        if (!playerFound) {
            // 새로운 데이터의 랭킹을 설정합니다.
            while (row < model.getRowCount() && score <= (int) model.getValueAt(row, 2)) {
                row++;
            }
            // 테이블에 새로운 행을 추가합니다.
            model.insertRow(row, new Object[]{row + 1, name, score, difficulty, mode});

            // 새로운 데이터의 랭킹을 설정합니다.
            for (int i = row; i < model.getRowCount(); i++) {
                model.setValueAt(i + 1, i, 0); // 랭킹 열 업데이트
            }
        } else {
            // 같은 이름을 가진 플레이어의 기록이 있는 경우, 새로운 행으로 추가합니다.
            model.addRow(new Object[]{row + 1, name, score, difficulty, mode});

            // 테이블의 마지막 행으로 추가되므로 해당 행의 랭킹을 업데이트합니다.
            model.setValueAt(model.getRowCount(), model.getRowCount() - 1, 0);
        }
    }

}