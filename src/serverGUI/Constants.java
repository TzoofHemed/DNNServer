package serverGUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public final static int PANEL_W = 700;
	public final static int PANEL_H = 700;
	public final static String PANEL_TITLE = "DNN SERVER";
	
	public final static int CMD_FIELD_W = 500;
	public final static int CMD_FIELD_H = 45;
	
	public final static int MAIN_LOG_H = 400;
	public final static int MAIN_LOG_W = PANEL_W;
	
	public final static Color LOG_SCREEN_FONT_COLOR = Color.getHSBColor((float) (190.0/360.0),(float) 1, (float)1);
	public final static Color LOG_SCREEN_BACKGROUND_COLOR = Color.BLACK;
	
	public final static Color CMD_FONT_COLOR = Color.PINK;
	public final static Color CMD_BACKGROUND_COLOR = Color.BLACK;

	public final static String delimiter = ":";
	public final static String printStatistics = "p_stats";
	public final static String saveStatistics = "s_stats";
	public final static String printTrainerStatistics = "pt_stats";
	public final static String resetModel = "r_model";
	public final static String trainersCount = "t_cnt";
	public final static String notACmd = "cmd not valid";
	public final static String help = "help";
	public final static String ip = "ip";
	public final static String start = "start";
	public final static String stop = "stop";
	public final static String quit = "quit";
	public final static String setDataSize = "s_size";
	public final static String setDataSet = "s_set";
	
	public final static List<String> getAvailableCmds(){
		List<String> availableCmd = new ArrayList<>(Arrays.asList(printStatistics,saveStatistics,printTrainerStatistics,resetModel,trainersCount,help,ip,start,setDataSet,setDataSize,stop,quit));
		return availableCmd;
	}
	
	public final static String getHelp(){
		String helpMenu ="";
		helpMenu += printStatistics +"\t\t"+ " - print all training statistics for all trainers" +"\n";
		helpMenu += printTrainerStatistics + delimiter + "<clientName>" + "\t\t"+ " - print all training statistics for a specific trainer"+"\n";
		helpMenu += saveStatistics + "\t\t"+ " - save all training statistics to file"+"\n";
		helpMenu += resetModel + "\t\t"+ " - resets the model"+"\n";
		helpMenu += trainersCount + "\t\t"+ " - print all trainers names"+"\n";
		helpMenu += setDataSize + "\t\t"+ " - set the data size - either 100 or 1000"+"\n";
		helpMenu += setDataSet + "\t\t"+ " - set the data type: mnist or cifar10"+"\n";
		helpMenu += ip + "\t\t"+ " - show the server ip"+"\n";
		helpMenu += start + "\t\t"+ " - start the server"+"\n";
		helpMenu += stop + "\t\t"+ " - stop the server"+"\n";
		helpMenu += quit + "\t\t"+ " - quit DNN server app"+"\n";
		helpMenu += help + "\t\t"+ " - this message"+"\n";
		return helpMenu;
	}
	
}
