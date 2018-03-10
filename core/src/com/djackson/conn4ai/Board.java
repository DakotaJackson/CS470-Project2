package com.djackson.conn4ai;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.djackson.conn4ai.entities.AbstractGameObject;
import com.djackson.conn4ai.entities.BlankSpace;
import com.djackson.conn4ai.entities.P1Space;
import com.djackson.conn4ai.entities.P2Space;

public class Board {

    public Array<BlankSpace> blankSpaces;
    public Array<P1Space> p1Spaces;
    public Array<P2Space> p2Spaces;

    public Board() { init(); }

    public void init() {
        blankSpaces = new Array();
        p1Spaces = new Array();
        p2Spaces = new Array();

        int numRow = 6;
        int numCol = 7;
        float currX = 10;
        float currY = 430;
        float offsetY = 4;
        float offsetX = 34;

        for (int row = 0; row < numRow; row++) {
            currY = currY - offsetY - 55;
            currX = 10;
            for (int col = 0; col < numCol; col++) {
                currX = currX + offsetX;
                if (col == 0) {
                    currX = currX - 18;
                }
                AbstractGameObject s,p1,p2;
                s = new BlankSpace();
                p1 = new P1Space();
                p2 = new P2Space();
                s.position.set(currX, currY);
                p1.position.set(currX, currY);
                p2.position.set(currX, currY);
                s.active = true;
                p1.active = false;
                p2.active = false;
                blankSpaces.add((BlankSpace) s);
                p1Spaces.add((P1Space) p1);
                p2Spaces.add((P2Space) p2);
                currX = currX + 55;
            }
        }
    }

    // handleGravity simulates gravity and ensures chips behave normally.
    public void handleGravity(int [][]currBoard) {
        for(int r = 0; r < 6; r++) {
            for(int c = 0; c < 7; c++) {
                if (currBoard[r][c] > 0 && r < 5) {
                    int temp = r;
                    while (currBoard[r+1][c] == 0 && temp < 5) {
                        currBoard[temp+1][c] = currBoard[temp][c];
                        currBoard[temp][c] = 0;
                        temp++;
                    }
                }
            }
        }

        update(currBoard);
    }

    public boolean playToken(int colDrop, int turn, int [][]currBoard) {
        // make sure ai didn't make an error/no move
        if (colDrop != -1) {
            // make sure there is space for the move
            if (currBoard[0][colDrop] == 0) {
                if (turn == 1) {
                    currBoard[0][colDrop] = 1;
                }
                if (turn == 2) {
                    currBoard[0][colDrop] = 2;
                }
                handleGravity(currBoard);
                return true;
            }
        }
        // false if no move was made
        return false;
    }

    public void update(int [][]currBoard) {
        int pType = 0;
        int pNum = 0;
        for(int r = 0; r < 6; r++) {
            for(int c = 0; c < 7; c++) {
                pType = currBoard[r][c];
                if (pType == 1) {
                    p1Spaces.get(pNum).active = true;
                } else if (pType == 2) {
                    p2Spaces.get(pNum).active = true;
                } else {
                    p1Spaces.get(pNum).active = false;
                    p2Spaces.get(pNum).active = false;
                }
                pNum++;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (BlankSpace blankSpace : blankSpaces) {
            if (blankSpace.active) {
                blankSpace.render(batch);
            }
        }
        for (P1Space p1Space : p1Spaces) {
            if (p1Space.active) {
                p1Space.render(batch);
            }
        }
        for (P2Space p2Space : p2Spaces) {
            if (p2Space.active) {
                p2Space.render(batch);
            }
        }
    }
}
