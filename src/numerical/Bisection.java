package numerical;

public class Bisection extends NumericalMethod {

	@SuppressWarnings("WeakerAccess")
	public static final Solver SOLVER_INSTANCE = (a, f_a, b, f_b) -> (a + b) / 2;

	@Override
	protected Solver get_solver() {
		return SOLVER_INSTANCE;
	}

}
