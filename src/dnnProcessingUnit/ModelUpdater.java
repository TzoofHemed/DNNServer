package dnnProcessingUnit;

import java.util.ArrayList;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	private ArrayList<DnnModelDelta> mDeltasToUpdate;

	public ModelUpdater(){
		mDeltasToUpdate = new ArrayList<>();
	}
	
	public void deltaChecker(DnnModelDelta delta){
	//TODO implement	
		if(true){		//add better rule for delta checking
			mDeltasToUpdate.add(delta);
		}
	}

	
	public DnnModel rewriteModel(DnnModel oldModel){
		DnnModel newModel = oldModel;
		for (DnnModelDelta modelDelta : mDeltasToUpdate) {
			newModel.updateModel(modelDelta);
		}
		return newModel;
	}

}
