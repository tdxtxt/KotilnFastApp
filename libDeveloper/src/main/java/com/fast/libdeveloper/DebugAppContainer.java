package com.fast.libdeveloper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import androidx.core.view.GravityCompat;


public final class DebugAppContainer implements AppContainer {

    private static WeakReference<DebugAppContainer> instance = null;
    private final Context context;
    private final Endpoint endpoint;
    private final LumberYard lumberYard;
    private DebugDrawerLayout debugDrawerLayout;

    private DebugAppContainer(Context context, Endpoint endpoint) {
        if (endpoint == null) {
            throw new NullPointerException("endpoint cannot be null");
        }
        this.context = context.getApplicationContext();
        this.endpoint = endpoint;
        this.lumberYard = new LumberYard(this.context);
    }

    public static DebugAppContainer getInstance(Context context, Endpoint endpoint) {
        if (instance == null) {
            synchronized (DebugAppContainer.class) {
                if (instance == null) {
                    instance = new WeakReference<>(new DebugAppContainer(context, endpoint));
                }
            }
        }else if(instance.get() == null){
            synchronized (DebugAppContainer.class) {
                if (instance == null) {
                    instance = new WeakReference<>(new DebugAppContainer(context, endpoint));
                }
            }
        }
        return instance.get();
    }

    public DebugEnvironment debugEnvironment() {
        int index = EnvironmentConfigLayout.getEndpointIndex(context, 0);
        return DebugEnvironment.builder()
                .index(index).name(EnvironmentConfigLayout.getEndpointName(context, endpoint.name(index)))
                .url(EnvironmentConfigLayout.getEndpointUrl(context, endpoint.url(index)))
                .extraUrls(EnvironmentConfigLayout.getEndpointExtraUrl(context, endpoint.extraUrls(index)))
                .build();
    }

    public int mockDelayMillis() {
        return EnvironmentConfigLayout.getMockDelayMilliseconds(context);
    }

    @Override
    public ViewGroup bind(final Activity activity) {
        ViewGroup parent = activity.findViewById(android.R.id.content);
        LayoutInflater.from(activity).inflate(R.layout.layout_debug_drawer, parent, true);
        debugDrawerLayout = parent.findViewById(R.id.layout_debug_drawer);
        debugDrawerLayout.setLumberYard(lumberYard);
        debugDrawerLayout.setEndpoint(endpoint);
        return (ViewGroup) debugDrawerLayout.findViewById(R.id.content);
    }

    @Override
    public void openDrawerLayout() {
        debugDrawerLayout.openDrawer(GravityCompat.START);
    }
}
