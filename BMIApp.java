import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BMIApp extends JFrame {

    private final CardLayout layout = new CardLayout();
    private final JPanel cards = new JPanel(layout);

    // Welcome page
    private JTextField nameField;

    // Calculator page
    private JTextField heightField; // cm
    private JTextField weightField; // kg
    private JTextArea resultArea;

    public BMIApp() {
        super("BMI Project");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(450, 320);
        setLocationRelativeTo(null);

        cards.add(buildWelcomePanel(), "WELCOME");
        cards.add(buildCalculatorPanel(), "CALC");

        setContentPane(cards);
        layout.show(cards, "WELCOME");
    }

    private JPanel buildWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Welcome to BMI Calculator", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));

        JPanel center = new JPanel(new GridLayout(2, 1, 8, 8));
        center.setOpaque(false);

        nameField = new JTextField();
        nameField.setText("Your Name");

        JLabel hint = new JLabel("Enter your name and click Start", SwingConstants.CENTER);

        center.add(nameField);
        center.add(hint);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> layout.show(cards, "CALC"));

        panel.add(title, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCalculatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel header = new JLabel("BMI Calculator (Height: cm, Weight: kg)", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));

        form.add(new JLabel("Name:"));
        JLabel namePreview = new JLabel("");
        form.add(namePreview);

        form.add(new JLabel("Height (cm):"));
        heightField = new JTextField();
        form.add(heightField);

        form.add(new JLabel("Weight (kg):"));
        weightField = new JTextField();
        form.add(weightField);

        JButton calcButton = new JButton("Calculate BMI");
        JButton backButton = new JButton("Back");

        form.add(calcButton);
        form.add(backButton);

        // Result area
        resultArea = new JTextArea(4, 25);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(resultArea);

        // Actions
        calcButton.addActionListener(e -> {
            namePreview.setText(getSafeName());
            calculateBMI();
        });

        backButton.addActionListener(e -> layout.show(cards, "WELCOME"));

        panel.add(header, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        // Default text
        resultArea.setText("Enter height and weight, then press Calculate BMI.");
        return panel;
    }

    private String getSafeName() {
        String n = (nameField == null) ? "" : nameField.getText().trim();
        return n.isEmpty() ? "Guest" : n;
    }

    private void calculateBMI() {
        try {
            double heightCm = Double.parseDouble(heightField.getText().trim());
            double weightKg = Double.parseDouble(weightField.getText().trim());

            if (heightCm <= 0 || weightKg <= 0) {
                JOptionPane.showMessageDialog(this, "Height and weight must be positive numbers!");
                return;
            }

            double heightM = heightCm / 100.0;
            double bmi = weightKg / (heightM * heightM);

            String status;
            if (bmi < 18.5) status = "Underweight";
            else if (bmi < 25) status = "Normal weight";
            else if (bmi < 30) status = "Overweight";
            else status = "Obese";

            resultArea.setText(
                    "Hello, " + getSafeName() + "!\n" +
                    "Your BMI: " + String.format("%.2f", bmi) + "\n" +
                    "Status: " + status
            );

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for height and weight!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BMIApp().setVisible(true));
    }
}
