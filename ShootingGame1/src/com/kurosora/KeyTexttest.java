package com.kurosora;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// 방향키로 10 씩 라벨 움직이기
public class KeyTexttest extends JFrame {
	
	JPanel contentPane = new JPanel();
	JLabel la = new JLabel("Hellow");
	final int FLY_UNIT = 10;
	
	public KeyTexttest() {
		// TODO Auto-generated constructor stub
		setTitle("상하좌우키 조작으로 텍스트 움직이기");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.addKeyListener(new MyKeyListener());
		la.setLocation(50, 50);
		la.setSize(100, 20);
		contentPane.add(la);
		setSize(300, 300);
		setVisible(true);
		contentPane.requestFocus();
	}
	
	class MyKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated constructor stub
			int keyCode = e.getKeyCode();
			switch (keyCode) {
			case KeyEvent.VK_UP:
				la.setLocation(la.getX(), la.getY()-FLY_UNIT);
				break;
			case KeyEvent.VK_DOWN:
				la.setLocation(la.getX(), la.getY()+FLY_UNIT);
				break;
			case KeyEvent.VK_RIGHT:
				la.setLocation(la.getX()+FLY_UNIT, la.getY());
				break;
			case KeyEvent.VK_LEFT:
				la.setLocation(la.getX()-FLY_UNIT, la.getY());
				break;
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new KeyTexttest();
	}

}