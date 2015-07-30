package ateamcomp354.projectmanagerapp.ui.util;

import ateamcomp354.projectmanagerapp.services.ProjectService;
import ateamcomp354.projectmanagerapp.services.ApplicationContext;
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
