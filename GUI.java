import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class GUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFileChooser fileChooser = new JFileChooser(".");
	private JButton button = new JButton("Open Simulation Folder");
	private JButton queryButton = new JButton("Query");

	public GUI() {
		this.setTitle("Energy Plus Simulation Import/Query");
		this.setPreferredSize(new Dimension(200, 100));
		this.getContentPane().add(button, BorderLayout.NORTH);
		this.getContentPane().add(queryButton, BorderLayout.CENTER);
		button.addActionListener(this);
		queryButton.addActionListener(this);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setIgnoreRepaint(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(button))
			try {
				openFile();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		else if (source.equals(queryButton)) {
			System.out.println("...");
		}
		
	}

	public void openFile() throws UnknownHostException, IOException, ParseException {
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("Open Simulation Folder");
		int ret = fileChooser.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			//System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
			
			// Call Write Data and Write all simulations into OPENTSDB
			iterateSimulationFolder(fileChooser.getSelectedFile().getAbsolutePath());

		}
	}

	public void iterateSimulationFolder(String folderPath) throws UnknownHostException, IOException, ParseException {

		File path = new File(folderPath);
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				System.out.println(files[i]);
				writeData(files[i].getPath());
			}
		}


	}
	
	public void writeData(String filePath) throws UnknownHostException, IOException, ParseException {
		
		BufferedReader BR = new BufferedReader(new FileReader(filePath));
		
		String[] Tokens = filePath.split("/");
		String SimName = Tokens[Tokens.length-1].split("\\.")[0];
		
		String Line;
		int LineNum = 0;
		Socket sock = new Socket("172.16.209.128", 4242); 
		List<String> PointsName = new ArrayList<String>();

		while ((Line = BR.readLine()) != null) {
			String[] TmpList = Line.split(",");
			if (LineNum == 0) { // Create Point List
				for (int i = 1; i != TmpList.length; i++) {
					String tmpName = TmpList[i].replace(":", "_").replace(" ", "_");
					String replaceName[] = tmpName.split("_");
					StringBuilder trueName = new StringBuilder();
					for (int j = 0; j != replaceName.length-1; j++)
						trueName.append(replaceName[j]);
					PointsName.add(trueName.toString());
				}
			}
			
			else {
				//String point = "put my.metric 1314137008 60 host=someplace.net foo=1\n"; 
				// Change time to Unix time
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat currentTime = new SimpleDateFormat("MM/dd HH:mm:ss");
				cal.setTime(currentTime.parse(TmpList[0]));
				cal.set(Calendar.YEAR, 2014);
				Long UnixTime = cal.getTimeInMillis();
				StringBuilder pointToWrite = new StringBuilder();
				for (int i = 1; i != TmpList.length; i++) 
					pointToWrite.append("put " + SimName + " " + UnixTime + " " + TmpList[i] + " PointsName=" + PointsName.get(i-1) + "\n");
				sock.getOutputStream().write(pointToWrite.toString().getBytes());
			}
			LineNum++;
		}
		
		BR.close();
		sock.close();
	}
	

	public static void main(String[] args) {
		Frame frame = new GUI();
		frame.setVisible(true);
	}

}
