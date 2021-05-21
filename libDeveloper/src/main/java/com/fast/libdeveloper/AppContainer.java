package com.fast.libdeveloper;

import android.app.Activity;
import android.view.ViewGroup;

public interface AppContainer {
    AppContainer DEFAULT = new AppContainer() {
        @Override
        public ViewGroup bind(Activity activity) {
            return (ViewGroup) activity.findViewById(android.R.id.content);
        }

        @Override
        public void openDrawerLayout() {

        }
    };

    ViewGroup bind(Activity activity);

    void openDrawerLayout();
}
