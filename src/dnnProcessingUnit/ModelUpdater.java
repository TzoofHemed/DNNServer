package dnnProcessingUnit;

import java.util.concurrent.LinkedBlockingQueue;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	private LinkedBlockingQueue<DnnWeightsData> mWeightsToUpdate;

	public ModelUpdater(){
		mWeightsToUpdate = new LinkedBlockingQueue<>();
	}	
	/*
	 * also adds the given delta to the toUpdate queue
	 */
	public void weightChecker(DnnWeightsData weight){	
		if(true){		//TODO add better rule for delta checking...
			mWeightsToUpdate.add(weight);
		}
	}
	
	public void mergeWeights(){
		for (int WDIndex = 0; WDIndex < mWeightsToUpdate.size(); WDIndex++) {
			DnnWeightsData weightData = mWeightsToUpdate.remove();
			
			
		}
	}
	
	public DnnModel rewriteModel(DnnModel oldModel){
		DnnModel newModel = oldModel;
		while (!mWeightsToUpdate.isEmpty()) {
			newModel.setWeightsData(mWeightsToUpdate.remove());
		}
		return newModel;
	}

}
