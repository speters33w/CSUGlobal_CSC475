package edu.csuglobal.csc475.composables

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import edu.csuglobal.csc475.composables.ui.theme.ComposablesTheme

/**
 * MainActivity is the entry point of the application.
 * It sets up the Jetpack Compose UI and manages the main content of the app.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting. This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     * shut down then this Bundle contains the data it most recently supplied in
     * onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposablesTheme {
                MainContent(onExit = { finish() })
            }
        }
    }
}

/**
 * MainContent is the top-level composable function that sets up the main UI structure.
 *
 * @param onExit A lambda function to be called when the user wants to exit the app.
 */
@Composable
private fun MainContent(onExit: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        ScaffoldContent(onExit)
    }
}

/**
 * ScaffoldContent sets up the Scaffold composable which provides the basic material design
 * visual layout structure.
 *
 * @param onExit A lambda function to be called when the user wants to exit the app.
 */
@Composable
private fun ScaffoldContent(onExit: () -> Unit) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ComposableSlideshowApp(onExit = onExit)
        }
    }
}

/**
 * ComposableSlideshowApp is the main composable function that implements the slideshow functionality.
 *
 * @param onExit A lambda function to be called when the user wants to exit the app.
 */
@Composable
fun ComposableSlideshowApp(onExit: () -> Unit) {
    var currentSlide by remember { mutableStateOf(Slide.Text) }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp)
            ) {
                when (currentSlide) {
                    Slide.Text -> TextExample()
                    Slide.Button -> ButtonExample()
                    Slide.TextField -> TextFieldExample()
                    Slide.Checkbox -> CheckboxExample()
                    Slide.RadioButton -> RadioButtonExample()
                }
            }

            // Navigation buttons
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TriangleButton(
                    onClick = { currentSlide = currentSlide.previous() },
                    isLeft = true
                )
                TriangleButton(
                    onClick = { currentSlide = currentSlide.next() },
                    isLeft = false
                )
            }

            // Exit button
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(onClick = onExit) {
                    Text("Exit")
                }
            }
        }
    }
}

/**
 * Represents the different slides in the slideshow application.
 */
enum class Slide {
    Text, Button, TextField, Checkbox, RadioButton;

    /**
     * Returns the next slide in the sequence.
     * @return The next Slide in the enum, wrapping around to the first if at the end.
     */
    fun next(): Slide = entries[(ordinal + 1) % entries.size]
    /**
     * Returns the previous slide in the sequence.
     * @return The previous Slide in the enum, wrapping around to the last if at the beginning.
     */
    fun previous(): Slide = entries[(ordinal - 1 + entries.size) % entries.size]
}

/**
 * Composable function that creates a wedge-shaped button for navigation.
 *
 * @param onClick Lambda to be invoked when the button is clicked.
 * @param isLeft Boolean indicating whether the triangle should point left (true) or right (false).
 */
@Composable
fun TriangleButton(onClick: () -> Unit, isLeft: Boolean) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(48.dp)
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.size(32.dp)) {
            val path = Path()
            if (isLeft) {
                path.moveTo(size.width, 0f)
                path.lineTo(0f, size.height / 2)
                path.lineTo(size.width, size.height)
            } else {
                path.moveTo(0f, 0f)
                path.lineTo(size.width, size.height / 2)
                path.lineTo(0f, size.height)
            }
            path.close()
            drawPath(path, color = Color.Gray)
        }
    }
}

/**
 * Composable function that displays a simple text example.
 */
@Composable
fun TextExample() {
    Text("This is a simple Text composable")
}

/**
 * Composable function that demonstrates a button example with a click counter.
 */
@Composable
fun ButtonExample() {
    var clickCount by remember { mutableIntStateOf(0) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { clickCount++ }) {
            Text("Click me!")
        }
        Text("Button clicked $clickCount times")
    }
}

/**
 * Composable function that demonstrates a text field example with focus management.
 */
@Composable
fun TextFieldExample() {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter text") },
            modifier = Modifier
                .focusRequester(focusRequester)
                .padding(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("You entered: $text")
    }
}

/**
 * Composable function that demonstrates a checkbox example.
 */
@Composable
fun CheckboxExample() {
    var isChecked by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked = it }
        )
        Text("Check me")
    }
}

/**
 * Composable function that demonstrates a radio button group example.
 */
@Composable
fun RadioButtonExample() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by remember { mutableStateOf(options[0]) }
    Column {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { selectedOption = option }
                )
                Text(option)
            }
        }
    }
}