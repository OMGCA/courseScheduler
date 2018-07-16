package courseScheduler;

class Course{
	String courseTitle;
	int courseLength;
	String courseLocation;
	String courseTutor;
	

	public Course(){
		this.courseTitle = ("");
		this.courseLength = 0;
		this.courseLocation = ("");
		this.courseTutor = ("");
	}
	
	/*Course Title Method Set*/
	public void setTitle(String title){
		this.courseTitle = title;
	}
	
	public String getTitle(){
		return this.courseTitle;
	}
	
	/*Course Length Method Set*/
	public void setLength(int length){
		this.courseLength = length;
	}
	
	public int getLength(){
		return this.courseLength;
	}
	
	/*Course Location Method Set*/
	public void setLocation(String location){
		this.courseLocation = location;
	}
	
	public String getLocation(){
		return this.courseLocation;
	}
	
	/*Course Tutor Method Set*/
	public void setTutor(String tutorName){
		this.courseTutor = tutorName;
	}
	
	public String getTutor(){
		return this.courseTutor;
	}
}
