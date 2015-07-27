package ateamcomp354.projectmanagerapp.ui.gen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Activity;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import ateamcomp354.projectmanagerapp.model.Status;
import ateamcomp354.projectmanagerapp.services.ActivityService;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JInternalFrame;

public class ActivityOnNode extends JPanel {

	private static final long serialVersionUID = 1L;
	private ActivityService ase = null;
	
	private ArrayList<ArrayList<Activity>> goDepth(ArrayList<ArrayList<Activity>> activityDepths, ArrayList<ArrayList<String>> activityChains, int depth, Activity a) {	
		List<Integer> actsIds= ase.getDependencies(a.getId());
		
		if(actsIds.size() > 0) {
			if(activityDepths.size() <= depth) {
				activityDepths.add(new ArrayList<Activity>());
				activityChains.add(new ArrayList<String>());
			}
		}
		
		for(Integer aId : actsIds) {		
			boolean foundEle = false;
			boolean foundChain = false;
			
			for(ArrayList<String> al : activityChains) {
				for(String ala : al) {
					String[] alal = ala.split(",");
					if(aId.toString().equals(alal[0]) && a.getId().toString().equals(alal[1])) {
						foundChain = true;
					}
				}
			}
			
			for(ArrayList<Activity> al : activityDepths) {
				for(Activity ala : al) {
					if(aId == ala.getId()) {
						foundEle = true;
					}
				}
			}
			
			if(!foundChain) {
				activityChains.get(depth).add(aId+","+a.getId());
			}
			if(!foundEle) {
				Activity newAct = ase.getActivity(aId);
				activityDepths.get(depth).add(newAct);
			}
		}
		for(Integer aId : actsIds) {
			goDepth(activityDepths, activityChains, depth+1, ase.getActivity(aId));
		}
		return activityDepths;
	}
	
	public ActivityOnNode(ActivityService ase, String title, Activity lastActivity) {
		this.ase = ase;
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy/MM/dd");
		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();
		
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
		
		ArrayList<ArrayList<Activity>> acts = new ArrayList<ArrayList<Activity>>();
		ArrayList<ArrayList<String>> actsChains = new ArrayList<ArrayList<String>>();
		acts.add(new ArrayList<Activity>());
		acts.get(0).add(lastActivity);
		actsChains.add(new ArrayList<String>());
		actsChains.get(0).add("");
		
		acts = goDepth(acts, actsChains, 1, lastActivity);
		Map<String, Object> map = new HashMap<String, Object>();
		
		graph.getModel().beginUpdate();
		try {
			
			
			int xPos = 0;
			for(int x = acts.size()-1; x >= 0; x--) {
				int yPos = 0;
				for(int y = 0; y < acts.get(x).size(); y++) {
					Activity a = acts.get(x).get(y);
					String label = "";
					
					if(a.getLabel().length() > 20) {
						label = String.format("%-20s", "Label: "+a.getLabel().substring(0, 17)+"...");
					}
					else {
						label = String.format("%-20s", "Label: "+a.getLabel());
					}
					
					String first = label+" "+String.format("%30s", "Dur: "+a.getDuration())+"\n";
					String second = String.format("%-25s", "ES: "+formatter2.format(formatter.parse(a.getEarliestStart().toString())))+" "+String.format("%25s", "EF: "+formatter2.format(formatter.parse(a.getEarliestFinish().toString())))+"\n";
					String third = String.format("%-25s", "LS: "+formatter2.format(formatter.parse(a.getLatestStart().toString())))+" "+String.format("%25s", "LF: "+formatter2.format(formatter.parse(a.getLatestFinish().toString())))+"\n";
					String fourth = String.format("%-20s", "Max Dur: "+a.getMaxDuration())+" "+String.format("%39s", "Float: "+a.getFloat());
					Object v1 = null;
					
					if(a.getFloat() == 0) {
						v1 = graph.insertVertex(parent, a.getId().toString(), first+second+third+fourth, xPos*280+25, yPos*100+25, 218,	60, "strokeColor=black;fillColor=yellow");
					}
					else {
						v1 = graph.insertVertex(parent, a.getId().toString(), first+second+third+fourth, xPos*280+25, yPos*100+25, 218,	60, "strokeColor=black;fillColor=white");
					}

					map.put(a.getId().toString(), v1);						
					yPos++;
				}
				xPos++;
			}
			
			for(int x = 0; x < actsChains.size(); x++) {
				for(int y = 0; y < actsChains.get(x).size(); y++) {
					String link = actsChains.get(x).get(y);
					
					if(!link.equals("")) {
						String[] ids = link.split(",");
						
						mxCell from = (mxCell) map.get(ids[0]);
						mxCell to = (mxCell) map.get(ids[1]);
						
						Object e1 = graph.insertEdge(parent, null, "", from, to);
					}
				}
			}
		} catch (Exception e) {}
		finally {
			graph.getModel().endUpdate();
		}
	
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.setEnabled(false);
		this.add(graphComponent);
		
//		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, Color.WHITE, true, null);
//		try {
//			ImageIO.write(image, "PNG", new File("graph.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
    }
}
