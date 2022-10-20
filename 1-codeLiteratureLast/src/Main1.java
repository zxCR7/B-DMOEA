
import CalW.GetW;
import TCA.NewTCA;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import getMMD.MMD;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Main1 {
    public static void main(String[] args) throws IOException, MWException {
        
        //开始时间
        double allTimeBegin = System.currentTimeMillis();
        //种群数量
        int population_num = 150;
        //进化代数
        int evolution_num = 30;
        List<TAOG> taog_list=new ArrayList<>();
        TAOG taog_sum=new TAOG();
        read_original_TAOG(taog_sum,taog_list);

        for(int z = 0; z < 10; z++){

            System.out.println("Experiment " + z + " is beginning");

            //使用TCA
            //首先是初始环境
            //建立一个集合存储迭代过程中的当前环境下的种群,这个种群存储的都是引导种群(这个种群里可以认为是进化完成后的种群，比较好的种群),用该种群去参与构建变换矩阵
            List<List<Individual>> good_population1 = new ArrayList<>();
            List<ArrayList<Individual>> pf1 = new ArrayList<ArrayList<Individual>>();

            for (int i = 0; i < 1; i++){

                //开始前将存放所有解的文件夹清空
                String deletePath = "F:\\pfResult\\NSGAII\\TCA\\run"+ (z+1) + "\\"+ + i +"\\all";
                Problem.deleteDir(deletePath);

                System.out.println("Environment " + i );
                //初始化一个种群
                NSGAII pop = new NSGAII(population_num, evolution_num);

                List<Integer> state = new ArrayList<>();
                for (int j = 0; j < Problem.all_state[0].length; j++){
                    state.add(Problem.all_state[0][j]);
                }

                pop.pt_list.addAll(pop.init_solution(taog_list, taog_sum, state));

                //当前环境下的所有解
                List<Individual> now_all_solutions = new ArrayList<>();
                //进化
                pop.evolution(taog_list, taog_sum, now_all_solutions);

                //创建一个种群用来预搜索
                NSGAII samplePop = new NSGAII(population_num, evolution_num);
                samplePop.pt_list.addAll(samplePop.init_solution(taog_list, taog_sum, state));

                //samplePop中的解用来作为源域数据
                good_population1.add(samplePop.pt_list);

                //将来在这儿规范化处理
                pop.output_PF(pf1);

                //保存本次实验的所有的解
                String pathAll = "F:\\pfResult\\NSGAII\\TCA\\run"+ (z+1) +"\\"+ i +"\\all\\"+"all"+i+".txt";
                File fileAll = new File(pathAll);
                if(!fileAll.exists()){
                    fileAll.createNewFile();
                }
                FileWriter fileWriteAll = new FileWriter(fileAll,true);
                String lineAll = System.getProperty("line.separator");
                //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                BufferedWriter dataAll = new BufferedWriter(fileWriteAll);
                for (int j = 0; j < now_all_solutions.size(); j++){
                    dataAll.write("" + now_all_solutions.get(j).objectives.get(0));
                    dataAll.write("\t");
                    dataAll.write("" + now_all_solutions.get(j).objectives.get(1));
                    dataAll.write("\t");
                    dataAll.write("" + now_all_solutions.get(j).objectives.get(2));
                    dataAll.write(lineAll);
                }
                dataAll.close();
                fileWriteAll.close();
                System.out.println(i + "-------");

            }

            //后面变化后的几个环境
            for (int i = 1; i < 8; i++){

                //开始前将存放所有解的文件夹清空
                String deletePath = "F:\\pfResult\\NSGAII\\TCA\\run"+ (z+1) +"\\"+ + i +"\\all";
                Problem.deleteDir(deletePath);

                System.out.println("Environment " + i );
                //初始化一个种群构建变换矩阵
                NSGAII pop = new NSGAII(population_num, evolution_num);

                List<Integer> state = new ArrayList<>();
                for (int j = 0; j < Problem.all_state[i].length; j++){
                    state.add(Problem.all_state[i][j]);
                }
                //当前环境下的初代种群
                pop.pt_list.addAll(pop.init_solution(taog_list, taog_sum, state));

                MWNumericArray gCovHistory = new MWNumericArray();
                MWNumericArray gCovNow = new MWNumericArray();
//                //MMD距离
//                MMD mmd = new MMD();
                //变换矩阵W
                GetW calW = new GetW();
//                //保存mmd距离的集合
//                List<Double> mmdList = new ArrayList<>();
                Object[] array_temp_history;
                Object[] array_temp_now;

                //预搜索
                //pop.evolutionPresearch(taog_list,taog_sum,3);

                //这里就将预搜索得到的种群加到好解集合中，用来构建W矩阵
                good_population1.add(pop.pt_list);

                List<Double> list_temp_now = new ArrayList<>();
                for (int j = 0; j < pop.pt_list.size(); j++){
                    list_temp_now.add(pop.pt_list.get(j).objectives.get(0));
                    list_temp_now.add(pop.pt_list.get(j).objectives.get(1));
                    list_temp_now.add(pop.pt_list.get(j).objectives.get(2));
                }
                array_temp_now = list_temp_now.toArray();

                //将数组转换为矩阵，这是当前环境下的种群
                gCovNow = new MWNumericArray(array_temp_now, MWClassID.DOUBLE);

                int minIndex = i - 1;

                System.out.println("selected environment " + minIndex);

                //最相似环境下的种群
                List<Double> list_temp_similar = new ArrayList<>();
                for (int k = 0; k < good_population1.get(minIndex).size(); k++){
                    list_temp_similar.add(good_population1.get(minIndex).get(k).objectives.get(0));
                    list_temp_similar.add(good_population1.get(minIndex).get(k).objectives.get(1));
                    list_temp_similar.add(good_population1.get(minIndex).get(k).objectives.get(2));
                }
                array_temp_history = list_temp_similar.toArray();
                //将数组转换成矩阵
                gCovHistory = new MWNumericArray(array_temp_history, MWClassID.DOUBLE);
                //此处要获得转换矩阵W
                Object[] W = null;

                System.out.println("Run by zx,"+(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                W = calW.CalW(1,gCovHistory,gCovNow);
                MWNumericArray dataW = (MWNumericArray) W[0];
                //接下来要通过遗传算法解决单目标问题，这个单目标就是距离最小，这个距离就作为适应度值，实际上这部分就是一个单目标优化的问题了
                //先求出初代的适应度（距离值）
                Object[] distance_object = null;

                //创建一个对象用来表示最终的经过迁移学习和单目标优化得到的种群pop_final,假设最后针对这个种群利用NSGAII进化40代
                NSGAII pop_final = new NSGAII(population_num, 30);

    //                List<Integer> pfSample = new ArrayList<>();
    //                Individual.getRandomNumListNew(15,0,pf1.get(minIndex).size(),pfSample);
    //                List<Individual> sort_crowding_distance = pop.sort_individual_by_crowding_distance(pf1.get(minIndex));

                //针对最相似环境下的PF上的每一个解，将当前环境下的种群作为这个单目标优化问题的初始种群，个体离PF上的解的距离作为适应度值
                NSGAII popSingleObj = new NSGAII(population_num, evolution_num);
                for (int k = 0; k < pf1.get(minIndex).size(); k++){
                //for (int k = 0; k < pfSample.size(); k++){
                    //既然对每一个解都要做迭代，那么每次应该有一个初始种群用来做单目标优化
                    popSingleObj.pt_list.clear();
                    popSingleObj.pt_list.addAll(popSingleObj.init_solution(taog_list, taog_sum, state));

                    Object[] array_single_obj;
                    List<Double> list_single_obj = new ArrayList<>();

                    for (int m = 0; m < popSingleObj.pt_list.size(); m++){
                        list_single_obj.add(popSingleObj.pt_list.get(m).objectives.get(0));
                        list_single_obj.add(popSingleObj.pt_list.get(m).objectives.get(1));
                        list_single_obj.add(popSingleObj.pt_list.get(m).objectives.get(2));
                    }
                    array_single_obj = list_single_obj.toArray();
                    //将数组转换为矩阵
                    MWNumericArray gCovSingleObj = new MWNumericArray(array_single_obj, MWClassID.DOUBLE);

                    //最相似环境下的帕累托最优前沿面上的个体
                    Object[] array_pf_similar;
                    List<Double> list_pf_similar = new ArrayList<>();

                    list_pf_similar.add(pf1.get(minIndex).get(k).objectives.get(0));
                    list_pf_similar.add(pf1.get(minIndex).get(k).objectives.get(1));
                    list_pf_similar.add(pf1.get(minIndex).get(k).objectives.get(2));

                    array_pf_similar = list_pf_similar.toArray();
                    //将数组转换成矩阵
                    MWNumericArray gCovPf = new MWNumericArray(array_pf_similar, MWClassID.DOUBLE);

                    //有了相似历史环境下的种群、当前环境下的种群以及相似历史环境下的PF，以及映射矩阵后，可以在隐空间里计算当前环境下的种群距离PF上的解的距离
                    NewTCA TCA = new NewTCA();

                    //result里存储的是计算距离之后，排序的结果，接下来要基于这个排序去选择解作为父代种群去迭代
                    distance_object = TCA.TCA(2,gCovHistory,gCovSingleObj,gCovPf,dataW);

                    MWNumericArray distance_index_array = (MWNumericArray) distance_object[1];

                    MWNumericArray distance_array = (MWNumericArray) distance_object[0];

                    //distance_index[0]数组里存放着index，升序排列的
                    int[][] distance_index = (int[][]) distance_index_array.toIntArray();
                    //distance[0]数组里存放着距离，跟distance_index[0]数组是对应的
                    double[][] distance = (double[][]) distance_array.toDoubleArray();

                    //接下来要进化了，暂定进化3代
                    for (int m = 0; m < 3; m++){
                        //进行二进制竞标选择父代生成子代qt,此处竞标应该根据上面的排序结果,因为适应度值发生了变化
                        popSingleObj.qt_list.clear();
                        for (int n = 0; n < population_num; n++){
                            int parents[]=new int[2];
                            //随机选择两个父本进行交叉变异产生一个较好个体 进行非支配排序，对于ENSGAII来说，constraintKind为0
                            //找到适合繁殖的父代，此处的适合繁殖应当是基于前面计算的距离来判定，距离越小适应度大
                            Algorithm.new_tournament_selection(popSingleObj.pt_list, parents, distance_index[0]);
                            //选择交叉变异生成较好的个体，加入到子种群中
                            popSingleObj.qt_list.addAll(Algorithm.variation(popSingleObj.pt_list.get(parents[0]), popSingleObj.pt_list.get(parents[1]), 1.0, taog_sum));
                        }
                        //再将pt_list合并qt_list成为rt_list，这个单目标优化问题，应该不需要快速非支配排序之类的，比较适应度值即可
                        popSingleObj.combine_pt_and_qt_to_rt();

                        //同样要将rt_list的一部分映射到隐空间里，计算距离，根据这个距离去选择优良个体来生成pt
                        Object[] rt;
                        List<Double> rt_list = new ArrayList<>();

                        //对rt_list进行非支配排序，先从这个解集里面去选择150个个体
                        popSingleObj.fast_non_dominated_sort(popSingleObj.rt_list);
                        NSGAII aaa = new NSGAII(population_num, evolution_num);
                        int flag = 0;
                        forOut:
                        for (int j = 0; j < popSingleObj.fronts.size(); j++){
                            for (int l = 0; l < popSingleObj.fronts.get(j).size(); l++){
                                aaa.pt_list.add(popSingleObj.fronts.get(j).get(l));
                                rt_list.add(popSingleObj.fronts.get(j).get(l).objectives.get(0));
                                rt_list.add(popSingleObj.fronts.get(j).get(l).objectives.get(1));
                                rt_list.add(popSingleObj.fronts.get(j).get(l).objectives.get(2));
                                flag = flag + 1;
                                if (flag == 150){
                                    break forOut;
                                }
                            }
                        }

                        rt = rt_list.toArray();

                        gCovNow = new MWNumericArray(rt, MWClassID.DOUBLE);
                        //计算映射后的距离以及排序,此处，gCovNow代表的是rt_list,所以数量比较大，原来的gCovHistory里只包含了pt_list不够

    //                    list_temp_similar.clear();
    //                    for (int n = all_population_all_environment.get(minIndex).size() - 1; n >= all_population_all_environment.get(minIndex).size() - popSingleObj.rt_list.size(); n--){
    //                        list_temp_similar.add(all_population_all_environment.get(minIndex).get(n).objectives.get(0));
    //                        list_temp_similar.add(all_population_all_environment.get(minIndex).get(n).objectives.get(1));
    //                        list_temp_similar.add(all_population_all_environment.get(minIndex).get(n).objectives.get(2));
    //                    }
    //                    array_temp_history = list_temp_similar.toArray();
    //                    //将数组转换成矩阵
    //                    gCovHistory = new MWNumericArray(array_temp_history, MWClassID.DOUBLE);

                        distance_object = TCA.TCA(2, gCovHistory, gCovNow,gCovPf, dataW);
                        distance_index_array = (MWNumericArray) distance_object[1];
                        distance_array = (MWNumericArray) distance_object[0];

                        //distance_index[0]数组里存放着经index，升序排列的
                        distance_index = (int[][]) distance_index_array.toIntArray();
                        //distance[0]数组里存放着距离
                        distance = (double[][]) distance_array.toDoubleArray();

                        //将距离小的解存入到pt_list中
                        popSingleObj.pt_list.clear();
                        popSingleObj.pt_list.addAll(aaa.pt_list);
                    }

                    //针对每个解做单目标优化后，将距离最小的那个解填充到初始种群中
                    pop_final.pt_list.add(popSingleObj.pt_list.get(distance_index[0][0] - 1));
                }

                //随机从样本集中选一些来填充到初始种群中
                int buchong = population_num - pop_final.pt_list.size();
                List<Integer> pfBuchong = new ArrayList<>();
                Individual.getRandomNumListNew(buchong,0,pop.pt_list.size(),pfBuchong);
                for (int m = 0; m < buchong; m++) {
                    pop_final.pt_list.add(pop.pt_list.get(pfBuchong.get(m)));
                }

                //至此，得到了pop_final
                //在pop_final的pt_list生成之后，开始对pop_final完成进化
                //当前环境下的所有解
                List<Individual> now_all_solutions = new ArrayList<>();
                pop_final.evolution(taog_list, taog_sum, now_all_solutions);
                //good_population1.add(pop_final.pt_list);
                //输出其帕累托最优解
                pop_final.output_PF(pf1);

                //保存本次实验的所有的解
                String pathAll = "F:\\pfResult\\NSGAII\\TCA\\run"+ (z+1) +"\\" + i +"\\all\\"+"all"+i+".txt";
                File fileAll = new File(pathAll);
                if(!fileAll.exists()){
                    fileAll.createNewFile();
                }
                FileWriter fileWriteAll = new FileWriter(fileAll,true);
                String lineAll = System.getProperty("line.separator");
                //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                BufferedWriter dataAll = new BufferedWriter(fileWriteAll);
                for (int j = 0; j < now_all_solutions.size(); j++){
                    dataAll.write("" + now_all_solutions.get(j).objectives.get(0));
                    dataAll.write("\t");
                    dataAll.write("" + now_all_solutions.get(j).objectives.get(1));
                    dataAll.write("\t");
                    dataAll.write("" + now_all_solutions.get(j).objectives.get(2));
                    dataAll.write(lineAll);
                }
                dataAll.close();
                fileWriteAll.close();

                System.out.println(i + "-------");
            }


            //开始前将存放帕累托解的文件夹清空
            String deleteParetoPath = "F:\\pfResult\\NSGAII\\TCA\\run"+ (z+1) +"\\pf";
            Problem.deleteDir(deleteParetoPath);

                //把八个环境下的pf保存下来
            for (int i = 0; i < 8; i++) {
                //针对每个环境，要存储当前环境下的所有解，最后将其作为真实的PF，来计算IGD距离
    //                double max[]= {Double.MIN_VALUE,Double.MIN_VALUE,Double.MIN_VALUE};
    //                double min[]= {Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE};
    //                getMaxAndMinObjectives(pf1.get(i),max,min);
    //                //规范化处理
    //                normalization(pf1.get(i),max,min);

                String path = "F:\\pfResult\\NSGAII\\TCA\\run"+ (z+1) +"\\pf\\OriPF"+i+".txt";
                File file = new File(path);
                if(!file.exists()){
                    file.createNewFile();
                }
                FileWriter fileWrite = new FileWriter(file,true);
                String line = System.getProperty("line.separator");
                //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                BufferedWriter data = new BufferedWriter(fileWrite);
                for (int j = 0; j < pf1.get(i).size(); j++){
                    data.write("" + pf1.get(i).get(j).objectives.get(0));
                    data.write("\t");
                    data.write("" + pf1.get(i).get(j).objectives.get(1));
                    data.write("\t");
                    data.write("" + pf1.get(i).get(j).objectives.get(2));
                    data.write(line);
                }
                data.close();
                fileWrite.close();

            }

            double allTimeEnd=System.currentTimeMillis();

            System.out.println("Time cost is "+(allTimeEnd-allTimeBegin)/1000/60+"min!");

        }


    }

    public static void getMaxAndMinObjectives(List<Individual> pf,double max[],double min[]) {

        for(Individual ind:pf) {
            for(int i=0;i<ind.objectives.size();i++) {
                if(ind.objectives.get(i)>max[i]) {
                    max[i] = ind.objectives.get(i);
                }
                if(ind.objectives.get(i)<min[i]) {
                    min[i] = ind.objectives.get(i);
                }
            }
        }
    }

    public static List<Individual> getTruePF(List<Individual> all_now_solutions){
        List<Individual> ret=new ArrayList<>();

        ret = Problem.non_dominate_solution(Problem.delete_same_individual(all_now_solutions));
        return ret;
    }

    public static void write_IGD(String writeFile, double IGD) throws IOException {
        File file = new File(writeFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWrite = new FileWriter(file, true);
        // 如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
        BufferedWriter data = new BufferedWriter(fileWrite);
        data.write("" + IGD + "\n");
        data.close();
        fileWrite.close();
    }

    public static void read_original_TAOG(TAOG taog_sum,List<TAOG> taog_list) throws IOException {

        int[] TAOG_name = {1,3,6};

        for(int TAOG_name_index = 0; TAOG_name_index < TAOG_name.length;TAOG_name_index++) {
            TAOG taog = new TAOG();
            //读取TAOG的信息，
            File file = new File("F:\\TD-TAOG\\Model" + TAOG_name[TAOG_name_index]);
            // 建立一个输入流对象reader
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            String normal_node_flag = "";
            if (TAOG_name[TAOG_name_index] == 1){
                normal_node_flag = "9";
            }else if (TAOG_name[TAOG_name_index] == 2){
                normal_node_flag = "32";
            }else if (TAOG_name[TAOG_name_index] == 3){
                normal_node_flag = "52";
            }else if (TAOG_name[TAOG_name_index] == 4){
                normal_node_flag = "65";
            }else if (TAOG_name[TAOG_name_index] == 5){
                normal_node_flag = "87";
            }else if (TAOG_name[TAOG_name_index] == 6){
                normal_node_flag = "117";
            }

            List<Integer> temp = new ArrayList<>();
            //每次读取一行
            line = br.readLine();
            int iteration = 0;
            while (line != null) {
                String str[] = new String[200];
                //将每一行的数据根据"    "来切分并放到一个数组里
                str = line.split("\t|  |   ");
                //第一行是Model的name，不做处理
                if (iteration == 0) {

                }
                //第二行数据，分别代表ModelName,组件数，人工节点数，普通节点数和单元节点数,人工节点开始编号
                else if (iteration == 1) {
                    taog.name = Integer.parseInt(str[0]);
                    taog.component_num = Integer.parseInt(str[1]);
                    taog.artificial_node_num = Integer.parseInt(str[2]);
                    taog.normal_node_num = Integer.parseInt(str[3]);
                    taog.unit_node_num = Integer.parseInt(str[4]);
                    taog.start_art_node = Integer.parseInt(str[5]);
                }
                //从第三行开始，就是三种节点的数据了,其中，每一行的第二个元素用来区分节点类型，0代表人工节点，1代普通节点，2代表单元节点
                else {
                    //录入人工节点、普通节点和单元节点
                    //人工节点的父节点的临时集合
                    List<Integer> artificial_node_parent_temp = new ArrayList<>();
                    //人工节点的子节点的临时集合
                    List<Integer> artificial_node_children_temp = new ArrayList<>();
                    //普通节点的父节点的临时集合
                    List<Integer> normal_node_parent_temp = new ArrayList<>();
                    //普通节点的直接前继普通节点的临时集合
                    List<Integer> normal_node_parent_B_temp = new ArrayList<>();
                    //普通节点的子节点的临时集合
                    List<Integer> normal_node_children_temp = new ArrayList<>();
                    //普通节点任务层面的子节点的临时集合
                    List<Integer> normal_node_task_children_temp = new ArrayList<>();
                    //单元节点的父节点的临时集合
                    List<Integer> unit_node_parent_temp = new ArrayList<>();
                    //单元节点的子节点的临时集合
                    List<Integer> unit_node_children_temp = new ArrayList<>();
                    //任务被操作者处理所花费时间集合(第一个元素是该任务所属的operation的index)
                    List<Integer> task_operator_time_temp = new ArrayList<>();

                    //如果是人工节点，要将其父节点和子节点都存到集合里
                    if (str[1].equals("0")) {
//                        taog.artificial_node_of_DT.add(Integer.parseInt(str[2]));
                        //str[3]也就是该行第四个元素代表的是人工节点的父节点的个数,将父节点(普通节点)存到临时集合中
                        for (int i = 0; i < Integer.parseInt(str[3]); i++) {
                            artificial_node_parent_temp.add(Integer.parseInt(str[4 + i]));
                        }
                        //将子节点(普通节点)存到临时集合中
                        for (int i = 0; i < Integer.parseInt(str[Integer.parseInt(str[3]) + 4]); i++) {
                            artificial_node_children_temp.add(Integer.parseInt(str[i + Integer.parseInt(str[3]) + 5]));
                        }
                        //将每一个人工节点的父节点信息存到taog的artificial_node_parent集合里
                        taog.artificial_node_parent.put(Integer.parseInt(str[2]), artificial_node_parent_temp);
                        //将每一个人工节点的子节点信息存到taog的artificial_node_children集合里
                        taog.artificial_node_children.put(Integer.parseInt(str[2]), artificial_node_children_temp);
                    }
                    //如果是普通节点，也要存储父节点跟子节点的信息,普通节点行的最后一个数字代表的是该operation下的task个数
                    else if (str[1].equals("1")) {
                        //普通节点只有一个父节点，即一个Operation只能对应一个人工节点
                        normal_node_parent_temp.add(Integer.parseInt(str[4]));
                        //接着要判断这个普通节点有哪些直接前继的普通节点
//                        if (str[5].equals("1")){
//                            normal_node_parent_B_temp.add(Integer.parseInt(str[6]));
//
//                        }
//                        else if (str[5].equals("2")){
//                            normal_node_parent_B_temp.add(Integer.parseInt(str[6]));
//                            normal_node_parent_B_temp.add(Integer.parseInt(str[7]));
//                        }else if (str[5].equals("3")){
//                            normal_node_parent_B_temp.add(Integer.parseInt(str[6]));
//                            normal_node_parent_B_temp.add(Integer.parseInt(str[7]));
//                            normal_node_parent_B_temp.add(Integer.parseInt(str[8]));
//                        }

                        for (int i = 0; i < Integer.parseInt(str[5]); i++) {
                            normal_node_parent_B_temp.add(Integer.parseInt(str[6+i]));
                        }

                        //紧接着要判断这个普通节点是否有子节点
                        //1个子节点(人工节点)
                        if (str[6 + Integer.parseInt(str[5])].equals("1")) {
                            normal_node_children_temp.add(Integer.parseInt(str[7 + Integer.parseInt(str[5])]));
                            for (int i = 0; i < Integer.parseInt(str[8 + Integer.parseInt(str[5])]); i++) {
                                normal_node_task_children_temp.add(Integer.parseInt(str[9 + Integer.parseInt(str[5]) + i]));
                            }
                        }
                        //2个子节点(人工节点)
                        else if (str[6 + Integer.parseInt(str[5])].equals("2")) {
                            normal_node_children_temp.add(Integer.parseInt(str[7 + Integer.parseInt(str[5])]));
                            normal_node_children_temp.add(Integer.parseInt(str[8 + Integer.parseInt(str[5])]));
                            for (int i = 0; i < Integer.parseInt(str[9 + Integer.parseInt(str[5])]); i++) {
                                normal_node_task_children_temp.add(Integer.parseInt(str[10 + Integer.parseInt(str[5]) + i]));
                            }
                        }
                        //0个子节(人工节点)
                        else if (str[6 + Integer.parseInt(str[5])].equals("0")) {
                            for (int i = 0; i < Integer.parseInt(str[7 + Integer.parseInt(str[5])]); i++) {
                                normal_node_task_children_temp.add(Integer.parseInt(str[8 + Integer.parseInt(str[5]) + i]));
                            }
                        }

                        //taog.normal_node_parent.add(normal_node_parent_temp);
                        taog.normal_node_parent.put(Integer.parseInt(str[2]) - 1, normal_node_parent_temp);
                        //taog.normal_node_parent_B.add(normal_node_parent_B_temp);
                        taog.normal_node_parent_B.put(Integer.parseInt(str[2]) - 1, normal_node_parent_B_temp);
                        //taog.normal_node_children.add(normal_node_children_temp);
                        taog.normal_node_children.put(Integer.parseInt(str[2]) - 1, normal_node_children_temp);
                        //taog.normal_node_task_children.add(normal_node_task_children_temp);
                        taog.normal_node_task_children.put(Integer.parseInt(str[2]) - 1, normal_node_task_children_temp);
                    }
                    //如果是单元节点，既要存储父节点跟子节点的信息，又要存储每个操作者处理任务的时间
                    else if (str[1].equals("2")) {
                        //将该任务属于哪个拆卸操作存到集合里
                        //taog.parent_B_of_unit.add(Integer.parseInt(str[3]));
                        taog.parent_B_of_unit.put(Integer.parseInt(str[2]) - 1, Integer.parseInt(str[3]));
                        //将该任务的类型存储到集合里
                        //taog.unit_node_type.add(Integer.parseInt(str[4]));
                        taog.unit_node_type.put(Integer.parseInt(str[2]) - 1, Integer.parseInt(str[4]));
                        if (normal_node_flag.equals(str[3])){
                            temp.add(Integer.parseInt(str[2]));
                            taog.normal_node_task.put(Integer.parseInt(str[3]), temp);
                            normal_node_flag = str[3];
                        }else {
//                            taog.normal_node_of_DT.add(Integer.parseInt(normal_node_flag));
                            List<Integer> test = new ArrayList<>();
                            for (int k = 0; k < temp.size(); k++){
                                test.add(temp.get(k));
                            }
                            temp.clear();
                            temp.add(Integer.parseInt(str[2]));
                            taog.normal_node_task.put(Integer.parseInt(normal_node_flag), test);
                            normal_node_flag = str[3];
                        }


                        //接下来存储该单元节点的父节点
                        for (int i = 0; i < Integer.parseInt(str[5]); i++) {
                            //父节点可能有初始节点
                            if (str[6 + i].contains("i")) {
                                unit_node_parent_temp.add(0);
                            } else {
                                unit_node_parent_temp.add(Integer.parseInt(str[6 + i]));
                            }

                        }
                        //接下来存储该单元节点的子节点
                        for (int i = 0; i < Integer.parseInt(str[Integer.parseInt(str[5]) + 6]); i++) {
                            //子节点可能包括终止节点
                            if (str[i + 6 + Integer.parseInt(str[5]) + 1].contains("t")) {
                                unit_node_children_temp.add(Integer.MAX_VALUE);
                            } else {
                                unit_node_children_temp.add(Integer.parseInt(str[i + 6 + Integer.parseInt(str[5]) + 1]));
                            }
                        }
                        //接下来存储该任务被操作者操作所花费的时间,总共有24个机器人和10个工人
                        for (int i = 0; i < 34; i++) {
                            task_operator_time_temp.add(Integer.parseInt(str[6 + Integer.parseInt(str[5]) + Integer.parseInt(str[6 + Integer.parseInt(str[5])]) + 1 + i]));
                        }

                        //存储每个单元节点的父节点
                        //taog.unit_node_parent.add(unit_node_parent_temp);
                        taog.unit_node_parent.put(Integer.parseInt(str[2]) - 1, unit_node_parent_temp);
                        //存储每个单元节点的子节点
                        //taog.unit_node_children.add(unit_node_children_temp);
                        taog.unit_node_children.put(Integer.parseInt(str[2]) - 1, unit_node_children_temp);
                        //存储每个单元节点被处理的时间
                        //taog.task_operator_time.add(task_operator_time_temp);
                        taog.task_operator_time.put(Integer.parseInt(str[2]) - 1, task_operator_time_temp);
                        //存储每个单元节点可以被操作的类型
//                        taog.type_of_process.add(Integer.parseInt(str[6 + Integer.parseInt(str[5]) + Integer.parseInt(str[6 + Integer.parseInt(str[5])]) + 35]));
                        taog.type_of_process.put(Integer.parseInt(str[2]) - 1, Integer.parseInt(str[6 + Integer.parseInt(str[5]) + Integer.parseInt(str[6 + Integer.parseInt(str[5])]) + 35]));
                    }
                }
                line = br.readLine(); // 一次读入一行数据
                iteration++;
            }

            taog_list.add(taog);
        }

        int index = 0;

        for (int i = 0; i < taog_list.size(); i++){
            //总的组件数目
            taog_sum.component_num = taog_sum.component_num + taog_list.get(i).component_num;
            //总的人工节点数目
            taog_sum.artificial_node_num = taog_sum.artificial_node_num + taog_list.get(i).artificial_node_num;
            //总的普通节点的数目
            taog_sum.normal_node_num = taog_sum.normal_node_num + taog_list.get(i).normal_node_num;
            //总的单元节点的数目
            taog_sum.unit_node_num = taog_sum.unit_node_num + taog_list.get(i).unit_node_num;
            //总的人工节点的父节点
//            for(int j=0;j<taog_list.get(i).artificial_node_parent.size();j++) {
//                taog_sum.artificial_node_parent.put(taog_list.get(i)., taog_list.get(i).artificial_node_parent.get(j));
//            }
            for (Integer key : taog_list.get(i).artificial_node_parent.keySet()){
                taog_sum.artificial_node_parent.put(key, taog_list.get(i).artificial_node_parent.get(key));
            }


            //总的人工节点的子节点
//            for(int j=0;j<taog_list.get(i).artificial_node_children.size();j++) {
//                taog_sum.artificial_node_children.add(taog_list.get(i).artificial_node_children.get(j));
//            }
            for (Integer key : taog_list.get(i).artificial_node_children.keySet()){
                taog_sum.artificial_node_children.put(key, taog_list.get(i).artificial_node_children.get(key));
            }


            //总的普通节点的父节点
//            for(int j=0;j<taog_list.get(i).normal_node_parent.size();j++) {
//                taog_sum.normal_node_parent.add(taog_list.get(i).normal_node_parent.get(j));
//            }
            for (Integer key : taog_list.get(i).normal_node_parent.keySet()){
                taog_sum.normal_node_parent.put(key, taog_list.get(i).normal_node_parent.get(key));
            }


            //总的普通节点的直接前继普通节点
//            for (int j=0;j<taog_list.get(i).normal_node_parent_B.size();j++){
//                taog_sum.normal_node_parent_B.add(taog_list.get(i).normal_node_parent_B.get(j));
//            }
            for (Integer key : taog_list.get(i).normal_node_parent_B.keySet()){
                taog_sum.normal_node_parent_B.put(key, taog_list.get(i).normal_node_parent_B.get(key));
            }



            //总的普通节点的子节点
//            for(int j=0;j<taog_list.get(i).normal_node_children.size();j++) {
//                taog_sum.normal_node_children.add(taog_list.get(i).normal_node_children.get(j));
//            }
            for (Integer key : taog_list.get(i).normal_node_children.keySet()){
                taog_sum.normal_node_children.put(key, taog_list.get(i).normal_node_children.get(key));
            }


            //总的普通节点在任务层面的子节点
//            for(int j=0;j<taog_list.get(i).normal_node_task_children.size();j++) {
//                taog_sum.normal_node_task_children.add(taog_list.get(i).normal_node_task_children.get(j));
//            }
            for (Integer key : taog_list.get(i).normal_node_task_children.keySet()){
                taog_sum.normal_node_task_children.put(key, taog_list.get(i).normal_node_task_children.get(key));
            }


            //总的单元节点的父节点
//            for(int j=0;j<taog_list.get(i).unit_node_parent.size();j++) {
//                taog_sum.unit_node_parent.add(taog_list.get(i).unit_node_parent.get(j));
//            }
            for (Integer key : taog_list.get(i).unit_node_parent.keySet()){
                taog_sum.unit_node_parent.put(key, taog_list.get(i).unit_node_parent.get(key));
            }


            //总的单元节点的子节点
//            for(int j=0;j<taog_list.get(i).unit_node_children.size();j++) {
//                taog_sum.unit_node_children.add(taog_list.get(i).unit_node_children.get(j));
//            }
            for (Integer key : taog_list.get(i).unit_node_children.keySet()){
                taog_sum.unit_node_children.put(key, taog_list.get(i).unit_node_children.get(key));
            }


            //总的任务处理时间
//            for (int j = 0; j < taog_list.get(i).task_operator_time.size(); j++){
//                taog_sum.task_operator_time.add(taog_list.get(i).task_operator_time.get(j));
//            }
            for (Integer key : taog_list.get(i).task_operator_time.keySet()){
                taog_sum.task_operator_time.put(key, taog_list.get(i).task_operator_time.get(key));
            }


            //总的任务类型
//            for (int j = 0; j < taog_list.get(i).unit_node_type.size(); j++){
//                taog_sum.unit_node_type.add(taog_list.get(i).unit_node_type.get(j));
//            }
            for (Integer key : taog_list.get(i).unit_node_type.keySet()){
                taog_sum.unit_node_type.put(key, taog_list.get(i).unit_node_type.get(key));
            }


            //总的单元节点属于哪个拆卸操作
//            for(int j=0;j<taog_list.get(i).parent_B_of_unit.size();j++) {
//                taog_sum.parent_B_of_unit.add(taog_list.get(i).parent_B_of_unit.get(j));
//            }
            for (Integer key : taog_list.get(i).parent_B_of_unit.keySet()){
                taog_sum.parent_B_of_unit.put(key, taog_list.get(i).parent_B_of_unit.get(key));
            }


            //总的普通节点下的任务数
//            for (int j = index; j <index + taog_list.get(i).normal_node_task.size(); j++){
//                taog_sum.normal_node_task.put(j + 1, taog_list.get(i).normal_node_task.get(j + 1));
//            }
            for (Integer key : taog_list.get(i).normal_node_task.keySet()){
                taog_sum.normal_node_task.put(key, taog_list.get(i).normal_node_task.get(key));
            }


            //总的任务可被操作的类型
//            for (int j = 0; j < taog_list.get(i).type_of_process.size(); j++){
//                taog_sum.type_of_process.add(taog_list.get(i).type_of_process.get(j));
//            }
            for (Integer key : taog_list.get(i).type_of_process.keySet()){
                taog_sum.type_of_process.put(key, taog_list.get(i).type_of_process.get(key));
            }

            index = index + taog_list.get(i).normal_node_task.size();
        }

    }

    public static void normalization(List<Individual> pf,double max[],double min[]) {

        for(int n=0;n<pf.size();n++) {
            Individual ind=pf.get(n);
            for(int i=0;i<ind.objectives.size();i++) {
                ind.objectives.set(i, (ind.objectives.get(i)-min[i])/(max[i]-min[i]));
            }
        }

    }
}
