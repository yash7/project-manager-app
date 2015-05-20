package ateamcomp354.projectmanagerapp.services;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;

import java.util.List;

// User story 4 proposition
public interface ProjectMemberService {

    List<Activity> getAssignedActivities();

    void markPercentCompleteOfActivity( Activity activity );
}
