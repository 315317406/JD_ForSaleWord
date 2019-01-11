package view;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import util.PBE;

public class RegisterFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7686842173428502095L;
	private JPanel contentPane;
	private JTextField mcodeTextField;
	private JTextField pcodeTextField;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RegisterFrame frame = new RegisterFrame();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public RegisterFrame() {
		setTitle("注册");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 528, 163);
		setResizable(false);
		this.setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("机器码:");
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		label.setBounds(27, 30, 54, 21);
		contentPane.add(label);
		
		mcodeTextField = new JTextField();
		mcodeTextField.setEditable(false);
		mcodeTextField.setText(PBE.MD5Encode(PBE.getSerialNumber("C") + PBE.getCpuSerial(), "utf8"));
		mcodeTextField.setBounds(91, 30, 288, 21);
		contentPane.add(mcodeTextField);
		mcodeTextField.setColumns(10);
		
		JButton button = new JButton("复制");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String mcodeText = mcodeTextField.getText();
				setSysClipboardText(mcodeText);
				JOptionPane.showMessageDialog(null,"复制成功!");
			}
		});
		button.setBounds(389, 29, 93, 23);
		contentPane.add(button);
		
		JLabel label_1 = new JLabel("注册码:");
		label_1.setFont(new Font("宋体", Font.PLAIN, 15));
		label_1.setBounds(27, 75, 54, 21);
		contentPane.add(label_1);
		
		pcodeTextField = new JTextField();
		pcodeTextField.setBounds(91, 75, 288, 21);
		contentPane.add(pcodeTextField);
		pcodeTextField.setColumns(10);
		
		JButton button_1 = new JButton("提交");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pcodeText = pcodeTextField.getText();
				if(pcodeText.length()==0) {
					JOptionPane.showMessageDialog(null, "注册码不能为空!", "错误", JOptionPane.ERROR_MESSAGE);
				}else if(pcodeText.length()<=20) {
					JOptionPane.showMessageDialog(null, "注册码错误!", "错误", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}else {
					if(PBE.checkPollCode(pcodeText)) {
//						UI.homeFrame = new HomeFrame();
//						UI.homeFrame.setVisible(true);
						JOptionPane.showMessageDialog(null, "注册成功,请退出后重新启动!", "提示", JOptionPane.INFORMATION_MESSAGE);
					}else {
						System.exit(0);
					}
				}
			}
		});
		button_1.setBounds(389, 74, 93, 23);
		contentPane.add(button_1);
	}
	
	/**
	 * 将字符串复制到剪切板。
	 */
	public void setSysClipboardText(String writeMe) {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable tText = new StringSelection(writeMe);
		clip.setContents(tText, null);
	}
	
}
