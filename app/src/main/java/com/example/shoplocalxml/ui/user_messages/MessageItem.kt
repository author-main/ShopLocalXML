package com.example.shoplocalxml.ui.user_messages

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.custom_view.CheckableImageView
import com.example.shoplocalxml.databinding.MessageItemBinding
import com.example.shoplocalxml.databinding.ProductCardBinding
import com.example.shoplocalxml.getStringArrayResource
import com.example.shoplocalxml.log

/*@BindingMethods(
    BindingMethod(type = MessageItem::class, attribute = "app:onClickItem",  method = "setOnClickItem"),
    BindingMethod(type = MessageItem::class, attribute = "app:onDeleteItem", method = "setOnDeleteItem")
)*/
class MessageItem: ConstraintLayout {
/*    private var onMessageItemListener: OnMessageItemListener? = null
    fun setOnMessageItemListener(value: OnMessageItemListener) {
        onMessageItemListener = value
    }*/
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

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


   /* interface OnClickItem {
        fun onClickItem(index: Int)
    }

    interface OnDeleteItem {
        fun onDeleteItem(index: Int)
    }


    private var onClickItem: OnClickItem? = null
    private var onDeleteItem: OnDeleteItem? = null

    fun setOnClickItem(value: OnClickItem) {
        onClickItem = value
    }

    fun setOnDeleteItem(value: OnDeleteItem) {
        onDeleteItem = value
    }*/

    private lateinit var dataBinding: MessageItemBinding
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataBinding =
            DataBindingUtil.inflate(inflater, com.example.shoplocalxml.R.layout.message_item, this, true)
        dataBinding.message      = message
        dataBinding.eventhandler = this
    }

    private fun setUserMessage(value: UserMessage) {
        dataBinding.message = value
        dataBinding.invalidateAll()
    }

    fun updateReadState(value: Int) {
        message.read = value
        dataBinding.invalidateAll()
    }

    companion object {

        @JvmStatic
        fun getMessageIcon(message: UserMessage): Int {
          /*val USER_MESSAGE_NORMAL           = 0
            val USER_MESSAGE_DELIVERY         = 1
            val USER_MESSAGE_DISCOUNT         = 2
            val USER_MESSAGE_GIFT             = 3  */
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