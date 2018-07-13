package example.wyf.com.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.base.muslim.tipsdialog.TipsDialog
import com.bumptech.glide.Glide
import com.wyf.pictures.camera.OnPicturePathListener
import com.wyf.pictures.camera.PictureCapture
import com.wyf.pictures.camera.utils.Photo
import com.wyf.pictures.compression.PictureCompression
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            PictureCapture.getCompressionPicture(this, object : OnPicturePathListener {
                override fun onPhoto(photo: Photo) {
                    Glide.with(this@MainActivity)
                            .load(photo.compressionFile)
                            .into(ImageView)
                    Log.v("==yufei==", "originalFile = ${photo.originalFile}")
                    Log.v("==yufei==", "compressionFile = ${photo.compressionFile}")
                    Log.v("==yufei==", "cropFile = ${photo.cropFile}")
                    Log.v("==yufei==", "pictureType = ${photo.pictureType}")
                }
            }, true)
        }

        button1.setOnClickListener {
//            PictureCapture.getPicture(this, object : OnPicturePathListener {
//                override fun onPhoto(photo: Photo) {
//                    PictureCompression.compressionPictureRx(this@MainActivity, photo, object : OnPicturePathListener {
//                        override fun onPhoto(photo: Photo) {
//                            Glide.with(this@MainActivity)
//                                    .load(photo.compressionFile)
//                                    .into(ImageView1)
//                            Log.v("==yufei==", "originalFile = ${photo.originalFile}")
//                            Log.v("==yufei==", "compressionFile = ${photo.compressionFile}")
//                            Log.v("==yufei==", "cropFile = ${photo.cropFile}")
//                            Log.v("==yufei==", "pictureType = ${photo.pictureType}")
//                        }
//                    })
//                }
//            }, false)
            TipsDialog(this,object :TipsDialog.OnDropBtnClickListener{
                override fun onClick(v: View, type: Any) {

                }
            }).showDialog("OK","aaaaa")
        }
    }

}
