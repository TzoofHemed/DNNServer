package dnnProcessingUnit;

import dnnUtil.dnnModel.*;

public class ModelUpdater {

	static {
		System.loadLibrary("");  		//TODO add library name!
	}
	public ModelUpdater(){
		
	}
	
	public void deltaChecker(DnnModelDelta delta){
	//TODO implement	
	}
	
	public DnnModel rewriteModel(DnnModel oldModel){
		DnnModel newModel = oldModel;
		
		//TODO implement
		return newModel;
	}
	
	private native void updateModel();
}
