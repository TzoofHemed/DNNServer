package dnnProcessingUnit;

import java.util.ArrayList;

public class ClientDataManager {
	
	public ClientDataManager(DnnController dnnController){
		mSectionsADT = new ArrayList<>();
		mTestSectionsADT = new ArrayList<>();
		mDnnController = dnnController;
		nextSectionIndex = 0;
		nextTestSectionIndex = 0;
	}
	private DnnController mDnnController;
	private ArrayList<SectionFriends> mSectionsADT;
	private int nextSectionIndex;
	private ArrayList<SectionFriends> mTestSectionsADT;
	private int nextTestSectionIndex;
	
	public static enum SectionStatus{
		SentToTraining, Trained, NotSent
	}
	
	public void Init(){
		InitiateSectionADT();
//		InitiateTestSectionADT();		//TODO uncomment once NumberOfTestingObjects is available 
	}
		
	
	// training
	private void InitiateSectionADT(){
		for(int index =0; index< mDnnController.getModel().getNumberOfTrainingObjects()/mDnnController.getSectionLength(); index++){
			mSectionsADT.add(new SectionFriends(index));
		}
	}
	public void assignSection(String clientName){
		
		mSectionsADT.get(nextSectionIndex).setTrainingClient(clientName);
		mSectionsADT.get(nextSectionIndex).setStatus(SectionStatus.SentToTraining);
		nextSectionIndex++;
	}
	
	public void updateSection(String clientName,SectionStatus status, float successRate) {
		int tmpIndex = -1;
		for(SectionFriends section : mSectionsADT){
			if(section.mTrainingClient == clientName){
				tmpIndex = section.SectionIndex();
			}
		}
		if(tmpIndex >= 0){
			mSectionsADT.get(tmpIndex).setStatus(status);
			mSectionsADT.get(tmpIndex).setTrainingClient(clientName);
			mSectionsADT.get(tmpIndex).setSuccessRate(successRate);
		}

	}
	
	//test
	private void InitiateTestSectionADT(){
		for(int index =0; index< mDnnController.getModel().getNumberOfTestingObjects()/mDnnController.getSectionLength(); index++){
			mTestSectionsADT.add(new SectionFriends(index));
		}
	}
	public void assignTestSection(String clientName){
		
		mTestSectionsADT.get(nextTestSectionIndex).setTrainingClient(clientName);
		mTestSectionsADT.get(nextTestSectionIndex).setStatus(SectionStatus.SentToTraining);
		mTestSectionsADT.get(nextTestSectionIndex).setTimeStamp();
		nextTestSectionIndex++;
	}
	
	public void updateTestSection(String clientName,SectionStatus status, float successRate) {
		int tmpIndex = -1;
		for(SectionFriends section : mTestSectionsADT){
			if(section.mTrainingClient == clientName){
				tmpIndex = section.SectionIndex();
			}
		}
		if(tmpIndex >= 0){
			mTestSectionsADT.get(tmpIndex).setStatus(status);
			mTestSectionsADT.get(tmpIndex).setTrainingClient(clientName);
			mTestSectionsADT.get(tmpIndex).setSuccessRate(successRate);
		}

	}
	
	
	public class SectionFriends{
		private  String mTrainingClient;
		private  SectionStatus mStatus;
		private  float mSuccessRate;
		private  int mSectionIndex;
		private  long timeStamp;
		
		public SectionFriends(int sectionIndex, String trainingClient, SectionStatus status, float successRate){
			mTrainingClient = trainingClient;
			mStatus = status;
			mSuccessRate = successRate;
			mSectionIndex = sectionIndex;
			timeStamp = System.currentTimeMillis();
		}
		public SectionFriends(int sectionIndex){
			mTrainingClient = "";
			mStatus = SectionStatus.NotSent;
			mSuccessRate = 0;
			mSectionIndex =	sectionIndex;
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
		public int SectionIndex(){
			return mSectionIndex;
		}
		public void setTrainingClient(String clientName){
			mTrainingClient = clientName;
		}
		public void setStatus(SectionStatus status){
			mStatus = status;
		}
		public void setSuccessRate(float successRate){
			mSuccessRate = successRate;
		}
		public long getTimeStamp(){
			return timeStamp;
		}
		public void setTimeStamp(){
			timeStamp = System.currentTimeMillis();
		}
	}
	
	
	
	
}
