package com.example.dhikrcounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: DhikrCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
            ) {
                DhikrCounterScreen(viewModel = viewModel)
            }
        }
    }
}

class DhikrCounterViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val dhikrList = listOf(
        "لَا إِلٰهَ إِلَّا اللّٰهُ",
        "سُبْحَانَ اللّٰهِ ، اَلْحَمْدُ لِلّٰهِ ، اَللّٰهُ أَكْبَرُ",
        "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللّٰهِ",
        "لَا إِلٰهَ إِلَّا اللهُ وَحْدَهُ لَا شَرِيْكَ لَهُ ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيْرٌ",
        "سُبْحَانَ اللهِ وَبِحَمْدِهِ ، سُبْحَانَ اللهِ الْعَظِيْم",
        "يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
        "لَآ إِلٰهَ إِلَّآ أَنْتَ سُبۡحٰنَكَ إِنِّيْ كُنْتُ مِنَ الظّٰلِمِيْنَ",
        "أَسْتَغْفِرُ اللهَ الْعَظِيْمَ الَّذِيْ لَا إِلٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّوْمُ ، وَأَتُوْبُ إِلَيْهِ"
    )

    var counter by mutableIntStateOf(savedStateHandle[COUNTER_KEY] ?: 0)
        private set

    var selectedDhikr by mutableStateOf(savedStateHandle[SELECTED_DHIKR_KEY] ?: dhikrList.first())
        private set

    fun incrementCounter() {
        if (counter < MAX_COUNTER_VALUE) {
            counter += 1
            savedStateHandle[COUNTER_KEY] = counter
        }
    }

    fun selectDhikr(dhikr: String) {
        selectedDhikr = dhikr
        savedStateHandle[SELECTED_DHIKR_KEY] = dhikr
    }

    companion object {
        private const val COUNTER_KEY = "counter"
        private const val SELECTED_DHIKR_KEY = "selected_dhikr"
        private const val MAX_COUNTER_VALUE = 999
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhikrCounterScreen(viewModel: DhikrCounterViewModel) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Choose a Dhikr",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = menuExpanded,
            onExpandedChange = { menuExpanded = !menuExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = viewModel.selectedDhikr,
                onValueChange = {},
                readOnly = true,
                textStyle = TextStyle(textDirection = TextDirection.ContentOrRtl),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                viewModel.dhikrList.forEach { dhikr ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = dhikr,
                                style = TextStyle(textDirection = TextDirection.ContentOrRtl)
                            )
                        },
                        onClick = {
                            viewModel.selectDhikr(dhikr)
                            menuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = viewModel.selectedDhikr,
            style = MaterialTheme.typography.titleMedium.copy(textDirection = TextDirection.ContentOrRtl),
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = viewModel.counter.toString(),
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.incrementCounter() },
            modifier = Modifier
                .height(52.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Count", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
