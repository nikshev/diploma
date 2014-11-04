import java.awt.*;
import javax.swing.*;


public class AboutWindow extends JFrame {
	
	public AboutWindow(String title) {
		super(title);
		// TODO Auto-generated constructor stub
		confAndShow();
	}
	
	public AboutWindow(String title, GraphicsConfiguration gc) {
		super(title, gc);
		// TODO Auto-generated constructor stub
		confAndShow();
	}
    
	public void confAndShow(){
    //Устанавливаем стиль окна-Java
	 try {
	      UIManager.setLookAndFeel(
		  UIManager.getCrossPlatformLookAndFeelClassName());
	 } catch (Exception e){ 
	  System.out.println("Error setting Java LAF: "+e);
	 }
	//Устанавливаем размер
	setSize(500,200);
	setResizable(false);
		
	//Устанавливаем по центру экрана
	Toolkit toolkit=getToolkit();
    Dimension scrnSize=toolkit.getScreenSize();
	Dimension frameSize=getSize();
	setLocation((scrnSize.width-frameSize.width)/2,
	            (scrnSize.height-frameSize.height)/2);
		  
    JPanel mainPanel=new JPanel();
	mainPanel.setPreferredSize(new Dimension(500,200));
	JLabel fLabel=new JLabel("\"Автоматизированное проектирование ПИД - регулятора\"");
	fLabel.setFont(new Font("Arial",Font.BOLD,14));
	JLabel sLabel=new JLabel("выполнил студент ДНТУ группы ЭЛСс-07з");
	sLabel.setFont(new Font("Arial",Font.BOLD,14));
	JLabel tLabel=new JLabel("Шкурников Е.В.");
	tLabel.setFont(new Font("Arial",Font.BOLD,14));
	JLabel foLabel=new JLabel("Руководитель: д.т.н Чичикало Н.И.");
	foLabel.setFont(new Font("Arial",Font.BOLD,14));
	mainPanel.add(fLabel);
	mainPanel.add(sLabel);
	mainPanel.add(tLabel);
	mainPanel.add(foLabel);
    //Добавляем панель
	Container frameCont =getContentPane();
	frameCont.add(mainPanel);
	//Выводим на экран окно
	setVisible(true);
    }
	
 }
