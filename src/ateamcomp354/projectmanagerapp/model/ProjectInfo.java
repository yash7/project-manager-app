package ateamcomp354.projectmanagerapp.model;

/**
 * Additional info for a project that is not in the auto generated code.
 */
public class ProjectInfo {

    /**
     * The percent completed, see ProjectService.getProjectCompletion() for more info
     */
    private final int completion;

    /**
     * The number of activities in a project, see ProjectService.getProjectActivitiesCount() for more info
     */
    private final int activitesCount;

    public ProjectInfo(int completion, int activitesCount) {
        this.completion = completion;
        this.activitesCount = activitesCount;
    }

    public int getCompletion() {
        return completion;
    }

    public int getActivitesCount() {
        return activitesCount;
    }
}
