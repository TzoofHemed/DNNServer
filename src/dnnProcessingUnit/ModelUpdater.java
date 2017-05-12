package dnnProcessingUnit;

import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	private LinkedBlockingQueue<DnnWeightsData> mWeightsToUpdate;
	private int numOfRequeiredWeights;
	private DnnController mDnnController; 
	
	public ModelUpdater(DnnController dnnCotroller){
		mWeightsToUpdate = new LinkedBlockingQueue<>();
		numOfRequeiredWeights = 10;		//TODO change to something relative to size of batch/ epoch
		mDnnController = dnnCotroller;
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
			
			DnnWeightsData updatedWeights= mergeWeights();
			mDnnController.setDnnWeightsData(updatedWeights);
			oldModel.setWeightsData(updatedWeights);
			
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
