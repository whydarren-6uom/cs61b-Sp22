package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author Darren Wang
 */
class Arrays {

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        int[] n = new int[A.length + B.length];
        System.arraycopy(A, 0, n, 0, A.length);
        System.arraycopy(B, 0, n, A.length, B.length);
        return n;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. If the start + len is out of bounds for our array, you
     *  can return null.
     *  Example: if A is [0, 1, 2, 3] and start is 1 and len is 2, the
     *  result should be [0, 3]. */
    static int[] remove(int[] A, int start, int len) {
        int[] n = new int[A.length - len];
        int i = 0;
        int j = 0;
        while (i < A.length) {
            if (i < start || i >= start + len) {
                n[j] = A[i];
                j++;
            }
            i++;
        }
        return n;
    }

}
