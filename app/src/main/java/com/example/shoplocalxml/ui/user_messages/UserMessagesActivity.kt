package com.example.shoplocalxml.ui.user_messages

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
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
        val itemTouchCallback = object: SimpleCallback(0, LEFT) {//or RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedPosition = viewHolder.adapterPosition
                val deletedItem = adapter.getItem(deletedPosition)
                //log("deleted = $deletedItem")
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