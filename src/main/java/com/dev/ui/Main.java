package com.dev.ui;

import com.dev.core.Coordinate;
import com.dev.core.PartFinder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import java.util.LinkedList;


public class Main extends Application {
    private Stage window;
    private Scene welcome;
    private Scene board;
    private GridPane gridBoard;
    private Label current;
    private TextField rows;
    private TextField cols;
    private Label status;
    private int row, col;


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setScene(getWelcome());
        window.setTitle("Paper");
        window.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        window.setResizable(true);
        window.setMinWidth(300);
        window.setMinHeight(200);
        window.show();
    }

    private Scene getWelcome() {
        String msg = "Enter digit 1...100";
        rows = new TextField();
        rows.setPromptText(msg);
        cols = new TextField();
        cols.setPromptText(msg);
        cols.setMinWidth(120);
        rows.setMinWidth(120);

        Button createBtn = new Button("Create Paper");
        createBtn.setOnAction(event -> validateAndOpenBoard());
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.add(new Label("Rows:"), 0, 0);
        gridPane.add(new Label("Columns:"), 0, 1);
        gridPane.add(rows, 1, 0);
        gridPane.add(cols, 1, 1);
        gridPane.add(createBtn, 1, 2);
        status = new Label("");
        gridPane.add(status, 0, 3, 2, 1);
        gridPane.setAlignment(Pos.CENTER);
        welcome = new Scene(gridPane, 230, 150);
        welcome.getStylesheets().add("/welcome.css");
        return welcome;
    }

    private void validateAndOpenBoard() {
        status.setTextFill(Color.YELLOW);
        try {
            row = Integer.parseInt(rows.getText().trim());

            try {
                col = Integer.parseInt(cols.getText().trim());
                if ((row > 0 && row < 101) && (col > 0 && col < 101)) {
                    status.setText("");
                    window.setScene(getBoard());
                } else if (!(row > 0 && row < 101))
                    status.setText("Warning: Rows must be 1..100");
                else if (!(col > 0 && col < 101))
                    status.setText("Warning: Columns must be 1..100");

            } catch (NumberFormatException e) {
                status.setText("Warning: Columns isn`t a number!");
            }

        } catch (NumberFormatException e) {
            status.setText("Warning: Rows isn`t a number!");
        }
    }

    private Scene getBoard() {

        HBox hbox = new HBox();
        current = new Label("Current Part: 1");
        current.setFont(Font.font("Times New Roman", FontPosture.REGULAR, 18));
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        Button delBtn = createButton("Delete" , event -> {
            if (event.getTarget() instanceof Button) {
                ((Button) event.getTarget()).setDisable(true);
            }
            gridBoard.setDisable(true);
            rePaint();
        });

        delBtn.getStyleClass().add("button-del");
        Button clearBtn = createButton("Clear",event -> {
            board = null;
            window.setScene(getBoard());
        });

        clearBtn.getStyleClass().add("button-clr");
        Button newPaperBtn = createButton("New", event -> window.setScene(welcome));
        newPaperBtn.getStyleClass().add("button-new");
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));

        hbox.getChildren().addAll(delBtn, clearBtn, newPaperBtn);
        hbox.setAlignment(Pos.CENTER);
        gridBoard = new GridPane();
        gridBoard.setAlignment(Pos.CENTER);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                double cellSize = 25;
                Cell cell = new Cell(cellSize);
                gridBoard.add(cell, j, i);
            }
        }

        gridBoard.setOnMouseClicked(event -> {
            if((event.getTarget()) instanceof Cell) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    ((Cell) event.getTarget()).setFill(Color.SKYBLUE);
                } else if (event.getButton() == MouseButton.SECONDARY)
                    ((Cell) event.getTarget()).setFill(Color.WHITE);
            }
        });

        gridBoard.setOnTouchPressed(event -> {
            if(( event.getTarget()) instanceof Cell) {
                if (((Cell) event.getTarget()).getFill() == Color.WHITE) {
                    ((Cell) event.getTarget()).setFill(Color.SKYBLUE);
                } else {
                    ((Cell) event.getTarget()).setFill(Color.WHITE);
                }
            }
        });
        vbox.setPadding(new Insets(15));
        current.setPadding(new Insets(10));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(hbox, current, gridBoard);
        board = new Scene(vbox);
//        board = new Scene(new ScrollPane(vbox));
        board.getStylesheets().add("/board.css");
        return board;
    }

    private Button createButton (String name, EventHandler<ActionEvent> onAction ) {
        Button button = new Button(name);
        button.setOnAction(onAction);
        return button;
    }

    private void rePaint() {
        int k = 0;
        byte[][] cellsArr = new byte[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (((Cell) gridBoard.getChildren().get(k)).getFill() == Color.SKYBLUE) {
                    (gridBoard.getChildren().get(k)).setDisable(true);
                    (gridBoard.getChildren().get(k)).setVisible(false);
                    cellsArr[i][j] = 0;
                } else {
                    cellsArr[i][j] = 1;
                }
                k++;
            }
        }

        PartFinder finder = new PartFinder(cellsArr, row, col);
        LinkedList<LinkedList<Coordinate>> resultParts;
        resultParts = finder.getResults();

        Color colorParts;
        int x;
        int y;
        int colorCount = 0;
        Rectangle rectangle;
        int colors[][] = {
                {255, 168, 168},
                {138, 46, 230},
                {255, 92, 51},
                {102, 255, 51},
                {209, 25, 117},
                {255, 204, 0},
                {0, 255, 255},
        };
        for (LinkedList<Coordinate> part : resultParts) {
            if (colorCount % colors.length == 0)
                colorCount = 0;
            colorParts = Color.rgb(colors[colorCount][0], colors[colorCount][1], colors[colorCount][2]);
            for (Coordinate coordinate : part) {
                x = coordinate.getX();
                y = coordinate.getY();
                rectangle = (Rectangle) (gridBoard.getChildren().get(col * y + x));
                rectangle.setFill(colorParts);
                gridBoard.getChildren().set(col * y + x, rectangle);
            }
            colorCount++;
        }
        if (resultParts.size() > 0)
            if (resultParts.size() == 1)
                current.setText("Current Part: " + resultParts.size());
            else
                current.setText("Current Parts: " + resultParts.size());
        else
            current.setText("Current Part: 0");

    }
}
