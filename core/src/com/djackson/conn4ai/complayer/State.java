package com.djackson.conn4ai.complayer;


import com.djackson.conn4ai.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class State {
    // holds current 6x7 game board array to look at
    public int[][] sBoard;
    // helps with checking row to look at 0-5,
    // 5 = empty column w/ space open at 5, 0 = almost full column w/ space open at 0
    public int[] moveIndex;
    private int[] scores;
    private int score;
    public static boolean TIE = false;
    Random tem;

    // orig/default state construct
    public State() {
        moveIndex = new int[7];
        sBoard = new int[6][7];
        for (int i=0; i<6; i++) {
            for (int j = 0; j < 7; j++) {
                sBoard[i][j] = 0;
            }
        }
        for (int j=0; j<7; j++){
            moveIndex[j] = 5;
        }
    }

    // informed state construct, makes a copy of the original state given
    public State(State original) {
        this.score = original.score;
        this.moveIndex = new int[7];
        this.sBoard = new int[6][7];
        for (int i=0; i<6; i++){
            this.sBoard[i] = original.sBoard[i].clone();
        }
        for (int j=0; j<7; j++){
            this.moveIndex[j] = original.moveIndex[j];
        }
    }

    // constructs state from the 2d board used in the program/display
    public State(int[][] board) {
        this.moveIndex = new int[7];
        this.sBoard = new int[6][7];
        int rowCnt;
        for (int ro = 0; ro < 6; ro++) {
            this.sBoard[ro] = board[ro].clone();
        }

        for (int coCnt=0; coCnt<7; coCnt++){
            rowCnt = 0;
            while (rowCnt < 5 && board[rowCnt][coCnt] == 0) {
                rowCnt++;
            }
            this.moveIndex[coCnt] = rowCnt;
        }
    }

    // find all valid moves from current state
    // returns array of all the states for valid moves (should be 7 if no column is full)
    public ArrayList<State> getChildren(int player) {
        ArrayList<State> children = new ArrayList<State>();
        State temp = null;

        for (int i=0; i<7; i++){
            temp = new State(this);
            // tries to make a move, for one of each of the 7 columns
            if (temp.move(i, player)) {
                children.add(temp);
            }
        }

        return children;
    }

    // supplements the alpha beta search with basic move ordering
    public ArrayList<State> getChildrenMoveOrdering(int player) {
        ArrayList<State> temps = new ArrayList<State>();
        ArrayList<State> children = new ArrayList<State>();
        State temp = null;
        scores = new int[7];

        // find score of all states
        for (int i = 0; i < 7; i++) {
            temp = new State(this);
            if (temp.move(i, player)) {
                temp.evaluate();
                temps.add(temp);
                scores[i] = temp.getScore();
            }
        }
        // order the scores
        Arrays.sort(scores);

        for (State tem : temps) {
            for (int i : scores) {
                if (tem.score == i) {
                    children.add(tem);
                }
            }
        }
        return temps;
    }

    // actually make move from a state, based on the column and player
    public boolean move(int col, int player) {
        if (moveIndex[col] < 0) {
            return false;
        }

        // moveIndex[col] is the row number
        sBoard[moveIndex[col]][col] = player;
        moveIndex[col]--;
        return true;
    }

    // returns true if the currently analyzed game board is a win/tie board, otherwise false
    public boolean isTerminal() {
        // checkWinCond returns 0 if no winner, 1 for p1win, 2 for p2/cpu win, and 3 for tie
        int winType = Utility.getInstance().checkWinner(this.sBoard);
        if (winType >= 1) {
            return true;
        }
        return false;
    }


    // get heuristic score of the current state for weighing min max algorithm decisions
    public void evaluate() {
        // adds together good options for comp and subtract potential moves from player
        int score = Utility.getInstance().getScoreForBoard(this.sBoard);
        setScore(score);
    }

    // gets score of the state
    public int getScore() {
        return score;
    }

    // sets the score of the current state when given a calculated score
    public void setScore(int score) {
        this.score = score;
    }
}
