load("c1.mat");
c1 = [];
for i = 1:size(Population,2)
    c1 = [c1;Population(1,i).objs];
end
x=c1(:,1);y=c1(:,2);z=c1(:,3);
scatter3(x,y,z)%ɢ��ͼ
figure
[X,Y,Z]=griddata(x,y,z,linspace(min(x),max(x))',linspace(min(y),max(y)),'v4');%��ֵ
pcolor(X,Y,Z);shading interp%α��ɫͼ
figure,contourf(X,Y,Z) %�ȸ���ͼ
figure,surf(X,Y,Z);%��ά����
figure,meshc(X,Y,Z)%����ͼ
view(0,0); 
figure,meshc(X,Y,Z);%s��ά���棨ǳɫ��+�ȸ���
hidden off;