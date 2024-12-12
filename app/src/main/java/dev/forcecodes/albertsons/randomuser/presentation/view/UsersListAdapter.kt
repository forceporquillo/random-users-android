package dev.forcecodes.albertsons.randomuser.presentation.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import dev.forcecodes.albertsons.randomuser.databinding.ItemUserLayoutBinding
import dev.forcecodes.albertsons.randomuser.extensions.executeAfter
import kotlinx.coroutines.Dispatchers

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserSimpleInfo>() {

    override fun areItemsTheSame(oldItem: UserSimpleInfo, newItem: UserSimpleInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserSimpleInfo, newItem: UserSimpleInfo): Boolean {
        return oldItem == newItem
    }

}

class UsersListAdapter(
    private val onClick: (UserSimpleInfo) -> Unit,
    private val requestNextPage: () -> Unit
): PagingDataAdapter<UserSimpleInfo, UsersListAdapter.UsersViewHolder>(
    DIFF_CALLBACK, workerDispatcher = Dispatchers.IO
) {

    private var layoutManager: LinearLayoutManager? = null

    inner class UsersViewHolder(
        private val binding: ItemUserLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: UserSimpleInfo) {
            binding.executeAfter {
                uiModel = item
            }
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UsersViewHolder(ItemUserLayoutBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = initLayoutManagerIfNull(recyclerView)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            // simple workaround implementation that mimics the approach of paging library.
            if (closestItemToLastPosition(totalItemCount, visibleItemCount, lastVisibleItem)) {
                // instead of retrieving the page that links to the next cursor.
                // we can manually hook it up by retrieving the last item
                if (snapshot().isEmpty() && itemCount <= 0) {
                    return
                }
                requestNextPage.invoke()
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
        layoutManager = null
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(onScrollListener)
        initLayoutManagerIfNull(recyclerView)
    }

    /**
     * Coerces the index in the list, including its footer when necessary.
     *
     * Checks whether if it reached the closest bottom item of the list. This is backed by an additional
     * [NEXT_PAGE_THRESHOLD] so it can advance the request upon reaching to the end of the list.
     */
    private fun closestItemToLastPosition(
        totalItemCount: Int, visibleItemCount: Int, lastVisibleItem: Int
    ): Boolean {
       return visibleItemCount + lastVisibleItem + NEXT_PAGE_THRESHOLD >= totalItemCount
    }

    private fun initLayoutManagerIfNull(recyclerView: RecyclerView): LinearLayoutManager {
        if (layoutManager == null) {
            layoutManager = recyclerView.layoutManager as LinearLayoutManager
        }
        return layoutManager ?: throw NullPointerException()
    }

    companion object {

        /**
         * Visible threshold until force reload.
         *
         * Note: Increase the value if not required to show progress bar when fetching new data as possible.
         */
        private const val NEXT_PAGE_THRESHOLD = 0
    }
}
