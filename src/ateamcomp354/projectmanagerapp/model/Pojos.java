package ateamcomp354.projectmanagerapp.model;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

/**
 * A place for utils related to Jooq generated Pojos.
 *
 * Jooq does not generate an equals method for the generated pojos.
 * There may or may not be a way to configure that. At the time of writing nothing was known.
 */
public class Pojos {

    /**
     * Returns true if project p1 is logical equal to project p2, otherwise false.
     */
    public static boolean projectsEqual( Project p1, Project p2 ) {

        // this code was generated by an IDE

        if (p1 == p2) return true;
        if (p2 == null) return false;

        if (p1.getCompleted() != null ? !p1.getCompleted().equals(p2.getCompleted()) : p2.getCompleted() != null) return false;
        if (p1.getDescription() != null ? !p1.getDescription().equals(p2.getDescription()) : p2.getDescription() != null) return false;
        if (p1.getId() != null ? !p1.getId().equals(p2.getId()) : p2.getId() != null) return false;
        if (p1.getProjectName() != null ? !p1.getProjectName().equals(p2.getProjectName()) : p2.getProjectName() != null) return false;

        return true;
    }
}
