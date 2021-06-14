package view;

import java.util.ArrayList;

import javax.swing.*;

import Model.Player;
import Model.Strategy;

import java.awt.*;

import controller.FirstWindowController;

public class FirstWindow extends JFrame {

    private final JButton doneBtn;
    private final JLabel rowLabel, colLabel, instLabel;
    private final JTextField rowField, colField;
    private final JFrame frame1, frame2, frame3;

    private FirstWindowController fwc;

    public FirstWindow() {

        frame2 = new JFrame("First Window");
        frame3 = new JFrame();

        frame1 = new JFrame();
        frame1.setName("First Window");
        frame1.setBounds(10, 10, 500, 300);
        frame1.setLayout(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setResizable(false);

        instLabel = new JLabel("Please enter your matrix's row and column");
        instLabel.setBounds(50, 50, 450, 30);
        frame1.add(instLabel, 0);

        rowLabel = new JLabel("Row");
        rowLabel.setBounds(100, 100, 100, 30);
        frame1.add(rowLabel, 0);

        rowField = new JTextField("2");
        rowField.setBounds(200, 100, 150, 30);
        frame1.add(rowField, 0);

        colLabel = new JLabel("Column");
        colLabel.setBounds(100, 150, 100, 30);
        frame1.add(colLabel, 0);

        colField = new JTextField("2");
        colField.setBounds(200, 150, 150, 30);
        frame1.add(colField, 0);

        doneBtn = new JButton("ENTER");
        doneBtn.setBounds(300, 200, 100, 30);
        frame1.add(doneBtn, 0);
        doneBtn.addActionListener(e -> {
            fwc = new FirstWindowController();
            fwc.doneAction(rowField.getText(), colField.getText());

            // getContentPane().removeAll();
            // repaint();
            frame1.setVisible(false);
            secondWindow(fwc.M, fwc.N);
        });
        // --------------------------------------------------------

        // ---------------------------------------------------------------

        frame1.setVisible(true);
    }

    void secondWindow(int M, int N) {
        ArrayList<JTextField> textFieldsList = new ArrayList<>();
        frame2.setLayout(new BorderLayout());
        var headPanel = new JPanel();
        var footPanel = new JPanel();

        var okBtn = new JButton("Done");
        okBtn.addActionListener(e -> {
            fwc.okAction(textFieldsList);
            frame2.setVisible(false);
            thirdWindow();
        });

        frame2.add(headPanel, BorderLayout.CENTER);
        frame2.add(footPanel, BorderLayout.SOUTH);

        headPanel.setLayout(new GridLayout(M + 1, N + 1));

        footPanel.setLayout(new FlowLayout());
        footPanel.add(okBtn);

        JPanel jPanel = null;
        JLabel label;

        for (int i = 0; i < M; i++) {
            if (i == 0) {
                headPanel.add(new JLabel(""));
                for (int j = 0; j < N; j++) {
                    label = labelWithColor("A" + j, Color.RED);
                    headPanel.add(label);
                }
            }
            label = labelWithColor("B" + i, Color.BLUE);
            headPanel.add(label);
            for (int j = 0; j < N; j++) {
                jPanel = new JPanel();

                JTextField l = new JTextField();
                l.setColumns(5);

                JTextField l2 = new JTextField();
                l2.setColumns(5);

                jPanel.add(l);
                jPanel.add(l2);

                textFieldsList.add(l);
                textFieldsList.add(l2);

                headPanel.add(jPanel);
            }

            frame2.pack();
            frame2.setLocationRelativeTo(null);
            frame2.setVisible(true);
        }

    }

    private void thirdWindow() {
        Player p1 = fwc.getPlayer1();
        Player p2 = fwc.getPlayer2();

        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        JScrollPane scrollBar = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        frame3.add(scrollBar);
        //=============================================================================
        ArrayList<String> nashList = fwc.getNashEqualibrium2();
        ArrayList<String> paretoList = fwc.getPareto();
        //==============================================================================
        System.out.println("Nash Equalibrium:");
        for (String string : nashList) {
            System.out.println(string);
        }       

        //================================================================================
        // secured strategies
        System.out.println("Security Level: ");
        ArrayList<Strategy> ss1 = fwc.p1.getSecuretyLevel2();
        ArrayList<Strategy> ss2 = fwc.p2.getSecuretyLevel2();
        System.out.println("------------------");
        for (Strategy string : ss1) {
            System.out.println(fwc.displaySecuredStrategy2("A", string));
        }
        System.out.println("-------------------");
        for (Strategy string : ss2) {
            System.out.println(fwc.displaySecuredStrategy2("B", string));
        }
        System.out.println("-----------------");
        //======================================================================================

        displayMatrix(mainPanel);

        Strategy[] strategies;
        strategies = p1.getDominantStrategy();

        while (true) {

            findGameIssue(strategies, p1, p2, mainPanel);

            strategies = p2.getDominantStrategy();

            findGameIssue(strategies, p2, p1, mainPanel);

            strategies = p1.getDominantStrategy();

            if (strategies == null) {
                JLabel l;
                l = labelWithColor(fwc.getGameIssueMsg(), Color.ORANGE);
                mainPanel.add(l);
                break;
            }
        }

        
        mainPanel.add(new JLabel(fwc.showMsg("Nash Equalibrium: ", nashList)));
        mainPanel.add(new JLabel(fwc.showMsg("Pareto-Optimum: ", paretoList)));

        // Security Level
        for (Strategy strategy : ss1)
            mainPanel.add(new JLabel(displaySecuredStrategy2("A", strategy)));

        for (Strategy strategy : ss2)
            mainPanel.add(new JLabel(displaySecuredStrategy2("B", strategy)));

        frame3.pack();
        frame3.setLocationRelativeTo(null);
        frame3.setVisible(true);
    }

    /**
     * Creates JLabel with text lable and color c
     * 
     * @param text The text to be displayed by the label.
     * @param c    The color of the text.
     * @return JLabel
     */
    private JLabel labelWithColor(String text, Color c) {
        JLabel l;
        l = new JLabel(text);
        l.setForeground(c);
        return l;
    }

    private String displaySecuredStrategy2(String playerName, Strategy s){
        return new StringBuilder()
                            .append("Secured Strategies of ")
                            .append(playerName)
                            .append(": ")
                            .append(s.getName())
                            .append(" (")
                            .append(s.getMinValue())
                            .append(")")
                            .toString();
    }


    private void displayMatrix(JPanel mainPanel) {
        StringBuilder label = new StringBuilder();
        JLabel l;
        JPanel panel;
        panel = new JPanel();
        panel.setLayout(new GridLayout(fwc.M + 1, fwc.N + 1));

        mainPanel.add(panel);

        for (int i = 0; i < fwc.M; i++) {
            if (i == 0) {
                panel.add(new Label(""));
                for (int j = 0; j < fwc.N; j++) {
                    l = labelWithColor(fwc.p2.getStrategies().get(j).getName(), Color.RED);
                    panel.add(l);

                }
            }
            l = labelWithColor(fwc.p1.getStrategies().get(i).getName(), Color.BLUE);
            panel.add(l);
            for (int j = 0; j < fwc.N; j++) {
                label.append("(").append(fwc.p1.getStrategies().get(i).getValues().get(j)).append(",")
                        .append(fwc.p2.getStrategies().get(j).getValues().get(i)).append(")");
                l = new JLabel(label.toString());
                label.setLength(0);
                panel.add(l);
            }
        }
    }

    /**
     * 
     * @param strategies    Array with two values {dominant strategy, dominated
     *                      strategy}
     * @param currentPlayer The owner of strategies
     * @param otherPlayer   The other player
     * @param mainPanel     the main panel of window
     */
    private void findGameIssue(Strategy[] strategies, Player currentPlayer, Player otherPlayer, JPanel mainPanel) {
        JLabel l;

        while (strategies != null) {
            // // Display message about dominated strategy
            String label = fwc.getDominantMsg(currentPlayer);

            l = labelWithColor(label, Color.MAGENTA);
            mainPanel.add(l);

            fwc.remove(currentPlayer, otherPlayer);

            displayMatrix(mainPanel);
            strategies = currentPlayer.getDominantStrategy();
        }
    }
}