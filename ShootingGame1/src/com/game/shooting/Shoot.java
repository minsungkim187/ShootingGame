package com.game.shooting;

import java.awt.*;
import java.awt.event.*;//KeyListener(interface) 사용시 필요함
import java.awt.image.*;
import java.util.ArrayList;
import javax.swing.*; //JFrame 사용시 필요



/*
 *  - 리펙토링 (Refactoring) - 
 *  1 . 정렬 = ctrl + shift + f
 *  2 . 소스 구분 (class) = 부품 처럼
 *  3 . 
 *  
 *  
 *  
 * */
//GUI를 구현하려면 JFrame를 상속해야함
//KeyListener : 키보드에서 키 눌린거 처리할때 사용
//Runnable : 쓰레드를 만들고 사용하기 위해서 implements 함
public class Shoot extends JFrame implements Runnable, KeyListener {
	private BufferedImage bi = null;
	private ArrayList msList = null; //ArrayList 객체 생성
	private ArrayList enList = null; //ArrayList 객체 생성
	//방향키 & 발사키 정보 담는 변수인듯...
	private boolean left = false, right = false, up = false, down = false, fire = false;
	private boolean start = false, end = false;
	
	//    w / h = 넓이/높이             ,xw / xy  = 플레이어 크기
	private int w = 300, h = 500, x = 130, y = 450, xw = 20, xh = 20;

	public Shoot() { //shoot 클래스 생성자
		//
		bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		msList = new ArrayList(); //ArrayList 담는 변수 선언
		enList = new ArrayList(); //ArrayList 담는 변수 선언
		this.addKeyListener(this);
		this.setSize(w, h);//창의 크기지정 하는것  x=w , y=h
		this.setTitle("Shooting Game");// 창 제목 표시
		this.setResizable(false); //창 크기 조절을 관리함  false == 조절 못험
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //swing x버튼 클릭시 종료 되는 부분
		this.setVisible(true);//프레임 보이기
		
	}

	public void run() {
		try {
			int msCnt = 0;
			int enCnt = 0;
			while (true) { //무한루프 
				Thread.sleep(10); // 잠시 정지시키는 메소드 속도 조절

				if (start) { // 
					if (enCnt > 200) {//enCnt > 2000 크면
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
		for (int i = 0; i <9; i++) {//9번 반복
			//지역변수
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
		gs.drawString("Enemy 객체수 : " + enList.size(), 180, 50);
		gs.drawString("Ms 객체수 : " + msList.size(), 180, 70);
		gs.drawString("게임시작 : Enter", 180, 90);

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
			//예외가 생김ㄴ 실행될코드
			System.out.println("예외 발생 함!!");
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
		Thread t = new Thread(new Shoot());//쓰레드 하나 생성 new shoot객체를 가지고 있음  생성자  == new shoot()
		t.start();//쓰레드 시작
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

class Enemy {//적군 관련
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