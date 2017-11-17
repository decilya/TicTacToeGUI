package ru.geekbrains.java.tic_tac_toe_gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionsWindow extends JFrame {
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 230;
    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;

    private GameWindow gameWindow;
    private JRadioButton humVSAI;
    private JRadioButton humVShum;
    private JSlider slideFeildSize;
    private JSlider slideWinLength;

    OptionsWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int) gameWindowBounds.getCenterX() - WINDOW_WIDTH / 2;
        int posY = (int) gameWindowBounds.getCenterY() - WINDOW_HEIGHT / 2;
        setLocation(posX, posY);
        setResizable(false);
        setTitle("Создание новой игры");
        setLayout(new GridLayout(10, 1));

        addGameControlsMode();
        addGameControlsField();

        JButton btnStartGame = new JButton("Запустить игру");
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStartGameClick();
            }
        });
        add(btnStartGame);
    }

    private void addGameControlsMode() {
        add(new JLabel("Выберите режим:"));
        humVSAI = new JRadioButton("Человек VS Компьютер");
        humVShum = new JRadioButton("Человек VS Человек");

        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(humVSAI);
        gameMode.add(humVShum);

        humVSAI.setSelected(true);
        add(humVSAI);
        add(humVShum);
    }

    private void addGameControlsField() {
        final String FIELD_SIZE_PREFIX = "Размер поля равен ";
        JLabel lblFieldSize = new JLabel(FIELD_SIZE_PREFIX + MIN_FIELD_SIZE);
        slideFeildSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        slideFeildSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = slideFeildSize.getValue();
                lblFieldSize.setText(FIELD_SIZE_PREFIX + currentValue);
                slideWinLength.setMaximum(currentValue);
            }
        });

        final String WIN_LEN_PREFIX = "Выигрышная длина равна ";
        JLabel lblWimLength = new JLabel(WIN_LEN_PREFIX + MIN_WIN_LENGTH);
        slideWinLength = new JSlider(MIN_WIN_LENGTH, MIN_WIN_LENGTH, MIN_WIN_LENGTH);
        slideWinLength.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lblWimLength.setText(WIN_LEN_PREFIX + slideWinLength.getValue());
            }
        });

        add(new JLabel("Выберите размер поля: "));
        add(lblFieldSize);
        add(slideFeildSize);

        add(new JLabel("Выберите необходимую длинну для победы: "));
        add(lblWimLength);
        add(slideWinLength);
    }

    //int mode, int sizeFieldX, int sizeFieldY, int winLength
    private void btnStartGameClick() {
        int gameMode;
        if (humVSAI.isSelected())
            gameMode = Map.GAME_MODE_HVA;
        else if (humVShum.isSelected())
            gameMode = Map.GAME_MODE_HVH;
        else
            throw new RuntimeException("Что-то пошло не так... не выбран ни один из режимов игры.");

        int sizeFieldX = slideFeildSize.getValue();
        int winLen = slideWinLength.getValue();

        gameWindow.startNewGame(gameMode, sizeFieldX, sizeFieldX, winLen);
        setVisible(false);
    }
    
}
