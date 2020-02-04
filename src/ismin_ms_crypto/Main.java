package ismin_ms_crypto;

public class Main {

    public static void main(String[] args) {

        int prime_number = 2147483642;
        BigNumber modulo = new BigNumber(prime_number);

        BigNumber A = new BigNumber(prime_number-10, prime_number-1, 1);
        A.showValue();
        BigNumber B = new BigNumber(prime_number-10, prime_number-1, 1);
        B.showValue();

        long A_bis = A.arrayToString();
        System.out.println("A = " + A_bis);
        long B_bis = B.arrayToString();
        System.out.println("B = " + B_bis);
        long modulo_bis = modulo.arrayToString();
        System.out.println("N = " + modulo_bis);

        // testing sub_by_word function (results only reliable when A > B
//        A.sub_by_word(B);
//        System.out.println("(diy) A - B = " + A.arrayToString());
//        System.out.println("(java) A - B = " + (A_bis - B_bis));

        // testing add function
        A.modular_add(B, modulo);
        System.out.println("(diy) A + B = " + A.arrayToString());
        System.out.println("(java) A + B = " + (A_bis + B_bis) % modulo_bis);



    }
}
