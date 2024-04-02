package play_screen;

import settings.LoadData;
import startscreen.ScoreInput;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class PlayFrame extends JFrame {
    private int screenRatio;
    private boolean isColorBlindness;
    private String gameMode;
    private String difficulty;
    private TetrisPanel gamePanel;
    private ScorePanel scorePanel;
    private NextBlockPanel nextBlockPanel;
    private PausePanel pausePanel;
    private Timer timer;
    private boolean isGameOver = false;
    private boolean isPause = false;

    public PlayFrame(int screenRatio, boolean isColorBlindness) {
        //JFrame 설정.
        this.screenRatio = screenRatio;
        this.isColorBlindness = isColorBlindness;
        this.gameMode = LoadData.loadGameMode();
        this.difficulty = LoadData.loadDifficulty();
        setTitle("Play Frame");
        setSize(screenRatio * 20 * 20, screenRatio * 20 * 20);
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        initUI();
        createTimer();
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                LoadData key = new LoadData();
                if (isGameOver) {
                    // 게임 오버 상황에서의 키 처리
                    endGame();
                } else if (isPause) {
                    int keyCode = e.getKeyCode();
                    if (keyCode == key.getUpKey() || keyCode == key.getDownKey()) {
                        pausePanel.changePoint();
                        if (keyCode == key.getDownKey()) {
                            updateGame();
                        }
                    } else if (keyCode == KeyEvent.VK_ENTER) {
                        if (pausePanel.getCurrPoint() == 0) {
                            isPause = !isPause;
                            pausePanel.setVisible(isPause); // isPause 값에 따라 PausePanel 표시 또는 숨김
                            timer.start();
                        } else if (pausePanel.getCurrPoint() == 1) {
                            endGame();
                        }
                    } else if (keyCode == KeyEvent.VK_ESCAPE) {
                        isPause = !isPause;
                        pausePanel.setVisible(isPause); // isPause 값에 따라 PausePanel 표시 또는 숨김
                        timer.start();
                    }
                } else {
                    int keyCode = e.getKeyCode();
                    if (keyCode == key.getUpKey()) {
                        gamePanel.rotate90();
                    } else if (keyCode == key.getDownKey()) {
                        gamePanel.goDown();
                        updateGame();
                    } else if (keyCode == key.getLeftKey()) {
                        gamePanel.goLeft();
                    } else if (keyCode == key.getRightKey()) {
                        gamePanel.goRight();
                    } else if (keyCode == KeyEvent.VK_ESCAPE) {
                        isPause = !isPause;
                        pausePanel.setVisible(isPause); // isPause 값에 따라 PausePanel 표시 또는 숨김
                        timer.stop();
                    }
                }
            }
        });

        pack();
        setLocationRelativeTo(null); // 화면 가운데에 위치
        setVisible(true);
    }

    private void initUI() {
        setLayout(new GridLayout(1, 2)); // 프레임을 가로로 2등분
        // 왼쪽 패널 : 테트리스 패널
        gamePanel = new TetrisPanel(screenRatio, isColorBlindness);
        add(gamePanel);

        // 오른쪽 패널 (세로로 4등분)
        JPanel rightPanel = new JPanel(new GridLayout(4, 1));

        // ScorePanel 추가
        scorePanel = new ScorePanel(screenRatio, 0);
        rightPanel.add(scorePanel);

        // NextBlockPanel 추가
        nextBlockPanel = new NextBlockPanel(screenRatio, gamePanel.getNextBlock(), isColorBlindness);
        rightPanel.add(nextBlockPanel);

        // 나머지 두 개의 패널은 비워둡니다.
        rightPanel.add(new JPanel());
        rightPanel.add(new JPanel());

        add(rightPanel);
        pausePanel = new PausePanel(screenRatio); // PausePanel 인스턴스 생성
        pausePanel.setSize(200, 100); // 적당한 크기 설정
        pausePanel.setLocation((getWidth() - pausePanel.getWidth()) / 2, (getHeight() - pausePanel.getHeight()) / 2); // 위치 중앙으로 설정
        pausePanel.setVisible(false); // 초기에는 보이지 않게 설정

        getLayeredPane().add(pausePanel, JLayeredPane.POPUP_LAYER); // JLayeredPane에 PausePanel 추가
    }

    private void createTimer() {
        // ActionListener 정의: 점수를 업데이트하고 타이머의 지연 시간 조정
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isGameOver) {
                    timer.stop();
                }
                else {
                    gamePanel.goDown();
                    updateGame();
                    // 타이머의 지연 시간 조정
                    //timer.setDelay(50); //디버그용 딜레이
                    timer.setDelay(1000 - (int)(0.01 * gamePanel.getScore()));
                }
            }
        };

        // 타이머 생성: 초기 지연 시간은 1000ms
        timer = new Timer(1000, actionListener);
    }
    private void updateGame() {
        isGameOver = gamePanel.getIsGameOver();
        scorePanel.updateScore(gamePanel.getScore());
        nextBlockPanel.updateBlock(gamePanel.getNextBlock());
    }

    // 게임 종료 시 호출되는 메서드
    private void endGame() {
        // ScoreInput 창을 띄우고 사용자로부터 이름을 입력받음
        String name = JOptionPane.showInputDialog(this, "이름을 입력하세요:");

        if (name != null && !name.isEmpty()) {
            // 게임의 난이도와 모드를 설정합니다.
            String difficulty = this.difficulty;
            String mode =gameMode;

            // 테이블에 이름과 현재 점수, 난이도, 모드 추가
            new ScoreInput(name, gamePanel.getScore(), difficulty, mode).setVisible(true);
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PlayFrame(2, true).setVisible(true);
            }
        });
    }
}