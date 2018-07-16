
package courseScheduler;

import javax.swing.JFrame;  
import javax.swing.UIManager;  

public class Main {
	public static void main(String args[]){
		try {  
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
        }catch (Exception e) {  
        	e.printStackTrace();  
        }  
		CourseSchedule courseSchedule = new CourseSchedule();
		courseSchedule.setBounds(100,100,1000,600);
		courseSchedule.setTitle("Course Schedule");
		courseSchedule.setLocationRelativeTo(null);
		courseSchedule.setVisible(true);
		courseSchedule.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
