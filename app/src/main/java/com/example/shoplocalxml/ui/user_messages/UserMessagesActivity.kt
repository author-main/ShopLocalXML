package com.example.shoplocalxml.ui.user_messages

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.databinding.ActivityUserMessagesBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.ui.product_item.DividerItemRowDecoration
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserMessagesActivity: AppCompatActivity() {
    private lateinit var dataBinding: ActivityUserMessagesBinding
    //private var messages = mutableListOf<UserMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityUserMessagesBinding.inflate(layoutInflater)

        HeaderTitle(dataBinding.mainLayoutMessages, getStringResource(R.string.text_usermessages)){
            finish()
        }
        setContentView(dataBinding.root)
        supportActionBar?.hide()



        val typeToken = object : TypeToken<List<UserMessage>>() {}.type
        val gson    = Gson()
        val data   = intent.getStringExtra("messages")
        val messages = gson.fromJson<List<UserMessage>>(data, typeToken).toMutableList()
        val adapter = MessagesAdapter(baseContext, messages)
        val manager = LinearLayoutManager(
            baseContext,
            LinearLayoutManager.VERTICAL,
            false
        )
        dataBinding.recyclerViewMessages.layoutManager = manager
        dataBinding.recyclerViewMessages.addItemDecoration(DividerItemRowDecoration())
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
}