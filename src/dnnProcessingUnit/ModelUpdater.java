package dnnProcessingUnit;

import java.util.concurrent.LinkedBlockingQueue;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	private LinkedBlockingQueue<DnnWeightsData> mWeightsToUpdate;
//	private LinkedBlockingQueue<DnnDeltaData> mDeltaToUpdate;
	private int numOfRequeiredWeights;
	private DnnController mDnnController; 

	public ModelUpdater(DnnController dnnCotroller){
		mWeightsToUpdate = new LinkedBlockingQueue<>();
		numOfRequeiredWeights = 10;		//TODO change to something relative to size of batch/ epoch
		mDnnController = dnnCotroller;
	}	
	
	synchronized public void addDelta(DnnDeltaData delta){
		mDnnController.getModel().setDeltaData(delta);
		mDnnController.setModelVersion(mDnnController.getModel().getModelVersion());
//		mDnnController.getModel().loadTrainingData(
//				"/home/tzoof/workspace/dnnP/DNNServer/src/validate-images.idx3-ubyte.10000.01",
//				"/home/tzoof/workspace/dnnP/DNNServer/src/validate-labels.idx1-ubyte.10000.01", "mnist");
//		Float accuracy = mDnnController.getModel().validateModel().getAccuracy();
//		System.out.println("accuracy: "+ accuracy + "\n");
	}
	
	/*
	 * also adds the given delta to the toUpdate queue
	 */
	public void weightChecker(DnnWeightsData weight){	
		if(true){		//TODO add better rule for delta checking...
			mWeightsToUpdate.add(weight);
		}
		if(mWeightsToUpdate.size() >= numOfRequeiredWeights){
			mDnnController.updateModel();
		}
	}

	public boolean rewriteModel(DnnModel oldModel){

		DnnWeightsData updatedWeights= mergeWeights();
		mDnnController.setDnnWeightsData(updatedWeights);
		oldModel.setWeightsData(updatedWeights);
		mDnnController.setModelVersion(oldModel.getModelVersion());
//		mDnnController.stepModelVersion();
		return true;

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
