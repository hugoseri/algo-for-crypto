package ismin_ms_crypto;

import java.math.BigInteger;
import java.util.Random;

public class BigNumber {

    public static final long TWO_POW_31 = 0x80000000L;
    public static final long FILTER_32_BIT = 0x7fffffff;

    private int size = 8;
    private int[] value; // [0] : MSB ; [size - 1] : LSB
    public int r; // Montgomery transformation paramater


    /**
     * General constructor for generic BigNumber object.
     * @param n : elementary words number
     */
    public BigNumber(int n){
        this.size = n;
        value = new int[this.size];
    }

    /**
     * General constructor for 256-bit BigNumber object.
     * @param random : if true, BigNumber will be initialized with random values
     * @param maxWordValue : if random is true, max limit for random word values
     */
    public BigNumber(boolean random, int maxWordValue){
        this.size = 8;
        value = new int[this.size];
        if (random){
            randomValue(maxWordValue);
        }
    }

    /**
     * Constructor for 256-bit BigNumber object with random numbers inside specific interval and only n numbers not null.
     * @param minWordValue : if random is true, min limit for random word values
     * @param maxWordValue : if random is true, max limit for random word values
     * @param n : number of not null words
     */
    public BigNumber(int minWordValue, int maxWordValue, int n){
        this.size = 8;
        value = new int[this.size];
        if (n < this.size) {
            randomValue(minWordValue, maxWordValue, n);
        } else {
            System.out.println("Error: n is too big (must be less than 8).");
        }
    }

    /**
     * Constructor for 256-bit BigNumber object used for modulo.
     * @param prime_nb : value of the modulo (32 bit)
     * @param n : index of not null word (that equals prime_nb)
     */
    public BigNumber(int prime_nb, int n){
        this.size = 8;
        value = new int[this.size];
        value[n] = prime_nb;
    }

    /**
     * Constructor for 256-bit BigNumber object that equals 1.
     * @param one : must be "1" to have a BigNumber value of 1
     */
    public BigNumber(String one){
        this.size = 8;
        value = new int[this.size];
        if (one.equals("1")) {
            value[this.size - 1] = 1;
        }
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
     * Function returning a BigInteger from value parameter array.
     * @return
     */
    public BigInteger toBigInteger(){
        String binary_string = "";
        for (int i=0; i < this.size; i++){
            binary_string += String.format("%31s", Integer.toBinaryString(this.value[i])).replace(' ', '0');
        }
        return new BigInteger(binary_string, 2);
    }

    /**
     * Function computing modular addition.
     * @param B BigNumber to add
     * @param modulo modulo value
     */
    public void modular_add(BigNumber B, BigNumber modulo){
        BigNumber result = new BigNumber(this.size);
        System.arraycopy(this.value, 0, result.value, 0, this.size);
        result.add_by_word(B);
        if (result.sup(modulo)){
            result.sub_by_word(modulo);
        }
        this.value = result.value;
    }

    /**
     * Function computing modular addition using Java BigIntegers.
     * @param B BigNumber to add
     * @param modulo modulo value
     */
    public BigInteger modular_add_java(BigNumber B, BigNumber modulo){
        return this.toBigInteger().add(B.toBigInteger()).mod(modulo.toBigInteger());
    }

    /**
     * Function computing modular substraction.
     * @param B BigNumber to substract
     * @param modulo modulo value
     */
    public void modular_sub(BigNumber B, BigNumber modulo){
        BigNumber result = new BigNumber(false, 0);
        System.arraycopy(this.value, 0, result.value, 0, this.size);
        if (this.sup(B)){
            result.sub_by_word(B);
        } else {
            result.add_by_word(modulo);
            result.sub_by_word(B);
        }
        this.value = result.value;
    }

    /**
     * Function computing modular substraction using Java BigIntegers.
     * @param B BigNumber to substract
     * @param modulo modulo value
     */
    public BigInteger modular_sub_java(BigNumber B, BigNumber modulo){
        return this.toBigInteger().subtract(B.toBigInteger()).mod(modulo.toBigInteger());
    }

    public BigNumber montgomery_algo(BigNumber A, BigNumber B, BigNumber modulo, BigNumber r, BigNumber v, int k){
        BigNumber s = A.mult_by_word(B);
        int double_size = this.size * 2;
        BigNumber v_double = new BigNumber(double_size);
        System.arraycopy(v.value, 0, v_double.value, this.size, this.size);
        BigNumber t = s.mult_by_word(v);
        t.compute_modulo_r(k);
        BigNumber t_x_n = t.mult_by_word(modulo);
        s.add_by_word(t_x_n); // equals m
        return s;
    }


    /**
     * Function implementing >= operator for BigNumber objects.
     * @param B: BigNumber
     * @return boolean
     */
    private boolean sup(BigNumber B){
        boolean sup = false;
        boolean found = false;
        int max = this.size;
        int min = B.size;
        boolean this_is_sup = true;
        BigNumber maxNumber = this;
        BigNumber minNumber = B;
        if (this.size <= B.size){
            min = this.size;
            max = B.size;
            maxNumber = B;
            minNumber = this;
        }
        int k = 0;
        if (min == max) {
            while (!found && k < max) {
                if (this.value[k] > B.value[k]) {
                    sup = true;
                    found = true;
                } else if (this.value[k] < B.value[k]) {
                    sup = false;
                    found = true;
                } else {
                    k++;
                }
            }
        } else {
            while (!found && k < max){
                if (k < min && maxNumber.value[k] > 0){
                    if (this_is_sup) {
                        sup = true;
                        found = true;
                    } else {
                        sup = false;
                        found = true;
                    }
                } else if (k < min && maxNumber.value[k] == 0) {
                    k++;
                } else {
                    if (maxNumber.value[k] > minNumber.value[k - min]) {
                        if (this_is_sup) {
                            sup = true;
                            found = true;
                        } else {
                            sup = false;
                            found = true;
                        }
                    } else if (maxNumber.value[k] < minNumber.value[k - min]) {
                        if (this_is_sup) {
                            sup = false;
                            found = true;
                        } else {
                            sup = true;
                            found = true;
                        }
                    } else {
                        k++;
                    }
                }
            }
        }
        if (k == max) {
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
                if (this.value[k] >= B.value[k]) {
                    result[k] += this.value[k] - B.value[k];
                } else if (this.value[k] != 0 || B.value[k] != 0 || result[k] != 0){
                    result[k] += (int) (this.value[k] + TWO_POW_31 - B.value[k]);
                    if (k > 0) {
                        result[k - 1] -= 1;
                    }
                }
                k--;
            }
            this.value = result;
        } else {
            System.out.println("[Sub_by_word] Error: Input has different size than current BigNumber.");
        }
    }

    /**
     * Function computing addition between value parameter and B.value.
     * @param B
     * @return carry between each MSB
     */
    private void add_by_word(BigNumber B){
        if (B.size == this.size) {
            BigNumber result = new BigNumber(this.size);
            // long mask_retenue = 0xFFFF0000L;
            // int mask_32 = 0xFFFF;
            long carry = 0;
            for (int i=this.size - 1; i>=0; i--) {
                long ai_plus_bi = (long) this.value[i] + B.value[i] + result.value[i];
                long temp = ai_plus_bi & FILTER_32_BIT;
                result.value[i] = (int) temp;
                carry = ai_plus_bi >> 31;
                if (i > 0) {
                    result.value[i - 1] = (int) carry;
                } else if (carry > 0){
                    System.out.println("Error: Inputs are too big to compute result on 256 bits.");
                }
            }
            this.value = result.value;
        } else {
            System.out.println("[Add_by_word] Error: Input has different size than current BigNumber.");
        }
    }


    /**
     * Function computing multiplication.
     * @param B BigNumber to substract
     */
    public BigNumber mult_by_word(BigNumber B){
        int double_size = this.size * 2;
        //BigNumber modulo_double = new BigNumber(double_size);
        //System.arraycopy(modulo.value, 0, modulo_double.value, this.size, this.size);
        BigNumber result = new BigNumber(double_size);
        for (int ai=0; ai<this.size; ai++){
            for (int bi=0; bi<this.size; bi++){
                long mult = (long) this.value[this.size - 1 - ai] * B.value[this.size - 1 - bi];
                BigNumber temp = new BigNumber(double_size);
                temp.value[double_size - 1 - ai - bi] = (int) ((int) mult & FILTER_32_BIT);
                long temp_carry = mult >> 31;
                temp.value[double_size - 2 - ai - bi] = (int) temp_carry;
                //result.modular_add(temp, modulo_double);
                result.add_by_word(temp);

            }
        }
        return result;
    }

    /**
     * Function computing multiplication using java BigIntegers.
     * @param B BigNumber to multiply
     */
    public BigInteger mult_by_word_java(BigNumber B){
        return this.toBigInteger().multiply(B.toBigInteger());
    }

    /**
     * Function computing BigNumber modulo r (r = 2^k) in Montgomery algorithm.
     * @param k : parameter such as r = 2^k
     */
    public void compute_modulo_r(int k){
        int k_31 = k/31;
        for (int i=0; i < this.size - 1 - k_31; i++){
            this.value[i] = 0;
        }
        for (int i=0; i<=k_31; i++){
            int pow = k - i*31 < 32 ? k - i*31 : 31;

            // keep 2^pow first LSB bits
            this.value[this.size - 1 - i] = this.value[this.size - 1 - i] & ((2 << pow-1) - 1);
        }
    }

    /**
     * Function computing BigNumber modulo r (r = 2^k) in Montgomery algorithm using java BigIntegers.
     * @param r : parameter from Montgomery algorithm
     * @return result of BigNumber modulo r
     */
    public BigInteger compute_module_r_java(BigNumber r){
        return this.toBigInteger().mod(r.toBigInteger());
    }

    /**
     * Function computing BigNumber divided by r (r = 2^k) in Montgomery algorithm.
     * @param k : parameter such as r = 2^k
     */
    public void divide_by_r(int k){
        int k_31 = k/31;
        int max_31_k = k > 31 ? 31 : k;
        int k_31_rest = k % 31;
        int max_31_k_rest = k_31_rest == 0 ? 31 : k_31_rest;
        int temp = 0;
        for (int i=this.size - 1; i>k_31; i--){
            // keep 2^k_31_rest first LSB bits
            if (i >= k_31 + 1) {
                this.value[i] = this.value[i - k_31] >> k_31_rest;
                temp = this.value[i - 1 - k_31] & ((2 << k_31_rest - 1) - 1);
            } else {
                temp = 0;
                this.value[i] = 0;
            }
            int temp3 = (temp << 31 - k_31_rest);
            this.value[i] = this.value[i] + (temp << 31 - k_31_rest);
        }
        for (int i=0; i<=k_31; i++){
            this.value[i] = 0;
        }
    }

    public BigInteger divide_by_r_java(BigNumber r){
        return this.toBigInteger().divide(r.toBigInteger());
    }
}
