import java.io.IOException;
import javax.swing.JFrame;

public class RunQuiz {

	public static void main(String[] args) throws IOException {
		Question q = new Question();
		q.setSize(350, 370);
		q.setTitle("Quiz Game");
		q.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		q.setVisible(true);
	}		
}
