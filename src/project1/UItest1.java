package project1;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class UItest1 {

	private JFrame frame;
	private final Action action_selectEncDirectory = new selectEncDirectory();
	private final Action action_selectDecDirectory = new selectDecDirectory();
	private final Action action_openEncDirectory = new openEncDirectory();
	private final Action action_openDecDirectory = new openDecDirectory();
	private final Action action_saveKey = new saveKeyToFile();
	private final Action action_loadKey = new loadKeyFromFile();
	private final Action action_1 = new selectProcessFile();
	final JTextArea text1 = new JTextArea();
	private JScrollPane scroll = new JScrollPane(text1);
	private JPasswordField txtNhpKeyM = new JPasswordField();
	private JFileChooser dirEncSelected = new JFileChooser(); ;
	private JFileChooser dirDecSelected = new JFileChooser(); ;
	private List<File> chosedFiles = null;
	private final Action action_2 = new EncryptFile();
	private final Action action_3 = new DecryptFile();
	private JButton btn_openEncDir;
	private JButton btn_openDecDir;
	private JToggleButton togglepassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UItest1 window = new UItest1();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UItest1() {
		initialize();
		modifyLabel();
		configurate();
	}

	public void modifyLabel() {
		TransferHandler th1 = new TransferHandler() {
			@Override
			public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
				return true;
			};
			@Override
			public boolean importData(JComponent comp, Transferable t) {
				try {
					chosedFiles = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
		            String fileNames = "";
		            for(File file: chosedFiles){
		            	fileNames += file.getName() + "\n";
		            }
		            text1.setText("File(s) Selected:\n\n" + fileNames);
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			
		};
		text1.setFont(new Font("Monospaced", Font.BOLD, 25));
		text1.setTransferHandler(th1);
		//
		TransferHandler th2 = new TransferHandler() {
			@Override
			public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
				return true;
			};
			@Override
			public boolean importData(JComponent comp, Transferable t) {
				try {
					List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
					File inFile = files.get(0);
					System.out.println(inFile.getAbsolutePath());
					FileInputStream inStream = new FileInputStream(inFile);
					byte[] data = new byte[128];
					int readed = inStream.read(data);
					byte[] input = new byte[readed];
					System.arraycopy(data, 0, input, 0, input.length);
					Decryptor Dec = new Decryptor("12345678");
					byte[] deckey = Dec.decryptBytes(input);
					if (deckey.length==3 && deckey[0]==-1 && deckey[1]==-1 && deckey[2]==-1) {
						JOptionPane.showMessageDialog(frame,"File l??u kh??a b??? l???i!","Th??ng b??o",JOptionPane.ERROR_MESSAGE);
					}
					else {
						String key = new String(Dec.decryptBytes(input));
						System.out.println("KEY:" + key);
						txtNhpKeyM.setText(key);
					}
					inStream.close();
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			
		};
		txtNhpKeyM.setTransferHandler(th2);
	}	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.setBounds(100, 100, 624, 582);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Ch???n th?? m???c l??u file m?? h??a");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setAction(action_selectEncDirectory);
		btnNewButton.setBounds(117, 422, 244, 27);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Ch???n file c???n x??? l??");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_1.setAction(action_1);
		btnNewButton_1.setBounds(220, 11, 190, 36);
		frame.getContentPane().add(btnNewButton_1);
		
		
		text1.setBounds(117, 58, 390, 294);
		text1.setEditable(false);
		scroll = new JScrollPane(text1);
		scroll.setBounds(117, 58, 390, 294);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frame.getContentPane().add(scroll);
		
		JButton btnNewButton_2 = new JButton("M?? h??a");
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_2.setAction(action_2);
		btnNewButton_2.setBounds(371, 422, 136, 52);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Gi???i m??");
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnNewButton_3.setAction(action_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton_3.setBounds(371, 480, 136, 52);
		frame.getContentPane().add(btnNewButton_3);
		
		txtNhpKeyM.setBounds(179, 364, 219, 20);
		frame.getContentPane().add(txtNhpKeyM);
		txtNhpKeyM.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nh???p Key");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(117, 363, 58, 20);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton_4 = new JButton("Ch???n th?? m???c l??u file gi???i m??");
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_4.setAction(action_selectDecDirectory);
		btnNewButton_4.setBounds(117, 480, 244, 27);
		frame.getContentPane().add(btnNewButton_4);
		
		btn_openEncDir = new JButton("...ch??a ch???n ???????ng d???n");
		btn_openEncDir.setAction(action_openEncDirectory);
		btn_openEncDir.setBounds(117, 449, 244, 27);
		frame.getContentPane().add(btn_openEncDir);
		
		btn_openDecDir = new JButton("...ch??a ch???n ???????ng d???n");
		btn_openDecDir.setAction(action_openDecDirectory);
		btn_openDecDir.setBounds(117, 507, 244, 25);
		frame.getContentPane().add(btn_openDecDir);
		
		togglepassword = new JToggleButton("Hi???n th???");
		togglepassword.setBounds(402, 363, 105, 23);
		frame.getContentPane().add(togglepassword);
		
		JButton btnSaveKey = new JButton("New button");
		btnSaveKey.setAction(action_saveKey);
		btnSaveKey.setBounds(293, 388, 105, 23);
		frame.getContentPane().add(btnSaveKey);
		
		JButton btnLoadKey = new JButton("New button");
		btnLoadKey.setAction(action_loadKey);
		btnLoadKey.setBounds(402, 388, 105, 23);
		frame.getContentPane().add(btnLoadKey);
		
		set_togglepassword();
		
		String choosertitle = "Ch???n th?? m???c l??u file m?? h??a";
		dirEncSelected.setCurrentDirectory(new java.io.File("C:\\Users\\hoang\\Desktop"));
		dirEncSelected.setDialogTitle(choosertitle);
		dirEncSelected.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirEncSelected.setAcceptAllFileFilterUsed(false); 
		
		choosertitle = "Ch???n th?? m???c l??u file";
		dirDecSelected.setCurrentDirectory(new java.io.File("C:\\Users\\hoang\\Desktop"));
		dirDecSelected.setDialogTitle(choosertitle);
		dirDecSelected.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		dirDecSelected.setAcceptAllFileFilterUsed(false); 
	}
	
	private void configurate() {
	    File cffile  = new File("config.txt");
	    if (!cffile.exists())
	    	System.out.println("Config file not found!");
	    else {
	    	try {
	    	System.out.println("Configurated!");
	    	FileInputStream inStream = new FileInputStream(cffile);
	    	String encPath = new String("");
	    	byte readed = 0;
	    	while ((readed = (byte) inStream.read()) >= 0)
	    	{
	    		char c = (char) readed;
	    		if (c=='\n') break;
	    		encPath = encPath + c;
	    	}
	    	inStream.read();
	    	String decPath = new String("");
	    	readed = 0;
	    	while ((readed = (byte) inStream.read()) >= 0)
	    	{
	    		char c = (char) readed;
	    		if (c=='\n') break;
	    		decPath = decPath + c;
	    	}
	    	
	    	encPath = encPath.substring(17);
	    	if (!encPath.equals("null") && encPath.length()>0) {
	    		dirEncSelected.setSelectedFile(new File(encPath));
	    		System.out.println(dirEncSelected.getSelectedFile());
	    		btn_openEncDir.setText(encPath);
	    	}
	    	
	    	decPath = decPath.substring(17);
	    	if (!decPath.equals("null") && decPath.length()>0) {
		    	dirDecSelected.setSelectedFile(new File(decPath));
		    	System.out.println(dirDecSelected.getSelectedFile());
		    	btn_openDecDir.setText(decPath);
	    	}
	    
	    } catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	}
	
   	private void set_togglepassword() {
	
		ItemListener itemListener = new ItemListener() {
			 
	        public void itemStateChanged(ItemEvent itemEvent)
	        {
	
	            // event is generated in button
	            int state = itemEvent.getStateChange();
	
	            // if selected print selected in console
	            if (state == ItemEvent.SELECTED) {
	            	System.out.println("Selected");
	            	txtNhpKeyM.setEchoChar((char)0);
	            }
	            else {
	
	                // else print deselected in console
	            	System.out.println("Deselected");
	            	txtNhpKeyM.setEchoChar('???');
	            }
	        }
	    };
	    togglepassword.addItemListener(itemListener);
	}
   	
   	private void update_config() {
   		
		String encPath = "Encrypt File Dir=";
    	try{
    		encPath = encPath + dirEncSelected.getSelectedFile();
    	} catch (Exception e2) {}
    	encPath = encPath + "\n";
    	
    	String decPath = "Decrypt File Dir=";
    	try {
    		decPath = decPath + dirDecSelected.getSelectedFile();
    	} catch (Exception e3) {}
    	encPath = encPath + "\n";
    	File cffile  = new File("config.txt");
		try {
			FileOutputStream outStream = new FileOutputStream(cffile);
			outStream.write(encPath.getBytes());
			outStream.write(decPath.getBytes());
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

   	}
	
	private class selectEncDirectory extends AbstractAction {
		public selectEncDirectory() {
			putValue(NAME, "Ch???n th?? m???c l??u file gi???i m??");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			     
			    if (dirEncSelected.showOpenDialog(dirEncSelected) == JFileChooser.APPROVE_OPTION) {
			    	btn_openEncDir.setText(""+dirEncSelected.getSelectedFile());
			    	update_config();			
			      }
			    else {
			    	//btn_openEncDir.setText("No Selection ");
			      }
		}
	}
	
	private class selectDecDirectory extends AbstractAction {
		public selectDecDirectory() {
			putValue(NAME, "Ch???n th?? m???c l??u file m?? h??a");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			   
			    if (dirDecSelected.showOpenDialog(dirDecSelected) == JFileChooser.APPROVE_OPTION) { 
			    	btn_openDecDir.setText(""+dirDecSelected.getSelectedFile());
			    	update_config();			
			      }
			    else {
			    	//btn_openDecDir.setText("No Selection ");
			      }
		}
	}
	
	private class openEncDirectory extends AbstractAction {
		public openEncDirectory() {
			putValue(NAME, "...ch??a ch???n ???????ng d???n");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
        	File file = new File (""+dirEncSelected.getSelectedFile());
        	Desktop desktop = Desktop.getDesktop();
        	try {
				desktop.open(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class openDecDirectory extends AbstractAction {
		public openDecDirectory() {
			putValue(NAME, "...ch??a ch???n ???????ng d???n");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
        	File file = new File (""+dirDecSelected.getSelectedFile());
        	Desktop desktop = Desktop.getDesktop();
        	try {
				desktop.open(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class selectProcessFile extends AbstractAction {
		public selectProcessFile() {
			putValue(NAME, "Ch???n File c???n x??? l??");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		
		@Override
        public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);

            int option = fileChooser.showOpenDialog(frame);
            if(option == JFileChooser.APPROVE_OPTION){
            	chosedFiles = Arrays.asList(fileChooser.getSelectedFiles());
               String fileNames = "";
               for(File file: chosedFiles){
                  fileNames += file.getName() + "\n";
                  System.out.println(file.getName());
               }
               text1.setText("File(s) Selected:\n\n" + fileNames);
            }else{
            	text1.setText("Open command canceled");
            }
         }
	}
	
	private class EncryptFile extends AbstractAction {
		public EncryptFile() {
			putValue(NAME, "M?? h??a");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {

			char[] pass = txtNhpKeyM.getPassword();
			String key= new String(pass);
			Encryptor Enc = new Encryptor(key);
			
			if (pass.length>128) {
				JOptionPane.showMessageDialog(frame, "Key nh???p v??o qu?? d??i!","Th??ng b??o",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (chosedFiles == null)
			{
				JOptionPane.showMessageDialog(frame, "Ch??a ch???n File ????? x??? l??!","Th??ng b??o",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String encPath = "" + dirEncSelected.getSelectedFile();
			if (encPath.equals("null")) {
				JOptionPane.showMessageDialog(frame, "Ch??a ch???n ???????ng d???n l??u File m?? h??a!","Th??ng b??o",JOptionPane.ERROR_MESSAGE);
				return;	
			}
			
			for(File file: chosedFiles) {
				Enc.encrypting(file,""+dirEncSelected.getSelectedFile());
			}
			JOptionPane.showMessageDialog(frame, "M?? h??a th??nh c??ng!","Th??ng b??o",JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private class DecryptFile extends AbstractAction {
		public DecryptFile() {
			putValue(NAME, "Gi???i m??");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {

			char[] pass = txtNhpKeyM.getPassword();
			String key= new String(pass);
			
			if (pass.length>128) {
				JOptionPane.showMessageDialog(frame, "Key nh???p v??o qu?? d??i!","Th??ng b??o",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			Decryptor Dec = new Decryptor(key);
			
			
			
			if (chosedFiles == null)
			{
				JOptionPane.showMessageDialog(frame, "Ch??a ch???n File ????? x??? l??!","Th??ng b??o",JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String decPath = "" + dirDecSelected.getSelectedFile();
			if (decPath.equals("null")) {
				JOptionPane.showMessageDialog(frame, "Ch??a ch???n ???????ng d???n l??u File gi???i m??!","Th??ng b??o",JOptionPane.ERROR_MESSAGE);
				return;	
			}
			
			for(File file: chosedFiles) {
				Dec.decrypting(file,""+dirDecSelected.getSelectedFile());
			}
			JOptionPane.showMessageDialog(frame, "Gi???i m?? th??nh c??ng!","Th??ng b??o",JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private class saveKeyToFile extends AbstractAction{
		public saveKeyToFile() {
			putValue(NAME, "L??u kh??a");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			char[] pass = txtNhpKeyM.getPassword();
			byte[] bt = {1,1,1,1,1,1,1,1};
			try {
				bt = new String(pass).getBytes("UTF-8");
			} catch (UnsupportedEncodingException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Encryptor Enc = new Encryptor("12345678");
			byte[] key = Enc.encryptBytes(bt);
			System.out.println(key.length);
		    JFileChooser chooser = new JFileChooser();
		    chooser.setDialogTitle("Ch???n ???????ng d???n l??u file Kh??a");
		    chooser.showSaveDialog(frame);
		    File outFile  = new File(""+chooser.getSelectedFile());
		    try {
				FileOutputStream outStream = new FileOutputStream(outFile);
				outStream.write(key);
				outStream.flush();
				outStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class loadKeyFromFile extends AbstractAction{
		public loadKeyFromFile() {
			putValue(NAME, "D??ng kh??a");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(fileChooser);
            fileChooser.setDialogTitle("Ch???n ???????ng d???n m??? file Kh??a");
            System.out.println(""+fileChooser.getSelectedFile());
			File inFile = new File(""+fileChooser.getSelectedFile());
			try {
				FileInputStream inStream = new FileInputStream(inFile);
				byte[] data = new byte[128];
				int readed = inStream.read(data);
				byte[] input = new byte[readed];
				System.arraycopy(data, 0, input, 0, input.length);
				Decryptor Dec = new Decryptor("12345678");
				byte[] deckey = Dec.decryptBytes(input);
				if (deckey.length==3 && deckey[0]==-1 && deckey[1]==-1 && deckey[2]==-1) {
					JOptionPane.showMessageDialog(frame,"File l??u kh??a b??? l???i!","Th??ng b??o",JOptionPane.ERROR_MESSAGE);
				}
				else {
					String key = new String(Dec.decryptBytes(input));
					System.out.println("KEY:" + key);
					txtNhpKeyM.setText(key);
				}
				inStream.close();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
}
