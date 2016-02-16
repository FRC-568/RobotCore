package org.usfirst.frc.team568.util;

public class Vector2 implements Comparable<Vector2> {
	public double x;
	public double y;

	public Vector2() {
		x = 0;
		y = 0;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static final Vector2 up = new Vector2(0, 1);
	public static final Vector2 down = new Vector2(0, -1);
	public static final Vector2 left = new Vector2(-1, 0);
	public static final Vector2 right = new Vector2(1, 0);
	public static final Vector2 zero = new Vector2(0, 0);
	public static final Vector2 one = new Vector2(1, 1);

	public double magnitude() {
		return Math.sqrt(sqrMagnitude());
	}

	public double sqrMagnitude() {
		return x * x + y * y;
	}

	public Vector2 normalized() {
		double magnitude = magnitude();
		return new Vector2(x / magnitude, y / magnitude);
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Vector2) ? (this.x == ((Vector2) o).x && this.y == ((Vector2) o).y) : false;
	}

	@Override
	public int compareTo(Vector2 o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static Vector2 add(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x + v2.x, v1.y + v2.y);
	}

	public static Vector2 subtract(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x - v2.x, v1.y - v2.y);
	}

	public static Vector2 multiply(Vector2 v1, Vector2 v2) {
		return new Vector2(v1.x * v2.x, v1.y * v2.y);
	}

	public static Vector2 scale(Vector2 v1, double s) {
		return new Vector2(v1.x * s, v1.y * s);
	}

	public static double dotProduct(Vector2 v1, Vector2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}

	public static double sqrDistance(Vector2 v1, Vector2 v2) {
		double dx = v2.x - v1.x;
		double dy = v2.y - v1.y;
		return dx * dx + dy * dy;
	}

	public static double distance(Vector2 v1, Vector2 v2) {
		return Math.sqrt(sqrDistance(v1, v2));
	}

	public static double angle(Vector2 v1, Vector2 v2) {
		return Math.atan2(v2.y - v1.y, v2.x - v1.x) / Math.PI * 180;
	}

	public static double heading(Vector2 v1, Vector2 v2) {
		double angle = angle(v1, v2);
		return (angle > 0) ? (360 - angle) : (-angle);
	}

	public static Vector2 rotate(Vector2 v1, double r) {
		double rRad = Math.toRadians(r);
		double rCos = Math.cos(rRad);
		double rSin = Math.sin(rRad);
		return new Vector2(v1.x * rCos - v1.y * rSin, v1.x * rSin + v1.y * rCos);

	}
}
