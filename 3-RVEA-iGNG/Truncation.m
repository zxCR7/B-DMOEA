function Population = Truncation(Population,MaxSize)
% Limit the size of final popualtion in RVEA*

%------------------------------- Copyright --------------------------------
% Copyright (c) 2018-2019 BIMK Group. You are free to use the PlatEMO for
% research purposes. All publications which use this platform or any code
% in the platform should acknowledge the use of "PlatEMO" and reference "Ye
% Tian, Ran Cheng, Xingyi Zhang, and Yaochu Jin, PlatEMO: A MATLAB platform
% for evolutionary multi-objective optimization [educational forum], IEEE
% Computational Intelligence Magazine, 2017, 12(4): 73-87".
%--------------------------------------------------------------------------

%     Choose = true(1,length(Population));
%     Cosine = 1 - pdist2(Population.objs,Population.objs,'cosine');
%     Cosine(logical(eye(length(Cosine)))) = 0;
%     while sum(Choose) > MaxSize
%         Remain   = find(Choose);
%         Temp     = sort(-Cosine(Remain,Remain),2);
%         [~,Rank] = sortrows(Temp);
%         Choose(Remain(Rank(1))) = false;
%     end
%     Population = Population(Choose);
    
    %     %% Find the non-dominated solutions
%     Archive = Population;
%     ND = NDSort(Archive.objs,1);
%     Archive = Archive(ND==1);
%     N  = length(Archive);
%     if N <= MaxSize
%         return;
%     end
%     p = size(Archive.objs,2);
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
%     Population = Archive(Choose);


PopObj = Population.objs;
zmax = max(PopObj,[],1);
zmin = min(PopObj,[],1);

PopObj= (PopObj - repmat(zmin,size(PopObj,1),1))./(zmax-zmin);
M = size(PopObj,2);
H = [eye(M-1)-ones(M-1)/M;-ones(1,M-1)/M];
Pe = H*inv(H'*H)*H';
f = PopObj*Pe';
minf = min(f,[],1);
maxf = max(f,[],1);
f = (f-minf)./(maxf-minf);
temp1 = f./sum(f,2);

dis =  pdist2(temp1,temp1);
dis(logical(eye(length(dis)))) = inf;
Choose = true(1,length(Population));

while sum(Choose) > MaxSize
    Remain   = find(Choose);
    Temp     = sort(dis(Remain,Remain),2);
    [~,Rank] = sortrows(Temp);
    Choose(Remain(Rank(1))) = false;
end
Population = Population(Choose);
    
end