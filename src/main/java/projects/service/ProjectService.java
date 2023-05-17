package projects.service;

import projects.dao.ProjectDao;
import projects.entity.Project;

public class ProjectService {
    private ProjectDao projectDao = new ProjectDao(); // Create an instance of ProjectDao

    // Add a project by calling the insertProject method of ProjectDao
    public Project addProject(Project project) {
        return projectDao.insertProject(project);
    }
}