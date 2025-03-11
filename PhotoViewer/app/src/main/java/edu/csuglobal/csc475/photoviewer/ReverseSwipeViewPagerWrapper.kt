package edu.csuglobal.csc475.photoviewer

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.Field

/**
 * A wrapper for ViewPager2 that reverses the swipe direction
 */
class ReverseSwipeViewPagerWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewPager2: ViewPager2

    init {
        // Inflate the layout with ViewPager2
        LayoutInflater.from(context).inflate(R.layout.layout_reverse_viewpager, this, true)
        viewPager2 = findViewById(R.id.viewPager)
        
        // Reverse the swipe direction
        reverseScrollDirection()
    }

    /**
     * Reverses the scroll direction of ViewPager2 using reflection
     * to access the internal RecyclerView
     */
    private fun reverseScrollDirection() {
        try {
            // Access the internal RecyclerView
            val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
            recyclerViewField.isAccessible = true
            val recyclerView = recyclerViewField.get(viewPager2) as RecyclerView
            
            // Reverse the layout direction
            recyclerView.layoutDirection = RecyclerView.LAYOUT_DIRECTION_RTL
            
            // Add a page transformer to fix the visual transition
            viewPager2.setPageTransformer { page, position ->
                page.translationX = -position * page.width
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}