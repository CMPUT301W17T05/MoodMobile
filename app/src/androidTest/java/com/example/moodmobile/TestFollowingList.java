import android.test.ActivityInstrumentationTestCase2;

import java.util.Date;
import java.util.ArrayList;

import junit.framework.Assert;

public class TestFollowingList extends ActivityInstrumentationTestCase2 {
    public TestFollowingList() {
        super(MainPageActivity.class);
    }
    
    public void testAddNewMood(){
    	MoodList moods = new MoodList;
    	//setup mood
    	Mood newMood1 = new Mood;
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType(newType1);
    	newMood1.setSituation(newSituation1);
    	newMood1.setReason(newReason1);
    	
    	//add mood to list
    	moods.addNewMood(newMood1);
    	
    	//check
    	Mood filteredMood = moods.index(0);
    	assertTrue(filteredMood == newMood1);
    }
    
    public void testFilterRecentWeek(){
    	MoodList moods = new MoodList();
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType(newType1);
    	newMood1.setSituation(newSituation1);
    	newMood1.setReason(newReason1);
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType(newType2);
    	newMood2.setSituation(newSituation2);
    	newMood2.setReason(newReason2);
    	
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
    	//return 1
    	MoodList filtered = moods.filterRecentWeek();
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    	//filter and check
    	//return 2
    	moods.clear();
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood1);
    	
    	MoodList filtered = moods.filterRecentWeek();
    	assertTrue(filtered.size() = 2);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood = newMood1);
    	assertFalse(filteredMood = newMood2);
    	Mood filteredMood = filtered.index(1);
    	assertTrue(filteredMood = newMood2);
    	assertFalse(filteredMood = newMood1);
    	
    	//filer and check
    	//return 0
    	moods.clear();
    	moods.addNewMood(newMood2);
    	moods.addNewMood(newMood2);
    	
    	MoodList filtered = moods.filterRecentWeek();
    	assertTrue(filtered.size() = 0);    	
    }
    
    public void testFilterByType(){
    	MoodList moods = new MoodList();
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType(newType1);
    	newMood1.setSituation(newSituation1);
    	newMood1.setReason(newReason1);
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType(newType2);
    	newMood2.setSituation(newSituation2);
    	newMood2.setReason(newReason2);
    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//filter and check
    	//return 1
    	MoodList filtered = moods.filterByType("Happy");
    	assertTrue(filtered.size() == 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    	//filter and check
    	//return 1
    	filtered = moods.filterByType("Sad");
    	assertTrue(filtered.size() = 1);
    	filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    	
    	//filter and check
    	//return 0
		filtered = moods.filterByType("Angry");
    	assertTrue(filtered.size() == 0);
    	
    	//filter and check
    	//return 2
    	moods.index(1).setType(newTpe1);
		filtered = moods.filterByType("Happy");
    	assertTrue(filtered.size() == 2);
    	
    	filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    	filteredMood = filtered.index(1);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    	
    	
    	//filter and check
    	//return 2
    	moods.index(0).setType(newTpe2);
		filtered = moods.filterByType("Sad");
    	assertTrue(filtered.size() == 2);
    	
    	filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    	filteredMood = filtered.index(1);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    }
    
    public void testFilterWithWord(){
    	MoodList moods = new MoodList();
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType(newType1);
    	newMood1.setSituation(newSituation1);
    	newMood1.setReason(newReason1);
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType(newType2);
    	newMood2.setSituation(newSituation2);
    	newMood2.setReason(newReason2);
    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//filter and check
    	//return 1
    	MoodList filtered = moods.filterWithWord("one");
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    	//filer and check
    	//return 1
    	filtered = moods.filterWithWord("two");
    	assertTrue(filtered.size() = 1);
    	filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    	
    	//filter and check
    	//return 2
    	filtered = moods.filterWithWord("test");
    	assertTrue(filtered.size() = 2);
    	filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	filteredMood = filtered.index(1);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    	
    	//filter and check
    	//return 2
    	filtered = moods.filterWithWord("test word:");
    	assertTrue(filtered.size() = 2);
    	filteredMood = filtered.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	filteredMood = filtered.index(1);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    	
    	//filter and check
    	//return 0
    	filtered = moods.filterWithWord("false");
    	assertTrue(filtered.size() == 0);
    	
    }
    
    public void testDeleteMood(){
    	MoodList moods = new MoodList();
    	//setup moods
    	Mood newMood1 = new Mood();
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType(newType1);
    	newMood1.setSituation(newSituation1);
    	newMood1.setReason(newReason1);
    	
    	Mood newMood2 = new Mood();
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType(newType2);
    	newMood2.setSituation(newSituation2);
    	newMood2.setReason(newReason2);
    	
    	//add moods to list
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	//delete
    	//return 1
    	moods.deleteMood(newMood2);
    	
    	assertTrue(moods.size() = 1);
    	Mood filteredMood = moods.index(0);
    	assertTrue(filteredMood == newMood1);
    	assertFalse(filteredMood == newMood2);
    	
    	//delete
    	//return 1
    	moods.clear();
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood2);
    	
    	moods.deleteMood(newMood1);
    	
    	assertTrue(moods.size() = 1);
    	filteredMood = moods.index(0);
    	assertTrue(filteredMood == newMood2);
    	assertFalse(filteredMood == newMood1);
    	
    	//delete
    	//return 0
    	moods.clear();
    	
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood1);
    	
    	moods.deleteMood(newMood1);
    	assertTrue(moods.size() == 0);
    	
    	//delete
    	//return 2
    	moods.clear();
    	
    	moods.addNewMood(newMood1);
    	moods.addNewMood(newMood1);
    	
    	moods.deleteMood(newMood2);
    	assertTrue(moods.size() == 2);
    }
}