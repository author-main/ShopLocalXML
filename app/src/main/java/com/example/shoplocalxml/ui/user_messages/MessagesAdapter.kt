package com.example.shoplocalxml.ui.user_messages

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.ProductItem
import com.example.shoplocalxml.ui.product_item.ProductsAdapter

class MessagesAdapter(private val context: Context, private val listMessages: MutableList<UserMessage> = mutableListOf()): RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    private var onMessageItemListener: OnMessageItemListener? = null
    fun setOnMessageItemListener(value: OnMessageItemListener) {
        onMessageItemListener = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MessageItem(context)
        return ViewHolder(view, onMessageItemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMessages[position])
    }

    override fun getItemCount(): Int {
        return listMessages.size
    }

    class ViewHolder(private val view: View, private val onMessageItemListener: OnMessageItemListener?): RecyclerView.ViewHolder(view) {
        fun bind(message: UserMessage) {
            val item = view as MessageItem
            item.message = message
            item.setOnClickListener {
                if (message.read == 1) message.read = 0 else message.read = 1
                if (message.read == 1) {
                    item.updateReadState(message.read)
                    onMessageItemListener?.onClick(adapterPosition)
                    //log(listMessages)
                }
            }
        }
    }
}