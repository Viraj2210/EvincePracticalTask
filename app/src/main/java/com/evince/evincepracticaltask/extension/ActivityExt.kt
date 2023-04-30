package com.evince.evincepracticaltask.extension

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.progressindicator.CircularProgressIndicator

/**
 * Can show [Toast] from every [Activity].
 */
fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

fun Activity.showToastForSelect(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, "Please select $message", duration).show()
}

fun Activity.showToastForEnter(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, "Please enter $message", duration).show()
}

fun Activity.showToastForValid(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, "Please enter valid $message", duration).show()
}

fun Activity.hideKeyboard(v : View){
    val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken,0)
}

/**
 * Kotlin Extensions for simpler, easier and funw way
 * of launching of Activities
 */

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {},
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {},
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

/*
// Simple Activities
launchActivity<UserDetailActivity>()

// Add Intent extras
launchActivity<UserDetailActivity> {
    putExtra(INTENT_USER_ID, user.id)
}

// Add custom flags
launchActivity<UserDetailActivity> {
    putExtra(INTENT_USER_ID, user.id)
    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
}

// Add Shared Transistions
val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, avatar, "avatar")
launchActivity<UserDetailActivity>(options = options) {
    putExtra(INTENT_USER_ID, user.id)
}

// Add requestCode for startActivityForResult() call
launchActivity<UserDetailActivity>(requestCode = 1234) {
    putExtra(INTENT_USER_ID, user.id)
}*/

inline fun <reified T : Any> Activity.getValue(
    lable: String, defaultvalue: T? = null,
) = lazy {
    val value = intent?.extras?.get(lable)
    if (value is T) value else defaultvalue
}

inline fun <reified T : Any> Activity.getValueNonNull(
    lable: String, defaultvalue: T? = null,
) = lazy {
    val value = intent?.extras?.get(lable)
    requireNotNull((if (value is T) value else defaultvalue)) { lable }
}

/*fun Activity.showProgress(value : Int,context: Context){
    var dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!.addFlags(2)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.dialog_progress)

    var progress : CircularProgressIndicator = findViewById(R.id.progress)


    if (value == 0) {
        progress.show()
        dialog.show()
    }else{

        dialog.dismiss()
    }*/

/**/
/*
fun showProgress(value: Int? = 0, dialog : Dialog) {
    //dialog = Dialog(context)
//    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
   // dialog.window!!.addFlags(2)
    dialog.setCancelable(true)
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.dialog_progress)

    val progress: CircularProgressIndicator = dialog.findViewById(R.id.progress)
    if (value == 0) {
        progress.show()
        dialog.show()
    } else if (value == 1){
            progress.hide()
            dialog.dismiss()
    }else{
        if (dialog.isShowing) {
            progress.hide()
            dialog.dismiss()
        }
    }
*/
//}


