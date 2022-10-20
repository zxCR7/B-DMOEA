import com.mathworks.toolbox.javabuilder.MWException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface Population {

	List<Individual> init_solution(List<TAOG>taog_list, TAOG taog_sum, List<Integer> state)throws IOException;

	void evolution(List<TAOG> taog_list, TAOG taog_sum, List<Individual> now_all_solutions) throws MWException, IOException;

	void output_PF(List<ArrayList<Individual>> pf)throws IOException;
}
