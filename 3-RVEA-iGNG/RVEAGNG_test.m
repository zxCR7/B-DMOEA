function RVEAGNG_test(Global,objs)
    % <algorithm> <R>
    % RVEA-GNG
    % alpha ---   2 --- The parameter controlling the rate of change of penalty
    % fr    --- 0.1 --- The frequency of employing reference vector adaptation
    %--------------------------------------------------------------------------
    % Copyright (c) 2016-2017 BIMK Group. You are free to use the PlatEMO for
    % research purposes. All publications which use this platform or any code
    % in the platform should acknowledge the use of "PlatEMO" and reference "Ye
    % Tian, Ran Cheng, Xingyi Zhang, and Yaochu Jin, PlatEMO: A MATLAB Platform
    % for Evolutionary Multi-Objective Optimization [Educational Forum], IEEE
    % Computational Intelligence Magazine, 2017, 12(4): 73-87".
    %--------------------------------------------------------------------------
    % This version is to find the missing solutions in global selection
    % NΪ������
    % MΪά��
    % objsӦ�������и����Ŀ�꺯��ֵ
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

    % ���ݸ������Լ�ά�����ɳ�ʼ�Ĳο�����
    [V,~] = UniformPoint(N,M);
    % �������Ŀ�꺯��ֵ��װ����Ⱥ
    Population = [];
    for i = 1 : N
        Population = [Population DLBP(objs(i,:))];
    end
    % ���ó�ʼ�ο���������Ⱥ��ʼ�����ɳ�ʼ������
    net = InitilizeGrowingGasNet(V,Population,params);
    % �ҵ���Ⱥ�еķ�֧���
    Archive = UpdateArchive(Population,[],N);
    scale = ones(1,M);
    zmin = min(Population.objs,[],1);
    genFlag =[];

    % �˴�Ӧ�õ���java�˵Ľ�����������Ӵ���Ⱥ
    for i = 1 : 30
        % �ֲ���дexcel�ķ�ʽ����population��indexд��excel�У���̨����population�е�indexȷ��ѡ����Щ������Ϊ������Ⱥ
        % ȷ��������Ⱥ�󣬺�̨�����ɵ��Ӵ���Ⱥ������Ŀ�꺯��ֵ��index�������
        
        path = ['F:\RVEAiGNGOutput\2\parent' num2str(i)  '.xls'];
        xlswrite(path,[Population.objs Population.gens Population.inds]);
       
        delete('F:\RVEAiGNGOutput\2\flag.txt') 
        
        path = ['F:\RVEAiGNGOutput\2\offspring' num2str(i)  '.xls'];
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
    end
end

