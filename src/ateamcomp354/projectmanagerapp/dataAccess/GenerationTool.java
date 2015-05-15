package ateamcomp354.projectmanagerapp.dataAccess;

public class GenerationTool {

	public static void main(String[] args) throws Exception {
		
		 // first, create/update the database schema
		new DatabaseManager();
		
		// use jooq to generate sql code for us!
		org.jooq.util.GenerationTool.main(new String[] { "jooq-code.xml" });
	}
}
