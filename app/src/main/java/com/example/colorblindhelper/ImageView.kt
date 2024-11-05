import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix


@Composable
fun ImageView(
    photoUri: Uri,
    shouldShowPhoto: MutableState<Boolean>
) {
    var selectedFilter by remember { mutableStateOf("None") }
    val filterOptions = listOf("None", "Protanopia", "Deuteranopia", "Tritanopia", "Protanomaly", "Deuteranomaly", "Tritanomaly", "Achromatopsia", "Achromatomaly")
    var showDropdown by remember { mutableStateOf(false) }
    var colorMatrix by remember { mutableStateOf(getFilterMatrix("None")) }

    Column {
        Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp / 6))

        // Dropdown for filter selection
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { showDropdown = true }) {
                Text("Select Filter: $selectedFilter")
            }
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                filterOptions.forEach { filter ->
                    DropdownMenuItem(text = { Text(text = filter) }, onClick = {
                        selectedFilter = filter
                        showDropdown = false
                        colorMatrix = getFilterMatrix(filter)
                    })
                }
            }
        }

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

        // Back button
        Button(
            onClick = { shouldShowPhoto.value = false }, // Navigate back
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp) // Add padding to avoid the edge
        ) {
            Text("Back")
        }
    }
}

