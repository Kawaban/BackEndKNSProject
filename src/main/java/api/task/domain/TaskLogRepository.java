package api.task.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskLogRepository extends JpaRepository<TaskLog, UUID> {
}
