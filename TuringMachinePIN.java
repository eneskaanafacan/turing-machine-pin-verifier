import java.util.*;

public class TuringMachinePIN {

    public enum State {
        Q0, Q1, Q2, Q3, Q4, Q5, Q6, ACCEPT, REJECT
    }

    public static class Tape {
        private List<Character> tape;
        private int head;

        public Tape(String input) {
            tape = new ArrayList<>();
            for (char c : input.toCharArray()) tape.add(c);
            head = 0;
        }

        public char read() {
            return (head < 0 || head >= tape.size()) ? 'B' : tape.get(head);
        }

        public void write(char c) {
            while (head >= tape.size()) tape.add('B');
            if (head >= 0) tape.set(head, c);
        }

        public void moveLeft() { head--; }
        public void moveRight() { head++; }
        public int getHead() { return head; }

        public String getFormattedTape() {
            StringBuilder sb = new StringBuilder("Şerit: ");
            for (int i = 0; i < tape.size(); i++) {
                sb.append(i == head ? "[" + tape.get(i) + "]" : " " + tape.get(i) + " ");
            }
            return sb.toString();
        }
    }

    private static class Transition {
        State nextState;
        char writeChar;
        char direction;

        Transition(State nextState, char writeChar, char direction) {
            this.nextState = nextState;
            this.writeChar = writeChar;
            this.direction = direction;
        }
    }

    private Map<String, Transition> transitions = new HashMap<>();
    private State currentState;
    private Tape tape;
    private char rememberedChar;

    public TuringMachinePIN() {
        rememberedChar = 'B';
        initializeTransitions();
    }

    private void initializeTransitions() {
        transitions.put("Q0,#", new Transition(State.Q1, '#', 'R'));

        for (char digit = '0'; digit <= '9'; digit++) {
            transitions.put("Q1," + digit, new Transition(State.Q2, 'X', 'R'));
            transitions.put("Q3," + digit, new Transition(State.Q4, 'Y', 'L'));
        }
        transitions.put("Q1,#", new Transition(State.Q6, '#', 'L'));
        transitions.put("Q1,X", new Transition(State.Q1, 'X', 'R'));

        // Q2: Kullanıcı PIN karakteri işaretlendi -> ikinci #’ya ulaş
        for (char c : "0123456789XY".toCharArray()) {
            transitions.put("Q2," + c, new Transition(State.Q2, c, 'R'));
        }
        transitions.put("Q2,#", new Transition(State.Q3, '#', 'R'));

        transitions.put("Q3,Y", new Transition(State.Q3, 'Y', 'R'));
        transitions.put("Q3,#", new Transition(State.REJECT, '#', 'S'));

        // Q4: Başa dön
        transitions.put("Q4,B", new Transition(State.Q5, 'B', 'R'));
        for (char c : "0123456789XY#".toCharArray()) {
            transitions.put("Q4," + c, new Transition(State.Q4, c, 'L'));
        }

        // Q5: sıradaki işlenmemiş kullanıcı PIN karakterine ulaş
        for (char c : "Y0123456789#".toCharArray()) {
            transitions.put("Q5," + c, new Transition(State.Q5, c, 'R'));
        }
        transitions.put("Q5,X", new Transition(State.Q1, 'X', 'S'));

        // Q6: Tüm X'ler kontrol edildi mi?
        transitions.put("Q6,X", new Transition(State.Q6, 'X', 'L'));
        transitions.put("Q6,#", new Transition(State.ACCEPT, '#', 'S'));
        for (char d = '0'; d <= '9'; d++) {
            transitions.put("Q6," + d, new Transition(State.REJECT, d, 'S'));
        }
    }

    public boolean run(String input) {
        tape = new Tape(input);
        currentState = State.Q0;
        int step = 0;

        while (currentState != State.ACCEPT && currentState != State.REJECT) {
            char currentChar = tape.read();
            String key = currentState + "," + currentChar;

            System.out.println("Adım " + (++step) + ": Durum=" + currentState + ", Okunan='" + currentChar + "'");
            System.out.println(tape.getFormattedTape());

            if (currentState == State.Q1 && currentChar >= '0' && currentChar <= '9') {
                rememberedChar = currentChar;
            } else if (currentState == State.Q3 && currentChar >= '0' && currentChar <= '9') {
                if (rememberedChar != currentChar) {
                    System.out.println("Karakter uyuşmazlığı: '" + rememberedChar + "' ≠ '" + currentChar + "'");
                    currentState = State.REJECT;
                    break;
                }
            }

            Transition transition = transitions.get(key);

            if (transition == null) {
                System.out.println("Tanımsız geçiş - REJECT");
                currentState = State.REJECT;
                break;
            }

            tape.write(transition.writeChar);
            if (transition.direction == 'L') tape.moveLeft();
            else if (transition.direction == 'R') tape.moveRight();

            currentState = transition.nextState;
        }

        return currentState == State.ACCEPT;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final String systemPIN = "1234";

        System.out.print("4 haneli PIN girin: ");
        String userPIN = scanner.nextLine().trim();

        if (!userPIN.matches("\\d{4}")) {
            System.out.println("Geçersiz PIN. 4 haneli rakam girin.");
            return;
        }

        String input = "#" + userPIN + "#" + systemPIN + "#";
        TuringMachinePIN tm = new TuringMachinePIN();
        boolean result = tm.run(input);

        System.out.println("\nSonuç: " + (result ? "Şifre doğru (ACCEPT)" : "Şifre hatalı (REJECT)"));
    }
}
