function iGNG_2(Global,objs)
    % ��һ����Ӧ����ΪiGNG�ĵڶ�������ȷ���ĳ���������֮��ĺ�����������
    
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
%     eee = [Population.objs Population.cons Population.gens Population.inds];
    zmin = min(Population.objs,[],1);
    genFlag =[];

    % �˴�Ӧ�õ���java�˵Ľ�����������Ӵ���Ⱥ���������������õ�Ԥ���������Ⱥ(��Ԥ������10��)
    for i = 1 : 50
        % �ֲ���дexcel�ķ�ʽ����population��indexд��excel�У���̨����population�е�indexȷ��ѡ����Щ������Ϊ������Ⱥ
        % ȷ��������Ⱥ�󣬺�̨�����ɵ��Ӵ���Ⱥ������Ŀ�꺯��ֵ��Լ��Υ��ֵ��index�������
        
        path = ['F:\����\�������2\parent' num2str(i)  '.xls'];
        xlswrite(path,[Population.objs Population.cons Population.gens Population.inds]);
        
        path = ['F:\����\�������2\offspring' num2str(i)  '.xls'];
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
        disp("��ͣ");
    end
end

