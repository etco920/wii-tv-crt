package com.gathin.tvhome;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

/** The Mii channel, opened from its tile in the channel menu; Back from the plaza returns there. */
public class MiiChannelActivity extends Activity {

    private MiiChannelView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WiiHand.loadPointer(getResources());
        view = new MiiChannelView(this, () -> {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
        setContentView(view);
        view.requestFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        view.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (view.keyDown(keyCode, event.getRepeatCount(), event.getUnicodeChar())) return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (view.keyUp(keyCode)) return true;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        // Back arrives through onKeyDown; this only catches a system back the view never saw.
        view.keyDown(KeyEvent.KEYCODE_BACK, 0, 0);
    }
}
