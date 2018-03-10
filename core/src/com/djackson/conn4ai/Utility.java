package com.djackson.conn4ai;

import com.djackson.conn4ai.complayer.State;

public class Utility {
    private static Utility utilSingleton;

    // singleton for use everywhere
    public static Utility getInstance() {
        if (utilSingleton == null) {
            utilSingleton = new Utility();
        }
        return utilSingleton;
    }

    // makes a state for use from the min max algorithm from a 2d array
    public State arrayToState(int[][] cBoard) {
        return new State(cBoard);
    }

    // gets the column number of the move made by the algorithm
    public int getMoveMade(State newState, State oldState) {
        // get the number of tokens in each row, like moveIndex
        for (int coCnt=0; coCnt<7; coCnt++){
            // if the move indexes are different, that column is the one that changes
            // meaning the move was made
            if (newState.moveIndex[coCnt] != oldState.moveIndex[coCnt]) {
                return coCnt;
            }
        }
        return -1;
    }

    // checkWinner returns 0 if no winner, 1 for p1win, 2 for p2/cpu win, and 3 for tie
    public int checkWinner(int [][]sBoard) {
        int hCnt = 0;
        int vCnt = 0;
        int dCnt = 0;
        int ownBy = 0;

        //check full board (tie condition)
        while(hCnt < 7 && sBoard[0][hCnt] != 0) {
            hCnt++;
        }
        // return 3 indicating a tie
        if (hCnt >= 6) {
            return 3;
        }

        // check Horizontal
        hCnt = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                if (sBoard[r][c] != 0) {
                    int z = c;
                    // iterate to the right at each row, ensuring each token is the same
                    ownBy = sBoard[r][c];
                    while (z < 7 && sBoard[r][z] == ownBy) {
                        z++;
                        hCnt++;
                    }
                    // if there is 4 of the same token in a row, that player/cpu wins
                    if (hCnt >= 4) {
                        return ownBy;
                    }
                    hCnt = 0;
                }
            }
        } // end check Horizontal

        // check Vertical
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 7; c++) {
                if (sBoard[r][c] != 0) {
                    // iterate down at each row, ensuring each token is the same
                    ownBy = sBoard[r][c];
                    int z = r;
                    while (z < 6 && sBoard[z][c] == ownBy) {
                        z++;
                        vCnt++;
                    }
                    // if there is 4 of the same token in a column, that player/cpu wins
                    if (vCnt >= 4) {
                        return ownBy;
                    }
                    vCnt = 0;
                }
            }
        } // end check Vertical

        // check Diagonal
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                // player piece found
                if (sBoard[r][c] != 0) {
                    dCnt = 0;
                    ownBy = sBoard[r][c];
                    int rr = r;
                    int cc = c;
                    // check left side high, right side low (down(+) row, right col)
                    if (c <= 3 && r <= 2 ) {
                        while (rr < 6 && cc < 7 && sBoard[rr][cc] == ownBy) {
                            cc++;
                            rr++;
                            dCnt++;
                        }
                    }
                    // if there is 4 of the same token diagonally, that player/cpu wins
                    if (dCnt >= 4) {
                        return ownBy;
                    }
                    // reset values and check other diagonal option
                    dCnt = 0;
                    rr = r;
                    cc = c;
                    // check left side low, right side high (up(-) row, right col)
                    if (c <= 3 && r >= 3) {
                        while (rr >= 0 && rr < 6 && cc < 7 && sBoard[rr][cc] == ownBy) {
                            cc++;
                            rr--;
                            dCnt++;
                        }
                    }
                    // if there is 4 of the same token diagonally, that player/cpu wins
                    if (dCnt >= 4) {
                        return ownBy;
                    }
                    dCnt = 0;
                }
            }
        }// end check Diagonal

        // if nothing returned yet, there is no win condition
        return 0;
    }

    // finds the overall score assigned to a board via this heuristic function
    // higher score is better for computer player
    public int getScoreForBoard(int[][] sBoard) {
        // checks for all sequential tokens for either player and adds them
        // together with various weights, but only if those tokens are not blocked
        // if they are blocked, it is beneficial to the opposing player
        // this uses a lot of the same functionality as the checkWinner function
        int compScore = 0;
        int playerScore = 0;

        int hCnt = 0;
        int vCnt = 0;
        int dCnt = 0;
        int ownBy = 0;

        // check Horizontal
        hCnt = 0;
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 6; c++) {
                if (sBoard[r][c] != 0) {
                    // iterate to the right at each row, ensuring each token is the same
                    ownBy = sBoard[r][c];
                    int z = c;
                    while (z < 7 && sBoard[r][z] == ownBy) {
                        z++;
                        hCnt++;
                    }

                    // weights for horizontal elements
                    if (ownBy == 1 && hCnt > 1) {
                        if (isBlocked(sBoard, r, c, 1, hCnt, ownBy)) {
                            compScore = compScore + (hCnt+hCnt);
                        }
                        playerScore = playerScore + (hCnt*hCnt);
                    }
                    if (ownBy == 2 && hCnt > 1) {
                        if (isBlocked(sBoard, r, c, 1, hCnt, ownBy)) {
                            playerScore = playerScore + (hCnt+hCnt);
                        }
                        compScore = compScore + (hCnt*hCnt);
                    }
                    hCnt = 0;
                }
            }
        } // end check Horizontal

        // check Vertical
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 7; c++) {
                if (sBoard[r][c] != 0) {
                    // iterate down at each row, ensuring each token is the same
                    ownBy = sBoard[r][c];
                    int z = r;
                    while (z < 6 && sBoard[z][c] == ownBy) {
                        z++;
                        vCnt++;
                    }

                    // weights for vertical elements
                    if (ownBy == 1 && vCnt > 1) {
                        if (isBlocked(sBoard, r, c, 2, vCnt, ownBy)) {
                            compScore = compScore + (vCnt+vCnt);
                        }
                        playerScore = playerScore + (vCnt*vCnt);
                    }
                    if (ownBy == 2 && vCnt > 1) {
                        if (isBlocked(sBoard, r, c, 2, vCnt, ownBy)) {
                            playerScore = playerScore + (vCnt+vCnt);
                        }
                        compScore = compScore + (vCnt*vCnt);
                    }

                    vCnt = 0;
                }
            }
        } // end check Vertical

        // check Diagonal
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 7; c++) {
                // player piece found
                if (sBoard[r][c] != 0) {
                    ownBy = sBoard[r][c];
                    int rr = r;
                    int cc = c;
                    // check left side high, right side low (down(+) row, right col)
                    if (c <= 4 && r <= 3 ) {
                        while (rr < 6 && cc < 7 && sBoard[rr][cc] == ownBy) {
                            cc++;
                            rr++;
                            dCnt++;
                        }
                    }

                    // weights for diagonal elements
                    if (ownBy == 1 && dCnt > 1) {
                        if (isBlocked(sBoard, r, c, 3, dCnt, ownBy)) {
                            compScore = compScore + (dCnt+dCnt);
                        }
                        playerScore = playerScore + (dCnt*dCnt);
                    }
                    if (ownBy == 2 && dCnt > 1) {
                        if (isBlocked(sBoard, r, c, 3, dCnt, ownBy)) {
                            playerScore = playerScore + (dCnt+dCnt);
                        }
                        compScore = compScore + (dCnt*dCnt);
                    }

                    dCnt = 0;
                    rr = r;
                    cc = c;
                    // check left side low, right side high (up(-) row, right col)
                    if (c <= 4 && r >= 2) {
                        while (rr >= 0 && rr < 6 && cc < 7 && sBoard[rr][cc] == ownBy) {
                            cc++;
                            rr--;
                            dCnt++;
                        }
                    }

                    // weights for diagonal elements
                    if (ownBy == 1 && dCnt > 1) {
                        if (isBlocked(sBoard, r, c, 4, dCnt, ownBy)) {
                            compScore = compScore + (dCnt+dCnt);
                        }
                        playerScore = playerScore + (dCnt*dCnt);
                    }
                    if (ownBy == 2 && dCnt > 1) {
                        if (isBlocked(sBoard, r, c, 4, dCnt, ownBy)) {
                            playerScore = playerScore + (dCnt+dCnt);
                        }
                        compScore = compScore + (dCnt*dCnt);
                    }
                    dCnt = 0;
                }
            }
        }// end check Diagonal

        return compScore-playerScore;
    }

    // takes in the board, coordinates, x/y values, attempted direction, and number of tokens
    // returns if the sequence of tokens is blocked (true) or not (false)
    private boolean isBlocked(int[][] board, int x, int y, int dir, int tokens, int pl) {
        // dir: 1 = horizontal, 2 = vertical, 3 = diagonal(left high), 4 = diagonal (left low)
        if (dir == 1) {
            if (y > 3) {
                return true;
            }
            if (tokens+y+1 < 7 && board[x][tokens+y+1] != pl && board[x][tokens+y+1] != 0) {
                return true;
            }
            if (y-1 > 0 && board[x][y-1] != pl && board[x][y-1] != 0) {
                return true;
            }
        }
        if (dir == 2) {
            if (x < 2) {
                return true;
            }
            if (tokens+x+1 < 6 && board[tokens+x+1][y] != pl && board[tokens+x+1][y] != 0) {
                return true;
            }
            if (x-1 > 0 && board[x-1][y] != pl && board[x-1][y] != 0) {
                return true;
            }
        }
        if (dir == 3) {
            if (x > 2 && y > 3) {
                return true;
            }
            if (tokens+x+1 < 6 && tokens+y+1 < 7 && board[tokens+x+1][tokens+y+1] != pl && board[tokens+x+1][tokens+y+1] != 0) {
                return true;
            }
            if (x-1 > 0 && y-1 > 0 && board[x-1][y-1] != pl && board[x-1][y-1] != 0) {
                return true;
            }
        }
        if (dir == 4) {
            if (x <= 2 && y > 3) {
                return true;
            }
        }
        return false;
    }
}
