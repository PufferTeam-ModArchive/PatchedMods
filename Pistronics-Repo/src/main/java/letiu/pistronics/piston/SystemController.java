package letiu.pistronics.piston;

public class SystemController {
	private MoveData moveData;

	private String key;

	public SystemController(MoveData moveData) {
		this.moveData = moveData;
	}

	public boolean update(ControllerData data) {
		boolean result = false;
		if (data.lastValue == this.moveData.getValue()) {
			this.moveData.update();
			result = true;
		}
		data.lastValue = this.moveData.getValue();
		if (this.moveData.isDone())
			ControllerRegistry.remove(this.key);
		return result;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public MoveData getMoveData() {
		return this.moveData;
	}
}
