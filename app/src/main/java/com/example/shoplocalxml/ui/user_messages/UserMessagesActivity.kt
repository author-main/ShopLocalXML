package com.example.shoplocalxml.ui.user_messages

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.shoplocalxml.R
import com.example.shoplocalxml.alpha
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.ActivityUserMessagesBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.product_item.DividerItemRowDecoration
import com.example.shoplocalxml.vibrate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserMessagesActivity: AppCompatActivity() {
    private lateinit var adapter: MessagesAdapter
    private val listRead = mutableListOf<Int>()
    private val listDeleted = mutableListOf<Int>()
    private lateinit var dataBinding: ActivityUserMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityUserMessagesBinding.inflate(layoutInflater)
        HeaderTitle(dataBinding.mainLayoutMessages, getStringResource(R.string.text_usermessages)){
            performClose()
            finish()
        }
        setContentView(dataBinding.root)
        supportActionBar?.hide()
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                performClose()
                finish()
            }
        })
        val typeToken = object : TypeToken<List<UserMessage>>() {}.type
        val gson    = Gson()
        val data   = intent.getStringExtra("messages")
        val messages = gson.fromJson<List<UserMessage>>(data, typeToken).toMutableList()
        setVisibilityInformationCard(intent.getIntExtra("notification", 0))
        adapter = MessagesAdapter(baseContext, messages)
        adapter.setOnMessageItemListener(object: OnMessageItemListener{
            override fun onClick(id: Int) {
                listRead.add(id)
            }

            override fun onDelete(id: Int) {
              if (!adapter.isReadItem(id))
                  listRead.add(id)
                listDeleted.add(id)
                if (adapter.itemCount == 0) {
                    performClose()
                    finish()
                }
            }
        })
        val manager = LinearLayoutManager(
            baseContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        dataBinding.recyclerViewMessages.layoutManager = manager
        dataBinding.recyclerViewMessages.addItemDecoration(DividerItemRowDecoration())
        setSwipeItem()
        dataBinding.recyclerViewMessages.adapter = adapter
    }

    private fun performClose(){
        val intent = Intent()
        if (listRead.size > 0) {
            val joinRead = listRead.joinToString(",")
            intent.putExtra("read_messages", joinRead)
        }
        if (listDeleted.size > 0) {
            val joinDeleted = listDeleted.joinToString(",")
            intent.putExtra("delete_messages", joinDeleted)
        }
        setResult(RESULT_OK, intent)
    }

    private fun setSwipeItem(){
        val itemTouchCallback = object: SimpleCallback(0, LEFT) {//or RIGHT){
            private var limit = false
            private val dp24 = 24.toPx
            private val color = applicationContext.getColor(R.color.EditTextBorderErrorDark).toColor().toArgb()
            private val p = Paint(Paint.ANTI_ALIAS_FLAG)
            val icon = run {
                val drawable = VectorDrawableCompat.create(resources,
                    R.drawable.ic_close,
                    baseContext.theme
                )
                drawable?.setTint(baseContext.getColor(R.color.EditTextFont))
                drawable?.toBitmap()
            }
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            fun removeItem(viewHolder: RecyclerView.ViewHolder) {
                val deletedPosition = viewHolder.adapterPosition
                val deletedItem = adapter.getItem(deletedPosition)
                adapter.removeItem(deletedPosition)
                val snackBar = SnackbarExt(
                    dataBinding.root,
                    getString(R.string.text_delete_usermessages)) {
                        listRead.find{it == deletedItem.id}?.let{item ->
                            listRead.remove(item)
                        }
                        listDeleted.find { it == deletedItem.id}?.let{item ->
                            listDeleted.remove(item)
                        }
                        (viewHolder as MessagesAdapter.ViewHolder).changeBackgroundColor(applicationContext.getColor(R.color.BackgroundDark))
                        adapter.restoreItem(deletedPosition, deletedItem)
                    }
                snackBar.type = SnackbarExt.Companion.SnackbarType.INFO
                snackBar.setAction(getString(R.string.button_cancel))
                snackBar.show()
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                (viewHolder as MessagesAdapter.ViewHolder).changeBackgroundColor(applicationContext.getColor(R.color.EditTextBorderErrorDark))
                CoroutineScope(Dispatchers.Main).launch {
                    delay(300)
                    removeItem(viewHolder)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
               if(actionState == ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val widthBackground = itemView.height
                    if (dX < 0) {
                        val deltaX = if (kotlin.math.abs(dX) <= widthBackground) {
                            limit = false
                            dX
                        }
                            else {
                                if (!limit) {
                                    vibrate(100)
                                }
                                limit = true
                                -widthBackground.toFloat()
                            }
                            val leftBackground = itemView.width.toFloat() + deltaX
                            val rectBackground = RectF(
                                leftBackground,
                                itemView.top.toFloat(),
                                itemView.width.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.clipRect(rectBackground)
                            val alpha = (kotlin.math.abs(dX) / widthBackground.toFloat()).coerceAtMost(1f)
                            p.color = color.alpha(alpha)
                            c.drawRect(rectBackground, p)
                            icon?.let{
                                val left_dest   = (itemView.width - widthBackground) + (widthBackground - dp24) / 2f
                                val top_dest    = itemView.top + (widthBackground - dp24) / 2f
                                c.drawBitmap(icon, left_dest, top_dest, p)
                                super.onChildDraw(
                                    c,
                                    recyclerView,
                                    viewHolder,
                                    deltaX,
                                    dY,
                                    actionState,
                                    isCurrentlyActive
                                )

                            }
                         }
                    }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return viewHolder.itemView.height.toFloat() / viewHolder.itemView.width.toFloat()
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(dataBinding.recyclerViewMessages)
    }

    private fun setVisibilityInformationCard(value: Int){
        if (value == 1)
            dataBinding.cardViewInformation.visibility = View.GONE
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        dataBinding.cardViewInformation.visibility = View.GONE
        val typeToken = object : TypeToken<List<UserMessage>>() {}.type
        val gson    = Gson()
        val data   = intent.getStringExtra("messages")
        val messages = gson.fromJson<List<UserMessage>>(data, typeToken).toMutableList()
        setVisibilityInformationCard(intent.getIntExtra("notification", 0))
        adapter.setMessages(messages)
    }
}