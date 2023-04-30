package com.evince.evincepracticaltask.activity.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
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


        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        registerReceiver(downloadReceiver, intentFilter)


        activityUserProfileBinding.btnUpdate.setOnClickListener {
            updateata()
            finish()
        }

        activityUserProfileBinding.btnDelete.setOnClickListener {
            userVM.delUser(userModel!!)
            finish()
        }

        activityUserProfileBinding.constUserProfile.setOnClickListener {
            if (checkPermission()){
                downloadImg()
            }else{
                requestPermission()
            }
        }

        bindData(userModel!!)
    }

    fun bindData(userModel: UserModel.DataX){

        activityUserProfileBinding.evFName.setText(userModel.first_name)
        activityUserProfileBinding.evLName.setText(userModel.last_name)
        activityUserProfileBinding.evEmail.setText(userModel.email)
        Glide.with(this).load(userModel.avatar).into(activityUserProfileBinding.ivUserProfile)


    }

    fun downloadImg(){
        val request = DownloadManager.Request(Uri.parse(userModel!!.avatar))
            .setTitle("Image Download")
            .setDescription("Downloading image")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Image.jpg")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)


    }

    private val downloadReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("Range")
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val query = DownloadManager.Query().setFilterById(id)
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        val fileUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                        showDownloadNotification(fileUri)
                    }
                }
                cursor.close()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDownloadNotification(fileUri: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setContentTitle("Image Downloaded")
            .setContentText("The image has been downloaded successfully.")
            .setSmallIcon(R.drawable.ic_profile1)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeFile(fileUri)))

        notificationManager.notify(1, notificationBuilder.build())
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




    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }


}