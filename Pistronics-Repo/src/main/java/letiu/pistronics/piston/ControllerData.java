package letiu.pistronics.piston;

public class ControllerData {
    public SystemController controller;

    public float lastValue;

    public String key;

    public void updateController() {
        if (this.controller != null)
            this.controller.update(this);
    }
}