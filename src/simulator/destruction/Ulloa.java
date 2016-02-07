package simulator.destruction;

import java.awt.Color;
import java.awt.image.BufferedImage;

import simulator.CulturalSimulator;
import simulator.previous.Axelrod;

/**
 * Based on FlacheExperiment1 this class implements:
 * 1. Homophily
 * 2. Probabilistic change confronting agents homophily and institution homophily 
 * (and an alpha associated)
 * 3. Probabilistic institutional change according to homophily between neighbor's
 * culture and the agent's (and an alpha prime associated)
 * 
 * This corresponds to the PlosOne code corresponding to the Experiments A and C
 * 
 * @author tico
 *
 */
public class Ulloa extends Axelrod {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6739780243602561128L;

	/**
	 * Nationality of each culture
	 */
	protected int institutions[][] = null;
	
	/**
	 * Possible cultures
	 */
	protected int [][] institution_beliefs = null;
	protected int [] institutionsN = null;
	
	/**
	 * Metrics for my own implementation
	 */
	private int culturesU;
	private int biggest_clusterU;

	/**
	 * This are variables that I use inside run_iteration, declared
	 * as fields for efficiency, to avoid re-declaration, all of them
	 * are transient in order to avoid them to be saved in the serialized
	 * object. 
	 */
	protected transient int c;
	protected transient int r;
	protected transient int n;
	protected transient int nr;
	protected transient int nc;
	protected transient int institution;
	protected transient int neighbors_institution;
	protected transient int mismatchesN;
	protected transient int institution_overlap;
	protected transient int neighbors_institution_overlap;
	protected transient int selected_feature;
	protected transient int selected_trait;
	protected transient int institution_trait;
	protected transient int neighbors_institution_trait;
	protected transient double institution_resistance;
	protected transient float institutional_factor;
	

	@Override
	public void setup() {
		super.setup();
		
		institutions = new int[ROWS][COLS];
		
		institution_beliefs = new int[ROWS*COLS][FEATURES];
		institutionsN = new int[ROWS*COLS];
		
		// Initialize cultures and nationalities
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				institutions[r][c] = r * ROWS + c;
				institutionsN[r * ROWS + c] = 1;
				for (int f = 0; f < FEATURES; f++) {
					institution_beliefs[r * ROWS + c][f] = -1;
				}
			}
		}
		
	}

	@Override
	protected void reset(){
		super.reset();
		institutions = null;
		institution_beliefs = null;
		institutionsN = null;
	}


	@Override
	public void run_iteration() {

		for (int ic = 0; ic < CHECKPOINT; ic++) {
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
				
				// get the number of identical traits between the agent and its institution					
				institution_overlap = 0;		
				
				// get the number of identical traits between the agent and its neighbors's institution					
				neighbors_institution_overlap = 0;
				
				for (int f = 0; f < FEATURES; f++) {
					if (beliefs[r][c][f] != beliefs[nr][nc][f]) {
						mismatches[mismatchesN] = f;
						mismatchesN++;
					}
					if (beliefs[r][c][f] == institution_beliefs[neighbors_institution][f]) {
						neighbors_institution_overlap++;
					}
					if (beliefs[r][c][f] == institution_beliefs[institution][f]) {
						institution_overlap++;
					}
				}

				// if arrive here by selection error, there is still a chance of influence the institution
				if (mismatchesN == 0 ) {  
					selected_feature = rand.nextInt(FEATURES);
				} else {
					selected_feature = mismatches[rand.nextInt(mismatchesN)];
				}
				
				// select the traits in play (agent's, neighbor's and institution's)
				selected_trait = beliefs[nr][nc][selected_feature];
				institution_trait = institution_beliefs[institution][selected_feature];
				institution_trait = institution_beliefs[institution][selected_feature];
				neighbors_institution_trait = institution_beliefs[neighbors_institution][selected_feature];
				

				
				// When there is no institutional conflict because the institution favors the selected trait
				// or because the trait is already different, then just use homophily
				if (selected_trait == institution_trait || 
						institution_trait != -1 && beliefs[r][c][selected_feature] != institution_trait){
					
					// check if there is actual interaction checking against the homophily and 
					// considering the selection error. The present formula integrates homophily
					// and selection error into one probability. 
					if (rand.nextFloat() <  
							( ((FEATURES - mismatchesN) / (float) FEATURES) * (1-SELECTION_ERROR) + 
							(              mismatchesN  / (float) FEATURES) * SELECTION_ERROR  ) ) {

						//////////////////////
						// CHANGE THE TRAIT //
						//////////////////////
						beliefs[r][c][selected_feature] = selected_trait;
					}
						
				} else {
					
					// Probability of interaction taking into account selection error
					float prob_int = ( ((FEATURES - mismatchesN) / (float) FEATURES) * (1-SELECTION_ERROR) + 
									   (            mismatchesN  / (float) FEATURES) * SELECTION_ERROR  );
					
					// Calculate the institutional resistance
					institution_resistance = ALPHA * institution_overlap / (float) FEATURES;
					
					// check if there is actual interaction 
					if (rand.nextFloat() >= institution_resistance / (prob_int * BETA + institution_resistance) ) {
					
						
						// at this point, we are sure that the agent is losing similarity with the institution
						// if the new trait is the same of the institution trait, and the current 
						// agent's trait is different from the institution then the cultural overlap 
						// will increase
						if (institution_trait == selected_trait && 
								beliefs[r][c][selected_feature] != institution_trait) {
							 institution_overlap++;
						} 
						//otherwise, if the new trait institutiont from the institution trait, and 
						// the current agent's trait is the same of the institution then the cultural 
						// overlap will decrease
						else if (institution_trait != selected_trait && 
								beliefs[r][c][selected_feature] == institution_trait) {
							institution_overlap--;
						}
													
						
						// if the new trait is the same of the institution trait, and the current 
						// agent's trait is different from the institution then the institutional overlap 
						// will increase
						if (neighbors_institution_trait == selected_trait && 
								beliefs[r][c][selected_feature] != neighbors_institution_trait) {
							 neighbors_institution_overlap++;
						} 
						//otherwise, if the new trait is different from the institution trait, and 
						// the current agent's trait is the same of the institution then the institutional 
						// overlap will decrease
						else if (neighbors_institution_trait != selected_trait && 
								beliefs[r][c][selected_feature] == neighbors_institution_trait) {
							neighbors_institution_overlap--;
						}
						
						// if the selected feature of the neighbor's institution hasn't been
						// assigned yet, then it is an opportunity to be more similar
						if (institution_beliefs[neighbors_institution][selected_feature] == -1) {
							neighbors_institution_overlap++;
						}						
						

						//////////////////////
						// CHANGE THE TRAIT //
						//////////////////////
						// we change the trait after adjusting the overlaps //
						beliefs[r][c][selected_feature] = selected_trait;
					
						// when the agent doesn't have any similarity with the institutions then
						// he loses his identity and accept the trait. This also avoid divisions 
						// by 0 in the next condition.
						if (institution_overlap == 0 && neighbors_institution_overlap == 0) {
							// Nothing happen when the agent is not similar to any of the two institutions
						}						
						// if there is no institutional shock (current trait is different to its institution's), 
						// accept the change
						else {
							institutional_factor = institution_overlap * ALPHA_PRIME;
						
							if (rand.nextFloat() >= institutional_factor / 
									(float) neighbors_institution_overlap * BETA_PRIME +  
									institutional_factor) {
									
								// if the institutions are different, then institution change to its neighbors
								if (institution != neighbors_institution) {
									// its institution lost a citizen
									institutionsN[institution]--;
									institutions[r][c] = neighbors_institution;
									institutionsN[neighbors_institution]++;	
								} // END of different institution
								
								// if there is no trait selected for the selected feature, then make the
								// selected trait part of the institution
								if (institution_beliefs[neighbors_institution][selected_feature] == -1) {
									institution_beliefs[neighbors_institution][selected_feature] = selected_trait;
								} // END of add a institutional trait to institution
		
							} // END of institution change
							
						} // END of else
						
					} // END of institutional shock
					
				} // END of else
				
				
				// mutation
				if ( rand.nextFloat() >= 1 - MUTATION ) {
					beliefs[r][c][rand.nextInt(FEATURES)] = rand.nextInt(TRAITS);
				}
				
			} // END of total agents
			
		} // END of checkpoint
			

	} // END of run_experiment

	
	/**
	 * Search for the biggest culture and counts the number of cultures so far
	 */
	private void count_clustersU(){
		biggest_clusterU = 0;
		culturesU = 0;
		for (int i = 0; i < institutionsN.length; i++) {
			if (institutionsN[i] > 0) {
				culturesU++;
				if (institutionsN[i] > biggest_clusterU){
					biggest_clusterU = institutionsN[i];					
				}
			}
		}
	}

	@Override
	protected String results() {
		count_clustersU();
		return super.results();
	}

	@Override
	public String get_results() {
		return  IDENTIFIER + "," +
				new java.sql.Timestamp(startTime) + "," +
				((endTime == 0) ? (System.currentTimeMillis() - startTime) : (endTime - startTime)) + "," +
				ITERATIONS + "," +  
				CHECKPOINT + "," +  
				TYPE + "," +  
				ROWS + "," +
				COLS + "," +  
				FEATURES + "," +  
				TRAITS + "," +  
				RADIUS + "," +  
				ALPHA + "," +
				ALPHA_PRIME + "," +
				FREQ_DEM + "," +
				FREQ_PROP + "," +
				MUTATION + "," +  
				SELECTION_ERROR + "," +
				iteration * CHECKPOINT+ "," +
				cultureN  + "," +
				(float) cultureN / TOTAL_AGENTS + "," +
				biggest_cluster + "," +
				(float) biggest_cluster / TOTAL_AGENTS + "," + 
				culturesU  + "," +
				(float) culturesU / TOTAL_AGENTS + "," +
				biggest_clusterU + "," +
				(float) biggest_clusterU / TOTAL_AGENTS +  "\n";				
	}
	
	protected void print_beliefs_spaces(){
		super.print_beliefs_spaces();
		print_alife_institutional_beliefs_space();
		print_alife_institutions();
		print_institutional_beliefs_association();
	}
	

	protected void print_institutional_beliefs_association(){
		BufferedImage image = new BufferedImage(ROWS, COLS, BufferedImage.TYPE_INT_RGB);
		int institution = 0;
		String ohex = "";
		
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				
				institution = institutions[r][c];
				ohex = "";
				for (int f = 0; f < FEATURES; f++) {
					if (institution_beliefs[institution][f] == -1){
						ohex += Integer.toHexString(15);
					} else if (institution_beliefs[institution][f] == -2){
						ohex += Integer.toHexString(0);
					} else {
						ohex += Integer.toHexString(institution_beliefs[institution][f]+1);
					}
				}
				ohex = "#" + ohex;
				
				image.setRGB(r, c, Color.decode(ohex).getRGB());
			}
		}
		
		CulturalSimulator.set_institutional_beliefs_association(image);
	}
	
	
	protected void print_alife_institutional_beliefs_space(){
		BufferedImage image = new BufferedImage(ROWS, COLS, BufferedImage.TYPE_INT_RGB);
		int institution = 0;
		String ohex = "";
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				ohex = "";
				institution = r*COLS+c;
				if (institutionsN[institution] == 0){
					ohex = "#000000";
				} else {
					for (int f = 0; f < FEATURES; f++) {
						if (institution_beliefs[institution][f] == -1){
							ohex += Integer.toHexString(15);
						} else {
							ohex += Integer.toHexString(institution_beliefs[r*COLS+c][f]+1);	
						}
					}
					ohex = "#" + ohex;
				}
				
				image.setRGB(r, c, Color.decode(ohex).getRGB());
			}
		}
		
		CulturalSimulator.set_alife_institutional_beliefs_space(image);
	}
	
	
	
	

	protected void print_alife_institutions(){
		BufferedImage image = new BufferedImage(ROWS, COLS, BufferedImage.TYPE_INT_RGB);
		int institution = 0;
		String ohex;
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLS; c++) {
				ohex = "";
				institution = r*COLS+c;
				
				if (institutionsN[institution] == 0){
					ohex = "#000000";
				} else {
					ohex = "#ffffff";
				}
				
				image.setRGB(r, c, Color.decode(ohex).getRGB());
			}
		}
		
		CulturalSimulator.set_alife_institutions(image);
		
	}

}
