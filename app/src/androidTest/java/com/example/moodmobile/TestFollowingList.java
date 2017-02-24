import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

public class TestFollowingList extends ActivityInstrumentationTestCase2 {
    public TestFollowingList() {
        super(FollowingListActivity.class);
    }
    
    //TODO CHECK HOW TO ACCESS FRIENDS LIST
    public void testAddANewFriend(){
    	//TODO CHECK HOW TO ADD TEST FRIEND
    	//need to check if friend already added or not
    }
    
    //TODO CHECK VIEWEDIT FUNCTIONALITY
    public void testViewEdit(){
    
    }
    
    //TODO CHECK FILTERRECENTWEEK FUNCTIONALITY
    public void testFilterRecentWeek(){
    	FollowingList following = new FollowingList;
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood;
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood;    	
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
    	following.addNewMood(newMood1);
    	following.addNewMood(newMood2);
    	
    	//filter and check
    	FollowingList filtered = filterRecentWeek();
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood = newMood1);
    	assertFalse(filteredMood = newMood2);
    }
    
    //TODO CHECK FILTERBYTYPE FUNCTIONALITY.
    public void testFilterByType(){
    	FollowingList following = new FollowingList;
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood;
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood;    	
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType = newType2;
    	newMood2.setSituation = newSituation2;
    	newMood2.setReason = newReason2;
    	
    	//add moods to list
    	following.addNewMood(newMood1);
    	following.addNewMood(newMood2);
    	
    	//filter and check
    	FollowingList filtered = filterByType("Happy");
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood = newMood1);
    	assertFalse(filteredMood = newMood2);
    }
    
    //TODO CHECK FILTERWITHWORD FUNCTIONALITY.
    public void testFilterWithWord(){
    	FollowingList following = new FollowingList;
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTIONALITY.
    	//setup moods
    	Mood newMood1 = new Mood;
    	String newType1 = "Happy";
    	String newSituation1 = "Alone";
    	String newReason1 = "test word: one";
    	newMood1.setType = newType1;
    	newMood1.setSituation = newSituation1;
    	newMood1.setReason = newReason1;
    	
    	Mood newMood2 = new Mood;    	
    	String newType2 = "Sad";
    	String newSituation2 = "Crowd";
    	String newReason2 = "test word: two";
		newMood2.setType = newType2;
    	newMood2.setSituation = newSituation2;
    	newMood2.setReason = newReason2;
    	
    	//add moods to list
    	following.addNewMood(newMood1);
    	following.addNewMood(newMood2);
    	
    	//filter and check
    	FollowingList filtered = filterWithWord("one");
    	assertTrue(filtered.size() = 1);
    	Mood filteredMood = filtered.index(0);
    	assertTrue(filteredMood = newMood1);
    	assertFalse(filteredMood = newMood2);
    	
    }
    
}
