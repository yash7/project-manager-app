package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import java.util.List;

/**
 * Provides project related functionality.
 */
public interface ProjectService {

    /**
     * @return all the projects that exist.
     */
    List<Project> getProjects();

    /**
     * A convenience method to obtain a project by id.
     *
     * @param id The project id.
     * @return A project whose id corresponds to the id parameter.
     */
    Project getProject( int id );

    /**
     * @param id The id of the project to determine completion.
     * @return A percentage from 0 to 100.
     */
    int getProjectCompletion( int id );

    /**
     * Add a new project. The id property must be null. Other properties
     * may or may not be null.
     *
     * @param project The project to add.
     */
    void addProject( Project project );

    /**
     * Delete a project from existence! The id property cannot be null.
     *
     * @param projectId The id of the project to murder.
     */
    void deleteProject( int projectId );

    /**
     * Update changes made on a project. The id property cannot be null
     * as well as any other non null fields.
     *
     * @param project The project to update
     */
    void updateProject( Project project );
}
