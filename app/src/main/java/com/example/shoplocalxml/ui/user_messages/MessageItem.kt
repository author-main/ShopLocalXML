package com.example.shoplocalxml.ui.user_messages

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.databinding.MessageItemBinding
import com.example.shoplocalxml.getStringArrayResource

class MessageItem: ConstraintLayout {
    var message: UserMessage = UserMessage()
        set(value) {
            field = value
            setUserMessage(value)
        }
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val dataBinding: MessageItemBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.message_item, this, true)
        dataBinding.message      = message
        dataBinding.eventhandler = this

        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setUserMessage(value: UserMessage) {
        dataBinding.message = value
        dataBinding.invalidateAll()
    }

    fun updateReadState(value: Int) {
        message.read = value
        dataBinding.invalidateAll()
    }

    fun changeBackgroundColor(color: Int) {
        dataBinding.layoutMainMessageItem.setBackgroundColor(color)
    }

    companion object {

        @JvmStatic
        fun getMessageIcon(message: UserMessage): Int {
            val drawable = arrayOf(
                R.drawable.ic_usermessage,
                R.drawable.ic_delivery,
                R.drawable.ic_discount,
                R.drawable.ic_gift
            )
            return drawable[message.type]
        }

        @JvmStatic
        fun getMessageTitle(message: UserMessage): String {
            val title = getStringArrayResource(R.array.typemessage)
            return title[message.type]
        }
    }
}