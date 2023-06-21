package com.example.shoplocalxml.ui.filter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter

data class GroupItem(val id: Long, val name: String, val count: Int)
data class ChildItem(val id: Long, val name: String, val count: Int, var selected: Boolean = false)

class ExpandableAdapter: BaseExpandableListAdapter() {
    private val groupItems = arrayListOf<GroupItem>()
    private val childItems = hashMapOf<Long, ArrayList<ChildItem>>()
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
        return childItems[groupItems[groupPosition].id]?.get(childPosition)?.id ?: -1L
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
        TODO("Not yet implemented")
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        TODO("Not yet implemented")
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return childItems[groupItems[groupPosition].id]?.get(childPosition)?.selected ?: false
    }
}