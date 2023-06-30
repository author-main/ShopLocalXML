package com.example.shoplocalxml.ui.detail_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.FragmentDetailProductBinding
import com.example.shoplocalxml.getStringArrayResource
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DetailProductFragment : Fragment() {
    private lateinit var dataBinding: FragmentDetailProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentDetailProductBinding.inflate(inflater, container, false)
        setDateDelivery(System.currentTimeMillis())
        return dataBinding.root
    }

    private fun setDateDelivery(date: Long){
        val arrayMonth = getStringArrayResource(R.array.month)
        //val dateFormat = SimpleDateFormat.getDateInstance()
        //val dateDelivery = Date(date)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.add(Calendar.DAY_OF_MONTH, 3)
        val day   = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val textDate = "${getString(R.string.text_datedelivery)} $day ${arrayMonth[month]}"
        dataBinding.textViewDateDelivery.text = textDate
    }

    private fun setProduct(value: Product) {

    }

    private fun setBrandName(value: String) {

    }

    private fun setImageIndex(value: Int) {

    }

    companion object {
        @JvmStatic
        fun newInstance(product: Product, brandName: String, imageIndex: Int) =
            DetailProductFragment().apply {
                setProduct(product)
                setBrandName(brandName)
                setImageIndex(imageIndex)
            }
    }
}