function [Y, I] = BDAmmd(lastArray,nowArray,pfArray,A)
% Find the latent space of domain adaptation
%���룺��һ�����µ�һЩ��Fs����ǰ������һЩ��Fa�����������������ӳ�����A
%      ��һ�����µ�POF
%���̣�����һ�����µ�POF��������Aӳ��ΪPOF_deduced,�ٽ���ǰ������һЩ�⾭������Wӳ�䵽latent_space�У���POF_deduced
%      ��Ľ���бȽϣ��ҵ���������Ľ⣨�ý⣩����Щ��ʵ���Ͼ��ǵ�ǰʱ�̵ĳ�ʼ��Ⱥ��������NSGAII���е���

%��֮ǰʱ�̵�һЩ���н�ת��Ϊ����
for i=1: length(lastArray)/3
    Fs(1,i) = lastArray((i-1)*3 + 1);
    Fs(2,i) = lastArray((i-1)*3 + 2);
    Fs(3,i) = lastArray((i-1)*3 + 3);  
end

%����ǰʱ�̵�һЩ���н�ת��Ϊ����
for i=1: length(nowArray)/3
    Fa(1,i) = nowArray((i-1)*3 + 1);
    Fa(2,i) = nowArray((i-1)*3 + 2);
    Fa(3,i) = nowArray((i-1)*3 + 3);  
end

%��֮ǰʱ�̵�һЩ�����н�ת��Ϊ����
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
%����������֮��ľ���
dis_px = @(p, q)sum((q - p).^2);

every_distance = [];
all_mean_distance = [];

%���˱任����֮��Ҫ�������
for i = 1 : size(Fa,2)
    for j = 1 : size(pf,2)
        every_distance(end + 1) = dis_px(POF_deduced(:,j), getNewY(Fs, Fa, Fa(:,i), A, kind, p1, p2, p3));
    end
    all_mean_distance(end + 1) = mean(every_distance);
    every_distance = [];
end

[Y, I] = sort(all_mean_distance);






