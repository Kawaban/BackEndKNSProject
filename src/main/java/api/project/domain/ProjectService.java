package api.project.domain;

import api.developer.DeveloperService;
import api.developer.domain.Developer;
import api.infrastructure.exception.EntityNotFoundException;
import api.project.dto.ProjectRequest;
import api.project.dto.ProjectResponse;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
record ProjectService(ProjectRepository projectRepository, DeveloperService developerService, ProjectMapper projectMapper) implements api.project.ProjectService {

    public ProjectResponse getProject(UUID projectId) throws EntityNotFoundException {
        val project = projectRepository.findById(projectId)
                .orElseThrow(EntityNotFoundException::new);

        return projectMapper.toProjectResponse(project);

    }

    public void addProject(ProjectRequest projectRequest) {
        ArrayList<Developer> developers = new ArrayList<>();
        projectRequest.developers().forEach(developerId ->
                developers.add(developerService.findByDeveloperId(developerId)));

        val project = Project.builder()
                .name(projectRequest.projectName())
                .projectDevelopers(developers)
                .build();
        projectRepository.save(project);
    }

    public Project findByProjectId(UUID projectId) throws EntityNotFoundException {
        return projectRepository.findByUuid(projectId).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteProject(UUID projectId) throws EntityNotFoundException {
        projectRepository.delete(findByProjectId(projectId));
    }

    public void updateProject(UUID projectId, ProjectRequest projectRequest) throws EntityNotFoundException {
        val project = findByProjectId(projectId);
        project.setName(projectRequest.projectName());
        projectRepository.save(project);
    }

    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toProjectResponse)
                .toList();
    }

    @Override
    public List<ProjectResponse> getAllProjectsForUser(UUID userId) {
        return projectRepository.findAll().stream()
                .filter(project -> project.getProjectDevelopers().stream()
                        .anyMatch(developer -> developer.getUuid().equals(userId)))
                .map(projectMapper::toProjectResponse)
                .toList();
    }

}
