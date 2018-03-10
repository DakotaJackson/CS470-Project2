package com.djackson.conn4ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.djackson.conn4ai.complayer.AIMain;

// TODO: add option for either vs comp or pvp
public class BoardManager implements InputProcessor, Disposable {
    private SpriteBatch batch;
    private OrthographicCamera cam;
    public Board gBoard;
    public AIMain ai;
    public int[][] currBoard;
    public int[][] resetBoard;
    public int turn;
    public boolean gameover;
    Texture startup;
    Texture p1Turn;
    Texture p2Turn;
    Texture p1Win;
    Texture p2Win;
    Texture help;

    public BoardManager(int[][]spaces) { init(spaces); }

    public void init(int[][]spaces) {
        Gdx.input.setInputProcessor(this);
        batch = new SpriteBatch();

        startup = new Texture("use/C4GameStartup.png");
        p1Turn = new Texture("use/C4GameScreenP1Turn.png");
        p2Turn = new Texture("use/C4GameScreenP2Turn.png");
        p1Win = new Texture("use/C4GameScreenP1Victory.png");
        p2Win = new Texture("use/C4GameScreenP2Victory.png");
       // help = new Texture("Connect4GameScreenHelp.png");

        currBoard = spaces;
        resetBoard = spaces;
        gBoard = new Board();
        ai = new AIMain();
        turn = 0;

        cam = new OrthographicCamera(640, 480);
        cam.position.set(0, 0, 0);
        cam.update();
    }

    public void render() {
        batch.begin();
        if (turn == 1) {
            batch.draw(p1Turn, 0, 0);
            gBoard.render(batch);
        } else if (turn == 2) {
            batch.draw(p2Turn, 0, 0);
            gBoard.render(batch);
        } else if (turn == 10) {
            batch.draw(p1Win, 0, 0);
        } else if (turn == 20) {
            batch.draw(p2Win, 0,0);
        } else if (turn == 30) {
            // TODO: change to tie screen
            batch.draw(startup, 0, 0);
        } else {
            batch.draw(startup, 0, 0);
        }
        batch.end();
    }

    public void update() {
        int winType = Utility.getInstance().checkWinner(currBoard);
        if (winType == 1) {
            turn = 10;
        }
        if (winType == 2) {
            turn = 20;
        }
        if (winType == 3) {
            turn = 30;
        }
        if (turn == 1) {
            turn = 2;
            render();
            int cpuMove = ai.aiMakeMove(currBoard);
            boolean cpuMoveSuccess = gBoard.playToken(cpuMove, turn, currBoard);
            if (!cpuMoveSuccess) {
                System.out.print("FAILURE TO MAKE MOVE BY CPU.");
            }
        }
        if (turn < 5) {
            turn = 1;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean moveMade = false;
        if (turn == 1 && screenY < 50) {
            float xCor = 10;
            for (int x = 0; x < 7; x++) {
                if (screenX > xCor && screenX < xCor + 88 && xCor < 635) {
                    moveMade = gBoard.playToken(x, turn, currBoard);
                    break;
                }
                xCor = xCor + 88;
            }
            if (moveMade) {
                update();
            }
        }

        if (turn == 0) {
            if (screenX >= 220 && screenX <= 407 && screenY >= 200 && screenY <= 269) {
                turn = 1;
                render();
            }
            if (screenX >= 145 && screenX <= 500 && screenY >= 324 && screenY <= 394) {
                turn = 1;
                update();
            }
        }
        if (turn > 5) {
            gameover = true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
