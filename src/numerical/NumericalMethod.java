package numerical;

import java.util.function.DoubleUnaryOperator;

@SuppressWarnings("WeakerAccess")
public abstract class NumericalMethod {

	public static final double LN_2 = Math.log(2);

	private static final int EXPONENT_SHIFT;

	private static final double ERROR_THRESHOLD;

	static {
		final double REQUIRED_PRECISION = 1e-12;

		EXPONENT_SHIFT = Math.getExponent(REQUIRED_PRECISION);

		ERROR_THRESHOLD = REQUIRED_PRECISION / Math.exp(LN_2 * EXPONENT_SHIFT);
	}

	@FunctionalInterface
	public interface Solver {

		double next_approximation(double a, double f_a, double b, double f_b);

	}

	public double compute(DoubleUnaryOperator function, double a, double b) {
		double f_a = function.applyAsDouble(a);

		if (f_a == 0)
			return a;

		double f_b = function.applyAsDouble(b);

		if (f_b == 0)
			return b;

		boolean neg;

		CHECK:
		{
			if (f_a < 0) {
				if (f_b > 0) {
					neg = true;
					break CHECK;
				}
			} else {
				if (f_b < 0) {
					neg = false;
					break CHECK;
				}
			}

			throw new IllegalArgumentException("Wrong initial bracketing");
		}

		Solver solver = get_solver();
		double c = solver.next_approximation(a, f_a, b, f_b);
		long iteration = 0;

		do {
			double f_c = function.applyAsDouble(c);

			System.out.println(iteration++ + ": a = " + a + ", b = " + b + ", c = " + c + ", f(c) = " + f_c);

			if (f_c == 0)
				break;

			if ((f_c < 0) ^ neg) {
				b = c;
				f_b = f_c;
			} else {
				a = c;
				f_a = f_c;
			}

			double old_c = c;

			c = solver.next_approximation(a, f_a, b, f_b);

			if (Math.abs(c - old_c) / Math.exp(LN_2 * (Math.getExponent(c) + EXPONENT_SHIFT)) < ERROR_THRESHOLD)
				break;
		} while (true);

		return c;
	}

	protected abstract Solver get_solver();

}
