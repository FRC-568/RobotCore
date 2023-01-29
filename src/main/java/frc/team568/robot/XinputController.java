package frc.team568.robot;

import java.util.EnumMap;
import java.util.Map;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class XinputController extends XboxController {
	private final Map<Direction, Boolean> povPressed = new EnumMap<>(Direction.class);
	private final Map<Direction, Boolean> povReleased = new EnumMap<>(Direction.class);

	public static enum Direction {
		kUp(0),
		kRight(90),
		kDown(180),
		kLeft(270);

		public final int value;

		Direction(int value) {
			this.value = value;
		}
	}

	public XinputController(int port) {
		super(port);
	}

	public Trigger getButton(Button button) {
		return new JoystickButton(this, button.value);
	}

	public Trigger getButton(Direction direction) {
		return new POVButton(this, direction.value);
	}

	public Trigger getAxis(Button button) {
		return new JoystickButton(this, button.value);
	}

	public boolean getUpButton() {
		return getPOV() == Direction.kUp.value;
	}

	public boolean getUpButtonPressed() {
		synchronized(povPressed) {
			boolean pNow = getUpButton();
			boolean pLast = povPressed.get(Direction.kUp);

			if (pNow != pLast)
				povPressed.put(Direction.kUp, pNow);

			return pNow && !pLast;
		}
	}

	public boolean getUpButtonReleased() {
		synchronized(povReleased) {
			boolean pNow = getUpButton();
			boolean pLast = povReleased.get(Direction.kUp);

			if (pNow != pLast)
				povReleased.put(Direction.kUp, pNow);

			return !pNow && pLast;
		}
	}

	public boolean getRightButton() {
		return getPOV() == Direction.kRight.value;
	}

	public boolean getRightButtonPressed() {
		synchronized(povPressed) {
			boolean pNow = getRightButton();
			boolean pLast = povPressed.get(Direction.kRight);

			if (pNow != pLast)
				povPressed.put(Direction.kRight, pNow);

			return pNow && !pLast;
		}
	}

	public boolean getRightButtonReleased() {
		synchronized(povReleased) {
			boolean pNow = getRightButton();
			boolean pLast = povReleased.get(Direction.kRight);

			if (pNow != pLast)
				povReleased.put(Direction.kRight, pNow);

			return !pNow && pLast;
		}
	}

	public boolean getDownButton() {
		return getPOV() == Direction.kDown.value;
	}

	public boolean getDownButtonPressed() {
		synchronized(povPressed) {
			boolean pNow = getDownButton();
			boolean pLast = povPressed.get(Direction.kDown);

			if (pNow != pLast)
				povPressed.put(Direction.kDown, pNow);

			return pNow && !pLast;
		}
	}

	public boolean getDownButtonReleased() {
		synchronized(povReleased) {
			boolean pNow = getDownButton();
			boolean pLast = povReleased.get(Direction.kDown);

			if (pNow != pLast)
				povReleased.put(Direction.kDown, pNow);

			return !pNow && pLast;
		}
	}

	public boolean getLeftButton() {
		return getPOV() == Direction.kLeft.value;
	}

	public boolean getLeftButtonPressed() {
		synchronized(povPressed) {
			boolean pNow = getLeftButton();
			boolean pLast = povPressed.get(Direction.kLeft);

			if (pNow != pLast)
				povPressed.put(Direction.kLeft, pNow);

			return pNow && !pLast;
		}
	}

	public boolean getLeftButtonReleased() {
		synchronized(povReleased) {
			boolean pNow = getLeftButton();
			boolean pLast = povReleased.get(Direction.kLeft);

			if (pNow != pLast)
				povReleased.put(Direction.kLeft, pNow);

			return !pNow && pLast;
		}
	}
}