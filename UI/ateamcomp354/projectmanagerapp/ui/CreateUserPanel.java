package ateamcomp354.projectmanagerapp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;



import org.jooq.ateamcomp354.projectmanagerapp.tables.pojos.Users;

import ateamcomp354.projectmanagerapp.services.ApplicationContext;
import ateamcomp354.projectmanagerapp.services.CreateUserService;
import ateamcomp354.projectmanagerapp.services.UserService;
import ateamcomp354.projectmanagerapp.ui.gen.CreateUserPanelGen;
import ateamcomp354.projectmanagerapp.ui.util.FrameSaver;



public class CreateUserPanel {
	
	private final ApplicationContext appCtx;
	
	private  CreateUserPanelGen createUserPanelGen;
	
	private SwapInterface swap;
	
	private Users newUser;
	
	public CreateUserPanel( ApplicationContext appCtx , SwapInterface swap) {
	
		this.appCtx = appCtx;
		this.swap = swap;
		
		createUserPanelGen = new CreateUserPanelGen();
		createUserPanelGen.getSaveButton().addActionListener(new saveActionListener());
		createUserPanelGen.getCancelButton().addActionListener(new cancelActionListener());
	}
	
	public JComponent getComponent() {
		return createUserPanelGen;
	}
	
	// Sets all the registration field to default
	public void resetComponents()
	{
		
		createUserPanelGen.getFirstNameTextField().setText("");
		createUserPanelGen.getLastNameTextField().setText("");
		createUserPanelGen.getUsernameTextField().setText("");
		createUserPanelGen.getPasswordField().setText("");
		createUserPanelGen.getManagerRoleComboBox().setSelectedItem("Yes");	
		createUserPanelGen.getErrorFirstNameLabel().setText("");
		createUserPanelGen.getErrorLastNameLabel().setText("");
		createUserPanelGen.getErrorUsernameLabel().setText("");
		createUserPanelGen.getErrorPasswordLabel().setText("");
	}
	
	// Verifies if the form has been correctly filled
	public boolean completedFormValidation()
	{
		int validFieldNumber = 4;
		int passwordSize = 4;
		
		if (createUserPanelGen.getFirstNameTextField().getText().equals(""))
		{
			createUserPanelGen.getErrorFirstNameLabel().setText("Please Enter A First Name");
			 validFieldNumber--;
		}
		else
			createUserPanelGen.getErrorFirstNameLabel().setText("");
		
			
		if (createUserPanelGen.getLastNameTextField().getText().equals(""))
		{
			createUserPanelGen.getErrorLastNameLabel().setText("Please Enter A Last Name");
			 validFieldNumber--;
		}
		else
			createUserPanelGen.getErrorLastNameLabel().setText("");
		
		if (createUserPanelGen.getUsernameTextField().getText().equals(""))
		{
			createUserPanelGen.getErrorUsernameLabel().setText("Please Enter A Username");
			validFieldNumber--;
		}
		else
			createUserPanelGen.getErrorUsernameLabel().setText("");
		
		// Verifies if the pass word is at least 4 characters long
		if (createUserPanelGen.getPasswordField().getText().length() < passwordSize )
		{
			createUserPanelGen.getErrorPasswordLabel().setText("Please Enter A Password Of At Least 4 characters");
			 validFieldNumber--;
		}
		else
			createUserPanelGen.getErrorPasswordLabel().setText("");
		
		
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
				
				newUser.setFirstName(createUserPanelGen.getFirstNameTextField().getText());
				newUser.setLastName(createUserPanelGen.getLastNameTextField().getText());  
				newUser.setUsername(createUserPanelGen.getUsernameTextField().getText());
				newUser.setPassword(new String(createUserPanelGen.getPasswordField().getText())); 
				String managerRole = createUserPanelGen.getManagerRoleComboBox().getSelectedItem().toString();
				cus.setNewMember(newUser);
				cus.ProjectMemberRole(managerRole);
			
				 boolean duplicateResult = cus.duplicateUsername();
				
				 if (duplicateResult == false)
				 {
					us.addUser(newUser);
					resetComponents();
					swap.frameSwitch();
				 }
				 else
				 {
					 createUserPanelGen.getErrorUsernameLabel().setText("Username Already Exists");
				 }
			}
		}
	}
	
}
