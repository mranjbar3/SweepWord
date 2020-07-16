package ir.hitolid.sweepword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView[] textViews = new TextView[4];
    private TextView result;
    int[] selectedItem;
    private float h, eventX, eventY;
    private MyView myView;
    private String lastResult = "گوجه";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        textViews[0] = findViewById(R.id.btn1);
        textViews[1] = findViewById(R.id.btn2);
        textViews[2] = findViewById(R.id.btn3);
        textViews[3] = findViewById(R.id.btn4);
        myView = findViewById(R.id.imageView);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        eventX = (float) (event.getX() + textViews[0].getWidth() / 2.0);
        h = getStatusBarHeight(this);
        eventY = (float) (event.getY() + textViews[0].getHeight() / 2.0 - h);
        int i = detectChar();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                result.setText("");
                myView.clearLines();
                myView.check();
                selectedItem = new int[4];
                if (i != -1) {
                    result.setText(textViews[i].getText());
                    selectedItem[0] = textViews[i].getId();
                    myView.addLine(textViews[i]);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int j = isFree(i);
                if (j != -1) {
                    result.setText(result.getText().toString() + textViews[i].getText());
                    selectedItem[j] = textViews[i].getId();
                    myView.addLine(textViews[i]);
                } else
                    myView.setLine(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                if (result.getText().toString().equals(lastResult)) {
                    Toast.makeText(this, "درست است.", Toast.LENGTH_LONG).show();
                    myView.win();
                } else {
                    myView.lose();
                }
                myView.removeLastLine();
                break;
        }
        myView.invalidate();
        return super.onTouchEvent(event);
    }

    public static int getStatusBarHeight(final Context context) {
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            return resources.getDimensionPixelSize(resourceId);
        else
            return (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * resources.getDisplayMetrics().density);
    }

    public int detectChar() {
        for (int i = 0; i < textViews.length; i++) {
            TextView textView = textViews[i];
            double distance;
            if (i == 0 || i == 2)
                distance = Math.sqrt(Math.pow(textView.getX() + textView.getWidth() / 2.0 - eventX, 2) +
                        Math.pow(textView.getY() + textView.getHeight() / 2.0 - eventY, 2));
            else
                distance = Math.sqrt(Math.pow(textView.getX() + textView.getWidth() / 2.0 +
                        ((LinearLayout) textView.getParent()).getX() - eventX, 2) +
                        Math.pow(textView.getY() + textView.getHeight() / 2.0 +
                                ((LinearLayout) textView.getParent()).getY() - eventY + h, 2));
            if (distance < 70) {
                return i;
            }
        }
        return -1;
    }

    public int isFree(int i) {
        if (i != -1) {
            int k = 0;
            for (int j = 0; j < selectedItem.length; j++) {
                if (selectedItem[j] == 0) {
                    k = j;
                    break;
                }
                if (selectedItem[j] == textViews[i].getId())
                    return -1;
            }
            i = k;
        }
        return i;
    }
}