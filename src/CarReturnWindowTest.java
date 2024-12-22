public class CarReturnWindowTest {

    public static void testWindowInitialization() {
        CarReturnWindow window = new CarReturnWindow();
        assert window != null : "Window should not be null";
        assert "Car Return".equals(window.getTitle()) : "Window title should be 'Car Return'";
        assert window.getWidth() == 400 : "Window width should be 400";
        assert window.getHeight() == 300 : "Window height should be 300";
        System.out.println("testWindowInitialization passed");
    }

    public static void main(String[] args) {
        testWindowInitialization();
    }
}

