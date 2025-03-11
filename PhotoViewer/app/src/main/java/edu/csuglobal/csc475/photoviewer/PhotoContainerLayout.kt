package edu.csuglobal.csc475.photoviewer

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Custom FrameLayout that properly overrides performClick for accessibility
 */
class PhotoContainerLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun performClick(): Boolean {
        // Call super.performClick() to handle the accessibility events
        val handled = super.performClick()
        if (!handled) {
            // If not handled by parent, play the click sound
            playSoundEffect(android.view.SoundEffectConstants.CLICK)
        }
        return handled
    }
}