package mail.mailGui;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Editor {
	Frame f;
	TextField subject;
	TextField to;
	TextArea message;
	MailManager mm = null;
	ActionListener editorAction = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			String cmd = ae.getActionCommand();
			if (MailManager.debug) {
				System.out.println("MMGui.Editon.editorAction:" + cmd);
			}
			if ("OK".equalsIgnoreCase(cmd)) {
				mm.sendmail(to.getText(), subject.getText(), message.getText());
			}
			f.dispose();
		}
	};

	public Editor() {
		f = new Frame();
		f.setSize(600, 400);
		subject = new TextField(50);
		to = new TextField(50);
		message = new TextArea(15, 70);
		f.setLayout(new FlowLayout());

		Panel p = new Panel();
		p.add(new Label("Subject"));
		p.add(subject);
		f.add(p);

		p = new Panel();
		p.add(new Label("To:"));
		p.add(to);
		f.add(p);

		Button b = new Button("OK");
		b.addActionListener(editorAction);
		f.add(b);

		b = new Button("CANCEL");
		b.addActionListener(editorAction);
		f.add(b);
		f.setVisible(true);
		mm = new MailManager();
		// TODO
		// f.show();
	}

}
