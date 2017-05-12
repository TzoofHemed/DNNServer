package dnnProcessingUnit;

import java.util.concurrent.LinkedBlockingQueue;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	private LinkedBlockingQueue<DnnWeightsData> mWeightsToUpdate;
	private int numOfRequeiredWeights;
	
	public ModelUpdater(){
		mWeightsToUpdate = new LinkedBlockingQueue<>();
		numOfRequeiredWeights = 10;		//TODO change to something relative to size of batch/ epoch
	}	
	/*
	 * also adds the given delta to the toUpdate queue
	 */
	public void weightChecker(DnnWeightsData weight){	
		if(true){		//TODO add better rule for delta checking...
			mWeightsToUpdate.add(weight);
		}
	}
	
	public boolean rewriteModel(DnnModel oldModel){
		if(mWeightsToUpdate.size() >= numOfRequeiredWeights){
			oldModel.setWeightsData(mergeWeights());
			return true;
		}
		return false;
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
