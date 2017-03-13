/*import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

import java.util.Date;

public class TestMainPage extends ActivityInstrumentationTestCase2 {
    public TestMainPage() {
        super(MainPageActivity.class);
    }
    
    public void testAddMood(){
    	MoodList moods = new MoodList();
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup mood
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	//add mood to list
    	moods.addNewMood(newMood1);
    	
    	//check
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    }
    
    //TODO CHECK FILTERRECENTWEEK FUNCTIONALITY
    public void testFilterRecentWeek(){
    	MoodList moods = new MoodList();
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType = newType2;
    	newMood2.setSituation = newSituation2;
    	newMood2.setReason = newReason2;
    	
    	//set dates
    	Date newDate1 = new Date();
    	//sets current date back approx. 2 weeks.
    	Date newDate2 = new Date(System.currentTimeMillis() - 604800000 * 2);
    	newMood1.setDate(newDate1);
    	newMood2.setDate(newDate2);
    	    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//filter and check
    	MoodList filtered = filterRecentWeek();
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    }
    
    //TODO CHECK FILTERBYTYPE FUNCTIONALITY.
    public void testFilterByType(){
    	MoodList moods = new MoodList();
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType = newType2;
    	newMood2.setSituation = newSituation2;
    	newMood2.setReason = newReason2;
    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//filter and check
    	MoodList filtered = filterByType("Happy");
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    }
    
    //TODO CHECK FILTERWITHWORD FUNCTIONALITY.
    public void testFilterWithWord(){
    	MoodList moods = new MoodList();
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType = newType2;
    	newMood2.setSituation = newSituation2;
    	newMood2.setReason = newReason2;
    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//filter and check
    	MoodList filtered = filterWithWord("one");
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    }
    
     //TODO CHECK VIEWEDITMOOD FUNCTIONALITY.
    public void testViewEditMood(){
    
    }
    
    public void testDeleteMood(){
    	MoodList moods = new MoodList();
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType = newType2;
    	newMood2.setSituation = newSituation2;
    	newMood2.setReason = newReason2;
    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//TODO CHECK DELETEMOOD FUNCTIONALITY.
    	//delete
    	deleteMood(newMood2);
    	
    	//TODO CHANGE VARIABLE NAMES.
    	Mood filteredMood = moods.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    }
    
    //TODO CHECK EDITPROFILE FUNCTIONALITY.
    public void testEditProfile(){
    
    }
    
}
*/