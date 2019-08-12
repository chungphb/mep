package general;

public class Vector {
    public static double[] add(double[] v1, double[] v2) {
        /* Add two vectors */
        int length = v1.length;
        double[] sum = new double[length];
        for(int i = 0; i < length; i++) 
            sum[i] = v1[i] + v2[i];
        return sum;
    }
    
    public static double[] mul(double[] v, double k) {
        /* Multiply a vector with a real number */
        int length = v.length;
        double[] product = new double[length];
        for(int i = 0; i < length; i++)
            product[i] = v[i]*k;
        return product;
    }
    
    public static boolean isGreaterThan(double[] v, double alpha) {
        /* Compare vector with a real number */
        for(int i = 0; i < v.length; i++)
            if(v[i] < alpha)
                return false;
        return true;
    }
}
