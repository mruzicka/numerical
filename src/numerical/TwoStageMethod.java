package numerical;

public class TwoStageMethod extends NumericalMethod {

	public static class CombinedSolver implements Solver {

		@SuppressWarnings("WeakerAccess")
		protected double switch_range;

		@SuppressWarnings("WeakerAccess")
		protected Solver solver = (a, f_a, b, f_b) -> {
			switch_range = Math.abs(a - b) / 100;

			solver = (_a, _f_a, _b, _f_b) -> {
				if (Math.abs(_a - _b) < switch_range)
					return (solver = FalsePosition.SOLVER_INSTANCE).next_approximation(_a, _f_a, _b, _f_b);

				return Bisection.SOLVER_INSTANCE.next_approximation(_a, _f_a, _b, _f_b);
			};

			return Bisection.SOLVER_INSTANCE.next_approximation(a, f_a, b, f_b);
		};

		@Override
		public double next_approximation(double a, double f_a, double b, double f_b) {
			return solver.next_approximation(a, f_a, b, f_b);
		}

	}

	@Override
	protected Solver get_solver() {
		return new CombinedSolver();
	}

}
