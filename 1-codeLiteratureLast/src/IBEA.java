import CalFitness.Fitness;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.*;
import EnvironmentalSelection.Selection;



public class IBEA implements Population {
	public int population_num;
	public int evolution_num;

	List<Individual> pt_list = new ArrayList<>();
	List<Individual> qt_list = new ArrayList<>();
	List<Individual> rt_list = new ArrayList<>();

	public List<List<Individual>> fronts = new ArrayList<>();

	//有参构造器
	public IBEA(int population_num, int evolution_num) {
		this.population_num = population_num;
		this.evolution_num = evolution_num;
	}

	//调用Jar包计算适应度值
	public double[] calculateFitness(List<Individual> population) throws MWException, IOException {
	    List<Double> objList = new ArrayList<>();
        Object[] array_obj;
        MWNumericArray gCovNow = new MWNumericArray();
        Fitness fitness = new Fitness();
        for (int i = 0; i < population.size(); i++){
            objList.add(population.get(i).objectives.get(0));
            objList.add(population.get(i).objectives.get(1));
            objList.add(population.get(i).objectives.get(2));
        }

        array_obj = objList.toArray();
        gCovNow = new MWNumericArray(array_obj, MWClassID.DOUBLE);
        Object[] fitnessResult = null;
        fitnessResult = fitness.CalFitness(1,gCovNow, 0.05);
		MWNumericArray fitnessResultArray = (MWNumericArray) fitnessResult[0];
		double[][] allFitenss = (double[][])fitnessResultArray.toDoubleArray();
		for (int i = 0; i < allFitenss[0].length; i++){
			allFitenss[0][i] = 0 - allFitenss[0][i];
		}
		fitness.dispose();
		return allFitenss[0];

	}

	@Override
	public List<Individual> init_solution(List<TAOG>taog_list, TAOG taog_sum, List<Integer> state) throws IOException {
		return Algorithm.init_solution(taog_list, taog_sum, population_num,state);
	}

	@Override
	public void evolution(List<TAOG> taog_list, TAOG taog_sum, List<Individual> now_all_solutions) throws MWException, IOException {

        Selection selection = new Selection();
		for (int i = 0; i < evolution_num; i++) {
			//计算经过交叉变异之后的种群的fitness
			double[] all_fitness = calculateFitness(pt_list);
            qt_list.clear();
            //先生成子代
			for (int j = 0; j < population_num; j++) {
				//确定了适应度值后，要开始进行二进制竞标
				int parents[]=new int[2];
				Algorithm.binary_tournament_selection_IBEA(pt_list, parents,all_fitness);
				qt_list.addAll(Algorithm.variation(pt_list.get(parents[0]), pt_list.get(parents[1]), 1.0, taog_sum));

			}
            //父代和子代合并
            combine_pt_and_qt_to_rt();

			now_all_solutions.addAll(rt_list);

            List<Double> rtList = new ArrayList<>();
            Object[] array_rt;
            MWNumericArray gCovRt = new MWNumericArray();
            for (int j = 0; j < rt_list.size(); j++){
                rtList.add(rt_list.get(j).objectives.get(0));
                rtList.add(rt_list.get(j).objectives.get(1));
                rtList.add(rt_list.get(j).objectives.get(2));
            }
            array_rt = rtList.toArray();
            gCovRt = new MWNumericArray(array_rt, MWClassID.DOUBLE);

            //从合并的子代中选择
            Object[] selctionResult = null;
            selctionResult = selection.EnvironmentalSelection(1, gCovRt, population_num, 0.05);
            MWNumericArray selctionResultArray = (MWNumericArray) selctionResult[0];
            //返回的是从1开始的，记得在List中减一
            double[][] goodInd = (double[][])selctionResultArray.toDoubleArray();

            List<Integer> good_index = new ArrayList<>();
            for (int j = 0; j < goodInd[0].length; j++){
				good_index.add((int) goodInd[0][j] - 1);
			}
            //在找到好解之后，将其作为下一代的父代来完成进化
			int flag = 0;
			for (Iterator<Individual> iterator = rt_list.iterator(); iterator.hasNext();) {
				Individual ind = iterator.next();
				if (!good_index.contains(flag)){
					iterator.remove();
				}
				flag ++;
			}


			//将筛选过后的个体替换到pt_list中
			pt_list.clear();
			pt_list.addAll(rt_list);
			//System.out.println("------------");

        }
	}

	public void evolutionPresearch(List<TAOG> taog_list, TAOG taog_sum,int presearchNum) throws MWException, IOException {

		Selection selection = new Selection();
		for (int i = 0; i < presearchNum; i++) {
			//计算经过交叉变异之后的种群的fitness
			double[] all_fitness = calculateFitness(pt_list);
			qt_list.clear();
			//先生成子代
			for (int j = 0; j < population_num; j++) {
				//确定了适应度值后，要开始进行二进制竞标
				int parents[]=new int[2];
				Algorithm.binary_tournament_selection_IBEA(pt_list, parents,all_fitness);
				qt_list.addAll(Algorithm.variation(pt_list.get(parents[0]), pt_list.get(parents[1]), 1.0, taog_sum));

			}
			//父代和子代合并
			combine_pt_and_qt_to_rt();

			List<Double> rtList = new ArrayList<>();
			Object[] array_rt;
			MWNumericArray gCovRt = new MWNumericArray();
			for (int j = 0; j < rt_list.size(); j++){
				rtList.add(rt_list.get(j).objectives.get(0));
				rtList.add(rt_list.get(j).objectives.get(1));
				rtList.add(rt_list.get(j).objectives.get(2));
			}
			array_rt = rtList.toArray();
			gCovRt = new MWNumericArray(array_rt, MWClassID.DOUBLE);

			//从合并的子代中选择
			Object[] selctionResult = null;
			selctionResult = selection.EnvironmentalSelection(1, gCovRt, population_num, 0.05);
			MWNumericArray selctionResultArray = (MWNumericArray) selctionResult[0];
			//返回的是从1开始的，记得在List中减一
			double[][] goodInd = (double[][])selctionResultArray.toDoubleArray();

			List<Integer> good_index = new ArrayList<>();
			for (int j = 0; j < goodInd[0].length; j++){
				good_index.add((int) goodInd[0][j] - 1);
			}
			//在找到好解之后，将其作为下一代的父代来完成进化
			int flag = 0;
			for (Iterator<Individual> iterator = rt_list.iterator(); iterator.hasNext();) {
				Individual ind = iterator.next();
				if (!good_index.contains(flag)){
					iterator.remove();
				}
				flag ++;
			}
			//System.out.println("------------");

			//将筛选过后的个体替换到pt_list中
			pt_list.clear();
			pt_list.addAll(rt_list);

		}
	}

	public void combine_pt_and_qt_to_rt() {
		rt_list.clear();
		for(int i=0;i<pt_list.size();i++) {
			rt_list.add(pt_list.get(i));
		}
		for(int i=0;i<qt_list.size();i++) {
			rt_list.add(qt_list.get(i));
		}
	}

	@Override
	public void output_PF(List<ArrayList<Individual>> pf) throws IOException {
		List<Individual> res = Problem.non_dominate_solution(Problem.delete_same_individual(pt_list));
		pf.add((ArrayList<Individual>) res);
		System.out.println("-----");
	}

	class n_s_class{
		int n;
		List<Integer> s;
		n_s_class(){
			this.n=0;
			this.s=new ArrayList<>();
		};
	}

	public void fast_non_dominated_sort(List<Individual> population) {
		n_s_class pop[]=new n_s_class[population.size()];
		for(int i=0;i<population.size();i++) {
			pop[i]=new n_s_class();
		}
		for (int i = 0; i < population.size()-1; i++) {
			for (int j = i + 1; j < population.size(); j++) {
				//如果i个体支配j
				int dominate_relation = Problem.is_pareto_dominated(population.get(i), population.get(j));
				if (1 == dominate_relation) {
					pop[i].s.add(j);
					pop[j].n++;
				}
				//如果j个体支配i
				else if (2 == dominate_relation) {
					pop[j].s.add(i);
					pop[i].n++;
				}
			}
		}
		// 第一层,n=0,找出那些不被任何个体支配的个体
		List<List<Integer>> fronts_temp = new ArrayList<>();
		List<Integer> level_0 = new ArrayList<>();
		for (int i = 0; i < pop.length; i++) {
			if (pop[i].n == 0) {
				level_0.add(i);
			}
		}
		if (level_0.isEmpty()) {
			System.out.println("非支配排序第一层为null!");
			System.exit(1);
		}
		fronts_temp.add(level_0);

		int level_no = 0;
		while (fronts_temp.get(level_no).size()!=0) {// 是否所有个体分等级
			List<Integer> level_temp = new ArrayList<>();
			for (int i = 0; i < fronts_temp.get(level_no).size(); i++) {// F0中的每个个体
				for (int j = 0; j < pop[fronts_temp.get(level_no).get(i)].s.size(); j++) {
					pop[pop[fronts_temp.get(level_no).get(i)].s.get(j)].n--;
					// 该层中每个个体支配的个体的n值-1
					if (pop[pop[fronts_temp.get(level_no).get(i)].s.get(j)].n == 0) {
						level_temp.add(pop[fronts_temp.get(level_no).get(i)].s.get(j));
					}
				}
			}
			fronts_temp.add(level_temp);
			level_no = level_no + 1;
		}
		fronts_temp.remove(fronts_temp.size()-1);

		this.fronts.clear();
		int sum=0;
		for (int i = 0; i < fronts_temp.size(); i++) {
			List<Individual> list = new ArrayList<>();
			sum=sum+fronts_temp.get(i).size();
			for (int j = 0; j < fronts_temp.get(i).size(); j++) {
				list.add(population.get(fronts_temp.get(i).get(j)));
			}
			this.fronts.add(list);
			if(sum>=this.population_num) {
				break;
			}

		}
	}

	public List <Individual> sort_individual_by_crowding_distance(List <Individual> population){
		Map<Integer, Double> individual_crowding_distance = new TreeMap<Integer, Double>();
		for(int i=0;i<population.size();i++) {
			individual_crowding_distance.put(i,population.get(i).crowding_distance);
		}
		Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer, Double>>() {
			@Override
			public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
				// TODO Auto-generated method stub
				if(o1.getValue()-o2.getValue()>0) {
					return -1;
				}
				else if(o1.getValue()-o2.getValue()<0) {
					return 1;
				}
				else {
					return 0;
				}
			}

		};
		// map转换成list进行排序
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(individual_crowding_distance.entrySet());
		// 排序
		Collections.sort(list,valueComparator);

		List <Individual> result=new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			result.add(population.get(list.get(i).getKey()));
		}
		return result;
	}

}
