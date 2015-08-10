package ateamcomp354.projectmanagerapp.ui.gen;
 
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;
import ateamcomp354.projectmanagerapp.services.ActivityService;
 
public class PERTChartGen extends JPanel {
    private static final long serialVersionUID 				= 1L;
    private ActivityService ase 							= null;
    private ArrayList<ArrayList<Properties>> verticesArray 	= null;
    private ArrayList<Properties> edges 					= null;
    private List<Activity> activities						= null;
    private Integer currentEvent 							= 1;
    private Integer closestFromDistance 					= -1;
    private boolean createVertex 							= false;
     
    public PERTChartGen(ActivityService ase, List<Activity> independentActivities) {
    	this.ase = ase;
    	verticesArray 	= new ArrayList<ArrayList<Properties>>();
        edges 			= new ArrayList<Properties>();
        activities 		= ase.getActivities();
       
        mxGraph graph 	= new mxGraph();
		Object parent 	= graph.getDefaultParent();
		
		Map<String, Object> edge = new HashMap<String, Object>();
	    edge.put(mxConstants.STYLE_ROUNDED, true);
	    edge.put(mxConstants.STYLE_ORTHOGONAL, false);
	    //edge.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
	    edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
	    edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
	    edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
	    edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_TOP);
	    edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
	    edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");
	    edge.put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
	    
	    mxStylesheet edgeStyle = new mxStylesheet();
	    edgeStyle.setDefaultEdgeStyle(edge);
	    graph.setStylesheet(edgeStyle);
	    graph.setKeepEdgesInBackground(true);
	    
        processIndependentActivities(independentActivities);
        generateChart();
        
        graph.getModel().beginUpdate();
       
        try {
    		//ase.calculateAllParamsOfChain(startingNodes.get(0), endingNodes.get(0));
    		//activities 	= ase.getActivities();
    		
    		for (Activity a : activities) {
        		this.ase.setActivityDuration(a);
        	}
    		
        	ase.calculateEstimatesAndDerivatives(activities);
        	
        	for (int x = 0; x < verticesArray.size(); ++x) {
        		for (int y = 0; y < verticesArray.get(x).size(); ++y) {
        			Object v = null;
        			
        			float maximumExpectedDate 		= 0;
    				float maximumStandardDeviation 	= 0;
        			
        			if (x == 0 && y == 0) {
        				maximumExpectedDate 		= 0;
        				maximumStandardDeviation 	= 0;
        			} else {
        				
        				ArrayList<Properties> incomingActivities = getDependentActivitiesOfEvent((String) verticesArray.get(x).get(y).get("id"));
        				
        				for (Properties incomingEdge: incomingActivities) {
        					Properties vertex = getVertexFromName((String) incomingEdge.get("from"));
        					
        					if ((float)vertex.get("expectedDate") + activities.get((Integer)incomingEdge.get("id") - 1).getExpectedTime() > maximumExpectedDate) {
        						maximumExpectedDate = (float)vertex.get("expectedDate") + activities.get((Integer)incomingEdge.get("id") - 1).getExpectedTime();
        					}
        					
        					float previousVertexSD = (float)vertex.get("standardDeviation");
        					float incomingActivitySD = activities.get((Integer)incomingEdge.get("id") - 1).getStandardDeviation();
        					
        					if (Math.sqrt((previousVertexSD * previousVertexSD) + (incomingActivitySD * incomingActivitySD)) > maximumStandardDeviation) {
        						maximumStandardDeviation = (float) Math.sqrt((previousVertexSD * previousVertexSD) + (incomingActivitySD * incomingActivitySD));
        					}
        				}
        			}
        			
        			verticesArray.get(x).get(y).put("expectedDate", maximumExpectedDate);
        			verticesArray.get(x).get(y).put("standardDeviation", maximumStandardDeviation);
        			
        			String eventNumber = "Event Number: " + (String) verticesArray.get(x).get(y).get("id") + "\n";
        			String targetDate = "Target Date: " + "\n";
        			String expectedDate = "Expected Date: " + String.format("%.02f", maximumExpectedDate) + "\n";
        			String standardDeviation = "Standard Deviation: " + String.format("%.02f", maximumStandardDeviation);
        			String vertexValue = eventNumber + targetDate + expectedDate + standardDeviation;
        			
        			v = graph.insertVertex(parent, (String) verticesArray.get(x).get(y).get("id"), vertexValue, x*280+50, y*280+50, 120, 120, "strokeColor=black;fillColor=yellow");
        			verticesArray.get(x).get(y).put("cell", v);
        		}
        	}
        	
        	for (Properties edg : edges) {
        		Properties from = getVertexFromName((String) edg.get("from"));
        		Properties to 	= getVertexFromName((String) edg.get("to"));	
        		
        		Activity activity = activities.get((Integer) edg.get("id") - 1);
        		String activityInfo = "";
        		
        		Object[] alreadyCreatedEdge = graph.getEdgesBetween((mxCell) from.get("cell"), (mxCell) to.get("cell"));
        		
        		for (int i = 0; i < Array.getLength(alreadyCreatedEdge); ++i) {
        			mxCell existingEdge = (mxCell) alreadyCreatedEdge[i];
        			activityInfo = (String) existingEdge.getValue();
        			activityInfo += "\n";
        			graph.removeCells(alreadyCreatedEdge);
        		}
        			
    			String label				= activity.getLabel() + "\n";
        		String expectedTime 		= "ET: " + String.format("%.02f", activity.getExpectedTime()) + "\n";
        		String standardDeviation 	= "SD: " + String.format("%.02f", activity.getStandardDeviation());
        		activityInfo += label + expectedTime + standardDeviation;
            		
        		graph.insertEdge(parent, null, activityInfo, (mxCell) from.get("cell") , (mxCell) to.get("cell"));
        	}

        } catch (Exception e) {}
		finally {
			graph.getModel().endUpdate();
		}
        
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		this.add(graphComponent);
		
		
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e)
			{
				if (e.isPopupTrigger())
				{
					showGraphPopupMenu(e, graphComponent);
				}
			}
		});
    }
    
    void showGraphPopupMenu(MouseEvent e, mxGraphComponent graphComponent) {
		Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),
				graphComponent);
		
		JPopupMenu pop = new JPopupMenu();
		JMenuItem mItem = new JMenuItem("Save Chart");
		pop.add(mItem);
		pop.show(graphComponent, pt.x, pt.y);
		
		mItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("png","PNG Images"));
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int status = chooser.showSaveDialog(null);
				if(status == JFileChooser.APPROVE_OPTION) {
					String path = chooser.getSelectedFile().getPath();
					BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, Color.WHITE, true, null);
					try {
						if (!path.endsWith("." + "png")) {
					      path += ".png";
					    }
						ImageIO.write(image, "PNG", new File(path));
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
			}
		});

		e.consume();
	}
    
    private void generateChart() {
    	for (Activity activity : activities) { // checks who depends on each activity and build vertices and edges
    		List<Integer> dependents = ase.getDependents(activity.getId());
    		
    		for (Integer dependent : dependents) {
    			Activity dependentActivity = ase.getActivity(dependent);
    			
    			// Grab edge. If edge does not exist, create one. Otherwise, use its "from" and put it into activity's "to"
    			Properties edge = getActivityEdgeFromID(dependent);
    			if (edge == null) {
    				fillEdgeInformation(dependentActivity.getId(), edge);
    				createVertex = true;
    			}
    			else { createVertex = false; }

    			// More than one coming
    			List<Integer> dependentActivitityDependencies = ase.getDependencies(dependentActivity.getId());
    			
    			for (Integer dependentActivitityDependency  : dependentActivitityDependencies) {
    				Properties fromActivityEdge = getActivityEdgeFromID(dependentActivitityDependency);
    				
    				if (fromActivityEdge.getProperty("to").equals("")) {
    					fromActivityEdge.put("to", "E" + currentEvent);
    				}
    				
    				String vertexName = (String) fromActivityEdge.get("from");
    				Integer fromDistance = (Integer) getVertexFromName(vertexName).get("distance");
    				
    				if (fromDistance > closestFromDistance) {
    					closestFromDistance = fromDistance;
    				}
				}
			}
    		if (createVertex) {
    			createVertex();
			}
		}
    	fillEdgesWithNoTo();
	}

    private void createVertex() {
    	 Properties vertex = new Properties();
         vertex.put("id", "E" + currentEvent);
         vertex.put("distance", closestFromDistance + 1);
         
         if (verticesArray.size() < closestFromDistance + 2) {
         	verticesArray.add(new ArrayList<Properties>());
         }
         
         verticesArray.get(closestFromDistance + 1).add(vertex);
          
         closestFromDistance = -1;
         currentEvent++;
         createVertex = false;
    }
    
    private void fillEdgeInformation(Integer id, Properties edge) {
    	edge = new Properties();
    	edge.put("id", id);
        edge.put("from", "E" + currentEvent);
        edge.put("to", "");
        //edge.put("activityInfo", "");
        edges.add(edge);
    }
    
    private void processIndependentActivities(List<Activity> independentActivities) {
    	Integer distance = 0;
    	
		for (Activity activity : independentActivities) {
			Properties edge = new Properties();
			fillEdgeInformation(activity.getId(), edge);
		 }
		 
		 Properties firstVertex = new Properties();
		 firstVertex.put("id", "E" + currentEvent);
		 firstVertex.put("distance", distance);
		 verticesArray.add(new ArrayList<Properties>());
		 verticesArray.get(0).add(firstVertex);
		 ++currentEvent;
    }
    
    private Properties getActivityEdgeFromID(Integer id) {
        for (Properties edge : edges) {
            if (edge.get("id").equals(id)) {
                return edge;
            }
        }
        return null;
    }
     
    private Properties getVertexFromName(String name) {
         
        for (ArrayList<Properties> row : verticesArray) {
            for (Properties vertex : row) {
                if (vertex.get("id").equals(name)) {
                    return vertex;
                }
            }
        }
         
        return null;
    }

    private ArrayList<Properties> getDependentActivitiesOfEvent(String vertexName) {
    	ArrayList<Properties> activities = new ArrayList<Properties>();
    	
    	for (Properties edge: edges) {
    		if (edge.get("to").equals(vertexName)) {
    			activities.add(edge);
    		}
    	}
    	
    	return activities;
    }
    
    private void fillEdgesWithNoTo() {
    	Integer closestFromDistance = -1;
    	
    	for (Properties edge : edges) {
    		if (edge.get("to").equals("")) {
    			
    			Properties fromVertex = getVertexFromName((String) edge.get("from"));
    			Integer fromVertexDistance = (Integer) fromVertex.get("distance");
    			
    			if (fromVertexDistance > closestFromDistance) {
    				closestFromDistance = fromVertexDistance;
    			}
    			
    			edge.put("to", "E" + currentEvent);
    		}
    	}
    	
    	Properties vertex = new Properties();
        vertex.put("id", "E" + currentEvent);
        vertex.put("distance", closestFromDistance + 1);
        verticesArray.add(new ArrayList<Properties>());
        verticesArray.get(closestFromDistance + 1).add(vertex);
    	
    }
}