classdef DLBP < handle
    properties(SetAccess = private)
        obj;        % Objective values of the individual
        gen;        % ����Ĵ���
        ind;        %�����index
    end
    methods
        %% Constructor
        % objs���и����Ŀ�꺯��ֵ�����
        function obj = DLBP(Objs)
            obj(1).obj = Objs(1,[1 2 3]);
            obj(1).gen = Objs(1,4);
            obj(1).ind = Objs(1,5);
        end
        %% Get the matrix of objective values of the population
        function value = objs(obj)
        %objs - Get the matrix of objective values of the population.
        %
        %   A = obj.objs returns the matrix of objective values of the
        %   population obj, where obj is an array of INDIVIDUAL objects.
            value = cat(1,obj.obj);
        end
        %% ��ȡ��������
        function value = inds(obj)
            value = cat(1,obj.ind);
        end
        %% ��ȡ����Ĵ���
        function value = gens(obj)
            value = cat(1,obj.gen);
        end
    end
end