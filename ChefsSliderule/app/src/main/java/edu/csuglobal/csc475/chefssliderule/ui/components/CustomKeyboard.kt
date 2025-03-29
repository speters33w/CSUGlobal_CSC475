package edu.csuglobal.csc475.chefssliderule.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomKeyboard(
    onKeyClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onDone: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyboardButton("7") { onKeyClick("7") }
            KeyboardButton("8") { onKeyClick("8") }
            KeyboardButton("9") { onKeyClick("9") }
            KeyboardButton("⌫", Color.Gray) { onBackspace() }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyboardButton("4") { onKeyClick("4") }
            KeyboardButton("5") { onKeyClick("5") }
            KeyboardButton("6") { onKeyClick("6") }
            KeyboardButton("-") { onKeyClick("-") }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyboardButton("1") { onKeyClick("1") }
            KeyboardButton("2") { onKeyClick("2") }
            KeyboardButton("3") { onKeyClick("3") }
            KeyboardButton("/") { onKeyClick("/") }
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            KeyboardButton("C", Color.Gray) { onClear() }
            KeyboardButton("0") { onKeyClick("0") }
            KeyboardButton(".") { onKeyClick(".") }
            KeyboardButton("✓", Color(11,100,35)) { onDone() }
        }
    }
}

@Composable
fun RowScope.KeyboardButton(
    text: String,
    color: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .padding(2.dp),
        colors = if (color != Color.Unspecified) {
            ButtonDefaults.buttonColors(containerColor = color)
        } else {
            ButtonDefaults.buttonColors()
        }
    ) {
        Text(text)
    }
}