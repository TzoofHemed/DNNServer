package dnnProcessingUnit;

import dnnUtil.dnnModel.DnnModelParameters;
import dnnUtil.dnnModel.DnnModel;
import dnnUtil.dnnModel.DnnModelDescriptor;
import dnnUtil.dnnModel.DnnTrainingDescriptor;
import dnnUtil.dnnMessage.DnnMessage;
import dnnUtil.dnnMessage.DnnTrainMessage;
import dnnUtil.dnnMessage.DnnValidationMessage;
import dnnUtil.dnnModel.DnnBundle;
import dnnUtil.dnnModel.DnnIndex;
import dnnUtil.dnnModel.DnnWeightsData;
import dnnUtil.dnnStatistics.DnnStatistics;
import dnnUtil.dnnStatistics.DnnValidationResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import dnnProcessingUnit.ClientDataManager.SectionStatus;
import messagesSwitch.MessagesSwitch;

public class DnnController extends Thread{

	private DnnModel mModel;
	private DnnModelParameters mModelParameters;
	private MessagesSwitch mMessageSwitch;
	private List<DnnStatistics> mControllerStatistics;
	private List<DnnValidationResult> mValidationResults;
	private ModelUpdater mModelUpdater;
	private int mNextBeginningSection;
	private int mNextEndingSection;
	private ClientDataManager mClientDataManager;
	private Integer mDataSize;
	private String mDataSet;
	private DnnWeightsData mDnnWeightsData;
	private int mModelVersion;
	//	private LinkedBlockingDeque<DnnTrainingData> mTrainingDataQueue;
	//	private int QueueSize;
	private String lastStatPath;
	private boolean mTrainingDone;
	private String mDataType;
	private int mNextTrainingIndex;
	private int mUpdateInterval;
	private int mValidateInterval;
	private WorkingMode mMode;
	private int mNextValidationIndex;
	private int mDeltaRxCount;
	private ReentrantLock mDelataLock;
	private int mValidationSize = 10000;


	public static enum WorkingMode{
		TRAIN, VALIDATE, TEST
	}

	public DnnController(MessagesSwitch messageSwitch){
		this(messageSwitch,1000,"mnist");
	}
	public DnnController(MessagesSwitch messagesSwitch, int dataSize){
		this(messagesSwitch,dataSize,"mnist");
	}
	public DnnController(MessagesSwitch messagesSwitch,String dataSet){
		this(messagesSwitch,1000,dataSet);
	}	
	public DnnController(MessagesSwitch messageSwitch,int dataSize,String dataSet){
		setModel(new DnnModel(mModelParameters));
		mMessageSwitch = messageSwitch;
		mControllerStatistics = new ArrayList<>();
		mValidationResults = new ArrayList<>();
		setModelUpdater(new ModelUpdater(this));
		mNextBeginningSection = 0;
		mDataSize = dataSize;
		mNextEndingSection = mDataSize; 
		mClientDataManager = new ClientDataManager(this);
		mModelVersion =0;
		//		QueueSize = 5;
		//		mTrainingDataQueue = new LinkedBlockingDeque<>(QueueSize); 
		lastStatPath = "";
		mTrainingDone = false;
		mNextTrainingIndex = 1;
		mDataType = "train";
		mDataSet = dataSet;
		mUpdateInterval = 5;
		mValidateInterval = 100;
		mMode = WorkingMode.TRAIN;
		mNextValidationIndex =1; 
		mDeltaRxCount = 0;
		mDelataLock = new ReentrantLock();
	}

	public void runDnnController(){
		mMessageSwitch.setController(this);
		mClientDataManager.Init();
		//		trainigDataQueueInit();
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

	//	public void trainingDataQueueFiller(){
	//		try {
	//			mTrainingDataQueue.put(mModel.getTrainingData(getNextTrainingDescriptor()));
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		}
	//	}
	//	public void trainigDataQueueInit(){
	//		for(int i=0; i<QueueSize; i++ ){
	//
	//			try {
	//				mTrainingDataQueue.put(mModel.getTrainingData(getNextTrainingDescriptor()));
	//			} catch (InterruptedException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//	}

	//	public DnnTrainingData getNextTrainingData() throws InterruptedException{
	//
	//		return mTrainingDataQueue.take();
	//	}

	public DnnIndex getNextTrainingIndex() throws InterruptedException{
		DnnIndex nextTrainingIndex = new DnnIndex(mDataSize,mDataType,mNextTrainingIndex,mDataSet);
		if(mNextTrainingIndex < mModel.getNumberOfTrainingObjects()/mDataSize){
			mNextTrainingIndex++;
			if(mNextEndingSection > mModel.getNumberOfTrainingObjects()/mDataSize){
				mTrainingDone = true;
			}
		}
		return nextTrainingIndex;
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
		saveStatisticsToCSV();
		saveStatisticsToFile();
		mMessageSwitch.closeAll();
	}

	public int getSectionLength(){
		return mDataSize;
	}
	public void setSectionLength(int sectionLength){
		mDataSize = sectionLength;
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
			mNextBeginningSection += mDataSize;
			mNextEndingSection += mDataSize;
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

		out = null;
		fileName = "DnnValidation_" + df.format(dateObj)+ ".txt";
		if(!outDir.exists()){
			outDir.mkdir();
		}
		File validationFile = new File("dnnOut/"+fileName);
		try{
			out = new PrintWriter(validationFile ,"UTF-8");
			for (DnnValidationResult dnnValidation : mValidationResults) {
				out.print(dnnValidation.getModelVersion().toString() + dnnValidation.getAccuracy().toString() + "\n ----------------------------------------------------- \n");
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

		out = null;
		fileName = "DnnValidation_" + df.format(dateObj)+ ".csv";
		if(!outDir.exists()){
			outDir.mkdir();
		}
		File validationFile = new File("dnnOut/"+fileName);
		try{
			out = new PrintWriter(validationFile ,"UTF-8");
			if(!mValidationResults.isEmpty()){
				out.print("model,accuracy\n");
			}
			for (DnnValidationResult dnnValidation : mValidationResults) {
				out.print(dnnValidation.getModelVersion().toString()+","+dnnValidation.getAccuracy().toString()+"\n");
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

	public String getDataType() {
		return mDataSet;
	}

	public void setDataType(String mDataType) {
		this.mDataSet = mDataType;
	}
	public void setModelVersion(int modelVersion) {
		mModelVersion = modelVersion;		
	}
	public int getUpdateInterval() {
		return mUpdateInterval;
	}
	public DnnMessage getNextBundleMessage() {
		DnnMessage message = null;
		mDelataLock.lock();
		try{		
			if(mDeltaRxCount >= mValidateInterval){
				mDeltaRxCount = 0;
				mMode = WorkingMode.VALIDATE;
			}else{
				mDeltaRxCount++;
			}
			DnnBundle dnnBundle = null;
			switch(mMode){
			case TRAIN:{
				dnnBundle = getNextTrainingBundle();
				message = new DnnTrainMessage(dnnBundle);
				break;
			}case TEST:{

				break;
			}case VALIDATE:{
				dnnBundle = getNextValidationBundle();
				message = new DnnValidationMessage("",dnnBundle);
				mMode = WorkingMode.TRAIN;
				break;
			}default:
				dnnBundle = getNextTrainingBundle();
				message = new DnnTrainMessage(dnnBundle);
				break;
			}
		}finally {
			mDelataLock.unlock();
		}
		return message;
	}
	private DnnBundle getNextTrainingBundle(){
		DnnIndex dnnIndex = new DnnIndex(mDataSize, "train", (Integer)mNextTrainingIndex, mDataSet);
		mNextTrainingIndex++;
		if(mNextTrainingIndex > mModel.getNumberOfTrainingObjects()/mDataSize){
			mNextTrainingIndex=1;
		}
		
		DnnModelDescriptor modelDescriptor = mModel.getModelDescriptor();
		DnnBundle trainingBundle =  new DnnBundle(modelDescriptor, dnnIndex);
		return trainingBundle;
	}
	private DnnBundle getNextValidationBundle(){
		DnnIndex dnnIndex = new DnnIndex(mValidationSize, "validate", (Integer)mNextValidationIndex, mDataSet);
		DnnModelDescriptor modelDescriptor = mModel.getModelDescriptor();
		DnnBundle trainingBundle =  new DnnBundle(modelDescriptor, dnnIndex);
		return trainingBundle;
	}
	public void addValidationResult(DnnValidationResult validationResult) {
		mValidationResults.add(validationResult);
	}

}
