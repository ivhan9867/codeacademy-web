package com.example.ui.screens.social

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatConversationEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.FriendshipEntity
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FriendsScreen(
    friends: List<FriendshipEntity>,
    onAcceptFriend: (String, String) -> Unit,
    onAddFriend: (String) -> Unit,
    onOpenChat: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val acceptedFriends = remember(friends) { friends.filter { it.status == "ACCEPTED" } }
    val pendingFriends = remember(friends) { friends.filter { it.status == "PENDING" } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                }

                Text("Teman & Belajar Bareng", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)

                IconButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.clip(CircleShape).background(PrimaryPurple)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Tambah Teman", tint = Color.White)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 30.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (pendingFriends.isNotEmpty()) {
                item(key = "pending_header") {
                    Text(
                        text = "Permintaan Pertemanan (${pendingFriends.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentWarning
                    )
                }

                items(pendingFriends, key = { it.addresseeId }) { friend ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AccentWarning, PrimaryPurple)))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(PrimaryPurple),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(friend.friendName.first().toString().uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Column {
                                    Text(friend.friendName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                    Text(friend.friendBio, style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                                }
                            }

                            Button(
                                onClick = { onAcceptFriend(friend.requesterId, friend.addresseeId) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AccentSuccess)
                            ) {
                                Text("Terima", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            item(key = "friends_header") {
                Text(
                    text = "Daftar Teman (${acceptedFriends.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
            }

            items(acceptedFriends, key = { it.addresseeId }) { friend ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark)))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(friend.friendName.first().toString().uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text(friend.friendName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                Text("Level ${friend.friendLevel} • ${friend.friendXp} XP", style = MaterialTheme.typography.labelSmall, color = SecondaryTeal)
                            }
                        }

                        IconButton(
                            onClick = { onOpenChat(friend.addresseeId) },
                            modifier = Modifier.clip(CircleShape).background(PrimaryPurple.copy(alpha = 0.2f))
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "Kirim Pesan", tint = SecondaryTeal, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var friendNameInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Teman Belajar", fontWeight = FontWeight.Bold, color = TextPrimaryDark) },
            text = {
                OutlinedTextField(
                    value = friendNameInput,
                    onValueChange = { friendNameInput = it },
                    label = { Text("Nama Pengguna Teman") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (friendNameInput.isNotBlank()) {
                            onAddFriend(friendNameInput)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                ) {
                    Text("Kirim Permintaan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Batal", color = TextSecondaryDark)
                }
            },
            containerColor = SurfaceDark
        )
    }
}

@Composable
fun ChatListScreen(
    conversations: List<ChatConversationEntity>,
    onOpenConversation: (String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                }
                Text("Pesan Pribadi (Direct Chat)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                Box(modifier = Modifier.size(40.dp))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(conversations, key = { it.id }) { conv ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenConversation(conv.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(BorderDark, BorderDark)))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(conv.participantName.first().toString().uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(conv.participantName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                Text(
                                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(conv.lastMessageTime)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMutedDark
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = conv.lastMessage ?: "Mulai percakapan...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondaryDark,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatRoomScreen(
    conversation: ChatConversationEntity?,
    messages: List<ChatMessageEntity>,
    currentUserId: String? = null,
    onNavigateBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (conversation == null) {
        Box(modifier = Modifier.fillMaxSize().background(BackgroundDark), contentAlignment = Alignment.Center) {
            Text("Percakapan tidak ditemukan", color = TextSecondaryDark)
        }
        return
    }

    var textInput by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundDark)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.clip(CircleShape).background(SurfaceDark)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = TextPrimaryDark)
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(conversation.participantName.first().toString().uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Column {
                        Text(conversation.participantName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                        Text("Online • Siswa Aktif", style = MaterialTheme.typography.labelSmall, color = AccentSuccess)
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = SurfaceDark,
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(BorderDark, SurfaceDark))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Ketik pesan...", color = TextMutedDark, fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BackgroundDark,
                            unfocusedContainerColor = BackgroundDark
                        )
                    )

                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput)
                                textInput = ""
                            }
                        },
                        enabled = textInput.isNotBlank(),
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (textInput.isNotBlank()) PrimaryPurple else SurfaceVariantDark)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Kirim", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                val isMe = (currentUserId != null && msg.senderId == currentUserId) || msg.senderId == "my_user"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 4.dp,
                            bottomEnd = if (isMe) 4.dp else 16.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMe) PrimaryPurple else SurfaceDark
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.body,
                                color = if (isMe) Color.White else TextPrimaryDark,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(msg.sentAt)),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isMe) Color.White.copy(alpha = 0.7f) else TextMutedDark,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}
