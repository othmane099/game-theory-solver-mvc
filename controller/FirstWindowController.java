package controller;

import java.util.ArrayList;

import javax.swing.JTextField;

import Model.Player;
import Model.Strategy;

public class FirstWindowController {

    public int M, N;
    public Player p1, p2;

    public FirstWindowController() {
        p1 = new Player(1);
        p2 = new Player(2);
    }

    public void doneAction(String rowCount, String colCount) {

        M = Integer.parseInt(rowCount);
        N = Integer.parseInt(colCount);

        generateStrategiesName(p1, "A", M);
        generateStrategiesName(p2, "B", N);

    }

    public void okAction(ArrayList<JTextField> textFields) {
        readMatrix(textFields);
    }

    private void readMatrix(ArrayList<JTextField> textFieldsList) {
        int cpt = 0;
        // Read the matrix values
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                p1.getStrategies().get(i).getValues().add(Integer.valueOf(textFieldsList.get(cpt++).getText()));
                p2.getStrategies().get(j).getValues().add(Integer.valueOf(textFieldsList.get(cpt++).getText()));
            }
        }
    }

    private void generateStrategiesName(Player p, String name, int size) {
        for (int k = 0; k < size; k++)
            p.getStrategies().add(new Strategy(name + k, new ArrayList<>()));
    }

    public ArrayList<String> getNashEqualibrium2() {
        ArrayList<String> response1 = p1.getBestResponses2(p2, N);
        ArrayList<String> response2 = p2.getBestResponses2(p1, M);

        ArrayList<String> nashList = new ArrayList<>();

        for (int i = 0; i < response1.size(); i++) {
            for (int j = 0; j < response2.size(); j++) {
                if (response1.get(i).equals(response2.get(j))) {
                    nashList.add(response1.get(i));
                }
            }
        }

        return nashList;
    }

    public String displaySecuredStrategy2(String playerName, Strategy s) {
        return new StringBuilder().append("Secured Strategies of ").append(playerName).append(": ").append(s.getName())
                .append(" (").append(s.getMinValue()).append(")").toString();
    }

    public ArrayList<String> getPareto() {
        ArrayList<String> paretoList = new ArrayList<>();
        Strategy s;
        boolean exist = false;
        int x1, x2, y1, y2;

        for (int i = 0; i < M; i++) {
            s = p1.getStrategies().get(i);
            for (int j = 0; j < N; j++) {
                for (int j2 = 0; j2 < M; j2++) {
                    for (int k = 0; k < N; k++) {
                        x1 = s.getValues().get(j);
                        x2 = p1.getStrategies().get(j2).getValues().get(k);
                        y1 = p2.getStrategies().get(j).getValues().get(i);
                        y2 = p2.getStrategies().get(k).getValues().get(j2);

                        if (x1 == x2 && y1 == y2)
                            continue;

                        if (x1 <= x2 && y1 <= y2) {
                            exist = true;
                            break;
                        }
                    }
                    if (exist)
                        break;
                }
                if (!exist) {
                    paretoList.add("(" + s.getName() + "," + p2.getStrategies().get(j).getName() + ")");
                }
                exist = false;

            }
        }
        return paretoList;
    }

    public Player getPlayer1() {
        return p1;
    }

    public Player getPlayer2() {
        return p2;
    }

    public String getDominantMsg(Player p) {

        String label = p.getDominantStrategy()[1].getName() + " is "
                + p.isStrictlyOrWeaklyDominantStrategy(p.getDominantStrategy()[0], p.getDominantStrategy()[1])
                + " dominated by " + p.getDominantStrategy()[0].getName();

        return label;
    }

    public void remove(Player currentPlayer, Player otherPlayer) {
        // Remove dominated strategy
        int indexOfRemovedStrategy = currentPlayer.removeDominatedStrategy(currentPlayer.getDominantStrategy()[1]);
        otherPlayer.removeValuesByIndex(indexOfRemovedStrategy);

        if (currentPlayer == p1)
            M--;
        else
            N--;
    }

    public String showMsg(String label, ArrayList<String> list) {
        // Pareto-Optimum
        StringBuilder paretoString = new StringBuilder(label);
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                paretoString.append(list.get(i)).append(" ");
            }
        } else
            paretoString.append("Doesn't exist !!!");

        return paretoString.toString();
    }

    public String getGameIssueMsg() {
        StringBuilder label = new StringBuilder();
        if (p1.getStrategies().size() == 1 && p2.getStrategies().size() == 1) {

            label.append("Issue de jeu:  (").append(p1.getStrategies().get(0).getValues().get(0)).append(",")
                    .append(p2.getStrategies().get(0).getValues().get(0)).append(")");

        } else
            label.append("Pas de strategie dominant !!!!");

        return label.toString();
    }
}
