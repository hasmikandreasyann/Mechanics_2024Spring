import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TimeTable extends JFrame implements ActionListener {

    private JPanel screen = new JPanel(), tools = new JPanel();
    private JButton tool[];
    private JTextField field[];
    private CourseArray courses; // Assuming CourseArray class is defined elsewhere
    private Color CRScolor[] = {Color.RED, Color.GREEN, Color.BLACK};
    private boolean running = false; // Flag to indicate whether the algorithm is running
    private int min; // Declare min variable within the class scope

    public TimeTable() {
        super("Dynamic Time Table");
        setSize(500, 800);
        setLayout(new BorderLayout());

        screen.setPreferredSize(new Dimension(400, 800));
        add(screen, BorderLayout.CENTER);

        setTools();
        add(tools, BorderLayout.EAST);

        setVisible(true);
    }

    public void setTools() {
        String capField[] = {"Slots:", "Courses:", "Clash File:", "Iters:", "Shift:"};
        field = new JTextField[capField.length];

        String capButton[] = {"Load", "Start", "Step", "Print", "Exit", "Continue"};
        tool = new JButton[capButton.length];

        tools.setLayout(new GridLayout(2 * capField.length + capButton.length, 1));

        for (int i = 0; i < field.length; i++) {
            tools.add(new JLabel(capField[i]));
            field[i] = new JTextField(6);
            tools.add(field[i]);
        }

        for (int i = 0; i < tool.length; i++) {
            tool[i] = new JButton(capButton[i]);
            tool[i].addActionListener(this);
            tools.add(tool[i]);
        }

        field[0].setText("17");
        field[1].setText("381");
        field[2].setText("lse-f-91.stu");
        field[3].setText("1");
    }

    public void draw() {
        screen.repaint();
    }

    private int getButtonIndex(JButton source) {
        int result = 0;
        while (source != tool[result]) result++;
        return result;
    }

    public void actionPerformed(ActionEvent click) {
        int step, clashes;

        switch (getButtonIndex((JButton) click.getSource())) {
            case 0:
                try {
                    int slots = Integer.parseInt(field[0].getText());
                    courses = new CourseArray(Integer.parseInt(field[1].getText()) + 1, slots);
                    courses.readClashes(field[2].getText());
                    draw();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input for slots or courses.");
                }
                break;
            case 1:
                // Start algorithm
                startAlgorithm();
                break;
            case 2:
                if (running) {
                    // Continue algorithm
                    continueAlgorithm();
                } else {
                    JOptionPane.showMessageDialog(this, "Algorithm is not running.");
                }
                break;
            case 3:
                if (courses != null) {
                    System.out.println("Exam\tSlot\tClashes");
                    for (int i = 1; i < courses.length(); i++)
                        System.out.println(i + "\t" + courses.slot(i) + "\t" + courses.status(i));
                } else {
                    JOptionPane.showMessageDialog(this, "Courses not loaded.");
                }
                break;
            case 4:
                System.exit(0);
                break;
        }
    }

    private void startAlgorithm() {
        if (!running) {
            running = true;
            min = Integer.MAX_VALUE;
            int step = 0;
            if (courses != null) {
                for (int i = 1; i < courses.length(); i++) courses.setSlot(i, 0);
                try {
                    for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
                        courses.iterate(Integer.parseInt(field[4].getText()));
                        draw();
                        int clashes = courses.clashesLeft();
                        if (clashes < min) {
                            min = clashes;
                            step = iteration;
                        }
                    }
                    System.out.println("Shift = " + field[4].getText() + "\tMin clashes = " + min + "\tat step " + step);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input for iterations or shift.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Courses not loaded.");
            }
            running = false; // Set flag back to false when algorithm finishes
        } else {
            JOptionPane.showMessageDialog(this, "Algorithm is already running.");
        }
    }

    private void continueAlgorithm() {
        try {
            courses.iterate(Integer.parseInt(field[4].getText()));
            draw();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input for shift.");
        }
    }

    public static void main(String[] args) {
        new TimeTable();
    }
}
