package ateamcomp354.projectmanagerapp.services.impl;

import ateamcomp354.projectmanagerapp.services.ProjectService;
import org.jooq.DSLContext;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.util.List;

public class ProjectServiceImpl implements ProjectService {

    private final DSLContext create;

    public ProjectServiceImpl(DSLContext create) {
        this.create = create;
    }

    @Override
    public List<Project> getProjects() {
        return null;
    }

    @Override
    public Project getProject(int id) {
        return null;
    }

    @Override
    public void addProject(Project project) {

    }

    @Override
    public void deleteProject(Project project) {

    }

    @Override
    public void updateProject(Project project) {

    }
}
