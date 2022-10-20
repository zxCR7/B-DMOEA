import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TAOG implements Serializable{

	//TAOG的name
	public int name;

	//该TAOG对应的Model所包含的component,也就是part,例如一个手电筒最后可以我们分为7个component
	public int component_num;

	//人工节点的数目
	public int artificial_node_num;

	//标准节点的数目
	public int normal_node_num;

	//单元节点的数目
	public int unit_node_num;

	//人工节点的开始编号
	public int start_art_node;

	//人工节点的父节点
	public HashMap<Integer,List<Integer>> artificial_node_parent = new HashMap<>();

	//人工节点的子节点
	public HashMap<Integer,List<Integer>> artificial_node_children = new HashMap<>();

	//普通节点的父节点
	public HashMap<Integer,List<Integer>> normal_node_parent = new HashMap<>();

	//普通节点的直接前继普通节点
	public HashMap<Integer,List<Integer>> normal_node_parent_B = new HashMap<>();

	//普通节点的子节点
	public HashMap<Integer,List<Integer>> normal_node_children = new HashMap<>();

	//普通节点的任务层面的子节点(任务层面初始节点的子节点)
	public HashMap<Integer,List<Integer>> normal_node_task_children = new HashMap<>();

	//单元节点的父节点
	public HashMap<Integer,List<Integer>> unit_node_parent = new HashMap<>();

	//单元节点的子节点
	public HashMap<Integer,List<Integer>> unit_node_children = new HashMap<>();

	//因为现在是人机协作，机器人和工人是两种类型的operator,每个机器人和工人处理每个任务所花费的时间是不一样的
	public HashMap<Integer,List<Integer>> task_operator_time = new HashMap<>();

	//每个NormalNode下有自己的task
	public HashMap<Integer,List<Integer>> normal_node_task = new HashMap<>();

	//存储每个unit_node的类型(共有7种)
    public HashMap<Integer, Integer> unit_node_type = new HashMap<>();

    //单位节点属于哪个操作
	public HashMap<Integer, Integer> parent_B_of_unit = new HashMap<>();

    //存储每个unit_node的被执行类型（0代表只能被工人执行，1代表只能被机器人执行，2代表能同时被工人和机器人执行）
    public HashMap<Integer, Integer> type_of_process = new HashMap<>();
}
