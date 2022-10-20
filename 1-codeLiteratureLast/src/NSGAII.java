import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


public class NSGAII implements Population {

	public int population_num;
	public int evolution_num;
	public List<Individual> pt_list = new ArrayList<>();
	public List<Individual> qt_list = new ArrayList<>();
	public List<Individual> rt_list = new ArrayList<>();
	public List<List<Individual>> fronts = new ArrayList<>();
//	public List<Individual> pt_list;
//	public List<Individual> qt_list;
//	public List<Individual> rt_list;
//	public List<List<Individual>> fronts;
	double CROWDING_DISTANCE_MAX=1e12;
	
	double time_begin;
	double time_end;

	//有参构造器
	NSGAII(int population_num, int evolution_num) {
		this.population_num=population_num;
		this.evolution_num=evolution_num;
	}


	@Override
	public List<Individual> init_solution(List<TAOG>taog_list, TAOG taog_sum, List<Integer> state) throws IOException {
		return Algorithm.init_solution(taog_list, taog_sum, population_num,state);
	}

	@Override
	public void evolution(List<TAOG> taog_list, TAOG taog_sum, List<Individual> now_all_solutions){
//		//首先要对初代种群进行非支配排序和拥挤度排序
//		fast_non_dominated_sort(this.pt_list);
//		cal_crowding_distance(this.pt_list);
		for (int i = 0; i < evolution_num; i++) {
			produce_qt_list(taog_list,taog_sum);
			//System.out.println("第" + i + "代进化产生qt！");
			combine_pt_and_qt_to_rt();
			//System.out.println("第" + i + "代进化产生rt！");
			fast_non_dominated_sort(this.rt_list);
			//System.out.println("第" + i + "代进化完成非支配排序！");
			cal_crowding_distance(this.rt_list);

			//把进化过程中产生的所有个体全部保存下来
			now_all_solutions.addAll(this.rt_list);

			//System.out.println("第" + i + "代进化完成拥挤距离计算！");
			select_elite();
			//System.out.println("第" + i + "代进化选择优良个体生成pt！");
		}
	}

	//预搜索的进化
	public void evolutionPresearch(List<TAOG> taog_list, TAOG taog_sum, int presearchNum){
//		//首先要对初代种群进行非支配排序和拥挤度排序
//		fast_non_dominated_sort(this.pt_list);
//		cal_crowding_distance(this.pt_list);
		for (int i = 0; i < presearchNum; i++) {
			produce_qt_list(taog_list,taog_sum);
			//System.out.println("第" + i + "代进化产生qt！");
			combine_pt_and_qt_to_rt();
			//System.out.println("第" + i + "代进化产生rt！");
			fast_non_dominated_sort(this.rt_list);
			//System.out.println("第" + i + "代进化完成非支配排序！");
			cal_crowding_distance(this.rt_list);

			//System.out.println("第" + i + "代进化完成拥挤距离计算！");
			select_elite();
		}
	}

	public void produce_qt_list(List<TAOG> taog_list,TAOG taog_sum) {
		qt_list.clear();
		for(int i=0;i<population_num;i++) {
			int parents[]=new int[2];
			Algorithm.binary_tournament_selection(pt_list, parents, this);
			qt_list.addAll(Algorithm.variation(pt_list.get(parents[0]), pt_list.get(parents[1]), 1.0, taog_sum));
		}
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

	class n_s_class{
		int n;
		List<Integer> s;
		n_s_class(){
			this.n=0;
			this.s=new ArrayList<>();
		};
	}

	public void cal_crowding_distance(List<Individual> population) {
		for(int i=0;i<population.size();i++) {
			population.get(i).crowding_distance = 0.0;
		}

		int result[][]=new int[Problem.numberOfObjectives][population.size()];
		for(int i=0;i<Problem.numberOfObjectives;i++) {
			result[i]=sort_individual_by_objective(population,i);//按升序排列
			population.get(result[i][0]).crowding_distance=CROWDING_DISTANCE_MAX;
			population.get(result[i][population.size()-1]).crowding_distance=CROWDING_DISTANCE_MAX;
			for(int j=1;j<population.size()-1;j++) {
				population.get(result[i][j]).crowding_distance
						= population.get(result[i][j]).crowding_distance + (population.get(result[i][j+1]).objectives.get(i)-population.get(result[i][j-1]).objectives.get(i))/(population.get(result[i][population.size()-1]).objectives.get(i)-population.get(result[i][0]).objectives.get(i));
				//System.out.println(population.get(result[i][j]).crowding_distance);
			}
		}
	}

	public int[] sort_individual_by_objective(List<Individual> individual,int objective_no) {
		Map<Integer, Double> individual_objective_no = new TreeMap<Integer, Double>();
		for(int i=0;i<individual.size();i++) {
			individual_objective_no.put(i,individual.get(i).objectives.get(objective_no));
		}
		Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer, Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
				// TODO Auto-generated method stub
				if(o1.getValue()-o2.getValue()>0) {
					return 1;
				}
				else if(o1.getValue()-o2.getValue()<0) {
					return -1;
				}
				else {
					return 0;
				}
			}

		};
		// map转换成list进行排序
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(individual_objective_no.entrySet());
		// 排序
		Collections.sort(list,valueComparator);

		int[] result=new int[individual.size()];
		for(int i=0;i<list.size();i++) {
			result[i]=list.get(i).getKey();
		}
		return result;
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

	public void select_elite() {
		this.pt_list.clear();
		for (int j = 0; j < fronts.size() - 1; j++) {
			for (int k = 0; k < fronts.get(j).size(); k++) {
				pt_list.add(fronts.get(j).get(k));
			}
		}
		List<Individual> sort_crowding_distance = sort_individual_by_crowding_distance(fronts.get(fronts.size() - 1));
		for (int j = 0; j < sort_crowding_distance.size(); j++) {
			if(pt_list.size()!=population_num) {
				pt_list.add(sort_crowding_distance.get(j));
			}
			else {
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
			public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
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

	@Override
	public void output_PF(List<ArrayList<Individual>> pf) throws IOException{
		List<Individual> res = Problem.non_dominate_solution(Problem.delete_same_individual(this.pt_list));
		pf.add((ArrayList<Individual>) res);
	}

	public void produce_qt_list_RVEA(List<TAOG> taog_list,TAOG taog_sum, List<Integer> indexList, List<List<Individual>> all_pop) {
		qt_list.clear();
		for(int i=0;i<population_num;i++) {
			int parents[]=new int[2];
//			parents[0]=new Random().nextInt(pt_list.size());
//			parents[1]=new Random().nextInt(pt_list.size());
//			while(parents[0]==parents[1]) {
//				parents[1]=new Random().nextInt(pt_list.size());
//			}

			parents[0]=new Random().nextInt(indexList.size()/2);
			parents[1]=new Random().nextInt(indexList.size()/2);
			while(parents[0]==parents[1]) {
				parents[1]=new Random().nextInt(indexList.size()/2);
			}

//			qt_list.addAll(Algorithm.variation(pt_list.get(parents[0]), pt_list.get(parents[1]), 1.0, taog_sum));

			qt_list.addAll(Algorithm.variation(all_pop.get(indexList.get(parents[0]*2)).get(indexList.get(parents[0]*2+1)), all_pop.get(indexList.get(parents[1]*2)).get(indexList.get(parents[1]*2+1)), 1.0, taog_sum));
		}
	}


}
