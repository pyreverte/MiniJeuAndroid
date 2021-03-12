package helloandroid.m2dl.minijeuandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Pair;
import android.view.SurfaceHolder;

import androidx.annotation.RequiresApi;

import java.util.Random;

public class GameThread extends Thread {

    public void setCoordinates(Pair<Float, Float> coordinates) {
        this.coordinates = coordinates;
    }

    public Pair<Float, Float> getCoordinates() {
        return coordinates;
    }

    // first : width
    // second : height
    private Pair<Float, Float> coordinates;

    private float direction_x;

    private float direction_y;

    private final SurfaceHolder surfaceHolder;

    private final GameView gameView;

    private boolean running;

    private final int speed = 10;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GameThread(SurfaceHolder surfaceHolder, GameView gameView, Pair<Float, Float> coordinates) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.coordinates = coordinates;
        setNewDirection();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setNewDirection() {
        switch (getZoneBalle()) {
            case SOUTH_EAST:
                do {
                    this.direction_x = getRandomNumberInRange(-1, 0);
                    this.direction_y = getRandomNumberInRange(-1, 0);
                } while (this.direction_x == 0 && this.direction_y == 0);
                break;
            case SOUTH_WEST:
                do {
                    this.direction_x = getRandomNumberInRange(0, 1);
                    this.direction_y = getRandomNumberInRange(-1, 0);
                } while (this.direction_x == 0 && this.direction_y == 0);
                break;
            case NORTH_EAST:
                do {
                    this.direction_x = getRandomNumberInRange(-1, 0);
                    this.direction_y = getRandomNumberInRange(0, 1);
                } while (this.direction_x == 0 && this.direction_y == 0);
                break;
            case NORTH_WEST:
                do {
                    this.direction_x = getRandomNumberInRange(0, 1);
                    this.direction_y = getRandomNumberInRange(0, 1);
                } while (this.direction_x == 0 && this.direction_y == 0);
                break;
            default:
        }
    }

    private Zone getZoneBalle() {
        if (coordinates.first < this.gameView.getWidth() / 2.0 && coordinates.second < this.gameView.getHeight() / 2.0) {
            return Zone.NORTH_WEST;
        } else if (this.gameView.getWidth() / 2.0 <= coordinates.first && coordinates.first < this.gameView.getWidth() && coordinates.second < this.gameView.getHeight() / 2.0) {
            return Zone.NORTH_EAST;
        } else if (this.gameView.getWidth() / 2.0 <= coordinates.first && coordinates.first < this.gameView.getWidth() && this.gameView.getHeight() / 2.0 <= coordinates.second && coordinates.second < this.gameView.getHeight()) {
            return Zone.SOUTH_EAST;
        } else {
            return Zone.SOUTH_WEST;
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000 / speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.draw(canvas);
                    Paint paint = new Paint();
                    paint.setColor(Color.rgb(250, 0, 0));
                    setCoordinates(new Pair<>(this.coordinates.first + direction_x, this.coordinates.second + direction_y));
                    Rect r = new Rect();
                    r.left = (int) (this.coordinates.first - 50);
                    r.top = (int) (this.coordinates.second - 50);
                    r.right = (int) (this.coordinates.first + 50);
                    r.bottom = (int) (this.coordinates.second + 50);
                    canvas.drawRect(r, paint);
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }
}