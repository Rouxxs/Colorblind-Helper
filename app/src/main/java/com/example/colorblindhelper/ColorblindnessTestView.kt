package com.example.colorblindhelper

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

data class IshiharaPlate(
    val imageResId: Int, // Resource ID for the image
    val answerCount: List<Int>, // Normal, Protanopia, Deuteranopia, Achromatopsia
    val options: List<String> // Options for the user to choose
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorblindnessTestView(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Ishihara Colorblind Test")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
        ColorblindnessTestContent(innerPadding)
    }
}

@Composable
fun ColorblindnessTestContent(innerPadding: PaddingValues) {
    var testCompleted by remember { mutableStateOf(false) }
    var results by remember { mutableStateOf(listOf(0, 0, 0, 0)) }
    if (testCompleted) {
        // Display the results
        ColorblindnessTestResults(innerPadding, results, testCompleted)
    } else {
        // Display the test
        ColorblindnessTest(innerPadding,
            onTestCompleted = { answer ->
            results = answer
            testCompleted = true
        })
    }

}

@Composable
fun ColorblindnessTestResults(
    innerPadding: PaddingValues,
    results: List<Int>,
    testCompleted: Boolean
) {
    val types = listOf("Normal", "Protanopia", "Deuteranopia", "Achromatopsia")
    val indx = results.indexOf(results.maxOrNull())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text( "Your Color Blind Test Results", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = types[indx], fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(20.dp))

    }
}

@Composable
fun ColorblindnessTest(
    innerPaddingValues: PaddingValues
    , onTestCompleted: (List<Int>) -> Unit)
{
    val pagerState = rememberPagerState(pageCount = { ishiharaPlates.size }, initialPage = 0)
    var selectedPage = remember { mutableIntStateOf(pagerState.currentPage) }
    var answer = mutableListOf(0, 0, 0, 0) // Normal, Protanopia, Deuteranopia, Achromatopsia
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.padding(innerPaddingValues),
    ) { page ->
        val plate = ishiharaPlates[page]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the Ishihara plate image
            Image(
                painter = painterResource(plate.imageResId),
                contentDescription = "Ishihara Plate",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What do you see?",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Dynamically generate answer buttons

                plate.options.forEach { option ->
                    val scope = rememberCoroutineScope()
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray, contentColor = Color.White),
                        onClick = {
                        if (page >= 0 && page <= 6) {
                            if (plate.options.indexOf(option) == 1) {
                                answer[1]+=1;
                                answer[2]+=1;
                                Log.d("Answer", "Test: " + plate.options.indexOf(option).toString())
                            } else if(plate.options.indexOf(option) == 2) {
                                answer[3]+=1;
                            } else if(plate.options.indexOf(option) == 0) {
                                answer[0]+=1;
                            }
                        }

                        if (page >= 7 && page <= 14) {
                            if (plate.options.indexOf(option) == 1) {
                                answer[2]+=1;
                                answer[1]+=1;
                            } else if(plate.options.indexOf(option) == 0) {
                                answer[0]+=1;
                                answer[3]+=1;

                            }
                        }

                        if (page >= 15 && page <= 17) {
                            if (plate.options.indexOf(option) == 1) {
                                answer[1]+=1;
                            } else if(plate.options.indexOf(option) == 2) {
                                answer[2]+=1;
                            } else if(plate.options.indexOf(option) == 0) {
                                answer[0]+=1;
                            }
                        }
                        if (page == 18) {
                            if (plate.options.indexOf(option) == 1) {
                                answer[2] += 1;
                                answer[1] += 1;
                            } else if (plate.options.indexOf(option) == 0) {
                                answer[0] += 1;
                                answer[3] += 1;

                            }
                        }

                        if (page == 19 || page == 20) {
                            if (plate.options.indexOf(option) == 1) {
                                answer[2] += 1;
                                answer[1] += 1;
                                answer[3] += 1;
                            } else if (plate.options.indexOf(option) == 0) {
                                answer[0] += 1;
                            }
                        }
                        if (page == 21 || page == 22) {
                            if (plate.options.indexOf(option) == 1) {
                                answer[2] += 1;
                                answer[1] += 1;
                            } else if (plate.options.indexOf(option) == 0) {
                                answer[0] += 1;
                            } else if (plate.options.indexOf(option) == 2) {
                                answer[3] += 1;
                            }
                        }
                        scope.launch {
                            if (pagerState.currentPage < ishiharaPlates.size - 1) {
                                pagerState.animateScrollToPage(
                                    pagerState.currentPage + 1
                                )
                            } else {
                                onTestCompleted(answer)
                            }
                            Log.d("Answer", "Answer: " + answer.toString())
                        }
                    }) {
                        Text(option)
                    }
                }

        }
    }
}


val ishiharaPlates = listOf(
    IshiharaPlate(
        imageResId = R.drawable.plate1,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("12", "1", "2", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate2,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("8", "3", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate3,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("29", "70", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate4,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("5", "2", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate5,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("3", "5", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate6,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("15", "17", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate7,
        answerCount = listOf(1, 1, 1, 1),
        options = listOf("74", "21", "Nothing")
    ),
    // Normal and Colorblind
    IshiharaPlate(
        imageResId = R.drawable.plate8,
        answerCount = listOf(1, 0, 0, 1),
        options = listOf("6", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate9,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("45", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate10,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("5", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate11,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("7", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate12,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("16", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate13,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("73", "Nothing")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate14,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Nothing", "5")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate15,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Nothing", "45")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate16,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("26", "6, faint 2", "2, faint 6")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate17,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("42", "2, faint 4", "4, faint 2")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate18,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Purple and Red Lines", "Purple Line", "Red Line")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate19,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("No seamless line", "Wiggly line")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate20,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Green wiggly line", "No seamless line")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate21,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Orange wiggly line", "No seamless line")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate22,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Blue-green/Yellow-green wiggly line", "Blue-green and Red line", "No seamless line")
    ),
    IshiharaPlate(
        imageResId = R.drawable.plate23,
        answerCount = listOf(1, 0, 0, 0),
        options = listOf("Red and Orange wiggly line", "Blue-green and Red line", "No seamless line")
    ))