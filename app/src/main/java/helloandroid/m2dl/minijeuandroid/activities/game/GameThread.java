package helloandroid.m2dl.minijeuandroid.activities.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.util.Pair;
import android.view.SurfaceHolder;

import java.util.Random;

import helloandroid.m2dl.minijeuandroid.models.SystemTheme;
import helloandroid.m2dl.minijeuandroid.models.Zone;

public class GameThread extends Thread {

    private final SurfaceHolder surfaceHolder;
    private final GameView gameView;
    private int borderWidth;
    private int borderHeight;
    private int width;
    private int height;
    private int speed;
    private float stride;
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
        this.width = gameView.getScreenWidth();
        this.height = gameView.getScreenHeight();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
        this.coordinates = coordinates;
        this.paint = paint;
        this.speed = width / 800;
        this.stride = (float) ((width / 800) * 3);
        this.borderWidth = (int) Math.round(0.0625 * width);
        this.borderHeight = (int) Math.round(0.0625 * height);
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
        if (coordinates.first < width / 2.0 && coordinates.second < height / 2.0) {
            return Zone.NORTH_WEST;
        } else if (width / 2.0 <= coordinates.first && coordinates.first < width && coordinates.second < height / 2.0) {
            return Zone.NORTH_EAST;
        } else if (width / 2.0 <= coordinates.first && coordinates.first < width && height / 2.0 <= coordinates.second && coordinates.second < height) {
            return Zone.SOUTH_EAST;
        } else {
            return Zone.SOUTH_WEST;
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkTheme();
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.draw(canvas);
                    setCoordinates(new Pair<>(this.coordinates.first + direction_x * stride, this.coordinates.second + direction_y * stride));
                    Rect r = new Rect();
                    r.left = (int) (this.coordinates.first - borderWidth);
                    r.top = (int) (this.coordinates.second - borderWidth);
                    r.right = (int) (this.coordinates.first + borderWidth);
                    r.bottom = (int) (this.coordinates.second + borderWidth);
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

    public void checkTheme() {
        SensorManager mySensorManager = (SensorManager)gameView.getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = mySensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null){
            mySensorManager.registerListener(
                    lightSensorListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    private final SensorEventListener lightSensorListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_LIGHT){
                if (event.values[0] < 10) {
                    gameView.setSystemTheme(SystemTheme.DARK);
                }
                else {
                    gameView.setSystemTheme(SystemTheme.LIGHT);
                }
            }
        }

    };


    private boolean isOutOfBounds() {
        int west_border = borderWidth;
        int east_border = width - borderWidth;
        int north_border = borderHeight;
        int south_border = height - borderHeight;

        int posx = Math.round(coordinates.first);
        int posy = Math.round(coordinates.second);


        if (posx <= west_border || posx >= east_border) {
            return true;
        }

        if (posy <= north_border || posy >= south_border) {
            return true;
        }

        return false;
    }
}