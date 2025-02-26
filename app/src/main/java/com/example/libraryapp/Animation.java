package com.example.libraryapp;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

public class Animation {
    public void animateButtonTint(Button button) {
        int startColor = button.getBackgroundTintList() != null
                ? button.getBackgroundTintList().getDefaultColor()
                : button.getSolidColor();

        int endColor = (startColor == Color.parseColor("#00A950")) ? Color.parseColor("#036632")
                : (startColor == Color.parseColor("#35C2C1")) ? Color.parseColor("#019CB4")
                : Color.parseColor("#6D6D6D");

        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnim.setDuration(400);
        colorAnim.addUpdateListener(animator -> {
            int animatedColor = (int) animator.getAnimatedValue();
            button.setBackgroundTintList(ColorStateList.valueOf(animatedColor));
        });
        colorAnim.start();
    }

    public void animateHyperLink(TextView button) {
        int startColor = button.getCurrentTextColor();
        int endColor = Color.parseColor("#019CB4");

        ValueAnimator colorAnim = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
        colorAnim.setDuration(50);
        colorAnim.addUpdateListener(animator -> {
            int animatedColor = (int) animator.getAnimatedValue();
            button.setTextColor(animatedColor);
        });
        colorAnim.start();
    }
}
