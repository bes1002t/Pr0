package com.pr0gramm.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.pr0gramm.app.R;
import com.pr0gramm.app.feed.ContentType;
import com.pr0gramm.app.ui.fragments.FeedFragment;

import java.util.Collection;
import java.util.EnumSet;

/**
 */
public class ContentTypeDrawable extends Drawable {
    private final String text;
    private float textSize;

    public ContentTypeDrawable(Context context, Collection<ContentType> types) {
        textSize = 16f;

        EnumSet<ContentType> cleaned = ContentType.cleaned(types);
        boolean abbreviate = cleaned.size() == 3;

        String text;
        if (abbreviate) {
            text = context.getString(R.string.all);
        } else {
            text = FluentIterable.from(cleaned)
                    .transform(type -> type.name().toLowerCase())
                    .join(Joiner.on("\n"));
        }

        if (FeedFragment.isAudioAvailable()) {
            // add "audio" or "quiet" flag
            if (types.contains(ContentType.AUDIO)) {
                text += "\n" + context.getString(R.string.audio_on);
            } else {
                text += "\n" + context.getString(R.string.audio_off);
            }
        }

        this.text = text;
    }

    @Override
    public int getIntrinsicWidth() {
        return 96;
    }

    @Override
    public int getIntrinsicHeight() {
        return 96;
    }

    public void setTextSize(float dp) {
        this.textSize = dp;
    }

    public float getTextSize() {
        return textSize;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect rect = getBounds();

        TextPaint tp = new TextPaint();
        tp.setColor(Color.WHITE);
        tp.setTextSize(textSize);
        tp.setTypeface(Typeface.DEFAULT_BOLD);
        tp.setAntiAlias(true);

        StaticLayout layout = new StaticLayout(text, tp, canvas.getWidth(),
                Layout.Alignment.ALIGN_CENTER, 0.8f, 0, false);

        canvas.save();

        // center the text on the icon
        canvas.translate(
                0.5f * (rect.width() - layout.getWidth()),
                0.5f * (rect.height() - layout.getHeight()));

        layout.draw(canvas);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        // do nothing
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // yea, just do nothing..
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
