import javax.swing.*;
import java.awt.*;

public class ProcGraphPainter extends JPanel{
  private double Uk; //Падение напряжения катода
  private double Ua; //Падение напряжения анода
  private double Ud; //Напряжение дуги
  private double Ls; //Высота дуги
  
  //Конструктор
  public ProcGraphPainter(){
	Uk=0;
	Ua=0;
	Ud=0;
	Ls=0;
  }
  
  //Изображение процесса
  public void paint (Graphics g){
   super.paint(g);	 
   double temp;
   int[] xP={90,70,130,110}; //Точки вершин трапеции по X
   int[] yP={85,147,147,85}; //Точки вершин трапеции по Y
   g.setColor(Color.black); 
   g.fillRect(10, 20, 235, 150);
   //Рисуем катод
   g.setColor(Color.green);
   g.drawRect(90, 25, 20, 60);
   g.drawLine(100, 45, 130, 35);
   g.drawString("Катод:Uk="+Double.toString(Uk)+"В",135, 35);
   //Рисуем анод
   g.drawRect(70, 147,60, 20);
   g.drawLine(115, 157,150, 157);
   g.drawString("Анод:Ua="+Double.toString(Ua)+"В",155, 160);
   //Рисуем дугу
   g.fillPolygon(xP, yP, 4);
   g.drawLine(115, 116,145, 106);
   g.drawString("Дуга:",150, 93);
   temp=Math.round(Ud*10000);
   temp/=10000;
   g.drawString("Ud="+temp+"В",150, 106);
   temp=Math.round(Ls*100000);
   temp/=100000;
   g.drawString("ls="+Double.toString(temp)+"м",150, 119);
   
  }
  
public void setUk(double uk){
 Uk=uk;	
}

public void setUa(double ua){
 Ua=ua;	
}

public void setUd(double ud){
 Ud=ud;	
}

public void setls(double ls){
 Ls=ls;	
}

//Метод очищает график процесса
public void clearGraph(){
	Uk=0;
	Ua=0;
	Ud=0;
	Ls=0;		
}

}
