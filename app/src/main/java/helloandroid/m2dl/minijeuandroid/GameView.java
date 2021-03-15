package helloandroid.m2dl.minijeuandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private final int width;
    private final int height;
    private final GameThread thread;

    private final SharedPreferences sharedPreferences;

    private SystemTheme systemTheme;
    private Paint cubePaint;
    private Paint backgroundPaint;

    private int score;

    public GameView(Context context, SharedPreferences sharedPreferences) {
        super(context);

        setSystemTheme();

        this.score = 0;

        this.sharedPreferences = sharedPreferences;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score", 0);
        editor.apply();

        this.height = sharedPreferences.getInt("screen_height", 0);
        this.width = sharedPreferences.getInt("screen_width", 0);

        getHolder().addCallback(this);

        this.thread = new GameThread(getHolder(), this, genererCoordonnees(height, width), cubePaint);
        setFocusable(true);
    }

    private static int getRandomNumberInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(backgroundPaint.getColor());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCubeTouched(event)) {
            score++;
            thread.setNewDirection();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("score", score);
            editor.apply();
        }
        return super.onTouchEvent(event);
    }

    private boolean isCubeTouched(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        return x <= thread.getCoordinates().first + 50 && x >= thread.getCoordinates().first - 50 && y <= thread.getCoordinates().second + 50 && y >= thread.getCoordinates().second - 50;
    }

    private Pair<Float, Float> genererCoordonnees(int height, int width) {
        float posx = getRandomNumberInRange((int) Math.round(width * 0.2), (int) Math.round(width * 0.8));
        float posy = getRandomNumberInRange((int) Math.round(height * 0.2), (int) Math.round(height * 0.8));
        return new Pair<>(posx, posy);
    }

    public void setSystemTheme() {
        this.systemTheme = SystemTheme.DARK;
        switch (systemTheme) {
            case DARK:
                backgroundPaint = new Paint() {{
                    setColor(Color.rgb(30, 30, 30));
                }};
                cubePaint = new Paint() {{
                    setColor(Color.rgb(255, 255, 255));
                }};
                break;
            case LIGHT:
                backgroundPaint = new Paint() {{
                    setColor(Color.rgb(255, 255, 255));
                }};
                cubePaint = new Paint() {{
                    setColor(Color.rgb(0, 0, 0));
                }};
                break;
        }
    }
}
