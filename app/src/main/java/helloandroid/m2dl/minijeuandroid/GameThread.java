package helloandroid.m2dl.minijeuandroid;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;
import android.view.SurfaceHolder;

import java.util.Random;

public class GameThread extends Thread {

    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private final long speed = 1;
    private final int stride = 3;
    private final double sqrt_semi = Math.sqrt(0.5);
    private final Paint paint;
    // first : width
    // second : height
    private Pair<Float, Float> coordinates;
    private float direction_x;
    private float direction_y;
    private boolean running;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView, Pair<Float, Float> coordinates, Paint paint) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.coordinates = coordinates;
        this.paint = paint;
        setNewDirection();
    }

    private static int getRandomNumberInRange(int[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    public Pair<Float, Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Pair<Float, Float> coordinates) {
        this.coordinates = coordinates;
    }

    public void setNewDirection() {
        Zone zone = getZoneBalle();
        do {
            switch (zone) {
                case SOUTH_EAST:
                    this.direction_x = getRandomNumberInRange(new int[]{-1, 0});
                    this.direction_y = getRandomNumberInRange(new int[]{-1, 0});
                    break;
                case SOUTH_WEST:
                    this.direction_x = getRandomNumberInRange(new int[]{1, 0});
                    this.direction_y = getRandomNumberInRange(new int[]{-1, 0});
                    break;
                case NORTH_EAST:
                    this.direction_x = getRandomNumberInRange(new int[]{-1, 0});
                    this.direction_y = getRandomNumberInRange(new int[]{1, 0});
                    break;
                case NORTH_WEST:
                    this.direction_x = getRandomNumberInRange(new int[]{1, 0});
                    this.direction_y = getRandomNumberInRange(new int[]{1, 0});
                    break;
                default:
            }
        } while (this.direction_x == 0 && this.direction_y == 0);
        if (this.direction_x != 0 && this.direction_y != 0) {
            this.direction_x *= sqrt_semi;
            this.direction_y *= sqrt_semi;
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
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.draw(canvas);
                    setCoordinates(new Pair<>(this.coordinates.first + direction_x * stride, this.coordinates.second + direction_y * stride));
                    Rect r = new Rect();
                    r.left = (int) (this.coordinates.first - 50);
                    r.top = (int) (this.coordinates.second - 50);
                    r.right = (int) (this.coordinates.first + 50);
                    r.bottom = (int) (this.coordinates.second + 50);
                    canvas.drawRect(r, paint);
                }
            } catch (Exception ignored) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (isOutOfBounds()) {
                gameView.endGame();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    private boolean isOutOfBounds() {
        int west_border = 50;
        int east_border = gameView.getWidth() - 50;
        int north_border = 50;
        int south_border = gameView.getHeight() - 50;

        int posx = Math.round(coordinates.first);
        int posy = Math.round(coordinates.second);
        ;

        if (posx <= west_border || posx >= east_border) {
            return true;
        }

        if (posy <= north_border || posy >= south_border) {
            return true;
        }

        return false;
    }
}