package id.sisi.postoko.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener


object AnimationUtil {
    var ANIMATION_DURATION_SHORT = 150
    var ANIMATION_DURATION_MEDIUM = 400
    var ANIMATION_DURATION_LONG = 800
    fun crossFadeViews(showView: View, hideView: View) {
        crossFadeViews(showView, hideView, ANIMATION_DURATION_SHORT)
    }

    fun crossFadeViews(showView: View, hideView: View, duration: Int) {
        fadeInView(showView, duration)
        fadeOutView(hideView, duration)
    }

    fun fadeInView(view: View) {
        fadeInView(view, ANIMATION_DURATION_SHORT)
    }

    fun fadeInView(view: View, duration: Int) {
        fadeInView(view, duration, null)
    }

    fun fadeInView(
        view: View,
        duration: Int,
        listener: AnimationListener?
    ) {
        view.setVisibility(View.VISIBLE)
        view.setAlpha(0f)
        var vpListener: ViewPropertyAnimatorListener? = null
        if (listener != null) {
            vpListener = object : ViewPropertyAnimatorListener {
                override fun onAnimationStart(view: View) {
                    if (!listener.onAnimationStart(view)) {
                        view.setDrawingCacheEnabled(true)
                    }
                }

                override fun onAnimationEnd(view: View) {
                    if (!listener.onAnimationEnd(view)) {
                        view.setDrawingCacheEnabled(false)
                    }
                }

                override fun onAnimationCancel(view: View?) {}
            }
        }
        ViewCompat.animate(view).alpha(1f).setDuration(duration.toLong()).setListener(vpListener)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun reveal(view: View, listener: AnimationListener) {
        val cx: Int = view.getWidth() - TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 24f, view.getResources().getDisplayMetrics()
        ).toInt()
        val cy: Int = view.getHeight() / 2
        val finalRadius: Int = Math.max(view.getWidth(), view.getHeight())
        val anim =
            ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
        view.setVisibility(View.VISIBLE)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                listener.onAnimationStart(view)
            }

            override fun onAnimationEnd(animation: Animator) {
                listener.onAnimationEnd(view)
            }

            override fun onAnimationCancel(animation: Animator) {
                listener.onAnimationCancel(view)
            }

            override fun onAnimationRepeat(animation: Animator) {}
        })
        anim.start()
    }

    fun fadeOutView(view: View) {
        fadeOutView(view, ANIMATION_DURATION_SHORT)
    }

    fun fadeOutView(view: View, duration: Int) {
        fadeOutView(view, duration, null)
    }

    fun fadeOutView(
        view: View,
        duration: Int,
        listener: AnimationListener?
    ) {
        ViewCompat.animate(view).alpha(0f).setDuration(duration.toLong())
            .setListener(object : ViewPropertyAnimatorListener {
                override fun onAnimationStart(view: View) {
                    if (listener == null || !listener.onAnimationStart(view)) {
                        view.setDrawingCacheEnabled(true)
                    }
                }

                override fun onAnimationEnd(view: View) {
                    if (listener == null || !listener.onAnimationEnd(view)) {
                        view.setVisibility(View.GONE)
                        view.setDrawingCacheEnabled(false)
                    }
                }

                override fun onAnimationCancel(view: View?) {}
            })
    }

    interface AnimationListener {
        /**
         * @return true to override parent. Else execute Parent method
         */
        fun onAnimationStart(view: View?): Boolean
        fun onAnimationEnd(view: View?): Boolean
        fun onAnimationCancel(view: View?): Boolean
    }
}