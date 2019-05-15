package com.jemer.atong.view.guide.transformer;

import android.view.View;

public class StackTransformer extends ABaseTransformer {
    public StackTransformer() {
    }

    protected void onTransform(View view, float position) {
        view.setTranslationX(position < 0.0F ? 0.0F : (float)(-view.getWidth()) * position);
    }
}
