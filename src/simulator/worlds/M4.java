package simulator.worlds;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import simulator.CulturalSimulator;
import simulator.control.Controller;

/**
 * Based on experiment 1 of Flache and Macy (2011) this class implements:
 * homophily (Axelrod , 1997), mutations and selection error.
 * 
 * This class also implements: 1. Institutional influence: a probabilistic
 * change confronting agents homophily and institution homophily (and associated
 * alpha and beta related to them) 2. Institutional loyalty: probabilistic
 * institutional change according to homophily between neighbor's culture and
 * the agent's (and an alpha prime and beta prime associated associated to them)
 * 3. Democratic process: each D iterations, there is a democratic process in
 * which the agents vote for an institutional trait that they would like to
 * change in the institution they are associated to. The selected trait would be
 * the one that makes the institution more similar to them. 4. Propaganda
 * process: each P iterations, there is a propaganda process in which the
 * institution send a propaganda message its associated agents in order to
 * change a trait that would make them more similar to the institution. The
 * trait is selected based on the most divergent trait, i.e. the trait that less
 * of the agent share with their own institution.
 * 
 * @author Roberto Ulloa
 * @version 1.0, March 2016
 *
 */
public class M4 extends M1 {

	private static final long serialVersionUID = 6739780243602561128L;

	/**
	 * Institution of each agent.
	 */
	protected int institutions[][] = null;
	/**
	 * Center of each institution (according to the agents who belong to it
	 */
	protected int institutionsCenters[][] = null;
	/**
	 * Implements a circular doubled linked list with the members of the same
	 * culture (country men)
	 */
	protected int[][] countryman_right_r = null;
	protected int[][] countryman_right_c = null;
	private int[][] countryman_left_r = null;
	private int[][] countryman_left_c = null;
	/**
	 * Keep the votes of the country men
	 */
	protected int[][] votes = null;
	/**
	 * Keep track of the agents that had already vote
	 */
	protected boolean[][] votes_flags;
	/**
	 * The current flag to indicate an agent has vote
	 */
	protected boolean hasnt_vote_flag = false;
	/**
	 * Keep the current max traits
	 */
	protected int max_traits[] = null;
	/**
	 * Keep the current max features
	 */
	private int max_features[] = null;
	/**
	 * Inactive trait main for the institutions
	 */
	private static int INACTIVE_TRAIT = -1;
	/**
	 * This are variables that I use inside run_iteration, declared as fields
	 * for efficiency, to avoid re-declaration, all of them are transient in
	 * order to avoid them to be saved in the serialized object.
	 */
	private transient int c;
	private transient int r;
	private transient int n;
	private transient int nr;
	private transient int nc;
	private transient int institution;
	private transient int neighbors_institution;
	private transient int mismatchesN;
	private transient int institution_overlap;
	private transient int neighbors_institution_overlap;
	private transient int selected_feature;
	private transient int selected_trait;
	private transient int institution_trait;
	private transient int neighbors_institution_trait;
	private transient double institution_resistance;
	private transient float institutional_factor;
	private transient int non_death_traitsN;
	private transient int differences;
	private transient int rr;
	private transient int rc;
	private transient int lr;
	private transient int lc;
	private transient int nrr;
	private transient int nrc;
	private transient int temp_r;
	private transient int max_difference_trait_votes;
	private transient int max_feature_traitN;
	private transient int culture_current_trait_votes;

	@Override
	public void setup() {
		super.setup();

		institutions = new int[ROWS][COLS];
		institutionsCenters = new int[ROWS][COLS];

		institution_traits = new int[ROWS * COLS][FEATURES];
		institutionsN = new int[ROWS * COLS];

		countryman_right_r = new int[ROWS][COLS];
		countryman_right_c = new int[ROWS][COLS];
		countryman_left_r = new int[ROWS][COLS];
		countryman_left_c = new int[ROWS][COLS];

		// Initialize cultures and nationalities
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				institutions[r][c] = r * COLS + c;
				institutionsCenters[r][c] = r * COLS + c;
				institutionsN[r * COLS + c] = 1;
				for (int f = 0; f < FEATURES; f++) {
					institution_traits[r * COLS + c][f] = -1;
				}

				countryman_right_r[r][c] = r;
				countryman_right_c[r][c] = c;
				countryman_left_r[r][c] = r;
				countryman_left_c[r][c] = c;

			}
		}

		votes_flags = new boolean[ROWS][COLS];
		votes = new int[FEATURES][TRAITS + 1];
		max_traits = new int[TRAITS];

		max_traits = new int[FEATURES * TRAITS];
		max_features = new int[FEATURES * TRAITS];

		if (!RANDOM_INITIALIZATION) {
			nr = ROWS / 2;
			nc = COLS / 2;

			int middle_trait = (int) Math.round(TRAITS / 2.0 - 0.01);

			for (int f = 0; f < FEATURES; f++) {
				institution_traits[institutions[nr][nc]][f] = middle_trait;
			}

			for (int r = 0; r < ROWS; r++) {
				for (int c = 0; c < COLS; c++) {
					move_to_institution(r, c, nr, nc);
				}
			}

			calculate_institutions_centers();

		}
	}

	@Override
	protected void reset() {
		super.reset();
		institutions = null;
		institutionsCenters = null;
		institution_traits = null;
		institutionsN = null;
		countryman_right_r = null;
		countryman_right_c = null;
		countryman_left_r = null;
		countryman_left_c = null;

		votes_flags = null;
		votes = null;
		max_traits = null;

		max_traits = null;
		max_features = null;
	}

	@Override
	public String getModelDescription() {
		return MODEL + ": Institutions including homophily Axelrod (1997) - Ulloa et al. (2016)";
	}

	@Override
	public void run_iterations() {

		for (int ic = 0; ic < SPEED; ic++) {

			for (int i = 0; i < TOTAL_AGENTS; i++) {

				// select the agent
				r = rand.nextInt(ROWS);
				c = rand.nextInt(COLS);

				// select the neighbor that might influence the agent
				n = rand.nextInt(neighboursN[r][c]);
				nr = neighboursX[r][c][n];
				nc = neighboursY[r][c][n];

				// select the agent's and neighbor's institution
				institution = institutions[r][c];
				neighbors_institution = institutions[nr][nc];

				// get the number of mismatches between the two agents
				mismatchesN = 0;
				differences = 0;
				non_death_traitsN = 0;

				// get the number of identical traits between the agent and its
				// institution
				institution_overlap = 0;

				// get the number of identical traits between the agent
				// and its neighbors's institution
				neighbors_institution_overlap = 0;

				// Compare the agents
				for (int f = 0; f < FEATURES; f++) {
					if (traits[nr][nc][f] != DEAD_TRAIT) {
						non_death_features[non_death_traitsN] = f;
						non_death_traitsN++;
						if (traits[r][c][f] != traits[nr][nc][f]) {
							mismatches[mismatchesN] = f;
							mismatchesN++;
							if (traits[r][c][f] != DEAD_TRAIT) {
								differences++;
							}
						}
					} else {
						differences++;
					}
					if (traits[r][c][f] == institution_traits[neighbors_institution][f]) {
						neighbors_institution_overlap++;
					}
					if (traits[r][c][f] == institution_traits[institution][f]) {
						institution_overlap++;
					}
				}

				// the agent cannot interact with a dead individual
				if (non_death_traitsN > 0) {

					// if it manage to arrive by selection error, there is still
					// a chance of influencing the institution
					if (mismatchesN == 0) {
						selected_feature = non_death_features[rand.nextInt(non_death_traitsN)];
					} else {
						selected_feature = mismatches[rand.nextInt(mismatchesN)];
					}

					// select the traits in play (agent's, neighbor's and
					// institution's)
					selected_trait = traits[nr][nc][selected_feature];
					institution_trait = institution_traits[institution][selected_feature];
					neighbors_institution_trait = institution_traits[neighbors_institution][selected_feature];

					// When there is no institutional conflict because the
					// institution favors the selected trait
					// or because the trait is already different, then just use
					// homophily
					if (selected_trait == institution_trait
							|| institution_trait != -1 && traits[r][c][selected_feature] != institution_trait) {

						// check if there is actual interaction checking against
						// the homophily and
						// considering the selection error. The present formula
						// integrates homophily
						// and selection error into one probability.
						if (rand.nextFloat() < (((FEATURES - differences) / (float) FEATURES) * (1 - SELECTION_ERROR)
								+ (differences / (float) FEATURES) * SELECTION_ERROR)) {

							//////////////////////
							// CHANGE THE TRAIT //
							//////////////////////
							traits[r][c][selected_feature] = selected_trait;
						}

					} else {

						// Probability of interaction taking into account
						// selection error
						float prob_int = (((FEATURES - differences) / (float) FEATURES) * (1 - SELECTION_ERROR)
								+ (differences / (float) FEATURES) * SELECTION_ERROR);

						// Calculate the institutional resistance
						institution_resistance = ALPHA * institution_overlap / (float) FEATURES;

						// check if there is actual interaction
						if (rand.nextFloat() >= institution_resistance / (prob_int * BETA + institution_resistance)) {

							// at this point, we are sure that the agent is
							// losing similarity with the institution
							// if the new trait is the same of the institution
							// trait, and the current
							// agent's trait is different from the institution
							// then the cultural overlap
							// will increase
							if (institution_trait == selected_trait
									&& traits[r][c][selected_feature] != institution_trait) {
								institution_overlap++;
							}
							// otherwise, if the new trait is different from the
							// institution trait, and
							// the current agent's trait is the same of the
							// institution then the cultural
							// overlap will decrease
							else if (institution_trait != selected_trait
									&& traits[r][c][selected_feature] == institution_trait) {
								institution_overlap--;
							}

							// if the new trait is the same of the institution
							// trait, and the current
							// agent's trait is different from the institution
							// then the institutional overlap
							// will increase
							if (neighbors_institution_trait == selected_trait
									&& traits[r][c][selected_feature] != neighbors_institution_trait) {
								neighbors_institution_overlap++;
							}
							// otherwise, if the new trait is different from the
							// institution trait, and
							// the current agent's trait is the same of the
							// institution then the institutional
							// overlap will decrease
							else if (neighbors_institution_trait != selected_trait
									&& traits[r][c][selected_feature] == neighbors_institution_trait) {
								neighbors_institution_overlap--;
							}

							// if the selected feature of the neighbor's
							// institution hasn't been
							// assigned yet, then it is an opportunity to be
							// more similar
							if (institution_traits[neighbors_institution][selected_feature] == -1) {
								neighbors_institution_overlap++;
							}

							//////////////////////
							// CHANGE THE TRAIT //
							//////////////////////
							// we change the trait after adjusting the overlaps
							////////////////////// //
							traits[r][c][selected_feature] = selected_trait;

							// when the agent doesn't have any similarity with
							// the institutions then
							// he loses his identity and accept the trait. This
							// also avoid divisions
							// by 0 in the next condition.
							if (institution_overlap == 0 && neighbors_institution_overlap == 0) {
								// Nothing happen when the agent is not similar
								// to any of the two institutions
							}
							// if there is no institutional shock (current trait
							// is different to its institution's),
							// accept the change
							else {
								institutional_factor = institution_overlap * ALPHA_PRIME;

								if (rand.nextFloat() >= institutional_factor / (float) neighbors_institution_overlap
										* BETA_PRIME + institutional_factor) {

									// if the institutions are different, then
									// institution change to its neighbors
									if (institution != neighbors_institution) {

										// its institution lost a citizen
										institutionsN[institution]--;
										institutions[r][c] = neighbors_institution;
										institutionsN[neighbors_institution]++;

										// temporal variables of the agent
										rr = countryman_right_r[r][c];
										rc = countryman_right_c[r][c];
										lr = countryman_left_r[r][c];
										lc = countryman_left_c[r][c];

										// Remove the agent from the current
										// culture asking my country men
										// to grab each other
										countryman_left_r[rr][rc] = lr;
										countryman_left_c[rr][rc] = lc;
										countryman_right_r[lr][lc] = rr;
										countryman_right_c[lr][lc] = rc;

										// temporal variables of the neighbor
										nrr = countryman_right_r[nr][nc];
										nrc = countryman_right_c[nr][nc];

										// The agent grab the neighbor with the
										// left and the right's neighbor
										// country man with the right
										countryman_right_r[r][c] = nrr;
										countryman_right_c[r][c] = nrc;
										countryman_left_r[r][c] = nr;
										countryman_left_c[r][c] = nc;

										// The neighbor and its right countryman
										// grab the agent
										countryman_right_r[nr][nc] = r;
										countryman_right_c[nr][nc] = c;
										countryman_left_r[nrr][nrc] = r;
										countryman_left_c[nrr][nrc] = c;

									} // END of different institution

									/**
									 * In principle institutions just fill their
									 * features when they receive a new agent
									 * and there is no current trait in the
									 * institution for a particular feature.
									 * This decision has many implications, e.g.
									 * some institution might never be
									 * "completed" (i.e. some features could
									 * stay with -1), institutions features are
									 * written only once, initial conditions
									 * quickly decide the features that an
									 * institution contains. When democracy is
									 * active, this restriction is relaxed,
									 * somehow democracy allows institutional
									 * change in the new generations.
									 */
									// if there is no trait selected for the
									// selected feature, then make the
									// selected trait part of the institution
									if (institution_traits[neighbors_institution][selected_feature] == -1) {
										institution_traits[neighbors_institution][selected_feature] = selected_trait;
									} // END of add a institutional trait to
										// institution

								} // END of institution change

							} // END of else

						} // END of institutional shock

					} // END of else

				} // END of not grieving

				// mutation
				if (rand.nextFloat() >= 1 - MUTATION) {
					mutant_feature = rand.nextInt(FEATURES);
					// Don't change dead features
					if (mutant_feature != DEAD_TRAIT) {
						traits[r][c][mutant_feature] = rand.nextInt(TRAITS);
					}
				}

			} // END of total agents

			if (FREQ_DEM > 0 && (iteration + ic + 1) % FREQ_DEM == 0) {
				// log.print(IDENTIFIER,"Democratic Process");

				// Democratic Process
				// traverse rows
				for (r = 0; r < ROWS; r++) {

					// traverse columns
					for (c = 0; c < COLS; c++) {

						// if it hasn't vote
						if (votes_flags[r][c] == hasnt_vote_flag) {

							// select the institution
							institution = institutions[r][c];

							// clean the votes of the features
							for (int f = 0; f < FEATURES; f++) {
								for (int t = 0; t < TRAITS; t++) {
									votes[f][t] = 0;
								}
							}

							// include my votes
							nr = r;
							nc = c;
							temp_r = nr;

							// country-men votes
							do {

								// let the agent vote on all the active features
								for (int f = 0; f < FEATURES; f++) {
									if (traits[nr][nc][f] != DEAD_TRAIT) {
										votes[f][traits[nr][nc][f]]++;
									}
								}

								votes_flags[nr][nc] = !hasnt_vote_flag;

								// avoid overwriting the nr before time
								temp_r = nr;

								// look for the next country man on the right
								nr = countryman_right_r[nr][nc];
								nc = countryman_right_c[temp_r][nc];

								if (votes_flags[nr][nc] != hasnt_vote_flag && !(nr == r && nc == c)) {
									log.print(IDENTIFIER,
											"ERROR! somebody already voted, the circular list is inconsistent.");
								}

								// while the next agent hasn't vote (nr == r &&
								// nc == c)
							} while (votes_flags[nr][nc] == hasnt_vote_flag);
							// END of country men votes

							// set winner traits for the current culture
							max_difference_trait_votes = 0;
							max_feature_traitN = 0;
							culture_current_trait_votes = 0;

							// iterate over the active features
							for (int f = 0; f < FEATURES; f++) {
								culture_current_trait_votes = 0;
								if (institution_traits[institution][f] != -1) {
									culture_current_trait_votes = votes[f][institution_traits[institution][f]];
								}
								// search for the traits with most votes
								for (int t = 0; t < TRAITS; t++) {
									if (max_difference_trait_votes < votes[f][t] - culture_current_trait_votes) {
										max_difference_trait_votes = votes[f][t] - culture_current_trait_votes;
										max_feature_traitN = 0;
										max_traits[max_feature_traitN] = t;
										max_features[max_feature_traitN++] = f;
									} else if (max_difference_trait_votes == votes[f][t]
											- culture_current_trait_votes) {
										max_traits[max_feature_traitN] = t;
										max_features[max_feature_traitN++] = f;
									}
								} // END of search for the traits with most
									// votes

							} // END of the iteration over the active features

							// if there is maximal group
							if (max_feature_traitN > 0) {
								int feature_trait_index = rand.nextInt(max_feature_traitN);
								selected_feature = max_features[feature_trait_index];

								// if there was actually a trait that got more
								// (and only more) votes
								// then randomly select one out of the winners
								// and change the trait
								if (institution_traits[institution][selected_feature] == -1
										|| max_difference_trait_votes > 0) {
									institution_traits[institution][selected_feature] = max_traits[feature_trait_index];
								}
							}

						} // END of it hasn't vote

					} // END of cols

				} // END Democratic Process

				// change the flag
				hasnt_vote_flag = !hasnt_vote_flag;
			}

			if (FREQ_PROP > 0 && (iteration + ic + 1) % FREQ_PROP == 0) {
				// log.print(IDENTIFIER,"Prop Process");

				// Propaganda Process
				// traverse rows
				for (r = 0; r < ROWS; r++) {

					// traverse columns
					for (c = 0; c < COLS; c++) {

						// if it hasn't vote
						if (votes_flags[r][c] == hasnt_vote_flag) {

							// select the institution
							institution = institutions[r][c];

							// include my votes
							nr = r;
							nc = c;
							temp_r = nr;

							do {

								// reset the mismatches counter
								mismatchesN = 0;

								// count mismatches between the agent and the
								// institution
								for (int f = 0; f < FEATURES; f++) {
									if (institution_traits[institution][f] != traits[nr][nc][f]) {
										mismatchesN++;
									}
								}

								for (int f = 0; f < FEATURES; f++) {
									// check if the propaganda has effect by
									// measuring the similarity with the
									// institution
									if (institution_traits[institution][f] != -1
											&& traits[nr][nc][f] != institution_traits[institution][f]
											&& rand.nextFloat() > mismatchesN / (float) FEATURES) {
										traits[nr][nc][f] = institution_traits[institution][f];
									}
								}

								votes_flags[nr][nc] = !hasnt_vote_flag;

								// avoid overwriting the nr before time
								temp_r = nr;

								// look for the next country man on the right
								nr = countryman_right_r[nr][nc];
								nc = countryman_right_c[temp_r][nc];

								if (votes_flags[nr][nc] != hasnt_vote_flag && !(nr == r && nc == c)) {
									log.print(IDENTIFIER,
											"ERROR! somebody already voted, the circular list is inconsistent.");
								}

								// while the next agent hasn't vote (nr == r &&
								// nc == c)
							} while (votes_flags[nr][nc] == hasnt_vote_flag);

						} // END of it hasn't vote

					} // END of cols

				} // END Propaganda Process

				// change the flag
				hasnt_vote_flag = !hasnt_vote_flag;

			}

		} // END of checkpoint

	} // END of run_experiment

	/**
	 * This is an auxiliar method for debugging, finding errors in the circular
	 * list that keeps the agents that belong to the same institution.
	 * 
	 * @return true if the circular list is consistent, i.e. the circular lists
	 *         have the correct beginnings and ends, all the members belong to
	 *         the same institutions and the size of the circular list
	 *         correspond to the registered sizes in the corresponding structure
	 */
	protected boolean check_circular_list() {
		boolean stop = false;
		boolean fail = false;
		int members_counter = 0;

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {

				// include my votes
				int nr = r;
				int nc = c;
				int temp_r;
				int nationality = institutions[r][c];

				members_counter = 0;
				boolean did_i_vote_before = votes_flags[nr][nc];

				while (votes_flags[nr][nc] == hasnt_vote_flag) {

					votes_flags[nr][nc] = !hasnt_vote_flag;

					members_counter++;

					if (institutions[nr][nc] != nationality) {
						System.out.println("1. Error in circular list: Different nationalities (" + nationality + ","
								+ institutions[nr][nc] + ")");
						fail = true;
					}

					// avoid overwriting the nr before time
					temp_r = nr;

					// look for the next country man on the right
					nr = countryman_right_r[nr][nc];
					nc = countryman_right_c[temp_r][nc];

					if (votes_flags[nr][nc] != hasnt_vote_flag && !(nr == r && nc == c)) {
						System.out.println("2. Error in circular list: Somebody already voted.");
						stop = true;
						fail = true;
					}
				}

				if (did_i_vote_before == hasnt_vote_flag && members_counter != institutionsN[nationality]) {
					System.out.println("3. Error in circular list: different number of members  iter(" + iteration
							+ ") inst (" + nationality + ") " + members_counter + "!=" + institutionsN[nationality]);
					fail = true;
				}
			}

		}

		if (stop) {
			System.out.println();
		}

		hasnt_vote_flag = !hasnt_vote_flag;

		return fail;
	}

	/**
	 * Calculate the institutional "geographical" centers based on the agents
	 * that belong to it.
	 */
	protected void calculate_institutions_centers() {
		int r_sum = 0;
		int c_sum = 0;
		int nr = 0;
		int nc = 0;
		int inst;
		int temp_r;
		int rc[] = new int[2];

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				institutionsCenters[r][c] = EMPTY;
			}
		}

		for (int r = 0; r < ROWS; r++) {

			for (int c = 0; c < COLS; c++) {

				// include my votes
				nr = r;
				nc = c;

				if (votes_flags[nr][nc] == hasnt_vote_flag) {

					r_sum = 0;
					c_sum = 0;

					while (votes_flags[nr][nc] == hasnt_vote_flag) {

						votes_flags[nr][nc] = !hasnt_vote_flag;

						r_sum += nr;
						c_sum += nc;

						// avoid overwriting the nr before time
						temp_r = nr;

						// look for the next country man on the right
						nr = countryman_right_r[nr][nc];
						nc = countryman_right_c[temp_r][nc];

					} // end of while

					inst = institutions[r][c];

					r_sum = Math.round(((float) r_sum / institutionsN[inst]));
					c_sum = Math.round(((float) c_sum / institutionsN[inst]));

					rc = search_free_institutionCenter(r_sum, c_sum);
					institutionsCenters[rc[0]][rc[1]] = inst;
				}
			}
		}

		hasnt_vote_flag = !hasnt_vote_flag;
	}

	/**
	 * This looks for a non currently occupied institution near the specified
	 * (ideal) coordinates in the geographical representation of institutions.
	 * The ideal center might be occupied by other institution already, so it is
	 * necessary to look for the nearest free cell.
	 * 
	 * @param r
	 *            ideal row for the institutional center
	 * @param c
	 *            ideal column for the institutional center
	 * @return a coordinate (r,c) representing a free cell to be the
	 *         geographical institutional center
	 */
	private int[] search_free_institutionCenter(int r, int c) {
		int x = 0, y = 0, dx = 0, dy = -1;
		int t = Math.max(ROWS, COLS);
		int maxI = (t * 2) * (t * 2);

		for (int i = 0; i < maxI; i++) {
			if ((-1 < r + x) && (r + x < ROWS) && (-1 < c + y) && (c + y < COLS)) {
				if (institutionsCenters[r + x][c + y] == EMPTY) {
					return new int[] { (r + x), (c + y) };
				}
			}

			if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
				t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
		}

		log.print(IDENTIFIER,
				"WARNING!! The circular spiral loop have failed finding a free instititution center. "
						+ "This should have never happened, searching for a free institution in the whole space. (" + r
						+ "," + c + ") \n");

		// This should never happen
		for (int r1 = 0; r1 < ROWS; r1++) {
			for (int c1 = 0; c1 < COLS; c1++) {
				if (institutionsCenters[r1][c1] == EMPTY) {

					return new int[] { r1, c1 };

				}
			}
		}

		log.print(IDENTIFIER, "ERROR! No free institution center was found in an exhaustive search.");
		return new int[] { -99999, -99999 };
	}

	/**
	 * This looks for a currently occupied institution near the specified
	 * (ideal) coordinates in the geographical representation of institutions.
	 * The ideal center is probably not occupied by other institution already,
	 * so it is necessary for one around
	 * 
	 * @param r
	 *            ideal row for the institutional center
	 * @param c
	 *            ideal column for the institutional center
	 * @return a coordinate (r,c) representing the nearest cell to the
	 *         geographical institutional center
	 */
	public int[] search_nearest_institutionCenter(int r, int c) {
		int x = 0, y = 0, dx = 0, dy = -1;
		int t = Math.max(ROWS, COLS);
		int maxI = (t * 2) * (t * 2);

		for (int i = 0; i < maxI; i++) {
			if ((-1 < r + x) && (r + x < ROWS) && (-1 < c + y) && (c + y < COLS)) {
				if (institutionsCenters[r + x][c + y] != EMPTY && institutionsN[institutionsCenters[r + x][c + y]] > 1 ) {
					return new int[] { (r + x), (c + y) };
				}
			}

			if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
				t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
		}
		
		return new int[] { r, c };

	}

	/**
	 * Count the number of institutions and search for the biggest one
	 */
	private void count_institutions() {
		biggest_institution = 0;
		alife_institutions = 0;
		for (int i = 0; i < institutionsN.length; i++) {
			if (institutionsN[i] > 0) {
				alife_institutions++;
				if (institutionsN[i] > biggest_institution) {
					biggest_institution = institutionsN[i];
				}
			}
		}
	}

	@Override
	protected String results() {
		count_institutions();
		return super.results();
	}

	@Override
	public void remove_partial_institution_content(int r, int c, double prob, Random rand) {
		int institution = institutionsCenters[r][c];
		if (institution != EMPTY && institutionsN[institution] > 0) {
			for (int f = 0; f < FEATURES; f++) {
				if (rand.nextDouble() < prob) {
					this.removed_traits++;
					institution_traits[institution][f] = -1;
				}
			}
		}
	}

	@Override
	public void remove_full_institution_content(int r, int c) {
		int institution = institutionsCenters[r][c];
		if (institution != EMPTY && institutionsN[institution] > 0) {
			this.removed_institutions++;
			for (int f = 0; f < FEATURES; f++) {
				institution_traits[institution][f] = -1;
			}
		}
	}

	@Override
	public void destoy_institution(int r, int c) {
		int institution = institutionsCenters[r][c];

		if (institution != EMPTY && institutionsN[institution] > 0) {
			this.destoyed_institutions++;
			int[] member = search_member(institution);
			int mr = member[0];
			int mc = member[1];

			if (institutionsN[institution] == 1) {
				this.stateless++;
				abandon_institution(mr, mc);
			} else {
				while (institutionsN[institution] > 1) {
					int nr = countryman_right_r[mr][mc];
					int nc = countryman_right_c[mr][mc];
					this.stateless++;
					abandon_institution(mr, mc);
					mr = nr;
					mc = nc;
				}
			}
		}
	}

	/**
	 * Search for any member that belong to the institution. This is useful to
	 * start iterating over the circular list that contains all institutional
	 * members.
	 * 
	 * @param institution
	 *            the institution the member has to be search
	 * @return any member of the institution
	 */
	private int[] search_member(int institution) {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				if (institutions[r][c] == institution) {
					return new int[] { r, c };
				}
			}
		}

		return new int[] { -555, -555 };
	}

	@Override
	public void apostasy(int r, int c) {
		this.apostates++;
		abandon_institution(r, c);
	}

	@Override
	public void convert_full_institution(int r, int c) {
		int institution = institutionsCenters[r][c];
		if (institution != EMPTY && institutionsN[institution] > 0) {
			this.converted_institutions++;
			for (int f = 0; f < FEATURES; f++) {
				institution_traits[institution][f] = TRAITS;
			}
		}
	}

	@Override
	public void convert_partial_institution(int r, int c, double prob, Random rand) {
		int institution = institutionsCenters[r][c];
		if (institution != EMPTY && institutionsN[institution] > 0) {
			for (int f = 0; f < FEATURES; f++) {
				if (rand.nextDouble() < prob) {
					this.converted_traits++;
					institution_traits[institution][f] = TRAITS;
				}
			}
		}
	}

	@Override
	public int pre_settlement(int r, int c) {
		int institution = abandon_institution(r, c);

		for (int f = 0; f < FEATURES; f++) {
			traits[r][c][f] = TRAITS;
			institution_traits[institution][f] = TRAITS;
		}

		return institution;
	}

	@Override
	public void settle(int r, int c, int nr, int nc) {
		this.settlers++;
		move_to_institution(r, c, nr, nc);
		for (int f = 0; f < FEATURES; f++) {
			traits[r][c][f] = TRAITS;
		}
	}

	@Override
	public void immigrate(int r, int c) {
		this.immigrants++;
		abandon_institution(r, c);
		for (int f = 0; f < FEATURES; f++) {
			traits[r][c][f] = TRAITS;
		}
	}

	@Override
	public void kill_individual(int r, int c) {
		this.casualties++;
		abandon_institution(r, c);
		for (int f = 0; f < FEATURES; f++) {
			traits[r][c][f] = DEAD_TRAIT;
		}
	}

	/**
	 * Make an individual abandon its own institute
	 * 
	 * @param r
	 *            row of the individual
	 * @param c
	 *            column of the individual
	 */
	private int abandon_institution(int r, int c) {

		if (institutionsN[institutions[r][c]] > 1) {
			// Search for a new institution to belong to
			institution = search_free_institution(r, c);

			// its institution lost a citizen
			institutionsN[institutions[r][c]]--;
			institutions[r][c] = -1;

			// temporal variables of the agent
			rr = countryman_right_r[r][c];
			rc = countryman_right_c[r][c];
			lr = countryman_left_r[r][c];
			lc = countryman_left_c[r][c];

			// Remove the agent from the current culture asking my country men
			// to grab each other
			countryman_left_r[rr][rc] = lr;
			countryman_left_c[rr][rc] = lc;
			countryman_right_r[lr][lc] = rr;
			countryman_right_c[lr][lc] = rc;

			// Grab myself since I don't have any institution associated and
			// I am by myself
			countryman_right_r[r][c] = r;
			countryman_right_c[r][c] = c;
			countryman_left_r[r][c] = r;
			countryman_left_c[r][c] = c;

			// add the agent to the new institution
			institutions[r][c] = institution;
			institutionsN[institution] = 1;
		}

		// Remove the traits of the new (empty) institution
		for (int f = 0; f < FEATURES; f++) {
			institution_traits[institutions[r][c]][f] = INACTIVE_TRAIT;
		}

		return institution;
	}

	/**
	 * Move an agent to neighbors agent institution
	 * 
	 * @param r
	 *            row of the agent
	 * @param c
	 *            column of the agent
	 * @param nr
	 *            row of the neighbor
	 * @param nc
	 *            column of the neighbor
	 */
	private void move_to_institution(int r, int c, int nr, int nc) {
		if (!(r == nr && c == nc)) {
			institution = institutions[r][c];
			neighbors_institution = institutions[nr][nc];

			// its institution lost a citizen
			institutionsN[institution]--;
			institutions[r][c] = neighbors_institution;
			institutionsN[neighbors_institution]++;

			// temporal variables of the agent
			rr = countryman_right_r[r][c];
			rc = countryman_right_c[r][c];
			lr = countryman_left_r[r][c];
			lc = countryman_left_c[r][c];

			// Remove the agent from the current culture asking my country men
			// to grab each other
			countryman_left_r[rr][rc] = lr;
			countryman_left_c[rr][rc] = lc;
			countryman_right_r[lr][lc] = rr;
			countryman_right_c[lr][lc] = rc;

			// temporal variables of the neighbor
			nrr = countryman_right_r[nr][nc];
			nrc = countryman_right_c[nr][nc];

			// The agent grab the neighbor with the left and the right's
			// neighbor
			// country man with the right
			countryman_right_r[r][c] = nrr;
			countryman_right_c[r][c] = nrc;
			countryman_left_r[r][c] = nr;
			countryman_left_c[r][c] = nc;

			// The neighbor and its right countryman grab the agent
			countryman_right_r[nr][nc] = r;
			countryman_right_c[nr][nc] = c;
			countryman_left_r[nrr][nrc] = r;
			countryman_left_c[nrr][nrc] = c;

		}

	}

	@Override
	protected void update_gui() {
		super.update_gui();
		if (!Controller.IS_BATCH) {
			display_institutional_cultural_space();
		}
	}

	/**
	 * Display the institutional cultural state in the interface
	 */
	private void display_institutional_cultural_space() {
		calculate_institutions_centers();

		BufferedImage alife_institutional_cultural_space_image = new BufferedImage(ROWS, COLS,
				BufferedImage.TYPE_INT_RGB);
		String ohex_alife_institutions_cultural_space_image = "";

		BufferedImage alife_insititutions_image = new BufferedImage(ROWS, COLS, BufferedImage.TYPE_INT_RGB);
		String alife_institution_ohex;

		BufferedImage institutonal_cultural_association_image = new BufferedImage(ROWS, COLS,
				BufferedImage.TYPE_INT_RGB);
		String institutonal_cultural_association_ohex = "";

		int institution = 0;
		int belonging_institution = 0;

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				institution = institutionsCenters[r][c];
				belonging_institution = institutions[r][c];

				ohex_alife_institutions_cultural_space_image = "";
				institutonal_cultural_association_ohex = "";
				alife_institution_ohex = "";

				// Institution association
				for (int f = 0; f < Math.min(FEATURES, 6); f++) {
					institutonal_cultural_association_ohex += get_color_for_trait(
							institution_traits[belonging_institution][f]);
				}
				institutonal_cultural_association_ohex = "#" + institutonal_cultural_association_ohex;

				// Institution association
				if ((institution != EMPTY && institutionsN[institution] <= 1 ) || institution == EMPTY) {
				//if ( institution == EMPTY) {
					ohex_alife_institutions_cultural_space_image = "000000";
				} else {
					for (int f = 0; f < Math.min(FEATURES, 6); f++) {
						ohex_alife_institutions_cultural_space_image += get_color_for_trait(
								institution_traits[institution][f]);
					}
				}
				ohex_alife_institutions_cultural_space_image = "#" + ohex_alife_institutions_cultural_space_image;

				// Alife Institutions
				if ((institution != EMPTY && institutionsN[institution] <= 1 ) || institution == EMPTY) {
				//if ( institution == EMPTY) {
					alife_institution_ohex = "#000000";
				} else {
					alife_institution_ohex = "#ffffff";
				}

				alife_insititutions_image.setRGB(r, c, Color.decode(alife_institution_ohex).getRGB());
				alife_institutional_cultural_space_image.setRGB(r, c,
						Color.decode(ohex_alife_institutions_cultural_space_image).getRGB());
				institutonal_cultural_association_image.setRGB(r, c,
						Color.decode(institutonal_cultural_association_ohex).getRGB());

			}
		}

		CulturalSimulator.set_institutional_cultural_association(institutonal_cultural_association_image);
		CulturalSimulator.set_alife_institutions(alife_insititutions_image);
		CulturalSimulator.set_alife_institutional_cultural_space(alife_institutional_cultural_space_image);
	}

	/**
	 * This looks for a non currently occupied institution near the given
	 * coordinates.
	 * 
	 * @param r
	 *            row of the institution
	 * @param c
	 *            column of the institution
	 * @return the institution that is not occupied
	 */
	private int search_free_institution(int r, int c) {
		int x = 0, y = 0, dx = 0, dy = -1;
		int t = Math.max(ROWS, COLS);
		int maxI = (t * 2) * (t * 2);

		for (int i = 0; i < maxI; i++) {
			if ((-1 < r + x) && (r + x < ROWS) && (-1 < c + y) && (c + y < COLS)) {
				if (institutionsN[(r + x) * COLS + (c + y)] == 0) {
					return (r + x) * COLS + (c + y);
				}
			}

			if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
				t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
		}

		// This should never happen, though right it is not important anymore
		// because,
		// the centers are now calculated based on the individuals that belongs
		// to an
		// institution
		for (int r1 = 0; r1 < ROWS; r1++) {
			for (int c1 = 0; c1 < COLS; c1++) {
				if (institutionsN[r1 * COLS + c1] == 0
						|| institutionsN[r1 * COLS + c1] == 1 && r1 * COLS + c1 == institutions[r1][c1]) {
					return r * COLS + c;
				}
			}
		}

		// This should never happen either
		return -99999;
	}

}
