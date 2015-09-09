package com.tutorial.deeplayer.app.deeplayer.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

/**
 * Created by ilya.savritsky on 09.09.2015.
 */
public class Utils {
    public static void unbindDrawables(View view) {
        try {
            if (view != null) {
                if (view.getBackground() != null) {
                    view.getBackground().setCallback(null);
                    view.setBackgroundDrawable(null);
                }

                if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    imageView.setImageBitmap(null);
                } else if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    for (int i = 0; i < viewGroup.getChildCount(); i++)
                        unbindDrawables(viewGroup.getChildAt(i));

                    if (!(view instanceof AdapterView))
                        viewGroup.removeAllViews();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
