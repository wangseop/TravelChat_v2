package com.hoingmarry.travelchat.customview;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.hoingmarry.travelchat.R;
import com.hoingmarry.travelchat.utils.AnimUtil;
import com.hoingmarry.travelchat.utils.KeyboardVisibilityEvent;
import com.hoingmarry.travelchat.utils.MediaUtils;
import com.hoingmarry.travelchat.utils.ViewUtil;
import static com.hoingmarry.travelchat.contracts.StringContract.RequestCode.ADD_GALLERY;

public class AttachmentTypeSelector extends PopupWindow {


    private static final int ANIMATION_DURATION = 300;

    @SuppressWarnings("unused")
    private static final String TAG = AttachmentTypeSelector.class.getSimpleName();

    private final @NonNull
    ImageView imageButton;
    private final @NonNull
    ImageView closeButton;
    private @Nullable
    View currentAnchor;
    private @Nullable
    AttachmentClickedListener listener;
    private Rect rect;
    private int winHeight;


    public AttachmentTypeSelector(@NonNull Context context, @Nullable AttachmentClickedListener listener) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.attachment_type_selector, null, true);
        this.listener = listener;
        this.imageButton = ViewUtil.findById(layout, R.id.gallery_button);

        this.closeButton = ViewUtil.findById(layout, R.id.close_button);

        Drawable imageDrawable = context.getResources().getDrawable(R.drawable.ic_gallery_black);
        Drawable closeDrawable = context.getResources().getDrawable(R.drawable.ic_down_arrow_black);

        this.imageButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, imageDrawable));
        this.closeButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, closeDrawable));


        this.imageButton.setOnClickListener(new PropagatingClickListener(ADD_GALLERY));

        this.closeButton.setOnClickListener(new CloseClickListener());


        setContentView(layout);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(0);
        setClippingEnabled(false);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        setFocusable(true);
        setTouchable(true);


    }


    public void show(@NonNull Activity activity, final @NonNull View anchor) {

        int y;
        if (KeyboardVisibilityEvent.isKeyboardVisible(activity)) {
            y = 0;
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } else {
            rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            winHeight = activity.getWindow().getDecorView().getHeight();
            y = winHeight - rect.bottom;
        }


        this.currentAnchor = anchor;


        showAtLocation(anchor, Gravity.BOTTOM, 0, y);


        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animateWindowInCircular(anchor, getContentView());
                } else {
                    animateWindowInTranslate(getContentView());
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateButtonIn(imageButton, ANIMATION_DURATION / 2);
            animateButtonIn(closeButton, 0);
        }
    }

    @Override
    public void dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateWindowOutCircular(currentAnchor, getContentView());
        } else {
            animateWindowOutTranslate(getContentView());
        }
    }

    public void setListener(@Nullable AttachmentClickedListener listener) {
        this.listener = listener;
    }

    private void animateButtonIn(View button, int delay) {
        AnimationSet animation = new AnimationSet(true);
        Animation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);

        animation.addAnimation(scale);
        animation.setInterpolator(new OvershootInterpolator(1));
        animation.setDuration(ANIMATION_DURATION);
        animation.setStartOffset(delay);
        button.startAnimation(animation);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateWindowInCircular(@Nullable View anchor, @NonNull View contentView) {
        Pair<Integer, Integer> coordinates = AnimUtil.getClickOrigin(anchor, contentView);
        Animator animator = ViewAnimationUtils.createCircularReveal(contentView,
                coordinates.first,
                coordinates.second,
                0,
                Math.max(contentView.getWidth(), contentView.getHeight()));
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }

    private void animateWindowInTranslate(@NonNull View contentView) {
        Animation animation = new TranslateAnimation(0, 0, contentView.getHeight(), 0);
        animation.setDuration(ANIMATION_DURATION);

        getContentView().startAnimation(animation);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateWindowOutCircular(@Nullable View anchor, @NonNull View contentView) {
        Pair<Integer, Integer> coordinates = AnimUtil.getClickOrigin(anchor, contentView);
        Animator animator = ViewAnimationUtils.createCircularReveal(getContentView(),
                coordinates.first,
                coordinates.second,
                Math.max(getContentView().getWidth(), getContentView().getHeight()),
                0);

        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AttachmentTypeSelector.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start();
    }

    private void animateWindowOutTranslate(@NonNull View contentView) {
        Animation animation = new TranslateAnimation(0, 0, 0, contentView.getTop() + contentView.getHeight());
        animation.setDuration(ANIMATION_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AttachmentTypeSelector.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        getContentView().startAnimation(animation);
    }


    private class PropagatingClickListener implements View.OnClickListener {

        private final int type;

        private PropagatingClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            animateWindowOutTranslate(getContentView());

            if (listener != null) listener.onClick(type);
        }

    }

    private class CloseClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    public interface AttachmentClickedListener {
        void onClick(int type);

    }

}