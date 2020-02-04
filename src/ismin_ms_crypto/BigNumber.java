package ismin_ms_crypto;

import java.util.Random;

public class BigNumber {

    public static final int TWO_POW_31 = 0x80000000;

    private int size = 8;
    private int[] value; // [0] : MSB ; [size - 1] : LSB
    public int r; // Montgomery transformation paramater

    /**
     * General constructor for generic BigNumber object.
     * @param random : if true, BigNumber will be initialized with random values
     * @param maxWordValue : if random is true, max limit for random word values
     */
    public BigNumber(boolean random, int maxWordValue){
        value = new int[this.size];
        if (random){
            randomValue(maxWordValue);
        }
    }

    /**
     * Constructor for generic BigNumber object with random numbers inside specific interval.
     * @param minWordValue : if random is true, min limit for random word values
     * @param maxWordValue : if random is true, max limit for random word values
     */
    public BigNumber(int minWordValue, int maxWordValue){
        value = new int[this.size];
        randomValue(minWordValue, maxWordValue);
    }

    /**
     * Constructor for generic BigNumber object with random numbers inside specific interval and only n numbers not null.
     * @param minWordValue : if random is true, min limit for random word values
     * @param maxWordValue : if random is true, max limit for random word values
     * @param n : number of not null words
     */
    public BigNumber(int minWordValue, int maxWordValue, int n){
        value = new int[this.size];
        if (n < this.size) {
            randomValue(minWordValue, maxWordValue, n);
        } else {
            System.out.println("Error: n is too big (must be less than 8).");
        }
    }

    /**
     * Constructor for BigNumber object used for modulo.
     * @param prime_nb : value of the modulo (32 bit)
     */
    public BigNumber(int prime_nb){
        value = new int[this.size];
        value[0] = prime_nb;
    }

    /**
     *
     * @param modulo
     */
    private void compute_modulo_montgomery(BigNumber modulo){

    }

    /**
     * Function initializing BigNumber value by randoms.
     * @param maxValue : max random value
     */
    private void randomValue(int maxValue){
        Random ran = new Random();
        for (int i=0; i<this.size; i++){
            value[i] = Math.abs(ran.nextInt(maxValue));
        }
    }

    /**
     * Function initializing BigNumber value by randoms.
     * @param minValue : min random value
     * @param maxValue : max random value
     */
    private void randomValue(int minValue, int maxValue){
        Random ran = new Random();
        for (int i=0; i<this.size; i++){
            value[i] = Math.abs(ran.nextInt(maxValue - minValue) + minValue);
        }
    }

    /**
     * Function initializing BigNumber value by randoms.
     * @param minValue : min random value
     * @param maxValue : max random value
     * @param n : number of not null numbers
     */
    private void randomValue(int minValue, int maxValue, int n){
        Random ran = new Random();
        for (int i=this.size - 1; i>=this.size - n; i--){
            value[i] = Math.abs(ran.nextInt(maxValue - minValue) + minValue);
        }
    }

    /**
     * Function printing BigNumber value parameter.
     */
    public void showValue(){
        for (int i=0; i<this.size; i++) {
            System.out.print(value[i] + " ");
        }
        System.out.println("\n");
    }

    /**
     * Function returning a long from value parameter array.
     * @return long
     */
    public long arrayToString(){
        long returned = 0;
        for (int i=0; i < this.size; i++){
            returned += this.value[this.size - 1 - i] * Math.pow(2, 32*i);
        }
        return returned;
    }

    /**
     * Function computing modular addition
     * @param B BigNumber to add
     * @param modulo modulo value
     */
    public void modular_add(BigNumber B, BigNumber modulo){
        BigNumber result = new BigNumber(false, 0);
        System.arraycopy(this.value, 0, result.value, 0, this.size);
        long carry = result.add_by_word(B);
        if (result.sup(modulo) || carry > 0){
            result.sub_by_word(modulo);
        }
        this.value = result.value;
    }

    public void modular_sub(BigNumber B){

    }

    /**
     * Function implementing >= operator for BigNumber objects.
     * @param B: BigNumber
     * @return boolean
     */
    private boolean sup(BigNumber B){
        boolean sup = false;
        boolean found = false;
        int k = 0;
        while (!found && k < this.size){
            if (this.value[k] > B.value[k]){
                sup = true;
                found = true;
            } else if (this.value[k] < B.value[k]) {
                sup = false;
                found = true;
            } else {
                k++;
            }
        }
        if (k == this.size){
            sup = true;
        }
        return sup;
    }

    /**
     * Function computing substraction between value parameter and B.value considering value > B.value.
     * @param B
     */
    private void sub_by_word(BigNumber B){
        if (B.size == this.size) {
            int result[] = new int[this.size];
            int k = this.size - 1;
            while (k >= 0) {
                if (this.value[k] > B.value[k]) {
                    result[k] += this.value[k] - B.value[k];
                } else {
                    result[k] += (int) (this.value[k] + TWO_POW_31 - B.value[k]);
                    if (k > 0) {
                        result[k - 1] -= 1;
                    }
                }
                k--;
            }
            this.value = result;
        } else {
            System.out.println("Error: Input has different size than current BigNumber.");
        }
    }

    /**
     * Function computing addition between value parameter and B.value.
     * @param B
     * @return carry between each MSB
     */
    private long add_by_word(BigNumber B){
        if (B.size == this.size) {
            BigNumber result = new BigNumber(false, 0);
            // long mask_retenue = 0xFFFF0000L;
            // int mask_32 = 0xFFFF;
            long carry = 0;
            for (int i=this.size - 1; i>=0; i--) {
                long ai_plus_bi = (long) this.value[i] + B.value[i] + result.value[i];
                result.value[i] = (int) ai_plus_bi & 0xFFFFFFF;
                carry = ai_plus_bi >> 31;
                if (i > 0) {
                    result.value[i - 1] = (int) carry;
                } else if (carry > 0){
                    System.out.println("Error: Inputs are two big to compute result on 256 bits.");
                }
            }
            this.value = result.value;
            return carry;
        } else {
            System.out.println("Error: Input has different size than current BigNumber.");
            return -1;
        }
    }
}
