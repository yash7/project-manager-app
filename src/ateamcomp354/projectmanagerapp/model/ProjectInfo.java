package ateamcomp354.projectmanagerapp.model;

public class ProjectInfo {

    private final int completion;
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
