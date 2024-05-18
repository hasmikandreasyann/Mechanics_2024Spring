import java.io.*;
import java.util.StringTokenizer;

public class CourseArray {

    private Course[] elements;
    private int period;

    public CourseArray(int numOfCourses, int numOfSlots) {
        period = numOfSlots;
        elements = new Course[numOfCourses];
        for (int i = 1; i < elements.length; i++)
            elements[i] = new Course();
    }

    public void readClashes(String filename) {
        try {
            BufferedReader file = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = file.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line);
                int count = tokenizer.countTokens();
                int[] index = new int[count];
                int i = 0;
                while (tokenizer.hasMoreTokens()) {
                    index[i] = Integer.parseInt(tokenizer.nextToken());
                    i++;
                }

                for (i = 0; i < index.length; i++) {
                    for (int j = 0; j < index.length; j++) {
                        if (j != i) {
                            Course courseI = elements[index[i]];
                            Course courseJ = elements[index[j]];
                            if (!courseI.clashesWith.contains(courseJ)) {
                                courseI.addClash(courseJ);
                            }
                        }
                    }
                }
            }
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int length() {
        return elements.length;
    }

    public int status(int index) {
        return elements[index].clashSize();
    }

    public int slot(int index) {
        return elements[index].mySlot;
    }

    public void setSlot(int index, int newSlot) {
        elements[index].mySlot = newSlot;
    }

    public int maxClashSize(int index) {
        return elements[index] == null || elements[index].clashesWith.isEmpty() ? 0 : elements[index].clashesWith.size();
    }

    public int clashesLeft() {
        int result = 0;
        for (int i = 1; i < elements.length; i++)
            result += elements[i].clashSize();

        return result;
    }

    public void iterate(int shifts) {
        for (int index = 1; index < elements.length; index++) {
            elements[index].setForce();
            for (int move = 1; move <= shifts && elements[index].force != 0; move++) {
                elements[index].setForce();
                elements[index].shift(period);
            }
        }
    }

    public void printResult() {
        for (int i = 1; i < elements.length; i++)
            System.out.println(i + "\t" + elements[i].mySlot);
    }

    // New method: getTimeSlot
    public int[] getTimeSlot(int index) {
        int[] timeSlot = new int[elements.length];
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].mySlot == index) {
                timeSlot[i] = 1;
            } else {
                timeSlot[i] = -1;
            }
        }
        return timeSlot;
    }
    public void printClashes() {
        for (int i = 1; i < elements.length; i++) {
            for (int j = 0; j < elements[i].clashesWith.size(); j++) {
                Course clashCourse = elements[i].clashesWith.get(j);
                if (elements[i].mySlot == clashCourse.mySlot) {
                    System.out.println("Clash between course " + i + " and course "  + " at slot " + elements[i].mySlot);
                }
            }
        }
    }

    // Method to convert CourseArray to an array of course data
    public int[] toArray() {
        int[] courseData = new int[length()]; // Assuming length() returns the number of courses
        for (int i = 1; i < length(); i++) { // Assuming courses start from index 1
            courseData[i] = slot(i); // Assuming slot() returns the time slot of the course at index i
        }
        return courseData;
    }

    public void setTimeslots(int[] timeslots) {
        for (int i = 1; i < elements.length; i++) {
            setSlot(i, timeslots[i]);
        }
    }

}
