import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TimeTable extends JFrame implements ActionListener {

    private JPanel screen = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            renderCourses(g);
        }
    };
    private JPanel tools = new JPanel();
    private JButton tool[];
    private JTextField field[];
    private CourseArray courses;
    private Color CRScolor[] = {Color.RED, Color.GREEN, Color.BLACK};
    private boolean running = false;
    private boolean canContinue = false; // Flag to indicate if the algorithm can be continued
    private int min; 

    public TimeTable() {
        super("Dynamic Time Table");
        setSize(500, 800);
        setLayout(new BorderLayout());

        screen.setPreferredSize(new Dimension(400, 800));
        add(screen, BorderLayout.CENTER);

        setTools();
        add(tools, BorderLayout.EAST);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        field[2].setText("rye-s-93.stu");
        field[3].setText("1");
    }

    public void renderCourses(Graphics g) {
        if (courses == null) return;
        int width = Integer.parseInt(field[0].getText()) * 10;
        for (int courseIndex = 1; courseIndex < courses.length(); courseIndex++) {
            g.setColor(CRScolor[courses.status(courseIndex) > 0 ? 0 : 1]);
            g.drawLine(0, courseIndex * 2, width, courseIndex * 2); // Adjust spacing between lines
            g.setColor(CRScolor[CRScolor.length - 1]);
            g.drawLine(10 * courses.slot(courseIndex), courseIndex * 2, 10 * courses.slot(courseIndex) + 10, courseIndex * 2);
        }
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
                    screen.repaint(); // Repaint the screen
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input for slots or courses.");
                }
                break;
            case 1:
                // Start algorithm
                startAlgorithm();
                break;
            case 2:
                // Step
                stepAlgorithm();
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
            case 5:
                continueAlgorithm();
                break;
        }
    }

    private void startAlgorithm() {
        if (!running) {
            running = true;
            canContinue = true;
            min = Integer.MAX_VALUE;
            int step = 0;
            if (courses != null) {
                try {
                    for (int iteration = 1; iteration <= Integer.parseInt(field[3].getText()); iteration++) {
                        courses.iterate(Integer.parseInt(field[4].getText()));
                        screen.repaint(); // Repaint the screen
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

    private void stepAlgorithm() {
        if (courses != null) {
            try {
                courses.iterate(Integer.parseInt(field[4].getText()));
                screen.repaint(); // Repaint the screen
                canContinue = true; // Enable continue functionality
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input for shift.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Courses not loaded.");
        }
    }

    private void continueAlgorithm() {
        if (canContinue) {
            startAlgorithm(); // Continue from the current state
        } else {
            JOptionPane.showMessageDialog(this, "Algorithm cannot be continued at this point.");
        }
    }

    public static void main(String[] args) {
        new TimeTable();
    }
}
