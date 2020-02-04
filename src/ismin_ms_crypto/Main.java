package ismin_ms_crypto;

public class Main {

    public static void main(String[] args) {

        int prime_number = 2147483647;
        BigNumber modulo = new BigNumber(prime_number, 0);
        modulo.showValue();

        // ---- testing modular_add carry ----
        BigNumber A = new BigNumber(prime_number, 7);
        A.showValue();
        BigNumber B = new BigNumber(1, 7);
        B.showValue();


        // ---- showing A, B and N values ----
//        long A_bis = A.arrayToString();
//        System.out.println("A = " + A_bis);
//        long B_bis = B.arrayToString();
//        System.out.println("B = " + B_bis);
//        long modulo_bis = modulo.arrayToString();
//        System.out.println("N = " + modulo_bis);


        // ---- testing sub_by_word function (results only reliable when A > B -----
//        A.sub_by_word(B);
//        System.out.println("(diy) A - B = " + A.arrayToString());
//        System.out.println("(java) A - B = " + (A_bis - B_bis));


        A.modular_add(B, modulo);
        A.showValue();


        System.out.println("(diy) A - B = " + A.arrayToString());
        // System.out.println("(java) A - B = " + (A_bis - B_bis) % modulo_bis);










    }
}
