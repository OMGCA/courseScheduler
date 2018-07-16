package courseScheduler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;  
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;  

public class CourseSchedule extends JFrame implements ActionListener{
	/*Day assignment*/
	String[] dayOfWeek = {"Time","Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	JLabel dayLabel[] = new JLabel[8];
	
	/*Time Zone*/
	String[] timeOfDay;
	JLabel dayTime[] = new JLabel[13];
	
	/*Single Course Block*/
	JButton courseBlock[] = new JButton[108];
	
	JButton saveBut = new JButton("Save");
	SaveButton saveButton = new SaveButton();
	
	JPanel mainFrame = new JPanel();
	JPanel weekDay = new JPanel();
	JPanel timeZone = new JPanel();
	
	Course[] newCourse = new Course[108];
	
	JTextField[] inTitle = new JTextField[108];
	JTextField[] inLen = new JTextField[108];
	JTextField[] inLoca = new JTextField[108];
	JTextField[] inTutor = new JTextField[108];
	
	int index = 0;
	
	String csvFile = "database.csv";
	String line = "";
	String cvsSplitby = ",";
	
	Font font = new Font("Microsoft YaHei", Font.PLAIN,14);
	
	BufferedReader br = null;
	FileWriter writer = null;
	
	public CourseSchedule(){
		
		saveBut.addActionListener(saveButton);
		saveBut.setFont(font);
		
		mainFrame.setLayout(new GridLayout(12,7));
		
		add(mainFrame, BorderLayout.CENTER);
		for ( int i = 0; i < 84; i++ )
		{
			courseBlock[i] = new JButton(" ");
			courseBlock[i].addActionListener(this);		
			mainFrame.add(courseBlock[i]);
		}
		
		weekDay.setLayout(new GridLayout(1,7));
		add(weekDay, BorderLayout.NORTH);
		for ( int i = 0; i < 8; i++ ){
			dayLabel[i] = new JLabel("",JLabel.CENTER);
			dayLabel[i].setFont(font);
			dayLabel[i].setText(dayOfWeek[i]);
			weekDay.add(dayLabel[i]);
		}
		
		
		timeZone.setLayout(new GridLayout(13,1));
		add(timeZone, BorderLayout.WEST);
		for ( int i = 0; i < 13; i++ ){
			int j = i + 8;
			dayTime[i] = new JLabel("",JLabel.LEFT);
			dayTime[i].setFont(font);
			dayTime[i].setText("   "+ j + ":00   ");
			timeZone.add(dayTime[i]);
		}
		
		/*Course object & Text field initialization*/
		for ( int i = 0; i < 84; i++ ){
			newCourse[i] = new Course();
			inTitle[i] = new JTextField(newCourse[i].getTitle());
			inTitle[i].setFont(font);
			inLen[i] = new JTextField(String.valueOf(newCourse[i].getLength()));
			inLen[i].setFont(font);
			inLoca[i] = new JTextField(newCourse[i].getLocation());
			inLoca[i].setFont(font);
			inTutor[i] = new JTextField(newCourse[i].getTutor());
			inTutor[i].setFont(font);
		}
		
		/*CSV file reading block*/
		/*CSV file, known as comma-separated values, can be edited*/
		/*Both in notepad and excel*/
		/*In plain text it has comma separater*/
		/*While in excel, words are separated in cells*/
		try{
			br = new BufferedReader(new FileReader(csvFile));
			while((line = br.readLine()) != null){ //Reading the file line by line
				String[] courseInfo = line.split(cvsSplitby);//Separate values by comma
				
				int index = blockComp(courseInfo[3],courseInfo[2]);//Get info of time and day, to allocate the info to certain block
				newCourse[index].setTitle(courseInfo[0]);
				newCourse[index].setLength(Integer.parseInt(courseInfo[1]));
				newCourse[index].setLocation(courseInfo[4]);
				newCourse[index].setTutor(courseInfo[5]);
				
				courseBlockUpdate(index);//Update course info
					
			}
		} catch (FileNotFoundException e){//When not file found
			e.printStackTrace();
		} catch (IOException e){//IO exception
			e.printStackTrace();
		} finally {
			if ( br != null ){
				try{
					br.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		/*Block ends here*/
		
	}
	
	class SaveButton implements ActionListener{
		public void actionPerformed(ActionEvent e){	
			if(e.getSource() == saveBut){
				/*Writing new course info in the program*/
				newCourse[index].setTitle(inTitle[index].getText());
				inTitle[index].setText(newCourse[index].getTitle());
				
				newCourse[index].setLength(Integer.parseInt(inLen[index].getText()));
				inLen[index].setText(String.valueOf(newCourse[index].getLength()));
				
				newCourse[index].setLocation(inLoca[index].getText());
				inLoca[index].setText(newCourse[index].getLocation());
				
				newCourse[index].setTutor(inTutor[index].getText());
				inTutor[index].setText(newCourse[index].getTutor());
				
				courseBlockUpdate(index);
				
				/*Update database file*/
				/*Retrieve information from the updated course object*/
				try{
					
					writer = new FileWriter(csvFile, true);
					String[] blockIdentifier = indexDecode(index);//Get time and day info from the button
					
					/*Add a title in the file*/
					writer.append(newCourse[index].getTitle());
					writer.append(',');
						
					/*Course length*/
					writer.append(String.valueOf(newCourse[index].getLength()));
					writer.append(',');
					
					/*Course day*/
					writer.append(blockIdentifier[1]);
					writer.append(',');
					
					/*Course time*/
					writer.append(blockIdentifier[0]);
					writer.append(',');

					/*Course location*/
					writer.append(newCourse[index].getLocation());
					writer.append(',');
					
					/*Course tutor name*/
					writer.append(newCourse[index].getTutor());
					writer.append(',');
					writer.append("\r\n");
					
					writer.flush();
					writer.close();
					
					
				} catch (FileNotFoundException exc){
					exc.printStackTrace();
				} catch (IOException exc){
					exc.printStackTrace();
				}
				
			}
		}
	}
	
	public int blockComp(String newTime, String newDay){
		int counter = 0;
		int indexBlock = (Integer.parseInt(newTime) / 100 - 8) * 7;
		while(newDay.equals(dayOfWeek[counter]) == false && counter < 7){
			counter++;
		}
		counter--;
		indexBlock+=counter;
		
		return indexBlock;
	}

	public String[] indexDecode(int index){
		String[] dayAndTime = new String[2];
		int counter = 0;
		
		while(index % 7 != 0){
			counter++;
			index--;
		}
		dayAndTime[0] = String.valueOf( (index/7 + 8) * 100 );
		dayAndTime[1] = dayOfWeek[counter+1];
		return dayAndTime;
	}
	
	/*This method update the button and the text field in regard*/
	public void courseBlockUpdate(int index){ 
		courseBlock[index].setFont(font);
		courseBlock[index].setText(newCourse[index].getTitle());
		inTitle[index].setText(newCourse[index].getTitle());
		inLen[index].setText(String.valueOf(newCourse[index].getLength()));
		inLoca[index].setText(newCourse[index].getLocation());
		inTutor[index].setText(newCourse[index].getTutor());
	}
	
	public void actionPerformed(ActionEvent e){

		index = 0;
		while(courseBlock[index] != e.getSource()){
			index++;
		}
		
		/*New pop up window to input course information*/
		JFrame secondFrame = new JFrame();
		secondFrame.setBounds(100,100,500,300);
		secondFrame.setTitle("Course Registration");
		secondFrame.setLocationRelativeTo(null);
		secondFrame.setVisible(true);
		
		/*Pop up window components*/
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(5,2));
		secondFrame.add(mainPanel);
		
		JLabel newTitle = new JLabel("Course Title: ", JLabel.CENTER);
		newTitle.setFont(font);
		JLabel newLength = new JLabel("Course Length: ", JLabel.CENTER);
		newLength.setFont(font);
		JLabel newLocation = new JLabel("Course Location: ", JLabel.CENTER);
		newLocation.setFont(font);
		JLabel newTutor = new JLabel("Tutor Name: ", JLabel.CENTER);
		newTutor.setFont(font);
			
		
		/*Adding components in the pop up window*/
		mainPanel.add(newTitle);
		mainPanel.add(inTitle[index]);
		mainPanel.add(newLength);
		mainPanel.add(inLen[index]);
		mainPanel.add(newLocation);
		mainPanel.add(inLoca[index]);
		mainPanel.add(newTutor);
		mainPanel.add(inTutor[index]);
		
		/*Register save button*/
		mainPanel.add(saveBut);
		
	
	}
}
