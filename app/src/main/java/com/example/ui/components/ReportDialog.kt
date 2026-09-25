package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.AppLanguage
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.AppStrings

enum class ReportType {
    USER,
    MOMENT,
    MESSAGE
}

@Composable
fun ReportDialog(
    targetName: String,
    reportType: ReportType,
    language: AppLanguage = AppLanguage.INDONESIAN,
    onDismiss: () -> Unit,
    onSubmitReport: (reason: String, notes: String, alsoBlock: Boolean) -> Unit
) {
    val context = LocalContext.current
    val reasons = remember(reportType, language) {
        when (reportType) {
            ReportType.USER -> AppStrings.reportReasonsUser(language)
            ReportType.MOMENT -> AppStrings.reportReasonsMoment(language)
            ReportType.MESSAGE -> AppStrings.reportReasonsMessage(language)
        }
    }

    var selectedReason by remember { mutableStateOf(reasons.first()) }
    var notes by remember { mutableStateOf("") }
    var alsoBlock by remember { mutableStateOf(reportType == ReportType.USER) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.ReportProblem,
                contentDescription = null,
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = when (reportType) {
                    ReportType.USER -> "${AppStrings.reportTitleUser(language)}: $targetName"
                    ReportType.MOMENT -> AppStrings.reportTitleMoment(language)
                    ReportType.MESSAGE -> AppStrings.reportTitleMessage(language)
                },
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = NeutralDark
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = AppStrings.reportSubtitle(language, targetName),
                    fontSize = 12.5.sp,
                    color = NeutralMedium,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                reasons.forEach { reason ->
                    val isSelected = selectedReason == reason
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFFFEBEE) else Color(0xFFF8FAFC)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color(0xFFEF5350) else NeutralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clickable { selectedReason = reason }
                            .testTag("report_reason_$reason")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = reason,
                                fontSize = 13.sp,
                                color = if (isSelected) Color(0xFFC62828) else NeutralDark,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { if (it.length <= 250) notes = it },
                    label = { Text(AppStrings.reportNotesPlaceholder(language).removeSuffix("..."), fontSize = 12.sp) },
                    placeholder = { Text(AppStrings.reportNotesPlaceholder(language), fontSize = 12.sp) },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = NeutralBorder
                    )
                )

                if (reportType == ReportType.USER) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { alsoBlock = !alsoBlock }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = alsoBlock,
                            onCheckedChange = { alsoBlock = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD32F2F))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = AppStrings.reportAlsoBlockCheckbox(language),
                            fontSize = 12.5.sp,
                            color = NeutralDark
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        AppStrings.reportSuccessToast(language),
                        Toast.LENGTH_LONG
                    ).show()
                    onSubmitReport(selectedReason, notes, alsoBlock)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("btn_submit_report")
            ) {
                Text(AppStrings.reportSubmitBtn(language), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("btn_cancel_report")
            ) {
                Text(AppStrings.btnCancel(language), color = NeutralMedium, fontSize = 13.sp)
            }
        },
        shape = RoundedCornerShape(18.dp),
        containerColor = Color.White
    )
}
