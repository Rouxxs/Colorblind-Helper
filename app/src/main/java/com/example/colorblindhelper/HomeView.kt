package com.example.colorblindhelper

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Color Assist")
                }
            )
        },
    ) { innerPadding ->
        HomeViewContent(innerPadding, navController, photoPickerLauncher)
    }

}

@Composable
fun HomeViewContent(
    innerPadding: PaddingValues,
    navController: NavController,
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) {
    var openInfor = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),

        ) {
        Image(
            painter = painterResource(id = R.drawable.logo_removebg),
            contentDescription = "Colorblind Helper Logo",
        )
        Column(
            modifier = Modifier.fillMaxWidth() ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledTonalButton(
                onClick = { navController.navigate("CameraView") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
            ) {
                Box (modifier = Modifier.fillMaxSize()) {
                    Icon(painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 30.dp))
                    //Spacer(modifier = Modifier.width(20.dp))
                    Text("Open Color Detection Camera", modifier = Modifier.align(Alignment.CenterStart).padding(start = 70.dp), fontSize = 17.sp)
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            FilledTonalButton(
                onClick = { photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                ) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
            ) {
                Box (modifier = Modifier.fillMaxSize()) {
                    Icon(painter = painterResource(id = R.drawable.baseline_burst_mode_24),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 30.dp))
                    //Spacer(modifier = Modifier.width(20.dp))
                    Text("Choose Image from Gallery",modifier = Modifier.align(Alignment.CenterStart).padding(start = 70.dp), fontSize = 17.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            FilledTonalButton(
                onClick = { openInfor.value = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
            ) {
                Box (modifier = Modifier.fillMaxSize()) {
                    Icon(painter = painterResource(id = R.drawable.baseline_book_24),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 30.dp))
                    //Spacer(modifier = Modifier.width(20.dp))
                    Text("Types of Colorblindness",
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 70.dp)
                        , fontSize = 17.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            FilledTonalButton(
                onClick = { navController.navigate("TestView") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White)
            ) {
                Box (modifier = Modifier.fillMaxSize()) {
                    Icon(painter = painterResource(id = R.drawable.baseline_disabled_visible_24),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 30.dp))
                    //Spacer(modifier = Modifier.width(20.dp))
                    Text("Colorblindness Test",
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 70.dp)
                        , fontSize = 17.sp)
                }
            }
        }

        if (openInfor.value) {
            ColorblindTypes(openInfor)
        }
    }
}


@Composable
fun ColorblindTypes(openInfor: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openInfor.value = false}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Pages()
        }
    }
}

@Composable
fun Pages() {
    val pagerState = rememberPagerState(pageCount = { colorBlindnessTypes.size }, initialPage = 0)
    var selectedPage = remember { mutableStateOf(pagerState.currentPage) }

    Box(modifier = Modifier.fillMaxSize()){
        HorizontalPager(state = pagerState, modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {currentPage ->
            selectedPage.value = currentPage
            ColorblindTypeInfor(colorBlindType = colorBlindnessTypes[currentPage])
        }
        val scope = rememberCoroutineScope()
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(100))
                .align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                    contentDescription = "Go back"
                )
            }
            IconButton(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                    contentDescription = "Go forward"
                )
            }
        }
    }

}

@Composable
fun ColorblindTypeInfor(
    colorBlindType: ColorBlindnessType
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = colorBlindType.name,
            fontSize = 30.sp,
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = colorBlindType.description, fontSize = 20.sp, textAlign = TextAlign.Left)
    }
}

data class ColorBlindnessType(val name: String, val description: String)

val colorBlindnessTypes = listOf(
    ColorBlindnessType(
        "Protanopia",
        "This type of color blindness makes it hard to see red. Reds might look darker, and it can be hard to tell red and green apart."
    ),
    ColorBlindnessType(
        "Deuteranopia",
        "This type makes it hard to see green. People with this condition often mix up reds and greens."
    ),
    ColorBlindnessType(
        "Tritanopia",
        "A rare type of color blindness that makes it hard to see blue. It can also make it tricky to tell blue from green and yellow from pink."
    ),
    ColorBlindnessType(
        "Protanomaly",
        "A mild problem seeing red. Reds can look dull or blend in with green, but it’s not as severe as protanopia."
    ),
    ColorBlindnessType(
        "Deuteranomaly",
        "The most common type of color blindness. It makes green look dull, and greens might mix with reds."
    ),
    ColorBlindnessType(
        "Tritanomaly",
        "A very rare condition that makes blue look less vivid. It can also make it hard to tell the difference between blue and green or yellow and purple."
    ),
    ColorBlindnessType(
        "Achromatopsia",
        "A very serious condition where everything looks like shades of gray. People with this condition can’t see any colors at all."
    ),
    ColorBlindnessType(
        "Achromatomaly",
        "A very rare condition where colors look extremely faded, almost like seeing the world in black and white with a little color."
    )
)
