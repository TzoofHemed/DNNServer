package DNNProcessingUint;

import DNNModel.DNNModelConstants;
import DNNModel.DnnModel;

public class DNNController extends Thread{

	private boolean mRunning;
	private DnnModel mModel;
	private DNNModelConstants mModelConstatns;
	
	public DNNController(){
		setmModel(new DnnModel(mModelConstatns));
		
	}
	
	public void runDnnController(){
		while(mRunning){
			
		}
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
	
	
	public DNNModelConstants getmModelConstatns() {
		return mModelConstatns;
	}
	public void setmModelConstatns(DNNModelConstants mModelConstatns) {
		this.mModelConstatns = mModelConstatns;
	}
	public DnnModel getmModel() {
		return mModel;
	}
	public void setmModel(DnnModel mModel) {
		this.mModel = mModel;
	}

}
