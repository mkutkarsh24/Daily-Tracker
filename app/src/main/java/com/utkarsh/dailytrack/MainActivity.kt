package com.utkarsh.dailytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.utkarsh.dailytrack.data.DailyTrackDatabase
import com.utkarsh.dailytrack.data.DailyTrackRepository
import com.utkarsh.dailytrack.data.HabitEntity
import com.utkarsh.dailytrack.data.TaskEntity
import com.utkarsh.dailytrack.ui.DailyTrackViewModel
import com.utkarsh.dailytrack.ui.DailyTrackViewModelFactory

class MainActivity : ComponentActivity() {
    private val dailyTrackViewModel: DailyTrackViewModel by viewModels {
        DailyTrackViewModelFactory(
            DailyTrackRepository(DailyTrackDatabase.getInstance(applicationContext).dailyTrackDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    DailyTrackApp(dailyTrackViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTrackApp(vm: DailyTrackViewModel) {
    val tasks by vm.tasks.collectAsStateWithLifecycle()
    val habits by vm.habits.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDialog by remember { mutableStateOf(false) }

    val doneTasks = tasks.count { it.isDone }
    val doneHabits = habits.count { it.completedToday }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("DailyTrack", fontWeight = FontWeight.Bold)
                        Text(
                            "Small steps. Better days.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                androidx.compose.material3.Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Text("Your daily progress", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "$doneTasks of ${tasks.size} tasks completed · $doneHabits of ${habits.size} habits done",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (tasks.isEmpty() && habits.isEmpty()) "Start with one small goal today."
                        else "Keep going — consistency beats perfection.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Tasks") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Habits") })
            }

            if (selectedTab == 0) {
                if (tasks.isEmpty()) {
                    EmptyState("No tasks yet", "Tap + to add your first task.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tasks, key = { it.id }) { task ->
                            TaskRow(task, onToggle = { vm.toggleTask(task) }, onDelete = { vm.deleteTask(task.id) })
                        }
                    }
                }
            } else {
                if (habits.isEmpty()) {
                    EmptyState("No habits yet", "Add a habit you want to practise every day.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(habits, key = { it.id }) { habit ->
                            HabitRow(habit, onToggle = { vm.toggleHabit(habit) }, onDelete = { vm.deleteHabit(habit.id) })
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddItemDialog(
            isHabit = selectedTab == 1,
            onDismiss = { showAddDialog = false },
            onAdd = { title ->
                if (selectedTab == 0) vm.addTask(title) else vm.addHabit(title)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun EmptyState(title: String, message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 42.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(message, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TaskRow(task: TaskEntity, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isDone, onCheckedChange = { onToggle() })
            Text(
                task.title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
            )
            TextButton(onClick = onDelete) {
                androidx.compose.material3.Icon(Icons.Default.Delete, contentDescription = "Delete task")
            }
        }
    }
}

@Composable
private fun HabitRow(habit: HabitEntity, onToggle: () -> Unit, onDelete: () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = habit.completedToday, onCheckedChange = { onToggle() })
            Column(modifier = Modifier.weight(1f)) {
                Text(habit.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    if (habit.completedToday) "Completed today" else "Not done yet",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(onClick = onDelete) {
                androidx.compose.material3.Icon(Icons.Default.Delete, contentDescription = "Delete habit")
            }
        }
    }
}

@Composable
private fun AddItemDialog(isHabit: Boolean, onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var title by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isHabit) "Add a habit" else "Add a task") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (isHabit) "Habit name" else "Task name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onAdd(title.trim()) }, enabled = title.isNotBlank()) {
                Text("Save")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}
