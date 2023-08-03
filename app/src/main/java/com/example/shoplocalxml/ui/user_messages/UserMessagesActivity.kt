package com.example.shoplocalxml.ui.user_messages

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.ActivityUserMessagesBinding
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.product_item.DividerItemRowDecoration
import com.example.shoplocalxml.vibrate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class UserMessagesActivity: AppCompatActivity() {
    private lateinit var adapter: MessagesAdapter
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
        adapter = MessagesAdapter(baseContext, messages)
        adapter.setOnMessageItemListener(object: OnMessageItemListener{
            override fun onClick(id: Int) {
                listRead.add(id)
                //log(listRead)
            }

            override fun onDelete(id: Int) {
                listRead.find { it == id } ?.let {
                    listRead.remove(it)
                }
                listDeleted.add(id)
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
        val itemTouchCallback = object: SimpleCallback(0, LEFT) {//or RIGHT){

          /*  private var deletedPosition = -1
            private fun removeItem() {
            //val deletedPosition = viewHolder.adapterPosition
            adapter.removeItem(deletedPosition)
            val snackBar = SnackbarExt(
                dataBinding.root,
                //window.decorView.rootView,
                getString(R.string.text_delete_usermessages)) {
            }
            snackBar.type = SnackbarExt.Companion.SnackbarType.INFO
            snackBar.setAction(getString(R.string.button_cancel))
            snackBar.show()
            }
*/
            private var limit = false
            private val dp24 = 24.toPx
            private val p = Paint(Paint.ANTI_ALIAS_FLAG)
            val icon = run {
                val drawable = VectorDrawableCompat.create(resources,
                    R.drawable.ic_close,
                    baseContext.theme
                )
                drawable?.setTint(baseContext.getColor(R.color.EditTextFont))
                drawable?.toBitmap()
            }
            /*val icon: Bitmap =
                BitmapFactory.decodeResource(resources,
                        R.drawable.ic_close)*/

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            fun removeItem(viewHolder: RecyclerView.ViewHolder) {
                //val position = viewHolder.adapterPosition
                val deletedPosition = viewHolder.adapterPosition
                //deletedPosition = adapter.getItem(position).id
                val deletedItem = adapter.getItem(deletedPosition)
                deletedItem.read = 0
                adapter.removeItem(deletedPosition)

                val snackBar = SnackbarExt(
                    dataBinding.root,
                    getString(R.string.text_delete_usermessages)) {
                        listDeleted.find { it == deletedItem.id}?.let{
                            listDeleted.remove(it)
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

               /* val deletedPosition = viewHolder.adapterPosition
                val deletedItem = adapter.getItem(deletedPosition)
                adapter.removeItem(deletedPosition)
                val snackBar = SnackbarExt(
                    dataBinding.root,
                    //window.decorView.rootView,
                    getString(R.string.text_delete_usermessages)) {
                }
                snackBar.type = SnackbarExt.Companion.SnackbarType.INFO
                snackBar.setAction(getString(R.string.button_cancel))
                snackBar.show()*/


                /*val snackbarExt = SnackbarExt(dataBinding.root, getStringResource(R.string.message_login_error))
                snackbarExt.type = SnackbarExt.Companion.SnackbarType.ERROR
                snackbarExt.show()*/

                //log("deleted = $deletedItem")
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
             //   log("draw...")
            /* fun drawBackground(rectBackground: RectF){
                   canvas?.let {canvas_ ->
                       val color_from = baseContext.getColor(R.color.BackgroundDark)
                       val color_to = baseContext.getColor(R.color.EditTextBorderErrorDark)
                       val animator = ValueAnimator.ofObject(ArgbEvaluator(), color_from, color_to)
                       animator.duration = 150
                       animator.addUpdateListener {
                           p.color = it.animatedValue as Int
                           canvas_.drawRect(rectBackground!!, p)
                       }
                       animator.start()
                   }


             }*/


                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val height = itemView.bottom - itemView.top
                    val widthBackground = itemView.height
                    //val width = height / 3
                    if (dX < 0) {
                        //val left = itemView.width.toFloat()





                        val deltaX = if (kotlin.math.abs(dX) <= widthBackground) {
                           // log("less...")
                            limit = false
                            dX
                        }
                            else {
                            //log("else less...")
                                if (!limit) {
                                    vibrate(100)
                                    //removeItem(viewHolder)
                                    //log((viewHolder.itemView as MessageItem).message)
                                    //removeItem(viewHolder)

                                }
                                limit = true
                                -widthBackground.toFloat()
                            }
                       // if (kotlin.math.abs(dX) <= widthBackground) {
                           // p.color = applicationContext.getColor(R.color.EditTextBorderErrorDark)
                            val leftBackground = itemView.width.toFloat() + deltaX
                            val rectBackground = RectF(
                                leftBackground,
                                itemView.top.toFloat(),
                                itemView.width.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.clipRect(rectBackground)
                            //c.drawRect(background, p)
                            if (limit) {
                                p.color = applicationContext.getColor(R.color.EditTextBorderErrorDark)
                                c.drawRect(rectBackground, p)
                            }
                            //drawBackground(rectBackground)
                            icon?.let{
                                val left_dest   = (itemView.width - widthBackground) + (widthBackground - dp24) / 2f
                                val top_dest    = itemView.top + (widthBackground - dp24) / 2f
                                val right_dest  = left_dest + dp24
                                val bottom_dest = top_dest + dp24

                                    c.drawBitmap(icon,
                                        left_dest, top_dest, p)
                                   /* val rect = RectF(
                                        (itemView.width - widthBackground).toFloat(),
                                        itemView.top.toFloat(),
                                        leftBackground,
                                        itemView.bottom.toFloat()
                                    )

                                    p.color = baseContext.getColor(R.color.BackgroundDark)
                                    c.drawRect(rect, p)*/

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
                        /*    val icon_dest = RectF(itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width)
                            c.drawBitmap(icon,null,icon_dest,p)*/

                            /*icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p)*/
                        }
                    }



                    /*super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )*/
               // }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                //log ((viewHolder.itemView.height / viewHolder.itemView.width).toFloat())
                return viewHolder.itemView.height.toFloat() / viewHolder.itemView.width.toFloat()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(dataBinding.recyclerViewMessages)

    }

}

/*
private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final Model deletedModel = imageModelArrayList.get(position);
                    final int deletedPosition = position;
                    adapter.removeItem(position);
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // undo is selected, restore the deleted item
                            adapter.restoreItem(deletedModel, deletedPosition);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                } else {
                    final Model deletedModel = imageModelArrayList.get(position);
                    final int deletedPosition = position;
                    adapter.removeItem(position);
                    // showing snack bar with Undo option
                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " removed from Recyclerview!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // undo is selected, restore the deleted item
                            adapter.restoreItem(deletedModel, deletedPosition);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
 */