package startscreen;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.awt.Font; // 폰트 설정을 위해 추가
import java.awt.Color;
import settings.LoadData;
import play_screen.PlayFrame;


public class StartMenu extends JFrame {
    private JFrame nextFrame; // 다음 화면에 해당하는 JFrame
    public static int screenRatio = 2; //화면 비율 조절
    public static boolean isColorblindness = false; //색맹모드
    public static String keySetting="ArrowKeys";//키설정
    public static String gameMode="normalMode";
    public static String difficulty="normal";
    public static LoadData loadData = new LoadData();
    public static void setScreenRatio(){
        screenRatio = loadData.loadScreenSize();
    }
    public static void setColorBlindness(){
        isColorblindness = loadData.loadColorBlindMode();
    }
    public static void setGameMode(){
        gameMode = loadData.loadGameMode();
    }
    public static void setDifficutly(){
        difficulty = loadData.loadDifficulty();
    }
    public static void setControlKey(){
        keySetting = loadData.loadKeySettings();
    }
    public StartMenu() {
        setScreenRatio();//화면 비율 조절
        setColorBlindness();
        setControlKey();
        setGameMode();
        setDifficutly();

        setSize(500*screenRatio, 400*screenRatio);
        setTitle("테트리스 게임");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시

        // 패널 생성
        JPanel panel = new JPanel(null); // 레이아웃 매니저를 null로 설정하여 수동으로 위치 및 크기 지정

        // 제목 라벨 생성
        JLabel titleLabel = new JLabel("SE Team9 Tetris", SwingConstants.CENTER); // 중앙 정렬
        titleLabel.setBounds(150*screenRatio, 50*screenRatio, 200*screenRatio, 25*screenRatio); // 제목 라벨 위치 및 크기 지정
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12*screenRatio)); // 폰트 설정
        panel.add(titleLabel); // 패널에 제목 라벨 추가

        // 설정값 표현
        JLabel variableLabel = new JLabel();
        int fontSize = 10*screenRatio;
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        variableLabel.setFont(font);

        String htmlText = "<html>" +
                "<tr><td><strong>화면 비율:</strong></td><td>" + screenRatio + "</td></tr>" +
                "<tr><td><strong>색맹 모드:</strong></td><td>" + isColorblindness + "</td></tr>" +
                "<tr><td><strong>게임 모드:</strong></td><td>" + gameMode + "</td></tr>" +
                "<tr><td><strong>난이도:</strong></td><td>" + difficulty + "</td></tr>" +
                "<tr><td><strong>키 설정:</strong></td><td>" + keySetting + "</td></tr>" +
                "</table>";
        if(keySetting.equals("ArrowKeys")) {
            variableLabel.setText(htmlText+"<table>\n"+
                    "  <tr>\n" +
                    "    <td>↑</td>\n" +
                    "    <td>90도 회전</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td>←</td>\n" +
                    "    <td>좌로 이동</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td>↓</td>\n" +
                    "    <td>아래로 이동</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td>→</td>\n" +
                    "    <td>우로 이동</td>\n" +
                    "  </tr>\n" +
                    "</table></html>");
        } else {
            variableLabel.setText(htmlText+"<table>\n" +
                    "  <tr>\n" +
                    "    <td>W</td>\n" +
                    "    <td>90도 회전</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td>A</td>\n" +
                    "    <td>좌로 이동</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td>S</td>\n" +
                    "    <td>아래로 이동</td>\n" +
                    "  </tr>\n" +
                    "  <tr>\n" +
                    "    <td>D</td>\n" +
                    "    <td>우로 이동</td>\n" +
                    "  </tr>\n" +
                    "</table>\n</html>");
        }
        variableLabel.setBounds(350*screenRatio, 150*screenRatio, 130*screenRatio, 200*screenRatio);

        // 라벨에 테두리 추가
        variableLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // 내용을 가운데 정렬
        variableLabel.setHorizontalAlignment(SwingConstants.CENTER);
        variableLabel.setVerticalAlignment(SwingConstants.CENTER);
        panel.add(variableLabel);
        add(panel);


        // 버튼 생성
        JButton startButton = new JButton("게임 시작");
        JButton settingsButton = new JButton("설정");
        JButton exitButton = new JButton("게임 종료");
        JButton scoreButton = new JButton("스코어보드");

        configureButton(startButton);
        configureButton(settingsButton);
        configureButton(exitButton);
        configureButton(scoreButton);

        // 패널에 버튼 추가 및 위치 설정
        panel.add(startButton);
        startButton.setBounds(200*screenRatio, 225*screenRatio, 100*screenRatio, 25*screenRatio); // 버튼 위치 및 크기 지정 (x, y, width, height)

        panel.add(settingsButton);
        settingsButton.setBounds(200*screenRatio, 260*screenRatio, 100*screenRatio, 25*screenRatio);

        panel.add(exitButton);
        exitButton.setBounds(200*screenRatio, 295*screenRatio, 100*screenRatio, 25*screenRatio);

        panel.add(scoreButton);
        scoreButton.setBounds(200*screenRatio, 330*screenRatio, 100*screenRatio, 25*screenRatio);


        // 프레임에 패널 추가
        add(panel);
        // 게임 시작 버튼 이벤트 처리
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 다음 화면으로 넘어가기 위해 새로운 JFrame 생성
                nextFrame = new PlayFrame(screenRatio, isColorblindness);
                nextFrame.setVisible(true);
                setVisible(false); // 현재 화면 숨기기
            }
        });

        // 설정 버튼 이벤트 처리
        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 다음 화면으로 넘어가기 위해 새로운 JFrame 생성
                nextFrame = new Setting();
                nextFrame.setVisible(true);
                setVisible(false); // 현재 화면 숨기기
            }
        });
        // 스코어보드 버튼 이벤트 처리
        scoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame scoreboardFrame = new JFrame("Scoreboard");
                scoreboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창을 닫으면 종료되지 않고 창만 닫힘
                scoreboardFrame.setSize(500, 300);
                scoreboardFrame.setLocationRelativeTo(null); // 중앙에 위치
                DefaultTableModel model = new DefaultTableModel(); // 모델 생성
                JTable scoreboardTable = new JTable(model); // 테이블 생성
                // 컬럼 추가
                String[] columnNames = {"Rank", "Name", "Score", "Difficulty", "Mode"};
                for (String columnName : columnNames) {
                    model.addColumn(columnName);
                }
                // 스코어보드 데이터 읽어오기
                ShowScoreboard.readScoreboard(model, "scoreboard.txt");
                // 스코어보드 테이블을 스크롤 가능하도록 패널에 추가
                JScrollPane scrollPane = new JScrollPane(scoreboardTable);
                scoreboardFrame.add(scrollPane);
                scoreboardFrame.setVisible(true); // 스코어보드 창 보이기


            }
        });

        // 게임 종료 버튼 이벤트 처리
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 프로그램 종료
                System.exit(0);
            }
        });



        // 방향키 및 엔터키 처리를 위한 설정
        setupDirectionalFocusTraversal(startButton, settingsButton, exitButton, scoreButton);
    }
    private void configureButton(JButton button) {
        Color defaultColor = new Color(230, 230, 230); // 기본 배경색을 밝은 GRAY로 설정
        Color focusedColor = new Color(210, 210, 210); // 포커스가 있을 때의 배경색
        Color hoverColor = defaultColor.brighter(); // 마우스 오버 시 색상, 기본 색상보다 약간 밝게

        button.setBackground(defaultColor); // 버튼의 기본 배경색 설정
        button.setFocusPainted(false);
        button.setBorderPainted(false); // 버튼의 테두리를 그리지 않음

        // 마우스 리스너
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor); // 마우스가 버튼 위에 있을 때
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isFocusOwner()) {
                    button.setBackground(defaultColor); // 마우스가 떠나면 기본 색상으로 복원
                }
            }
        });

        // 포커스 리스너
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                button.setBackground(focusedColor); // 포커스를 얻으면 색상을 변경
            }

            @Override
            public void focusLost(FocusEvent e) {
                button.setBackground(defaultColor); // 포커스를 잃으면 기본 색상으로 복원
            }
        });
    }

    /* switch문으로 키 적용
        private void setupDirectionalFocusTraversal(JButton... buttons) {
            for (int i = 0; i < buttons.length; i++) {
                final int index = i;
                buttons[i].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_UP:
                            case KeyEvent.VK_LEFT:
                                // 위쪽 또는 왼쪽 방향키
                                int prevIndex = (index - 1 + buttons.length) % buttons.length;
                                buttons[prevIndex].requestFocus();
                                break;
                            case KeyEvent.VK_DOWN:
                            case KeyEvent.VK_RIGHT:
                                // 아래쪽 또는 오른쪽 방향키
                                int nextIndex = (index + 1) % buttons.length;
                                buttons[nextIndex].requestFocus();
                                break;
                            case KeyEvent.VK_ENTER:
                                // 엔터키
                                buttons[index].doClick();
                                break;
                        }
                    }
                });
            }
        }

     */
    // if문으로 키 적용
    private void setupDirectionalFocusTraversal(JButton... buttons) {
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            LoadData key = new LoadData();
            buttons[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    if (keyCode == key.getUpKey() || keyCode == key.getLeftKey()) {
                        // 위쪽 방향키
                        int targetIndex = (index - 1 + buttons.length) % buttons.length;
                        buttons[targetIndex].requestFocus();
                    } else if (keyCode == key.getDownKey() || keyCode == key.getRightKey()) {
                        // 아래쪽 방향키
                        int targetIndex = (index + 1) % buttons.length;
                        buttons[targetIndex].requestFocus();
                    } else if (keyCode == KeyEvent.VK_ENTER){
                        buttons[index].doClick();
                    }
                }
            });
        }
    }

}