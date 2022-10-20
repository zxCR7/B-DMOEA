function [ Archive ] = UpdateArchive(Population,Archive,MaxSize)
%UPDATEARCHIVE 此处显示有关此函数的摘要
%   此处显示详细说明
% kappa = 0.05;
% % Population = Population(NDSort(Population.objs,1)==1);
% WholePopulation = [Population Archive];
% WholePopulation = WholePopulation(NDSort(WholePopulation.objs,1)==1);
% WholeObj = WholePopulation.objs;
% % plot(1:6,WholePopulation.objs,'g')
% [Fitness,I,C] = CalFitness(WholePopulation.objs,kappa);
% indSize = size(WholePopulation,2);
% Delete =[];
% if indSize>N
%     %         for i = 1:indSize-(100-popSize)
%     for i = 1: indSize-N
%         index = setdiff(1: indSize,Delete);
%         [~,delete] = min(Fitness(index),[],2);
%         Delete = [Delete;index(delete)];
%         Fitness(index(delete)) = 0;
%         for j = 1:size(WholePopulation,2)
%             if Fitness(j)~=0
%                 Fitness(j) = Fitness(j) + exp(-I(index(delete),j)./(C(j)*kappa));
%             end
%         end
%     end
%     WholePopulation(Delete) = [];
% end
% 
% Archive = WholePopulation;




    Archive = [Archive,Population];
    o = Archive.objs;
    [c,ia,ic] = unique(o,'rows');
    Archive = Archive(ia);
    ND = NDSort(Archive.objs,1);
    Archive = Archive(ND==1);
    N  = length(Archive);
    if N <= MaxSize
        return;
    end
    
    %% Calculate the fitness of each solution
    CAObj = Archive.objs;
    CAObj = (CAObj-repmat(min(CAObj),N,1))./(repmat(max(CAObj)-min(CAObj),N,1));
    I = zeros(N);
    for i = 1 : N
        for j = 1 : N
            I(i,j) = max(CAObj(i,:)-CAObj(j,:));
        end
    end
    C = max(abs(I));
    F = sum(-exp(-I./repmat(C,N,1)/0.05)) + 1;
    
    %% Delete part of the solutions by their fitnesses
    Choose = 1 : N;
    while length(Choose) > MaxSize
        [~,x] = min(F(Choose));
        F = F + exp(-I(Choose(x),:)/C(Choose(x))/0.05);
        Choose(x) = [];
    end
    Archive = Archive(Choose);

%% delete those which are too far from the archive
    
    % method2
%     o = Archive.objs;
%     o = o-repmat(min(o),size(o,1),1);
% [Q,IQR] = Quartile_self(o);
% upper = Q(3,:)+3*IQR;
% lower = Q(1,:)-3*IQR;
% delete = find(sum(o<=upper,2)~=size(o,2));
% Archive(delete)=[];

%     method1
    o = Archive.objs;
    o = o-repmat(min(o),size(o,1),1);
    d = sqrt(sum(o.^2,2));
    meanD = sum(d,1)/size(o,1);
    delete = find(d>10*meanD);
    Archive(delete)=[];


%%     %% Find the non-dominated solutions
%     Archive = [Archive,Population];
%         o = Archive.objs;
%     [c,ia,ic] = unique(o,'rows');
%     Archive = Archive(ia);
%     ND = NDSort(Archive.objs,1);
%     Archive = Archive(ND==1);
%     N  = length(Archive);
%     if N <= MaxSize
%         return;
%     end
%     p = 1./size(Archive.objs,2);
%     %% Select the extreme solutions first
%     Choose = false(1,N);
%     [~,Extreme1] = min(Archive.objs,[],1);
%     [~,Extreme2] = max(Archive.objs,[],1);
%     Choose(Extreme1) = true;
%     Choose(Extreme2) = true;
%     
%     %% Delete or add solutions to make a total of K solutions be chosen by truncation
%     if sum(Choose) > MaxSize
%         % Randomly delete several solutions
%         Choosed = find(Choose);
%         k = randperm(sum(Choose),sum(Choose)-MaxSize);
%         Choose(Choosed(k)) = false;
%     elseif sum(Choose) < MaxSize
%         % Add several solutions by truncation strategy
%         Distance = inf(N);
%         for i = 1 : N-1
%             for j = i+1 : N
%                 Distance(i,j) = norm(Archive(i).obj-Archive(j).obj,p);
%                 Distance(j,i) = Distance(i,j);
%             end
%         end
%         while sum(Choose) < MaxSize
%             Remain = find(~Choose);
%             [~,x]  = max(min(Distance(~Choose,Choose),[],2));
%             Choose(Remain(x)) = true;
%         end
%     end
%     Archive = Archive(Choose);
end

