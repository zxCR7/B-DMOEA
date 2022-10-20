import java.io.Serializable;

public class Operator_Task_Time_Node implements Serializable{
	//任务的序号
	public int task_index;
	//任务所属的operation的序号
	public int normal_node_index;
	//操作者的序号
	public int operator_index;
	//工作站的序号
	public int workstation_index;
	//该任务被该机器人处理的时间
	public double process_time;
	//任务处理的开始时间
	public double staring_time_task;
	//任务处理的结束时间
	public double end_time;
	//任务处理的能量消耗
	//public double energy_consume;

	//自定义的构造器
	Operator_Task_Time_Node(int task_index, int operator_index, int workstation_index, double process_time,
							double staring_time_task) {
		this.task_index = task_index;
		this.operator_index = operator_index;
		this.workstation_index = workstation_index;
		this.process_time = process_time;
		this.staring_time_task = staring_time_task;
	}

	Operator_Task_Time_Node() {
		// TODO Auto-generated constructor stub
	}

	//重置对象
	public void reset_Robot_Task_Time_Node() {
		this.task_index = 0;
		this.operator_index = 0;
		this.workstation_index = 0;
		this.process_time = 0.0;
		this.staring_time_task = 0.0;
	}
}
