
import com.example.simbirsofttasks.domain.model.TaskEntity
import com.example.simbirsofttasks.domain.repository.TaskRepo
import com.example.simbirsofttasks.domain.useCases.GetTaskByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class GetTaskByIdUseCaseTest {

    @Test
    fun `should return task when repository returns task`(){

        val taskRepository = mockk<TaskRepo>()
        val getTaskByIdUseCase = GetTaskByIdUseCase(taskRepository)

        val expectedTask = TaskEntity(
            id = 0,
            startDateTime = LocalDateTime.now(),
            endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now().plusHours(1)),
            color = 0,
            taskName = "Test task",
            taskDesc = "Test desc"
        )
        coEvery { taskRepository.getTaskById(0) } returns(expectedTask)

        val result = runBlocking { getTaskByIdUseCase(0) }

        assertEquals(expectedTask, result)

        coVerify { taskRepository.getTaskById(0) }
    }
}