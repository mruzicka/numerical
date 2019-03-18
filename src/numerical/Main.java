package numerical;

public class Main {

	public static void main(String[] args) {
		double h_MAX = 10000;
		double F_T = 350000000;
		double V_BURN = 1000;
		double Q_V = 1;
		double m_0 = 260000;
		double rho = 5000;

		Burn suicideBurn = new Burn(h_MAX, F_T, V_BURN, Q_V, m_0, rho);
		NumericalMethod method;

//		method = new Bisection();
//		method = new FalsePosition();
//		method = new TwoStageMethod();
		method = new AlternatingMethod();

//		method.set_debug(true);

		double time_burn = method.compute(suicideBurn::solution_function, suicideBurn.lower_bound, suicideBurn.tentative_upper_bound);

		System.out.println(time_burn);
		System.out.println(suicideBurn.h_BURN(time_burn));
	}

}
