package com.djackson.conn4ai.complayer;

import com.djackson.conn4ai.Utility;

import java.util.ArrayList;
import java.util.Random;

public class AIMain {
    // deepest node down on tree to evaluate
    private int mDepth = 6;
    // current turn, either p1 or cpu
    public static int turn;
    // random val to help with breaking ties
    Random r = new Random();

    // counter of number of branches
    public int countBranch = 0;

    // returns the column selected by the computer to drop a token in.
    public int aiMakeMove(int[][] currBoard) {
        // utility to change the current board 2d array into a state for the min max algorithm
        State currState = Utility.getInstance().arrayToState(currBoard);

        // gets the state of the board chosen by the min max algorithm
        State nextMove = MinMax_AlphaBeta(currState);

        // gets the move made from the state given from the min max algorithm.
        return Utility.getInstance().getMoveMade(nextMove, currState);
    }

    // MinMax_AlphaBeta is where the actual algorithm takes place, as
    //  described in the project report.
    public State MinMax_AlphaBeta(State initialState) {
        // alpha/beta variables for alpha beta pruning
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // temp/score helps with min/max and alpha/beta computation
        int temp = 0;
        int score = Integer.MIN_VALUE;

        // state that computer will invoke
        // maybe have a return of column to make move on??
        State maxState = null;

        // find all possible moves from the current state (children)
        ArrayList<State> children = new ArrayList<State>();
        ArrayList<State> t = initialState.getChildrenMoveOrdering(turn);

        // if the array of possible moves is null, then return the null max state
        if (t != null) {
            children.addAll(t);
        } else {
            return maxState;
        }

        // iterates over the possible move states and evaluates...
        for (State child : children) {
            // starts by calling min on the first possible state
            temp = min(new State(child), 1, alpha, beta);

            // check if we need to update current best move state and score
            if (temp >= score){
                // break a tie with a random value
                if (temp == score) {
                    if (r.nextInt(2) == 0) {
                        maxState = child;
                        score = temp;
                    }
                } else {
                    maxState = child;
                    score = temp;
                }
            }

            // check if we need to update alpha (for pruning)
            if (score > alpha) {
                alpha = score;
            }
            // if not, break (prune)
            if (alpha >= beta) {
                break;
            }
        }

        // set and return the best move found via the algorithm
        maxState.setScore(score);
        return maxState;
    }

    // max calculates out max node and as defined, calls min with recursion
    private int max(State state, int depth, int alpha, int beta) {
        // same as in min max algorithm
        int temp = 0;
        int score = Integer.MIN_VALUE;
        // checks if game end
        boolean terminal = false;

        if (state.isTerminal()) {
            terminal = true;
        }

        // end condition for recursion
        if (terminal || depth == mDepth) {
            // game about to end, with human player winning
            if (terminal) {
                state.setScore(Integer.MIN_VALUE);
                return state.getScore();
            }
            // find score of current state and return it
            state.evaluate();
            return state.getScore();
        }

        // alternate turn for min/max
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }

        // again find all possible moves for current state
        ArrayList <State> children = new ArrayList<State>();
        ArrayList <State> t = state.getChildren(turn);

        if (t != null) {
            children.addAll(t);
        } else {
            return Integer.MAX_VALUE;
        }

        // iterate over states
        for (State child : children) {
            // the recursive min call
            temp = min(child, depth+1, alpha, beta);

            // update score if temp is better
            if (temp >= score) {
                // break ties with random value
                if (temp == score) {
                    if (r.nextInt(2) == 0) {
                        score = temp;
                    }
                } else {
                    score = temp;
                }
            }

            // perform alpha/beta pruning and updating
            if (score > alpha) {
                alpha = score;
            }
            if (alpha >= beta) {
                break;
            }
        }

        // returns maximum score evaluated
        return score;
    }

    // min calculates out min node and as defined, calls max with recursion
    private int min(State state, int depth, int alpha, int beta) {
        // same as min/max above
        int temp = 0;
        int score = Integer.MAX_VALUE;
        // check if game end
        boolean terminal = false;

        if (state.isTerminal()) {
            terminal = true;
        }

        // end condition for recursion
        if (terminal || depth == mDepth) {
            // game end at state, ai wins
            if (terminal) {
                state.setScore(Integer.MAX_VALUE);
                return state.getScore();
            }
            // find score of current state and return it
            state.evaluate();
            return state.getScore();
        }

        // alternate turn for min/max
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }


        // again find all possible moves for current state
        ArrayList <State> children = new ArrayList<State>();
        ArrayList <State> t = state.getChildren(turn);

        if (t != null) {
            children.addAll(t);
        } else {
            return Integer.MIN_VALUE;
        }

        // iterate over states
        for (State child : children) {
            // the recursive max call
            temp = max(child, depth+1, alpha, beta);

            // updates score if temp is better
            if (temp <= score) {
                // break tie with random value
                if (temp == score) {
                    if (r.nextInt(2) == 0) {
                        score = temp;
                    }
                } else {
                    score = temp;
                }
            }

            // perform alpha/beta pruning and updating
            if (score < beta) {
                beta = score;
            }
            if (alpha >= beta) {
                break;
            }
        }

        // returns minimum score evaluated
        return score;
    }
}
