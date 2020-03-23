package com.tired.model.task

import com.tired.model.task.exceptions.CannnotAdoptTaskException
import com.tired.model.user.User

class VoluntaryTaskService(private val repository: VoluntaryTaskRepository) {

    fun search(location: Location, size: Long, radiusKm: Double) = repository.search(location, size, radiusKm)

    fun adopt(id: String, user: User): VoluntaryTask {
        val task = repository.get(id)
        if (task.voluntary == null) {
            throw CannnotAdoptTaskException()
        }
        val adoptedTask = task.copy(voluntary = user.id.toString())
        repository.adopt(id, user.id.toString())
        return adoptedTask
    }

}