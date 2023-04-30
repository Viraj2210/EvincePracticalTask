package com.evince.evincepracticaltask.activity.ui

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.provider.SyncStateContract
import android.service.autofill.UserData
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.evince.evincepracticaltask.R
import com.evince.evincepracticaltask.activity.model.UserModel
import com.evince.evincepracticaltask.activity.vm.UserVM
import com.evince.evincepracticaltask.databinding.ActivityUserProfileBinding
import com.evince.evincepracticaltask.extension.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class UserProfileActivity : AppCompatActivity() {
    lateinit var activityUserProfileBinding: ActivityUserProfileBinding
    var userModel : UserModel.DataX ?= null
    lateinit var userVM: UserVM
    val STORAGE_PERMISSION_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(activityUserProfileBinding.root)


        init()
    }

    fun init(){
        userVM = ViewModelProvider(this).get(UserVM::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
           userModel = intent.getSerializableExtra("userModel", UserModel.DataX::class.java)
        } else {
            userModel = intent.getSerializableExtra("userModel") as UserModel.DataX?
        }


        activityUserProfileBinding.btnUpdate.setOnClickListener {
            updateata()
            finish()
        }

        activityUserProfileBinding.btnDelete.setOnClickListener {
            userVM.delUser(userModel!!)
            finish()
        }

        bindData(userModel!!)
    }

    fun bindData(userModel: UserModel.DataX){

        activityUserProfileBinding.evFName.setText(userModel.first_name)
        activityUserProfileBinding.evLName.setText(userModel.last_name)
        activityUserProfileBinding.evEmail.setText(userModel.email)
        Glide.with(this).load(userModel.avatar).into(activityUserProfileBinding.ivUserProfile)


        if (checkPermission()){
            downloadImg()
        }else{
            requestPermission()
        }

    }

    fun downloadImg(){
        downloadImage(this,userModel?.avatar!!)
    }

    fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package",this.packageName,null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            }catch (e : java.lang.Exception){
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }else{
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }


    val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()){

            }else{
                Toast.makeText(this, "External storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkPermission() : Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }else{
            val write = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty()){
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (write && read){

                }else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun updateata(){
        if (TextUtils.isEmpty(activityUserProfileBinding.evFName.text)){
            return
        }
        if (TextUtils.isEmpty(activityUserProfileBinding.evLName.text)){
            return
        }
        if (TextUtils.isEmpty(activityUserProfileBinding.evEmail.text)){
            return
        }

            var userData = UserModel.DataX(
                userModel?.avatar ?: "",
                activityUserProfileBinding.evEmail.text.toString(),
                activityUserProfileBinding.evFName.text.toString(),
                userModel?.id!!,
                activityUserProfileBinding.evLName.text.toString()
            )
            userVM.updateUser(userData)
    }


    private val DOWNLOAD_NOTIFICATION_ID = 1

    fun downloadImage(context: Context, urlString: String) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()

            val input = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(input)
            withContext(Dispatchers.Main) {
                saveImage(context, bitmap)
                sendNotification(context)
            }
        }
    }

    fun saveImage(context: Context, bitmap: Bitmap) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myImage.png")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Add the image to the gallery so it can be viewed in the device's gallery app
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri = Uri.fromFile(file)
        mediaScanIntent.data = contentUri
        context.sendBroadcast(mediaScanIntent)
    }

    fun sendNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, "download_channel")
            .setSmallIcon(R.drawable.ic_profile1)
            .setContentTitle("Image Downloaded")
            .setContentText("Your image has been downloaded successfully.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, notificationBuilder.build())
    }


}