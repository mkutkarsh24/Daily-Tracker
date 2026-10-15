package com.utkarsh.dailytrack.data

class DailyTrackRepository(private val dao: DailyTrackDao) {
    val tasks = dao.observeTasks()
    val habits = dao.observeHabits()

    suspend fun addTask(title: String) = dao.insertTask(TaskEntity(title = title.trim()))
    suspend fun toggleTask(task: TaskEntity) = dao.updateTask(task.copy(isDone = !task.isDone))
    suspend fun deleteTask(id: Long) = dao.deleteTask(id)

    suspend fun addHabit(title: String) = dao.insertHabit(HabitEntity(title = title.trim()))
    suspend fun toggleHabit(habit: HabitEntity) =
        dao.updateHabit(habit.copy(completedToday = !habit.completedToday))
    suspend fun deleteHabit(id: Long) = dao.deleteHabit(id)
}
