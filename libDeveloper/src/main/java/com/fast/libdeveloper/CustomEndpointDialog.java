package com.fast.libdeveloper;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;


final class CustomEndpointDialog extends AlertDialog {
    private final SetCustomEndpointListener setCustomEndpointListener;
    private final String url;
    private final List<ExtraUrl> extraUrls = new ArrayList<>();
    private final List<EditText> editTexts = new ArrayList<>();
    private EditText nameEditText;
    private EditText urlEditText;
    private LinearLayout layoutContent;

    CustomEndpointDialog(final Context context, String url, List<ExtraUrl> extraUrlList,
                         SetCustomEndpointListener listener) {
        super(context);

        this.setCustomEndpointListener = listener;
        this.url = url;

        if (extraUrlList != null) {
            extraUrls.addAll(extraUrlList);
        }

        setTitle(R.string.debug_custom_endpoint_dialog_title);
        setView(getLayoutInflater().inflate(R.layout.dialog_custom_endpoint, layoutContent, false));
        setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getString(R.string.debug_cancel),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (setCustomEndpointListener != null) {
                            setCustomEndpointListener.onCancel();
                        }
                    }
                });
        setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(R.string.debug_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (setCustomEndpointListener != null) {
                            List<ExtraUrl> extraUrlList = new ArrayList<>(editTexts.size());
                            for (int i = 0; i < editTexts.size(); i++) {
                                extraUrlList.add(extraUrls.get(i)
                                        .newBuilder()
                                        .url(editTexts.get(i).getText().toString().trim())
                                        .build());
                            }
                            String url = urlEditText.getText().toString().trim();
                            if ((url.startsWith("https://") || url.startsWith("http://")) && url.endsWith("/")) {
                                setCustomEndpointListener.onSetCustomEndpoint(nameEditText.getText().toString(),
                                        url,
                                        extraUrlList);
                            } else {
                                setCustomEndpointListener.onCancel();
                                Toast.makeText(context, "地址格式有误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        nameEditText = (EditText) findViewById(R.id.et_name);

        urlEditText = (EditText) findViewById(R.id.et_url);
        initEditText(urlEditText, url);

        layoutContent = (LinearLayout) findViewById(R.id.layout_content);
        initExtraEditText();
    }

    private void initEditText(EditText editText, String url) {
        if (TextUtils.isEmpty(url)) {
            editText.setText(R.string.debug_endpoint_protocol);
        } else {
            editText.setText(url);
        }
        editText.setSelection(editText.getText().length());
    }

    private void initExtraEditText() {
        for (ExtraUrl extraUrl : extraUrls) {
            View view = getLayoutInflater().inflate(R.layout.item_extra_url, layoutContent, false);

            TextView description = (TextView) view.findViewById(R.id.tv_extra_url_description);
            description.setText(extraUrl.description);

            EditText url = (EditText) view.findViewById(R.id.et_extra_url);
            initEditText(url, extraUrl.url);
            editTexts.add(url);

            layoutContent.addView(view);
        }
    }

    interface SetCustomEndpointListener {
        void onSetCustomEndpoint(String name, String baseUrl, List<ExtraUrl> extraUrlList);

        void onCancel();
    }
}
