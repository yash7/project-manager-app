package ateamcomp354.projectmanagerapp.ui.util;

/**
 * @author Geodner
 * 
 *	Container to store the application's frames and their
 *  databases primary key ID
 *
 */
public class FrameSaver {

	private int firstID;
	private int secondID;
	private int thirdID;
	private int fourthID;
	private String windowName;

	public String getFrameName() {
		return windowName;
	}

	private int size;

	public FrameSaver ()
	{
		setSize(0);
	}

	public void setFrameName(String currentWindow)
	{
		this.windowName = currentWindow;
	}

	public void setID(int firstID)
	{
		setFirstID(firstID);
		setSize(1);
	}

	public void setID(int first,int secondID)
	{
		setFirstID(firstID);
		setSecondID(secondID);
		setSize(2);

	}

	public void setID(int firstID,int secondID,int thirdID)
	{
		setFirstID(firstID);
		setSecondID(secondID);
		setThirdID(thirdID);
		setSize(3);
	}

	public void setID(int firstID,int secondID,int thirdID,int fourthID)
	{
		setFirstID(firstID);
		setSecondID(secondID);
		setThirdID(thirdID);
		setFourthID(fourthID);
		setSize(4);
	}

	public void increaseSize()
	{
		size++;
	}

	public void decreaseSize()
	{
		size--;
	}

	public int getFirstID() {
		return firstID;
	}

	public void setFirstID(int fisrtID) {
		this.firstID = fisrtID;
	}

	public int getSecondID() {
		return secondID;
	}

	public void setSecondID(int secondID) {
		this.secondID = secondID;
	}

	public int getThirdID() {
		return thirdID;
	}

	public void setThirdID(int thirdID) {
		this.thirdID = thirdID;
	}

	public int getFourthID() {
		return fourthID;
	}

	public void setFourthID(int fourthID) {
		this.fourthID = fourthID;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
