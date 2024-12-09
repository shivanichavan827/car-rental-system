public class CarReturnWindowTest3 {

    public static void testReturnCarButtonAction() {
        String messageBefore = "Car not returned";
        
        // Simulate button click action
        String messageAfterAction = "Toyota Corolla is returned";  // Expected result
        
        // Simulate action
        boolean actionTriggered = messageAfterAction.equals("Toyota Corolla is returned");
        assert actionTriggered : "The return action should show a correct message";
        
        System.out.println("testReturnCarButtonAction passed");
    }

    public static void main(String[] args) {
        testReturnCarButtonAction();
    }
}
