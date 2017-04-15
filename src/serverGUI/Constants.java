package serverGUI;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public static int PANEL_W = 700;
	public static int PANEL_H = 700;
	public static String PANEL_TITLE = "DNN SERVER";
	
	public static int CMD_FIELD_W = 500;
	public static int CMD_FIELD_H = 45;
	
	public static int MAIN_LOG_H = 400;
	public static int MAIN_LOG_W = PANEL_W;
	
	public static Color LOG_SCREEN_FONT_COLOR = Color.getHSBColor((float) (190.0/360.0),(float) 1, (float)1);
	public static Color LOG_SCREEN_BACKGROUND_COLOR = Color.BLACK;
	
	public static Color CMD_FONT_COLOR = Color.PINK;
	public static Color CMD_BACKGROUND_COLOR = Color.BLACK;

	public static String delimiter = ":";
	public static String printStatistics = "p_stats";
	public static String saveStatistics = "s_stats";
	public static String printTrainerStatistics = "pt_stats";
	public static String resetModel = "r_model";
	public static String trainersCount = "t_cnt";
	public static String notACmd = "cmd not valid";
	
	public static List<String> getAvailableCmds(){
		List<String> availableCmd = new ArrayList<>(Arrays.asList(printStatistics,saveStatistics,printTrainerStatistics,resetModel,trainersCount));
		return availableCmd;
	}
	
	
}
