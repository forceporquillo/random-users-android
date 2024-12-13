/**
 * Copyright 2024 strongforce1
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package dev.forcecodes.albertsons.randomuser.presentation.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import dev.forcecodes.albertsons.randomuser.R
import dev.forcecodes.albertsons.randomuser.databinding.FragmentSliderDialogBinding

class SliderDialogFragment : DialogFragment(R.layout.fragment_slider_dialog) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        requestFeature()
        // call super because we already set layout via constructor
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val limitSize = requireArguments().getInt(LIMIT_SIZE)

        val binding = FragmentSliderDialogBinding.bind(view)

        binding.slider.value = limitSize.toFloat()

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.applyButton.setOnClickListener {
            val maxSizeBundle = bundleOf(LIMIT_SIZE to binding.slider.value.toInt())
            parentFragmentManager.setFragmentResult(MAX_USER_LIMIT_KEY, maxSizeBundle)
            dismiss()
        }
    }

    companion object {
        fun newInstance(limitSize: Int) =
            SliderDialogFragment().apply {
                arguments = bundleOf(LIMIT_SIZE to limitSize)
            }
    }
}

const val LIMIT_SIZE = "limit_size"
const val MAX_USER_LIMIT_KEY = "max_user_limit"

fun DialogFragment.requestFeature() {
    dialog?.apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.requestFeature(Window.FEATURE_NO_TITLE)
    }
}
