import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class Question extends JFrame implements ActionListener {

	private static final int NUM_ANSWERS = 4;
	private static final int[] ANSWERS = {0, 1, 2, 3};
	private JButton next;
	private JLabel question, score;
	private JButton[] answers;
	private JFileChooser fileChooser;
	private File selectedFile;
	private JPanel buttons, header;;
	private int curLine = 0;
	private int curScore = 0;
	private int curQuestion = 0;

	public Question() throws IOException {
		setLayout(new GridLayout(3, 1));
		// Create question and score layout and add to header panel
		score = new JLabel("", JLabel.CENTER);
		question = new JLabel("", JLabel.CENTER);
		header = new JPanel(new GridLayout(2, 1));
		header.add(score);
		header.add(question);
		// Create next button 
		next = new JButton("Next");
		next.addActionListener(this);
		// Create 4 answer buttons
		buttons = new JPanel(new GridLayout(4, 1));	
		answers = new JButton[4];
		for (int i = 0; i < NUM_ANSWERS; i++) {
			answers[i] = setupButton(buttons);
		}
		// Add elements to frame and choose file
		add(header);
		add(buttons);
		add(next);
		readFile();
	}

	private JButton setupButton(Container c) {
		JButton b = new JButton();
		b.setFont(new Font("Serif", Font.ITALIC, 18));
		c.add(b);
		b.addActionListener(this);
		return b;
	}

	public void readFile() throws IOException {
		// User chooses which file to read from
		fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			selectedFile = fileChooser.getSelectedFile();
			BufferedReader in = new BufferedReader(new FileReader(selectedFile));
			try {
				// Read in and check for questions
				String line = in.readLine();
				next.setEnabled(false);
				if (line != null) {
					switch (line) {
					case "What is 2 + 2?":
					case "What is 2 * 2?":
					case "What is 20 / 5?":
					case "What color is the sky?":
						displayNextQuestion();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			in.close();
		}
	}

	private void displayQuestion(BufferedReader in, int startLine) throws IOException{
		// First line read is question, next 4 are answers
		question.setText(in.readLine());
	    for (int i = 0; i < NUM_ANSWERS; i++) {
	        answers[i].setText(in.readLine());
	    }
	    // Increment by 5 to get to the next question
	    curLine += 5;
	}
	
	private void displayNextQuestion() throws IOException {
		// Reads in the next 5 lines depending on what the current line is
	    BufferedReader in = new BufferedReader(new FileReader(selectedFile));
	    for (int i = 0; i < curLine; i++) {
	        in.readLine();
	    }
	    // Calls method to do this
	    displayQuestion(in, curLine);
	}

	public void actionPerformed(ActionEvent e) {
	    int questionNum = 0;
	    for (int i = 0; i < NUM_ANSWERS; i++) {
	        if (e.getSource() == answers[i]) {
	            if (i == ANSWERS[questionNum]) {
	            	// Set correct answer to green and increment score
	                answers[i].setBackground(Color.GREEN);
	                curScore++;
	            } else {
	            	// Set correct answer to green and incorrect to red
	                answers[i].setBackground(Color.RED);
	                answers[ANSWERS[questionNum]].setBackground(Color.GREEN);
	            }
	            curQuestion++;
	            // Display the current score after every question and change state of buttons
	            if (curLine >= 20) {
	            	// If quiz is finished
	    	    	question.setText("Quiz finished!");
	    	    	next.setEnabled(false);
	    	    	for (JButton answer : answers) {
	                    answer.setEnabled(false);
	                }
	    	    	score.setText(curScore + "/" + curQuestion);
	    	    } else {
	    	    	// If quiz is not finished
	    	    	score.setText(curScore + "/" + curQuestion);
	    	    	next.setEnabled(true);
	    	    	for (JButton answer : answers) {
	    	    		answer.setEnabled(false);
	    	    	}
	    	    }
	            questionNum++;
	        }
	    }
	    if (e.getSource() == next) {
	        try {
	        	// Move to next question and change state of buttons
	            displayNextQuestion();
	            next.setEnabled(false);
	            for (JButton answer : answers) {
	                answer.setEnabled(true);
	                answer.setBackground(null);
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
}
