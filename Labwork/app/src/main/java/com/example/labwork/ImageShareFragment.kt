package com.example.labwork

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker

class ImageShareFragment : Fragment(R.layout.fragment_image_share) {

    private var selectedImageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_pick_image).setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun openGalleryForImage() {
        ImagePicker.with(this)
            .galleryOnly()
            .crop()
            .start()
    }

    private fun postImageToInstagram(uri: Uri) {
        try {
            Log.d("InstagramPost", "Selected image URI: $uri")

            val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
                type = "image/jpeg"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setPackage("com.instagram.android")
            }

            requireContext().packageManager.queryIntentActivities(intent, 0).forEach { resolveInfo ->
                requireContext().grantUriPermission(resolveInfo.activityInfo.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            if (intent.resolveActivity(requireContext().packageManager) != null) {
                Log.d("InstagramPost", "Launching Instagram story share")
                startActivity(intent)
            } else {
                Log.e("InstagramPost", "Instagram app not found")
                Toast.makeText(requireContext(), "Instagram not installed", Toast.LENGTH_SHORT).show()
                redirectToPlayStore()
            }
        } catch (e: Exception) {
            Log.e("InstagramPost", "Error sharing image: ${e.message}")
            Toast.makeText(requireContext(), "Failed to share: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun redirectToPlayStore() {
        val storeIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.instagram.android"))
        if (storeIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(storeIntent)
        } else {
            Toast.makeText(requireContext(), "Play Store unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data?.data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let { postImageToInstagram(it) }
        }
    }
}
