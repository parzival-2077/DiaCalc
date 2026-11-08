package com.parzival.diacalc.presentation.screen

import android.telephony.PhoneNumberUtils.formatNumber
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.parzival.diacalc.presentation.components.CalculatorTextField
import com.parzival.diacalc.presentation.components.ResultCard
import java.util.Locale


@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CalculatorContent(
        state = state,
        onCarbsChange = viewModel::onCarbsChange,
        onWeightChange = viewModel::onWeightChange,
        onGramsPerBreadUnitChange =
            viewModel::onGramsPerBreadUnitChange,
        onMultiplierChange = viewModel::onMultiplierChange
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorContent(
    state: CalculatorUIState,
    onCarbsChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onGramsPerBreadUnitChange: (String) -> Unit,
    onMultiplierChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        containerColor = colors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "DIACALC 2.0",
                        style = MaterialTheme.typography.labelLarge,
                        color = colors.onSurfaceVariant
                    )

                    Text(
                        text = "Калькулятор ХЕ",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colors.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        text = "Параметры продукта",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    CalculatorTextField(
                        value = state.carbsInput,
                        onValueChange = onCarbsChange,
                        label = "Углеводы на 100 г",
                        imeAction = ImeAction.Next,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )

                    CalculatorTextField(
                        value = state.weightInput,
                        onValueChange = onWeightChange,
                        label = "Вес порции, г",
                        imeAction = ImeAction.Next,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        CalculatorTextField(
                            modifier = Modifier.weight(1f),
                            value = state.gramsToXE,
                            onValueChange = onGramsPerBreadUnitChange,
                            label = "Грамм на 1 ХЕ",
                            imeAction = ImeAction.Next,
                            onImeAction = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        )

                        CalculatorTextField(
                            modifier = Modifier.weight(1f),
                            value = state.factorInput,
                            onValueChange = onMultiplierChange,
                            label = "Коэффициент",
                            imeAction = ImeAction.Done,
                            onImeAction = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        )
                    }
                }
            }


            ResultCard(
                title = "Хлебные единицы",
                value = formatNumber(state.result.XE),
                unit = "ХЕ",

                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {
                ResultCard(
                    title = "Углеводы",
                    value = formatNumber(
                        state.result.carbohydrates
                    ),
                    unit = "г",
                    modifier = Modifier.weight(1f)
                )

                ResultCard(
                    title = "Единиц инсулина",
                    value = formatNumber(
                        state.result.insulin
                    ),
                    unit = "",
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Результат носит справочный характер.",
                style = MaterialTheme.typography.bodySmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))


        }
    }
}

private fun formatNumber(value: Double): String {
    return String.format(
        Locale.getDefault(),
        "%.2f",
        value
    )
}

