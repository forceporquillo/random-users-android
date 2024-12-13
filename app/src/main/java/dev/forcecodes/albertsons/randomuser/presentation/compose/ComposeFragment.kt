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
package dev.forcecodes.albertsons.randomuser.presentation.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import dev.forcecodes.albertsons.domain.model.UserSimpleInfo
import dev.forcecodes.albertsons.randomuser.R
import dev.forcecodes.albertsons.randomuser.presentation.compose.theme.MyApplicationTheme
import dev.forcecodes.albertsons.randomuser.presentation.compose.theme.Shapes
import dev.forcecodes.albertsons.randomuser.presentation.compose.theme.Typography
import dev.forcecodes.albertsons.randomuser.presentation.view.DashboardViewModel
import dev.forcecodes.albertsons.randomuser.presentation.view.toBundle

class ComposeFragment : Fragment() {
    private val viewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        requireContentView(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed) {
            MyApplicationTheme {
                RandomUserListScreen(
                    viewModel = viewModel,
                ) { info ->
                    findNavController()
                        .navigate(R.id.action_compose_to_detailsCompose, info.toBundle())
                }
            }
        }
}

@Composable
fun RandomUserListScreen(
    viewModel: DashboardViewModel,
    onCardClick: (UserSimpleInfo) -> Unit = {},
) {
    val userInfo = viewModel.pagingData.collectAsLazyPagingItems()

    val listState = rememberLazyListState()
    val isEndReached = !listState.canScrollForward

    LaunchedEffect(isEndReached) {
        if (isEndReached) {
            viewModel.refresh(true)
        }
    }

    // for the meantime, vertical scrollbar is not supported
    // since composable here do not have constraints
    LazyColumn(
        state = listState,
        modifier =
            Modifier
                .fillMaxWidth(),
        contentPadding = PaddingValues(top = dimensionResource(R.dimen.spacing_small_2)),
        verticalArrangement = Arrangement.Top,
    ) {
        items(userInfo.itemCount) { index ->
            userInfo[index]?.let { user ->
                UserCard(user, onCardClick)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserCard(
    userSimpleInfo: UserSimpleInfo,
    onCardClick: (UserSimpleInfo) -> Unit = {},
) {
    val spacingNormal = dimensionResource(id = R.dimen.spacing_normal)
    val spacingSmall = dimensionResource(id = R.dimen.spacing_small_2)

    Card(
        modifier =
            Modifier
                .padding(
                    start = spacingNormal,
                    end = spacingNormal,
                    bottom = spacingSmall,
                ).fillMaxWidth(),
        shape = Shapes.large,
        onClick = {
            onCardClick.invoke(userSimpleInfo)
        },
    ) {
        Row(
            modifier = Modifier.padding(spacingSmall),
        ) {
            UserProfile(imageUrl = userSimpleInfo.thumbnailUrl)
            Column(
                modifier =
                    Modifier
                        .padding(bottom = 4.dp)
                        .padding(start = spacingNormal)
                        .align(Alignment.CenterVertically),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = userSimpleInfo.fullName,
                    style = Typography.h6,
                )
                Text(
                    text = userSimpleInfo.address,
                    style = Typography.caption,
                    color = Color.Gray.copy(0.9f),
                )
            }
        }
    }
}

@Composable
fun UserProfile(imageUrl: String?) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier =
            Modifier
                .size(dimensionResource(R.dimen.user_profile_size))
                .clip(Shapes.large),
    )
}
