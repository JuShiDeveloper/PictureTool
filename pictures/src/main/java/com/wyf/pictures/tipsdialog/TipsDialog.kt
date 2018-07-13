package com.base.muslim.tipsdialog

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.wyf.pictures.R
import com.wyf.pictures.tipsdialog.BaseDialog
import kotlinx.android.synthetic.main.exit_dialog_layout.*

/**
 * 弹窗
 */
class TipsDialog : BaseDialog {
    private lateinit var listener: OnDropBtnClickListener
    private lateinit var adapter: TipsDialogAdapter
    private var type: Any = "type"
    private var okBtnText: String = context.getString(R.string.tips_dialog_Drop)

    constructor(context: Context, listener: OnDropBtnClickListener) : super(context) {
        this.listener = listener
        setDialogSystemLine()
        init(R.layout.exit_dialog_layout)
    }

    /**
     * 适配低版本顶部的出现的蓝色横线问题
     */
    private fun setDialogSystemLine() {
        try {
            var divierId = context.resources.getIdentifier("android:id/titleDivider", null, null)
            val divider: View = this.findViewById(divierId)
            divider.setBackgroundColor(Color.TRANSPARENT) //横线透明色
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initResource() {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun initWidget() {
        Cancel_btn.setOnClickListener(this)
        Drop_btn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.Cancel_btn -> dismiss()
            R.id.Drop_btn -> {
                dismiss()
                if (listener != null)
                    listener.onClick(Drop_btn!!, type)
            }
        }
    }

    private fun setListViewHeight(tips: Array<out String>?) {
        if (tips == null)
            return
        if (tips.size > 4) {
            var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 480)
            ExitDialog_ListView.layoutParams = params
        }
    }

    /**
     * 显示弹窗
     * 参数1：确定按钮的文字，默认为Drop
     * 参数2：可变参数，弹窗的提示内容
     */
    fun showDialog(okBtnText: String?, vararg tipsText: String): TipsBuilder {
        setListViewHeight(tipsText)
        okBtnText?.let { this.okBtnText = okBtnText }
        Drop_btn.text = this.okBtnText
        adapter = TipsDialogAdapter(tipsText.toList(), context)
        ExitDialog_ListView.adapter = adapter
        show()
        return TipsBuilder()
    }

    /**
     * 显示弹窗
     * 参数1：确定按钮的文字，默认为Drop
     * 参数2：设置窗口类型
     * 参数3：可变参数，弹窗的提示内容
     */
    fun showDialog(okBtnText: String?, type: Any, vararg tipsText: String): TipsBuilder {
        setListViewHeight(tipsText)
        okBtnText?.let { this.okBtnText = okBtnText }
        Drop_btn.text = this.okBtnText
        adapter = TipsDialogAdapter(tipsText.toList(), context)
        ExitDialog_ListView.adapter = adapter
        this.type = type
        show()
        return TipsBuilder()
    }

    /**
     * 显示弹窗
     */
    fun showDialog(): TipsBuilder {
        setListViewHeight(null)
        show()
        return TipsBuilder()
    }

    /**
     * 同一个页面多个弹窗，设置弹窗类型，该类型值在点击确定按钮时返回
     */
    fun setDialogType(type: Any) {
        this.type = type
    }

    interface OnDropBtnClickListener {
        fun onClick(v: View, type: Any)
    }

    inner class TipsBuilder {
        fun setTextColor(color: Int) {
            Drop_btn.setTextColor(color)
            Cancel_btn.setTextColor(color)
            adapter.setContentColor(color)
        }

        fun setTextColor(okBtnColor: Int, cancelBtnColor: Int, contentColor: Int) {
            Drop_btn.setTextColor(okBtnColor)
            Cancel_btn.setTextColor(cancelBtnColor)
            adapter.setContentColor(contentColor)
        }

    }

}