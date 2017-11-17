package ru.geekbrains.java.tic_tac_toe_gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {

    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_POS_X = 650;
    private static final int WINDOW_POS_Y = 250;

    private ru.geekbrains.java.tic_tac_toe_gui.game.Map map;
    private ru.geekbrains.java.tic_tac_toe_gui.game.OptionsWindow optionsWindow;

    GameWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setTitle("Крестики-нолики");

        // отдадим расположение кнопок компановщику (BorderLayout() - это дефолтное значение, если же писать null
        // то тогда будет необходимо вводить координаты вручную)
        setLayout(new BorderLayout());

        JButton btnNewGame = new JButton("Новая игра");
        JButton btnExitGame = new JButton("Выход из игры");

        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optionsWindow.setVisible(true);
            }
        });

        btnExitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        map = new ru.geekbrains.java.tic_tac_toe_gui.game.Map();
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1, 2));
        panelBottom.add(btnNewGame);
        panelBottom.add(btnExitGame);
        add(map, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);
        optionsWindow = new ru.geekbrains.java.tic_tac_toe_gui.game.OptionsWindow(this);
        setVisible(true);
    }

    void startNewGame(int mode, int sizeFieldX, int sizeFieldY, int winLength) {
        map.startNewGame(mode, sizeFieldX, sizeFieldY, winLength);
    }
}
