package ateamcomp354.projectmanagerapp.ui.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.Layer;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;

import ateamcomp354.projectmanagerapp.model.Status;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;

public class ActivityOnNode extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public ActivityOnNode(String title, List<Activity> acts) {
		
        final IntervalCategoryDataset dataset = createDataset(acts);
        final JFreeChart chart = createChart(dataset, title);
        chart.getCategoryPlot().addRangeMarker(new ValueMarker(new Date().getTime(), Color.BLACK, new BasicStroke(1.0F)), Layer.FOREGROUND);
        
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);

    }
	
	public IntervalCategoryDataset createDataset(List<Activity> acts) {
    	
    	final TaskSeries s1 = new TaskSeries("Critical Path");
    	final TaskSeries s2 = new TaskSeries("Non-Critical");
        
    	for(Activity a:acts){
               
	        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
	    	try{
				Date esdate = format.parse(String.valueOf(a.getEarliestStart()).trim());
				Date eedate = format.parse(String.valueOf(a.getEarliestFinish()).trim());
			
				Task t = new Task(a.getLabel(), new SimpleTimePeriod(esdate,eedate));
				if(a.getFloat() == 0) {
					//t.setPercentComplete(1);
					s1.add(t);
				}
				else {
					s2.add(t);
				}
	    	}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
        final TaskSeriesCollection collection = new TaskSeriesCollection();
        collection.add(s1);
        collection.add(s2);
        return collection;
    }

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
