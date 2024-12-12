package dev.forcecodes.albertsons.randomuser.presentation.compose.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil3.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import dev.forcecodes.albertsons.randomuser.R
import dev.forcecodes.albertsons.randomuser.presentation.compose.requireContentView
import dev.forcecodes.albertsons.randomuser.presentation.compose.theme.MyApplicationTheme
import dev.forcecodes.albertsons.randomuser.presentation.compose.theme.Shapes
import dev.forcecodes.albertsons.randomuser.presentation.view.UserArgsKey
import dev.forcecodes.albertsons.randomuser.presentation.view.details.DetailsViewModel
import dev.forcecodes.albertsons.randomuser.presentation.view.details.UserDetailsViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class DetailsComposeFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getString(UserArgsKey.USER_ID)?.let {
            viewModel.getUserDetails(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = requireContentView(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed) {
        MyApplicationTheme {
            DetailsScreen(state = viewModel.userDetailsState) {
                requireArguments().run {
                    UserDetails(
                        fullName = getString(UserArgsKey.FULL_NAME) ?: "",
                        thumbnail = getString(UserArgsKey.THUMBNAIL) ?: "",
                        email = getString(UserArgsKey.EMAIL) ?: ""
                    )
                }
            }
        }
    }
}

data class UserDetails(
    val fullName: String,
    val thumbnail: String,
    val email: String
)

@Composable
fun DetailsScreen(
    state: StateFlow<UserDetailsViewState>,
    onGetDetails: () -> UserDetails,
) {
    val detailsState by state.collectAsState()

    when (val viewState = detailsState) {
        is UserDetailsViewState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        is UserDetailsViewState.Success -> {
            val details = onGetDetails()
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                DetailsTextSection(details)
                UserDetailsContent(
                    title = stringResource(R.string.details_personal_information),
                    details = viewState.userDetails
                )
                Spacer(modifier = Modifier.size(20.dp))
                UserDetailsContent(
                    title = stringResource(R.string.details_account_information),
                    details = viewState.loginCredentials,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }

        is UserDetailsViewState.Error -> {

        }
    }
}

@Composable
fun DetailsTextSection(details: UserDetails) {
    Spacer(modifier = Modifier.size(20.dp))
    HeaderProfilePhoto(details.thumbnail)
    Spacer(modifier = Modifier.size(20.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = details.fullName,
            fontSize = 24.sp,
            style = MaterialTheme.typography.h6.copy(color = MaterialTheme.colors.onBackground)
        )
        Text(
            text = details.email,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
        )
    }
    Spacer(modifier = Modifier.size(20.dp))
}

@Composable
fun UserDetailsContent(
    title: String,
    details: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        // since we don't define surface, we have to explicitly set the color
        color = MaterialTheme.colors.onBackground,
        fontSize = 14.sp
    )
    Spacer(modifier = Modifier.size(10.dp))
    Card(
        modifier = modifier.fillMaxWidth()
            .padding()
            .clip(Shapes.large),
        elevation = 4.dp
    ) {
        Column {
            List(details.size) { index ->
                val isLastIndex = index < details.lastIndex
                DetailedRowContent(details[index], isLastIndex)
            }
        }
    }
}

@Composable
fun DetailedRowContent(
    contents: Pair<String, String>,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            contents.first,
            fontSize = 14.sp,
        )
        Text(
            text = contents.second,
            fontSize = 14.sp,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
    if (showDivider) {
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp
        )
    }
}

@Composable
fun HeaderProfilePhoto(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    MyApplicationTheme {
        // Replace with a mock or your real ViewModel
        DetailsScreen(
            state = MutableStateFlow(UserDetailsViewState.Success(
                listOf(
                    "Dob" to "N/A",
                    "Age" to "N/A",
                    "Date Registered" to "N/A",
                    "Phone" to "N/A",
                    "Cell" to "N/A",
                    "Address" to "N/A",
                    "Timezone" to "N/A"
                ),
                listOf(
                    "UUID" to "N/A",
                    "Username" to "N/A",
                    "Password" to "N/A",
                    "Salt" to "N/A",
                    "Md5" to "N/A",
                    "Sha1" to "N/A",
                    "Sha256" to "N/A"
                )
            )),
            onGetDetails = {
                UserDetails(
                    fullName = "Alan Coleman",
                    email = "alan.coleman@example.com",
                    thumbnail = "https://randomuser.me/api/portraits/thumb/men/68.jpg" // Example thumbnail URL
                )
            }
        )
    }
}
