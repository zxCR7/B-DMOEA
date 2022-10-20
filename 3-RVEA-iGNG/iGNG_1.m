function iGNG_1(objs)
    %这个方法用来生成初代的网络  
    
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
end

