import android.test.ActivityInstrumentationTestCase2;

import junit.framework.Assert;

public class TestAddMood extends ActivityInstrumentationTestCase2 {
    public TestAddMood() {
        super(AddMoodActivity.class);
    }
    
    //TODO CHECK MAX REASON LENGTH.
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
    
    //TODO CHECK POSTMOOD FUNCTIONALITY.
    public void testPostMood(){
    
    }
}
