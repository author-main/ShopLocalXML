package com.example.shoplocalxml.ui.filter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.log
import java.util.zip.Inflater




class ExpandableAdapter: BaseExpandableListAdapter() {
    private var onExpanded: ((groupPosition: Int) -> Unit)? = null
    fun setOnExpandGroup(value: (groupPosition: Int) -> Unit) {
        onExpanded = value
    }
    data class GroupItem(val id: Long, val name: String, val count: Int, var expanded: Boolean = false)
    data class ChildItem(val id: Long, val name: String, val count: Int, var selected: Boolean = false)
    private val groupItems = arrayListOf<GroupItem>()
    private val childItems = hashMapOf<Long, ArrayList<ChildItem>>()
    private var childViewHolder: ChildViewHolder? = null
    private var groupViewHolder: GroupViewHolder? = null

    fun addGroupItem(id: Long, name: String, count: Int) {
        groupItems.add(GroupItem(id, name, count))
    }

    fun addChildItem(groupid: Long, id: Long, name: String, count: Int, selected: Boolean) {
        if (childItems[groupid] == null)
            childItems[groupid] = ArrayList()

        childItems[groupid]?.add(ChildItem(id, name, count, selected))
    }

    override fun getGroupCount(): Int {
        return groupItems.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return childItems[groupItems[groupPosition].id]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): GroupItem {
        return groupItems[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): ChildItem? {
        return childItems[groupItems[groupPosition].id]?.get(childPosition)
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupItems[groupPosition].id
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()//childItems[groupItems[groupPosition].id]?.get(childPosition)?.id ?: -1L
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertview = convertView
        if (convertview == null) {
            val inflater: LayoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertview = inflater.inflate(R.layout.group_item, null);
            groupViewHolder = GroupViewHolder()
            groupViewHolder?.let{
                it.nameGroupItem        = convertview.findViewById<TextView>(R.id.textGroupItemName)
            }
            convertview.setTag(R.layout.group_item, groupViewHolder);
        } else
            groupViewHolder = convertView?.getTag(R.layout.group_item) as GroupViewHolder

        groupViewHolder?.let{
            it.nameGroupItem?.text           = groupItems[groupPosition].name
        }

        if (groupItems[groupPosition].expanded)
            onExpanded?.invoke(groupPosition)
        return convertview!!
    }

    @SuppressLint("InflateParams")
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var convertview = convertView
        if (convertview == null) {
            val inflater: LayoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertview = inflater.inflate(R.layout.child_item, null);
            childViewHolder = ChildViewHolder()
            childViewHolder?.let{
                it.layoutChildItemRoot  = convertview.findViewById<LinearLayout>(R.id.layoutChildItemRoot)
                it.nameChildItem        = convertview.findViewById<TextView>(R.id.textChildItemName)
                it.countChildItem       = convertview.findViewById<TextView>(R.id.textChildItemCount)
                it.selectedChildItem    = convertview.findViewById<CheckBox>(R.id.checkboxChildItem)
            }
            convertview.setTag(R.layout.child_item, childViewHolder);
        } else
            childViewHolder = convertView?.getTag(R.layout.child_item) as ChildViewHolder

        groupItems[groupPosition].let {groupItem ->
            childViewHolder?.let {
                childItems[groupItem.id]?.get(childPosition)?.let{childItem ->
                    it.nameChildItem?.text = childItem.name
                    it.countChildItem?.text = childItem.count.toString()
                    it.selectedChildItem?.isChecked = childItem.selected


                    it.selectedChildItem?.setOnClickListener { view ->
                        val value = (view as CheckBox).isChecked
                        childItems[groupItems[groupPosition].id]?.get(childPosition)?.selected = value
                        //log("click checkbox $groupPosition, $childPosition")
                    }
                    it.layoutChildItemRoot?.setOnClickListener {view ->
                        val value = !it.selectedChildItem!!.isChecked
                        it.selectedChildItem!!.isChecked = value
                        childItems[groupItems[groupPosition].id]?.get(childPosition)?.selected = value
                        //log("click layout $groupPosition, $childPosition")
                    }



                }
            }
        }
        return convertview!!
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false//childItems[groupItems[groupPosition].id]?.get(childPosition)?.selected ?: false
    }

    fun getFilterEnum(): HashMap<Long, LongArray> {
        val enum = hashMapOf<Long, LongArray>()
        for (group in groupItems) {
            var array = emptyArray<Long>()
            childItems[group.id]?.let{child ->
                for (entity in child) {
                    if (entity.selected)
                        array += entity.id
                }
            }
            if (array.isNotEmpty()) {
                val expanded = if (group.expanded) -1 else 1
                    enum[expanded * group.id] = array.toLongArray()
            }
        }
        return enum
    }

    fun updateFilterSection(enum: HashMap<Long, LongArray>) {
        //val enum = filter.enum
        for (entry in enum) {
            val expanded = entry.key < 0
            val groupId = if (expanded) -entry.key else entry.key
/*            if (expanded)
                onExpanded?.invoke(groupPosition)*/


            groupItems.find { it.id == groupId }?.expanded = expanded
            childItems[groupId]?.let {child ->
                for (entityChild in child) {
                    for (entityEntry in entry.value) {
                        if (entityChild.id == entityEntry) {
                            entityChild.selected = true
                        }
                    }
                }
            }
            //groupPosition += 1
        }
    }

    override fun onGroupExpanded(groupPosition: Int) {
        super.onGroupExpanded(groupPosition)
        groupItems[groupPosition].expanded = true
    }

    override fun onGroupCollapsed(groupPosition: Int) {
        super.onGroupCollapsed(groupPosition)
        groupItems[groupPosition].expanded = false
    }

    class GroupViewHolder {
        var nameGroupItem: TextView? = null
    }

    class ChildViewHolder {
        var layoutChildItemRoot: LinearLayout? = null
        var nameChildItem: TextView? = null
        var countChildItem: TextView? = null
        var selectedChildItem: CheckBox? = null
    }


}