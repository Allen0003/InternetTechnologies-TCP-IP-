package mail.mailGui;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

//MailManager.java
public class MMGui {

	static boolean debug = MailManager.debug;
	MailManager mm;

	ActionListener listAction = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			String cmd = ae.getActionCommand();
			if (debug) {
				System.out.println("MMGui.listAction:" + cmd);
			}
			StringTokenizer st = new StringTokenizer(cmd);
			readMail(Integer.parseInt(st.nextToken()));
		}
	};

	ActionListener buttonAction = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			String cmd = ae.getActionCommand();
			if (debug) {
				System.out.println("MMGui.listAction:" + cmd);
			}
			if ("quit".equalsIgnoreCase(cmd)) {
				System.exit(0);
			} else if ("get".equalsIgnoreCase(cmd)) {
				mm.getmail();
				list();
			} else if ("send".equalsIgnoreCase(cmd)) {
				sendMail();
			}
		}
	};

	TextArea message;
	List list;
	Frame f;

	// constructor
	public MMGui() {
		f = new Frame();
		f.setSize(600, 480);

		GridBagLayout layout = new GridBagLayout();
		f.setLayout(layout);
//
		Panel p = new Panel();
//
//		// Get
		Button b = new Button("Get");
		b.addActionListener(buttonAction);
		p.add(b);

		// Send
		b = new Button("Send");
		b.addActionListener(buttonAction);
		p.add(b);

		// Quit
		b = new Button("Quit");
		b.addActionListener(buttonAction);
		p.add(b);
//
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = GridBagConstraints.REMAINDER;
//
		layout.setConstraints(p, c);
		f.add(p);
//
//		// show mail list
		list = new List(5);
		list.addActionListener(listAction);
		layout.setConstraints(list, c);
		f.add(list);
//
//		// show mail context
		message = new TextArea(18, 70);
		message.setEditable(false);
		layout.setConstraints(message, c);
		f.add(message);
		f.setVisible(true);


		mm = new MailManager();
		list();

	}

	void list() {
		String[] mailList = mm.list();
		list.removeAll();
		for (int i = 0; i < mailList.length; i++) {
			list.add(mailList[i]);
		}
	}

	void readMail(int i) {
		message.setText(mm.readMail(i));
	}

	void sendMail() {
		new Editor();
	}

	public static void main(String[] arg) {
		new MMGui();
	}

}
