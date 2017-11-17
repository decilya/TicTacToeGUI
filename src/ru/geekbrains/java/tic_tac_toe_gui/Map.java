package ru.geekbrains.java.tic_tac_toe_gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Random;

public class Map extends JPanel {

    static final int GAME_MODE_HVA = 0;
    static final int GAME_MODE_HVH = 1;

    private static final int DOT_EMPTY = 0;
    private static final int DOT_HUMAN = 1;
    private static final int DOT_AI = 2;
    private final static Random RANDOM = new Random();

    private int[][] field;
    private int fieldSizeX;
    private int fieldSizeY;
    private int winLength;

    private int cellWidth;
    private int cellHeight;

    private boolean initialized;

    /**
     * statusGame = 0 Игра не начата
     * statusGame = 1 Игра идет
     * statusGame = 2 Игра закончена
     */
    private int statusGame;
    private static final int GAME_STATUS_NULL = 0;
    private static final int GAME_STATUS_START = 1;
    private static final int GAME_STATUS_FINAL = 2;

    /**
     * stateGameOver = 0 ничья
     * stateGameOver = 1 Победил игрок
     * stateGameOver = 2 Победил компьютер
     */
    private int stateGameOver;
    private static final int STATE_DRAW = 0;
    private static final int STATE_HUMAN_WIN = 1;
    private static final int STATE_AI_WIN = 2;
    private static final String MSG_DRAW = "Ничья!";
    private static final String MSG_HUMAN_WIN = "Победил игрок!";
    private static final String MSG_AI_WIN = "Победил компьютер!";

    Map() {
        setBackground(Color.GREEN);
        this.initialized = false;
        this.statusGame = GAME_STATUS_NULL;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void update(MouseEvent e) {

        if (this.statusGame != GAME_STATUS_START) return;

        int cellX = (e.getX() / this.cellWidth);
        int cellY = (e.getY() / this.cellHeight);

        if ((!isEmptyCell(cellX, cellY)) || (!isValidCell(cellX, cellY))) return;

        field[cellX][cellY] = DOT_HUMAN;

        if (check(DOT_HUMAN)) {
            System.out.println("Победа!");
            this.statusGame = GAME_STATUS_FINAL;
            this.stateGameOver = STATE_HUMAN_WIN;
            repaint();
            return;
        }

        if (isFieldFull()) {
            System.out.println("Ничья!");
            this.stateGameOver = STATE_DRAW;
            this.statusGame = GAME_STATUS_FINAL;
            repaint();
            return;
        }

        aiTurn();
        repaint();

        if (check(DOT_AI)) {
            System.out.println("Поражение....");
            this.stateGameOver = STATE_AI_WIN;
            repaint();
            return;
        }

        if (isFieldFull()) {
            System.out.println("Ничья!");
            this.stateGameOver = STATE_DRAW;
            this.statusGame = GAME_STATUS_FINAL;
            repaint();
            return;
        }

        repaint();
    }

    void render(Graphics g) {

        if (!this.initialized) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        this.cellWidth = panelWidth / this.fieldSizeX;
        this.cellHeight = panelHeight / this.fieldSizeY;

        g.setColor(Color.BLACK);

        for (int i = 0; i < this.fieldSizeY; i++) {
            int y = i * this.cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int i = 0; i < this.fieldSizeX; i++) {
            int x = i * this.cellWidth;
            g.drawLine(x, 0, x, panelWidth);
        }

        if (this.initialized) {
            for (int y = 0; y < this.fieldSizeY; y++) {
                for (int x = 0; x < this.fieldSizeX; x++) {

                    if (isEmptyCell(x, y)) continue;

                    if (this.field[x][y] == DOT_HUMAN) {
                        g.setColor(Color.BLUE);
                    } else if (this.field[x][y] == DOT_AI) {
                        g.setColor(Color.RED);
                    } else {
                        throw new RuntimeException("Цвет игрока не опредлен");
                    }

                    g.fillOval(x * cellWidth + 5, y * cellHeight + 5, cellWidth - 10, cellHeight - 10);
                }
            }
        }

        if (statusGame == GAME_STATUS_FINAL) {
            showMessageGameOver(g);
        }
    }

    void clearField() {
        for (int y = 0; y < this.fieldSizeY; y++) {
            for (int x = 0; x < this.fieldSizeX; x++) {
                this.field[y][x] = DOT_EMPTY;
            }
        }
    }

    private boolean isEmptyCell(int x, int y) {
        return this.field[x][y] == DOT_EMPTY;
    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && y >= 0 && x < this.fieldSizeX && y < this.fieldSizeY;
    }

    void startNewGame(int mode, int sizeFieldX, int sizeFieldY, int winLength) {
        // сборщик мусора
        System.gc();

        this.fieldSizeX = sizeFieldX;
        /** @todo сделать разными */
        this.fieldSizeY = sizeFieldY;
        this.winLength = winLength;
        this.field = new int[this.fieldSizeY][this.fieldSizeX];

        clearField();

        this.initialized = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });

        this.statusGame = GAME_STATUS_START;
        repaint();
    }

    private void aiTurn() {
        int x;
        int y;
        do {
            x = RANDOM.nextInt(this.fieldSizeX);
            y = RANDOM.nextInt(this.fieldSizeY);
        } while (!isEmptyCell(x, y));

        field[x][y] = DOT_AI;
    }

    private boolean check(int c) {

        int resultTmp = 1;

        for (int i = 0; i < this.fieldSizeX; i++) {

            resultTmp = 1;
            for (int j = 0; j < this.fieldSizeX - 1; j++) {
                if ((this.field[i][j] == this.field[i][j + 1]) && (c == this.field[i][j])) {
                    resultTmp++;
                    if (resultTmp == winLength)
                        return true;
                } else {
                    resultTmp = 1;
                }
            }
        }

        for (int i = 0; i < this.fieldSizeY; i++) {

            resultTmp = 1;
            for (int j = 0; j < this.fieldSizeY - 1; j++) {
                if (field[j][i] == field[j + 1][i] && (c == field[j][i])) {
                    resultTmp++;
                    if (resultTmp == winLength)
                        return true;
                } else {
                    resultTmp = 1;
                }
            }
        }

        for (int i = 0; i < this.fieldSizeY; i++) {

            resultTmp = 1;
            for (int j = 1; j < this.fieldSizeX; j++) {
                if (field[i][i] == field[j][j] && (c == field[i][i])) {
                    resultTmp++;
                    if (resultTmp == winLength)
                        return true;
                } else {
                    resultTmp = 1;
                }
            }
        }

        int tmpResult = 0;

        for (int i = 0; i < this.fieldSizeX; i++) {
            for (int j = 1; j < this.fieldSizeY; j++) {
                tmpResult = cc(i, j, c, 1);
                if (tmpResult == winLength) {
                    return true;
                }
            }
        }

        return false;
    }

    private int cc(int i, int j, int c, int resultTmp) {
        if (field[i][j] != c) return resultTmp;

        int tmp = i + j;

        int y = i + 1;
        int x = j - 1;

        if (y + x != tmp) return resultTmp;

        if ((y < fieldSizeY) && (x >= 0) && (y >= 0) && (x < fieldSizeX)) {

            if ((field[i][j] == field[y][x]) && (field[i][j] == c)) {

                resultTmp++;

                if (resultTmp == winLength) {
                    return resultTmp;
                } else {
                    return cc(y, x, c, resultTmp);
                }

            } else {
                return resultTmp;
            }
        }

        return resultTmp;
    }

    private boolean isFieldFull() {
        for (int i = 0; i < this.fieldSizeX; i++) {
            for (int j = 0; j < this.fieldSizeY; j++) {
                if (field[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    void showMessageGameOver(Graphics g) {
        Font font = new Font("Times new roman", Font.BOLD, 48);
        int labelHeight = getHeight() / 2;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(font);
        switch (stateGameOver) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, labelHeight);
                break;
            case STATE_HUMAN_WIN:
                g.drawString(MSG_HUMAN_WIN, 70, labelHeight);
                break;
            case STATE_AI_WIN:
                g.drawString(MSG_AI_WIN, 20, labelHeight);
                break;
            default:
                throw new RuntimeException("unexpected GameOver Status: " + stateGameOver);
        }
    }


}
