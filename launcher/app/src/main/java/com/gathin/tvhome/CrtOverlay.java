package com.gathin.tvhome;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

/** Static CRT glass texture: two cached GPU draws, no blur, frame capture or animation timer. */
final class CrtOverlay {
    private final Paint raster = new Paint();
    private final Paint glass = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int width, height;

    CrtOverlay() {
        Bitmap tile = Bitmap.createBitmap(6, 4, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                int rgb = x % 3;
                int color = y == 3 ? 0x48000000 : y == 2 ? 0x14000000
                        : Color.argb(9, rgb == 0 ? 255 : 0, rgb == 1 ? 255 : 0, rgb == 2 ? 255 : 0);
                tile.setPixel(x, y, color);
            }
        }
        raster.setShader(new BitmapShader(tile, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
    }

    void draw(Canvas canvas, int w, int h) {
        if (w <= 0 || h <= 0) return;
        if (width != w || height != h) {
            width = w;
            height = h;
            glass.setShader(new RadialGradient(w * 0.5f, h * 0.44f, w * 0.59f,
                    new int[] {0x00FFFFFF, 0x03000000, 0x45000000},
                    new float[] {0f, 0.55f, 1f}, Shader.TileMode.CLAMP));
        }
        canvas.drawRect(0, 0, w, h, raster);
        canvas.drawRect(0, 0, w, h, glass);
    }
}
