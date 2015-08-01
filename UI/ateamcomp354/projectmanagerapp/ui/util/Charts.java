package ateamcomp354.projectmanagerapp.ui.util;

import ateamcomp354.projectmanagerapp.services.ActivityService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.ui.gen.ActivityOnNode;
import ateamcomp354.projectmanagerapp.ui.gen.EarnedValueChartGen;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A collection of functions for displaying charts for projects in a modal dialog.
 */
public class Charts {

    public static void viewCriticalPathsChart( ApplicationContext appCtx, Project project ) {

       ActivityService ase = appCtx.getActivityService(project.getId());

        List<Activity> acts = ase.getActivities();

        if(acts.size() > 0) {
            List<Integer> x = ase.calculateNumberOfStartingNodes(new ArrayList<Integer>(), acts.get(0).getId());
            if(x.size() == 1) {
                List<Integer> y = ase.calculateNumberOfEndingNodes(new ArrayList<Integer>(), acts.get(0).getId());
                if(y.size() == 1) {
                    List<Integer> sizeArray = ase.calculateSizeOfChain(new ArrayList<Integer>(), acts.get(0).getId());
                    if(acts.size() == sizeArray.size()) {
                        ase.calculateAllParamsOfChain(x.get(0), y.get(0));
                        openCriticalPathsChart(ase, project, y.get(0));
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "There are loose activities not linked to any others, all activities must be linked in order to create a working Critical Path Analysis");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "There are multiple ending activities (or dangles), there must be only a single end activity to create a working Critical Path Analysis");
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "There are multiple starting activities, there must be only a single start activity to create a working Critical Path Analysis");
            }
        }
        else {
            JOptionPane.showMessageDialog (null, "There are no activities for this project", "No Report", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private static void openCriticalPathsChart(ActivityService ase, Project project, int act) {
        ActivityOnNode chart = new ActivityOnNode(ase, project.getProjectName()+ " Critical Path Analysis", ase.getActivity(act));
        JOptionPane.showMessageDialog (null, chart, project.getProjectName()+" Critical Path Analysis", JOptionPane.PLAIN_MESSAGE);
    }

    public static void viewEVAnalysisChart(ApplicationContext appCtx, Project project){

        ProjectService projectService = appCtx.getProjectService();

        List<Activity> acts = projectService.EVactivitiesByEarliestStart(project.getId());

        if(acts.size()>0){
            List<Object> startProDate = projectService.EVStartDate(project.getId());
            openEVAnalysisChart(project, acts, startProDate);

        }
        else
            JOptionPane.showMessageDialog (null, "There are no activities for this project", "No Report", JOptionPane.PLAIN_MESSAGE);
    }

    public static void openEVAnalysisChart(Project project, List<Activity> acts, List<Object> startProDate){

        EarnedValueChartGen chart = new EarnedValueChartGen(project.getProjectName() + " Earned-Value Analysis",acts, startProDate);
        JOptionPane.showMessageDialog (null, chart, "Project", JOptionPane.PLAIN_MESSAGE);
    }
}