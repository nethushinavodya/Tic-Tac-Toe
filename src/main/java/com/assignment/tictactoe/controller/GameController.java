package com.assignment.tictactoe.controller;

import com.assignment.tictactoe.service.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameController implements BoardUI {

    BoardImpl board;
    AiPlayer ai;
    HumanPlayer human;

    private int xWins = 0;
    private int oWins = 0;

    @FXML
    private Label label1;
    @FXML
    private Label label2;

    public GameController() {
        board = new BoardImpl();
        ai = new AiPlayer(board);
        human = new HumanPlayer(board);
    }

    @FXML
    private GridPane MainGrid;

    @FXML
    void onclick_grid_btn(ActionEvent event) {
        Button button = (Button) event.getSource();
        int row = Integer.parseInt(button.getId().split("")[2]);
        int col = Integer.parseInt(button.getId().split("")[3]);

        if (board.checkWinner() == null && !button.isDisabled()) {
            human.move(row, col);
            updateUi();

            button.setDisable(true);

            if (board.checkWinner() != null) {
                NotifyWinner(board.checkWinner().getWinningPiece());
            } else if (board.isBoardFull()) {
                showAlert("Match Draw");
            } else {
                ai.findBestMove();
                updateUi();

                if (board.checkWinner() != null) {
                    NotifyWinner(board.checkWinner().getWinningPiece());
                } else if (board.isBoardFull()) {
                    showAlert("Match Draw");
                }
            }
        }
    }

    public void updateUi() {
        for (int i = 0; i < board.getPieces().length; i++) {
            for (int j = 0; j < board.getPieces()[i].length; j++) {
                update(i, j, board.getPieces()[i][j]);
            }
        }
    }

    @Override
    public void update(int row, int col, Piece piece) {
        String buttonId = "id" + row + col;
        for (Node node : MainGrid.getChildren()) {
            if (node instanceof Button button && buttonId.equals(node.getId())) {
                if (piece == Piece.X) {
                    button.setText("X");
                    button.setDisable(true);
                } else if (piece == Piece.O) {
                    button.setText("O");
                    button.setDisable(true);
                } else {
                    button.setText("");
                }
                break;
            }
        }
    }

    @Override
    public void NotifyWinner(Piece winner) {
        if (winner == Piece.X) {
            xWins++;
            label1.setText(String.valueOf(xWins));
            showAlert("CONGRADULATIONS! X wins");
        } else if (winner == Piece.O) {
            oWins++;
            label2.setText(String.valueOf(oWins));
            showAlert("AI Wins. BETTER TRY NEXT TIME!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION, message);
        alert.setOnCloseRequest((DialogEvent event) -> {
            board.initializeBoard();
            updateUi();
            resetButtons();
        });
        alert.showAndWait();
    }

    private void resetButtons() {
        for (Node node : MainGrid.getChildren()) {
            if (node instanceof Button button) {
                button.setText("");
                button.setDisable(false);
            }
        }
    }
}
