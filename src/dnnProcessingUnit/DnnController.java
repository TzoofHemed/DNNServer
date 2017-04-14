package dnnProcessingUnit;

import dnnUtil.dnnModel.DnnModelParameters;
import dnnUtil.dnnModel.DnnTrainingDescriptor;
import dnnUtil.dnnModel.DnnTrainingPackage;
import dnnUtil.dnnStatistics.DnnStatistics;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dnnUtil.dnnMessage.DnnTrainingPackageMessage;
import dnnUtil.dnnModel.DnnModel;
import messagesSwitch.MessagesSwitch;

public class DnnController extends Thread{

	private boolean mRunning;
	private DnnModel mModel;
	private DnnModelParameters mModelParameters;
	private MessagesSwitch mMessageSwitch;
	private List<DnnStatistics> mControllerStatistics;
	private ModelUpdater mModelUpdater;
	private int mNextBeginningSection;
	private int mNextEndingSection;
	
	public DnnController(MessagesSwitch messageSwitch){
		setModel(new DnnModel(mModelParameters));
		mMessageSwitch = messageSwitch;
		mControllerStatistics = new ArrayList<>();
		setModelUpdater(new ModelUpdater());

	}
	
	public void runDnnController(){
		mMessageSwitch.setController(this);
		while(mRunning){

			mModelUpdater.rewriteModel(this.mModel);
			forwardOutputMessages();
						
		}
	}
	
	 private void forwardOutputMessages(){
		 mMessageSwitch.updateOutputMessages();
	 }
	
	
    @Override
    public void run() {
        super.run();

        mRunning = true;
        runDnnController();

    }
    
    public void stopController(){
    	mRunning =false;
    }
	
	
	public DnnModelParameters getmModelConstatns() {
		return mModelParameters;
	}
	public void setmModelConstatns(DnnModelParameters mModelConstatns) {
		this.mModelParameters = mModelConstatns;
	}
	public DnnModel getModel() {
		return mModel;
	}
	public void setModel(DnnModel mModel) {
		this.mModel = mModel;
	}

	public void assignClient(String clientName){
		DnnTrainingDescriptor nextPackage = getNextTrainingDescriptor();
		mMessageSwitch.getClientManager().addClient(clientName, new DnnTrainingPackageMessage(new DnnTrainingPackage(mModel, nextPackage)) );
			}
	
	public void addStatistics(DnnStatistics stats){
		mControllerStatistics.add(stats);
	}

	public ModelUpdater getModelUpdater() {
		return mModelUpdater;
	}

	public void setModelUpdater(ModelUpdater mModelUpdater) {
		this.mModelUpdater = mModelUpdater;
	}
	
	public DnnTrainingDescriptor getNextTrainingDescriptor(){
		DnnTrainingDescriptor descriptor = new DnnTrainingDescriptor(mNextBeginningSection,mNextEndingSection);
		return descriptor;
	}

	public void saveStatisticsToFile(){
		PrintWriter out = null;
		DateFormat df = new SimpleDateFormat("HH:mm:ss_dd/MM/yy");
		Date dateObj = new Date();
		String fileName = "DnnStatistics" + df.format(dateObj)+ ".txt";
		try{
			out = new PrintWriter(fileName ,"UTF-8");
			for (DnnStatistics dnnStatistics : mControllerStatistics) {
				out.print(dnnStatistics.getStatistics() + "\n ----------------------------------------------------- \n");
			}
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}finally{
			if(out != null){
				out.close();
			}
		}
	}

}
