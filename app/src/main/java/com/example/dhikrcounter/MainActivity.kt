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
import androidx.compose.runtime.saveable.rememberSaveable
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
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Row
import com.example.dhikrcounter.ui.theme.DhikrCounterTheme
import com.example.dhikrcounter.ui.theme.NotoArabic
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.Divider


fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(
        VibrationEffect.createOneShot(
            40,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
    )
}

class MainActivity : ComponentActivity() {
    private val viewModel: DhikrCounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DhikrCounterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DhikrCounterScreen(viewModel = viewModel)
                }
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

    enum class CycleMode(val max: Int) {
        MODE_33(33),
        MODE_99(99)
    }

    var cycleMode by mutableStateOf(
        savedStateHandle["cycle_mode"] ?: CycleMode.MODE_33
    )
        private set


    fun incrementCounter() {
        if (counter < cycleMode.max) {
            counter += 1
            savedStateHandle[COUNTER_KEY] = counter
        } else {
            counter = 0
            savedStateHandle[COUNTER_KEY] = 0
        }
    }


    var counter by mutableIntStateOf(savedStateHandle[COUNTER_KEY] ?: 0)
        private set

    var selectedDhikr by mutableStateOf(savedStateHandle[SELECTED_DHIKR_KEY] ?: dhikrList.first())
        private set

    fun changeCycleMode(mode: CycleMode) {
        cycleMode = mode
        reset()
    }



    fun reset() {
        counter = 0
        savedStateHandle[COUNTER_KEY] = 0
    }

    fun selectDhikr(dhikr: String) {
        selectedDhikr = dhikr
        savedStateHandle[SELECTED_DHIKR_KEY] = dhikr
    }

    companion object {
        private const val COUNTER_KEY = "counter"
        private const val SELECTED_DHIKR_KEY = "selected_dhikr"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhikrCounterScreen(viewModel: DhikrCounterViewModel) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            style = TextStyle(
                textDirection = TextDirection.ContentOrRtl,
                fontFamily = NotoArabic
            )
        )

        Text(
            text = "Choose a Dhikr",
            style = MaterialTheme.typography.titleLarge,
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
                textStyle = TextStyle(
                    textDirection = TextDirection.ContentOrRtl,
                    fontSize = 22.sp,
                    lineHeight = 30.sp
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {
                ExposedDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    viewModel.dhikrList.forEachIndexed { index, dhikr ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = dhikr,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    style = TextStyle(
                                        textDirection = TextDirection.ContentOrRtl,
                                        fontSize = 22.sp,
                                        lineHeight = 30.sp
                                    )
                                )
                            },
                            onClick = {
                                viewModel.selectDhikr(dhikr)
                                menuExpanded = false
                            }
                        )

                        // faint separator, except after last item
                        if (index < viewModel.dhikrList.lastIndex) {
                            Divider(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp),
                                thickness = 1.dp,
                                color = Color.White.copy(alpha = 0.15f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { viewModel.changeCycleMode(DhikrCounterViewModel.CycleMode.MODE_33) },
                enabled = viewModel.cycleMode != DhikrCounterViewModel.CycleMode.MODE_33
            ) {
                Text("33")
            }

            OutlinedButton(
                onClick = { viewModel.changeCycleMode(DhikrCounterViewModel.CycleMode.MODE_99) },
                enabled = viewModel.cycleMode != DhikrCounterViewModel.CycleMode.MODE_99
            ) {
                Text("99")
            }
        }


        Text(
            text = viewModel.counter.toString(),
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current

        Button(
            onClick = {
                viewModel.incrementCounter()
                vibrate(context)
            },
            modifier = Modifier
                .height(52.dp)
                .padding(horizontal = 24.dp)
        ) {
            Text(text = "Count", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "بَارَكَ اللهُ لَنَا وَلِعَائِلَتِنَا فِي رَحْمَتِهِ وَبَرَكَتِهِ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = TextStyle(
                textDirection = TextDirection.ContentOrRtl,
                fontFamily = NotoArabic
            )
        )

        Spacer(modifier = Modifier.weight(0.6f))

        OutlinedButton(
            onClick = { viewModel.reset() }
        ) {
            Text("Reset")
        }
    }
}
