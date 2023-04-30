package com.evince.evincepracticaltask.extension

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.*

fun FragmentActivity.popBackStack() {
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.popBackStackInclusive() {
    if (supportFragmentManager.backStackEntryCount > 0)
        supportFragmentManager.popBackStack(
            supportFragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment, fragment.javaClass.simpleName) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        replace(
            frameId,
            fragment,
            fragment.javaClass.simpleName
        )
    }
}

fun FragmentActivity.replaceFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) replace(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else
            replace(frameId, fragment, fragment.javaClass.simpleName)
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToStack: Boolean,
    clearBackStack: Boolean
) {
    supportFragmentManager.inTransaction {

        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(
                first.id,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        if (addToStack) replace(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else
            replace(frameId, fragment, fragment.javaClass.simpleName)
    }
}

fun FragmentActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    addToStack: Boolean,
    clearBackStack: Boolean,
    bundle: Bundle?=null
) {
    supportFragmentManager.inTransaction {

        if (clearBackStack && supportFragmentManager.backStackEntryCount > 0) {
            val first = supportFragmentManager.getBackStackEntryAt(0)
            supportFragmentManager.popBackStack(
                first.id,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }

        if (addToStack) replace(frameId, fragment.javaClass,bundle, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else
            replace(frameId, fragment.javaClass, bundle,fragment.javaClass.simpleName)
    }
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, addToStack: Boolean) {
    supportFragmentManager.inTransaction {
        if (addToStack) add(frameId, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
        else add(frameId, fragment)
    }
}

fun AppCompatActivity.getCurrentFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    var fragmentTag: String? = ""

    if (fragmentManager.backStackEntryCount > 0)
        fragmentTag =
            fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 1).name

    return fragmentManager.findFragmentByTag(fragmentTag)
}
// Fragment related
inline fun <reified T: Any> Fragment.getValue(lable: String, defaultvalue: T? = null) = lazy {
    val value = arguments?.get(lable)
    if (value is T) value else defaultvalue
}

inline fun <reified T: Any> Fragment.getValueNonNull(lable: String, defaultvalue: T? = null) = lazy {
    val value = arguments?.get(lable)
    requireNotNull(if (value is T) value else defaultvalue) { lable }
}


/**
 * Can show [Toast] from every [Fragment].
 */
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}
fun Fragment.showToastForSelect(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), "Please select $message", duration).show()
}
fun Fragment.showToastForEnter(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), "Please enter $message", duration).show()
}
