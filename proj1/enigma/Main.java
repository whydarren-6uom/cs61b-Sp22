package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Darren Wang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                            + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
      *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(name);
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        if (!_input.hasNext("(?<=^|\n)\\*.*")) {
            throw new EnigmaException("Invalid input file.");
        }
        while (_input.hasNext("(?<=^|\n)\\*.*")) {
            String[] rotors = new String[machine.numRotors()];
            String ifstar = _input.next();
            if (ifstar.equals("*")) {
                rotors[0] = _input.next();
            } else {
                rotors[0] = ifstar.substring(1);
            }
            for (int i = 1; i < machine.numRotors(); i++) {
                rotors[i] = _input.next();
            }
            machine.insertRotors(rotors);
            setUp(machine, _input.next());

            Scanner scanner = new Scanner(_input.nextLine());
            String ringsetting = "";
            if (scanner.hasNext()
                    && !scanner.hasNext("(?<!\\()(\\(.+\\))(?!\\))")) {
                ringsetting = scanner.next();
            }
            machine.setRing(ringsetting);

            StringBuilder cycle = new StringBuilder();
            while (scanner.hasNext(".*([()])+.*")) {
                cycle.append(scanner.next());
            }
            machine.setPlugboard(new Permutation(cycle.toString(), _alphabet));

            while (_input.hasNextLine()
                    && !_input.hasNext("(?<=^|\n)\\*.*")) {
                String nextLine = _input.nextLine().replaceAll
                        ("\s", "");
                printMessageLine(machine.convert(nextLine));
            }
            _output.print("\n");
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int nRotors = _config.nextInt();
            int nPawls = _config.nextInt();
            HashMap<String, Rotor> rotors = new HashMap<>();
            while (_config.hasNext()) {
                Rotor rotor = readRotor();
                if (rotors.containsKey(rotor.name())) {
                    throw error("Duplicate rotor detected.");
                }
                rotors.put(rotor.name(), rotor);
            }
            return new Machine(_alphabet, nRotors, nPawls, rotors.values());
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            Rotor rotor;
            String name = _config.next();
            String typeNotch = _config.next();
            char type = typeNotch.charAt(0);
            String notch = typeNotch.substring(1);
            StringBuilder cycle = new StringBuilder();
            if (name.contains("(") || name.contains(")")) {
                throw error("'(' or ')' not allowed"
                        + " in rotor name.");
            }
            while (_config.hasNext(".*[(|)]+.*")) {
                cycle.append(_config.next());
            }
            Permutation permutation
                    = new Permutation(cycle.toString(), _alphabet);
            if (type == 'M') {
                if (notch.length() == 0) {
                    throw error("No notch detected "
                            + "for moving rotor.");
                }
                checkNotchAlphabet(_alphabet, notch);
                rotor = new MovingRotor(name, permutation, notch);
            } else if (type == 'N') {
                if (notch.length() != 0) {
                    throw error("Notch detected for "
                            + "a fixed rotor.");
                }
                rotor = new FixedRotor(name, permutation);
            } else if (type == 'R') {
                if (notch.length() != 0) {
                    throw error("Notch detected for "
                            + "a reflector.");
                }
                rotor = new Reflector(name, permutation);
            } else {
                throw error("No Such Kind of Rotor");
            }
            return rotor;
        } catch (NoSuchElementException excp) {
            throw error("Bad rotor description");
        }
    }

    /** Check if the character in notch is in the alphabet.
     * @param alpha the alphabet of the very notch.
     * @param notch the notch provided.
     */
    private void checkNotchAlphabet(Alphabet alpha, String notch) {
        for (int i = 0; i < notch.length(); i += 1) {
            if (!alpha.contains(notch.charAt(i))) {
                throw error("Notch "
                        + notch.charAt(i)
                        + " not found in alphabet.");
            }
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        M.setRotors(settings);
    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            _output.print(msg.charAt(i));
            if (((i + 1) % 5 == 0) && ((i + 1) != msg.length())) {
                _output.print(" ");
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private final Scanner _input;

    /** Source of machine configuration. */
    private final Scanner _config;

    /** File for encoded/decoded messages. */
    private final PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
