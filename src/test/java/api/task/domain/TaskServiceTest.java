package api.task.domain;

import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.infrastructure.model.FibonacciChecker;
import api.project.ProjectService;
import api.project.domain.Project;
import api.task.domain.TaskService;
import api.task.dto.TaskChange;
import api.task.dto.TaskRequest;
import api.task.dto.TaskResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import api.infrastructure.model.Specialization;
import api.infrastructure.model.TaskState;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

        @Mock
        private TaskRepository taskRepository;

        @Mock
        private DeveloperService developerService;

        @Mock
        private ProjectService projectService;

        @Mock
        private TaskLogRepository taskLogRepository;

        @Mock
        private FibonacciChecker fibonacciChecker;

        @InjectMocks
        private TaskService taskService;

        private Task testTask;
        private Developer testDeveloper;
        private Project testProject;
        private UUID testTaskId;

        @BeforeEach
        public void setUp() {
                MockitoAnnotations.openMocks(this);
                testTaskId = UUID.randomUUID();
                testTask = new Task();
                testTask.setUuid(testTaskId);
                testTask.setName("Test Task");
                testTask.setDescription("Test Description");
                testTask.setTaskState(TaskState.ASSIGNED);
                testDeveloper= new Developer();
                testDeveloper.setUuid(UUID.randomUUID());
                testTask.setAssignedTo(testDeveloper);
                testTask.setCreatedBy(UUID.randomUUID());
                testTask.setCreatedAt(LocalDate.now());
                testTask.setDeadline(LocalDate.now().plusDays(5));
                testProject = new Project();
                testProject.setUuid(UUID.randomUUID());
                testTask.setProject(testProject);
                testTask.setEstimation(5);
                testTask.setSpecialization(Specialization.BACKEND);
        }



        @Test
        public void testGetTask_TaskExists() throws EntityNotFoundException {
//                UUID taskId = UUID.randomUUID();
//                Task task = new Task();
//                task.setUuid(taskId);
//                task.setName("Test Task");
//                task.setDescription("Test Description");
//                task.setTaskState(TaskState.ASSIGNED);
//                Developer developer = new Developer();
//                developer.setUuid(UUID.randomUUID());
//                task.setAssignedTo(developer);
//                task.setCreatedBy(UUID.randomUUID());
//                task.setCreatedAt(LocalDate.now());
//                task.setDeadline(LocalDate.now().plusDays(5));
//                Project project = new Project();
//                project.setUuid(UUID.randomUUID());
//                task.setProject(project);
//                task.setEstimation(5);
//                task.setSpecialization(Specialization.BACKEND);

                when(taskService.taskRepository().findById(testTaskId)).thenReturn(Optional.of(testTask));

                TaskResponse response = taskService.getTask(testTaskId);

                assertNotNull(response);
                assertEquals(testTaskId, response.taskId());
                assertEquals("Test Task", response.name());
                assertEquals("Test Description", response.description());
                assertEquals("ASSIGNED", response.status());
                assertEquals(testTask.getAssignedTo().getUuid(), response.assignedTo());
        }

        @Test
        public void testGetTask_TaskDoesNotExist() {
                UUID taskId = UUID.randomUUID();
                when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class, () -> taskService.getTask(taskId));
        }

        @Test
        public void testAddTask_ValidTask() throws EntityNotFoundException {
                UUID projectId = UUID.randomUUID();
                TaskRequest taskRequest = new TaskRequest("Test Task", "Test Description", "COMPLETED", UUID.randomUUID(), "2023-07-27", "2023-07-30", 5, "BACKEND", UUID.randomUUID());

                when(fibonacciChecker.isFibonacci(taskRequest.estimation())).thenReturn(true);
                when(developerService.findByDeveloperId(taskRequest.assignedTo())).thenReturn(testDeveloper);
                when(projectService.findByProjectId(projectId)).thenReturn(testProject);

                taskService.addTask(projectId, taskRequest);

                verify(taskService.taskRepository(), times(1)).save(any(Task.class));
        }

        @Test
        public void testAddTask_InvalidEstimation() {
                UUID projectId = UUID.randomUUID();
                TaskRequest taskRequest = new TaskRequest("Test Task", "Test Description", "IN_PROGRESS", UUID.randomUUID(), "2023-07-27", "2023-07-30", 5, "BACKEND", UUID.randomUUID());

                when(fibonacciChecker.isFibonacci(taskRequest.estimation())).thenReturn(true);

                assertThrows(IllegalArgumentException.class, () -> taskService.addTask(projectId, taskRequest));
        }

        @Test
        public void testUpdateTaskStatus_TaskExists() throws EntityNotFoundException {

                TaskChange taskChange = new TaskChange("COMPLETED");

                when(taskRepository.findById(testTaskId)).thenReturn(Optional.of(testTask));
                when(projectService.findByProjectId(testProject.getUuid())).thenReturn(testProject);
                taskService.updateTaskStatus(testProject.getUuid(),testTaskId, taskChange);

                verify(taskLogRepository, times(1)).save(any(TaskLog.class));
                verify(taskRepository, times(1)).delete(testTask);
                verify(taskRepository, times(1)).save(any(Task.class));
        }

        @Test
        public void testUpdateTaskStatus_TaskDoesNotExist() {
                UUID taskId = UUID.randomUUID();
                TaskChange taskChange = new TaskChange("COMPLETED");

                when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class, () -> taskService.updateTaskStatus(UUID.randomUUID(), taskId, taskChange));
        }

        @Test
        public void testFindByTaskId_TaskExists() throws EntityNotFoundException {
                UUID taskId = UUID.randomUUID();
                Task task = new Task();
                task.setUuid(taskId);

                when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

                Task foundTask = taskService.findByTaskId(taskId);

                assertNotNull(foundTask);
                assertEquals(taskId, foundTask.getUuid());
        }

        @Test
        public void testFindByTaskId_TaskDoesNotExist() {
                UUID taskId = UUID.randomUUID();

                when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

                assertThrows(EntityNotFoundException.class, () -> taskService.findByTaskId(taskId));
        }

        @Test
        public void testUpdateTask() {
                taskService.updateTask(testTask);

                verify(taskService.taskRepository(), times(1)).save(testTask);
        }

}
