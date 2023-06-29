package com.example.shoplocalxml.ui.detail_product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.databinding.FragmentDetailProductBinding
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.log

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
        return dataBinding.root
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