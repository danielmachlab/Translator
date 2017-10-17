import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


/**
 * The ConvertNumbers class contains all fields and methods specific to converting between arabic numbers and
 * roman numeral. ConvertNumbers inherits from JFrame.
 */
public class ConvertNumbers extends JFrame {

    /**
     * Text field to input and output roman numerals
     */
    private JTextField romanInput;

    /**
     * Text field to input and output arabic numbers
     */
    private JTextField arabicInput;

    /**
     * Map with keys that are base roman numerals and values which are corresponding numbers
     */
    private Map<String, Integer> romanArabicMap;

    /**
     * Map with keys that are numbers corresponding to the values which are base roman numerals
     */
    private Map<Integer, String> arabicRomanMap;


    /**
     * Constructor for ConvertNumbers. The constructor calls the super constructor to set the title. Additionally, all
     * JLabels, JPanels, and JTextFields that are used by the GUI are initialized and added to the layout.
     */
    public ConvertNumbers() {
        super("Welcome to ConvertNumbers");
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

        Font font = new Font("Helvetica Neue ", Font.PLAIN, 32);
        romanArabicMap = new HashMap<>();
        arabicRomanMap = new HashMap<>();

        JPanel romanRow = new JPanel(new FlowLayout());
        JPanel arabicRow = new JPanel(new FlowLayout());

        romanInput = new JTextField(10);
        arabicInput = new JTextField(10);

        JLabel romanLabel = new JLabel("Roman Numeral: ");
        JLabel arabicLabel = new JLabel("Arabic Number:");

        romanRow.add(romanLabel);
        romanRow.add(romanInput);
        arabicRow.add(arabicLabel);
        arabicRow.add(arabicInput);

        this.add(controlPanel);
        controlPanel.add(romanRow);
        controlPanel.add(arabicRow);

        romanInput.setFont(font);
        romanLabel.setFont(font);
        arabicLabel.setFont(font);
        arabicInput.setFont(font);

        romanInput.getDocument().addDocumentListener(new TheDocListener());
        romanInput.getDocument().putProperty("owner", "roman");

        arabicInput.getDocument().addDocumentListener(new TheDocListener());
        arabicInput.getDocument().putProperty("owner", "arabic");
    }

    /**
     * Initializes both romanArabicMap and arabicRomanMap. Allows maps to be reinitialized after removal of pairs
     */
    private void initMap() {
        romanArabicMap.put("I", 1);
        romanArabicMap.put("V", 5);
        romanArabicMap.put("X", 10);
        romanArabicMap.put("L", 50);
        romanArabicMap.put("C", 100);
        romanArabicMap.put("D", 500);
        romanArabicMap.put("M", 1000);
        romanArabicMap.put("IV", 4);
        romanArabicMap.put("IX", 9);
        romanArabicMap.put("XL", 40);
        romanArabicMap.put("XC", 90);
        romanArabicMap.put("CD", 400);
        romanArabicMap.put("CM", 900);

        arabicRomanMap.put(1, "I");
        arabicRomanMap.put(5, "V");
        arabicRomanMap.put(10, "X");
        arabicRomanMap.put(50, "L");
        arabicRomanMap.put(100, "C");
        arabicRomanMap.put(500, "D");
        arabicRomanMap.put(1000, "M");
        arabicRomanMap.put(4, "IV");
        arabicRomanMap.put(9, "IX");
        arabicRomanMap.put(40, "XL");
        arabicRomanMap.put(90, "XC");
        arabicRomanMap.put(400, "CD");
        arabicRomanMap.put(900, "CM");
    }


    /**
     * Class TheDocListener inherits DocumentListener to detect an insertion or removal in the GUI's JTextFields
     * and updates output.
     */
    class TheDocListener implements DocumentListener {

        /**
         * Detects when an insertion has been made to a JTextField/Document
         * @param e event that occurred in the JTextField/Document
         */
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        /**
         * Detects when a removal has been made to a JTextField/Document
         * @param e event that occurred in the JTextField/Document
         */
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        public void changedUpdate(DocumentEvent e) {
//            System.out.println("changeUpdate");
//            update(e);
        }


        /**
         * Updates the field opposite of the input with the corresponding value.
         * @param e event that occurred in the JTextField/Document
         */
        private void update(DocumentEvent e) {
            Document document = e.getDocument();
            int length = document.getLength();


            if (document.getProperty("owner") == "roman" && romanInput.isFocusOwner()) {
                try {
                    char[] romanCharArr = e.getDocument().getText(0, length).toUpperCase().toCharArray();
                    arabicInput.setText(romanToArabic(romanCharArr));
                } catch (Exception er) {
                    System.out.println("BadLocationException");
                }
            }

            if (document.getProperty("owner") == "arabic" && arabicInput.isFocusOwner()) {
                try {
                    String arabicString = e.getDocument().getText(0, length).toUpperCase();
                    romanInput.setText(arabicToRoman(arabicString));
                } catch (Exception er) {
                    System.out.println("BadLocationException");
                }
            }
        }
    }

    /**
     * This method converts a arabic number to a roman numeral and returns the roman numeral as a String object.
     * @param arabicNumberString input the arabic number as a string.
     * @return output the numan numeral as a String object.
     */
    public String arabicToRoman(String arabicNumberString) {
        if (arabicNumberString.length() == 0)
            return "";
        int[] divisors = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        int divisorIndex = 0;
        String romanNumeral = "";
        int placeValueOccurrences;
        int arabicNumber = Integer.parseInt(arabicNumberString);
        initMap();


        if (arabicNumber < 1 || arabicNumber > 3999)
            return "Invalid";

        while (arabicNumber > 0) {
            int divisor = divisors[divisorIndex];
            placeValueOccurrences = arabicNumber / divisor;

            if (placeValueOccurrences == 0)
                divisorIndex++;
            else {
                for (int i = 0; i < placeValueOccurrences; i++) {
                    arabicNumber -= divisor;
                    romanNumeral = romanNumeral + arabicRomanMap.get(divisor);
                }
            }
        }
        return romanNumeral;
    }


    /**
     * Converts a roman numeral to an arabic number
     * @param romanNumeral the roman numeral to be converted as a char[]
     * @return Returns the arabic number as a String
     */
    private String romanToArabic(char[] romanNumeral) {
        initMap();
        if (romanNumeral.length == 0)
            return "";
        String currentLetter;
        String peekAheadLetter;
        int currentNumber;
        int peekAheadNumber;
        int sum = 0;

        for (int i = 0; i < romanNumeral.length; i++) {
            currentLetter = romanNumeral[i] + "";
            if (i != romanNumeral.length - 1)
                peekAheadLetter = romanNumeral[i + 1] + "";
            else
                peekAheadLetter = currentLetter;
            currentNumber = romanArabicMap.get(currentLetter);
            peekAheadNumber = romanArabicMap.get(peekAheadLetter);

            if (peekAheadNumber > currentNumber) {
                String combo = currentLetter + peekAheadLetter;

                sum += romanArabicMap.get(combo);
                romanArabicMap.remove(combo);
                i++;
            } else
                sum += currentNumber;
        }

        String sumString = sum + "";

        if(verifyRomanNumeral(romanNumeral, sumString))
            return sumString;
        else
            return "Invalid";
    }



    /**
     * This class verifies that a roman numeral is valid.
     * @param possibleRomanNumeral Input roman numeral from user. The roman numeral to be validated.
     * @param calculatedNumber Number calculated in romanToArabic().
     * @return Returns a boolean whether the roman numeral is valid or not.
     */
    private Boolean verifyRomanNumeral(char[] possibleRomanNumeral, String calculatedNumber) {
        String romanNumeralToTest = new String(possibleRomanNumeral);

        return arabicToRoman(calculatedNumber).equals(romanNumeralToTest);

    }

}
