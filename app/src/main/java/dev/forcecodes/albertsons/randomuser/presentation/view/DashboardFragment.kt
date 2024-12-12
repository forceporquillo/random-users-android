package dev.forcecodes.albertsons.randomuser.presentation.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import dev.forcecodes.albertsons.randomuser.R
import dev.forcecodes.albertsons.randomuser.databinding.FragmentConventionalXmlBinding
import dev.forcecodes.albertsons.randomuser.extensions.launchWithViewLifecycle
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_conventional_xml) {

    private val viewModel by activityViewModels<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refresh(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentConventionalXmlBinding.bind(view)

        val adapter = UsersListAdapter(
            onClick = {
                findNavController().navigate(R.id.detailsFragment, it.toBundle())
            },
            requestNextPage = {
                viewModel.refresh(true)
            }
        )

        launchWithViewLifecycle {
            viewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }
        }

        binding.recyclerView.adapter = adapter
    }

    // could be nav safe args
    private fun UserSimpleInfo.toBundle() =
        bundleOf(
            UserArgsKey.USER_ID to id,
            UserArgsKey.FULL_NAME to fullName,
            UserArgsKey.THUMBNAIL to thumbnailUrl,
            UserArgsKey.EMAIL to email,
        )
}
