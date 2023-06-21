package com.example.shoplocalxml.ui.filter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.databinding.ActivityFilterBinding
import com.example.shoplocalxml.log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FilterActivity : AppCompatActivity() {
    //private lateinit var sharedViewModel: SharedViewModel
    /*private var lateinit sharedViewModel: SharedViewModel =
        ViewModelProvider(this, FactoryViewModel(this, repository))[SharedViewModel::class.java]*/
    private lateinit var dataBinding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var typeToken = object : TypeToken<List<Brend>>() {}.type
        //val logs = gson.fromJson<List<JsonLong>>(br, myType)


        val gson = Gson()
        val dataBrans      = intent.getStringExtra("brands")
        val dataCategories = intent.getStringExtra("categories")


        dataBinding = ActivityFilterBinding.inflate(layoutInflater)
        dataBinding.eventhandler = this
        setContentView(dataBinding.root)
        dataBinding.buttonBackFilter.setOnClickListener {
            finish()
        }
        supportActionBar?.hide()
        val adapter = ExpandableAdapter()
       /* val brands = SharedViewModel.getBrands()
        val categories = SharedViewModel.getCategories()*/
     /*   adapter.addGroupItem(ID_BRAND, getStringResource(R.string.text_category), 50)
        brands.forEach {
            adapter.addChildItem(ID_BRAND, it.id.toLong(), it.name, 10, false)
        }

        adapter.addGroupItem(ID_CATEGORY, getStringResource(R.string.text_brend), 30)
        categories.forEach {
            adapter.addChildItem(ID_CATEGORY, it.id.toLong(), it.name, 10, false)
        }*/
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