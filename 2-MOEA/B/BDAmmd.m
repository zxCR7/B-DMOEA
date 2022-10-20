function [Y, I] = BDAmmd(lastArray,nowArray,pfArray,A)
% Find the latent space of domain adaptation
%输入：上一环境下的一些解Fs，当前环境的一些解Fa，这两组解用来生成映射矩阵A
%      上一环境下的POF
%过程：将上一环境下的POF经过矩阵A映射为POF_deduced,再将当前环境的一些解经过矩阵W映射到latent_space中，跟POF_deduced
%      里的解进行比较，找到距离最近的解（好解），这些解实际上就是当前时刻的初始种群，接着用NSGAII进行迭代

%将之前时刻的一些可行解转换为矩阵
for i=1: length(lastArray)/3
    Fs(1,i) = lastArray((i-1)*3 + 1);
    Fs(2,i) = lastArray((i-1)*3 + 2);
    Fs(3,i) = lastArray((i-1)*3 + 3);  
end

%将当前时刻的一些可行解转换为矩阵
for i=1: length(nowArray)/3
    Fa(1,i) = nowArray((i-1)*3 + 1);
    Fa(2,i) = nowArray((i-1)*3 + 2);
    Fa(3,i) = nowArray((i-1)*3 + 3);  
end

%将之前时刻的一些帕累托解转换为矩阵
for i=1: length(pfArray)/3
    pf(1,i) = pfArray((i-1)*3 + 1);
    pf(2,i) = pfArray((i-1)*3 + 2);
    pf(3,i) = pfArray((i-1)*3 + 3);  
end

kind = 'Gaussian';
p1 = 1;
p2 = 'unused'; 
p3 = 'unused';
POF_deduced = getNewY(Fs, Fa, pf, A, kind, p1, p2, p3);
%计算两个解之间的距离
dis_px = @(p, q)sum((q - p).^2);

every_distance = [];
all_mean_distance = [];

%有了变换矩阵之后要计算距离
for i = 1 : size(Fa,2)
    for j = 1 : size(pf,2)
        every_distance(end + 1) = dis_px(POF_deduced(:,j), getNewY(Fs, Fa, Fa(:,i), A, kind, p1, p2, p3));
    end
    all_mean_distance(end + 1) = mean(every_distance);
    every_distance = [];
end

[Y, I] = sort(all_mean_distance);






