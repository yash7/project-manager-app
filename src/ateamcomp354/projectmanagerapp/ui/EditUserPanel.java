package ateamcomp354.projectmanagerapp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;




import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.CreateUserService;
import ateamcomp354.projectmanagerapp.services.UserService;
import ateamcomp354.projectmanagerapp.ui.gen.CreateUserPanelGen;
import ateamcomp354.projectmanagerapp.ui.gen.EditUserPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;



public class EditUserPanel {
	
	private final ApplicationContext appCtx;
	
	private EditUserPanelGen editUserPanelGen;
	
	private SwapInterface swap;
	
	private Users newUser;
	
	private int userId;
	
	public EditUserPanel( ApplicationContext appCtx , SwapInterface swap) {
	
		this.appCtx = appCtx;
		this.swap = swap;
		
		editUserPanelGen = new EditUserPanelGen();
		editUserPanelGen.getSaveButton().addActionListener(new saveActionListener());
		editUserPanelGen.getCancelButton().addActionListener(new cancelActionListener());
	}
	
	public JComponent getComponent() {
		return editUserPanelGen;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	// Sets all the registration field to default
	public void resetComponents()
	{
		
		editUserPanelGen.getFirstNameTextField().setText("");
		editUserPanelGen.getLastNameTextField().setText("");
		editUserPanelGen.getUsernameTextField().setText("");
		editUserPanelGen.getPasswordField().setText("");
		editUserPanelGen.getManagerRoleComboBox().setSelectedItem("Yes");	
		editUserPanelGen.getErrorFirstNameLabel().setText("");
		editUserPanelGen.getErrorLastNameLabel().setText("");
		editUserPanelGen.getErrorUsernameLabel().setText("");
		editUserPanelGen.getErrorPasswordLabel().setText("");
	}
	
	// Verifies if the form has been correctly filled
	public boolean completedFormValidation()
	{
		int validFieldNumber = 4;
		int passwordSize = 4;
		
		if (editUserPanelGen.getFirstNameTextField().getText().equals(""))
		{
			editUserPanelGen.getErrorFirstNameLabel().setText("Please Enter A First Name");
			 validFieldNumber--;
		}
		else
			editUserPanelGen.getErrorFirstNameLabel().setText("");
		
			
		if (editUserPanelGen.getLastNameTextField().getText().equals(""))
		{
			editUserPanelGen.getErrorLastNameLabel().setText("Please Enter A Last Name");
			 validFieldNumber--;
		}
		else
			editUserPanelGen.getErrorLastNameLabel().setText("");
		
		if (editUserPanelGen.getUsernameTextField().getText().equals(""))
		{
			editUserPanelGen.getErrorUsernameLabel().setText("Please Enter A Username");
			validFieldNumber--;
		}
		else
			editUserPanelGen.getErrorUsernameLabel().setText("");
		
		// Verifies if the pass word is at least 4 characters long
		if (editUserPanelGen.getPasswordField().getPassword().length < passwordSize )
		{
			editUserPanelGen.getErrorPasswordLabel().setText("Please Enter A Password Of At Least 4 characters");
			 validFieldNumber--;
		}
		else
			editUserPanelGen.getErrorPasswordLabel().setText("");
		
		
		if (validFieldNumber < 4) // Fields have not been filled correctly
			return false;
		else 
			return true;
	}
	
	class cancelActionListener implements ActionListener{
	
		public void actionPerformed(ActionEvent e){
			
			resetComponents();			
			swap.frameSwitch(); //Returns the application to the previous frame
		}
	}
	
	class saveActionListener implements ActionListener{
	
		CreateUserService cus = appCtx.getCreateUserService();
		UserService us  = appCtx.getUserService();
		
		public void actionPerformed(ActionEvent e){

			if (completedFormValidation())
			{
				newUser = new Users();
				newUser.setId(userId);
				newUser.setFirstName(editUserPanelGen.getFirstNameTextField().getText());
				newUser.setLastName(editUserPanelGen.getLastNameTextField().getText());  
				newUser.setUsername(editUserPanelGen.getUsernameTextField().getText());
				newUser.setPassword(new String(editUserPanelGen.getPasswordField().getPassword())); 
				String managerRole = editUserPanelGen.getManagerRoleComboBox().getSelectedItem().toString();
				cus.setNewMember(newUser);
				cus.ProjectMemberRole(managerRole);
			
				 boolean duplicateResult = cus.duplicateUsername();
				
				 if (duplicateResult == false)
				 {
					us.updateUser(newUser);
					resetComponents();
					swap.frameSwitch();
				 }
				 else
				 {
					 editUserPanelGen.getErrorUsernameLabel().setText("Username Already Exists");
				 }
			}
		}
	}
	
}
