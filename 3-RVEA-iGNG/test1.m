load("c1.mat");
c1 = [];
for i = 1:size(Population,2)
    c1 = [c1;Population(1,i).objs];
end
x=c1(:,1);y=c1(:,2);z=c1(:,3);
scatter3(x,y,z)%散点图
figure
[X,Y,Z]=griddata(x,y,z,linspace(min(x),max(x))',linspace(min(y),max(y)),'v4');%插值
pcolor(X,Y,Z);shading interp%伪彩色图
figure,contourf(X,Y,Z) %等高线图
figure,surf(X,Y,Z);%三维曲面
figure,meshc(X,Y,Z)%剖面图
view(0,0); 
figure,meshc(X,Y,Z);%s三维曲面（浅色）+等高线
hidden off;