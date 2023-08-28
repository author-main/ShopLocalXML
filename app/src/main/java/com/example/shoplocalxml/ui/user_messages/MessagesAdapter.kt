package com.example.shoplocalxml.ui.user_messages

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.log

class MessagesAdapter(private val context: Context, private var listMessages: MutableList<UserMessage> = mutableListOf()): RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {
    private var onMessageItemListener: OnMessageItemListener? = null
    fun setOnMessageItemListener(value: OnMessageItemListener) {
        onMessageItemListener = value
    }

    /*init{
        log(listMessages)
    }*/

    fun restoreItem(index: Int, message: UserMessage) {
        listMessages.add(index, message)
        notifyItemInserted(index)
        //notifyItemRangeChanged(index, itemCount)
    }

    fun isReadItem(id: Int): Boolean {
        //log(listMessages)
        listMessages.find { it.id == id }?.let {item ->
//            log("id = $id, isRead = ${item.read}")
            return item.read > 0
        }  ?: return false
    }

    fun setMessages(list: MutableList<UserMessage>){
        notifyItemRangeRemoved(0, itemCount)
        listMessages.clear()
        listMessages = list
        log(list)
        notifyItemRangeChanged(0, itemCount)
    }

    fun removeItem(position: Int) {
        val id = listMessages[position].id
        listMessages.removeAt(position)
        notifyItemRemoved(position)
        onMessageItemListener?.onDelete(id)
        //notifyItemRangeChanged(position, itemCount)
        /*listMessages.find { it.id == position }?.let {
            //it.deleted = true
            listMessages.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = MessageItem(context)
        return ViewHolder(view, onMessageItemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMessages[position])
    }

    override fun getItemCount(): Int {
        /*val count = listMessages.count {
            !it.deleted
        }*/
        return listMessages.size
    }

   /* override fun getItemId(position: Int): Long {
        return listMessages[position].id.toLong()
    }*/


    fun getItem(position: Int): UserMessage =
        listMessages[position]

    class ViewHolder(view: View, private val onMessageItemListener: OnMessageItemListener?): RecyclerView.ViewHolder(view) {
        val item = view as MessageItem
        fun changeBackgroundColor(color: Int) {
            item.changeBackgroundColor(color)
                //applicationContext.getColor(R.color.EditTextBorderErrorDark))
        }

        fun bind(message: UserMessage) {
            //val item = view as MessageItem
            /*val lparams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            item.layoutParams = lparams*/
            item.message = message
            item.setOnClickListener {
                //log("unread...")
                if (message.read == 0) {
                    message.read = 1
                    item.updateReadState(message.read)
                    onMessageItemListener?.onClick(message.id)
                }
            }
            //log("item read = ${item.message.read}")
        }
    }
}