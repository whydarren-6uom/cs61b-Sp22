/** Solutions to the HW0 Java101 exercises.
 *  @author Allyson Park and [INSERT YOUR NAME HERE]
 */
public class Solutions {

    /** Returns whether or not the input x is even.
     */
    public static boolean isEven(int x) {
        return x % 2 == 0;
    }

    public static int max(int[] a) {
        int maxi = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] > maxi) {
                maxi = a[i];
            }
        }
        return maxi;
    }

    public static boolean wordBank(String word, String[] bank) {
        for (int i = 0; i < bank.length; i++) {
            if (word.equals(bank[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean threeSum(int[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < a.length; k++){
                    if (a[i] + a[j] + a[k] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
