package ismin_ms_crypto;

import java.util.Random;

public class BigNumber {

    private int size;
    private int[] value; // [0] : MSB ; [size - 1] : LSB

    public BigNumber(int size){
        this.size = size;
        value = new int[this.size];
    }

    /**
     * Function initializing BigNumber value by randoms.
     */
    public void randomValue(){
        Random ran = new Random();
        for (int i=0; i<this.size; i++){
            value[i] = Math.abs(ran.nextInt(100));
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
            returned += this.value[this.size - 1 - i] * Math.pow(2, 31*i);
        }
        return returned;
    }

    public void modular_add(BigNumber B){
        BigNumber result = new BigNumber(this.size);
        long mask_retenue = 0xFFFF0000;
        int mask_32 = 0xFFFF;
        for (int i=this.size - 1; i<=0; i--){
            long ai_plus_bi = this.value[i] + B.value[i] + result.value[i];
            result.value[i] = (int) ((int) mask_32 & ai_plus_bi);
            int retenue = (int) ((int) mask_retenue & ai_plus_bi);
            if (i > 0) {
                result.value[i + 1] = retenue;
            } else {
                if (result.sup(B) || retenue > 0){
                    result.sub_by_word(B);
                }
            }
        }
        this.value = result.value;
    }

    public void modular_sub(BigNumber B){

    }

    /**
     * Function implementing > operator for BigNumber objects.
     * @param B: BigNumber
     * @return boolean
     */
    public boolean sup(BigNumber B){
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
        return sup;
    }

    /**
     * Function computing substraction between value parameter and B.value considering value > B.value.
     * @param B : BigNumber object
     */
    public void sub_by_word(BigNumber B){
        if (B.size == this.size) {
            int result[] = new int[this.size];
            int k = this.size - 1;
            while (k >= 0) {
                if (this.value[k] > B.value[k]) {
                    result[k] += this.value[k] - B.value[k];
                } else {
                    result[k] += (int) (this.value[k] + Math.pow(2, 31) - B.value[k]);
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
}
