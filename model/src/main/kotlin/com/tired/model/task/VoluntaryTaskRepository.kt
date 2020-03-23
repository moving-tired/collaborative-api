package com.tired.model.task

interface VoluntaryTaskRepository {

    fun post(task: VoluntaryTask)
    fun get(id: String): VoluntaryTask
    fun search(location: Location, size: Long, radiusKm: Double): List<VoluntaryTask>
    fun adopt(taskId: String, voluntary: String): VoluntaryTask

}