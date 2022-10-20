function test
    % 读取当前时刻的解
    while true
        if exist('F:\RVEAiGNGOutput\2\parent.xls','file')
            c = xlsread("F:\RVEAiGNGOutput\2\parent.xls");
            Objs = c;
            break;
        end
    end
    
%     i = 1;
%     path = ['F:\科研\论文输出\' num2str(i) 'output.xlsx'];
%     xlswrite(path,c);
    
    RVEAGNG_test(GLOBAL,Objs);
end