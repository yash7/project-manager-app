package ateamcomp354.projectmanagerapp.ui.gen;

import java.awt.BasicStroke;
import java.awt.Color;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.general.Series;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RefineryUtilities;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Project;

import ateamcomp354.projectmanagerapp.App;
import ateamcomp354.projectmanagerapp.model.Status;

public class EarnedValueChartGen extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public EarnedValueChartGen(String title, List<Activity> acts, List<Object> startProDate) {


        final XYDataset dataset = createDataset(acts, startProDate);
        final JFreeChart chart = createChart(dataset, title);
       
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        add(chartPanel);

        
    }
   
    public XYDataset createDataset(List<Activity> acts, List<Object> startProDate) {
    	
    	final XYSeries series1 = new XYSeries("Planned Value");
    	final XYSeries series2 = new XYSeries("Earned Value");
    	final XYSeries series3 = new XYSeries("Actual Cost");
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    	
    	Date startDate = (Date) startProDate.get(0);
    	int weeks = (int) startProDate.get(1);
    	
    	int PV = 0;
    	int EV = 0;
    	int AC = 0;
    	  
        try{
        	series1.add(0,0);
        	series2.add(0,0);
        	series3.add(0,0);
        	      	
        	for(Activity a:acts){
				Date LFinishDate = format.parse(a.getLatestFinish().toString());
			
				int aDays = (int)Math.abs((LFinishDate.getTime() - startDate.getTime()) / (1000*60*60*24));
				float aWeeks = (float) aDays/7;

				if(a.getStatus() == Status.NEW) {
					PV += a.getPlannedValue();
					
					series1.add(aWeeks,PV);
				}
				else if(a.getStatus() == Status.IN_PROGRESS) {
					PV += a.getPlannedValue();
					EV += a.getPlannedValue();
					
					series1.add(aWeeks,PV);
					series2.add(aWeeks,EV);
				}
				else{
					PV += a.getPlannedValue();
					AC += a.getActualCost();
					
					series1.add(aWeeks,PV);
					series3.add(aWeeks,AC);
				}
        	}
    	}catch (ParseException e) {
    		e.printStackTrace();
		}
        
        final XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(series1);
        collection.addSeries(series2);
        collection.addSeries(series3);

        return collection;
    }
        
    /**
     * Creates a chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return The chart.
     */
    private JFreeChart createChart(final XYDataset dataset, String title) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
        		title, 
        		"Weeks", 
        		"Value", 
        		dataset, 
        		PlotOrientation.VERTICAL, 
        		true, true, false);
        
     // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.BLUE );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.RED );
        renderer.setSeriesStroke( 0 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 3.0f ) );
        plot.setRenderer( renderer );
        
        
        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;    
    }
    

}
