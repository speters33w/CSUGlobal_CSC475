@file:Suppress("PrivatePropertyName")

package edu.csuglobal.csc475.photoviewer

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlin.math.abs
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.view.isVisible

class PhotoDetailActivity : AppCompatActivity() {
    
    private lateinit var photoImageView: ImageView
    private lateinit var nextImageView: ImageView
    private lateinit var containerLayout: FrameLayout
    private lateinit var gestureDetector: GestureDetector
    private var currentPhotoIndex: Int = 0
    private lateinit var photosList: ArrayList<String>
    private val animationDuration = 300L // Animation duration in milliseconds
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        
        containerLayout = findViewById(R.id.photo_container)
        photoImageView = findViewById(R.id.detail_photo_image)
        
        // Create a second ImageView for smooth transitions
        nextImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
            isVisible = false
        }
        containerLayout.addView(nextImageView)
        
        // Get the photo URI and index from the intent
        val photoUri = intent.getStringExtra("PHOTO_URI") ?: return
        photosList = intent.getStringArrayListExtra("PHOTOS_LIST") ?: arrayListOf(photoUri)
        currentPhotoIndex = intent.getIntExtra("PHOTO_INDEX", 0)
        
        // Load the full-size image
        loadPhoto(currentPhotoIndex)
        
        // Set up gesture detector for swipe detection
        gestureDetector = GestureDetector(this, PhotoGestureListener())
        
        // Set touch listener on the ImageView to handle both clicks and swipes
        containerLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }
    
    private fun loadPhoto(index: Int) {
        if (index in photosList.indices) {
            Glide.with(this)
                .load(photosList[index])
                .into(photoImageView)
            currentPhotoIndex = index
        }
    }
    
    private fun animateToNextPhoto(index: Int, direction: Int) {
        if (index !in photosList.indices) return
        
        // Load the next image into the secondary ImageView
        Glide.with(this)
            .load(photosList[index])
            .into(nextImageView)
        
        // Set initial position for animation
        nextImageView.translationX = direction * containerLayout.width.toFloat()
        nextImageView.isVisible = true
        
        // Animate both views
        photoImageView.animate()
            .translationX(-direction * containerLayout.width.toFloat())
            .setDuration(animationDuration)
            .setInterpolator(DecelerateInterpolator())
            .start()
            
        nextImageView.animate()
            .translationX(0f)
            .setDuration(animationDuration)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                // Swap the views
                photoImageView.translationX = 0f
                Glide.with(this)
                    .load(photosList[index])
                    .into(photoImageView)
                nextImageView.isVisible = false
                currentPhotoIndex = index
            }
            .start()
    }
    
    // Gesture listener for swipe detection
    private inner class PhotoGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
        
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            finish() // Return to thumbnail view on tap
            return true
        }
        
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 == null) return false
            
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y
            
            if (abs(diffX) > abs(diffY) && 
                abs(diffX) > SWIPE_THRESHOLD && 
                abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                
                if (diffX > 0) {
                    // Swipe right - show previous photo
                    if (currentPhotoIndex > 0) {
                        animateToNextPhoto(currentPhotoIndex - 1, 1) // 1 for right direction
                    }
                } else {
                    // Swipe left - show next photo
                    if (currentPhotoIndex < photosList.size - 1) {
                        animateToNextPhoto(currentPhotoIndex + 1, -1) // -1 for left direction
                    }
                }
                return true
            }
            return false
        }
    }
}