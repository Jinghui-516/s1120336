package tw.edu.pu.s1120336

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tw.edu.pu.s1120336.ui.theme.S1120336Theme
import java.time.format.TextStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S1120336Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    Start(m = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Start(m: Modifier) {
    val context = LocalContext.current

    // 背景顏色
    val colors = listOf(
        Color(0xff95fe95),
        Color(0xfffdca0f),
        Color(0xfffea4a4),
        Color(0xffa5dfed)
    )

    var currentIndex by remember { mutableStateOf(0) } // Box 的背景顏色索引
    var isSwiping by remember { mutableStateOf(false) }
    var gameTime by remember { mutableStateOf(0) } // 遊戲持續時間
    var mariaPosition by remember { mutableStateOf(0f) } // 瑪利亞水平位置
    var mariaImageIndex by remember { mutableStateOf(0) } // 隨機生成瑪利亞圖片索引
    var score by remember { mutableStateOf(0) } // 遊戲分數

    val coroutineScope = rememberCoroutineScope()
    var isGameRunning by remember { mutableStateOf(true) }


    // 啟動遊戲邏輯
    LaunchedEffect(isGameRunning) {
        while (isGameRunning) { // 只有當遊戲正在運行時執行
            gameTime += 1 // 每秒增加遊戲持續時間
            mariaPosition += 50f // 瑪利亞每秒向右移動 50 像素
            delay(1000L) // 等待 1 秒

            if (mariaPosition >= 1080f) { // 當瑪利亞移出螢幕右側
                isGameRunning = false // 停止遊戲邏輯
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors[currentIndex])
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (!isSwiping) {
                        isSwiping = true
                        change.consume()
                        if (dragAmount > 0) {
                            currentIndex = (currentIndex - 1 + colors.size) % colors.size
                        } else if (dragAmount < 0) {
                            currentIndex = (currentIndex + 1) % colors.size
                        }
                        coroutineScope.launch {
                            delay(500)
                            isSwiping = false
                        }
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "2024期末上機考(資管二A楊靖惠)",
                style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.class_a),
                contentDescription = "Class Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "遊戲持續時間 $gameTime 秒",
                style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "您的成績 $score 分",
                style = androidx.compose.ui.text.TextStyle(fontSize = 10.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val activity = context as? Activity
                    activity?.finish()
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
            ) {
                Text(text = "結束App")
            }
        }

        // 瑪利亞圖片
        Image(
            painter = painterResource(id = when (mariaImageIndex) {
                0 -> R.drawable.maria0
                1 -> R.drawable.maria1
                2 -> R.drawable.maria2
                else -> R.drawable.maria3
            }), // 根據索引顯示不同圖片
            contentDescription = "瑪利亞",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = mariaPosition.dp, y = 0.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            // 雙擊檢測
                            if (currentIndex == mariaImageIndex) { // 背景顏色與圖片顏色索引相同
                                score += 1
                            } else {
                                score -= 1
                            }
                            // 重置瑪利亞圖片
                            mariaPosition = 0f
                            mariaImageIndex = (0..3).random()
                        }
                    )
                }
        )
    }
}