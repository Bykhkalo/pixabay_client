package com.example.viewimages.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.viewimages.R
import com.example.viewimages.model.ImageSearchRequest
import kotlinx.android.synthetic.main.fragment_image_filter.*

class ImageFilterFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        img_filter_toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStackImmediate()
        }

        search_button.setOnClickListener {
            hideKeyboard(it)
            searchImages()
        }

        initSpinners()
        restoreData()
    }

    private fun restoreData() {

        val searchRequest = requireArguments().getSerializable("searchRequest") as ImageSearchRequest

        searchRequest.let {
            query_editText.setText(it.query)

            var position = (img_type_spinner.getAdapter() as ArrayAdapter<CharSequence?>).getPosition(it.imageType)
            img_type_spinner.setSelection(position)

            position = (img_category_spinner.getAdapter() as ArrayAdapter<CharSequence?>).getPosition(it.imageCategory)
            img_category_spinner.setSelection(position)

            position = (order_spinner.getAdapter() as ArrayAdapter<CharSequence?>).getPosition(it.imageOrder)
            order_spinner.setSelection(position)

            min_height_editText.setText(it.minHeight.toString())
            min_width_editText.setText(it.minWidth.toString())
            safe_search_checkBox.setChecked(it.isSafeSearchEnabled)

            val orientation = it.orientation

            when (orientation) {
                "all" -> {
                    vertical_orientation_checkBox.setChecked(true)
                    horizontal_orientation_checkBox.setChecked(true)
                }
                "vertical" -> vertical_orientation_checkBox.setChecked(true)
                "horizontal" -> horizontal_orientation_checkBox.setChecked(true)
            }


        }
    }

    private fun searchImages() {
        val query: String = query_editText.text.toString()
        val imageType: String = img_type_spinner.selectedItem.toString()
        val imageCategory: String = img_category_spinner.selectedItem.toString()
        val imageOrder: String = order_spinner.selectedItem.toString()

        val minWidth: Int = if (min_width_editText.text.toString().isEmpty()) 0 else min_width_editText.text.toString().toInt()
        val minHeight: Int = if (min_height_editText.text.toString().isEmpty()) 0 else min_height_editText.text.toString().toInt()

        val isVerticalOrientationEnabled: Boolean = vertical_orientation_checkBox.isChecked
        val isHorizontalOrientationEnabled: Boolean = horizontal_orientation_checkBox.isChecked
        val isSafeSearchEnabled: Boolean = safe_search_checkBox.isChecked

        val searchRequest = ImageSearchRequest(query,
                imageType, imageCategory, imageOrder, minHeight, minWidth,
                isVerticalOrientationEnabled, isHorizontalOrientationEnabled, isSafeSearchEnabled)

        val args = Bundle()
        args.putString("type", "search")
        args.putSerializable("searchRequest", searchRequest)

        parentFragmentManager.setFragmentResult("searchKey", args)

        requireActivity().supportFragmentManager.popBackStack()


    }

    private fun initSpinners() {

        //Image type adapter
        val imgTypeAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.image_types, android.R.layout.simple_spinner_dropdown_item)
        img_type_spinner.adapter = imgTypeAdapter
        img_type_spinner.prompt = "Types"
        img_type_spinner.setSelection(0)

        //Image category adapter
        val imgCategoryAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, android.R.layout.simple_spinner_dropdown_item)
        img_category_spinner.adapter = imgCategoryAdapter
        img_category_spinner.prompt = "Categories"
        img_category_spinner.setSelection(0)

        val orderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.order, android.R.layout.simple_spinner_dropdown_item)
        order_spinner.adapter = orderAdapter
        order_spinner.prompt = "Order"
        order_spinner.setSelection(0)
    }

    private fun hideKeyboard(v: View) {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }
}