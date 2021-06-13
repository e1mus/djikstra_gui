package algo;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

import java.util.List;

public class oneNumber {
    static int Factorial(int x)
    {
        if (x == 0)
        {
            return 1;
        }
        else
        {
            return x * Factorial(x - 1);
        }
    }
    public static String myFull(double[] a, double[] b, int l, int n)
    {

        String myAnswer = "";
        int C = Factorial(n) / (Factorial(n - l) * Factorial(l));

        int[][] lamba = new int[C][];



        for (int i = 0; i < lamba.length; i++)
        {
            lamba[i] = new int[n];

            if (i == 0)
            {
                for (int j = 0; j < n; j++)
                {
                    if (j >= l)
                    {
                        lamba[i][j] = 0;
                    }
                    else
                    {
                        lamba[i][j] = 1;
                    }


                }

                ArrayUtils.reverse(lamba[i]);

            }
            else
            {
                lamba[i] = Lambada(lamba[i - 1]);
            }






        }
        double maxx=0;
        double[] sum = new double[C];
        for (int i = 0; i < lamba.length; i++)
        {



            double suma = 0;
            double sumb = 0;
            for (int j = 0; j < lamba[i].length; j++)
            {
                suma += lamba[i][j] * a[j];
                sumb += lamba[i][j] * b[j];
                myAnswer += (lamba[i][j] + " ");
            }
            sum[i] = suma / sumb;
            if(sum[i]> maxx){
                maxx = sum[i];
            }
            myAnswer+=("I[" + (i + 1) + "] =" + sum[i]);
            myAnswer += "\n";
        }
        List summ = Arrays.asList(ArrayUtils.toObject(sum));


        //   myAnswer += ("I[max]=" ++"\n");
        //  String maxx =

        return  String.format("%.0f",maxx*10);
    }


    static int[] Lambada(int[] sourceLamba)
    {
        int one = 1, zero = 0;
        int[] lamba1 = new int[sourceLamba.length];
        lamba1 = sourceLamba.clone();


        for (int i = 1; i < lamba1.length; ++i)
        {
            if (lamba1[i - 1] == 0 && lamba1[i] == 1 && lamba1[0] != 1)
            {
                Swap(lamba1, i - 1, i);
                return lamba1;
            }
            else
            {
                if (lamba1[0] == 1)
                {

                    if (lamba1[i] == 1 && zero == 0)
                    {
                        one++;
                        continue;
                    }
                    if (lamba1[i] == 0)
                    {
                        zero++;
                        continue;
                    }

                    if (zero > 1 && lamba1[i] == 1)
                    {
                        if (zero - 1 >= one)
                        {
                            int lastIndex = i - 2;
                            for (int j = 0; j < one; j++)
                            {
                                Swap(lamba1, j, lastIndex--);
                            }
                            Swap(lamba1, i - 1, i);
                            return lamba1;
                        }
                        else
                        {
                            int lastIndex = i - 2;
                            for (int j = 0; j < zero; j++)
                            {
                                Swap(lamba1, j, lastIndex);
                            }
                            Swap(lamba1, i - 1, i);
                            return lamba1;
                        }

                    }
                    if (zero == 1 && lamba1[i] == 1)
                    {
                        Swap(lamba1, i - 1, i);
                        return lamba1;
                    }
                }


            }
        }

        return lamba1;

    }

    static void Swap(int[] list, int indexA, int indexB)
    {
        int tmp = list[indexA];
        list[indexA] = list[indexB];
        list[indexB] = tmp;
    }
}
