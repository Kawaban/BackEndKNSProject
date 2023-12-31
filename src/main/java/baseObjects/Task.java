package baseObjects;

import additionalObjects.Specialization;
import additionalObjects.TaskState;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @Column(name = "id")
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "deadline")
    private LocalDate deadline;
    @Column(name = "created_by")
    private long createdBy;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_state")
    private TaskState taskState;
    @Column(name = "name")
    private String name;
    @Column(name = "estimation")
    private int estimation;
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;
    @OneToOne(mappedBy = "task")
    private Developer assignedTo;

    public Task(long id, Project project, LocalDate createdAt, LocalDate deadline, long createdBy, String name, int estimation, Specialization specialization) {
        this.id = id;
        this.project = project;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.name = name;
        this.estimation = estimation;
        this.specialization = specialization;
        this.taskState = TaskState.DEFAULT;
        this.deadline = deadline;
    }

    public Task(long id, Project project, LocalDate createdAt, LocalDate deadline, long createdBy, String name, int estimation, Specialization specialization, Developer assignedTo) {
        this.id = id;
        this.project = project;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.taskState = TaskState.DEFAULT;
        this.name = name;
        this.estimation = estimation;
        this.specialization = specialization;
        this.assignedTo = assignedTo;
        this.deadline = deadline;
    }

    public Task(long id, Project project, LocalDate createdAt, LocalDate deadline, long createdBy, TaskState taskState, String name, int estimation, Specialization specialization, Developer assignedTo) {
        this.id = id;
        this.project = project;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.taskState = taskState;
        this.name = name;
        this.estimation = estimation;
        this.specialization = specialization;
        this.assignedTo = assignedTo;
        this.deadline = deadline;
    }

    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Developer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Developer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public JSONObject ToJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("project_id", project.getId());
        obj.put("createdAt", createdAt.toString());
        obj.put("createdBy", createdBy);
        obj.put("name", name);
        obj.put("taskState", taskState);
        obj.put("specialization", specialization.toString());
        obj.put("estimation", estimation);
        if (assignedTo != null)
            obj.put("assignedTo", assignedTo.getId());
        else
            obj.put("assignedTo", "null");
        obj.put("deadline", deadline.toString());
        return obj;
    }
}
