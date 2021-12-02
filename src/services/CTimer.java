package services;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class CTimer extends Object implements Serializable {

	private static final long serialVersionUID = 1861304556437295528L;

	int delay = 1000; // milliseconds
	int timerResetCount;
	ActionListener taskPerformer = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.print("timer: " + timerResetCount);
			timerResetCount += 1;
		}

	};
}
