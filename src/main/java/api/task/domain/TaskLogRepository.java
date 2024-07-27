package api.task.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
interface TaskLogRepository extends JpaRepository<TaskLog, UUID> {
}
