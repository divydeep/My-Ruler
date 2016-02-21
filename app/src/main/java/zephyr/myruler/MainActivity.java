package zephyr.myruler;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private ImageView userImageView;
    private TextView displayValue;
    private int screenEnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenEnd = metrics.heightPixels - 20;
        float dpHeight = (float) Math.pow(metrics.heightPixels / metrics.densityDpi,2);
        float dpWidth  = (float) Math.pow(metrics.widthPixels / metrics.densityDpi,2);
        float inches   = (float) Math.sqrt(dpHeight + dpWidth);
        int totalParts  = (int)(16 * inches);

        ImageView drawingImageView = (ImageView) this.findViewById(R.id.imageView);
        userImageView = (ImageView) this.findViewById(R.id.imageView2);
        displayValue = (TextView) this.findViewById(R.id.rulerValue);
        final Bitmap[] bitmap = {Bitmap.createBitmap(getWindowManager()
                .getDefaultDisplay().getWidth(), getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888)};
        final Bitmap[] bitmap2 = {Bitmap.createBitmap(getWindowManager()
                .getDefaultDisplay().getWidth(), getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888)};

        final Canvas canvas = new Canvas(bitmap[0]);
        final Canvas canvas2 = new Canvas(bitmap2[0]);
        drawingImageView.setImageBitmap(bitmap[0]);
        userImageView.setImageBitmap(bitmap2[0]);

        final Paint paint = new Paint();
        paint.setColor(Color.parseColor("#4fc3f7"));
        paint.setStrokeWidth(3);

        Paint paint2 = new Paint();
        paint2.setColor(Color.GRAY);
        paint2.setStrokeWidth(3);
        int varY = screenEnd;
        for(int i=0; i<=totalParts; i++){
            int xVal = computeX(i, canvas, paint2, varY);
            canvas.drawLine(0, varY, xVal, varY, paint2);
            varY -= 20;
        }

        drawingImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        System.out.println("DOWN X " + x + " Y " + y);
                        bitmap2[0].eraseColor(Color.TRANSPARENT);
                        canvas2.drawCircle(x, y, 60, paint);
                        canvas2.drawLine(0, y, x - 10, y, paint);
                        userImageView.invalidate();
                        updateText(y);
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        System.out.println("MOVE X " + x + " Y " + y);
                        bitmap2[0].eraseColor(Color.TRANSPARENT);
                        canvas2.drawCircle(x, y, 60, paint);
                        canvas2.drawLine(0, y, x - 10, y, paint);
                        userImageView.invalidate();
                        updateText(y);
                        break;
                }
                return true;
            }
        });
    }


    private void updateText(int y) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float dpHeight = (float) (screenEnd-y) / metrics.densityDpi;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        displayValue.setText(df.format(dpHeight) + " inch");
    }

    private int computeX(int i, Canvas canvas, Paint paint2, int varY) {
        int largest = 160;
        if (i % 16 == 0){
            paint2.setTextSize(30);
            canvas.save();
            canvas.rotate(-90, largest + 10, varY + 10);
            canvas.drawText(String.valueOf(i/16), largest + 12, varY + 40, paint2);
            canvas.restore();
            return largest;
        }
        else if(i%8 == 0){
            return 3* largest /4;
        }
        else if(i%2 == 0){
            return largest /2;
        }
        else {
            return largest /4;
        }
    }
}
