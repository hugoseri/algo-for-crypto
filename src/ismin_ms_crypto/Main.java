package ismin_ms_crypto;

import java.math.BigInteger;

public class Main {

    public static void main(String[] args) {

        int prime_number = 39921;
        int r_montgomery = 32;
        int r_1_montgomery = 8;
        int v_montgomery = 15;

        BigNumber A = new BigNumber(32, prime_number, 7);

        BigInteger A_div_r = A.divide_by_r_java(new BigNumber(64, 5));
        String A_div_r_string = A_div_r.toString(2);

        System.out.println("java : " + A_div_r);
        A.divide_by_r(68);
        System.out.println("diy : " + A.toBigInteger());

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
