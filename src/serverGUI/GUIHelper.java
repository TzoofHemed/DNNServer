package serverGUI;

import javax.swing.JOptionPane;

public class GUIHelper {
	private MainScreen mGUI;
	public GUIHelper(MainScreen gui){
		mGUI = gui;
	}

	public String cmdInputHandler( String cmd){
		String hCmd = cmd;
		hCmd = hCmd.replaceAll("\\s+","");
		hCmd = hCmd.toLowerCase();
		if(hCmd.equals("")){
			return Constants.notACmd;
		}
		String[] splitCmd = hCmd.split(Constants.delimiter);
		hCmd = splitCmd[0];
		if(!Constants.getAvailableCmds().contains(hCmd)){
			return Constants.notACmd;
		}
		if(splitCmd.length > 1){
			hCmd += Constants.delimiter + splitCmd[1];
		}
		return hCmd;
	}

	public void cmdPrompt(String[] cmd){
		mGUI.getMainLog().append("\n>>"+ String.join(Constants.delimiter, cmd)+ "\n");
		switch (cmd[0]){
		case Constants.printStatistics:
			mGUI.getMainLog().append(mGUI.getController().getTrainingStatistics()+ "\n");
			break;
		case Constants.printTrainerStatistics:
			mGUI.getMainLog().append(mGUI.getController().getTrainerStatistics(cmd[1]) + "\n");
			break;
		case Constants.saveStatistics:
			mGUI.getController().saveStatisticsToFile();
			mGUI.getMainLog().append("Statistics were saved\n");
			break;
		case Constants.resetModel:
			mGUI.getController().resetModel();
			mGUI.getMainLog().append("Model is reset\n");
			break;
		case Constants.trainersCount:
			mGUI.getMainLog().append( mGUI.getController().getTrainerCount() + "\n");
			break;
		case Constants.help:
			JOptionPane.showMessageDialog(mGUI, Constants.getHelp(),"HELP!",JOptionPane.PLAIN_MESSAGE);
			break;
		case Constants.ip:
			mGUI.getIp();
			break;
		case Constants.stop:
			mGUI.stopServer();
			mGUI.getMainLog().append("server was stopped \n");
			break;
		case Constants.start:
			mGUI.getMainLog().append("server is starting, please wait... \n");
			mGUI.startServer();
			mGUI.getMainLog().append("server was started \n");
			break;
		case Constants.quit:
			mGUI.stopServer();
			mGUI.getMainLog().append("server was stopped, quiting... \n");
			mGUI.CloseGUI();
			break;
		default:
			break;
		}
	}

}
