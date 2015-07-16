package ateamcomp354.projectmanagerapp.ui.gen;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.model.Status;

public class GanttChartGen extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public GanttChartGen(String title, List<Activity> acts) {


        final IntervalCategoryDataset dataset = createDataset(acts);
        final JFreeChart chart = createChart(dataset, title);

        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);

    }

   
    public IntervalCategoryDataset createDataset(List<Activity> acts) {
    	
    	final TaskSeries s1 = new TaskSeries("Open");
    	final TaskSeries s2 = new TaskSeries("In Progress");
    	final TaskSeries s3 = new TaskSeries("Complete");
        
    	for(Activity a:acts){
               
	        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
	    	try{
				Date esdate = format.parse(String.valueOf(a.getEarliestStart()).trim());
				Date eedate = format.parse(String.valueOf(a.getLatestFinish()).trim());
			
				Task t = new Task(a.getLabel(), new SimpleTimePeriod(esdate,eedate));
				if(a.getStatus() == Status.RESOLVED) {
					t.setPercentComplete(1);
					s3.add(t);
				}
				else if(a.getStatus() == Status.IN_PROGRESS) {
					s2.add(t);
				}
				else {
					s1.add(t);
				}
	    	}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        collection.add(s2);
        collection.add(s3);

        return collection;
    }

    private Date date(final int day, final int month, final int year) {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        final Date result = calendar.getTime();
        return result;

    }
        
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final IntervalCategoryDataset dataset, String title) {
        final JFreeChart chart = ChartFactory.createGanttChart(
            title,  // chart title
            "Activity",              // domain axis label
            "Date",              // range axis label
            dataset,             // data
            true,                // include legend
            true,                // tooltips
            false                // urls
        );    

        return chart;    
    }
    

}
