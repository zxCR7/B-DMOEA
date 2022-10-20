function [Z,A,Distance] = GetBDAdistance(lastArray, LastLabel, nowArray, NowLabel)
    
    %% 将传过来的源域数据和目标域数据做成矩阵形式的
        %将之前时刻的一些可行解转换为矩阵
        for i=1: length(lastArray)/3
            Xs(i,1) = lastArray((i-1)*3 + 1);
            Xs(i,2) = lastArray((i-1)*3 + 2);
            Xs(i,3) = lastArray((i-1)*3 + 3);  
        end

        %将当前时刻的一些可行解转换为矩阵
        for i=1: length(nowArray)/3
            Xt(i,1) = nowArray((i-1)*3 + 1);
            Xt(i,2) = nowArray((i-1)*3 + 2);
            Xt(i,3) = nowArray((i-1)*3 + 3);  
        end

        %将之前时刻的解的标签转换成矩阵形式
        for i = 1 : length(LastLabel)
            Ys(i,1) = LastLabel(i);
        end
        
        %将当前时刻的解的标签转换成矩阵形式
        for i = 1 : length(NowLabel)
            Yt(i,1) = NowLabel(i);
        end
        
    %% Set algorithm options
    options.gamma = 1.0;
    options.lambda = 0.1;
    options.kernel_type = 'linear';
    options.T = 10;
    options.dim = 100;
    options.mu = 1;
    options.mode = 'BDA';

    %% Run algorithm
    [Z,A,Distance] = BDA(Xs,Ys,Xt,Yt,options);

end