import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Handle {
    public static void main(String[] args){

        normalPF();
        getReferPF();
        try {

            //从文本文件中读取参考解集
            List<List<List<Double>>> refPF = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                List<List<Double>> t = new ArrayList<>();
                //List<List<Double>> tCertain = new ArrayList<>();

                String path = "F:\\last\\pfResult\\4,5\\AnotherRefPF\\t"+ i +".txt";

                FileReader fr = new FileReader(path);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                // 按行读取字符串
                while ((str = bf.readLine()) != null) {
                    String[] split1 = str.split("\t");
                    List<Double> temp = new ArrayList<>();
                    temp.add(Double.parseDouble(split1[0]));
                    temp.add(Double.parseDouble(split1[1]));
                    temp.add(Double.parseDouble(split1[2]));
                    t.add(temp);
                }
                bf.close();
                fr.close();

                t = Problem.non_dominate_point(Problem.delete_same_point(t));
                List<List<Double>> normal_t = Problem.normalizationPoint(t);
                refPF.add(normal_t);

            }
            calIGD(refPF);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    //算每个环境下的参考帕累托面
    public static void getReferPF(){
        //环境0
        ArrayList<List<Double>> arrayListT0 = new ArrayList<>();
        //环境1
        ArrayList<List<Double>> arrayListT1 = new ArrayList<>();
        //环境2
        ArrayList<List<Double>> arrayListT2 = new ArrayList<>();
        //环境3
        ArrayList<List<Double>> arrayListT3 = new ArrayList<>();
        //环境4
        ArrayList<List<Double>> arrayListT4 = new ArrayList<>();
        //环境5
        ArrayList<List<Double>> arrayListT5 = new ArrayList<>();
        //环境6
        ArrayList<List<Double>> arrayListT6 = new ArrayList<>();
        //环境7
        ArrayList<List<Double>> arrayListT7 = new ArrayList<>();

        try {
            //读取环境0下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF0.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT0.add(temp);
//                                arrayListT0.add(Double.parseDouble(split1[0]));
//                                arrayListT0.add(Double.parseDouble(split1[1]));
//                                arrayListT0.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF0.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT0.add(temp);
//                                arrayListT0.add(Double.parseDouble(split1[0]));
//                                arrayListT0.add(Double.parseDouble(split1[1]));
//                                arrayListT0.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF0.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT0.add(temp);
//                                arrayListT0.add(Double.parseDouble(split1[0]));
//                                arrayListT0.add(Double.parseDouble(split1[1]));
//                                arrayListT0.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF0.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT0.add(temp);
//                                arrayListT0.add(Double.parseDouble(split1[0]));
//                                arrayListT0.add(Double.parseDouble(split1[1]));
//                                arrayListT0.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF0.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT0.add(temp);
//                                arrayListT0.add(Double.parseDouble(split1[0]));
//                                arrayListT0.add(Double.parseDouble(split1[1]));
//                                arrayListT0.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF0.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT0.add(temp);
//                                arrayListT0.add(Double.parseDouble(split1[0]));
//                                arrayListT0.add(Double.parseDouble(split1[1]));
//                                arrayListT0.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境1下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF1.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT1.add(temp);
//                                arrayListT1.add(Double.parseDouble(split1[0]));
//                                arrayListT1.add(Double.parseDouble(split1[1]));
//                                arrayListT1.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF1.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT1.add(temp);
//                                arrayListT1.add(Double.parseDouble(split1[0]));
//                                arrayListT1.add(Double.parseDouble(split1[1]));
//                                arrayListT1.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF1.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT1.add(temp);
//                                arrayListT1.add(Double.parseDouble(split1[0]));
//                                arrayListT1.add(Double.parseDouble(split1[1]));
//                                arrayListT1.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF1.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT1.add(temp);
//                                arrayListT1.add(Double.parseDouble(split1[0]));
//                                arrayListT1.add(Double.parseDouble(split1[1]));
//                                arrayListT1.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF1.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT1.add(temp);
//                                arrayListT1.add(Double.parseDouble(split1[0]));
//                                arrayListT1.add(Double.parseDouble(split1[1]));
//                                arrayListT1.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF1.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT1.add(temp);
//                                arrayListT1.add(Double.parseDouble(split1[0]));
//                                arrayListT1.add(Double.parseDouble(split1[1]));
//                                arrayListT1.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境2下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF2.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT2.add(temp);
//                                arrayListT2.add(Double.parseDouble(split1[0]));
//                                arrayListT2.add(Double.parseDouble(split1[1]));
//                                arrayListT2.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF2.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT2.add(temp);
//                                arrayListT2.add(Double.parseDouble(split1[0]));
//                                arrayListT2.add(Double.parseDouble(split1[1]));
//                                arrayListT2.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF2.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT2.add(temp);
//                                arrayListT2.add(Double.parseDouble(split1[0]));
//                                arrayListT2.add(Double.parseDouble(split1[1]));
//                                arrayListT2.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF2.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT2.add(temp);
//                                arrayListT2.add(Double.parseDouble(split1[0]));
//                                arrayListT2.add(Double.parseDouble(split1[1]));
//                                arrayListT2.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF2.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT2.add(temp);
//                                arrayListT2.add(Double.parseDouble(split1[0]));
//                                arrayListT2.add(Double.parseDouble(split1[1]));
//                                arrayListT2.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF2.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT2.add(temp);
//                                arrayListT2.add(Double.parseDouble(split1[0]));
//                                arrayListT2.add(Double.parseDouble(split1[1]));
//                                arrayListT2.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境3下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF3.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT3.add(temp);
//                                arrayListT3.add(Double.parseDouble(split1[0]));
//                                arrayListT3.add(Double.parseDouble(split1[1]));
//                                arrayListT3.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF3.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT3.add(temp);
//                                arrayListT3.add(Double.parseDouble(split1[0]));
//                                arrayListT3.add(Double.parseDouble(split1[1]));
//                                arrayListT3.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF3.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT3.add(temp);
//                                arrayListT3.add(Double.parseDouble(split1[0]));
//                                arrayListT3.add(Double.parseDouble(split1[1]));
//                                arrayListT3.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF3.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT3.add(temp);
//                                arrayListT3.add(Double.parseDouble(split1[0]));
//                                arrayListT3.add(Double.parseDouble(split1[1]));
//                                arrayListT3.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF3.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT3.add(temp);
//                                arrayListT3.add(Double.parseDouble(split1[0]));
//                                arrayListT3.add(Double.parseDouble(split1[1]));
//                                arrayListT3.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF3.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT3.add(temp);
//                                arrayListT3.add(Double.parseDouble(split1[0]));
//                                arrayListT3.add(Double.parseDouble(split1[1]));
//                                arrayListT3.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境4下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF4.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT4.add(temp);
//                                arrayListT4.add(Double.parseDouble(split1[0]));
//                                arrayListT4.add(Double.parseDouble(split1[1]));
//                                arrayListT4.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF4.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT4.add(temp);
//                                arrayListT4.add(Double.parseDouble(split1[0]));
//                                arrayListT4.add(Double.parseDouble(split1[1]));
//                                arrayListT4.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF4.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT4.add(temp);
//                                arrayListT4.add(Double.parseDouble(split1[0]));
//                                arrayListT4.add(Double.parseDouble(split1[1]));
//                                arrayListT4.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF4.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT4.add(temp);
//                                arrayListT4.add(Double.parseDouble(split1[0]));
//                                arrayListT4.add(Double.parseDouble(split1[1]));
//                                arrayListT4.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF4.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT4.add(temp);
//                                arrayListT4.add(Double.parseDouble(split1[0]));
//                                arrayListT4.add(Double.parseDouble(split1[1]));
//                                arrayListT4.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF4.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT4.add(temp);
//                                arrayListT4.add(Double.parseDouble(split1[0]));
//                                arrayListT4.add(Double.parseDouble(split1[1]));
//                                arrayListT4.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境5下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF5.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT5.add(temp);
//                                arrayListT5.add(Double.parseDouble(split1[0]));
//                                arrayListT5.add(Double.parseDouble(split1[1]));
//                                arrayListT5.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF5.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT5.add(temp);
//                                arrayListT5.add(Double.parseDouble(split1[0]));
//                                arrayListT5.add(Double.parseDouble(split1[1]));
//                                arrayListT5.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF5.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT5.add(temp);
//                                arrayListT5.add(Double.parseDouble(split1[0]));
//                                arrayListT5.add(Double.parseDouble(split1[1]));
//                                arrayListT5.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF5.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT5.add(temp);
//                                arrayListT5.add(Double.parseDouble(split1[0]));
//                                arrayListT5.add(Double.parseDouble(split1[1]));
//                                arrayListT5.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF5.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT5.add(temp);
//                                arrayListT5.add(Double.parseDouble(split1[0]));
//                                arrayListT5.add(Double.parseDouble(split1[1]));
//                                arrayListT5.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF5.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT5.add(temp);
//                                arrayListT5.add(Double.parseDouble(split1[0]));
//                                arrayListT5.add(Double.parseDouble(split1[1]));
//                                arrayListT5.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境6下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF6.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT6.add(temp);
//                                arrayListT6.add(Double.parseDouble(split1[0]));
//                                arrayListT6.add(Double.parseDouble(split1[1]));
//                                arrayListT6.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF6.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT6.add(temp);
//                                arrayListT6.add(Double.parseDouble(split1[0]));
//                                arrayListT6.add(Double.parseDouble(split1[1]));
//                                arrayListT6.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF6.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT6.add(temp);
//                                arrayListT6.add(Double.parseDouble(split1[0]));
//                                arrayListT6.add(Double.parseDouble(split1[1]));
//                                arrayListT6.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF6.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT6.add(temp);
//                                arrayListT6.add(Double.parseDouble(split1[0]));
//                                arrayListT6.add(Double.parseDouble(split1[1]));
//                                arrayListT6.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF6.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT6.add(temp);
//                                arrayListT6.add(Double.parseDouble(split1[0]));
//                                arrayListT6.add(Double.parseDouble(split1[1]));
//                                arrayListT6.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF6.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT6.add(temp);
//                                arrayListT6.add(Double.parseDouble(split1[0]));
//                                arrayListT6.add(Double.parseDouble(split1[1]));
//                                arrayListT6.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //读取环境7下的所有解
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF7.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT7.add(temp);
//                                arrayListT7.add(Double.parseDouble(split1[0]));
//                                arrayListT7.add(Double.parseDouble(split1[1]));
//                                arrayListT7.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 1:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF7.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
//                                arrayListT7.add(temp);
//                                arrayListT7.add(Double.parseDouble(split1[0]));
//                                arrayListT7.add(Double.parseDouble(split1[1]));
//                                arrayListT7.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 2:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF7.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT7.add(temp);
//                                arrayListT7.add(Double.parseDouble(split1[0]));
//                                arrayListT7.add(Double.parseDouble(split1[1]));
//                                arrayListT7.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 3:
                        for (int j = 0; j < 10; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF7.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT7.add(temp);
//                                arrayListT7.add(Double.parseDouble(split1[0]));
//                                arrayListT7.add(Double.parseDouble(split1[1]));
//                                arrayListT7.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 4:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (j+1) +"\\normalPf\\OriPF7.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT7.add(temp);
//                                arrayListT7.add(Double.parseDouble(split1[0]));
//                                arrayListT7.add(Double.parseDouble(split1[1]));
//                                arrayListT7.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                    case 5:
                        for (int j = 0; j < 12; j++) {
                            String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (j+1) +"\\normalPf\\OriPF7.txt";
                            FileReader fr = new FileReader(path);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;
                            // 按行读取字符串
                            while ((str = bf.readLine()) != null) {
                                String[] split1 = str.split("\t");
                                List<Double> temp = new ArrayList<>();
                                temp.add(Double.parseDouble(split1[0]));
                                temp.add(Double.parseDouble(split1[1]));
                                temp.add(Double.parseDouble(split1[2]));
                                arrayListT7.add(temp);
//                                arrayListT7.add(Double.parseDouble(split1[0]));
//                                arrayListT7.add(Double.parseDouble(split1[1]));
//                                arrayListT7.add(Double.parseDouble(split1[2]));
                            }
                            bf.close();
                            fr.close();
                        }
                        //System.out.println(i + "-----");
                        break;
                }

            }

            //求得每个环境下的非支配解
            List<List<List<Double>>> allFeiZhiPei = new ArrayList<>();
            //对每个环境下的非支配解做规范化
            List<List<Double>> feiZhiPei0 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT0)));
            List<List<Double>> feiZhiPei1 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT1)));
            List<List<Double>> feiZhiPei2 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT2)));
            List<List<Double>> feiZhiPei3 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT3)));
            List<List<Double>> feiZhiPei4 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT4)));
            List<List<Double>> feiZhiPei5 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT5)));
            List<List<Double>> feiZhiPei6 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT6)));
            List<List<Double>> feiZhiPei7 = Problem.normalizationPoint(Problem.non_dominate_point(Problem.delete_same_point(arrayListT7)));

            allFeiZhiPei.add(feiZhiPei0);
            allFeiZhiPei.add(feiZhiPei1);
            allFeiZhiPei.add(feiZhiPei2);
            allFeiZhiPei.add(feiZhiPei3);
            allFeiZhiPei.add(feiZhiPei4);
            allFeiZhiPei.add(feiZhiPei5);
            allFeiZhiPei.add(feiZhiPei6);
            allFeiZhiPei.add(feiZhiPei7);

            String deletePath = "F:\\last\\pfResult\\4,5\\AnotherRefPF";
            Problem.deleteDir(deletePath);

            for (int i = 0; i < 8; i++) {
                String path = "F:\\last\\pfResult\\4,5\\AnotherRefPF\\t"+ i +".txt";
                File file = new File(path);
                if(!file.exists()){
                    file.createNewFile();
                }

                for (int j = 0; j < allFeiZhiPei.get(i).size(); j++) {

                    FileWriter fileWrite = new FileWriter(file,true);

                    String line = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter data = new BufferedWriter(fileWrite);
//                    for (int k = 0; k < allFeiZhiPei.get(i).get(j).size(); k++){
                        data.write("" + allFeiZhiPei.get(i).get(j).get(0));
                        data.write("\t");
                        data.write("" + allFeiZhiPei.get(i).get(j).get(1));
                        data.write("\t");
                        data.write("" + allFeiZhiPei.get(i).get(j).get(2));
                        data.write(line);
                    //}
                    data.close();
                    fileWrite.close();
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    //规范化pf
    public static void normalPF(){

        try{

            for (int z = 0; z < 12; z++){

                String deleteNormalPf = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (z+1) +"\\normalPf";
                File normalFile = new File(deleteNormalPf);
                if (!normalFile.exists()){
                    normalFile.mkdir();
                }
                Problem.deleteDir(deleteNormalPf);

                for (int i = 0; i < 8; i++) {
                    List<List<Double>> TNpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (z+1) +"\\pf\\OriPF"+ i +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        TNpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    //对pf做归一化
                    List<List<Double>> normalTNpf = Problem.normalizationPoint(TNpf);

                    //将归一化的pf保存起来
                    String pathNormal = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (z+1) +"\\normalPf\\OriPF"+ i +".txt";

                    File fileNormal = new File(pathNormal);
                    if(!fileNormal.exists()){
                        fileNormal.createNewFile();
                    }
                    FileWriter fileWriteNormal = new FileWriter(fileNormal,true);
                    String lineNormal = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter dataNormal = new BufferedWriter(fileWriteNormal);
                    for (int j = 0; j < normalTNpf.size(); j++){
                        dataNormal.write("" + normalTNpf.get(j).get(0));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalTNpf.get(j).get(1));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalTNpf.get(j).get(2));
                        dataNormal.write(lineNormal);
                    }
                    dataNormal.close();
                    fileWriteNormal.close();
                }
            }

            for (int z = 0; z < 12; z++){

                String deleteNormalPf = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (z+1) +"\\normalPf";
                File normalFile = new File(deleteNormalPf);
                if (!normalFile.exists()){
                    normalFile.mkdir();
                }
                Problem.deleteDir(deleteNormalPf);

                for (int i = 0; i < 8; i++) {
                    List<List<Double>> BNpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (z+1) +"\\pf\\OriPF"+ i +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        BNpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    //对pf做归一化
                    List<List<Double>> normalBNpf = Problem.normalizationPoint(BNpf);

                    //将归一化的pf保存起来
                    String pathNormal = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (z+1) +"\\normalPf\\OriPF"+ i +".txt";

                    File fileNormal = new File(pathNormal);
                    if(!fileNormal.exists()){
                        fileNormal.createNewFile();
                    }
                    FileWriter fileWriteNormal = new FileWriter(fileNormal,true);
                    String lineNormal = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter dataNormal = new BufferedWriter(fileWriteNormal);
                    for (int j = 0; j < normalBNpf.size(); j++){
                        dataNormal.write("" + normalBNpf.get(j).get(0));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalBNpf.get(j).get(1));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalBNpf.get(j).get(2));
                        dataNormal.write(lineNormal);
                    }
                    dataNormal.close();
                    fileWriteNormal.close();
                }
            }

            for (int z = 0; z < 10; z++){

                String deleteNormalPf = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (z+1) +"\\normalPf";
                File normalFile = new File(deleteNormalPf);
                if (!normalFile.exists()){
                    normalFile.mkdir();
                }
                Problem.deleteDir(deleteNormalPf);

                for (int i = 0; i < 8; i++) {
                    List<List<Double>> TIpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (z+1) +"\\pf\\OriPF"+ i +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        TIpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    //对pf做归一化
                    List<List<Double>> normalTIpf = Problem.normalizationPoint(TIpf);

                    //将归一化的pf保存起来
                    String pathNormal = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (z+1) +"\\normalPf\\OriPF"+ i +".txt";

                    File fileNormal = new File(pathNormal);
                    if(!fileNormal.exists()){
                        fileNormal.createNewFile();
                    }
                    FileWriter fileWriteNormal = new FileWriter(fileNormal,true);
                    String lineNormal = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter dataNormal = new BufferedWriter(fileWriteNormal);
                    for (int j = 0; j < normalTIpf.size(); j++){
                        dataNormal.write("" + normalTIpf.get(j).get(0));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalTIpf.get(j).get(1));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalTIpf.get(j).get(2));
                        dataNormal.write(lineNormal);
                    }
                    dataNormal.close();
                    fileWriteNormal.close();

                }
            }

            for (int z = 0; z < 10; z++){

                String deleteNormalPf = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (z+1) +"\\normalPf";
                File normalFile = new File(deleteNormalPf);
                if (!normalFile.exists()){
                    normalFile.mkdir();
                }
                Problem.deleteDir(deleteNormalPf);

                for (int i = 0; i < 8; i++) {
                    List<List<Double>> BIpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (z+1) +"\\pf\\OriPF"+ i +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        BIpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    //对pf做归一化
                    List<List<Double>> normalBIpf = Problem.normalizationPoint(BIpf);

                    //将归一化的pf保存起来
                    String pathNormal = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (z+1) +"\\normalPf\\OriPF"+ i +".txt";

                    File fileNormal = new File(pathNormal);
                    if(!fileNormal.exists()){
                        fileNormal.createNewFile();
                    }
                    FileWriter fileWriteNormal = new FileWriter(fileNormal,true);
                    String lineNormal = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter dataNormal = new BufferedWriter(fileWriteNormal);
                    for (int j = 0; j < normalBIpf.size(); j++){
                        dataNormal.write("" + normalBIpf.get(j).get(0));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalBIpf.get(j).get(1));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalBIpf.get(j).get(2));
                        dataNormal.write(lineNormal);
                    }
                    dataNormal.close();
                    fileWriteNormal.close();
                }
            }

            for (int z = 0; z < 12; z++){

                String deleteNormalPf = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (z+1) +"\\normalPf";
                File normalFile = new File(deleteNormalPf);
                if (!normalFile.exists()){
                    normalFile.mkdir();
                }
                Problem.deleteDir(deleteNormalPf);

                for (int i = 0; i < 8; i++) {
                    List<List<Double>> TRpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (z+1) +"\\pf\\OriPF"+ i +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        TRpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    //对pf做归一化
                    List<List<Double>> normalTRpf = Problem.normalizationPoint(TRpf);

                    //将归一化的pf保存起来
                    String pathNormal = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (z+1) +"\\normalPf\\OriPF"+ i +".txt";

                    File fileNormal = new File(pathNormal);
                    if(!fileNormal.exists()){
                        fileNormal.createNewFile();
                    }
                    FileWriter fileWriteNormal = new FileWriter(fileNormal,true);
                    String lineNormal = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter dataNormal = new BufferedWriter(fileWriteNormal);
                    for (int j = 0; j < normalTRpf.size(); j++){
                        dataNormal.write("" + normalTRpf.get(j).get(0));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalTRpf.get(j).get(1));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalTRpf.get(j).get(2));
                        dataNormal.write(lineNormal);
                    }
                    dataNormal.close();
                    fileWriteNormal.close();


                }
            }

            for (int z = 0; z < 12; z++){

                String deleteNormalPf = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (z+1) +"\\normalPf";
                File normalFile = new File(deleteNormalPf);
                if (!normalFile.exists()){
                    normalFile.mkdir();
                }
                Problem.deleteDir(deleteNormalPf);

                for (int i = 0; i < 8; i++) {
                    List<List<Double>> BRpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (z+1) +"\\pf\\OriPF"+ i +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        BRpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    //对pf做归一化
                    List<List<Double>> normalBRpf = Problem.normalizationPoint(BRpf);

                    //将归一化的pf保存起来
                    String pathNormal = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (z+1) +"\\normalPf\\OriPF"+ i +".txt";

                    File fileNormal = new File(pathNormal);
                    if(!fileNormal.exists()){
                        fileNormal.createNewFile();
                    }
                    FileWriter fileWriteNormal = new FileWriter(fileNormal,true);
                    String lineNormal = System.getProperty("line.separator");
                    //如果FileWriter的构造参数为true，那么就进行内容追加;如果FileWriter的构造参数为false,那么就进行内容的覆盖;
                    BufferedWriter dataNormal = new BufferedWriter(fileWriteNormal);
                    for (int j = 0; j < normalBRpf.size(); j++){
                        dataNormal.write("" + normalBRpf.get(j).get(0));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalBRpf.get(j).get(1));
                        dataNormal.write("\t");
                        dataNormal.write("" + normalBRpf.get(j).get(2));
                        dataNormal.write(lineNormal);
                    }
                    dataNormal.close();
                    fileWriteNormal.close();

                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //计算IGD
    public static void calIGD(List<List<List<Double>>> refPF){
        double IGD_NSGAII = 0.0;
        double mean_IGD_NSGAII = 0.0;
        double IGD_IT_NSGAII = 0.0;
        double mean_IGD_IT_NSGAII = 0.0;
        double IGD_MOEAD = 0.0;
        double mean_IGD_MOEAD = 0.0;
        double IGD_IT_MOEAD = 0.0;
        double mean_IGD_IT_MOEAD = 0.0;
        double IGD_PBEA = 0.0;
        double mean_IGD_PBEA = 0.0;
        double IGD_IT_PBEA = 0.0;
        double mean_IGD_IT_PBEA = 0.0;

        //读取规范化后的pf
        try{
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 8; j++) {
                    List<List<Double>> normalTNpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\NSGAII\\TCA\\run"+ (i+1) +"\\normalPf\\OriPF"+ j +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        normalTNpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    IGD_NSGAII = IGD_NSGAII + Problem.IGD_point(normalTNpf,refPF.get(j));
                }

                IGD_NSGAII = IGD_NSGAII/8;
                System.out.println(IGD_NSGAII);

                mean_IGD_NSGAII = mean_IGD_NSGAII + IGD_NSGAII;
            }
            mean_IGD_NSGAII = mean_IGD_NSGAII/12;
            System.out.println("mean_IGD_Tr-NSGAII   " + mean_IGD_NSGAII);

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 8; j++) {
                    List<List<Double>> normalBNpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\NSGAII\\BDA\\run"+ (i+1) +"\\normalPf\\OriPF"+ j +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        normalBNpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    IGD_IT_NSGAII = IGD_IT_NSGAII + Problem.IGD_point(normalBNpf,refPF.get(j));
                }

                IGD_IT_NSGAII = IGD_IT_NSGAII/8;
                System.out.println(IGD_IT_NSGAII);

                mean_IGD_IT_NSGAII = mean_IGD_IT_NSGAII + IGD_IT_NSGAII;
            }
            mean_IGD_IT_NSGAII = mean_IGD_IT_NSGAII/12;
            System.out.println("mean_IGD_B-NSGAII   " + mean_IGD_IT_NSGAII);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 8; j++) {
                    List<List<Double>> normalTIpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\RVEA\\TCA\\run"+ (i+1) +"\\normalPf\\OriPF"+ j +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        normalTIpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    IGD_MOEAD = IGD_MOEAD + Problem.IGD_point(normalTIpf,refPF.get(j));
                }

                IGD_MOEAD = IGD_MOEAD/8;
                System.out.println(IGD_MOEAD);

                mean_IGD_MOEAD = mean_IGD_MOEAD + IGD_MOEAD;
            }
            mean_IGD_MOEAD = mean_IGD_MOEAD/10;
            System.out.println("mean_IGD_Tr-RVEA   " + mean_IGD_MOEAD);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 8; j++) {
                    List<List<Double>> normalBIpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\RVEA\\BDA\\run"+ (i+1) +"\\normalPf\\OriPF"+ j +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        normalBIpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    IGD_IT_MOEAD = IGD_IT_MOEAD + Problem.IGD_point(normalBIpf,refPF.get(j));
                }

                IGD_IT_MOEAD = IGD_IT_MOEAD/8;
                System.out.println(IGD_IT_MOEAD);

                mean_IGD_IT_MOEAD = mean_IGD_IT_MOEAD + IGD_IT_MOEAD;
            }
            mean_IGD_IT_MOEAD = mean_IGD_IT_MOEAD/10;
            System.out.println("mean_IGD_B-RVEA   " + mean_IGD_IT_MOEAD);

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 8; j++) {
                    List<List<Double>> normalTRpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\IBEA\\TCA\\run"+ (i+1) +"\\normalPf\\OriPF"+ j +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        normalTRpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    IGD_PBEA = IGD_PBEA + Problem.IGD_point(normalTRpf,refPF.get(j));
                }

                IGD_PBEA = IGD_PBEA/8;
                System.out.println(IGD_PBEA);

                mean_IGD_PBEA = mean_IGD_PBEA + IGD_PBEA;
            }
            mean_IGD_PBEA = mean_IGD_PBEA/12;
            System.out.println("mean_IGD_Tr-IBEA   " + mean_IGD_PBEA);
//
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 8; j++) {
                    List<List<Double>> normalBRpf = new ArrayList<>();
                    String path = "F:\\last\\pfResult\\4,5\\IBEA\\BDA\\run"+ (i+1) +"\\normalPf\\OriPF"+ j +".txt";
                    FileReader fr = new FileReader(path);
                    BufferedReader bf = new BufferedReader(fr);
                    String str;
                    // 按行读取字符串
                    while ((str = bf.readLine()) != null) {
                        String[] split1 = str.split("\t");
                        List<Double> temp = new ArrayList<>();
                        temp.add(Double.parseDouble(split1[0]));
                        temp.add(Double.parseDouble(split1[1]));
                        temp.add(Double.parseDouble(split1[2]));
                        normalBRpf.add(temp);
                    }
                    bf.close();
                    fr.close();

                    IGD_IT_PBEA = IGD_IT_PBEA + Problem.IGD_point(normalBRpf,refPF.get(j));
                }

                IGD_IT_PBEA = IGD_IT_PBEA/8;
                System.out.println(IGD_IT_PBEA);

                mean_IGD_IT_PBEA = mean_IGD_IT_PBEA + IGD_IT_PBEA;
            }
            mean_IGD_IT_PBEA = mean_IGD_IT_PBEA/12;
            System.out.println("mean_IGD_B-PBEA   " + mean_IGD_IT_PBEA);

        }catch (IOException e){
            e.printStackTrace();
        }



    }
}
