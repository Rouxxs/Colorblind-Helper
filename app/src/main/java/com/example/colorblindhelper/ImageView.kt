import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import com.example.colorblindhelper.HomeViewContent
import com.example.colorblindhelper.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageView(
    photoUri: Uri,
    shouldShowPhoto: MutableState<Boolean>
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Colorblind Filter")
                },
                navigationIcon = {
                    IconButton(onClick = { shouldShowPhoto.value = false}) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "Localized description",
                            tint = Color.White
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        ImageViewContent(innerPadding, photoUri, shouldShowPhoto)
    }

}

@Composable
fun ImageViewContent(innerPadding: PaddingValues, photoUri: Uri, shouldShowPhoto: MutableState<Boolean>) {
    var selectedFilter by remember { mutableStateOf("None") }
    val filterOptions = listOf("None", "Protanopia", "Deuteranopia", "Tritanopia", "Protanomaly", "Deuteranomaly", "Tritanomaly", "Achromatopsia", "Achromatomaly")
    var showDropdown by remember { mutableStateOf(false) }
    var colorMatrix by remember { mutableStateOf(getFilterMatrix("None")) }

    Column (modifier = Modifier.padding(innerPadding)){


        // Display the image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp * 4 / 6)
        ) {
            Image(
                painter = rememberImagePainter(photoUri),
                contentDescription = "Photo",
                modifier = Modifier.fillMaxSize(),
                colorFilter = ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)) {
            items(filterOptions.size) { index ->
                val filter = filterOptions[index]

                Column {
                    Image(
                        painter = rememberImagePainter(photoUri),
                        contentDescription = "Photo",
                        modifier = Modifier
                            .width(150.dp)
                            .height(120.dp)
                            .padding(5.dp)
                            .clickable {
                                selectedFilter = filter; colorMatrix = getFilterMatrix(filter)
                            },
                        colorFilter = ColorFilter.colorMatrix(ColorMatrix(getFilterMatrix(filter))),
                    )
                    Text(text = filter, modifier = Modifier.align(Alignment.CenterHorizontally))
                }


            }
        }
        // Back button
//        Button(
//            onClick = { shouldShowPhoto.value = false }, // Navigate back
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(16.dp) // Add padding to avoid the edge
//        ) {
//            Text("Back")
//        }

        BackHandler {
            shouldShowPhoto.value = false
        }
    }
}

