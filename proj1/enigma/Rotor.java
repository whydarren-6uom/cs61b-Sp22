package enigma;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Darren Wang
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** Return my current ringSetting. */
    int ringSetting() {
        return _ringSetting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set ringSetting() of rotor to character CPOSN. */
    void setRing(char cposn) {
        _ringSetting = permutation().alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int result = permutation().permute(p + setting() - ringSetting())
                - setting() + ringSetting();
        result = permutation().wrap(result);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        return result;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int result = permutation().invert(e + setting() - ringSetting())
                - setting() + ringSetting();
        result = permutation().wrap(result);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(result));
        }
        return result;
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return notches().contains(String.valueOf(alphabet().toChar(setting())));
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private final Permutation _permutation;

    /** Current setting of a Rotor. By default 0.
     */
    protected int _setting = 0;

    /** Current ring setting of a rotor.
     * By default 0.
     */
    protected int _ringSetting;
}
