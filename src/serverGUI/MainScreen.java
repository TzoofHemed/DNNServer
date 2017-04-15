package serverGUI;

import javax.swing.*;
import javax.swing.border.Border;

import dnnProcessingUnit.DnnController;
import dnnUtil.dnnMessage.DnnMessage;
import tcpConectivity.TCPServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.*;
import java.util.Enumeration;

public class MainScreen extends JFrame{
	private JTextArea mainLog;
	private JButton startRxButton;
	private JButton stopRxButton;
	private JButton showIpButton;
	private JButton saveLogButton;
	private JTextField serverCmd;
	private TCPServer mServer;
	private JButton sendCmd;
	private DnnController mDnnController;
	private GUIHelper mGUIHelper;



	public MainScreen(){
		mGUIHelper = new GUIHelper(this);
		JPanel panelFields = new JPanel();
		panelFields.setLayout(new BoxLayout(panelFields, BoxLayout.X_AXIS));

		JPanel panelFields2 = new JPanel();
		panelFields2.setLayout(new BoxLayout(panelFields2, BoxLayout.X_AXIS));

		JPanel panelFields3 = new JPanel();
		panelFields3.setLayout(new BoxLayout(panelFields3, BoxLayout.X_AXIS));

		mainLog = new JTextArea();
		mainLog.setColumns(60);
		mainLog.setRows(10);
		mainLog.setEditable(false);
		mainLog.setForeground(Constants.LOG_SCREEN_FONT_COLOR);
		mainLog.setBackground(Constants.LOG_SCREEN_BACKGROUND_COLOR);

		Font mainLogFont = new Font("SansSerif", Font.BOLD, 12);
		mainLog.setFont(mainLogFont);



		startRxButton = new JButton(" Start DNN Server ");
		startRxButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				startServer();
			}
		});
		startRxButton.setBorder(BorderFactory.createLineBorder(Constants.LOG_SCREEN_FONT_COLOR));

		stopRxButton = new JButton(" Stop DNN Server ");
		stopRxButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				stopServer();

			}
		});
		stopRxButton.setBorder(BorderFactory.createLineBorder(Constants.LOG_SCREEN_FONT_COLOR));


		serverCmd = new JTextField();
		serverCmd.setSize(Constants.CMD_FIELD_W, Constants.CMD_FIELD_H);
		serverCmd.setForeground(Constants.CMD_FONT_COLOR);
		serverCmd.setBackground(Constants.CMD_BACKGROUND_COLOR);
		Font cmdFont = new Font("SansSerif", Font.BOLD, 12);  

		serverCmd.setFont(cmdFont); 

		Action hitCmdAction = new AbstractAction()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String cmdInput = mGUIHelper.cmdInputHandler(serverCmd.getText());
				serverCmd.setText("");
				if(cmdInput.equals(Constants.notACmd)){
					mainLog.append(">>" + Constants.notACmd + "\n");	
				}else{
					mGUIHelper.cmdPrompt(cmdInput.split(Constants.delimiter));					
				}		
				
				
			}
		};

		serverCmd.addActionListener( hitCmdAction );


		sendCmd = new JButton("Send CMD");
		sendCmd.addActionListener(hitCmdAction);



		showIpButton = new JButton(" Server IP ");
		showIpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				getIp();

			}
		});
		showIpButton.setBorder(BorderFactory.createLineBorder(Constants.LOG_SCREEN_FONT_COLOR));
		saveLogButton = new JButton(" Save Log ");
		saveLogButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				try{
					mDnnController.saveStatisticsToFile();
				}catch(Exception e){
					mainLog.append(">>Error: server wasn't started!\n");
					System.out.println("server wasn't started!\n");
				}
			}
		});
		saveLogButton.setBorder(BorderFactory.createLineBorder(Constants.LOG_SCREEN_FONT_COLOR));

		//add the buttons and the text fields to the panel
		panelFields.add(mainLog);
		panelFields3.add(startRxButton);
		panelFields3.add(stopRxButton);

		panelFields2.add(serverCmd);
		panelFields2.add(sendCmd);

		panelFields3.add(showIpButton);
		panelFields3.add(saveLogButton);

		panelFields2.setPreferredSize(new Dimension(Constants.CMD_FIELD_W, Constants.CMD_FIELD_H));
		panelFields.setPreferredSize(new Dimension(Constants.MAIN_LOG_W, Constants.MAIN_LOG_W));

		getContentPane().add(panelFields);
		getContentPane().add(panelFields2);
		getContentPane().add(panelFields3);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

		setPreferredSize(new Dimension(Constants.PANEL_W, Constants.PANEL_H));
		setVisible(true);

		JScrollPane scrollPane = new JScrollPane(mainLog);
		panelFields.add(scrollPane);

		panelFields.setBackground(Color.DARK_GRAY);
		panelFields2.setBackground(Color.PINK);
		panelFields3.setBackground(Color.DARK_GRAY);
		Border cyanBorder = BorderFactory.createLineBorder(Constants.LOG_SCREEN_FONT_COLOR, 12);
		Border pinkBorder = BorderFactory.createLineBorder(Color.PINK, 12);
		Border orangeBorder = BorderFactory.createLineBorder(Color.ORANGE, 12);
		panelFields.setBorder(cyanBorder);
		panelFields2.setBorder(pinkBorder);
		panelFields3.setBorder(orangeBorder);

		getContentPane().setBackground(Color.ORANGE);

	}
	public DnnController getController(){
		return mDnnController;
	}
	
	public JTextArea getMainLog(){
		return mainLog;
	}
	
	public void startServer(){
		//creates the object OnMessageReceived asked by the TCPServer constructor
		mServer = new TCPServer(new TCPServer.OnMessageReceived() {
			@Override
			//this method declared in the interface from TCPServer class is implemented here
			//this method is actually a callback method, because it will run every time when it will be called from
			//TCPServer class (at while)
			public void messageReceived(DnnMessage message) { 
				mainLog.append("\nmessage type: " + message.getMessageType() + " from: "+ message.getSenderName() + " Content: " + message.getContent().toString()); 
			}
		});
		mServer.start();

		// disable the start button and enable the stop one
		startRxButton.setEnabled(false);
		stopRxButton.setEnabled(true);

		mDnnController = new DnnController(mServer.getMessageSwitch());
		mDnnController.start();
	}
	
	public void stopServer() {
		if (mServer != null) {
			mServer.close();
		}

		// disable the stop button and enable the start one
		startRxButton.setEnabled(true);
		stopRxButton.setEnabled(false);

	}
	
	public void getIp(){
		Enumeration<NetworkInterface> n = null;
		try {
			n = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while(n.hasMoreElements())
		{
			NetworkInterface NI = n.nextElement();

			Enumeration<InetAddress> a = NI.getInetAddresses();
			while (a.hasMoreElements())
			{
				InetAddress addr = a.nextElement();
				String IpAddress = addr.getHostAddress();
				if(IpAddress.contains("192.168.")){
					System.out.println("  " + IpAddress);
					mainLog.append(">>" +"Server Ip: "+IpAddress+ "\n");
				}

			}
		}
	}

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
