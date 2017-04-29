package dnnProcessingUnit;

import java.util.concurrent.LinkedBlockingQueue;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	private LinkedBlockingQueue<DnnModelDelta> mDeltasToUpdate;

	public ModelUpdater(){
		mDeltasToUpdate = new LinkedBlockingQueue<>();
	}
	
	/*
	 * also adds the given delta to the toUpdate queue
	 */
	public void deltaChecker(DnnModelDelta delta){	
		if(true){		//add better rule for delta checking...
			mDeltasToUpdate.add(delta);
		}
	}

	
	public DnnServerModel rewriteModel(DnnServerModel oldModel){
		DnnServerModel newModel = oldModel;
		while (!mDeltasToUpdate.isEmpty()) {
			newModel.updateModel(mDeltasToUpdate.remove());
		}
		return newModel;
	}

}
