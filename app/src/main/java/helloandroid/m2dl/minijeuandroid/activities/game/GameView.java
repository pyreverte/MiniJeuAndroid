package helloandroid.m2dl.minijeuandroid.activities.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import helloandroid.m2dl.minijeuandroid.activities.GameActivity;
import helloandroid.m2dl.minijeuandroid.models.SystemTheme;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private int screenWidth;
    private int screenHeight;
    private GameThread thread;
    private SharedPreferences sharedPreferences;
    private GameActivity activity;
    private int borderWidth;
    private Paint cubePaint;
    private Paint backgroundPaint;
    private int score;

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, SharedPreferences sharedPreferences, GameActivity activity) {
        super(context);
        this.activity = activity;
        setSystemTheme(SystemTheme.LIGHT);

        this.score = 0;

        this.sharedPreferences = sharedPreferences;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("last_score", 0);
        editor.apply();

        this.screenHeight = sharedPreferences.getInt("screen_height", 0);
        this.screenWidth = sharedPreferences.getInt("screen_width", 0);

        this.borderWidth = (int) Math.round(0.0625 * screenWidth);
        getHolder().addCallback(this);

        this.thread = new GameThread(getHolder(), this, genererCoordonnees(screenHeight, screenWidth), cubePaint);
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
            canvas.drawText("Score " + String.valueOf(score), screenWidth * 0.8f, screenHeight * 0.05f, new Paint() {{
                setColor(Color.GRAY);
                setTextSize(20);
            }});
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCubeTouched(event)) {
            score++;
            thread.setNewDirection();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("last_score", score);
            editor.apply();
        }
        return super.onTouchEvent(event);
    }

    private boolean isCubeTouched(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        return x <= thread.getCoordinates().first + borderWidth + 0.50 * borderWidth
                && x >= thread.getCoordinates().first - borderWidth - 0.50 * borderWidth
                && y <= thread.getCoordinates().second + borderWidth + 0.50 * borderWidth
                && y >= thread.getCoordinates().second - borderWidth - 0.50 * borderWidth;
    }

    private Pair<Float, Float> genererCoordonnees(int height, int width) {
        float posx = getRandomNumberInRange((int) Math.round(width * 0.2), (int) Math.round(width * 0.8));
        float posy = getRandomNumberInRange((int) Math.round(height * 0.2), (int) Math.round(height * 0.8));
        return new Pair<>(posx, posy);
    }

    public void setSystemTheme(SystemTheme systemTheme) {
        switch (systemTheme) {
            case DARK:
                backgroundPaint = new Paint() {{
                    setColor(Color.rgb(30, 30, 30));
                }};
                cubePaint = new Paint() {{
                    setColor(Color.rgb(120, 120, 120));
                }};
                break;
            case LIGHT:
                backgroundPaint = new Paint() {{
                    setColor(Color.rgb(255, 255, 255));
                }};
                cubePaint = new Paint() {{
                    setColor(Color.rgb(120, 120, 120));
                }};
                break;
        }
    }

    public void endGame() {
        thread.setRunning(false);
        activity.toScoreActivity();
    }
}