package enigma;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static enigma.EnigmaException.error;

/** Class that represents a complete enigma machine.
 *  @author Darren Wang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        for (Rotor r : allRotors) {
            _allRotorsMap.put(r.name(), r);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Change the numRotor.
     * @param i num of rotors.
     */
    void alterNumRotors(int i) {
        _numRotors = i;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _insertedRotors.get(k);
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < numRotors(); i++) {
            if (i == 0 && !_allRotorsMap.get(rotors[i]).reflector()) {
                throw error("Wrong place for the reflector.");
            }
            if (!_allRotorsMap.containsKey(rotors[i])) {
                throw error("No such rotor in the rotor stock.");
            }
            if (!set.add(rotors[i])) {
                throw error("Duplicate rotor detected.");
            } else {
                set.add(rotors[i]);
            }
            _insertedRotors.put(i, _allRotorsMap.get(rotors[i]));
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 1; i < numRotors(); i++) {
            _insertedRotors.get(i)._setting =
                    _alphabet.toInt(setting.charAt(i - 1));
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.print("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {
        boolean[] m = new boolean[numRotors()];
        for (int j = 0; j < numRotors(); j++) {
            m[j] = (j == numRotors() - 1)
                    || (_insertedRotors.get(j).rotates()
                    && _insertedRotors.get(j + 1).atNotch());
        }
        for (int j = 0; j < numRotors(); j++) {
            if (m[j]) {
                _insertedRotors.get(j).advance();
                if (j < numRotors() - 1) {
                    _insertedRotors.get(j + 1).advance();
                    j++;
                }
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        for (int i = numRotors() - 1; i >= 0; i--) {
            c = _insertedRotors.get(i).convertForward(c);
        }
        for (int i = 1; i < numRotors(); i++) {
            c = _insertedRotors.get(i).convertBackward(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < msg.length(); i++) {
            int chr = alphabet().toInt(msg.charAt(i));
            result.append(alphabet().toChar(convert(chr)));
        }
        return result.toString();
    }

    /** Set the ring setting to RINGSETTING.
     * By default, start with the first char in alphabet
     * @param ringsetting RINGSETTING
     * */
    void setRing(String ringsetting) {
        if (ringsetting.equals("")) {
            StringBuilder ringsettingBuilder = new StringBuilder(ringsetting);
            for (int i = 1; i < numRotors(); i++) {
                ringsettingBuilder.append(alphabet().toChar(0));
            }
            ringsetting = ringsettingBuilder.toString();
        }
        for (int i = 1; i < numRotors(); i++) {
            _insertedRotors.get(i).setRing(ringsetting.charAt(i - 1));
        }
    }

    /**
     * Find a rotor based on the name of a rotor.
     * @param name name of the rotor
     * @return the rotor
     */
    Rotor findRotors(String name) {
        try {
            return _allRotorsMap.get(name);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of Rotors slots on a machine.
     */
    private int _numRotors;

    /** Number of pawls on a machine. It also represents the
     *  number of MovingRotors.
     */
    private final int _pawls;

    /** A new hashmap to save all rotors,
     *  the keys are the names of rotors.
     */
    private final HashMap<String, Rotor> _allRotorsMap = new HashMap<>();

    /** A new hashmap to save all inserted rotors,
     *  the keys are the index of rotors inserted.
     */
    private final HashMap<Integer, Rotor> _insertedRotors = new HashMap<>();

    /** A permutation instance to save plugboard
     *  permutations.
     */
    private Permutation _plugboard;
}
