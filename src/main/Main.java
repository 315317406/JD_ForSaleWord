package main;

import util.PBE;
import view.MainApplication;
import view.UI;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName(); 
    	try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
    	String pc = PBE.readLocalPcode();
    	if(pc!=null) {
    		if(PBE.checkPollCode(pc)) {
//				UI.mainStage = new Stage();
				MainApplication.main(args);
    		}else {
    			UI.registerFrame.setVisible(true);
    		}
    	}else {
    		UI.registerFrame.setVisible(true);
    	}
	}
}
