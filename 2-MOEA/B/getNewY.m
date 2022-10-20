function Y=getNewY(Xs, Xt, X, A, kind, p1, p2, p3)
%对新数据X进行映射(X可以为源域或目标域的新数据)，映射到更低维
%Xs：源域数据
%Xt：目标域数据，与Xs行数相同
%X：待变换向量，与Xs行数相同
%W：变换矩阵n1+n2->k，完成降维
%kind：核函数选择:'Gaussian'、'Laplacian'、'Polynomial',其他一律返回-1
%p1,p2,p3：核函数所要附带的参数

    n1 = size(Xs, 2);           %源域中样本个数
	n2 = size(Xt, 2);           %目标域中样本个数
	n3 = size(X, 2);            %样本总数
    
    for j=1:n3
        for i=1:n1 
            K(i,j)=getKernel(Xs(:,i), X(:,j), kind, p1, p2, p3);            %对源域中的样本求核函数的值，Xs(:,i)表示取矩阵Xs的第i列
        end
        for i=1:n2
            K(i+n1,j)=getKernel(Xt(:,i), X(:,j), kind, p1, p2, p3);         %对目标域中样本求核函数
        end
    end
    
    Y=A'*K;                     %完成降维映射后的数据，实际上W矩阵只是为了完成降维