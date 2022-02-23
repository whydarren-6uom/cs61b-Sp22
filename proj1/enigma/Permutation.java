package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Darren Wang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles.replaceAll("\\s", "");
        if (!derangement()) {
            throw new EnigmaException("Permutation is NOT a derangement");
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        int index = _cycles.indexOf(alphabet().toChar(p));
        if (index < 0) {
            return p;
        }
        Character chr = _cycles.charAt(index + 1);
        if (chr.equals(')')) {
            for (int i = index; i > 0; i--) {
                Character c = _cycles.charAt(i - 1);
                if (c.equals('(')) {
                    c = _cycles.charAt(i);
                    return alphabet().toInt(c);
                }
            }
        }
        return alphabet().toInt(_cycles.charAt(index + 1));
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        int index = _cycles.indexOf(alphabet().toChar(c));
        if (index < 0) {
            return c;
        }
        Character chr = _cycles.charAt(index - 1);
        if (chr.equals('(')) {
            for (int i = index; i < _cycles.length(); i++) {
                Character p = _cycles.charAt(i + 1);
                if (p.equals(')')) {
                    p = _cycles.charAt(i);
                    return alphabet().toInt(p);
                }
            }
        }
        return alphabet().toInt(_cycles.charAt(index - 1));
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return alphabet().toChar(permute(alphabet().toInt(p)));
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return alphabet().toChar(permute(alphabet().toInt(c)));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < size(); i++) {
            if (_cycles.indexOf(alphabet().toChar(i))
                    != _cycles.lastIndexOf(alphabet().toChar(i))) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    private String _cycles;
}
