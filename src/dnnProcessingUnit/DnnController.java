package dnnProcessingUnit;

import dnnUtil.dnnModel.DnnModelParameters;
import dnnUtil.dnnStatistics.DnnStatistics;

import java.util.ArrayList;
import java.util.List;

import dnnUtil.dnnModel.DnnModel;
import messagesSwitch.MessagesSwitch;

public class DnnController extends Thread{

	private boolean mRunning;
	private DnnModel mModel;
	private DnnModelParameters mModelParameters;
	private MessagesSwitch mMessageSwitch;
	private List<DnnStatistics> mControllerStatistics;
	private ModelUpdater mModelUpdater;
	
	public DnnController(MessagesSwitch messageSwitch){
		setModel(new DnnModel(mModelParameters));
		mMessageSwitch = messageSwitch;
		mControllerStatistics = new ArrayList<>();
		setModelUpdater(new ModelUpdater());

	}
	
	public void runDnnController(){
		mMessageSwitch.setController(this);
		while(mRunning){
			//TODO implement DNN controller loop
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

	public void assignClient(){
		
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
}
