package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MomentComment
import com.example.model.MomentItem
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NeutralBorder
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.util.AppLanguage
import com.example.util.AppStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomentCommentsBottomSheet(
    moment: MomentItem,
    comments: List<MomentComment>,
    language: AppLanguage = AppLanguage.INDONESIAN,
    onDismiss: () -> Unit,
    onAddComment: (String) -> Unit,
    onAuthorClick: ((authorId: String, authorName: String) -> Unit)? = null
) {
    val context = LocalContext.current
    var commentInputText by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        dragHandle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFE0E0E0))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // Header Sheet Komentar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = AppStrings.momentsCommentsTitle(language),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = CircleShape,
                            color = EmeraldGreen.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "${comments.size}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "${AppStrings.reportTypeMoment(language)} (${moment.authorName})",
                        fontSize = 12.sp,
                        color = NeutralMedium
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("btn_close_moment_comments")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = AppStrings.btnCancel(language),
                        tint = NeutralDark
                    )
                }
            }

            HorizontalDivider(color = NeutralBorder.copy(alpha = 0.6f), thickness = 0.6.dp)

            // List Komentar
            if (comments.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF1F8E9),
                            modifier = Modifier.size(52.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ChatBubbleOutline,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = AppStrings.momentsNoCommentsTitle(language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NeutralDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = AppStrings.momentsNoCommentsDesc(language),
                            fontSize = 12.sp,
                            color = NeutralMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp, max = 380.dp)
                ) {
                    items(comments, key = { it.id }) { comment ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            LovyAvatar(
                                name = comment.authorName,
                                avatarColorHex = comment.authorAvatarHex,
                                avatarUrl = comment.authorAvatarUrl,
                                size = 38.dp,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable {
                                        onAuthorClick?.invoke(comment.authorId, comment.authorName)
                                    }
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Surface(
                                    shape = RoundedCornerShape(
                                        topStart = 4.dp,
                                        topEnd = 14.dp,
                                        bottomStart = 14.dp,
                                        bottomEnd = 14.dp
                                    ),
                                    color = Color(0xFFF5F6F8),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = comment.authorName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = NeutralDark,
                                                modifier = Modifier.clickable {
                                                    onAuthorClick?.invoke(comment.authorId, comment.authorName)
                                                }
                                            )
                                            Text(
                                                text = comment.timeAgo,
                                                fontSize = 10.5.sp,
                                                color = NeutralMedium
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = comment.text,
                                            fontSize = 13.sp,
                                            color = NeutralDark,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = NeutralBorder.copy(alpha = 0.5f), thickness = 0.6.dp)

            // Input Bar untuk Kirim Komentar
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    OutlinedTextField(
                        value = commentInputText,
                        onValueChange = { commentInputText = it },
                        placeholder = {
                            Text(
                                AppStrings.momentsWriteComment(language),
                                fontSize = 13.sp,
                                color = NeutralMedium
                            )
                        },
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(22.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = NeutralDark,
                            unfocusedTextColor = NeutralDark,
                            cursorColor = EmeraldGreen,
                            focusedBorderColor = EmeraldGreen,
                            unfocusedBorderColor = NeutralBorder.copy(alpha = 0.8f),
                            focusedContainerColor = Color(0xFFFAFAFA),
                            unfocusedContainerColor = Color(0xFFFAFAFA)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_moment_comment")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            val textToSend = commentInputText.trim()
                            if (textToSend.isNotBlank()) {
                                onAddComment(textToSend)
                                commentInputText = ""
                                Toast.makeText(context, AppStrings.momentsCommentSent(language), Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = commentInputText.isNotBlank(),
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (commentInputText.isNotBlank()) EmeraldGreen else Color(0xFFE0E0E0)
                            )
                            .testTag("btn_send_moment_comment")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = AppStrings.momentsWriteComment(language),
                            tint = if (commentInputText.isNotBlank()) Color.White else NeutralMedium,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
