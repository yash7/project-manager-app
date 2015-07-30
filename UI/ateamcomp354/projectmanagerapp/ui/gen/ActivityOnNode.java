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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import ateamcomp354.projectmanagerapp.services.ActivityService;

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
}