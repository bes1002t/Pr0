package com.pr0gramm.app.ui.views.viewer;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.pr0gramm.app.ActivityComponent;
import com.pr0gramm.app.R;
import com.pr0gramm.app.util.AndroidUtility;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import static com.pr0gramm.app.util.AndroidUtility.endAction;

/**
 */
@SuppressLint("ViewConstructor")
public class DelayedMediaView extends ProxyMediaView {
    private final View overlay;
    private final AtomicBoolean childCreated = new AtomicBoolean();

    @Inject
    Picasso picasso;

    DelayedMediaView(Config config) {
        super(config);
        hideBusyIndicator();

        overlay = LayoutInflater.from(getContext()).inflate(R.layout.player_delayed_overlay, this, false);

        // Display the overlay in a smooth animation
        overlay.setAlpha(0);
        overlay.setScaleX(0.8f);
        overlay.setScaleY(0.8f);
        overlay.animate()
                .alpha(1).scaleX(1).scaleY(1)
                .setStartDelay(300).start();

        addView(overlay);
    }

    @Override
    protected void injectComponent(ActivityComponent component) {
        component.inject(this);
    }

    @Override
    protected boolean onSingleTap(MotionEvent event) {
        // call this function only exactly once!
        if (!childCreated.compareAndSet(false, true))
            return false;

        // create the real view as a child.
        MediaView mediaView = MediaViews.newInstance(ImmutableConfig.copyOf(config));

        mediaView.removePreviewImage();
        setChild(mediaView);

        overlay.animate()
                .alpha(0).scaleX(0.8f).scaleY(0.8f)
                .setListener(endAction(() -> AndroidUtility.removeView(overlay)))
                .start();

        return true;
    }
}
