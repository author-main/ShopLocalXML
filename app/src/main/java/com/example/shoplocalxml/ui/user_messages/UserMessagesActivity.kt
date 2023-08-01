package com.example.shoplocalxml.ui.user_messages

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoplocalxml.FILTER_KEY
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.databinding.ActivityUserMessagesBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.DividerItemRowDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserMessagesActivity: AppCompatActivity() {
    private val listRead = mutableListOf<Int>()
    private val listDeleted = mutableListOf<Int>()
    private lateinit var dataBinding: ActivityUserMessagesBinding
    //private var messages = mutableListOf<UserMessage>()

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
        val adapter = MessagesAdapter(baseContext, messages)
        adapter.setOnMessageItemListener(object: OnMessageItemListener{
            override fun onClick(index: Int) {
                listRead.add(index)
                //log(listRead)
            }

            override fun onDelete(index: Int) {

            }
        })
        val manager = LinearLayoutManager(
            baseContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        dataBinding.recyclerViewMessages.layoutManager = manager
        dataBinding.recyclerViewMessages.addItemDecoration(DividerItemRowDecoration())

     /*   val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                   /* noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.adapterPosition))
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.note_deleted),
                        Toast.LENGTH_SHORT
                    ).show()*/
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(dataBinding.recyclerViewMessages)*/

        setSwipeItem()

        dataBinding.recyclerViewMessages.adapter = adapter

        /*dataBinding.recyclerViewProductHome.addItemDecoration(DividerItemDecoration())
        dataBinding.recyclerViewProductHome.itemAnimator = null*/

        /*val textView = TextView(this)
        textView.text = "adsfadsfasdf"
        textView.textSize = 20f
        textView.setTypeface(null, Typeface.NORMAL)
        textView.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        textView.gravity = Gravity.CENTER
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.customView = textView
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.PrimaryDark)))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)*/
    }


   /* override fun onBackPressed() {
        performClose()
        super.onBackPressed()
    }*/

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
        //dataBinding.recyclerViewMessages
    }

}