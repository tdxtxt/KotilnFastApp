package com.fast.libdeveloper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;
import java.io.File;
import androidx.core.app.ShareCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

final class Utils {

    static void restart(Context context) {
        ProcessPhoenix.triggerRebirth(context);
    }

    static Disposable logsShare(final Context context, LumberYard lumberYard) {
        return lumberYard.save()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        Intents.maybeStartChooser(context, sendIntent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, "Couldn't save the logs for sharing.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    static Disposable onBugReport(final Activity activity, final Report report, final File screenshot,
                                  LumberYard lumberYard) {
        if (report.includeLogs) {
            return lumberYard.save()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File logs) throws Exception {
                            submitReport(activity, report, screenshot, logs);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.e(throwable.getMessage());
                            submitReport(activity, report, screenshot, null);
                        }
                    });
        } else {
            submitReport(activity, report, screenshot, null);
        }
        return Disposables.empty();
    }

    static String getVersionCode(final Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e.getMessage());
            return "";
        }
        return String.valueOf(packageInfo.versionCode);
    }

    private static void submitReport(Activity activity, Report report, File logs, File screenshot) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(activity);
        intentBuilder.setType("message/rfc822").addEmailTo(report.email).setSubject(report.title);

        StringBuilder body = new StringBuilder();
        if (!TextUtils.isEmpty(report.description)) {
            body.append("{panel:title=Description}\n").append(report.description).append("\n{panel}\n\n");
        }
        String pkgName = activity.getPackageName();
        PackageInfo packageInfo = null;
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e.getMessage());
        }

        body.append("{panel:title=App}\n");
        body.append("Version: ")
                .append(packageInfo == null ? "" : packageInfo.versionName)
                .append('\n');
        body.append("Version code: ")
                .append(packageInfo == null ? "" : packageInfo.versionCode)
                .append('\n');
        body.append("{panel}\n\n");

        body.append("{panel:title=Device}\n");
        body.append("Make: ").append(Build.MANUFACTURER).append('\n');
        body.append("Model: ").append(Build.MODEL).append('\n');
        body.append("Resolution: ")
                .append(displayMetrics.heightPixels)
                .append("x")
                .append(displayMetrics.widthPixels)
                .append('\n');
        body.append("Density: ")
                .append(displayMetrics.densityDpi)
                .append("dpi (")
                .append(getDensityString(displayMetrics))
                .append(")\n");
        body.append("Release: ").append(Build.VERSION.RELEASE).append('\n');
        body.append("API: ").append(Build.VERSION.SDK_INT).append('\n');
        body.append("{panel}");

        intentBuilder.setText(body.toString());

        if (screenshot != null && report.includeScreenshot) {
            intentBuilder.addStream(Uri.fromFile(screenshot));
        }
        if (logs != null) {
            intentBuilder.addStream(Uri.fromFile(logs));
        }

        Intents.maybeStartActivity(activity, intentBuilder.getIntent());
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return String.valueOf(displayMetrics.densityDpi);
        }
    }
}
