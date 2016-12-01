package com.kurosora;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// F1 Ű�� ����ȭ�� �ʷϻ�, % Ű�� ��������� ����
public class KeyCodeTest extends JFrame{

	JPanel contentPane = new JPanel();
	JLabel jla = new JLabel();
	
	public KeyCodeTest() {

		setTitle("Ű�ڵ� �׽�Ʈ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setContentPane(contentPane);
		contentPane.addKeyListener(new MyKeyListener());
		contentPane.add(jla);
		
		setSize(300, 300);
		setVisible(true);
		// JPanel�� Ű �Է¹��� �� �ֵ��� ��Ŀ���� ��
		contentPane.requestFocus();
	}
	
	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			
			jla.setText((e.getKeyText(e.getKeyCode())));
			// % Ű�� �Ǻ��ϱ� ���� e.getKeyChar() �̿�
			if (e.getKeyChar() == '%') {
				contentPane.setBackground(Color.green);
			// �����ڵ尡 �ƴ� ��� e.getKeyCode() �̿� 
			} else if (e.getKeyCode() == KeyEvent.VK_F1) {
				contentPane.setBackground(Color.yellow);
			}
		}
	}
	
	public static void main(String[] args) {

		new KeyCodeTest();
	}

}