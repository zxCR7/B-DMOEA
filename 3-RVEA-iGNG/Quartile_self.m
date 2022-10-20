function [Q,IQR] = Quartile_self(X)
%Author: David Ferreira - Federal University of Amazonas
%Contact: ferreirad08@gmail.com
%
%quartile
%
%Syntax
%1. Q = quartile(X)
%2. [Q,IQR] = quartile(X)
%
%Description 
%1. Calculates the quartile (Q1, Q2 and Q3) of the data of a vector or matrix.
%2. Calculates the quartile (Q1, Q2 and Q3) and the interquartile range (IQR) of the data of a vector or matrix.
%
%Example
%1.
%     v = [1 2 3 4 7 10];
%     [Q,IQR] = quartile(v)
%     Q = 
%         2.2500    3.5000    6.2500
%     IQR =
%         4
%
%2.
%     A = [1 2; 2 5; 3 6; 4 10; 7 11; 10 13];
%     [Q,IQR] = quartile(A)
%     Q = 
%         2.2500    5.2500
%         3.5000    8.0000
%         6.2500   10.7500
%     IQR =
%         4.0000    5.5000

X = sort(X);
if isrow(X), X = X'; end
[n,m] = size(X);
if isscalar(X), X(2,1) = 0; end

LMU = [(n+3)/4; (n+1)/2; (3*n+1)/4];
i = floor(LMU);
d = LMU-i;
Q = X(i,:) + (X(i+1,:)-X(i,:)).*repmat(d,1,m);
IQR = Q(3,:)-Q(1,:);

if isvector(X), Q = Q'; end
end