package com.gathin.tvhome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

/** The two appearance choices are independent and work with a TV D-pad and OK. */
public final class AppearanceSettingsActivity extends Activity {
    private LinearLayout root;
    private TextView title, explanation;
    private Switch crt, dark;
    private SharedPreferences preferences;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        preferences = VisualOptions.preferences(this);
        ScrollView scroll = new ScrollView(this);
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(70), dp(36), dp(70), dp(32));
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        scroll.addView(root);
        title = new TextView(this);
        title.setText(R.string.wii_appearance_title);
        title.setTextSize(30);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));
        explanation = new TextView(this);
        explanation.setText(R.string.wii_appearance_help);
        explanation.setTextSize(17);
        explanation.setPadding(0, dp(10), 0, dp(24));
        root.addView(explanation, new LinearLayout.LayoutParams(-1, -2));
        crt = option(R.string.wii_crt_option, VisualOptions.crt(this));
        dark = option(R.string.wii_dark_option, VisualOptions.dark(this));
        crt.setOnCheckedChangeListener((button, enabled) ->
                preferences.edit().putBoolean(VisualOptions.CRT, enabled).apply());
        dark.setOnCheckedChangeListener((button, enabled) -> {
            preferences.edit().putBoolean(VisualOptions.DARK, enabled).apply();
            refreshColors();
        });
        Button tv = new Button(this);
        tv.setText(R.string.wii_tv_settings);
        tv.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_SETTINGS)));
        root.addView(tv, rowLayout());
        Button back = new Button(this);
        back.setText(R.string.wii_back_to_menu);
        back.setOnClickListener(v -> finish());
        root.addView(back, rowLayout());
        setContentView(scroll);
        refreshColors();
        crt.requestFocus();
    }

    private Switch option(int label, boolean enabled) {
        Switch option = new Switch(this);
        option.setText(label);
        option.setTextSize(21);
        option.setChecked(enabled);
        option.setPadding(dp(18), dp(8), dp(18), dp(8));
        option.setFocusable(true);
        option.setOnFocusChangeListener((view, focused) -> {
            GradientDrawable background = new GradientDrawable();
            background.setCornerRadius(dp(12));
            background.setColor(VisualOptions.dark(this) ? 0xFF253442 : 0xFFE2EAF0);
            background.setStroke(dp(focused ? 3 : 1), focused ? 0xFF3FC0EC : 0xFF708693);
            view.setBackground(background);
        });
        root.addView(option, rowLayout());
        return option;
    }

    private LinearLayout.LayoutParams rowLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, dp(64));
        params.setMargins(0, dp(6), 0, dp(6));
        return params;
    }

    private void refreshColors() {
        boolean night = VisualOptions.dark(this);
        int ink = night ? 0xFFE1ECF3 : 0xFF20313E;
        getWindow().getDecorView().setBackgroundColor(night ? 0xFF141D27 : 0xFFF0F3F6);
        title.setTextColor(ink);
        explanation.setTextColor(night ? 0xFFACC3D1 : 0xFF526A79);
        crt.setTextColor(ink);
        dark.setTextColor(ink);
        for (Switch option : new Switch[] {crt, dark}) {
            View.OnFocusChangeListener listener = option.getOnFocusChangeListener();
            if (listener != null) listener.onFocusChange(option, option.hasFocus());
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
