package dnnProcessingUnit;

import java.util.ArrayList;

public class ClientDataManager {
	
	public ClientDataManager(DnnController dnnController){
		mSectionsADT = new ArrayList<>();
		mDnnController = dnnController;
		InitiateSectionADT();
	}
	private DnnController mDnnController;
	private ArrayList<SectionFriends> mSectionsADT;
	
	public static enum SectionStatus{
		SentToTraining, Trained, NotSent
	}
	private void InitiateSectionADT(){
		for(int index =0; index< mDnnController.getModel().getNumberOfTrainingObjects()/mDnnController.getModel().getSectionLength(); index++){
			mSectionsADT.add(new SectionFriends());
		}
	}
	
	public class SectionFriends{
		private final String mTrainingClient;
		private final SectionStatus mStatus;
		private final float mSuccessRate;
		
		public SectionFriends(String trainingClient, SectionStatus status, int successRate){
			mTrainingClient = trainingClient;
			mStatus = status;
			mSuccessRate = successRate;
		}
		public SectionFriends(){
			mTrainingClient = "";
			mStatus = SectionStatus.NotSent;
			mSuccessRate = 0;
		}
		
		public String TrainingClient(){
			return mTrainingClient;
		}
		public SectionStatus Status(){
			return mStatus;
		}
		public float SuccessRate(){
			return mSuccessRate;
		}
	}
	
	
	
	
}
