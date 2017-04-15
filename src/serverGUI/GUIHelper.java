package serverGUI;

public class GUIHelper {
	public GUIHelper(){
		
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

}
