import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Algorithm {

	public static double crossoverProbability=0.9;
	private static final double EPS = 1.0e-14;

	//此处完成种群的初始化
	public static List<Individual> init_solution(List<TAOG> taog_list, TAOG taog_sum,
									   int population_num,List<Integer> state) throws IOException {
		//返回的初始种群
		List<Individual> pop = new ArrayList<>();
		//根据种群大小逐个初始化
		for (int i = 0; i < population_num; i++) {
			//new一个对象
			Individual ind = new Individual(taog_list, taog_sum);
			//根据当前环境的状态
			ind.individual_initialize(state);
			pop.add(ind);
		}
		//返回初始种群
		return pop;

	}

	//二进制锦标竞赛选择
	public static void binary_tournament_selection(List<Individual> pt_list,int parents[], NSGAII pop) {
		for(int i=0;i<2;i++) {
			int random[]=new int[2];
			int rank[] = new int[2];
			double distance[] = new double[2];
			random[0]=new Random().nextInt(pt_list.size());
			random[1]=new Random().nextInt(pt_list.size());
			while(random[0]==random[1]) {
				random[1]=new Random().nextInt(pt_list.size());
			}
			for (int j = 0; j < 2; j++){
				//先获取二者的等级喝拥挤度距离
				for (int k = 0; k < pop.fronts.size(); k++){
					if (pop.fronts.get(k).contains(pop.pt_list.get(random[j]))){
						rank[j] = k;
						break;
					}
				}
				distance[j] = pop.pt_list.get(random[j]).crowding_distance;
			}

			//先比较二者的等级排序,如果等级相同，则比较拥挤度
			if (rank[0] < rank[1]){
				parents[i] = random[0];
			}else if (rank[0] > rank[1]){
				parents[i] = random[1];
			}else {
				if (distance[0] >= distance[1]){
					parents[i] = random[0];
				}else {
					parents[i] = random[1];
				}
			}
		}
	}

	public static void binary_tournament_selection_IBEA(List<Individual> pt_list,int parents[],double[] all_fitness) {
		for(int i=0;i<2;i++) {
			int random[]=new int[2];
			random[0]=new Random().nextInt(pt_list.size());
			random[1]=new Random().nextInt(pt_list.size());
			while(random[0]==random[1]) {
				random[1]=new Random().nextInt(pt_list.size());
			}

			//先获取二者的适应度值
			double fitnessFirst = all_fitness[random[0]];
			double fitnessSecond = all_fitness[random[1]];

			//比较二者的适应度，这是变为正数修正后的适应度，我们认为这个正数越小越好，即对应真实的负数越大
			if (fitnessFirst < fitnessSecond){
				parents[i] = random[0];
			}else{
				parents[i] = random[1];
			}
		}
	}

	//在完成交叉变异之后，就把解的所有信息都确定下来，确保是可行解
	public static List<Individual> variation(Individual parent1, Individual parent2, double distributionIndex, TAOG taog_sum) {
		//存储交叉或者后的子代个体
		List<Individual> offspring = new ArrayList<Individual>(2);

		//交叉的参数
		double rand;
		double y1, y2, lowerBound, upperBound;
		double c1, c2;
		double alpha, beta, betaq;
		double valueX1, valueX2;

		//变异的参数
		double rnd, delta1, delta2, mutPow, deltaq;
		double y, yl, yu, val, xy;

		double is_cross = new Random().nextDouble();

		double part1_random = new Random().nextDouble();
		double part2_random = new Random().nextDouble();
		double part3_random = new Random().nextDouble();
		double part4_random = new Random().nextDouble();
		double part5_random = new Random().nextDouble();

		//五个part的标志位(0代表未发生variation,1代表做了交叉，2代表做了变异)
		int part1_flag = 0;
		int part2_flag = 0;
		int part3_flag = 0;
		int part4_flag = 0;
		int part5_flag = 0;


		if (is_cross <= crossoverProbability) {

			offspring.add(parent1.copy());
			offspring.add(parent2.copy());

			//第一步判断是否要交叉part1,0.5的概率交叉
			if (part1_random < 0.9){
				part1_flag = 1;
				for (int i = 0; i < parent1.part_1.size(); i++) {
					valueX1 = parent1.part_1.get(i);
					valueX2 = parent2.part_1.get(i);
					double aaa = new Random().nextDouble();
					if (aaa <= 0.5) {
						if (Math.abs(valueX1 - valueX2) > EPS) {
							if (valueX1 < valueX2) {
								y1 = valueX1;
								y2 = valueX2;
							} else {
								y1 = valueX2;
								y2 = valueX1;
							}

							lowerBound = 0;
							upperBound = 1;

							rand = getRandom(0,1001);
							beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

							beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

							//越界的要进行修复(part1是双闭区间)
							c1 = repairValueOfClosed(c1, lowerBound, upperBound);
							c2 = repairValueOfClosed(c2, lowerBound, upperBound);

							if (getRandom(0, 1001) <= 0.5) {
								offspring.get(0).part_1.set(i, c2);
								offspring.get(1).part_1.set(i, c1);
							} else {
								offspring.get(0).part_1.set(i, c1);
								offspring.get(1).part_1.set(i, c2);
							}
						} else {
							offspring.get(0).part_1.set(i, valueX1);
							offspring.get(1).part_1.set(i, valueX2);
						}
					} else {
						offspring.get(0).part_1.set(i, valueX2);
						offspring.get(1).part_1.set(i, valueX1);
					}
				}
			}

			//第一步判断是否要交叉part2,0.5的概率交叉
			if (part2_random < 0.9){
				part2_flag = 1;
				for (int i = 0; i < parent1.part_2.size(); i++) {
					valueX1 = parent1.part_2.get(i);
					valueX2 = parent2.part_2.get(i);
					double aaa = new Random().nextDouble();
					if (aaa <= 0.5) {
						if (Math.abs(valueX1 - valueX2) > EPS) {
							if (valueX1 < valueX2) {
								y1 = valueX1;
								y2 = valueX2;
							} else {
								y1 = valueX2;
								y2 = valueX1;
							}

							lowerBound = 0;
							upperBound = 1;

							rand = getRandom(0,1001);
							beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

							beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

							//越界的要进行修复（part2也是双闭区间）
							c1 = repairValueOfClosed(c1, lowerBound, upperBound);
							c2 = repairValueOfClosed(c2, lowerBound, upperBound);

							if (getRandom(0, 1001) <= 0.5) {
								offspring.get(0).part_2.set(i, c2);
								offspring.get(1).part_2.set(i, c1);
							} else {
								offspring.get(0).part_2.set(i, c1);
								offspring.get(1).part_2.set(i, c2);
							}
						} else {
							offspring.get(0).part_2.set(i, valueX1);
							offspring.get(1).part_2.set(i, valueX2);
						}
					} else {
						offspring.get(0).part_2.set(i, valueX2);
						offspring.get(1).part_2.set(i, valueX1);
					}
				}
			}

			//第一步判断是否要交叉part3,0.5的概率交叉
			if (part3_random < 0.9){
				part3_flag = 1;
				for (int i = 0; i < parent1.part_3.size(); i++) {
					valueX1 = parent1.part_3.get(i);
					valueX2 = parent2.part_3.get(i);
					double aaa = new Random().nextDouble();
					if (aaa <= 0.5) {
						if (Math.abs(valueX1 - valueX2) > EPS) {
							if (valueX1 < valueX2) {
								y1 = valueX1;
								y2 = valueX2;
							} else {
								y1 = valueX2;
								y2 = valueX1;
							}

							lowerBound = 0;
							upperBound = 1;

							rand = getRandom(0,1001);
							beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

							beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

							//越界的要进行修复（part3也是双闭区间）
							c1 = repairValueOfClosed(c1, lowerBound, upperBound);
							c2 = repairValueOfClosed(c2, lowerBound, upperBound);

							if (getRandom(0, 1001) <= 0.5) {
								offspring.get(0).part_3.put(i, c2);
								offspring.get(1).part_3.put(i, c1);
							} else {
								offspring.get(0).part_3.put(i, c1);
								offspring.get(1).part_3.put(i, c2);
							}
						} else {
							offspring.get(0).part_3.put(i, valueX1);
							offspring.get(1).part_3.put(i, valueX2);
						}
					} else {
						offspring.get(0).part_3.put(i, valueX2);
						offspring.get(1).part_3.put(i, valueX1);
					}
				}
			}

//			//在确定了子代个体的前三个part之后，将拆卸树、任务集合以及操作者和工作站的对应关系确定下来
//			for (int j = 0; j < 2; j++){
//				offspring.get(j).determin_DT_child();
//				offspring.get(j).determin_task_child(parent1.state);
//				offspring.get(j).determinOW_child();
//			}

			//第四步交叉part4,part4是取决于前面三个part的
			//如果前面三个部分有一个发生了改变，则part4基于改变后的前三部分重新进行生成part4以及任务和操作者的对应关系
			if (part1_flag == 1 || part2_flag == 1 || part3_flag == 1){

				for (int i = 0; i < 2; i++){
					offspring.get(i).determin_DT_child();
					offspring.get(i).determin_task_child(parent1.state);
					offspring.get(i).determinOW_child();
					offspring.get(i).part_4.clear();
					offspring.get(i).determinTO();
				}
			}
			//如果前面三个部分均未发生变化，则在父代的part4的基础上做小范围的交叉，或者说站内的交叉
			else {
				//复制拆卸树，任务集合，操作者和工作站的对应关系
				for (int i = 0; i < parent1.normal_node_of_DT.size(); i++){
					offspring.get(0).normal_node_of_DT.add(parent1.normal_node_of_DT.get(i));
				}
				for (int i = 0; i < parent1.task_set.size(); i++){
					offspring.get(0).task_set.add(parent1.task_set.get(i));
				}
				for (int i = 0; i < parent1.operator_workstation.size(); i++){
					offspring.get(0).operator_workstation.add(parent1.operator_workstation.get(i));
				}

				for (int i = 0; i < parent2.normal_node_of_DT.size(); i++){
					offspring.get(1).normal_node_of_DT.add(parent2.normal_node_of_DT.get(i));
				}
				for (int i = 0; i < parent2.task_set.size(); i++){
					offspring.get(1).task_set.add(parent2.task_set.get(i));
				}
				for (int i = 0; i < parent2.operator_workstation.size(); i++){
					offspring.get(1).operator_workstation.add(parent2.operator_workstation.get(i));
				}

				//复制interval的信息
				for (int i = 0; i < parent1.interval.size(); i++){
					offspring.get(0).interval.add(parent1.interval.get(i));
					offspring.get(1).interval.add(parent1.interval.get(i));
				}

				//如果前三部分未发生交叉，则先沿用父代的任务和操作者的对应关系
				for(Map.Entry<Integer, Integer> entry: parent1.task_operator.entrySet()){
					offspring.get(0).task_operator.put(entry.getKey(), entry.getValue());
				}
				for(Map.Entry<Integer, Integer> entry: parent2.task_operator.entrySet()){
					offspring.get(1).task_operator.put(entry.getKey(), entry.getValue());
				}

				for (int i = 0; i < 2; i++){
					//根据task_operator确定BW
					for (int j = 0; j < offspring.get(i).normal_node_of_DT.size(); j++){
						int B = offspring.get(i).normal_node_of_DT.get(j);
						int workstation = 3;
						//找某个B节点的任意一个task,看该任务被分配到了哪个工作站，即代表该B节点被分配到了当前工作站
						for (int k = 0; k < offspring.get(i).task_set.size(); k++){
							if (taog_sum.parent_B_of_unit.get(offspring.get(i).task_set.get(k) - 1) == B){
								//当前任务所分配的操作者，以及操作者所在的工作站
								int operator = offspring.get(i).task_operator.get(offspring.get(i).task_set.get(k));
								workstation = offspring.get(i).operator_workstation.get(operator);
								break;
							}
						}
						offspring.get(i).BW.put(B, workstation);
					}
				}

				//对part4进行交叉
				for (int i = 0; i < 2; i++){
					//应该以工作站为单位，在站内进行交叉
					for (int j = 0; j < Problem.workstation_num; j++){
						//要确定站内有哪些任务
						List<Integer> BOfStation = offspring.get(i).getB0fStation(j);
						List<Integer> tasksOfStation = new ArrayList<>();
						for (int k = 0; k < BOfStation.size(); k++){
							tasksOfStation.addAll(offspring.get(i).selected_task_of_B((BOfStation.get(k))));
						}
						//将任务分类三类，只能被工人执行的任务，只能被机器人执行的任务和可以被两种操作者执行的任务
						List<Integer> only_human_tasks = offspring.get(i).sameTypeOperator(tasksOfStation, 0);
						List<Integer> only_robot_tasks = offspring.get(i).sameTypeOperator(tasksOfStation, 1);
						List<Integer> both_human_robot_tasks = offspring.get(i).sameTypeOperator(tasksOfStation, 2);

						//以一定的概率进行交叉(0.5的概率)
//						if (new Random().nextDouble() < 0.5){
							if (only_human_tasks.size() > 2){
								//随机选择两个任务进行交叉
								int random[]=new int[2];
								random[0]=new Random().nextInt(only_human_tasks.size());
								random[1]=new Random().nextInt(only_human_tasks.size());
								while(random[0]==random[1]) {
									random[1]=new Random().nextInt(only_human_tasks.size());
								}
								//互换彼此的操作者
								int operator_0 = offspring.get(i).task_operator.get(only_human_tasks.get(random[0]));
								Double part4_0 = offspring.get(i).part_4.get(only_human_tasks.get(random[0]) - 1);
								int operator_1 = offspring.get(i).task_operator.get(only_human_tasks.get(random[1]));
								Double part4_1 = offspring.get(i).part_4.get(only_human_tasks.get(random[1]) - 1);
								offspring.get(i).task_operator.put(only_human_tasks.get(random[0]), operator_1);
								offspring.get(i).task_operator.put(only_human_tasks.get(random[1]), operator_0);
								//还要交叉part4中的对应值
								offspring.get(i).part_4.set(only_human_tasks.get(random[0]) - 1, part4_1);
								offspring.get(i).part_4.set(only_human_tasks.get(random[1]) - 1, part4_0);
							}
							if (only_robot_tasks.size() > 2){
								//随机选择两个任务进行交叉
								int random[]=new int[2];
								random[0]=new Random().nextInt(only_robot_tasks.size());
								random[1]=new Random().nextInt(only_robot_tasks.size());
								while(random[0]==random[1]) {
									random[1]=new Random().nextInt(only_robot_tasks.size());
								}
								//互换彼此的操作者
								int operator_0 = offspring.get(i).task_operator.get(only_robot_tasks.get(random[0]));
								Double part4_0 = offspring.get(i).part_4.get(only_robot_tasks.get(random[0]) - 1);
								int operator_1 = offspring.get(i).task_operator.get(only_robot_tasks.get(random[1]));
								Double part4_1 = offspring.get(i).part_4.get(only_robot_tasks.get(random[1]) - 1);
								offspring.get(i).task_operator.put(only_robot_tasks.get(random[0]), operator_1);
								offspring.get(i).task_operator.put(only_robot_tasks.get(random[1]), operator_0);
								//还要交叉part4中的对应值
								offspring.get(i).part_4.set(only_robot_tasks.get(random[0]) - 1, part4_1);
								offspring.get(i).part_4.set(only_robot_tasks.get(random[1]) - 1, part4_0);
							}
							if (both_human_robot_tasks.size() > 2){
								//随机选择两个任务进行交叉
								int random[]=new int[2];
								random[0]=new Random().nextInt(both_human_robot_tasks.size());
								random[1]=new Random().nextInt(both_human_robot_tasks.size());
								while(random[0]==random[1]) {
									random[1]=new Random().nextInt(both_human_robot_tasks.size());
								}
								//互换彼此的操作者
								int operator_0 = offspring.get(i).task_operator.get(both_human_robot_tasks.get(random[0]));
								Double part4_0 = offspring.get(i).part_4.get(both_human_robot_tasks.get(random[0]) - 1);
								int operator_1 = offspring.get(i).task_operator.get(both_human_robot_tasks.get(random[1]));
								Double part4_1 = offspring.get(i).part_4.get(both_human_robot_tasks.get(random[1]) - 1);
								offspring.get(i).task_operator.put(both_human_robot_tasks.get(random[0]), operator_1);
								offspring.get(i).task_operator.put(both_human_robot_tasks.get(random[1]), operator_0);
								//还要交叉part4中的对应值
								offspring.get(i).part_4.set(both_human_robot_tasks.get(random[0]) - 1, part4_1);
								offspring.get(i).part_4.set(both_human_robot_tasks.get(random[1]) - 1, part4_0);
							}
//						}

					}

				}
			}

			//第五步交叉part5
			if (part5_random < 0.9){
				for (int i = 0; i < parent1.part_5.size(); i++) {
					valueX1 = parent1.part_5.get(i);
					valueX2 = parent2.part_5.get(i);
					double aaa = new Random().nextDouble();
					if (aaa <= 0.5) {
						if (Math.abs(valueX1 - valueX2) > EPS) {
							if (valueX1 < valueX2) {
								y1 = valueX1;
								y2 = valueX2;
							} else {
								y1 = valueX2;
								y2 = valueX1;
							}

							lowerBound = 0;
							upperBound = 1;

							rand = getRandom(0,1001);
							beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

							beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
							alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

							if (rand <= (1.0 / alpha)) {
								betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
							} else {
								betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
							}
							c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

							//越界的要进行修复（part5也是双闭区间）
							c1 = repairValueOfClosed(c1, lowerBound, upperBound);
							c2 = repairValueOfClosed(c2, lowerBound, upperBound);

							if (getRandom(0, 1001) <= 0.5) {
								offspring.get(0).part_5.set(i, c2);
								offspring.get(1).part_5.set(i, c1);
							} else {
								offspring.get(0).part_5.set(i, c1);
								offspring.get(1).part_5.set(i, c2);
							}
						} else {
							offspring.get(0).part_5.set(i, valueX1);
							offspring.get(1).part_5.set(i, valueX2);
						}
					} else {
						offspring.get(0).part_5.set(i, valueX2);
						offspring.get(1).part_5.set(i, valueX1);
					}
				}
			}

			//part5确定之后，可以确定某个操作者下属任务的拆卸顺序
			offspring.get(0).determinYita_child();
			offspring.get(1).determinYita_child();

			//计算目标函数值
			offspring.get(0).cal_objectives();
			offspring.get(1).cal_objectives();

			if (offspring.get(0).is_empty_operator() == 1){
				System.out.println("--------");
			}

			if (offspring.get(1).is_empty_operator() == 1){
				System.out.println("----------");
			}

			return offspring;
		}
		//否则做变异
		else {

			//从两个父代中，随机选择一个来进行变异
			int which_mutation = new Random().nextInt() > 0.5 ? 1 : 0;
			Individual solution;
			Individual testInd;
			if (which_mutation == 1){
				solution = parent1.copy();
				testInd = parent1;
			}else {
				solution = parent2.copy();
				testInd = parent2;
			}

			offspring.add(solution);

			//先清空拆卸树等信息，因为五个part变异之后，要重新生成拆卸树等信息
//			offspring.get(0).normal_node_of_DT.clear();
//			offspring.get(0).task_set.clear();
//			offspring.get(0).operator_workstation.clear();
//			offspring.get(0).objectives.clear();
//			offspring.get(0).time_node.clear();
//			offspring.get(0).operator_tasks_desc.clear();

			//第一步变异part1,以0.5的概率变异
			if (part1_random < 0.9){
				part1_flag = 1;
				for (int i = 0; i < solution.part_1.size(); i++) {
					y = solution.part_1.get(i);
					yl = 0;
					yu = 1;
					if (yl == yu) {
						y = yl;
					} else {
						delta1 = (y - yl) / (yu - yl);
						delta2 = (yu - y) / (yu - yl);
						rnd = getRandom(0,1001);
						mutPow = 1.0 / (distributionIndex + 1.0);
						if (rnd <= 0.5) {
							xy = 1.0 - delta1;
							val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = Math.pow(val, mutPow) - 1.0;
						} else {
							xy = 1.0 - delta2;
							val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = 1.0 - Math.pow(val, mutPow);
						}
						y = y + deltaq * (yu - yl);
						//修复（part1是双闭区间）
						y = repairValueOfClosed(y, yl, yu);
					}
					solution.part_1.set(i, y);
				}
			}

			//第二步变异part2
			if (part2_random < 0.9){
				part2_flag = 1;
				for (int i = 0; i < solution.part_2.size(); i++) {
					y = solution.part_2.get(i);
					yl = 0;
					yu = 1;
					if (yl == yu) {
						y = yl;
					} else {
						delta1 = (y - yl) / (yu - yl);
						delta2 = (yu - y) / (yu - yl);
						rnd = getRandom(0,1001);
						mutPow = 1.0 / (distributionIndex + 1.0);
						if (rnd <= 0.5) {
							xy = 1.0 - delta1;
							val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = Math.pow(val, mutPow) - 1.0;
						} else {
							xy = 1.0 - delta2;
							val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = 1.0 - Math.pow(val, mutPow);
						}
						y = y + deltaq * (yu - yl);
						//修复（part2是双闭区间）
						y = repairValueOfClosed(y, yl, yu);
					}
					solution.part_2.set(i, y);
				}
			}

			//第三步变异part3
			if (part3_random < 0.9){
				part3_flag = 1;
				for (int i = 0; i < solution.part_3.size(); i++) {
					y = solution.part_3.get(i);
					yl = 0;
					yu = 1;
					if (yl == yu) {
						y = yl;
					} else {
						delta1 = (y - yl) / (yu - yl);
						delta2 = (yu - y) / (yu - yl);
						rnd = getRandom(0,1001);
						mutPow = 1.0 / (distributionIndex + 1.0);
						if (rnd <= 0.5) {
							xy = 1.0 - delta1;
							val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = Math.pow(val, mutPow) - 1.0;
						} else {
							xy = 1.0 - delta2;
							val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = 1.0 - Math.pow(val, mutPow);
						}
						y = y + deltaq * (yu - yl);
						//修复（part3是双闭区间）
						y = repairValueOfClosed(y, yl, yu);
					}
					solution.part_3.put(i, y);
				}
			}

//			//在确定了子代个体的前三个part之后，将拆卸树、任务集合以及操作者和工作站的对应关系确定下来
//
//			offspring.get(0).determin_DT_child();
//
//			offspring.get(0).determin_task_child(parent1.state);
//
//			offspring.get(0).determinOW_child();

			//第四步变异part4
			if (part1_flag == 1 || part2_flag == 1 || part3_flag == 1){
				for (int i = 0; i < offspring.size(); i++){
					offspring.get(0).normal_node_of_DT.clear();
					offspring.get(0).task_set.clear();
					offspring.get(0).operator_workstation.clear();
					offspring.get(0).objectives.clear();
					offspring.get(0).time_node.clear();
					offspring.get(0).operator_tasks_desc.clear();

					offspring.get(0).part_4.clear();
					offspring.get(0).BW.clear();
					offspring.get(0).task_operator.clear();
					offspring.get(0).determin_DT_child();
					offspring.get(0).determin_task_child(parent1.state);
					offspring.get(0).determinOW_child();
					//采用原始的方式，即重新生成part4，并确定任务和操作者的对应关系
					offspring.get(i).determinTO();
				}
			}
			//如果前面三个部分均未发生变化，则在父代的part4的基础上做小范围的变异，或者说站内的变异
			else {
				//复制拆卸树，任务集合，操作者和工作站的对应关系
				for (int i = 0; i < testInd.normal_node_of_DT.size(); i++){
					offspring.get(0).normal_node_of_DT.add(testInd.normal_node_of_DT.get(i));
				}
				for (int i = 0; i < testInd.task_set.size(); i++){
					offspring.get(0).task_set.add(testInd.task_set.get(i));
				}
				for (int i = 0; i < testInd.operator_workstation.size(); i++){
					offspring.get(0).operator_workstation.add(testInd.operator_workstation.get(i));
				}

				//复制interval的信息
				for (int i = 0; i < testInd.interval.size(); i++){
					offspring.get(0).interval.add(testInd.interval.get(i));
				}

				//如果前三部分未发生交叉，则先沿用父代的任务和操作者的对应关系
				for(Map.Entry<Integer, Integer> entry: testInd.task_operator.entrySet()){
					offspring.get(0).task_operator.put(entry.getKey(), entry.getValue());
				}

				for (int i = 0; i < 1; i++){
					//根据task_operator确定BW
					for (int j = 0; j < offspring.get(i).normal_node_of_DT.size(); j++){
						int B = offspring.get(i).normal_node_of_DT.get(j);
						int workstation = 3;
						//找某个B节点的任意一个task,看该任务被分配到了哪个工作站，即代表该B节点被分配到了当前工作站
						for (int k = 0; k < offspring.get(i).task_set.size(); k++){
							if (taog_sum.parent_B_of_unit.get(offspring.get(i).task_set.get(k) - 1) == B){
								//当前任务所分配的操作者，以及操作者所在的工作站
								int operator = offspring.get(i).task_operator.get(offspring.get(i).task_set.get(k));
								workstation = offspring.get(i).operator_workstation.get(operator);
								break;
							}
						}
						offspring.get(i).BW.put(B, workstation);
					}
				}

				//沿用父代的拆卸树、任务集合、操作者和工作站的对应关系这三点
				offspring.get(0).objectives.clear();
				offspring.get(0).time_node.clear();
				offspring.get(0).operator_tasks_desc.clear();

				for (int i = 0; i < 1; i++){
					//应该以工作站为单位，在站内进行变异
					for (int j = 0; j < Problem.workstation_num; j++){
						//要确定站内有哪些任务
						List<Integer> BOfStation = offspring.get(i).getB0fStation(j);
						List<Integer> tasksOfStation = new ArrayList<>();
						for (int k = 0; k < BOfStation.size(); k++){
							tasksOfStation.addAll(offspring.get(i).selected_task_of_B((BOfStation.get(k))));
						}
						//站内的机器人集合
						List<Integer> robots_list = offspring.get(i).specificOperatorOfStation(j, 1);
						//站内的工人集合
						List<Integer> human_list = offspring.get(i).specificOperatorOfStation(j, 0);
						//将任务分类三类，只能被工人执行的任务，只能被机器人执行的任务和可以被两种操作者执行的任务
						List<Integer> only_human_tasks = offspring.get(i).sameTypeOperator(tasksOfStation, 0);
						List<Integer> only_robot_tasks = offspring.get(i).sameTypeOperator(tasksOfStation, 1);
						List<Integer> both_human_robot_tasks = offspring.get(i).sameTypeOperator(tasksOfStation, 2);

						//针对站内的每个任务，以一定的概率进行变异
						//首先是只能被工人执行的任务
						for (int k = 0; k < only_human_tasks.size(); k++){
							//以0.5的概率变异
							if (new Random().nextDouble() < 0.5){
								int operator_0 = offspring.get(i).task_operator.get(only_human_tasks.get(k));
								//随机选择一个操作者(符合条件的)
								//选择机器人
								int mutation_operator;
								if (operator_0 >= 0 && operator_0 < 24){
									mutation_operator = robots_list.get(new Random().nextInt(robots_list.size()));
								}
								//选择工人
								else {
									mutation_operator = human_list.get(new Random().nextInt(human_list.size()));
								}

								//并生成相应的epsilon
								if (offspring.get(0).interval.size() == 0){
									System.out.println("-------------");
								}
								//并生成相应的epsilon
								double lb = offspring.get(0).interval.get(mutation_operator);
								double ub = offspring.get(0).interval.get(mutation_operator + 1);
								//在这个范围内生成随机数 [下限，上限)
								Double part4_0 = Individual.getRandomNum((int) (lb*1000),(int) (ub*1000));

								//对task_operator进行更改
								offspring.get(0).task_operator.put(only_human_tasks.get(k), mutation_operator);
								//对part4进行更改
								offspring.get(0).part_4.set(only_human_tasks.get(k) - 1, part4_0);
							}
						}
						//紧接着是只能被机器人执行的任务
						for (int k = 0; k < only_robot_tasks.size(); k++){
							//以0.5的概率变异
							if (new Random().nextDouble() < 0.5){
								int operator_0 = offspring.get(i).task_operator.get(only_robot_tasks.get(k));
								//随机选择一个操作者(符合条件的)
								//选择机器人
								int mutation_operator;
								if (operator_0 >= 0 && operator_0 < 24){
									mutation_operator = robots_list.get(new Random().nextInt(robots_list.size()));
								}
								//选择工人
								else {
									mutation_operator = human_list.get(new Random().nextInt(human_list.size()));
								}
								//并生成相应的epsilon
								if (offspring.get(0).interval.size() == 0){
									System.out.println("-------------");
								}
								double lb = offspring.get(0).interval.get(mutation_operator);
								double ub = offspring.get(0).interval.get(mutation_operator + 1);
								//在这个范围内生成随机数 [下限，上限)
								Double part4_0 = Individual.getRandomNum((int) (lb*1000),(int) (ub*1000));

								//对task_operator进行更改
								offspring.get(0).task_operator.put(only_robot_tasks.get(k), mutation_operator);
								//对part4进行更改
								offspring.get(0).part_4.set(only_robot_tasks.get(k) - 1, part4_0);
							}
						}
						//最后是既能被机器人又能被工人执行的任务
						for (int k = 0; k < both_human_robot_tasks.size(); k++){
							//以0.5的概率变异
							if (new Random().nextDouble() < 0.5){
								int operator_0 = offspring.get(i).task_operator.get(both_human_robot_tasks.get(k));
								//随机选择一个操作者(符合条件的)
								//选择机器人
								int mutation_operator;
								if (operator_0 >= 0 && operator_0 < 24){
									mutation_operator = robots_list.get(new Random().nextInt(robots_list.size()));
								}
								//选择工人
								else {
									mutation_operator = human_list.get(new Random().nextInt(human_list.size()));
								}

								//并生成相应的epsilon
								if (offspring.get(0).interval.size() == 0){
									System.out.println("-------------");
								}
								//并生成相应的epsilon
								double lb = offspring.get(0).interval.get(mutation_operator);
								double ub = offspring.get(0).interval.get(mutation_operator + 1);
								//在这个范围内生成随机数 [下限，上限)
								Double part4_0 = Individual.getRandomNum((int) (lb*1000),(int) (ub*1000));

								//对task_operator进行更改
								offspring.get(0).task_operator.put(both_human_robot_tasks.get(k), mutation_operator);
								//对part4进行更改
								offspring.get(0).part_4.set(both_human_robot_tasks.get(k) - 1, part4_0);
							}
						}
					}
				}
			}

			//第五步变异part5
			if (part5_random < 0.9){
				part5_flag = 1;
				for (int i = 0; i < solution.part_5.size(); i++) {

					y = solution.part_5.get(i);
					yl = 0;
					yu = 1;
					if (yl == yu) {
						y = yl;
					} else {
						delta1 = (y - yl) / (yu - yl);
						delta2 = (yu - y) / (yu - yl);
						rnd = getRandom(0,1001);
						mutPow = 1.0 / (distributionIndex + 1.0);
						if (rnd <= 0.5) {
							xy = 1.0 - delta1;
							val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = Math.pow(val, mutPow) - 1.0;
						} else {
							xy = 1.0 - delta2;
							val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
							deltaq = 1.0 - Math.pow(val, mutPow);
						}
						y = y + deltaq * (yu - yl);
						//修复（part5是双闭区间）
						y = repairValueOfClosed(y, yl, yu);
					}
					solution.part_5.set(i, y);
				}
			}

			//part5确定之后，可以确定某个操作者下属任务的拆卸顺序
			offspring.get(0).determinYita_child();

			//计算目标函数值
			offspring.get(0).cal_objectives();

			if (offspring.get(0).is_empty_operator() == 1){
				System.out.println("-----------");
			}

			return offspring;
		}
	}

	//针对双闭区间的修复
	public static double repairValueOfClosed(double value, double lowerBound, double upperBound) {
		double result = value ;
		if (value < lowerBound) {
			result = getRandom(0, 1001) ;
		}
		if (value > upperBound) {
			result = getRandom(0, 1001) ;
		}

		return result ;
	}

	//针对左闭右开区间的修复
	public static double repairValueOfOpen(double value, double lowerBound, double upperBound) {
		double result = value ;
		if (value < lowerBound) {
			result = getRandom(0, 1000) ;
		}
		if (value >= upperBound) {
			result = getRandom(0, 1000) ;
		}

		return result ;
	}

	//随即生成1个不同的数
	public static double getRandom(int start,int end){
		//2.创建Random对象
		Random r = new Random();

		int num = r.nextInt(end-start) + start;
		BigDecimal a = new BigDecimal(num);
		BigDecimal b = new BigDecimal(1000);
		BigDecimal result = a.divide(b);

		double double_result = result.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

		return double_result;
	}

	//新的二进制竞标方法
	public static void new_tournament_selection(List<Individual> pop, int parents[],int distance_index[]){
		for(int i=0;i<2;i++) {
			int random[]=new int[2];
			//随机选择两个父个体
			random[0]=new Random().nextInt(pop.size());
			random[1]=new Random().nextInt(pop.size());
			while(random[0]==random[1]) {
				random[1]=new Random().nextInt(pop.size());
			}
			//找到适合繁殖的父代，此时判定一个解的好坏，是看它在隐空间里和PF上某个解的距离，距离越小则解越好
			if(Problem.is_near_to_pf(random[0], random[1], distance_index) == 1) {
				parents[i] = random[0];
			}
			else {
				parents[i] = random[1];
			}
		}
	}
}
