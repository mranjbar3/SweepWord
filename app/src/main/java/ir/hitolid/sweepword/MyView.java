package ir.hitolid.sweepword;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

class MyView extends View {
    Paint paint;
    ArrayList<MyLine> lines = new ArrayList<>();

    public MyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (MyLine line : lines) {
            canvas.drawLine(line.startX, line.startY, line.stopX, line.stopY, paint);
        }
    }

    public void setLine(float endX, float endY) {
        if (lines.size() != 0)
            lines.get(lines.size() - 1).setPointTwo(endX, endY);
    }

    public void addLine(TextView textView) {
        float x = (float) (textView.getX() + textView.getWidth() / 2.0) + ((LinearLayout) textView.getParent()).getX(),
                y = (float) (textView.getY() + textView.getHeight() / 2.0) + ((LinearLayout) textView.getParent()).getY();
        if (lines.size() != 0) {
            lines.get(lines.size() - 1).setPointTwo(x, y);
        }
        lines.add(new MyLine(x, y));
    }

    public void removeLastLine() {
        if (lines.size() != 0) {
            lines.remove(lines.size() - 1);
        }
    }

    public void clearLines() {
        lines = new ArrayList<>();
    }

    public void win() {
        paint.setColor(Color.GREEN);
    }

    public void lose() {
        paint.setColor(Color.RED);
    }
    public void check(){
        paint.setColor(Color.BLACK);
    }
}

class MyLine {
    float startX, startY, stopX, stopY;

    public MyLine(float startX, float startY, float stopX, float stopY) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = stopX;
        this.stopY = stopY;
    }

    public MyLine(float startX, float startY) {
        this(startX, startY, startX, startY);
    }

    public void setPointTwo(float x, float y) {
        stopX = x;
        stopY = y;
    }
}