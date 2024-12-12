package dev.forcecodes.albertsons.randomuser.presentation.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.albertsons.randomuser.R
import dev.forcecodes.albertsons.randomuser.databinding.FragmentUserDetailsBinding
import dev.forcecodes.albertsons.randomuser.databinding.ItemCardInfoLayoutBinding
import dev.forcecodes.albertsons.randomuser.extensions.executeAfter
import dev.forcecodes.albertsons.randomuser.extensions.launchWithViewLifecycle
import dev.forcecodes.albertsons.randomuser.presentation.view.UserArgsKey

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_user_details) {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getString(UserArgsKey.USER_ID)?.let {
            viewModel.getUserDetails(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentUserDetailsBinding.bind(view)

        with(binding) {
            executeAfter {
                lifecycleOwner = viewLifecycleOwner
                requireArguments().apply {
                    fullName = getString(UserArgsKey.FULL_NAME) ?: ""
                    thumbnail = getString(UserArgsKey.THUMBNAIL) ?: ""
                    email = getString(UserArgsKey.EMAIL) ?: ""
                }
            }
        }

        launchWithViewLifecycle {
            viewModel.userDetailsState.collect { viewState ->
                binding.loadingContainer.isVisible = viewState is UserDetailsViewState.Loading
                if (viewState is UserDetailsViewState.Success) {
                    binding.personalInfoContainer.addDetailsToContainer(viewState.userDetails)
                    binding.accountInfoContainer.addDetailsToContainer(viewState.loginCredentials)
                }
            }
        }
    }

    private fun ViewGroup.addDetailsToContainer(detailedContents: List<Pair<String, String>>) {
        removeAllViews()
        val inflater = LayoutInflater.from(context)
        for ((index, detailed) in detailedContents.withIndex()) {
            val infoBinding = ItemCardInfoLayoutBinding.inflate(inflater, this,false).apply {
                executeAfter {
                    title = detailed.first
                    subtitle = detailed.second
                    hideDivider = index == detailedContents.lastIndex
                }
            }
            addView(infoBinding.root)
        }
    }
}