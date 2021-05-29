package com.library.todolist

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.Window
import java.lang.Exception


object ViewUtility {
    fun changeVisibility(visibleViews: List<View>? = null, goneViews: List<View>? = null) {
        visibleViews?.forEach {
            it.visibility = View.VISIBLE
        }

        goneViews?.forEach {
            it.visibility = View.GONE
        }
    }

    fun showDialog(context: Context?, layoutId: Int): AlertDialog? {
        try {
            context?.let {
                val alertDialog: AlertDialog
                val builder = AlertDialog.Builder(context)
                val view = View.inflate(context, layoutId, null)

                builder.setView(view)

                alertDialog = builder.create()
                alertDialog.apply {
                    requestWindowFeature(Window.FEATURE_NO_TITLE)
                    alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                    alertDialog.show()
                }

                return alertDialog
            }

            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}