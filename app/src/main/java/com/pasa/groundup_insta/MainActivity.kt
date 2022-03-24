package com.pasa.groundup_insta

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE

        findViewById<Button>(R.id.btnSubmit).setOnClickListener{

            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            val user = ParseUser.getCurrentUser()
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE

            if(photoFile != null) {

                submitPost(user, description, photoFile!!)

            }

            else {

            }
        }

        findViewById<Button>(R.id.btnPhoto).setOnClickListener {

            onLaunchCamera()
        }
    }

    private fun submitPost(user: ParseUser, description: String, image:File) {

        findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
        val post = Post()
        post.setUser(user)
        post.setDescription(description)
        post.setImage(ParseFile(image))
        post.saveInBackground { e ->

            if (e != null) {

                Log.e(TAG, "Error! Please try again")
                e.printStackTrace()

            }

            else {

                Log.i(TAG, "Success")
                findViewById<ProgressBar>(R.id.progressBar).visibility = View.INVISIBLE
            }
        }
    }

    private fun queryPost() {

        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)

        query.findInBackground { _, e ->
            if (e != null) {

                Log.e(TAG, "Error! Please try again")

            } else {

            }
        }
    }

    private fun onLaunchCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFileUri(photoFileName)

        if (photoFile != null) {

            val fileProvider: Uri = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (intent.resolveActivity(packageManager) != null) {

                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    private fun getPhotoFileUri(fileName: String): File {

        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {

            Log.d(TAG, "Error! Please try again")
        }

        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)

                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)

            }

            else {

                Toast.makeText(this, "Error! Please try again", Toast.LENGTH_SHORT).show()
            }
        }
    }
}