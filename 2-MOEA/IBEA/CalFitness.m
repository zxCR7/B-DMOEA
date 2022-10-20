function [Fitness,I,C] = CalFitness(Pop,kappa)
% Calculate the fitness of each solution

    for i=1: length(Pop)/3
        PopObj(i,1) = Pop((i-1)*3 + 1);
        PopObj(i,2) = Pop((i-1)*3 + 2);
        PopObj(i,3) = Pop((i-1)*3 + 3);  
    end

    N = size(PopObj,1);
    PopObj = (PopObj-repmat(min(PopObj),N,1))./(repmat(max(PopObj)-min(PopObj),N,1));
    I      = zeros(N);
    for i = 1 : N
        for j = 1 : N
            I(i,j) = max(PopObj(i,:)-PopObj(j,:));
        end
    end
    C = max(abs(I));
    Fitness = sum(-exp(-I./repmat(C,N,1)/kappa)) + 1;
end