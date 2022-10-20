import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;


public class Individual implements Serializable, Cloneable{

    //测试用的全局变量
    int test_flag = 0;

    // 一些固定的最终变量,如果发现周期时间设计的较短，就往上面加一个4.0
    public final double CYCLETIME_INCREMENT = 4.0;

    // 多个product一起拆卸，该集合存放的是一起拆卸的几个产品的TAOG
    public List<TAOG> taog_list;

    //所有的TAOG信息汇总
    public TAOG taog_sum;

    // 目标函数 目标0 周期时间;目标1 操作者总数;目标2 人数;
    public List<Double> objectives;

    //拥挤度
    public double crowding_distance;

    //当前环境的状态
    public List<Integer> state;

    //编码序列的第一部分
    public List<Double> part_1;

    //编码序列的第二部分
    public List<Double> part_2;

    //编码序列的第三部分
    public HashMap<Integer,Double> part_3;

    //编码序列的第四部分
    public List<Double> part_4;

    //编码序列的第五部分
    public List<Double> part_5;

    //拆卸操作跟工作站的对应关系
    public HashMap<Integer,Integer> BW;

    //操作者对应的伊布斯隆区间
    public List<Double> interval;

//    //操作者被分配的任务集合
//    public HashMap<Integer,List<Integer>> tasks_of_operator;

    //表示单个个体的向量,一共有3个
    //操作者-工作站对应关系 OW
    public List<Integer> operator_workstation;
//    //任务序列 TS
//    public List<Integer> task_sequence = new ArrayList<>();
    //任务-操作者对应关系 TO
    public HashMap<Integer,Integer> task_operator;

    //存储选取的DT包含的Operation(普通节点)
	public List<Integer> normal_node_of_DT;

	//存储选择的task
    public List<Integer> task_set;

    // 集合大小是操作者数量大小，此集合是为了在每一个可用的操作者上分配所有选择的任务
    public HashMap<Integer, List<Operator_Task_Time_Node>> time_node= new HashMap<>();

    //操作者的任务集合（排序过的）
    public HashMap<Integer, List<Integer>> operator_tasks_desc;

    // 用于IBEA**************************************************************
    double fitness=0.0;

    //有参构造器
    Individual(List<TAOG> taog_list, TAOG taog_sum){

        this.taog_list = taog_list;
        this.taog_sum = taog_sum;
        this.part_1 = new ArrayList<>();
        this.part_2 = new ArrayList<>();
        this.part_3 = new HashMap<>();
        this.part_4 = new ArrayList<>();
        this.part_5 = new ArrayList<>();

        this.state = new ArrayList<>();
        this.normal_node_of_DT = new ArrayList<>();
        this.task_set = new ArrayList<>();
        this.operator_workstation = new ArrayList<>();
        this.task_operator = new HashMap<>();
        this.BW = new HashMap<>();
        this.interval = new ArrayList<>();
        this.operator_tasks_desc = new HashMap<>();
        this.objectives = new ArrayList<>();
    }

    //个体初始化方法
    public void individual_initialize(List<Integer> state) throws IOException{

        this.state = state;

        //先确定拆卸树,在生成拆卸树的过程中，会对编码序列的part1进行初始化
        determin_DT();

        //紧接着去选择task
        determin_task(state);

        //接下来要确定操作者跟工作站的对应关系
        determinOW();

        //操作者跟工作站的对应关系确定了之后，要完成任务到操作者的分配
        determinTO();

        //在确定了任务到操作者的分配之后，针对某个操作者要确定其拆卸顺序
        determinYita();

        //最后计算三个目标函数值
        cal_objectives();

        //如果有解的工作站操作者个数为0
        if (is_empty_operator() == 1){
            System.out.println("-----------");
        }

    }


    //随即生成几个不同的数
    public static List getRandomNumList(int nums,int start,int end,List<Double> part){
//        //1.创建集合容器对象
        //part = new ArrayList();

        //2.创建Random对象
        Random r = new Random();
        //循环将得到的随机数进行判断，如果随机数不存在于集合中，则将随机数放入集合中，如果存在，则将随机数丢弃不做操作，进行下一次循环，直到集合长度等于nums
        while(part.size() != nums){
            int num = r.nextInt(end-start) + start;
            BigDecimal a = new BigDecimal(num);
            BigDecimal b = new BigDecimal(1000);
            BigDecimal result = a.divide(b);

            double double_result = result.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            if(!part.contains(double_result)){
                part.add(double_result);
            }
        }
        return part;
    }

    public static List getRandomNumListNew(int nums,int start,int end,List<Integer> part){
//        //1.创建集合容器对象
        //part = new ArrayList();

        //2.创建Random对象
        Random r = new Random();
        //循环将得到的随机数进行判断，如果随机数不存在于集合中，则将随机数放入集合中，如果存在，则将随机数丢弃不做操作，进行下一次循环，直到集合长度等于nums
        while(part.size() != nums){
            int num = r.nextInt(end-start);

            if(!part.contains(num)){
                part.add(num);
            }
        }
        return part;
    }

    //确定哪几种类型的任务可以被执行
    public void get_type_able(int state, List<Integer> type_able){
        //普通节点为正常状态，可执行的任务类型为1，4，5，7
        if (state == 0){
            type_able.add(1);
            type_able.add(4);
            type_able.add(5);
            type_able.add(7);
        }
        //普通节点为损坏状态，可执行的任务类型为2，4，6，7
        else if (state == 1){
            type_able.add(2);
            type_able.add(4);
            type_able.add(6);
            type_able.add(7);
        }
        //普通节点为丢失状态，可执行的任务类型为3，5，6，7
        else if (state == 2){
            type_able.add(3);
            type_able.add(5);
            type_able.add(6);
            type_able.add(7);
        }
    }

    //获取单个模型的DT
    public void select_DT_of_one_model(int artificial_node){
        //根据B节点的随机数值去选择A节点的子B节点
        //首先要找到当前A节点有几个子B节点
        List<Integer> child_of_A = taog_sum.artificial_node_children.get(artificial_node);
        int selected_B;
        //如果只有一个子B节点直接选中就行了
        if (child_of_A.size() == 1){
            selected_B = child_of_A.get(0);
            this.normal_node_of_DT.add(selected_B);
        }
        //如果有多个子B节点，则要比较随机数值，选择随机数值大的子B节点
        else {
//            //找到子B节点在part1中所在的位置
//            int index1InAll = getAllIndex(child_of_A.get(0), taog_sum.normal_node_task);
//            int index2InAll = getAllIndex(child_of_A.get(1), taog_sum.normal_node_task);
//
//            if (part_1.get(index1InAll) > part_1.get(index2InAll)){
//                selected_B = child_of_A.get(0);
//                this.normal_node_of_DT.add(selected_B);
//            }else {
//                selected_B = child_of_A.get(1);
//                this.normal_node_of_DT.add(selected_B);
//            }

            List<Double> a = new ArrayList<>();
            //找到子B节点在part1中所在的位置
            for (int i = 0; i < child_of_A.size(); i++) {
                a.add(part_1.get(getAllIndex(child_of_A.get(i), taog_sum.normal_node_task)));
            }

            int maxIndex = a.indexOf(Collections.max(a));
            selected_B = child_of_A.get(maxIndex);
            this.normal_node_of_DT.add(selected_B);
        }
//        //如果有两个子B节点，则要比较随机数值，选择随机数值大的子B节点
//        else {
//            //找到子B节点在part1中所在的位置
//            int index1InAll = getAllIndex(child_of_A.get(0), taog_sum.normal_node_task);
//            int index2InAll = getAllIndex(child_of_A.get(1), taog_sum.normal_node_task);
//
//            if (part_1.get(index1InAll) > part_1.get(index2InAll)){
//                selected_B = child_of_A.get(0);
//                this.normal_node_of_DT.add(selected_B);
//            }else {
//                selected_B = child_of_A.get(1);
//                this.normal_node_of_DT.add(selected_B);
//            }
//        }

        //获取当前选择的B节点的子A节点
        List<Integer> child_of_B = taog_sum.normal_node_children.get(selected_B - 1);
        for (int i = 0; i < child_of_B.size(); i++){
            select_DT_of_one_model(child_of_B.get(i));
        }

    }


    //确定拆卸树的生成
    public void determin_DT(){
        //接下来，初始化第一部分，为每一个B节点随机生成[0,1]之间的实数
        //先生成[0,1000]的整数之后，除1000
        getRandomNumList(taog_sum.normal_node_num,0,1001,part_1);

        //接下来，根据part1的随机数值来选择拆卸树DT
        //搜索的起始A节点点
        //int start_art_node = taog_list.get(0).start_art_node;
        for (int i = 0; i < taog_list.size(); i++){
            int start_art_node = taog_list.get(i).start_art_node;
            select_DT_of_one_model(start_art_node);

        }
    }

    //确定选择哪些任务
    public void determin_task(List<Integer> state){

        //存储可以执行的任务
        List<Integer> task_able = new ArrayList<>();

        //根据产品的状态首先确定有哪些任务是可选的task
        for (int i = 0; i < state.size(); i++){

            if (state.get(i) != -1){
                //找到当前普通节点下的所有任务
                List<Integer> current_normal_node_task = taog_sum.normal_node_task.get(i+1);
                //遍历所有找到的所有任务，找到在当前状态下可以执行的任务
                for (int j = 0; j < current_normal_node_task.size(); j++){
                    //获取任务可以在那种情况下被执行
                    int task_index = current_normal_node_task.get(j);
                    int task_type = taog_sum.unit_node_type.get(task_index - 1);

                    //找到当前状态下可以执行的任务，因为有些任务可以在多个状态下执行
                    List<Integer> type_able = new ArrayList<>();
                    get_type_able(state.get(i), type_able);

                    //找到可以执行的任务
                    if (type_able.contains(task_type)){
                        task_able.add(task_index);
                    }
                }
            }

        }

        //要确定任务，首先要为每个任务生成以个[0,1]之间的随机数值
        getRandomNumList(taog_sum.unit_node_num, 0, 1001, part_2);

        //随机数值生成完成之后，根据part2的随机数值来选择任务
        //选择任务，要在选中的B节点下去选择
        for (int i = 0; i < this.normal_node_of_DT.size(); i++){
            //找到当前B节点在当前状态下可以执行的任务
            List<Integer> task_processable_B = new ArrayList<>();
            List<Integer> all_task_B = taog_sum.normal_node_task.get(this.normal_node_of_DT.get(i));
            for (int j = 0; j < task_able.size(); j++){
                if (all_task_B.contains(task_able.get(j))){
                    task_processable_B.add(task_able.get(j));
                }
            }

            //找到每个B节点下的可以执行的任务后，开始遍历，要比较每个任务的随机数值，选择数值大的那个
            //首先针对某个B节点选中起始的Unit节点
            List<Integer> task_child_of_B = taog_sum.normal_node_task_children.get(this.normal_node_of_DT.get(i) - 1);

            List<Integer> candidate_start_unit = new ArrayList<>();

            for (int j = 0; j < task_child_of_B.size(); j++){
                if (task_processable_B.contains(task_child_of_B.get(j))){
                    candidate_start_unit.add(task_child_of_B.get(j));
                }
            }

            int start_unit_node = candidate_start_unit.get(0);

            //获取到候选的unit_node之后，找到随机数值最大的unit_node
            for (int j = 0; j < candidate_start_unit.size(); j++){
                //找到任务在part2中对应的全局index
                int indexOfStartInall = getAllIndex(start_unit_node - 1, taog_sum.unit_node_parent);
                int indexInAll = getAllIndex(candidate_start_unit.get(j) - 1, taog_sum.unit_node_parent);
                if (this.part_2.get(indexInAll) > this.part_2.get(indexOfStartInall)){
                    start_unit_node = candidate_start_unit.get(j);
                }
            }

            this.task_set.add(start_unit_node);

            //在确定了起始的unit_node之后，开始遍历，根据规则找到合适的unit_node

            int flag = 0;
            flag = find_node_of_task(start_unit_node, task_processable_B);
            while (flag != 0 && flag != Integer.MAX_VALUE){
                this.task_set.add(flag);
                flag = find_node_of_task(flag, task_processable_B);
            }
        }

    }

    //找unit节点后的节点
    public Integer find_node_of_task(Integer unit_node, List<Integer> task_processable_B){
        //找到当前unit节点的子节点
        List<Integer> task_children = taog_sum.unit_node_children.get(unit_node - 1);
        //遍历该task的子节点
        //该task只有一个子节点，为终止节点
        if (task_children.size() == 1 && task_children.get(0).equals(Integer.MAX_VALUE)){
            return 0;
        }
        //该task只有一个子节点，且不为终止节点
        else if (task_children.size() == 1 && !task_children.get(0).equals(Integer.MAX_VALUE) && task_processable_B.contains(task_children.get(0))){
            return task_children.get(0);
        }
        //该task不止有一个子节点，这个时候就要去比较子unit节点的随机数值，选择随机数值大的那个节点
        else {
            List<Integer> candidate_unit_node = new ArrayList<>();

            //找到符合状态条件的子节点
            for (int i = 0; i < task_children.size(); i++){
                if (task_processable_B.contains(task_children.get(i))){
                    candidate_unit_node.add(task_children.get(i));
                }
            }
            if (candidate_unit_node.size() == 0){
                System.out.println("-----------");
            }
            int start_unit_node = candidate_unit_node.get(0);
            //找到候选节点中的随机数值最大的那个节点
            for (int j = 0; j < candidate_unit_node.size(); j++){

                //找到任务在part2中对应的全局index
                int indexOfStartInall = getAllIndex(start_unit_node - 1, taog_sum.unit_node_parent);
                int indexInAll = getAllIndex(candidate_unit_node.get(j) - 1, taog_sum.unit_node_parent);

                if (this.part_2.get(indexInAll) > this.part_2.get(indexOfStartInall)){
                    start_unit_node = candidate_unit_node.get(j);
                }
            }

            //如果选中了终止节点，则直接返回终止节点
            return start_unit_node;
        }
    }

    //确定操作者跟工作站的对应关系
    public void determinOW(){

        //先将part3的每个元素赋为1，1*3 = 3即闲置
        for (int i = 0; i < Problem.operator_num; i++){
            this.part_3.put(i,1.0);
        }

        //首先要确定每个工作站的操作者数量，每个工作站的机器人数量范围为[1,4],工人数量范围为[1,3]
        for (int i = 0; i < Problem.workstation_num; i++){
            //机器人数
            int robot_num = new Random().nextInt(Problem.max_robot_in_workstation) + 1;
            //工人数
            int human_num = new Random().nextInt(Problem.max_human_in_workstation) + 1;
            //实际上对于每个工作站来说，它下属的操作者的随机数值实际上是有范围的
            //0号工作站，[0,0.34); 1号工作站，[0.34，0.67）; 2号工作站，[0.67,1);
            switch(i){
                case 0 :
                    for (int j =0; j < robot_num; j++){
                        //随机选择一个闲置的机器人，每次都要确定哪些机器人是限制的
                        List<Integer> free_robots = new ArrayList<>();
                        for (int k = 0; k < Problem.robot_num; k++){
                            if (this.part_3.get(k) == 1.0){
                                free_robots.add(k);
                            }
                        }

                        if (free_robots.size() == 0){
                            System.out.println("---------------");
                        }

                        //从空闲机器人集合里去随机选择机器人
                        int select_robot_index = new Random().nextInt(free_robots.size());
                        //选定了机器人之后，就是为其分配随机数值
                        double gama = getRandomNum(0, 334);
                        //将随机数值分配给对应的机器人
                        this.part_3.put(free_robots.get(select_robot_index), gama);
                    }

                    for (int j = 0; j < human_num; j++){
                        List<Integer> free_humans = new ArrayList<>();
                        for (int k = Problem.robot_num; k < Problem.operator_num; k++){
                            if (this.part_3.get(k) == 1.0){
                                free_humans.add(k);
                            }
                        }

                        if (free_humans.size() == 0){
                            System.out.println("---------------");
                        }


                        int select_human_index = new Random().nextInt(free_humans.size());
                        double gama = getRandomNum(0, 334);
                        this.part_3.put(free_humans.get(select_human_index), gama);
                    }
                    break;
                case 1 :
                    for (int j =0; j < robot_num; j++){
                        //随机选择一个闲置的机器人，每次都要确定哪些机器人是限制的
                        List<Integer> free_robots = new ArrayList<>();
                        for (int k = 0; k < Problem.robot_num; k++){
                            if (this.part_3.get(k) == 1.0){
                                free_robots.add(k);
                            }
                        }

                        if (free_robots.size() == 0){
                            System.out.println("---------------");
                        }

                        //从空闲机器人集合里去随机选择机器人
                        int select_robot_index = new Random().nextInt(free_robots.size());
                        //选定了机器人之后，就是为其分配随机数值
                        double gama = getRandomNum(334, 667);
                        //将随机数值分配给对应的机器人
                        this.part_3.put(free_robots.get(select_robot_index), gama);
                    }

                    for (int j = 0; j < human_num; j++){
                        List<Integer> free_humans = new ArrayList<>();
                        for (int k = Problem.robot_num; k < Problem.operator_num; k++){
                            if (this.part_3.get(k) == 1.0){
                                free_humans.add(k);
                            }
                        }

                        if (free_humans.size() == 0){
                            System.out.println("---------------");
                        }


                        int select_human_index = new Random().nextInt(free_humans.size());
                        double gama = getRandomNum(334, 667);
                        this.part_3.put(free_humans.get(select_human_index), gama);
                    }
                    break;
                case 2 :
                    for (int j =0; j < robot_num; j++){
                        //随机选择一个闲置的机器人，每次都要确定哪些机器人是限制的
                        List<Integer> free_robots = new ArrayList<>();
                        for (int k = 0; k < Problem.robot_num; k++){
                            if (this.part_3.get(k) == 1.0){
                                free_robots.add(k);
                            }
                        }

                        if (free_robots.size() == 0){
                            System.out.println("---------------");
                        }

                        //从空闲机器人集合里去随机选择机器人
                        int select_robot_index = new Random().nextInt(free_robots.size());
                        //选定了机器人之后，就是为其分配随机数值
                        double gama = getRandomNum(667, 1000);
                        //将随机数值分配给对应的机器人
                        this.part_3.put(free_robots.get(select_robot_index), gama);
                    }

                    for (int j = 0; j < human_num; j++){
                        List<Integer> free_humans = new ArrayList<>();
                        for (int k = Problem.robot_num; k < Problem.operator_num; k++){
                            if (this.part_3.get(k) == 1.0){
                                free_humans.add(k);
                            }
                        }

                        if (free_humans.size() == 0){
                            System.out.println("---------------");
                        }

                        int select_human_index = new Random().nextInt(free_humans.size());
                        double gama = getRandomNum(667, 1000);
                        this.part_3.put(free_humans.get(select_human_index), gama);
                    }
                    break;
            }
        }

        //针对操作者，根据各自的随机数值，计算其应该分配给哪个工作站
        for (int i = 0; i < part_3.size(); i++){
            int floor = (int)Math.floor(part_3.get(i) * Problem.workstation_num);
            this.operator_workstation.add(floor);
        }
    }


    //确定任务跟操作者的关系
    public void determinTO(){
        //一个拆卸操作下的所选择的任务，应该分配在一个站里面，实际上可以先确定操作到站，再确定任务到操作者

        do {
            for (int i = 0; i < this.normal_node_of_DT.size(); i++){
                //确定每个拆卸操作应该分配到哪个工作站，先要看该拆卸操作的直接前继拆卸操作被分配在了哪个工作站
                //所以首先要找到直接前继拆卸操作
                List<Integer> normal_node_parent_B = taog_sum.normal_node_parent_B.get(this.normal_node_of_DT.get(i) - 1);
                int pre_B_W = -1;
                for (int j = 0; j < normal_node_parent_B.size(); j++){
                    if (this.normal_node_of_DT.contains(normal_node_parent_B.get(j))){
                        //确定直接前继被分配到了哪个工作站
                        if (this.BW.get(normal_node_parent_B.get(j)) == null){
                            System.out.println("");
                        }
                        pre_B_W = this.BW.get(normal_node_parent_B.get(j));
                    }
                }

                int current_B_W = -1;

                //确定了前继操作分配到了哪个工作站后，再给当前拆卸操作选择工作站,为了确保让三个工作站都分配到任务
                //如果前继拆卸操作是分在了0号工作站，则当前拆卸操作可以分配在0，1，2号
                //如果前继拆卸操作是分在了1号工作站，则当前拆卸操作可以分配在1，2号
                //依此类推,这里分配到哪个站，是有倾向性的，
                double randm = getRandomNum(0,1001);
                switch (pre_B_W){
                    //无前继，则当钱拆卸操作为起始操作，将其分配在第一个工作站
                    case -1:
                        current_B_W = 0;
                        break;
                    case 0:
                        //生成一个[0,1]的随机数，[0,0.4)---0 [0.4 0.8)---1 [0.8 1]----2
                        if (randm >= 0 && randm < 0.4){
                            current_B_W = 0;
                        }else if (randm >= 0.4 && randm < 0.8){
                            current_B_W = 1;
                        }
                        else if (randm >= 0.8 && randm <= 1){
                            current_B_W = 2;
                        }
                        break;
                    case 1:
                        //生成一个[0,1]的随机数，[0,0.5)---1 [0.5 1]----2
                        if (randm >= 0 && randm < 0.5){
                            current_B_W = 1;
                        }else if (randm >= 0.5 && randm <= 1){
                            current_B_W = 2;
                        }
                        break;
                    case 2:
                        current_B_W = 2;
                        break;
                }
                this.BW.put(this.normal_node_of_DT.get(i), current_B_W);
            }
        }while (this.is_empty_station() == 0);


        //确定每个被选择的任务分配给谁，对于每个任务而言，实际上由于他所属的拆卸操作已经确定了分配给哪个工作站，所以任务可以被分配的操作者实际上也已经确定
        //确定每个操作者对应的伊布斯隆区间值
        for (int i = 0; i < Problem.operator_num; i++){
            BigDecimal a = new BigDecimal(i);
            BigDecimal b = new BigDecimal(34);
            BigDecimal lb = a.divide(b, 3, BigDecimal.ROUND_HALF_UP);
            double lBound = lb.doubleValue();
            this.interval.add(lBound);
        }
        this.interval.add(1.0);


        Integer allUnitNodeNum = 0;

        Iterator iter = taog_sum.type_of_process.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            if ((Integer)key >= allUnitNodeNum){
                allUnitNodeNum = (Integer) key + 1;
            }
        }
//        for (Integer key : taog_sum.type_of_process.keySet()) {
//            allUnitNodeNum = taog_sum.type_of_process.get(key) + 1;
//        }

        //在确定了每个操作者对应的区间之后，针对每个任务所在的工作站，根据所有可能的伊布斯隆区间去生成随机数，进而确定是分配个哪个操作者
        for (int i = 0; i < allUnitNodeNum; i++){
            if (!task_set.contains(i+1)){
                //随机生成一个[0,1)的实数
                double epsilon = getRandomNum(0,1000);
                this.part_4.add(epsilon);
            }else {
                //首先根据该任务可以被哪种类型的操作者执行（i代表的是任务的索引，任务编号实际上是从1开始的）
                int type_of_process = taog_sum.type_of_process.get(i);
                List<Integer> able_processor = new ArrayList<>();
                //还要确定当前任务是被分配到了哪个工作站
                int station = this.BW.get(taog_sum.parent_B_of_unit.get(i));
                //针对候选的操作者，每一个都有一个随机数值，再从这几个随机数值中随机选一个
                List<Double> candidate_epsilon = new ArrayList<>();

                //当前站内的可选操作者
                able_processor = this.operatorsOfStation(station,type_of_process);

                if (able_processor.size() == 0){
                    System.out.println("------");
                }

                for (int j = 0; j < able_processor.size(); j++){
                    //当前区间的上下限
                    double lb = this.interval.get(able_processor.get(j));
                    double ub = this.interval.get(able_processor.get(j) + 1);
                    //在这个范围内生成随机数 [下限，上限)
                    double result = getRandomNum((int) (lb*1000),(int) (ub*1000));
                    candidate_epsilon.add(result);
                }

                //在所有可能的epsilon中，随机选择一个，同时意味着为该任务选中了操作者
                int selected_O = new Random().nextInt(candidate_epsilon.size());
                part_4.add(candidate_epsilon.get(selected_O));
                task_operator.put(i+1, able_processor.get(selected_O));
            }
        }
    }

    //确定操作者对于自己分配到的任务的拆卸顺序
    public void determinYita(){
        //为每个任务随机生成yita值
        getRandomNumList(taog_sum.unit_node_num, 0, 1001, part_5);
        //初始化time_node
        initi_time_node();
        //针对每个站开始分析任务该怎么安排
        for (int i = 0; i < Problem.workstation_num; i++){
            //找到当前工作站有哪些拆卸操作
            List<Integer> BOfStation = new ArrayList<>();
            for (int j = 0; j < normal_node_of_DT.size(); j++){
                if (BW.get(normal_node_of_DT.get(j)) == i){
                    BOfStation.add(normal_node_of_DT.get(j));
                }
            }

            //每个站内任务的所有直接前驱任务(不是index)
            HashMap<Integer, Integer> all_pretask_station = new HashMap<>();
            //针对每个站的拆卸操作
            for (int j = 0; j < BOfStation.size(); j++){
                List<Integer> selected_task_of_B = selected_task_of_B(BOfStation.get(j));
                //遍历每个拆卸操作里的任务
                for (int k = 0; k < selected_task_of_B.size(); k++){
                    //找到任务的直接前驱任务
                    int pre_task_index = find_immediate_predecessor(selected_task_of_B.get(k), selected_task_of_B, BOfStation);
                    all_pretask_station.put(selected_task_of_B.get(k),pre_task_index + 1);
                }
            }

            //应该是分析每个站内的操作者，要根据yita值去决定两个无任务优先级约束的任务的执行顺序
            List<Integer> operator_of_station = get_Operator_of_station(i);
            //针对站内的每个操作者，决定该操作者下的任务执行顺序
            for (int j = 0; j < operator_of_station.size(); j++){
                //获取该操作者的任务集合
                List<Integer> tasks_of_operator = new ArrayList<>();
                for (int k = 0; k < task_set.size(); k++){
                    if (task_operator.get(task_set.get(k)).equals(operator_of_station.get(j))){
                        tasks_of_operator.add(task_set.get(k));
                    }
                }

                //将集合转换为数组
                int[] tasks = tasks_of_operator.stream().mapToInt(Integer::intValue).toArray();
                //对操作者的任务集合进行排序,冒泡排序法
                int temp = 0;
                for (int k = 0; k < tasks.length - 1; k++){
                    for (int l = 0; l < tasks.length - 1 - k; l++){
                        //先看两个任务之间有没有任务优先级，如果两个任务之间没有任务优先级，则比较yita值
                        //把优先级小的往后排
                        int priority = determin_task_priority(tasks[l+1], tasks[l]);
                        //
                        if (priority == 0){
                            temp = tasks[l];
                            tasks[l] = tasks[l+1];
                            tasks[l+1] = temp;
                        }else if (priority == 1){

                        }else if (priority == -1){
                            //此时两个任务之间没有优先级关系，那么应该比较yita值
                            //如果tasks[j+1]的yita值更大，则应该将该任务调整到前面
                            //此处要获取任务在part5中的顺序
                            int task1IndexInAll = getAllIndex(tasks[l] - 1, taog_sum.unit_node_parent);
                            int task2IndexInAll = getAllIndex(tasks[l + 1] - 1, taog_sum.unit_node_parent);

//                            if (part_5.get(tasks[l+1] - 1) > part_5.get(tasks[l] - 1)){
                            if (task2IndexInAll > task1IndexInAll){
                                temp = tasks[l];
                                tasks[l] = tasks[l+1];
                                tasks[l+1] = temp;
                            }
                        }
                    }
                }
                //将排序完成的数组元素存到集合中
                List<Integer> desc_list = new ArrayList<>();
                for (int k = 0; k < tasks.length; k++){
                    desc_list.add(tasks[k]);
                }
                operator_tasks_desc.put(operator_of_station.get(j), desc_list);
            }

            //有了每个操作者任务集合的执行顺序之后，针对操作者进行遍历
            //要设置标志位,这个记录了每个操作者执行任务到哪儿了
            int flag_array[] = new int[operator_of_station.size()];
            for (int j = 0; j < operator_of_station.size(); j++){
                flag_array[j] = 0;
            }

            //每个工作站创建一个集合用来存储已经执行的任务集合
            List<Integer> excuted_tasks = new ArrayList<>();
            while (true){
                //已经执行的任务的个数
                int already_tasks = excuted_tasks.size();
                //如果任务已经全部执行了，则跳出循环
                if (excuted_tasks.size() == all_pretask_station.size()){
                    break;
                }
                //如果还有任务未被执行
                else {
                    for (int j = 0; j < operator_of_station.size(); j++){
                        //当前的操作者
                        int operator_index = operator_of_station.get(j);
                        //如果当前操作者还有任务没有执行完
                        if (flag_array[j] < operator_tasks_desc.get(operator_index).size()){
                            //当前任务
                            int now_task = operator_tasks_desc.get(operator_index).get(flag_array[j]);
                            //根据当前操作者的标志位，来确定下一个任务能否被执行
                            //具体说来就是，看当前任务的前驱任务是否在已执行任务集合里，如果在，则当前任务可以被执行，如果不在，则不能执行当前任务
                            //还应包括同一个操作上的直接前驱任务是否已经被执行
                            int pre_task = all_pretask_station.get(now_task);
                            int same_operator_pre_task = 0;
                            if (flag_array[j] > 0){
                                same_operator_pre_task = operator_tasks_desc.get(operator_index).get(flag_array[j] - 1);
                            }

                            //如果前驱任务已经被执行或者没有前驱任务,则当前任务可以被执行(此处前驱任务包括约束优先级层面的前驱任务和同一个机器人上的前驱任务)
                            if ((pre_task == 0 || excuted_tasks.contains(pre_task)) && (same_operator_pre_task == 0 ||
                                    excuted_tasks.contains(same_operator_pre_task))){
                                //要获取当前任务所在的操作者的上一个任务
                                //int same_operator_pre_task = 0;

                                insert_time_node_asc(operator_index, now_task - 1, i, pre_task, same_operator_pre_task);
                                //标志位+1
                                flag_array[j] = flag_array[j] + 1;
                                //往已完成任务集合里加元素
                                excuted_tasks.add(now_task);
                                if (excuted_tasks.size() == all_pretask_station.size()){
                                    break;
                                }
                            }
                        }
                    }

                    //要进行修复，因为可能出现没有可执行的任务这一情况,要保证每次都有任务可以被执行
                    while (already_tasks == excuted_tasks.size()){
                        //调整操作者任务集合里，没有优先级约束的两个任务的顺序，判断是否有可以执行的任务，如果没有的话，接着调整
                        Loop:for (int j = 0; j < operator_of_station.size(); j++){
                            if (flag_array[j] < operator_tasks_desc.get(operator_of_station.get(j)).size()){
                                for (int k = flag_array[j] + 1; k < operator_tasks_desc.get(operator_of_station.get(j)).size(); k++){
                                    //如果当前任务可以移到前面来（在不违反约束优先级的基础上前移），并且他的前驱任务已经执行了，那么就将他前移
                                    int a = operator_tasks_desc.get(operator_of_station.get(j)).get(k);
                                    int b = operator_tasks_desc.get(operator_of_station.get(j)).get(flag_array[j]);
                                    int youxianji = determin_task_priority(a, b);
                                    //如果没有优先级约束
                                    if (youxianji == -1){
                                        //判断该任务的前驱任务是否已经被执行，即该任务是否应该被前移
                                        int qianqu = all_pretask_station.get(operator_tasks_desc.get(operator_of_station.get(j)).get(k));
                                        //如果前驱任务已经被执行,或者无前驱任务（当前站内）
                                        if (excuted_tasks.contains(qianqu) || qianqu == 0){
                                            int[] temp_operator_tasks = operator_tasks_desc.get(operator_of_station.get(j)).stream().mapToInt(Integer::intValue).toArray();
                                            //待移动的元素取出来
                                            int remove_task = temp_operator_tasks[k];
                                            //将前面的元素依此往后移动一位
                                            for (int l = k - 1; l >= flag_array[j]; l--){
                                                temp_operator_tasks[l + 1] = temp_operator_tasks[l];
                                            }
                                            temp_operator_tasks[flag_array[j]] = remove_task;

                                            //最后将数组转换为集合
                                            List<Integer> transform = new ArrayList<>();
                                            for (int l = 0; l < temp_operator_tasks.length; l++){
                                                transform.add(temp_operator_tasks[l]);
                                            }
                                            operator_tasks_desc.put(operator_of_station.get(j), transform);

                                            //同时要交换yita值
                                            //获取两个任务在Part5中的位置
                                            int index1InPart5 = getAllIndex(operator_tasks_desc.get(operator_of_station.get(j)).get(k) - 1, taog_sum.unit_node_parent);
                                            int index2InPart5 = getAllIndex(operator_tasks_desc.get(operator_of_station.get(j)).get(flag_array[j]) - 1, taog_sum.unit_node_parent);
                                            Collections.swap(part_5, index1InPart5, index2InPart5);
//                                            Collections.swap(part_5, operator_tasks_desc.get(operator_of_station.get(j)).get(k) - 1,
//                                                    operator_tasks_desc.get(operator_of_station.get(j)).get(flag_array[j]) - 1);
                                            break Loop;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }

                }
            }
        }
    }

    //随即生成1个不同的数
    public static double getRandomNum(int start,int end){
        //2.创建Random对象
        Random r = new Random();

        int num = r.nextInt(end-start) + start;
        BigDecimal a = new BigDecimal(num);
        BigDecimal b = new BigDecimal(1000);
        BigDecimal result = a.divide(b);

        double double_result = result.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

        return double_result;
    }

    //获取某个站内可以执行某个任务的操作者
    public List<Integer> operatorsOfStation(int workstation, int type_of_operator){
        List<Integer> operators = new ArrayList<>();
        //找出当前站内的机器人
        if (type_of_operator == 1){
            for (int i = 0; i < Problem.robot_num; i++){
                if (this.operator_workstation.get(i) == workstation){
                    operators.add(i);
                }
            }
        }
        //找出当前站内的工人
        else if (type_of_operator == 0){
            for (int i = Problem.robot_num; i < Problem.operator_num; i++){
                if (this.operator_workstation.get(i) == workstation){
                    operators.add(i);
                }
            }
        }
        //找出当前站内的所有操作者
        else if (type_of_operator == 2){
            for (int i = 0; i < Problem.operator_num; i++){
                if (this.operator_workstation.get(i) == workstation){
                    operators.add(i);
                }
            }
        }
        return operators;
    }

    //获取一堆任务中只能被某种操作者执行的任务
    public List<Integer> sameTypeOperator(List<Integer> tasks, int type_of_operator){
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < tasks.size(); i++){
            if (taog_sum.type_of_process.get(tasks.get(i) - 1) == type_of_operator){
                result.add(tasks.get(i));
            }
        }

        return result;
    }

    //获取一个站内的特定类型的操作者
    public List<Integer> specificOperatorOfStation(int B, int type){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < this.operator_workstation.size(); i++){
            if (this.operator_workstation.get(i) == B){
                //工人
                if (type == 0){
                    if (i >= 24 && i < Problem.operator_num){
                        result.add(i);
                    }
                }
                //机器人
                else if (type == 1){
                    if (i >= 0 && i < 24){
                        result.add(i);
                    }
                }
            }
        }
        return result;
    }

    //获取一个站内的操作(BW是一个hashmap,要使用特殊的遍历方式)
    public List<Integer> getB0fStation(int station){
        List<Integer> result = new ArrayList<>();

        for (Integer key : this.BW.keySet()) {
            if (this.BW.get(key) == station){
                result.add(key);
            }
        }

        return result;
    }


    // 用于初始化time_node
    public void initi_time_node() {
        for (int i = 0; i < (Problem.operator_num); i++) {
            //为每一个操作者创建一个List
            List<Operator_Task_Time_Node> list = new ArrayList<>();
            time_node.put(i, list);
        }
    }

    //获取某个拆卸操作中被选中的任务
    public List<Integer> selected_task_of_B(int B){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < task_set.size(); i++){
            if (taog_sum.parent_B_of_unit.get(task_set.get(i) - 1) == B){
                result.add(task_set.get(i));
            }
        }
        return result;
    }


    public void insert_time_node_asc(int operator_index, int task_index, int workstation_index, int pre_task,
                                    int same_operator_pre_task){
        //前驱任务的结束时间
        double end_time_preTask = 0.0;
        //当前任务的处理时间
        double process_time_task;
        //任务被所分配的操作者处理的开始时间
        double start_time_task = 0.0;
        //任务被所分配的操作者处理的结束时间
        double end_time_preTask_sameOperator = 0.0;

        //如果当前任务在自己所在的工作站中没有直接前驱任务
        if (0 == pre_task){
            end_time_preTask = 0.0;
        }
        //如果当前任务在自己所在的工作站中有直接前驱任务
        else {
            //要找到直接前驱任务的处理结束时间，当前任务的开始时间应该在直接前驱任务结束时间之后
            //获取前驱任务的结束时间
            end_time_preTask = find_complete_time(time_node.get(task_operator.get(pre_task)), pre_task);
        }
        //当前任务被当前操作者处理所花费的时间
        process_time_task = taog_sum.task_operator_time.get(task_index).get(operator_index);

        Operator_Task_Time_Node ins = new Operator_Task_Time_Node();
        ins.process_time = process_time_task;
        ins.task_index = task_index;
        ins.operator_index = operator_index;
        ins.workstation_index = workstation_index;

        if (same_operator_pre_task != 0){
            //当前任务的开始时间就是前驱任务的结束时间,此处还要比较当前机器人的上一个任务的处理结束时间
            end_time_preTask_sameOperator = find_complete_time(time_node.get(operator_index), same_operator_pre_task);
        }

        start_time_task = end_time_preTask > end_time_preTask_sameOperator ? end_time_preTask : end_time_preTask_sameOperator;

        ins.staring_time_task = start_time_task;
        ins.end_time = ins.staring_time_task + ins.process_time;
        time_node.get(operator_index).add(ins);
    }

    //找某个任务的直接前驱任务
    public Integer find_immediate_predecessor(int task, List<Integer> selected_task_of_B, List<Integer> BOfStation){
        int pre_task_index = -1;
        int pre_B = -1;
        //首先要确定当前任务所属的拆卸操作
        int current_B = taog_sum.parent_B_of_unit.get(task - 1);
        //确定当前任务的父任务节点
        List<Integer> unit_node_parent = taog_sum.unit_node_parent.get(task - 1);
        //确定当前任务所在的拆卸操作父拆卸操作
        List<Integer> B_parent_B = taog_sum.normal_node_parent_B.get(current_B - 1);
        //在本拆卸操作内找有没有任务是当前任务的前驱任务
        for (int i = 0; i < selected_task_of_B.size(); i++){
            if (unit_node_parent.contains(selected_task_of_B.get(i))){
                pre_task_index = selected_task_of_B.get(i) - 1;
                break;
            }
        }
        //如果在当前的拆卸操作里没有找到前驱任务，找当前站内的前驱拆卸操作，前驱拆卸操作的最后一个任务便是当前任务的前驱任务
        if (pre_task_index == -1){
            for (int i = 0; i < BOfStation.size(); i++){
                if (B_parent_B.contains(BOfStation.get(i))){
                    pre_B = BOfStation.get(i);
                    break;
                }
            }

            //找到同一个站内的前驱操作之后，确定该前驱拆卸操作的最后一个任务就是前驱任务
            if (pre_B != -1){
                List<Integer> tasks = selected_task_of_B(pre_B);
                //找到这个任务集合里面排在最后的那个
                for (int i = 0; i < tasks.size(); i++){
                    if (taog_sum.unit_node_children.get(tasks.get(i) - 1).contains(Integer.MAX_VALUE)){
                        pre_task_index = tasks.get(i) - 1;
                        break;
                    }
                }
            }
        }

        return pre_task_index;
    }

    //找一个已经完成的任务的完成时间
    public double find_complete_time(List<Operator_Task_Time_Node> time_node_of_operator, int task){
        //遍历time_node
        for (int i = 0; i < time_node_of_operator.size(); i++){
            if (time_node_of_operator.get(i).task_index == task - 1){
                return time_node_of_operator.get(i).staring_time_task + time_node_of_operator.get(i).process_time;
            }
        }
        return 0.0;
    }

    //获取某个工作站内的操作者,自带修复功能
    public List<Integer> get_Operator_of_station(int station){
        List<Integer> temp_result = new ArrayList<>();
        for (int i = 0; i < this.operator_workstation.size(); i++){
            if (this.operator_workstation.get(i).equals(station)){
                temp_result.add(i);
            }
        }
        List<Integer> result = new ArrayList<>();
        //如果该操作者实际上没有被分配到任务，那么将其剔除出去，相应的要改变part3中的gama值
        for (int i = 0; i < temp_result.size(); i++){
            int is_assigned_task = 0;
            for (Integer key : this.task_operator.keySet()) {
                if (this.task_operator.get(key).equals(temp_result.get(i))){
                    is_assigned_task = 1;
                    break;
                }
            }
            //如果被分配了任务
            if (is_assigned_task == 1){
                result.add(temp_result.get(i));
            }
            //如果没有被分配任务，则要将其剔除出去，并且相应的改变其part3中的值
            else {
                this.part_3.put(temp_result.get(i),1.0);
                this.operator_workstation.set(temp_result.get(i), 1 * Problem.workstation_num);
            }
        }
        return result;
    }

    //判定两个任务之间是否存在任务优先级约束,-1代表没有优先级约束，0代表A排在B前面，1代表B排在A前面
    public int determin_task_priority(int taskA, int taskB){
        int result = -1;
        //判断两个拆卸操作的优先级关系
        int is_A_pre_B = check_task_predecessor(taskA, taskB);
        int is_B_pre_A = check_task_predecessor(taskB, taskA);
        //如果A确实是B的前驱
        if (is_A_pre_B == -1){
            result = 0;
        }
        //如果B是A的前驱
        else if (is_B_pre_A == -1){
            result = 1;
        }
        //如果A跟B无任务优先级的关系
        else {
            result = -1;
        }

        return result;
    }

    //检查任务A是否是任务B的前驱，是（-1）否（0）,这里不仅是直接前驱，应该在所有前驱任务里找,实际上要先确定operation层面的顺序
    public int check_task_predecessor(int taskA, int taskB){

        test_flag = 0;
        //判断结果
        int result = 0;
        //先找到任务A所属的普通节点
        int normal_node_of_taskA = taog_sum.parent_B_of_unit.get(taskA - 1);

        //先要在taskA所在的普通节点的任务集里去找是否有任务taskB,如果有taskB,且taskB的确是taskA的子节点，则返回-1，否则返回0
        for (int i = 0; i < taog_sum.normal_node_task.get(normal_node_of_taskA).size(); i++){
            //如果taskB跟taskA处于同一个普通节点下
            if (taskB == taog_sum.normal_node_task.get(normal_node_of_taskA).get(i)){
                //taskB可能是taskA的直接后继，也有可能不是直接后继
                for (int j = 0; j < taog_sum.unit_node_children.get(taskA - 1).size(); j++){
                    result = find_by_unit_node(taskA - 1, taskB);
                    if (result == -1){
                        break;
                    }
                }
                return result;
            }
        }

        //如果taskB跟taskA不位于同一个普通节点,则找到所属普通节点后，找到其后续节点，判断任务B是否属于后续普通节点
        if (taog_sum.normal_node_children.get(normal_node_of_taskA - 1).size() > 0){
            test_flag = 0;
            return find_by_normal_node(normal_node_of_taskA, taskB);
        }

        return result;
    }

    //根据普通节点查找某个任务是否为其后继节点的任务
    public int find_by_normal_node(int normal_node, int taskB){
        for (int i = 0; i < taog_sum.normal_node_children.get(normal_node - 1).size(); i++){
            for (int k = 0; k < taog_sum.artificial_node_children.get(taog_sum.normal_node_children.get(normal_node - 1).get(i)).size(); k++){
                int normal_node_flag = taog_sum.artificial_node_children.get(taog_sum.normal_node_children.get(normal_node - 1).get(i)).get(k);
                for (int j = 0; j < normal_node_of_DT.size(); j++){
                    if (normal_node_flag == normal_node_of_DT.get(j)){
                        for (int p = 0; p < taog_sum.normal_node_task.get(normal_node_flag).size(); p++){
                            if (taskB == taog_sum.normal_node_task.get(normal_node_flag).get(p)){
                                test_flag = -1;
                                return -1;
                            }
                        }
                        //如果当前普通节点不是末端节点,且在当前普通节点的任务集里没找到taskB,则接着找
                        if (test_flag == 0 && taog_sum.normal_node_children.get(normal_node_flag - 1).size() > 0){
                            find_by_normal_node(normal_node_flag, taskB);
                        }
                    }
                }
            }
        }
        return test_flag;
    }

    //判断任务是否是某个unit_node的后继节点(不一定是直接后继)
    public int find_by_unit_node(int unit_node, int taskB){
        //int result = 0;
        if (unit_node != Integer.MAX_VALUE){
            for (int i = 0; i < taog_sum.unit_node_children.get(unit_node).size(); i++){
                if (taog_sum.unit_node_children.get(unit_node).get(i).equals(taskB)){
                    test_flag = -1;
                    break;
                }
            }
            //如果不是直接后继，则判断是否为后继
            if (test_flag == 0){
                for (int i = 0; i < taog_sum.unit_node_children.get(unit_node).size(); i++){
                    if (!taog_sum.unit_node_children.get(unit_node).get(i).equals(Integer.MAX_VALUE)){
                        find_by_unit_node(taog_sum.unit_node_children.get(unit_node).get(i) - 1, taskB);
                    }
                }
            }
        }
        return test_flag;
    }

    public void cal_objectives(){
        //周期时间集合
        List<Double> CT = new ArrayList<>();
        for (int i = 0; i < Problem.workstation_num; i++){
            CT.add(Double.MIN_VALUE);
        }

        //获取每个工作站的周期时间
        for (int i = 0; i < Problem.workstation_num; i++){
            //当前工作站的操作者
            List<Integer> operator_in_workstation = get_Operator_of_station(i);
            //
            for (int j = 0; j < operator_in_workstation.size(); j++){
                for (int k = 0; k < this.time_node.get(operator_in_workstation.get(j)).size(); k++){
                    if (this.time_node.get(operator_in_workstation.get(j)).get(k).end_time > CT.get(i)){
                        CT.set(i, this.time_node.get(operator_in_workstation.get(j)).get(k).end_time);
                    }
                }
            }
        }
        //目标一：周期时间最短
        objectives.add(Double.MIN_VALUE);
        for (int i = 0; i < Problem.workstation_num; i++){
            if (objectives.get(0) < CT.get(i)){
                objectives.set(0, CT.get(i));
            }
        }

        double flag = 0.0;
        //目标二：操作者（机器人和工人）总数最小
        for (int i = 0; i < operator_workstation.size(); i++){
            if (operator_workstation.get(i) != 3){
                flag = flag + 1;
            }
        }
        objectives.add(flag);

        //目标三：工人总数最小,operator_workstation集合前24个元素都是机器人，后十个元素是工人
        double flag1 = 0.0;
        for (int i = 24; i < operator_workstation.size(); i++){
            if (operator_workstation.get(i) != 3){
                flag1 = flag1 + 1;
            }
        }
        objectives.add(flag1);
    }

    Individual copy(){
        Individual a = new Individual(taog_list, taog_sum);
        //复制part1
        for (int i = 0; i < this.part_1.size(); i++){
            a.part_1.add(this.part_1.get(i));
        }
        //复制part2
        for (int i = 0; i < this.part_2.size(); i++){
            a.part_2.add(this.part_2.get(i));
        }
        //复制part3
        for (int i = 0; i < this.part_3.size(); i++){
            a.part_3.put(i, this.part_3.get(i));
        }
        //复制part4
        for (int i = 0; i < this.part_4.size(); i++){
            a.part_4.add(this.part_4.get(i));
        }
        //复制part5
        for (int i = 0; i < this.part_5.size(); i++){
            a.part_5.add(this.part_5.get(i));
        }
        //复制state
        for (int i = 0; i < this.state.size(); i++){
            a.state.add(this.state.get(i));
        }
//        for (int i = 0; i < this.interval.size(); i++){
//            a.interval.add(this.interval.get(i));
//        }
        return a;
    }

//    //个体初始化方法
//    public void individual_child_generate(List<Integer> state) throws IOException{
//
//        //先确定拆卸树,在生成拆卸树的过程中，会对编码序列的part1进行初始化
//        determin_DT_child();
//
//        //紧接着去选择task
//        determin_task_child(state);
//
//        //接下来要确定操作者跟工作站的对应关系
//        determinOW_child();
//
//        //操作者跟工作站的对应关系确定了之后，要完成任务到操作者的分配
//        determinTOChild();
//
//        //在确定了任务到操作者的分配之后，针对某个操作者要确定其拆卸顺序
//        determinYita_child();
//
//        //最后计算三个目标函数值
//        cal_objectives();
//    }

    //确定拆卸树的生成(子代个体)
    public void determin_DT_child(){

        for (int i = 0; i < taog_list.size(); i++){
            int start_art_node = taog_list.get(i).start_art_node;
            select_DT_of_one_model(start_art_node);

        }
    }

    //确定选择哪些任务(子代个体)
    public void determin_task_child(List<Integer> state){

        //存储可以执行的任务
        List<Integer> task_able = new ArrayList<>();

        //根据产品的状态首先确定有哪些任务是可选的task
        for (int i = 0; i < state.size(); i++){

            if (state.get(i) != -1){
                //找到当前普通节点下的所有任务
                List<Integer> current_normal_node_task = taog_sum.normal_node_task.get(i+1);
                //遍历所有找到的所有任务，找到在当前状态下可以执行的任务
                for (int j = 0; j < current_normal_node_task.size(); j++){
                    //获取任务可以在那种情况下被执行
                    int task_index = current_normal_node_task.get(j);
                    int task_type = taog_sum.unit_node_type.get(task_index - 1);

                    //找到当前状态下可以执行的任务，因为有些任务可以在多个状态下执行
                    List<Integer> type_able = new ArrayList<>();
                    get_type_able(state.get(i), type_able);

                    //找到可以执行的任务
                    if (type_able.contains(task_type)){
                        task_able.add(task_index);
                    }
                }
            }

        }

//        //要确定任务，首先要为每个任务生成以个[0,1]之间的随机数值
//        getRandomNumList(taog_sum.unit_node_num, 0, 1001, part_2);

        //随机数值生成完成之后，根据part2的随机数值来选择任务
        //选择任务，要在选中的B节点下去选择
        for (int i = 0; i < this.normal_node_of_DT.size(); i++){
            //找到当前B节点在当前状态下可以执行的任务
            List<Integer> task_processable_B = new ArrayList<>();
            List<Integer> all_task_B = taog_sum.normal_node_task.get(this.normal_node_of_DT.get(i));
            for (int j = 0; j < task_able.size(); j++){
                if (all_task_B.contains(task_able.get(j))){
                    task_processable_B.add(task_able.get(j));
                }
            }

            //找到每个B节点下的可以执行的任务后，开始遍历，要比较每个任务的随机数值，选择数值大的那个
            //首先针对某个B节点选中起始的Unit节点
            List<Integer> task_child_of_B = taog_sum.normal_node_task_children.get(this.normal_node_of_DT.get(i) - 1);

            List<Integer> candidate_start_unit = new ArrayList<>();

            for (int j = 0; j < task_child_of_B.size(); j++){
                if (task_processable_B.contains(task_child_of_B.get(j))){
                    candidate_start_unit.add(task_child_of_B.get(j));
                }
            }

            int start_unit_node = candidate_start_unit.get(0);

            //获取到候选的unit_node之后，找到随机数值最大的unit_node
            for (int j = 0; j < candidate_start_unit.size(); j++){

                //找到任务在part2中对应的全局index
                int indexOfStartInall = getAllIndex(start_unit_node - 1, taog_sum.unit_node_parent);
                int indexInAll = getAllIndex(candidate_start_unit.get(j) - 1, taog_sum.unit_node_parent);
                if (this.part_2.get(indexInAll) > this.part_2.get(indexOfStartInall)){
                    start_unit_node = candidate_start_unit.get(j);
                }

//                if (this.part_2.get(candidate_start_unit.get(j) - 1) > this.part_2.get(start_unit_node - 1)){
//                    start_unit_node = candidate_start_unit.get(j);
//                }
            }

            this.task_set.add(start_unit_node);

            //在确定了起始的unit_node之后，开始遍历，根据规则找到合适的unit_node

            int flag = 0;
            flag = find_node_of_task(start_unit_node, task_processable_B);
            while (flag != 0 && flag != Integer.MAX_VALUE){
                this.task_set.add(flag);
                flag = find_node_of_task(flag, task_processable_B);
            }
        }

    }

    //确定操作者跟工作站的对应关系(子代个体)
    public void determinOW_child(){

        List<Integer> lastFlag = new ArrayList<>();

        //针对操作者，根据各自的随机数值，计算其应该分配给哪个工作站
        for (int i = 0; i < this.part_3.size(); i++){
            int floor = (int)Math.floor(this.part_3.get(i) * Problem.workstation_num);
            this.operator_workstation.add(floor);
        }

        //要进行修复，因为经过variation之后的part3不一定可以满足要求
        for (int i = 0; i < Problem.workstation_num; i++){
            List<Integer> robotsOfStation = robots_of_station(i);
            List<Integer> humanOfStation = human_of_station(i);
            //要判断机器人的数量和工人的数量是否都符合要求
            //如果机器人数超过了最大限制，则随机选择一些机器人令其空闲
            robotsOfStation = this.robots_of_station(i);
            if (robotsOfStation.size() > Problem.max_robot_in_workstation){
                //确定最后要令为空闲的机器人个数
                int idle_robot_num = robotsOfStation.size() - (new Random().nextInt(Problem.max_robot_in_workstation) + 1);
                List<Integer> idle_robots_index_list = new ArrayList<>();
                getRandomNumListNew(idle_robot_num, 0, robotsOfStation.size(), idle_robots_index_list);
                //对这些选中的要置为空闲的机器人操作
                for (int j = 0; j < idle_robot_num; j++){
                    this.operator_workstation.set(robotsOfStation.get(idle_robots_index_list.get(j)), 3);
                    //还要对part3进行相应的更改
                    this.part_3.put(robotsOfStation.get(idle_robots_index_list.get(j)), 1.0);
                }
            }
            //如果机器人数为0
            else if (robotsOfStation.size() == 0){
                //要分配多少机器人
                int robots_num = new Random().nextInt(Problem.max_robot_in_workstation) + 1;
                for (int j = 0; j < robots_num; j++){
                    //确定空闲的机器人
                    List<Integer> free_robots = new ArrayList<>();
                    for (int k = 0; k < Problem.robot_num; k++){
                        if (this.operator_workstation.get(k) == 3){
                            free_robots.add(k);
                        }
                    }

                    if (free_robots.size() == 0){
                        robotsOfStation = robots_of_station(i);
                        if (robotsOfStation.size() > 0){
                            break;
                        }
                        lastFlag.add(i);
                        lastFlag.add(1);
                        break;
                    }
                    //从空闲机器人集合里去随机选择机器人
                    int select_robot_index = new Random().nextInt(free_robots.size());
                    //选定了机器人之后，就是为其分配随机数值
                    double gama = 1.0;
                    switch (i){
                        case 0: gama = getRandomNum(0, 334);
                            break;
                        case 1: gama = getRandomNum(334, 667);
                            break;
                        case 2: gama = getRandomNum(667, 1000);
                            break;
                    }

                    this.operator_workstation.set(free_robots.get(select_robot_index), i);
                    //将随机数值分配给对应的机器人
                    this.part_3.put(free_robots.get(select_robot_index), gama);

                }
            }
            //还要判断工人数是否超过了最大限制
            humanOfStation = this.human_of_station(i);
            if (humanOfStation.size() > Problem.max_human_in_workstation){
                //确定最后要令为空闲的工人个数
                int idle_human_num = humanOfStation.size() - (new Random().nextInt(Problem.max_human_in_workstation) + 1);
                List<Integer> idle_huamn_index_list = new ArrayList<>();
                getRandomNumListNew(idle_human_num, 0, humanOfStation.size(), idle_huamn_index_list);
                //对这些选中的要置为空闲的工人操作
                for (int j = 0; j < idle_human_num; j++){
                    this.operator_workstation.set(humanOfStation.get(idle_huamn_index_list.get(j)), 3);
                    //还要对part3进行相应的更改
                    this.part_3.put(humanOfStation.get(idle_huamn_index_list.get(j)), 1.0);
                }
            }
            //如果工人数为0
            else if (humanOfStation.size() == 0){
                //要分配多少工人
                humanOfStation = this.human_of_station(i);
                int human_num = new Random().nextInt(Problem.max_human_in_workstation) + 1;
                for (int j = 0; j < human_num; j++){
                    List<Integer> free_humans = new ArrayList<>();
                    for (int k = Problem.robot_num; k < Problem.operator_num; k++){
                        if (this.operator_workstation.get(k) == 3){
                            free_humans.add(k);
                        }
                    }

                    if (free_humans.size() == 0){
                        humanOfStation = this.human_of_station(i);
                        if (humanOfStation.size() > 0){
                            break;
                        }
                        //记录当前的站的序号，以及是哪种类型的操作者没有闲置的，最后再来分配
                        lastFlag.add(i);
                        lastFlag.add(0);
                        break;
                    }
//                    else {
                    int select_human_index = new Random().nextInt(free_humans.size());
                    double gama = 1.0;
                    switch (i){
                        case 0: gama = getRandomNum(0, 334);
                            break;
                        case 1: gama = getRandomNum(334, 667);
                            break;
                        case 2: gama = getRandomNum(667, 1000);
                            break;
                    }

                    this.operator_workstation.set(free_humans.get(select_human_index), i);
                    this.part_3.put(free_humans.get(select_human_index), gama);
//                    }
                }
            }
        }

        //最后针对前面没操作者的站进行分配
        for (int i = 0; i < lastFlag.size(); i = i+2){
            //如果是某个站没有分配工人
            if (lastFlag.get(i + 1) == 0){
                //要分配多少工人
                int human_num = new Random().nextInt(Problem.max_human_in_workstation) + 1;

                for (int j = 0; j < human_num; j++){

                    List<Integer> free_humans = new ArrayList<>();
                    for (int k = Problem.robot_num; k < Problem.operator_num; k++){
                        if (this.part_3.get(k) == 1.0){
                            free_humans.add(k);
                        }
                    }

                    //如果已经分配了工人，并且没有闲置的工人了，此时就跳出循环，不再进行分配了
                    if (j > 0 && free_humans.size() == 0){
                        break;
                    }

                    int select_human_index = new Random().nextInt(free_humans.size());
                    double gama = 1.0;
                    switch (i){
                        case 0: gama = getRandomNum(0, 334);
                            break;
                        case 1: gama = getRandomNum(334, 667);
                            break;
                        case 2: gama = getRandomNum(667, 1000);
                            break;
                    }

                    this.operator_workstation.set(free_humans.get(select_human_index), lastFlag.get(i));
                    this.part_3.put(free_humans.get(select_human_index), gama);

                }
            }
            //如果没有分配机器人
            if (lastFlag.get(i + 1) == 1){
                //要分配多少机器人
                int robots_num = new Random().nextInt(Problem.max_robot_in_workstation) + 1;

                for (int j = 0; j < robots_num; j++){

                    //确定空闲的机器人
                    List<Integer> free_robots = new ArrayList<>();
                    for (int k = 0; k < Problem.robot_num; k++){
                        if (this.part_3.get(k) == 1.0){
                            free_robots.add(k);
                        }
                    }

                    //如果已经分配了机器人，并且没有闲置的机器人了，此时就跳出循环，不再进行分配了
                    if (j > 0 && free_robots.size() == 0){
                        break;
                    }

                    //从空闲机器人集合里去随机选择机器人
                    int select_robot_index = new Random().nextInt(free_robots.size());
                    //选定了机器人之后，就是为其分配随机数值
                    double gama = 1.0;
                    switch (i){
                        case 0: gama = getRandomNum(0, 334);
                            break;
                        case 1: gama = getRandomNum(334, 667);
                            break;
                        case 2: gama = getRandomNum(667, 1000);
                            break;
                    }

                    this.operator_workstation.set(free_robots.get(select_robot_index), lastFlag.get(i));
                    //将随机数值分配给对应的机器人
                    this.part_3.put(free_robots.get(select_robot_index), gama);
                }
            }

        }

        for (int i = 0; i < Problem.workstation_num; i++){
            List<Integer> a = robots_of_station(i);
            List<Integer> b = human_of_station(i);
            if (a.size() == 0){
                System.out.println("--------");
            }
            if (b.size() == 0){
                System.out.println("--------");
            }
        }

    }

    //确定任务跟操作者的关系(子代个体)
    public void determinTOChild(){
        //直接根据随机数乘以总的操作者数即可确定任务分配给哪个操作者
        //这个地方有两个问题，通过公式计算后的操作者首先不一定是可以执行该类型的操作者，另外这样的话不一定同属一个操作的不同任务可能就分配到了不同的工作站


    }

    //确定操作者对于自己分配到的任务的拆卸顺序
    public void determinYita_child(){
        //初始化time_node
        initi_time_node();
        //针对每个站开始分析任务该怎么安排
        for (int i = 0; i < Problem.workstation_num; i++){
            //找到当前工作站有哪些拆卸操作
            List<Integer> BOfStation = new ArrayList<>();
            for (int j = 0; j < normal_node_of_DT.size(); j++){
                if (BW.size() == 0){
                    System.out.println("--------------");
                }
                if (BW.get(normal_node_of_DT.get(j)) == i){
                    BOfStation.add(normal_node_of_DT.get(j));
                }
            }

            //每个站内任务的所有直接前驱任务(不是index)
            HashMap<Integer, Integer> all_pretask_station = new HashMap<>();
            //针对每个站的拆卸操作
            for (int j = 0; j < BOfStation.size(); j++){
                List<Integer> selected_task_of_B = selected_task_of_B(BOfStation.get(j));
                //遍历每个拆卸操作里的任务
                for (int k = 0; k < selected_task_of_B.size(); k++){
                    //找到任务的直接前驱任务
                    int pre_task_index = find_immediate_predecessor(selected_task_of_B.get(k), selected_task_of_B, BOfStation);
                    all_pretask_station.put(selected_task_of_B.get(k),pre_task_index + 1);
                }
            }

            //应该是分析每个站内的操作者，要根据yita值去决定两个无任务优先级约束的任务的执行顺序
            List<Integer> operator_of_station = get_Operator_of_station(i);
            //针对站内的每个操作者，决定该操作者下的任务执行顺序
            for (int j = 0; j < operator_of_station.size(); j++){
                //获取该操作者的任务集合
                List<Integer> tasks_of_operator = new ArrayList<>();
                for (int k = 0; k < task_set.size(); k++){
                    if (task_operator.get(task_set.get(k)).equals(operator_of_station.get(j))){
                        tasks_of_operator.add(task_set.get(k));
                    }
                }

                //将集合转换为数组
                int[] tasks = tasks_of_operator.stream().mapToInt(Integer::intValue).toArray();
                //对操作者的任务集合进行排序,冒泡排序法
                int temp = 0;
                for (int k = 0; k < tasks.length - 1; k++){
                    for (int l = 0; l < tasks.length - 1 - k; l++){
                        //先看两个任务之间有没有任务优先级，如果两个任务之间没有任务优先级，则比较yita值
                        //把优先级小的往后排
                        int priority = determin_task_priority(tasks[l+1], tasks[l]);
                        //
                        if (priority == 0){
                            temp = tasks[l];
                            tasks[l] = tasks[l+1];
                            tasks[l+1] = temp;
                        }else if (priority == 1){

                        }else if (priority == -1){
                            //此时两个任务之间没有优先级关系，那么应该比较yita值
                            //如果tasks[j+1]的yita值更大，则应该将该任务调整到前面
                            //此处要获取任务在part5中的顺序
                            int task1IndexInAll = getAllIndex(tasks[l] - 1, taog_sum.unit_node_parent);
                            int task2IndexInAll = getAllIndex(tasks[l + 1] - 1, taog_sum.unit_node_parent);

//                            if (part_5.get(tasks[l+1] - 1) > part_5.get(tasks[l] - 1)){
                            if (task2IndexInAll > task1IndexInAll){
                                temp = tasks[l];
                                tasks[l] = tasks[l+1];
                                tasks[l+1] = temp;
                            }
                        }
                    }
                }
                //将排序完成的数组元素存到集合中
                List<Integer> desc_list = new ArrayList<>();
                for (int k = 0; k < tasks.length; k++){
                    desc_list.add(tasks[k]);
                }
                operator_tasks_desc.put(operator_of_station.get(j), desc_list);
            }

            //有了每个操作者任务集合的执行顺序之后，针对操作者进行遍历
            //要设置标志位,这个记录了每个操作者执行任务到哪儿了
            int flag_array[] = new int[operator_of_station.size()];
            for (int j = 0; j < operator_of_station.size(); j++){
                flag_array[j] = 0;
            }

            //每个工作站创建一个集合用来存储已经执行的任务集合
            List<Integer> excuted_tasks = new ArrayList<>();
            while (true){
                //已经执行的任务的个数
                int already_tasks = excuted_tasks.size();
                //如果任务已经全部执行了，则跳出循环
                if (excuted_tasks.size() == all_pretask_station.size()){
                    break;
                }
                //如果还有任务未被执行
                else {
                    for (int j = 0; j < operator_of_station.size(); j++){
                        //当前的操作者
                        int operator_index = operator_of_station.get(j);
                        //如果当前操作者还有任务没有执行完
                        if (flag_array[j] < operator_tasks_desc.get(operator_index).size()){
                            //当前任务
                            int now_task = operator_tasks_desc.get(operator_index).get(flag_array[j]);
                            //根据当前操作者的标志位，来确定下一个任务能否被执行
                            //具体说来就是，看当前任务的前驱任务是否在已执行任务集合里，如果在，则当前任务可以被执行，如果不在，则不能执行当前任务
                            //还应包括同一个操作上的直接前驱任务是否已经被执行
                            //int pre_task = all_pretask_station.get(now_task);

                            if (all_pretask_station.get(now_task) == null){
                                System.out.println("暂停一下");
                            }
                            int pre_task = all_pretask_station.get(now_task);


                            int same_operator_pre_task = 0;
                            if (flag_array[j] > 0){
                                same_operator_pre_task = operator_tasks_desc.get(operator_index).get(flag_array[j] - 1);
                            }

                            //如果前驱任务已经被执行或者没有前驱任务,则当前任务可以被执行(此处前驱任务包括约束优先级层面的前驱任务和同一个机器人上的前驱任务)
                            if ((pre_task == 0 || excuted_tasks.contains(pre_task)) && (same_operator_pre_task == 0 ||
                                    excuted_tasks.contains(same_operator_pre_task))){
                                //要获取当前任务所在的操作者的上一个任务
                                //int same_operator_pre_task = 0;

                                insert_time_node_asc(operator_index, now_task - 1, i, pre_task, same_operator_pre_task);
                                //标志位+1
                                flag_array[j] = flag_array[j] + 1;
                                //往已完成任务集合里加元素
                                excuted_tasks.add(now_task);
                                if (excuted_tasks.size() == all_pretask_station.size()){
                                    break;
                                }
                            }
                        }
                    }

                    //要进行修复，因为可能出现没有可执行的任务这一情况,要保证每次都有任务可以被执行
                    while (already_tasks == excuted_tasks.size()){
                        //调整操作者任务集合里，没有优先级约束的两个任务的顺序，判断是否有可以执行的任务，如果没有的话，接着调整
                        Loop:for (int j = 0; j < operator_of_station.size(); j++){
                            if (flag_array[j] < operator_tasks_desc.get(operator_of_station.get(j)).size()){
                                for (int k = flag_array[j] + 1; k < operator_tasks_desc.get(operator_of_station.get(j)).size(); k++){
                                    //如果当前任务可以移到前面来（在不违反约束优先级的基础上前移），并且他的前驱任务已经执行了，那么就将他前移
                                    int a = operator_tasks_desc.get(operator_of_station.get(j)).get(k);
                                    int b = operator_tasks_desc.get(operator_of_station.get(j)).get(flag_array[j]);
                                    int youxianji = determin_task_priority(a, b);
                                    //如果没有优先级约束
                                    if (youxianji == -1){
                                        //判断该任务的前驱任务是否已经被执行，即该任务是否应该被前移
                                        int qianqu = all_pretask_station.get(operator_tasks_desc.get(operator_of_station.get(j)).get(k));
                                        //如果前驱任务已经被执行,或者无前驱任务（当前站内）
                                        if (excuted_tasks.contains(qianqu) || qianqu == 0){
                                            int[] temp_operator_tasks = operator_tasks_desc.get(operator_of_station.get(j)).stream().mapToInt(Integer::intValue).toArray();
                                            //待移动的元素取出来
                                            int remove_task = temp_operator_tasks[k];
                                            //将前面的元素依此往后移动一位
                                            for (int l = k - 1; l >= flag_array[j]; l--){
                                                temp_operator_tasks[l + 1] = temp_operator_tasks[l];
                                            }
                                            temp_operator_tasks[flag_array[j]] = remove_task;

                                            //最后将数组转换为集合
                                            List<Integer> transform = new ArrayList<>();
                                            for (int l = 0; l < temp_operator_tasks.length; l++){
                                                transform.add(temp_operator_tasks[l]);
                                            }
                                            operator_tasks_desc.put(operator_of_station.get(j), transform);

                                            //同时要交换yita值
                                            //获取两个任务在Part5中的位置
                                            int index1InPart5 = getAllIndex(operator_tasks_desc.get(operator_of_station.get(j)).get(k) - 1, taog_sum.unit_node_parent);
                                            int index2InPart5 = getAllIndex(operator_tasks_desc.get(operator_of_station.get(j)).get(flag_array[j]) - 1, taog_sum.unit_node_parent);
                                            Collections.swap(part_5, index1InPart5, index2InPart5);

//                                            Collections.swap(part_5, operator_tasks_desc.get(operator_of_station.get(j)).get(k) - 1,
//                                                    operator_tasks_desc.get(operator_of_station.get(j)).get(flag_array[j]) - 1);
                                            break Loop;
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }

                }
            }
        }
    }

    //获取某个工作站内的机器人
    public List<Integer> robots_of_station(int station){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < Problem.robot_num; i++){
            if (this.operator_workstation.get(i).equals(station)){
                result.add(i);
            }
        }
        return result;
    }

    //获取某个工作站的工人
    public List<Integer> human_of_station(int station){
        List<Integer> result = new ArrayList<>();
        for (int i = 24; i < 34; i++){
            if (this.operator_workstation.get(i).equals(station)){
                result.add(i);
            }
        }
        return result;
    }

    //获取某个工作站的操作者
    public List<Integer> operator_of_station(int station){
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < 34; i++){
            if (this.operator_workstation.get(i).equals(station)){
                result.add(i);
            }
        }
        return result;
    }

//    //获取每个操作中被选中的任务
//    public List<Integer> selected_tasks_of_operation(int B, List<Integer> task_set){
//        List<Integer> result = new ArrayList<>();
//        List<Integer> tasks_of_normal_node = taog_sum.normal_node_task.get(B);
//        for (int i = 0; i < task_set.size(); i++){
//            if (tasks_of_normal_node.contains(task_set.get(i))){
//                result.add(task_set.get(i));
//            }
//        }
//        return result;
//    }

//    //获取每个操作中被选中的任务都被分配到了哪些操作者
//    public List<Integer> selected_tasks_to_operator(List<Integer> tasks_of_B){
//        List<Integer> result = new ArrayList<>();
//        for (int i = 0; i < tasks_of_B.size(); i++){
//            result.add(this.task_operator.get(tasks_of_B.get(i)));
//        }
//        return result;
//    }

    //判断该解是否存在空工作站的情况
    public int is_empty_operator(){
        List<Integer> station1 = operator_of_station(1);
        List<Integer> station2 = operator_of_station(2);
        List<Integer> station3 = operator_of_station(3);
        if (station1.size() == 0 || station2.size() == 0 || station3.size() == 0){
            return 1;
        }else {
            return 0;
        }
    }

    //根据BW来判断是否有空工作站
    public int is_empty_station(){
        //如果三个工作站都分配有拆卸操作
        if (BW.containsValue(0) && BW.containsValue(1) && BW.containsValue(2)){
            return 1;
        }else {
            //如果有空工作站，则先将BW清空，再返回重新分配
            BW.clear();
            return 0;
        }
    }

    //根据真实Index获取全局index
    public int getAllIndex(int trueIndex, HashMap<Integer,List<Integer>> trueMap){
        int flag = 0;
        for (Integer key : trueMap.keySet()) {
            if (key == trueIndex){
                return flag;
            }
            flag++;
        }
        return flag;
    }
}
