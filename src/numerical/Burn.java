package numerical;

@SuppressWarnings("WeakerAccess")
public class Burn {

	public static final double g = 9.81;
	public static final double two_g = 2 * g;

	private static final double m_0_fraction = 0.9;

	private final double h_MAX;

	private final double rho_Q_V;
	private final double F_T_over_rho_Q_V;
	private final double F_T_over_rho_Q_V_squared;
	private final double F_T_squared_over_2g_rho_Q_V_squared;

	private final double m_0_plus_rho_V_BURN;
	private final double ln_m_0_plus_rho_V_BURN;

	private final double linear_term;
	private final double constant_term;

	public final double lower_bound;
	public final double upper_bound;
	public final double tentative_upper_bound;

	public Burn(double _h_MAX, double F_T, double V_BURN, double Q_V, double m_0, double rho) {
		h_MAX = _h_MAX;

		double rho_V_BURN = rho * V_BURN;

		rho_Q_V = rho * Q_V;

		lower_bound = 0;
		upper_bound = V_BURN / Q_V;
		tentative_upper_bound = (m_0 * m_0_fraction + rho_V_BURN) / rho_Q_V;

		F_T_over_rho_Q_V = F_T / rho_Q_V;
		F_T_over_rho_Q_V_squared = F_T_over_rho_Q_V / rho_Q_V;

		F_T_squared_over_2g_rho_Q_V_squared = F_T * F_T_over_rho_Q_V_squared / two_g;

		m_0_plus_rho_V_BURN = m_0 + rho_V_BURN;
		ln_m_0_plus_rho_V_BURN = Math.log(m_0_plus_rho_V_BURN);

		linear_term = F_T_over_rho_Q_V * (1 + ln_m_0_plus_rho_V_BURN);

		constant_term = h_MAX - F_T_over_rho_Q_V_squared * m_0_plus_rho_V_BURN * ln_m_0_plus_rho_V_BURN;
	}

	public double solution_function(double t) {
		double m_0_plus_rho_V_BURN_minus_rho_Q_V_t = m_0_plus_rho_V_BURN - rho_Q_V * t;
		double ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t = Math.log(m_0_plus_rho_V_BURN_minus_rho_Q_V_t);
		double ln_m_0_plus_rho_V_BURN_minus_ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t = ln_m_0_plus_rho_V_BURN - ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t;

		return constant_term + linear_term * t
				+ F_T_over_rho_Q_V_squared * m_0_plus_rho_V_BURN_minus_rho_Q_V_t * ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t
				- F_T_squared_over_2g_rho_Q_V_squared * ln_m_0_plus_rho_V_BURN_minus_ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t * ln_m_0_plus_rho_V_BURN_minus_ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t;
	}

	public double h_BURN(double t) {
		double ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t = Math.log(m_0_plus_rho_V_BURN - rho_Q_V * t);
		double nominator = F_T_over_rho_Q_V * (ln_m_0_plus_rho_V_BURN - ln_m_0_plus_rho_V_BURN_minus_rho_Q_V_t) - g * t;

		return h_MAX - nominator * nominator / two_g;
	}

}
