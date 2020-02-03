package ismin_ms_crypto;

public class Main {

    public static void main(String[] args) {

        int modulo = 17;

        BigNumber A = new BigNumber(2);
        A.randomValue();
        A.showValue();
        BigNumber B = new BigNumber(2);
        B.randomValue();
        B.showValue();

        long A_bis = A.arrayToString();
        long B_bis = B.arrayToString();
        // testing sub_by_word function (results only reliable when A > B
//        A.sub_by_word(B);
//        System.out.println("(diy) A - B = " + A.arrayToString());
//        System.out.println("(java) A - B = " + (A_bis - B_bis));

        // testing add function
        A.modular_add(B);
        System.out.println("(diy) A + B = " + A.arrayToString());
        System.out.println("(java) A + B = " + (A_bis + B_bis) % modulo);



    }
}
