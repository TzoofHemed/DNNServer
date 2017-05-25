package dnnProcessingUnit;

import dnnUtil.dnnModel.DnnModelParameters;
import dnnUtil.dnnModel.DnnModel;
import dnnUtil.dnnModel.DnnTrainingData;
import dnnUtil.dnnModel.DnnTrainingDescriptor;
import dnnUtil.dnnModel.DnnWeightsData;
import dnnUtil.dnnStatistics.DnnStatistics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import dnnProcessingUnit.ClientDataManager.SectionStatus;
import messagesSwitch.MessagesSwitch;

public class DnnController extends Thread{

	private DnnModel mModel;
	private DnnModelParameters mModelParameters;
	private MessagesSwitch mMessageSwitch;
	private List<DnnStatistics> mControllerStatistics;
	private ModelUpdater mModelUpdater;
	private int mNextBeginningSection;
	private int mNextEndingSection;
	private ClientDataManager mClientDataManager;
	private int mSectionLength;
	private DnnWeightsData mDnnWeightsData;
	private int mModelVersion;
	private LinkedBlockingDeque<DnnTrainingData> mTrainingDataQueue;
	private int QueueSize;
	private String lastStatPath;
	private boolean mTrainingDone;

	public DnnController(MessagesSwitch messageSwitch){
		setModel(new DnnModel(mModelParameters));
		mMessageSwitch = messageSwitch;
		mControllerStatistics = new ArrayList<>();
		setModelUpdater(new ModelUpdater(this));
		mNextBeginningSection = 0;
		mSectionLength = 1000;
		mNextEndingSection = mSectionLength; 
		mClientDataManager = new ClientDataManager(this);
		mModelVersion =0;
		QueueSize = 5;
		mTrainingDataQueue = new LinkedBlockingDeque<>(QueueSize); 
		lastStatPath = "";
		mTrainingDone = false;
	}

	public void runDnnController(){
		mMessageSwitch.setController(this);
		mClientDataManager.Init();
		trainigDataQueueInit();
		//		while(mRunning){
		//			
		//			
		////			assignUnemployed();
		////			forwardOutputMessages();
		//
		//		}
	}
	public void saveServerStatistics(){
		
	}
	
	public void trainingDataQueueFiller(){
		try {
			mTrainingDataQueue.put(mModel.getTrainingData(getNextTrainingDescriptor()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void trainigDataQueueInit(){
		for(int i=0; i<QueueSize; i++ ){

			try {
				mTrainingDataQueue.put(mModel.getTrainingData(getNextTrainingDescriptor()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public DnnTrainingData getNextTrainingData() throws InterruptedException{

		return mTrainingDataQueue.take();
	}

	public void updateModel(){
		mModelUpdater.rewriteModel(this.mModel);
		setAllToOutOfDate();
		updateOutOfDateClients();
//		forwardOutputMessages();
	}	
	public void setDnnWeightsData(DnnWeightsData dnnWeightsData){
		mDnnWeightsData = dnnWeightsData;
	}
	public DnnWeightsData getDnnWeightsData(){
		return mDnnWeightsData;
	}

	private void setAllToOutOfDate(){
		mMessageSwitch.setAlltoOutOfDate();
	}

	public void updateSectionStatus(String clientName, SectionStatus status, float successRate){
		mClientDataManager.updateSection(clientName,status,successRate);
	}
	public void assignSection(String clientName){
		mClientDataManager.assignSection(clientName);
	}


//	private void forwardOutputMessages(){
//		mMessageSwitch.updateOutputMessages();
//	}

	//	private void assignUnemployed(){
	//		String unemployedName = mMessageSwitch.assignClient();
	//		if(unemployedName != ""){
	//			assignClient(unemployedName);
	//		}
	//	}

	@Override
	public void run() {
		super.run();

		runDnnController();

	}

	public void stopController(){
	}

	public int getSectionLength(){
		return mSectionLength;
	}
	public void setSectionLength(int sectionLength){
		mSectionLength = sectionLength;
	}

	public DnnModelParameters getmModelConstatns() {
		return mModelParameters;
	}
	public void setmModelConstatns(DnnModelParameters mModelConstatns) {
		this.mModelParameters = mModelConstatns;
	}
	public DnnModel getModel() {
		return mModel;
	}
	public void setModel(DnnModel mModel) {
		this.mModel = mModel;
	}

	//	private void assignClient(String clientName){
	//		DnnTrainingDescriptor nextPackage = getNextTrainingDescriptor();
	//		DnnTrainingData nextData = mModel.getTrainingData(nextPackage);
	//		mMessageSwitch.getClientManager().addMessageToClient(clientName, new DnnTrainingDataMessage(nextData));
	//		mClientDataManager.assignSection(clientName);
	//	}
	private void updateOutOfDateClients(){
		mMessageSwitch.updateOutOfDateClients();
	}

	public void addStatistics(DnnStatistics stats){
		mControllerStatistics.add(stats);
	}

	public ModelUpdater getModelUpdater() {
		return mModelUpdater;
	}

	public void setModelUpdater(ModelUpdater mModelUpdater) {
		this.mModelUpdater = mModelUpdater;
	}

	public DnnTrainingDescriptor getNextTrainingDescriptor(){
		DnnTrainingDescriptor descriptor = new DnnTrainingDescriptor(mNextBeginningSection,mNextEndingSection);
		if(mNextEndingSection < mModel.getNumberOfTrainingObjects()){
			mNextBeginningSection += mSectionLength;
			mNextEndingSection += mSectionLength;
			if(mNextEndingSection > mModel.getNumberOfTrainingObjects()){
				mTrainingDone = true;
			}
		}
		return descriptor;
	}
	public void trainingFinished(){
		
	}
	

	public void saveStatisticsToFile(){

		PrintWriter out = null;
		DateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
		Date dateObj = new Date();
		String fileName = "DnnStatistics_" + df.format(dateObj)+ ".txt";
		File outDir = new File("./dnnOut");
		if(!outDir.exists()){
			outDir.mkdir();
		}
		File statisticsFile = new File("dnnOut/"+fileName);
		try{
			out = new PrintWriter(statisticsFile ,"UTF-8");
			for (DnnStatistics dnnStatistics : mControllerStatistics) {
				out.print(dnnStatistics.getStatistics() + "\n ----------------------------------------------------- \n");
			}
			lastStatPath = "./dnnOut/"+fileName;
		}catch(IOException e){
			System.out.println(e.getMessage());
		}finally{
			if(out != null){
				out.close();
			}
		}
	}
	
	public void saveStatisticsToCSV(){
		
		PrintWriter out = null;
		DateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
		Date dateObj = new Date();
		String fileName = "DnnStatistics_" + df.format(dateObj)+ ".csv";
		File outDir = new File("./dnnOut");
		if(!outDir.exists()){
			outDir.mkdir();
		}
		File statisticsFile = new File("dnnOut/"+fileName);
		try{
			out = new PrintWriter(statisticsFile ,"UTF-8");
			if(!mControllerStatistics.isEmpty()){
				out.print(mControllerStatistics.get(0).getStatisticsCSVHeader());
				System.out.println("statistics header: " + mControllerStatistics.get(0).getStatisticsCSVHeader());
			}
			for (DnnStatistics dnnStatistics : mControllerStatistics) {
				out.print(dnnStatistics.getStatisticsInCSV());
			}
			lastStatPath = "./dnnOut/"+fileName;
		}catch(IOException e){
			System.out.println(e.getMessage());
		}finally{
			if(out != null){
				out.close();
			}
		}
	}

	public String getTrainingStatistics(){
		String statistics = "";
		for (DnnStatistics dnnStatistics : mControllerStatistics) {
			statistics += dnnStatistics.getStatistics() + "\n ----------------------------------------------------- \n";
		}

		return statistics;
	}

	public String getTrainerStatistics(String ClientName){
		String statistics = "";
		for (DnnStatistics dnnStatistics : mControllerStatistics) {
			if(dnnStatistics.getClientName().equals(ClientName)){
				statistics += dnnStatistics.getStatistics() + "\n ----------------------------------------------------- \n";
			}
		}		
		return statistics;
	}

	public void resetModel(){
		mModel = new DnnModel(mModelParameters);
	}

	public String getTrainerCount(){
		return mMessageSwitch.getClientCount();		
	}

	public int getModelVersion() {
		return mModelVersion;
	}

	public void stepModelVersion() {
		this.mModelVersion++;
	}
	public String getStatisticsPath(){
		return lastStatPath;
	}

	public boolean isTrainingDone() {
		return mTrainingDone;
	}
}
