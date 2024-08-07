package game;

import javafx.event.ActionEvent;
import mainmenu.MainMenuView;
import io.deeplay.camp.ModelManager;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GameView {
    @FXML
    public GridPane mainBoard;


    private static GameView instance;


    private int[][] board;
    private int[] score;
    private int player;
    private long blackValidMoves;
    private long whiteValidMoves;

    private String move;

    public static ModelManager modelManager;


    // Конструктор GameView и ее Инстанс

    public GameView(){}

    public static synchronized GameView getInstance(){
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    // Инициализация окна
    @FXML
    public void initialize() {
        modelManager = MainMenuView.getModelManager();
        modelManager.boardModelMethod();
    }

    // Получение обновленной доски
    public void updateBoard(String newBoard) {
        String[] boardRows = newBoard.split("\\s+"); // Разделяем строки доски по пробелам
        int[][] stringToBoard = new int[8][8]; // Создаем массив для доски

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                char cell = boardRows[i * 8 + j].charAt(0); // Получаем символ из строки для каждой ячейки
                if (cell == '.') {
                    stringToBoard[i][j] = 0; // Если символ ".", присваиваем значение 0
                } else if (cell == '0') {
                    stringToBoard[i][j] = 1; // Если символ "0", присваиваем значение 1
                } else if (cell == 'X') {
                    stringToBoard[i][j] = -1; // Если символ "X", присваиваем значение -1
                }
            }
        }
        this.board = stringToBoard;
    }

    public void tileClick(ActionEvent actionEvent) {
        move = "e3";
        modelManager.moveModelMethod(move);
    }
}