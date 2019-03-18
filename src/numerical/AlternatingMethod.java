package numerical;

public class AlternatingMethod extends NumericalMethod {

	public static class AlternatingSolver implements Solver {

		@SuppressWarnings("WeakerAccess")
		protected SwitchingSolver currentSolver;

		@SuppressWarnings("WeakerAccess")
		protected abstract class SwitchingSolver implements Solver {

			protected SwitchingSolver nextSolver;

			public void setNextSolver(SwitchingSolver nextSolver) {
				this.nextSolver = nextSolver;
			}

			protected void switchSolver() {
				currentSolver = nextSolver;
			}

		}

		@SuppressWarnings("WeakerAccess")
		public AlternatingSolver() {
			SwitchingSolver switchingBisectionSolver = new SwitchingSolver() {

				public double next_approximation(double a, double f_a, double b, double f_b) {
					double c = FalsePosition.SOLVER_INSTANCE.next_approximation(a, f_a, b, f_b);

					if (Math.getExponent(c) >= Math.getExponent(b - a)) {
						switchSolver();
						return c;
					}

					return Bisection.SOLVER_INSTANCE.next_approximation(a, f_a, b, f_b);
				}

			};
			SwitchingSolver switchingFalsePositionSolver = new SwitchingSolver() {

				public double next_approximation(double a, double f_a, double b, double f_b) {
					double c = FalsePosition.SOLVER_INSTANCE.next_approximation(a, f_a, b, f_b);
					double ratio = Math.abs((c - a) / (b - a));

					if (Math.abs(0.5 - ratio) > 0.4)
						switchSolver();

					return c;
				}

			};

			switchingBisectionSolver.setNextSolver(switchingFalsePositionSolver);
			switchingFalsePositionSolver.setNextSolver(switchingBisectionSolver);

			currentSolver = switchingBisectionSolver;
		}

		@Override
		public double next_approximation(double a, double f_a, double b, double f_b) {
			return currentSolver.next_approximation(a, f_a, b, f_b);
		}

	}

	@Override
	protected Solver get_solver() {
		return new AlternatingSolver();
	}

}
