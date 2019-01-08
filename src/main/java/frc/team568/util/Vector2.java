/*
 * Represents a two-dimensional vector quantity.
 * X+ is right, Y+ is forward. Positive rotation is clockwise.
 * Class is immutable for safety
 */

package frc.team568.util;

public class Vector2 {
	public static final Vector2 forward = new Vector2(0, 1);
	public static final Vector2 back = new Vector2(0, -1);
	public static final Vector2 left = new Vector2(-1, 0);
	public static final Vector2 right = new Vector2(1, 0);
	public static final Vector2 zero = new Vector2(0, 0);
	public static final Vector2 one = new Vector2(1, 1);

	public final double x;
	public final double y;

	protected Vector2(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2 of(final double x, final double y) {
		return new Vector2(x, y);
	}

	public double magnitude() {
		return Math.sqrt(sqrMagnitude());
	}

	public double sqrMagnitude() {
		return x * x + y * y;
	}

	public Vector2 normalize() {
		double magnitude = magnitude();
		return new Vector2(x / magnitude, y / magnitude);
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Vector2) ? (this.x == ((Vector2) o).x && this.y == ((Vector2) o).y) : false;
	}

	@Override
	public String toString() {
		return x + "," + y;
	}

	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	public Vector2 add(Vector2 v2) {
		return add(this, v2);
	}

	public Vector2 add(final double x, final double y) {
		return new Vector2(this.x + x, this.y + y);
	}

	public Vector2 addX(double x) {
		return new Vector2(this.x + x, this.y);
	}

	public Vector2 addY(double y) {
		return new Vector2(this.x, this.y + y);
	}

	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

	public Vector2 subtract(Vector2 v2) {
		return subtract(this, v2);
	}

	public static Vector2 multiply(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x * v2.x, v1.y * v2.y);
	}

	public Vector2 multiply(Vector2 v2) {
		return multiply(this, v2);
	}

	public static Vector2 divide(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x / v2.x, v1.y / v2.y);
	}

	public Vector2 divide(Vector2 v2) {
		return divide(this, v2);
	}

	public static Vector2 scale(Vector2 v1, double s) {
		return new Vector2(v1.x * s, v1.y * s);
	}

	public Vector2 scale(double s) {
		return scale(this, s);
	}

	public static double dotProduct(Vector2 v1, Vector2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public double dotProduct(Vector2 v2) {
		return dotProduct(this, v2);
	}

	public static double sqrDistance(Vector2 v1, Vector2 v2) {
		double dx = v2.x - v1.x;
		double dy = v2.y - v1.y;
		return dx * dx + dy * dy;
	}

	public double sqrDistance(Vector2 v2) {
		return sqrDistance(this, v2);
	}

	public static double distance(Vector2 v1, Vector2 v2) {
		return Math.sqrt(sqrDistance(v1, v2));
	}

	public double distance(Vector2 v2) {
		return distance(this, v2);
	}

	public static double angle(Vector2 v1, Vector2 v2) {
		return Math.atan2(v2.y - v1.y, v2.x - v1.x) / Math.PI * 180;
	}

	public double angle(Vector2 v2) {
		return angle(this, v2);
	}

	public static double heading(Vector2 v1, Vector2 v2) {
		return 360 - angle(v1, v2) % 360;
	}

	public double heading(Vector2 v2) {
		return heading(this, v2);
	}

	public static Vector2 rotate(Vector2 v1, double r) {
		double rRad = Math.toRadians(r);
		double rCos = Math.cos(rRad);
		double rSin = Math.sin(rRad);
		return new Vector2(v1.x * rCos - v1.y * rSin, v1.x * rSin + v1.y * rCos);
	}

	public Vector2 rotate(double r) {
		return rotate(this, r);
	}
}
