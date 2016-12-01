package com.game.shooting;

import java.awt.*;
import java.awt.event.*;//KeyListener(interface) ���� �ʿ���
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*; //JFrame ���� �ʿ�



/*
 *  - �����丵 (Refactoring) - 
 *  1 . ���� = ctrl + shift + f
 *  2 . �ҽ� ���� (class) = ��ǰ ó��
 *  3 . 
 *  
 *  
 *  
 * */
//GUI�� �����Ϸ��� JFrame�� ����ؾ���
//KeyListener : Ű���忡�� Ű ������ ó���Ҷ� ���
//Runnable : �����带 ����� ����ϱ� ���ؼ� implements ��
public class Shoot extends JFrame implements Runnable, KeyListener {
	private BufferedImage bi = null;
	private ArrayList msList = null; //ArrayList ��ü ����
	private ArrayList enList = null; //ArrayList ��ü ����
	//����Ű & �߻�Ű ���� ��� �����ε�...
	private boolean left = false, right = false, up = false, down = false, fire = false;
	private boolean start = false, end = false;
	
	//    w / h = ����/����             ,xw / xy  = �÷��̾� ũ��
	private int w = 300, h = 500, x = 130, y = 450, xw = 20, xh = 20;

	public Shoot() { //shoot Ŭ���� ������
		//
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		msList = new ArrayList(); //ArrayList ��� ���� ����
		enList = new ArrayList(); //ArrayList ��� ���� ����
		this.addKeyListener(this);
		this.setSize(w, h);//â�� ũ������ �ϴ°�  x=w , y=h
		this.setTitle("Shooting Game");// â ���� ǥ��
		this.setResizable(false); //â ũ�� ������ ������  false == ���� ����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //swing x��ư Ŭ���� ���� �Ǵ� �κ�
		this.setVisible(true);//������ ���̱�
		
	}

	public void run() {
		try {
			int msCnt = 0;
			int enCnt = 0;
			while (true) { //���ѷ��� 
				Thread.sleep(10); // ��� ������Ű�� �޼ҵ� �ӵ� ����

				if (start) { // 
					if (enCnt > 200) {//enCnt > 2000 ũ��
						enCreate(); //
						enCnt = 0;
					}
					if (msCnt >= 100) {
						fireMs();
						msCnt = 0;
					}
					msCnt += 10;
					enCnt += 10;
					keyControl();
					crashChk();
				}
				draw();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fireMs() {
		if (fire) {
			if (msList.size() < 100) {
				Ms m = new Ms(this.x, this.y);
				msList.add(m);
			}
		}
	}

	public void enCreate() { // 
		for (int i = 0; i <9; i++) {//9�� �ݺ�
			//��������
			double rx = Math.random() * (w - xw);
			double ry = Math.random() * 50;
			Enemy en = new Enemy((int) rx, (int) ry);
			enList.add(en);
		}
	}

	public void crashChk() {
		Graphics g = this.getGraphics();
		Polygon p = null;
		for (int i = 0; i < msList.size(); i++) {
			Ms m = (Ms) msList.get(i);
			for (int j = 0; j < enList.size(); j++) {
				Enemy e = (Enemy) enList.get(j);
				int[] xpoints = { m.x, (m.x + m.w), (m.x + m.w), m.x };
				int[] ypoints = { m.y, m.y, (m.y + m.h), (m.y + m.h) };
				p = new Polygon(xpoints, ypoints, 4);
				if (p.intersects((double) e.x, (double) e.y, (double) e.w, (double) e.h)) {
					msList.remove(i);
					enList.remove(j);
				}
			}
		}
		for (int i = 0; i < enList.size(); i++) {
			Enemy e = (Enemy) enList.get(i);
			int[] xpoints = { x, (x + xw), (x + xw), x };
			int[] ypoints = { y, y, (y + xh), (y + xh) };
			p = new Polygon(xpoints, ypoints, 4);
			if (p.intersects((double) e.x, (double) e.y, (double) e.w, (double) e.h)) {
				enList.remove(i);
				start = false;
				end = true;
			}
		}
	}

	public void draw() {
		Graphics gs = bi.getGraphics();
		gs.setColor(Color.white);
		gs.fillRect(0, 0, w, h);
		gs.setColor(Color.black);
		gs.drawString("Enemy ��ü�� : " + enList.size(), 180, 50);
		gs.drawString("Ms ��ü�� : " + msList.size(), 180, 70);
		gs.drawString("���ӽ��� : Enter", 180, 90);

		if (end) {
			gs.drawString("G A M E     O V E R", 100, 250);
		}

		gs.fillRect(x, y, xw, xh);

		for (int i = 0; i < msList.size(); i++) {
			Ms m = (Ms) msList.get(i);
			gs.setColor(Color.blue);
			gs.drawOval(m.x, m.y, m.w, m.h);
			if (m.y < 0)
				msList.remove(i);
			m.moveMs();
		}
		gs.setColor(Color.black);
		for (int i = 0; i < enList.size(); i++) {
			Enemy e = (Enemy) enList.get(i);
			gs.fillRect(e.x, e.y, e.w, e.h);
			if (e.y > h)
				enList.remove(i);
			e.moveEn();
		}

		Graphics ge = this.getGraphics();
		try {
			ge.drawImage(bi, 0, 0, w, h, this);
		} catch (Exception e) {
			//���ܰ� ���褤 ������ڵ�
			System.out.println("���� �߻� ��!!");
			// TODO: handle exception
		}
		
	}

	public void keyControl() {
		if (0 < x) {
			if (left)
				x -= 3;
		}
		if (w > x + xw) {
			if (right)
				x += 3;
		}
		if (25 < y) {
			if (up)
				y -= 3;
		}
		if (h > y + xh) {
			if (down)
				y += 3;
		}
	}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = true;
			break;
		case KeyEvent.VK_RIGHT:
			right = true;
			break;
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		case KeyEvent.VK_A:
			fire = true;
			break;
		case KeyEvent.VK_ENTER:
			start = true;
			end = false;
			break;
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			left = false;
			break;
		case KeyEvent.VK_RIGHT:
			right = false;
			break;
		case KeyEvent.VK_UP:
			up = false;
			break;
		case KeyEvent.VK_DOWN:
			down = false;
			break;
		case KeyEvent.VK_A:
			fire = false;
			break;
		}
	}

	public void keyTyped(KeyEvent ke) {
	}

	public static void main(String[] args) {
		Thread t = new Thread(new Shoot());//������ �ϳ� ���� new shoot��ü�� ������ ����  ������  == new shoot()
		t.start();//������ ����
	}
}

class Ms {//
	int x;
	int y;
	int w = 55;
	int h = 55;

	public Ms(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveMs() {
		y--;
	}
}

class Enemy {//���� ����
	int x;
	int y;
	int w = 10;
	int h = 10;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void moveEn() {
		y++;
	}
}