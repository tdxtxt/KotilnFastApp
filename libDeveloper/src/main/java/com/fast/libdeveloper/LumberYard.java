package com.fast.libdeveloper;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import okio.BufferedSink;
import okio.Okio;
import timber.log.Timber;

final class LumberYard {
  private static final int BUFFER_SIZE = 200;

  private final Context context;
  private final Deque<Log> entries = new ArrayDeque<>(BUFFER_SIZE + 1);
  private final PublishProcessor<Log> entrySubject = PublishProcessor.create();

  LumberYard(Context context) {
    this.context = context.getApplicationContext();
    cleanUp(this.context);
    Timber.plant(tree());
  }

  private Timber.Tree tree() {
    return new Timber.DebugTree() {
      @Override
      protected void log(int priority, String tag, String message, Throwable t) {
        addLog(new Log(priority, tag, message));
      }
    };
  }

  private synchronized void addLog(Log entry) {
    entries.addLast(entry);
    if (entries.size() > BUFFER_SIZE) {
      entries.removeFirst();
    }

    entrySubject.onNext(entry);
  }

  List<Log> bufferedLogs() {
    return new ArrayList<>(entries);
  }

  Flowable<Log> logs() {
    return entrySubject.onBackpressureBuffer();
  }

  void clear() {
    entries.clear();
  }

  /** Save the current logs to disk. */
  Observable<File> save() {
    return Observable.create(new ObservableOnSubscribe<File>() {
      @Override
      public void subscribe(ObservableEmitter<File> emitter) throws Exception {
        File folder = context.getExternalFilesDir(null);
        if (folder == null) {
          emitter.onError(new IOException("External storage is not mounted."));
          return;
        }

        String fileName = System.currentTimeMillis() + ".log";
        File output = new File(folder, fileName);

        BufferedSink sink = null;
        try {
          sink = Okio.buffer(Okio.sink(output));
          List<Log> entries = bufferedLogs();
          for (Log entry : entries) {
            sink.writeUtf8(entry.message()).writeByte('\n');
          }
          // need to close before emiting file to the subscriber, because when subscriber receives data in the same thread
          // the file may be truncated
          sink.close();
          sink = null;

          emitter.onNext(output);
          emitter.onComplete();
        } catch (IOException e) {
          emitter.onError(e);
        } finally {
          if (sink != null) {
            try {
              sink.close();
            } catch (IOException e) {
              emitter.onError(e);
            }
          }
        }
      }
    });
  }

  /**
   * Delete all of the log files saved to disk. Be careful not to call this before any intents have
   * finished using the file reference.
   */
  private static void cleanUp(final Context context) {
    Observable.just(context.getApplicationContext()).map(new Function<Context, Object>() {
      @Override
      public Object apply(Context context) throws Exception {
        boolean result = true;
        File folder = context.getExternalFilesDir(null);
        if (folder != null) {
          for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".log")) {
              result = file.delete();
            }
          }
        }
        return result;
      }
    }).subscribeOn(Schedulers.io()).subscribe();
  }
}
