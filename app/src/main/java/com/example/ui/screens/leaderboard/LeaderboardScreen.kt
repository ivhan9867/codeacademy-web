package com.example.ui.screens.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FriendshipEntity
import com.example.data.model.ProfileEntity
import com.example.ui.theme.*

data class LeaderboardUser(
    val id: String,
    val name: String,
    val xp: Int,
    val level: Int,
    val streak: Int,
    val isCurrentUser: Boolean = false
)

@Composable
fun LeaderboardScreen(
    profile: ProfileEntity?,
    friends: List<FriendshipEntity>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Mingguan, 1: Sepanjang Masa

    val allUsers = remember(profile, friends) {
        val list = mutableListOf<LeaderboardUser>()
        if (profile != null) {
            list.add(
                LeaderboardUser(
                    id = profile.id,
                    name = profile.displayName,
                    xp = profile.totalXp,
                    level = profile.level,
                    streak = profile.currentStreak,
                    isCurrentUser = true
                )
            )
        }
        friends.forEach { f ->
            list.add(
                LeaderboardUser(
                    id = f.addresseeId,
                    name = f.friendName,
                    xp = f.friendXp,
                    level = f.friendLevel,
                    streak = 4,
                    isCurrentUser = false
                )
            )
        }
        // Add additional sample leaderboard contenders
        list.add(LeaderboardUser("u1", "Sarah Putri", 1850, 8, 14))
        list.add(LeaderboardUser("u2", "Rian Hidayat", 1420, 6, 9))
        list.add(LeaderboardUser("u3", "Devina Clara", 1120, 5, 7))
        list.add(LeaderboardUser("u4", "Budi Santoso", 940, 4, 6))

        list.sortedByDescending { it.xp }
    }

    val currentUserRank = remember(allUsers) {
        val idx = allUsers.indexOfFirst { it.isCurrentUser }
        if (idx != -1) idx + 1 else 1
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        bottomBar = {
            if (profile != null) {
                Surface(
                    color = SurfaceDark,
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(PrimaryPurple, SecondaryTeal))),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 70.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryPurple),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("#$currentUserRank", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Column {
                                Text("Peringkatmu (${profile.displayName})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextPrimaryDark)
                                Text("Level ${profile.level} • ${profile.currentStreak} Hari Streak", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                            }
                        }

                        Text(
                            text = "${profile.totalXp} XP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryTeal
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "leaderboard_header") {
                Text(
                    text = "Papan Peringkat (Leaderboard) 🏆",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryDark
                )
                Spacer(modifier = Modifier.height(14.dp))

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SurfaceDark,
                    contentColor = SecondaryTeal,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("⚡ Mingguan", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("🌟 Sepanjang Masa", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Top 3 Podium
            if (allUsers.size >= 3) {
                item(key = "top_podium") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Rank 2 (Silver)
                        PodiumCard(
                            user = allUsers[1],
                            rank = 2,
                            badgeColor = Color(0xFFC0C0C0),
                            height = 140.dp,
                            modifier = Modifier.weight(1f)
                        )

                        // Rank 1 (Gold)
                        PodiumCard(
                            user = allUsers[0],
                            rank = 1,
                            badgeColor = Color(0xFFFFD700),
                            height = 165.dp,
                            modifier = Modifier.weight(1.1f)
                        )

                        // Rank 3 (Bronze)
                        PodiumCard(
                            user = allUsers[2],
                            rank = 3,
                            badgeColor = Color(0xFFCD7F32),
                            height = 125.dp,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Rest of leaderboard
            itemsIndexed(allUsers.drop(3)) { index, user ->
                val rank = index + 4
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (user.isCurrentUser) PrimaryPurple.copy(alpha = 0.15f) else SurfaceDark
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = if (user.isCurrentUser) Brush.horizontalGradient(listOf(PrimaryPurple, SecondaryTeal))
                        else Brush.horizontalGradient(listOf(BorderDark, BorderDark))
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "#$rank",
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryDark,
                            fontSize = 14.sp,
                            modifier = Modifier.width(30.dp)
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(PrimaryPurple, SecondaryTeal))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name.first().toString().uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name + if (user.isCurrentUser) " (Kamu)" else "",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (user.isCurrentUser) SecondaryTeal else TextPrimaryDark
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("L${user.level}", style = MaterialTheme.typography.labelSmall, color = TextSecondaryDark)
                                Text("•", style = MaterialTheme.typography.labelSmall, color = TextMutedDark)
                                Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = AccentStreak, modifier = Modifier.size(13.dp))
                                Text("${user.streak}h", style = MaterialTheme.typography.labelSmall, color = AccentStreak)
                            }
                        }

                        Text(
                            text = "${user.xp} XP",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumCard(
    user: LeaderboardUser,
    rank: Int,
    badgeColor: Color,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(height),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(badgeColor, BorderDark)))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = user.name,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark,
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            Text(
                text = "${user.xp} XP",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SecondaryTeal
            )
        }
    }
}
