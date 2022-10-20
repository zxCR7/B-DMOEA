function iGNG_2(Global,objs)
    % 这一部分应该作为iGNG的第二步，即确定的初代的网络之后的后续进化过程
    
    % N为个体数
    % M为维数
    % objs应该是所有个体的目标函数值
    N = size(objs,1);
    M = 3;

    %% Parameter setting
    params.N = N;
    params.MaxIt = 50;
    params.L = 50;
    params.epsilon_b = 0.2;
    params.epsilon_n = 0.006;
    params.alpha = 0.5;
    params.delta = 0.995;
    params.T =50;
    params.hab_threshold = 0.1;
    params.insertion_threshold = 0.95;
    params.tau_b = 0.3;
    params.tau_n = 0.1;
    %% Parameter setting
    % [alpha,fr] = Global.ParameterSet(2,0.1);
    alpha = 2;
    fr = 0.1;

    % 根据个体数以及维数生成初始的参考向量
    [V,~] = UniformPoint(N,M);
    % 将输入的目标函数值封装成种群
    Population = [];
    for i = 1 : N
        Population = [Population DLBP(objs(i,:))];
    end
    % 利用初始参考向量和种群初始化生成初始神经网络
    net = InitilizeGrowingGasNet(V,Population,params);
    % 找到种群中的非支配解
    Archive = UpdateArchive(Population,[],N);
    scale = ones(1,M);
%     eee = [Population.objs Population.cons Population.gens Population.inds];
    zmin = min(Population.objs,[],1);
    genFlag =[];

    % 此处应该调用java端的交叉变异生成子代种群，迭代个几代，得到预搜索后的种群(先预搜索个10代)
    for i = 1 : 50
        % 现采用写excel的方式，将population的index写到excel中，后台根据population中的index确定选择哪些个体作为父代种群
        % 确定父代种群后，后台将生成的子代种群的三个目标函数值、约束违反值和index传到这边
        
        path = ['F:\科研\论文输出2\parent' num2str(i)  '.xls'];
        xlswrite(path,[Population.objs Population.cons Population.gens Population.inds]);
        
        path = ['F:\科研\论文输出2\offspring' num2str(i)  '.xls'];
        while true
            if exist(path,'file')
                c = xlsread(path);
                aaa = c;
                break;
            end
        end
        Offspring = [];
        for j = 1 : N
            Offspring = [Offspring DLBP(aaa(j,:))];
        end
        zmin = min([zmin;Offspring.objs],[],1); 
        [Population,net,V,Archive,scale,genFlag] = EnvironmentalSelection([Population,Offspring],V,(Global.gen/Global.maxgen)^alpha,net,params,Archive,Global,scale,zmin,genFlag);
        disp("暂停");
    end
end

