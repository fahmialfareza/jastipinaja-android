package com.dinokeylas.jastipinaja

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView

/**
 * @author Aby Fajar(www.androidtinker.com)
 * abyfajar@gmail.com
 * on 03 December 2018
 */
class ConfirmationDialog(
    context: Context,
    title: String? = "",
    description: String? = "",
    okText: String? = "OK",
    cancelText: String? = "CANCEL",
    private var listener: ConfirmationDialogListener? = null,
    cancelOutside: Boolean = true
) {
    private val dialog: Dialog = Dialog(context)


    private constructor(builder: Builder) : this(
        builder.context,
        builder.title,
        builder.description,
        builder.okText,
        builder.cancelText,
        builder.listener,
        builder.cancelOutside
    )

    class Builder(var context: Context) {
        var title: String? = ""
            private set

        var description: String = ""
            private set

        var okText: String = "OK"
            private set

        var cancelText: String = "CANCEL"
            private set

        var listener: ConfirmationDialogListener? = null
            private set

        var cancelOutside: Boolean = false
            private set

        fun setTitle(_title: String) = apply { this.title = _title }

        fun setDescription(_description: String) = apply { this.description = _description }

        fun setOkText(_okText: String) = apply { this.okText = _okText }

        fun setCancelText(_cancelText: String) = apply { this.cancelText = _cancelText }

        fun setListener(_listener: ConfirmationDialogListener) = apply {
            this.listener = _listener
        }

        fun setCancelOutside(_cancelOutside: Boolean) = apply { this.cancelOutside = _cancelOutside }

        fun build() = ConfirmationDialog(this)
    }

    interface ConfirmationDialogListener {
        fun setOnOkListener()
        fun setOnCancelListener()
    }

    init {
        dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_confirmation)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(cancelOutside)

        val root = dialog.findViewById<ViewGroup>(R.id.parentLyt)
        val titleTxt = dialog.findViewById<TextView>(R.id.titleTxt)
        val contentTxt = dialog.findViewById<TextView>(R.id.contentTxt)
        val cancelLyt = dialog.findViewById<TextView>(R.id.cancelLyt)
        val okLyt = dialog.findViewById<TextView>(R.id.okLyt)

        titleTxt.text = title
        @Suppress("DEPRECATION")
        contentTxt.text = Html.fromHtml(description)
        okText?.let {
            okLyt.text = okText
        }

        cancelText?.let {
            cancelLyt.text = cancelText
        }

        okLyt.setOnClickListener {
            dialog.dismiss()
            listener?.setOnOkListener()
        }

        cancelLyt.setOnClickListener {
            dialog.dismiss()
            listener?.setOnCancelListener()
        }

        dialog.setOnCancelListener { dialog ->

            dialog.dismiss()
            listener?.setOnCancelListener()
        }

    }

    fun setListener(mCallBack: ConfirmationDialogListener) {
        this.listener = mCallBack
    }

    fun show(): ConfirmationDialog? {
        dialog.show()
        return null
    }
}