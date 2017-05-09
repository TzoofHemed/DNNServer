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
	
	public void rewriteModel(DnnModel oldModel){
		oldModel.setWeightsData(mergeWeights());
	}
	private DnnWeightsData mergeWeights(){
		DnnWeightsData weightData = new DnnWeightsData();
		float oneOverScaler = 0;
		for (int WDIndex = 0; WDIndex < mWeightsToUpdate.size(); WDIndex++) { 
			 weightData.addWeights(mWeightsToUpdate.remove());
			 oneOverScaler++;
		}
		if(oneOverScaler > 0){
			weightData.scaleWeights(1/oneOverScaler);
		}
		return weightData;
	}
}
