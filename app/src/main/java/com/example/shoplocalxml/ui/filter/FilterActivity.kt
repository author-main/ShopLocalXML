package com.example.shoplocalxml.ui.filter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.StateListDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.util.StateSet
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.databinding.ActivityFilterBinding
import com.example.shoplocalxml.databinding.ActivityMainBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log

class FilterActivity : AppCompatActivity() {
    //private lateinit var sharedViewModel: SharedViewModel
    /*private var lateinit sharedViewModel: SharedViewModel =
        ViewModelProvider(this, FactoryViewModel(this, repository))[SharedViewModel::class.java]*/
    private lateinit var dataBinding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityFilterBinding.inflate(layoutInflater)
        dataBinding.eventhandler = this
        setContentView(dataBinding.root)
        dataBinding.buttonBackFilter.setOnClickListener {
            finish()
        }
        supportActionBar?.hide()
        val adapter = ExpandableAdapter()
        val brands = SharedViewModel.getBrands()
        val categories = SharedViewModel.getCategories()
        adapter.addGroupItem(0, getStringResource(R.string.text_category), 50)
        brands.forEach {
            adapter.addChildItem(0, it.id.toLong(), it.name, 10, false)
        }

        adapter.addGroupItem(1, getStringResource(R.string.text_brend), 30)
        categories.forEach {
            adapter.addChildItem(1, it.id.toLong(), it.name, 10, false)
        }
        dataBinding.expListViewFilter.setAdapter(adapter)

       /* sharedViewModel =
            ViewModelProvider(this, FactoryViewModel(this, repository))[SharedViewModel::class.java]*/

        log(SharedViewModel.getCategories())
       /* onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        })*/
    }
    /*private fun getCheckboxIcon(): StateListDrawable {
        val size = 24.toPx
        val radius = 10.toPx.toFloat()
        val center = PointF(size/2f, size/2f)
        val bitmapOff = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val colorOffBorder = Color.WHITE//baseContext.getColor(R.color.TextDescription)
        val colorOffBackground = Color.RED//baseContext.getColor(R.color.EditTextBackgroundDark)
        val colorOnBackground = baseContext.getColor(R.color.colorBrend)
        val paint = Paint()
        val canvas = Canvas(bitmapOff)
        paint.style = Paint.Style.FILL
        paint.color = colorOffBackground
        canvas.drawCircle(center.x, center.y, radius - 2, paint)
        paint.color = colorOffBorder
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.toPx.toFloat()
        canvas.drawCircle(center.x, center.y, radius, paint)

        val drawableOn = AppCompatResources.getDrawable(baseContext, R.drawable.ic_check_on)
        val stateDrawable = StateListDrawable()
        val drawableOff = bitmapOff.toDrawable(resources)
        stateDrawable.addState(intArrayOf(android.R.attr.state_checked),  drawableOn)
        stateDrawable.addState(StateSet.WILD_CARD, drawableOff)
        stateDrawable.clearColorFilter()
        return stateDrawable
    }*/
}