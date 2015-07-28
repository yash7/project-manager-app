package ateamcomp354.projectmanagerapp.ui.gen;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.GridLayout;

import javax.swing.SwingConstants;

import java.awt.geom.Dimension2D;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.BoxLayout;

import java.awt.Component;

import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JScrollPane;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import ateamcomp354.projectmanagerapp.ui.MainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.freixas.jcalendar.JCalendarCombo;

public class SplitPane1Gen extends JPanel {
	private JLabel topLabel;
	private JScrollPane listScrollPane;
	private JButton addButton;
	private JButton deleteButton;
	private JButton btnEVanalysis;
	private JLabel activityNameLabel;

	private int appWidth = MainFrame.getAppWidth();
	private int appHeight = MainFrame.getAppHeight();
	
	private JLabel statusLabel;
	private JComboBox statusComboBox;
	private JLabel earliestStartLabel;
	private JLabel latestStartLabel;
	private JLabel earliestFinishLabel;
	private JLabel latestFinishLabel;
	private JLabel maxDurationLabel;
	private JLabel durationLabel;
	private JTextField earliestStartField;
	private JTextField latestStartField;
	private JTextField earliestFinishField;
	private JTextField latestFinishField;
	private JTextField maxDurationField;
	private JTextField durationField;
	private JScrollPane descriptionScrollPane;
	private JTextArea descriptionArea;
	private JLabel descriptionLabel;
	private JLabel dependenciesLabel;
	private JComboBox dependenciesComboBox;
	private JLabel assigneesLabel;
	private JComboBox assigneesComboBox;
	private JButton saveActivityButton;
	private JSplitPane splitPane;
	private JTextField textField;
	private JScrollPane dependencyScrollPane;
	private JButton addDepButton;
	private JButton delDepButton;
	private JScrollPane scrollPane;
	private JButton btnView;
	private JScrollPane assigneeScrollPane;
	private JButton delAssignButton;
	private JButton addAssignButton;
	private JButton backBtn;
	private JButton btnChart;
	private JTextField plannedValueField;
	private JCalendarCombo earliestStartDatePicker;
	private JCalendarCombo latestStartDatePicker;
	private JCalendarCombo earliestFinishDatePicker;
	private JCalendarCombo latestFinishDatePicker;
	
	/**
	 * Create the panel.
	 */
	public SplitPane1Gen() {
		
		setSize(appWidth, appHeight);
		setLayout(null);
		
		splitPane = new JSplitPane();
		splitPane.setBounds(0, 0, appWidth, appHeight);
		splitPane.setDividerLocation(2*(appWidth/5));
		add(splitPane);
		splitPane.setEnabled(false);
		
		JPanel LeftPanel = new JPanel();
		
		JPanel RightPanel = new JPanel();
		

		splitPane.setLeftComponent(LeftPanel);
		LeftPanel.setLayout(null);
		
		topLabel = new JLabel("TopLabel");
		topLabel.setBackground(Color.BLACK);
		topLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		topLabel.setHorizontalAlignment(SwingConstants.LEFT);
		topLabel.setBounds(43, 94, 77, 25);
		LeftPanel.add(topLabel);
		
		listScrollPane = new JScrollPane();
		listScrollPane.setBounds(43, 132, 258, 197);
		LeftPanel.add(listScrollPane);
		
		addButton = new JButton("Add");
		addButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addButton.setBounds(231, 91, 70, 30);
		LeftPanel.add(addButton);
		
		deleteButton = new JButton("Delete");
		deleteButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		deleteButton.setBounds(149, 91, 70, 30);
		LeftPanel.add(deleteButton);
		
		btnEVanalysis = new JButton("Earned-Value");
		btnEVanalysis.setBounds(170, 365, 131, 34);
		LeftPanel.add(btnEVanalysis);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(43, 410, 258, 197);
		LeftPanel.add(scrollPane);
		
		btnView = new JButton("View Activity");
		btnView.setBounds(149, 642, 148, 30);
		LeftPanel.add(btnView);
		
		backBtn = new JButton("< Back");
		backBtn.setBounds(17, 36, 90, 30);
		LeftPanel.add(backBtn);
		
		btnChart = new JButton("GANTT Chart");
		btnChart.setBounds(43, 365, 120, 34);
		LeftPanel.add(btnChart);
		
		JLabel lblAnalysis = new JLabel("Analysis:");
		lblAnalysis.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAnalysis.setBounds(47, 340, 73, 26);
		LeftPanel.add(lblAnalysis);

		splitPane.setRightComponent(RightPanel);
		RightPanel.setLayout(null);
		
		activityNameLabel = new JLabel("Name:");
		activityNameLabel.setBounds(12, 51, 128, 16);
		RightPanel.add(activityNameLabel);
		
		statusLabel = new JLabel("Status:");
		statusLabel.setBounds(12, 79, 128, 16);
		RightPanel.add(statusLabel);
		
		statusComboBox = new JComboBox();
		statusComboBox.setBounds(147, 76, 116, 25);
		RightPanel.add(statusComboBox);
		
		earliestStartLabel = new JLabel("Start Date:");
		earliestStartLabel.setBounds(12, 134, 128, 16);
		RightPanel.add(earliestStartLabel);
		
		latestStartLabel = new JLabel("Latest Start:");
		latestStartLabel.setBounds(273, 316, 128, 16);
		latestStartLabel.setVisible(false);
		RightPanel.add(latestStartLabel);
		
		earliestFinishLabel = new JLabel("Earliest Finish:");
		earliestFinishLabel.setBounds(273, 345, 128, 16);
		earliestFinishLabel.setVisible(false);
		RightPanel.add(earliestFinishLabel);
		
		latestFinishLabel = new JLabel("End Date:");
		latestFinishLabel.setBounds(11, 166, 128, 16);
		RightPanel.add(latestFinishLabel);
		
		maxDurationLabel = new JLabel("Max Duration:");
		maxDurationLabel.setBounds(273, 372, 128, 16);
		maxDurationLabel.setVisible(false);
		RightPanel.add(maxDurationLabel);
		
		durationLabel = new JLabel("Duration:");
		durationLabel.setBounds(273, 401, 128, 16);
		durationLabel.setVisible(false);
		RightPanel.add(durationLabel);
				
		maxDurationField = new JTextField();
		maxDurationField.setColumns(10);
		maxDurationField.setBounds(408, 372, 116, 22);
		maxDurationField.setVisible(false);
		RightPanel.add(maxDurationField);
		
		durationField = new JTextField();
		durationField.setColumns(10);
		durationField.setBounds(409, 398, 116, 22);
		durationField.setVisible(false);
		RightPanel.add(durationField);
		
		descriptionArea = new JTextArea();
		descriptionArea.setLineWrap(true);
		
		descriptionScrollPane = new JScrollPane(descriptionArea);
		descriptionScrollPane.setBounds(287, 80, 207, 178);
		RightPanel.add(descriptionScrollPane);
		
		descriptionLabel = new JLabel("Description:");
		descriptionLabel.setBounds(287, 51, 89, 16);
		RightPanel.add(descriptionLabel);
		
		dependenciesLabel = new JLabel("Dependencies:");
		dependenciesLabel.setBounds(12, 197, 89, 16);
		RightPanel.add(dependenciesLabel);
		
		dependenciesComboBox = new JComboBox();
		dependenciesComboBox.setBounds(12, 362, 116, 25);
		RightPanel.add(dependenciesComboBox);
		
		assigneesLabel = new JLabel("Assignees:");
		assigneesLabel.setBounds(147, 197, 89, 16);
		RightPanel.add(assigneesLabel);
		
		assigneesComboBox = new JComboBox();
		assigneesComboBox.setBounds(147, 362, 116, 25);
		RightPanel.add(assigneesComboBox);
		
		saveActivityButton = new JButton("Save Activity");
		saveActivityButton.setBounds(86, 486, 116, 30);
		RightPanel.add(saveActivityButton);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(147, 48, 116, 22);
		RightPanel.add(textField);
		
		addDepButton = new JButton("Add");
		addDepButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addDepButton.setBounds(31, 400, 70, 30);
		RightPanel.add(addDepButton);
		
		delDepButton = new JButton("Delete");
		delDepButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		delDepButton.setBounds(31, 438, 70, 30);
		RightPanel.add(delDepButton);
		
		addAssignButton = new JButton("Add");
		addAssignButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		addAssignButton.setBounds(166, 400, 70, 30);
		RightPanel.add(addAssignButton);
		
		delAssignButton = new JButton("Delete");
		delAssignButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		delAssignButton.setBounds(166, 438, 70, 30);
		RightPanel.add(delAssignButton);
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBounds(12, 226, 116, 129);
		RightPanel.add(dependencyScrollPane);
		
		assigneeScrollPane = new JScrollPane();
		assigneeScrollPane.setBounds(147, 226, 116, 129);
		RightPanel.add(assigneeScrollPane);
		
		JLabel lblPlannedValue = new JLabel("Planned Value ($):");
		lblPlannedValue.setBounds(12, 107, 128, 16);
		RightPanel.add(lblPlannedValue);
		
		plannedValueField = new JTextField();
		plannedValueField.setBounds(147, 107, 116, 22);
		RightPanel.add(plannedValueField);
		plannedValueField.setColumns(10);
		
		earliestStartDatePicker = new JCalendarCombo();
		earliestStartDatePicker.setBounds(147, 135, 117, 20);
		RightPanel.add(earliestStartDatePicker);
		
		latestStartDatePicker = new JCalendarCombo();
		latestStartDatePicker.setBounds(407, 317, 117, 20);
		latestStartDatePicker.setVisible(false);
		RightPanel.add(latestStartDatePicker);
		
		earliestFinishDatePicker = new JCalendarCombo();
		earliestFinishDatePicker.setBounds(408, 346, 117, 20);
		earliestFinishDatePicker.setVisible(false);
		RightPanel.add(earliestFinishDatePicker);
		
		latestFinishDatePicker = new JCalendarCombo();
		latestFinishDatePicker.setBounds(146, 166, 117, 20);
		RightPanel.add(latestFinishDatePicker);
		
		earliestStartField = new JTextField();
		earliestStartField.setBounds(147, 134, 116, 22);
		RightPanel.add(earliestStartField);
		earliestStartField.setColumns(10);
		earliestStartField.setVisible(false);
		
		latestStartField = new JTextField();
		latestStartField.setColumns(10);
		latestStartField.setBounds(411, 316, 116, 22);
		RightPanel.add(latestStartField);
		latestStartField.setVisible(false);
		
		earliestFinishField = new JTextField();
		earliestFinishField.setColumns(10);
		earliestFinishField.setBounds(411, 345, 116, 22);
		RightPanel.add(earliestFinishField);
		earliestFinishField.setVisible(false);
		
		latestFinishField = new JTextField();
		latestFinishField.setColumns(10);
		latestFinishField.setBounds(147, 163, 116, 22);
		RightPanel.add(latestFinishField);
		latestFinishField.setVisible(false);	
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		getEarliestStartDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
		getLatestStartDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
		getEarliestFinishDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
		getLatestFinishDatePicker().setDateFormat(new SimpleDateFormat("yyyy/MM/dd"));
	}

	public JLabel getTopLabel() {
		return topLabel;
	}
	public JScrollPane getListScrollPane() {
		return listScrollPane;
	}
	public JButton getAddButton() {
		return addButton;
	}
	public JButton getDeleteButton() {
		return deleteButton;
	}

	public JButton getEVanalysisButton(){
		return btnEVanalysis;
	}
	
	public JButton getChartButton(){
		return btnChart;
	}
	
	// RightPanel
	public JComboBox getStatusComboBox() {
		return statusComboBox;
	}
	public JTextField getEarliestStartField() {
		return earliestStartField;
	}
	public JTextField getLatestFinishField() {
		return latestFinishField;
	}
	public JTextField getDurationField() {
		return durationField;
	}
	public JTextField getEarliestFinishField() {
		return earliestFinishField;
	}
	public JTextField getLatestStartField() {
		return latestStartField;
	}
	public JTextField getMaxDurationField() {
		return maxDurationField;
	}
	public JTextArea getDescriptionArea() {
		return descriptionArea;
	}
	public JComboBox getDependenciesComboBox() {
		return dependenciesComboBox;
	}
	public JComboBox getAssigneesComboBox() {
		return assigneesComboBox;
	}
	public JButton getSaveActivityButton() {
		return saveActivityButton;
	}
	public JSplitPane getSplitPane() {
		return splitPane;
	}
	public JTextField getActivityNameField() {
		return textField;
	}
	public JScrollPane getDependencyScrollPane() {
		return dependencyScrollPane;
	}
	public JButton getAddDependencyButton() {
		return addDepButton;
	}
	public JButton getRemoveDependencyButton() {
		return delDepButton;
	}
	public JScrollPane getCompletedScrollPane() {
		return scrollPane;
	}
	public JButton getBtnView() {
		return btnView;
	}
	public JScrollPane getAssigneeScrollPane() {
		return assigneeScrollPane;
	}
	public JButton getRemoveAssigneeButton() {
		return delAssignButton;
	}
	public JButton getAddAssigneeButton() {
		return addAssignButton;
	}
	public JButton getBackBtn() {
		return backBtn;
	}
	public JTextField getPlannedValueField() {
		return plannedValueField;
	}
	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public JLabel getEarliestStartLabel() {
		return earliestStartLabel;
	}

	public JLabel getLatestStartLabel() {
		return latestStartLabel;
	}

	public JLabel getEarliestFinishLabel() {
		return earliestFinishLabel;
	}

	public JLabel getMaxDurationLabel() {
		return maxDurationLabel;
	}

	public JLabel getDurationLabel() {
		return durationLabel;
	}

	public JScrollPane getDescriptionScrollPane() {
		return descriptionScrollPane;
	}

	public JLabel getDescriptionLabel() {
		return descriptionLabel;
	}

	public JLabel getDependenciesLabel() {
		return dependenciesLabel;
	}

	public JTextField getTextField() {
		return textField;
	}

	public JButton getDelDepButton() {
		return delDepButton;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public JButton getDelAssignButton() {
		return delAssignButton;
	}
	
	public JButton getBtnEVanalysis() {
		return btnEVanalysis;
	}

	public JButton getBtnChart() {
		return btnChart;
	}
	public JLabel getAssigneesLabel() {
		return assigneesLabel;
	}

	public JCalendarCombo getEarliestStartDatePicker() {
		return earliestStartDatePicker;
	}

	public JCalendarCombo getLatestStartDatePicker() {
		return latestStartDatePicker;
	}

	public JCalendarCombo getEarliestFinishDatePicker() {
		return earliestFinishDatePicker;
	}

	public JCalendarCombo getLatestFinishDatePicker() {
		return latestFinishDatePicker;
	}
	
	public void resetDatePickers() {
		int yearInt = Calendar.getInstance().get(Calendar.YEAR);
		int monthInt = Calendar.getInstance().get(Calendar.MONTH);
		int dateInt = Calendar.getInstance().get(Calendar.DATE);
		
		String year = yearInt+"";
		String month = monthInt+1 < 10 ? "0"+(monthInt+1) : ""+(monthInt+1);
		String day = dateInt < 10 ? "0"+dateInt : ""+dateInt;
		
		earliestStartDatePicker.setDate(new Date());
		latestStartDatePicker.setDate(new Date());
		earliestFinishDatePicker.setDate(new Date());
		latestFinishDatePicker.setDate(new Date());
		
		earliestStartField.setText(year+month+day);
		latestStartField.setText(year+month+day);
		earliestFinishField.setText(year+month+day);
		latestFinishField.setText(year+month+day);
	}
}
