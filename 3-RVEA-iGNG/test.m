function test
    % ��ȡ��ǰʱ�̵Ľ�
    while true
        if exist('F:\RVEAiGNGOutput\2\parent.xls','file')
            c = xlsread("F:\RVEAiGNGOutput\2\parent.xls");
            Objs = c;
            break;
        end
    end
    
%     i = 1;
%     path = ['F:\����\�������\' num2str(i) 'output.xlsx'];
%     xlswrite(path,c);
    
    RVEAGNG_test(GLOBAL,Objs);
end