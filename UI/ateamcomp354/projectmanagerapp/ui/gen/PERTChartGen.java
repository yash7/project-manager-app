package ateamcomp354.projectmanagerapp.ui.gen;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.swing.JPanel;
import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
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
	    edge.put(mxConstants.STYLE_EDGE, "elbowEdgeStyle");
	    edge.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
	    edge.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
	    edge.put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);
	    edge.put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
	    edge.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // default is #6482B9
	    edge.put(mxConstants.STYLE_FONTCOLOR, "#446299");

	    mxStylesheet edgeStyle = new mxStylesheet();
	    edgeStyle.setDefaultEdgeStyle(edge);
	    graph.setStylesheet(edgeStyle);
		
        processIndependentActivities(independentActivities);
        generateChart();
        
        graph.getModel().beginUpdate();
        
        try {
        	for (int x = 0; x < verticesArray.size(); ++x) {
        		for (int y = 0; y < verticesArray.get(x).size(); ++y) {
        			Object v = null;
        			
        			v = graph.insertVertex(parent, (String) verticesArray.get(x).get(y).get("id"), (String) verticesArray.get(x).get(y).get("id"), x*280+25, y*280+25, 218, 60, "strokeColor=black;fillColor=yellow");
        			verticesArray.get(x).get(y).put("cell", v);
        		}
        	}
        	
        	for (Properties edg : edges) {
        		
        		
        		mxCell from = (mxCell) getVertexFromName((String) edg.get("from")).get("cell");
        		mxCell to = (mxCell) getVertexFromName((String) edg.get("to")).get("cell");	
        		
        		graph.insertEdge(parent, null, "", from , to);
        	}

        } catch (Exception e) {}
		finally {
			graph.getModel().endUpdate();
		}
        
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		this.add(graphComponent);
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