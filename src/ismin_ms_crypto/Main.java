package ismin_ms_crypto;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        int prime_number = 17;
        int r_montgomery = 32;
        int k = 5;
        int r2_montgomery = 1024;
        int v_montgomery = 15;

        BigNumber modulo = new BigNumber(prime_number, 7);
        BigNumber r = new BigNumber(r_montgomery, 7);
        BigNumber v = new BigNumber(v_montgomery, 7);

        System.out.println("Hello");

        BigNumber A = new BigNumber(8, 9, 1);
        BigNumber B = new BigNumber(5, 6, 1);

        // Testing modular multiplication
        System.out.println("java : " + A.modular_mult_java(B, modulo));
        BigNumber AxB = A.modular_mult(B, modulo, r, v, k);
        System.out.println("diy : " + AxB.toBigInteger());
//
//        BigNumber M = new BigNumber(2);
//        M.randomValue(6, 7);
//        BigNumber N = new BigNumber(1);
//        N.randomValue(9, 10);
//        System.out.println(M.toBigInteger() + " > " + N.toBigInteger());
//        System.out.println(M.sup(N));
        //------------------------------------------
        // ----- Testing divide by r function ------
        //------------------------------------------
//        BigNumber A = new BigNumber(1, 30000, 7);
//        int k = 1; // just choose any k value, other parameters are calculated based on its value
//        int k_rest_31 = k % 31;
//        int k_x_31 = k / 31;
//        BigNumber r_example = new BigNumber((int) Math.pow(2, k_rest_31), 7-k_x_31);
//
//        BigInteger A_div_r = A.divide_by_r_java(r_example);
//
//        System.out.println("java : " + A_div_r);
//        A.divide_by_r(k);
//        System.out.println("diy : " + A.toBigInteger());
        //------------------------------------------
        // ----- END Testing divide by r function -----
        //------------------------------------------


        // Testing modulo r

//        System.out.println(A.modulo_n_java(modulo));
//        A.compute_modulo_r(k);
//        System.out.println(A.toBigInteger());

//        BigNumber modulo = new BigNumber(prime_number, 3);
//        modulo.showValue();
//
//        // ---- testing modular_add carry ----
//        BigNumber A = new BigNumber(0, prime_number, 4);
//        A.showValue();
//        BigNumber B = new BigNumber(0, prime_number, 4);
//        B.showValue();
//
//        // ---- showing A, B and N values ----
////        long A_bis = A.arrayToString();
////        System.out.println("A = " + A_bis);
////        long B_bis = B.arrayToString();
////        System.out.println("B = " + B_bis);
////        long modulo_bis = modulo.arrayToString();
////        System.out.println("N = " + modulo_bis);
//
//
//        // ---- testing sub_by_word function (results only reliable when A > B -----
////        A.sub_by_word(B);
////        System.out.println("(diy) A - B = " + A.arrayToString());
////        System.out.println("(java) A - B = " + (A_bis - B_bis));
//
//        BigInteger A_B_java = A.mult_by_word_java(B);
//        System.out.println(A_B_java);
//
//        BigNumber A_B = A.mult_by_word(B);
//        System.out.println(A_B.toBigInteger());
//
//
////        BigNumber result_mult = A.mult_by_word(B, modulo);
////        result_mult.showValue();
////
////        System.out.println("(diy) A - B = " + A.arrayToString());
//        // System.out.println("(java) A - B = " + (A_bis - B_bis) % modulo_bis);

    }
}
