import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class LayerGraph extends JPanel{
  private int layer;
  private boolean NumOn;
	
 //Конструктор
 public LayerGraph(){
   layer=1;
   NumOn=true;
 }
	
//	Отображение
 public void paint (Graphics g){
  super.paint(g);
  int i;
  g.setColor(Color.black);
  //Рисуем первый квадрат
  g.draw3DRect(10, 20, 20, 20, false);
  g.setColor(Color.blue);
  g.fillRect(11, 21, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==1))
   g.drawString("1", 17, 34);
  //Рисуем второй квадрат   
  g.draw3DRect(38, 20, 20, 20, false);
  g.setColor(Color.cyan);
  g.fillRect(39, 21, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==2))
   g.drawString("2", 45, 34);
  //Рисуем третий квадрат   
  g.draw3DRect(66, 20, 20, 20, false);
  g.setColor(Color.gray);
  g.fillRect(67, 21, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==3))
   g.drawString("3", 73, 34);
  //Рисуем четвёртый квадрат   
  g.draw3DRect(94, 20, 20, 20, false);
  g.setColor(Color.green);
  g.fillRect(95, 21, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==4))
   g.drawString("4", 101, 34);
  //Рисуем пятый квадрат   
  g.draw3DRect(122, 20, 20, 20, false);
  g.setColor(Color.magenta);
  g.fillRect(123, 21, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==5))
   g.drawString("5", 129, 34);
  //Рисуем шестой квадрат   
  g.draw3DRect(150, 20, 20, 20, false);
  g.setColor(Color.orange);
  g.fillRect(151, 21, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==6))
   g.drawString("6", 157, 34);
  //Рисуем седьмой квадрат
  g.draw3DRect(10, 45, 20, 20, false);
  g.setColor(Color.pink);
  g.fillRect(11, 46, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==7))
   g.drawString("7", 17, 60);
  //Рисуем восьмой квадрат   
  g.draw3DRect(38, 45, 20, 20, false);
  g.setColor(Color.red);
  g.fillRect(39, 46, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==8))
   g.drawString("8", 45, 60);
  //Рисуем девятый квадрат   
  g.draw3DRect(66, 45, 20, 20, false);
  g.setColor(Color.white);
  g.fillRect(67, 46, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==9))
   g.drawString("9", 73, 60);
  //Рисуем десятый квадрат   
  g.draw3DRect(94, 45, 20, 20, false);
  g.setColor(Color.yellow);
  g.fillRect(95, 46, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==10))
   g.drawString("10", 98, 60);
  //Рисуем одинатцатый квадрат   
  g.draw3DRect(122, 45, 20, 20, false);
  g.setColor(Color.lightGray);
  g.fillRect(123, 46, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==11))
   g.drawString("11", 126, 60);
  //Рисуем двенадцатый квадрат
  g.draw3DRect(150, 45, 20, 20, false);
  g.setColor(Color.darkGray);
  g.fillRect(151, 46, 19, 19);
  g.setColor(Color.black);
  if (!(!NumOn && layer==12))
   g.drawString("12", 154, 60);
 }

 //При помощи этого метода отключается отображение номера слоя
 public void numVisible(boolean vis) { 
	NumOn=vis;
 }

 //Получения флага видимости
 public boolean numIsVisible(){
  return NumOn;	 
 }
 
 //Смена слоя
 public void changeLayer(){
  if (layer<12)
	 layer++;
  else 
   layer=1;
 }

 
 
}
