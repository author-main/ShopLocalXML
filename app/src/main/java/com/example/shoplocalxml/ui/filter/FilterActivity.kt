package com.example.shoplocalxml.ui.filter

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.shoplocalxml.FILTER_KEY
import com.example.shoplocalxml.ID_BRAND
import com.example.shoplocalxml.ID_CATEGORY
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.ActivityFilterBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class FilterActivity : AppCompatActivity() {
    private var filter = Filter()
    private var isCancelled = true
    private val adapter = ExpandableAdapter()
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

        dataBinding.expListViewFilter.setAdapter(
            getExpandableAdapter()
        )


        dataBinding.buttonFilterReset.setOnClickListener {
            perform(Filter())
        }

        dataBinding.buttonFilterConfirm.setOnClickListener {
            filter.enum = adapter.getFilterEnum()
            perform(filter)
        }

        dataBinding.editTextFilterDiscount.lossFocusOutside = true
        dataBinding.editTextFilterDiscount.onValidValue = {
            val value: Int = try {
                it.toInt()
            } catch(_: Exception) {
                dataBinding.editTextFilterDiscount.setText("0")
                0
            }
            true
        }



        /*val colors = intArrayOf(0, 0xff0000, 0) // red for the example
        dataBinding.expListViewFilter.setChildDivider(GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors))
        dataBinding.expListViewFilter.divider = GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors)*/


       /* sharedViewModel =
            ViewModelProvider(this, FactoryViewModel(this, repository))[SharedViewModel::class.java]*/

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

    private fun perform(queryFilter: Filter){
        val gson = Gson()
        val filterJson = gson.toJson(queryFilter)
        val data = Intent()
        data.putExtra(FILTER_KEY, filterJson)
        isCancelled = false
        setResult(RESULT_OK, data)
        finish()
    }

    private fun getExpandableAdapter(): ExpandableAdapter {

        adapter.setOnExpandGroup {
            if (!dataBinding.expListViewFilter.isGroupExpanded(it))
                dataBinding.expListViewFilter.expandGroup(it)
        }

        var typeToken = object : TypeToken<List<Brend>>() {}.type
        val gson    = Gson()
        var data    = intent.getStringExtra("brands")
        val brands  = gson.fromJson<List<Brend>>(data, typeToken)
        typeToken   = object : TypeToken<List<Category>>() {}.type
        data        = intent.getStringExtra("categories")
        val categories = gson.fromJson<List<Category>>(data, typeToken)

        data        = intent.getStringExtra(FILTER_KEY)
        filter = gson.fromJson(data, Filter::class.java)
        //val adapter = ExpandableAdapter()
        adapter.addGroupItem(ID_CATEGORY, getStringResource(R.string.text_category), categories[ID_CATEGORY.toInt()].count)
        categories.forEach {
            adapter.addChildItem(ID_CATEGORY, it.id.toLong(), it.name, it.count, false)
        }
        adapter.addGroupItem(ID_BRAND, getStringResource(R.string.text_brend), categories[ID_BRAND.toInt()].count)
        brands.forEach {
            adapter.addChildItem(ID_BRAND, it.id.toLong(), it.name, it.count, false)
        }
        adapter.updateFilterSection(filter.enum)
        updateFilterData()
        return adapter
    }

    private fun updateFilterData(){
        dataBinding.editTextFilterDiscount.setText(filter.discount.toString())
    }

  /*  private fun getFilterEnum(): HashMap<Long, IntArray>{

    }*/

    override fun onDestroy() {
        if (isCancelled)
            setResult(RESULT_CANCELED)
        super.onDestroy()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus;
            if ( v is EditTextExt) {
                if (v.lossFocusOutside) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        v.clearFocus()
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}