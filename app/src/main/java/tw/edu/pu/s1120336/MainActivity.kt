package tw.edu.pu.s1120336

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import androidx.compose.material3.Text // 導入正確的 Text 元件

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

    // 定義背景顏色列表
    val colors = listOf(
        Color(0xff95fe95), // 顏色 1
        Color(0xfffdca0f), // 顏色 2
        Color(0xfffea4a4), // 顏色 3
        Color(0xffa5dfed)  // 顏色 4
    )

    // 當前顏色索引
    var currentIndex by remember { mutableStateOf(0) }
    // 防止快速重複滑動
    var isSwiping by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors[currentIndex]) // 根據索引設置背景顏色
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    if (!isSwiping) {
                        isSwiping = true
                        change.consume() // 消耗滑動事件
                        if (dragAmount > 0) { // 右滑
                            currentIndex = (currentIndex - 1 + colors.size) % colors.size
                        } else if (dragAmount < 0) { // 左滑
                            currentIndex = (currentIndex + 1) % colors.size
                        }
                        // 延遲一段時間，避免快速滑動
                        kotlinx.coroutines.GlobalScope.launch {
                            kotlinx.coroutines.delay(500) // 延遲500毫秒
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
                style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.class_a), // 確保圖片存在於 res/drawable 目錄下
                contentDescription = "Class Image",
                modifier = Modifier
                    .fillMaxWidth() // 讓圖片寬度填滿版面
                    .wrapContentHeight(), // 根據圖片比例自動調整高度
                contentScale = ContentScale.Fit // 確保圖片完整顯示，不裁剪
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "遊戲持續時間 0 秒",
                style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "您的成績 0 分",
                style = androidx.compose.ui.text.TextStyle(fontSize = 20.sp, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    // 使用 LocalContext.current 並檢查是否為 Activity
                    val activity = context as? Activity
                    activity?.finish() // 如果是 Activity，則結束它
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
    }
}
