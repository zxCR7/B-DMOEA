import com.mathworks.toolbox.javabuilder.MWException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//import getMMD.MMD;


public class MainIBEA {
    public static void main(String[] args) throws IOException, MWException {

        //开始时间
        double allTimeBegin=System.currentTimeMillis();
        //种群数量
        int population_num = 150;
        //进化代数
        int evolution_num = 50;
        List<TAOG> taog_list=new ArrayList<>();
        TAOG taog_sum=new TAOG();
        read_original_TAOG(taog_sum,taog_list);

        //不用迁移学习
        List<ArrayList<Individual>> pf = new ArrayList<ArrayList<Individual>>();
        for (int i = 0; i < 8; i++){
            System.out.println("Environment " + i );

            //初始化一个种群
            IBEA pop = new IBEA(population_num, evolution_num);

            List<Integer> state = new ArrayList<>();
            for (int j = 0; j < Problem.all_state[i].length; j++){
                state.add(Problem.all_state[i][j]);
            }

            pop.pt_list.addAll(pop.init_solution(taog_list, taog_sum, state));

            //当前环境下的所有解
            List<Individual> now_all_solutions = new ArrayList<>();
            //进化
            pop.evolution(taog_list, taog_sum, now_all_solutions);
            //计算每个环境下的pareto解
            pop.output_PF(pf);

            //针对每个环境，要存储当前环境下的所有解，最后将其作为真实的PF，来计算IGD距离
            double max[]= {Double.MIN_VALUE,Double.MIN_VALUE,Double.MIN_VALUE};
            double min[]= {Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE};
            getMaxAndMinObjectives(pf.get(i),max,min);

            List<Individual> truePF = getTruePF(now_all_solutions);

            System.out.println("IGD----" + Problem.IGD(pf.get(i), truePF));
            System.out.println("-------------------");

            normalization(pf.get(i),max,min);

            String path = "F:\\pfResult\\IBEA\\original\\"+"OriPF"+i+".txt";
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWrite = new FileWriter(file,true);
            String line = System.getProperty("line.separator");
            //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
            BufferedWriter data = new BufferedWriter(fileWrite);
            //normalization(pf);
            for (int j = 0; j < pf.get(i).size(); j++){
                data.write("" + pf.get(i).get(j).objectives.get(0));
                data.write("\t");
                data.write("" + pf.get(i).get(j).objectives.get(1));
                data.write("\t");
                data.write("" + pf.get(i).get(j).objectives.get(2));
                data.write(line);
            }
            data.close();
            fileWrite.close();
        }

        double allTimeEnd=System.currentTimeMillis();

        System.out.println("Time cost is "+(allTimeEnd-allTimeBegin)/1000/60+"min!");

    }

    public static void getMaxAndMinObjectives(List<Individual> pf,double max[],double min[]) {
        //for(ArrayList<Individual> list:pf) {
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
        //}
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

        int[] TAOG_name = {2,3,4,5,6};

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
                        if (str[5].equals("1")){
                            normal_node_parent_B_temp.add(Integer.parseInt(str[6]));

                        }
                        else if (str[5].equals("2")){
                            normal_node_parent_B_temp.add(Integer.parseInt(str[6]));
                            normal_node_parent_B_temp.add(Integer.parseInt(str[7]));
                        }else if (str[5].equals("3")){
                            normal_node_parent_B_temp.add(Integer.parseInt(str[6]));
                            normal_node_parent_B_temp.add(Integer.parseInt(str[7]));
                            normal_node_parent_B_temp.add(Integer.parseInt(str[8]));
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
