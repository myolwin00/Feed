package com.emrys.feed.feature.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.emrys.feed.R
import com.emrys.feed.common.viewBinding
import com.emrys.feed.databinding.FragmentCategoryBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val binding by viewBinding(FragmentCategoryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}