package numerical;

public class FalsePosition extends NumericalMethod {

	@SuppressWarnings("WeakerAccess")
	public static final Solver SOLVER_INSTANCE = (a, f_a, b, f_b) -> b - (f_b * (b - a)) / (f_b - f_a);

	@Override
	protected Solver get_solver() {
		return SOLVER_INSTANCE;
	}

}
