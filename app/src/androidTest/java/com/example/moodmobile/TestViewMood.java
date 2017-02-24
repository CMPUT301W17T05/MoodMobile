import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

public class TestViewMood extends ActivityInstrumentationTestCase2 {
    public TestViewMood() {
        super(ViewMoodActivity.class);
    }
    
    //TODO CHECK MAX REASON LENGTH.
    //TODO CHECK REASONLENGTHCHECKFUCTIONALITY.
    public void testReasonLengthCheck(){
    	Mood newMood = new Mood;
    	String testReason1 = "short reason"
    	String testReason2 = "this reason is way to long to be put in a mood withought throwing some sort of exception or crashing the app because it causes some sort of error in some sort of way. this is assuming that the reason size is approximatly 140 characters. if the reason limit is more than that then this test needs to be updated."
    	String reason;
    	
    	//testReason1
    	assertTrue(reasonLengthCheck(testReason1));
    	newMood.setReason = testReason1;
    	reason = newMood.getReason();
    	assertTrue(reason = testReason1);
    	assertTrue(reason.size() = testReason1.size());
    	
    	//testReason2
    	assertFalse(reasonLengthCheck(testReason2));
    	//TODO CHECK FOR EXCEPTION HANDLING
    	//checks for excpetion to be thrown and that mood is not updated.
    	try {
            newMood.setReason = testReason2;
            Assert.fail("Expect IllegalArgumentException");
        } catch(IllegalArgumentException e){
            reason = newMood.getReason();
    		assertFalse(reason = testReason2);
    		assertFalse(reason.size() = testReason2.size());
        }
    	
    }
    
    //TODO CHECK UPDATEMOOD FUNCTIONALITY
    public void testUpdateMood() {
    	//TODO CHECK MOOD CONSTRUCTOR FUNCTINALITY
    	Mood selectedMood = new Mood;
    	String selectedType = "Happy";
    	String selectedSituation = "Alone";
    	String selectedReason = "test reason 1";
    	//TODO CHECK IMAGE AND LOCATION OBJECT TYPES
    	selectedMood.setType = selectedType;
    	selectedMood.setSituation = selectedSituation;
    	selectedMood.setReason = selectedReason;
    	    	
    	Mood newMood = new Mood;
    	String newType = "Sad";
    	String newSituation = "Crowd";
    	String newReason = "test reason 2";
    	//TODO CHECK IMAGE AND LOCATION OBJECT TYPES
    	newMood.setType = newType;
    	newMood.setSituation = newSituation;
    	newMood.setReason = newReason;
    	
    	//TODO CHECK UPDATEMOOD FUNCTIONALITY
    	updateMood(selectedMood, newMood)
    	
    	assertTrue(selectedMood.getType() = newType);
    	assertFalse(selectedMood.getType() = selectedType);
    	assertTrue(selectedMood.getSituation() = newSituation);
    	assertFalse(selectedMood.getSituation() = selectedSituation);
    	assertTrue(selectedMood.getReason() = newReason);
    	assertFalse(selectedMood.getReason() = selectedReason);
    }
}
