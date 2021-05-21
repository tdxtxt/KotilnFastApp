package com.fast.libdeveloper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import timber.log.Timber;

final class RemoteDebugger {
    private static final String TAG = "RemoteDebugger";
    private final JSONObject jsonObject = new JSONObject();
    private final LumberYard lumberYard;
    private final OkHttpClient okHttpClient;
    private Request request;
    private boolean connecting;
    private boolean working;
    private WebSocket ws;
    private Timber.Tree tree;

    RemoteDebugger(final LumberYard lumberYard) {
        this.lumberYard = lumberYard;
        this.okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
    }

    private void tryConnect() {
        if (ws == null && !connecting) {
            connecting = true;
            okHttpClient.dispatcher().cancelAll();
            okHttpClient.newWebSocket(request, new RemoteWSListener());
        }
    }

    void start(String wsUrl) {
        this.request = new Request.Builder()
                .url(wsUrl)
                .build();
        tryConnect();
        tree = new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                tryConnect();
                if (!working && ws != null) {
                    working = true;
                    Observable.create(new ObservableOnSubscribe<List<Log>>() {
                        @Override
                        public void subscribe(ObservableEmitter<List<Log>> emitter) throws Exception {
                            List<Log> logs = lumberYard.bufferedLogs();
                            emitter.onNext(logs);
                        }
                    }).subscribeOn(Schedulers.io()).subscribe(new Observer<List<Log>>() {

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(List<Log> logs) {
                            for (Log log : logs) {
                                if (!log.sent) {
                                    try {
                                        String msg = jsonObject.put("tag", log.tag())
                                                .put("level", log.levelString())
                                                .put("message", log.rawMessage()).toString();
                                        if (ws == null) {
                                            break;
                                        }
                                        boolean sent = ws.send(msg);
                                        if (!sent) {
                                            break;
                                        }
                                        log.sent = true;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        log.sent = true;
                                    }
                                }
                            }
                            working = false;
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                }
            }
        };
        Timber.plant(tree);
    }

    void stop() {
        okHttpClient.dispatcher().cancelAll();
        connecting = false;
        ws = null;
        if (tree != null) {
            Timber.uproot(tree);
            tree = null;
        }
    }

    final class RemoteWSListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            ws = webSocket;
            connecting = false;
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            ws = null;
            connecting = false;
            android.util.Log.i(TAG, "WS closed");
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            ws = null;
            connecting = false;
            android.util.Log.i(TAG, "WS failure");
            t.printStackTrace();
        }
    }
}