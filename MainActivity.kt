package com.nooralhoda.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF123A33)
private val Gold = Color(0xFFC6A45B)
private val Cream = Color(0xFFFAF7EF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NoorAlhodaApp() }
    }
}

data class RequestItem(val service: String, val method: String, val payment: String, val note: String)

@Composable
fun NoorAlhodaApp() {
    var page by remember { mutableIntStateOf(0) }
    val requests = remember { mutableStateListOf<RequestItem>() }
    MaterialTheme(colorScheme = lightColorScheme(primary = Green, secondary = Gold, background = Cream)) {
        Scaffold(
            containerColor = Cream,
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(page == 0, { page = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("خانه") })
                    NavigationBarItem(page == 1, { page = 1 }, icon = { Icon(Icons.Default.AddCircle, null) }, label = { Text("ثبت درخواست") })
                    NavigationBarItem(page == 2, { page = 2 }, icon = { Icon(Icons.Default.History, null) }, label = { Text("پیگیری") })
                }
            }
        ) { padding ->
            when (page) {
                0 -> HomePage(Modifier.padding(padding)) { page = 1 }
                1 -> RequestPage(Modifier.padding(padding)) { requests.add(it); page = 2 }
                else -> HistoryPage(Modifier.padding(padding), requests)
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier = Modifier, onStart: () -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Column(Modifier.fillMaxWidth().background(Green, RoundedCornerShape(24.dp)).padding(24.dp)) {
                Text("نورالهدی", color = Gold, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("با مدیریت الاء", color = Color.White, fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))
                Text("همراهی معنوی در مسیر تصمیم و دعا", color = Color(0xFFF4EBD6))
            }
        }
        item { Text("خدمات ما", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Green) }
        item { ServiceTile("استخاره با قرآن", "ثبت درخواست و دریافت پاسخ از کارشناس", Icons.Default.MenuBook) }
        item { ServiceTile("دعای اختصاصی", "ثبت و پیگیری درخواست دعا", Icons.Default.Favorite) }
        item { ServiceTile("راهنمای عمومی", "مطالب راهنما؛ نه پیشگویی و نه جایگزین مشورت تخصصی", Icons.Default.Lightbulb) }
        item {
            Button(onClick = onStart, modifier = Modifier.fillMaxWidth().height(54.dp)) {
                Text("ثبت درخواست جدید")
            }
        }
        item { Text("یادآوری: استخاره جایگزین بررسی، مشورت و تصمیم‌گیری مسئولانه نیست.", color = Color.Gray, fontSize = 12.sp) }
    }
}

@Composable
fun ServiceTile(title: String, detail: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = Gold, modifier = Modifier.size(32.dp))
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, color = Green, fontWeight = FontWeight.Bold)
            Text(detail, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun RequestPage(modifier: Modifier = Modifier, onSubmit: (RequestItem) -> Unit) {
    var service by remember { mutableStateOf("استخاره با قرآن") }
    var method by remember { mutableStateOf("کارشناس") }
    var payment by remember { mutableStateOf("درگاه ایران") }
    var note by remember { mutableStateOf("") }
    LazyColumn(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("ثبت درخواست", fontSize = 25.sp, color = Green, fontWeight = FontWeight.Bold) }
        item { Text("نوع خدمت") }
        item { Options(listOf("استخاره با قرآن", "دعای اختصاصی", "راهنمای عمومی"), service) { service = it } }
        item { Text("روش انجام") }
        item { Options(listOf("کارشناس", "راهنمای خودکار"), method) { method = it } }
        item { Text("روش پرداخت") }
        item { Options(listOf("درگاه ایران", "پرداخت بین‌المللی"), payment) { payment = it } }
        item { Text("توضیحات (اختیاری)") }
        item {
            OutlinedTextField(note, { note = it }, modifier = Modifier.fillMaxWidth(),
                minLines = 3, placeholder = { Text("موضوع درخواست را بنویسید") })
        }
        item {
            Button(onClick = { onSubmit(RequestItem(service, method, payment, note)) }, modifier = Modifier.fillMaxWidth()) {
                Text("ثبت آزمایشی درخواست")
            }
        }
        item { Text("پرداخت در این نسخه آزمایشی است و وجهی دریافت نمی‌شود.", color = Color.Gray, fontSize = 12.sp) }
    }
}

@Composable
fun Options(items: List<String>, selected: String, onSelect: (String) -> Unit) {
    Column {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected == item, { onSelect(item) })
                Text(item)
            }
        }
    }
}

@Composable
fun HistoryPage(modifier: Modifier = Modifier, requests: List<RequestItem>) {
    Column(modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("پیگیری درخواست‌ها", fontSize = 24.sp, color = Green, fontWeight = FontWeight.Bold)
        if (requests.isEmpty()) Text("هنوز درخواستی ثبت نشده است.")
        requests.forEachIndexed { i, r ->
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("درخواست ${i + 1}", color = Green, fontWeight = FontWeight.Bold)
                    Text("خدمت: ${r.service}")
                    Text("روش انجام: ${r.method}")
                    Text("پرداخت انتخابی: ${r.payment}")
                    Text("وضعیت: آزمایشی و در انتظار اتصال سامانه")
                }
            }
        }
    }
}
