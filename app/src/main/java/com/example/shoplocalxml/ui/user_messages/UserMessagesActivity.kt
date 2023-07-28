package com.example.shoplocalxml.ui.user_messages

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.shoplocalxml.R
import com.example.shoplocalxml.databinding.ActivityUserMessagesBinding
import com.example.shoplocalxml.getStringResource


class UserMessagesActivity: AppCompatActivity() {
    private lateinit var dataBinding: ActivityUserMessagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityUserMessagesBinding.inflate(layoutInflater)

        HeaderTitle(dataBinding.mainLayoutMessages, getStringResource(R.string.text_usermessages)){
            finish()
        }
        setContentView(dataBinding.root)
        supportActionBar?.hide()

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