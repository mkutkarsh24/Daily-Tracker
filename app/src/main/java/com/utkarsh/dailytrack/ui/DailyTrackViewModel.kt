package com.utkarsh.dailytrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utkarsh.dailytrack.data.DailyTrackRepository
import com.utkarsh.dailytrack.data.HabitEntity
import com.utkarsh.dailytrack.data.TaskEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DailyTrackViewModel(private val repository: DailyTrackRepository) : ViewModel() {
    val tasks = repository.tasks.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )
    val habits = repository.habits.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList()
    )

    fun addTask(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addTask(title) }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch { repository.toggleTask(task) }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch { repository.deleteTask(id) }
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        viewModelScope.launch { repository.addHabit(title) }
    }

    fun toggleHabit(habit: HabitEntity) {
        viewModelScope.launch { repository.toggleHabit(habit) }
    }

    fun deleteHabit(id: Long) {
        viewModelScope.launch { repository.deleteHabit(id) }
    }
}
