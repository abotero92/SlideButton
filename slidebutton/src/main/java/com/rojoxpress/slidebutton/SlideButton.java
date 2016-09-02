package com.rojoxpress.slidebutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class SlideButton extends FrameLayout {

    private TextView textView;
    private SlideBar slideBar;
    private SlideButtonListener listener;
    private int offestThumb;

    public SlideButton(Context context) {
        super(context);
        init(null);

    }

    public SlideButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public SlideButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    public int dpToPixels(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }

    public void init(@Nullable AttributeSet set){


        offestThumb = dpToPixels(16);
        textView = new TextView(getContext());
        slideBar = new SlideBar(getContext());

        LayoutParams childParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childParams.gravity = Gravity.CENTER;

        slideBar.setLayoutParams(childParams);
        textView.setLayoutParams(childParams);
        slideBar.setProgressDrawable(ContextCompat.getDrawable(getContext(),R.drawable.back_slide_layer));


        textView.setGravity(Gravity.CENTER);

        if(set != null){
            TypedArray a = getContext().obtainStyledAttributes(set,R.styleable.slider_button,0,0);



            if(a.hasValue(R.styleable.slider_button_text)){
                String buttonText = a.getString(R.styleable.slider_button_text);
                setText(buttonText);
            }

            if(a.hasValue(R.styleable.slider_button_thumb)){
                Drawable thumbDrawable;
                thumbDrawable = a.getDrawable(R.styleable.slider_button_thumb);
                slideBar.setThumb(thumbDrawable);
            } else {
                slideBar.setThumb(ContextCompat.getDrawable(getContext(),R.drawable.thumb_def));
            }

            if(a.hasValue(R.styleable.slider_button_thumbOffset)){
                int offset = a.getDimensionPixelSize(R.styleable.slider_button_thumbOffset,dpToPixels(10));
                offestThumb += offset;
            }

            if(a.hasValue(R.styleable.slider_button_sliderBackground)){
                setBackgroundDrawable(a.getDrawable(R.styleable.slider_button_sliderBackground));
            } else {
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.back_slide_button));
            }

            float unitsTextSize = a.getDimensionPixelSize(R.styleable.slider_button_textSize, dpToPixels(20));

            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,unitsTextSize);

            int color = a.getColor(R.styleable.slider_button_textColor,Color.WHITE);
            textView.setTextColor(color);

            a.recycle();
        }

        setThumbOffset(offestThumb);

        /*post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = slideBar.thumb;
                if(drawable != null){
                    Drawable n = new ScaleDrawable(drawable,Gravity.CENTER,getHeight(),getHeight());
                    slideBar.setThumb(n);
                }
            }
        });*/

        this.addView(textView);
        this.addView(slideBar);
    }

    public TextView getTexView(){
        return textView;
    }


    public void setText(@StringRes int res){
        textView.setText(res);
    }

    public void setText(CharSequence charSequence){
        textView.setText(charSequence);
    }

    public void setThumb(Drawable drawable){
        slideBar.setThumb(drawable);
    }

    public void setThumbOffset(int offset){
        slideBar.setThumbOffset(offset);
    }



    protected class SlideBar extends AppCompatSeekBar {

        private Drawable thumb;
        public SlideBar(Context context) {
            super(context);
        }

        public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public SlideBar(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setThumb(Drawable thumb) {
            super.setThumb(thumb);
            this.thumb = thumb;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
                    super.onTouchEvent(event);
                } else
                    return false;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (getProgress() > 70)
                    handleSlide();

                setProgress(0);
            } else
                super.onTouchEvent(event);

            return true;
        }

        private void handleSlide() {
            if(listener != null) {
                listener.handleSlide();
            }
        }

    }

    public void setSlideButtonListener(SlideButtonListener listener) {
        this.listener = listener;
    }

    public interface SlideButtonListener {
        public void handleSlide();
    }

}