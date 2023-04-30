package com.evince.evincepracticaltask.extension

import android.view.View

/*
fun View.visible(animate: Boolean = false, anim: Int = R.anim.silde_right_to_left) {
    strategy(View.VISIBLE, animate)
}
*/

/** Set the View visibility to INVISIBLE and eventually animate view alpha till 0% */
fun View.invisible(animate: Boolean = false) {
    strategy(View.INVISIBLE, animate)
}

/** Set the View visibility to GONE and eventually animate view alpha till 0% */
fun View.gone(animate: Boolean = false) {
    strategy(View.GONE, animate)
}


private fun View.strategy(hidingStrategy: Int, animate: Boolean = false) {
    if (animate) {
//        setAnimation(AnimationUtils.loadAnimation(context, R.anim.silde_left_to_right))
        visibility = hidingStrategy
    } else {
        visibility = hidingStrategy
    }
}