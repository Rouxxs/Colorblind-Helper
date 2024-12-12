package com.example.colorblindhelper

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraView(
    navController: NavController,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Gray,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Color Detect Camera")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp()}) {
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
        CameraViewContent(
            innerPadding,
            navController = navController,
            outputDirectory = outputDirectory,
            executor = executor,
            onImageCaptured = onImageCaptured,
            onError = onError
        )
    }

}

@Composable
fun CameraViewContent(innerPadding: PaddingValues, navController: NavController, outputDirectory: File, executor: Executor, onImageCaptured: (Uri) -> Unit, onError: (ImageCaptureException) -> Unit) {
// 1
    val colorUtils = ColorUtils()
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    val preview = Preview.Builder().build()

    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()
    val density = LocalDensity.current
    var bitmap: Bitmap? = null
    val handler = Handler(Looper.getMainLooper())
    val colorHandler = ColorHandler()
    val radius = 2.dp
    var rgbColor by remember { mutableStateOf(Triple(0, 0, 0)) }
    var hexColor by remember { mutableStateOf("#000000") }
    var colorName by remember {
        mutableStateOf("Unknown")
    }
    val bitmapCallback = object : Runnable {
        override fun run() {
            val currentBitmap = previewView.bitmap
            if (currentBitmap != null) {
                // Xử lý bitmap tại đây
                bitmap = currentBitmap
                val (r, g, b) = colorHandler.detectColor(bitmap!!, radius, density)
                hexColor = colorHandler.rgbToHex(r, g, b)
                rgbColor = Triple(r, g, b)
                colorName = colorUtils.getColorDescription(r, g, b)
//                Log.i("CameraBitmap", "Bitmap captured: ${bitmap?.width}x${bitmap?.height}")
                //Log.i("Color", "RGB: ($r, $g, $b), HEX: $hexColor")
                //Log.i("ColorName", "Color name: $colorName")
            }
            // Lặp lại callback sau một khoảng thời gian nhất định
            handler.postDelayed(this, 500)
        }
    }

    // 2
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
        handler.post(bitmapCallback)
    }

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(innerPadding)){
        //val camWidth = LocalConfiguration.current.screenWidthDp * 4 / 6;
        //Log.i("Camera Size", "Local: " + LocalConfiguration.current.screenWidthDp + " " + camWidth)

        ColorInfoBox(rgbColor, hexColor, colorName)
        // 3
        Box(contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            AndroidView({ previewView }, modifier = Modifier
                .matchParentSize())
            Box(
                modifier = Modifier
                    .matchParentSize(),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(100.dp)) {
                    drawCircle(
                        color = Color.Black, // Màu viền vòng tròn
                        radius = size.minDimension / 15,
                        style = Stroke(width = radius.toPx()) // Độ dày viền
                    )
                }
            }
            IconButton(
                onClick = {
                    takePhoto(
                        "yyyy-MM-dd-HH-mm-ss-SSS",
                        imageCapture,
                        outputDirectory,
                        executor,
                        onImageCaptured,
                        onError
                    )
                },
                modifier = Modifier.size(50.dp)
                // Add some padding from the bottom
            ) {
                Icon(painter = painterResource(id = R.drawable.baseline_camera_24), contentDescription = "",modifier = Modifier.size(100.dp))
            }

        }


        // Add the Back button in the bottom left
//        Button(
//            onClick = { navController.navigateUp() }, // Navigate back
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(16.dp)
//        ) {
//            Text("Back")
//        }
    }

    DisposableEffect(Unit) {
        onDispose {
            handler.removeCallbacks(bitmapCallback)
        }
    }
}

@Composable
fun ColorInfoBox(
    rgbColor: Triple<Int, Int, Int>,
    hexColor: String,
    colorName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.dp / 6)
            .background(Color.Black)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = colorName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RGB: $rgbColor",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = "HEX: $hexColor",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

private fun takePhoto(
    filenameFormat: String,
    imageCapture: ImageCapture,
    outputDirectory: File,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val photoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
        override fun onError(exception: ImageCaptureException) {
            Log.e("Photo", "Take photo error:", exception)
            onError(exception)
        }

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            val savedUri = Uri.fromFile(photoFile)
            onImageCaptured(savedUri)
        }
    })
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

//private fun getGalleryUri(filename: String, context : Context): Uri? {
//    val contentValues = ContentValues().apply {
//        put(MediaStore.Images.Media.DISPLAY_NAME, "$filename.jpg")
//        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/${R.string.app_name}")
//    }
//
//    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//}