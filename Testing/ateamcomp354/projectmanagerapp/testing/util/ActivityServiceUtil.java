package ateamcomp354.projectmanagerapp.testing.util;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityServiceUtil {

    public static void calculateAllParams(ActivityService activityService) {

        List<Activity> acts = activityService.getActivities();

        if (acts.isEmpty()) {
            return;
        }

        List<Integer> x = activityService.calculateNumberOfStartingNodes(new ArrayList<Integer>(), acts.get(0).getId());

        if ( x.size() != 1) {
            return;
        }


        List<Integer> y = activityService.calculateNumberOfEndingNodes(new ArrayList<Integer>(), acts.get(0).getId());

        if(y.size() != 1) {
            return;
        }

        List<Integer> sizeArray = activityService.calculateSizeOfChain(new ArrayList<Integer>(), acts.get(0).getId());

        if(acts.size() != sizeArray.size()) {
            return;
        }

        activityService.calculateAllParamsOfChain(x.get(0), y.get(0));
    }
}
