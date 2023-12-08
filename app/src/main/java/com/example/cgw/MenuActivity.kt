package com.example.cgw

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cgw.ui.theme.CGWTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CGWTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.fillMaxSize(0.4f))
                        Text(
                            text = stringResource(id = R.string.menu_title),
                            textAlign = TextAlign.Center,
                            fontSize = 30.sp
                        )
                        Button(onClick = {
                            startActivity(
                                Intent(
                                    this@MenuActivity,
                                    MainActivity::class.java
                                )
                            )
                        }) {
                            Text(text = stringResource(id = R.string.menu_start_button))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    CGWTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
                Text(text = "Accelerometer Test", textAlign = TextAlign.Center, fontSize = 30.sp)
                Button(onClick = { }) {
                    Text(text = "Start")
                }
            }
        }
    }
}