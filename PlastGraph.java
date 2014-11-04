import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class PlastGraph extends JPanel{
 private long X;
 private long Y;
 private long Z;
 private boolean emulFlag;
 //Конструктор
 public PlastGraph(){
  emulFlag=false;	 
 }
 
 //Отображение
 public void paint (Graphics g){
  super.paint(g);	
  String tmpStr;
  int [] x={90,220,180,50,90};
  int [] y={30,30,130,130,30};
  g.setColor(Color.black); 
  g.fillRect(10, 20, 235, 135);
  g.setColor(Color.green);
  if (!emulFlag){
   //Рисуем ось Y
   g.drawLine(20, 22, 20, 140); //Линия
   g.drawLine(20, 22, 17, 30);  //Левая стрелка
   g.drawLine(20, 22, 23, 30);  //Правая стрелка
   g.drawString("Y",25,30);     //Подпись оси
   //Рисуем ось X
   g.drawLine(20, 140, 230, 140);  //Линия
   g.drawLine(230, 140, 220, 137); //Левая стрелка
   g.drawLine(230, 140, 220, 143); //Правая стрелка
   g.drawString("X",230,138);      //Подпись оси
   //Рисуем ось Z
   g.drawLine(20, 140, 64, 22);    //Линия
   g.drawLine(64, 22, 55, 30);   //Левая стрелка
   g.drawLine(64, 22, 67, 30); //Правая стрелка
   g.drawString("Z",70,30);      //Подпись оси
   //Рисуем саму пластину
   g.drawPolygon(x, y, 5);
   //Рисуем линии и выводим координаты
   if (X!=0 && Z!=0){
    tmpStr="X:"+Long.toString(X)+"; "+"Y:"+Long.toString(Y)+"; "+ "Z:"+Long.toString(Z)+"; ";
    g.drawString(tmpStr, (int)((180-tmpStr.length())/2), 150);
    //Единоразово рисуем линии
    g.setColor(Color.blue);
    g.fillRect(60, 115, 120, 5);
    g.fillRect(63, 105, 120, 5);
    g.fillRect(67, 95, 120, 5);
    g.fillRect(72, 85, 120, 5);
    g.fillRect(76, 75, 120, 5);
    g.fillRect(81, 65, 120, 5);
    g.fillRect(127, 55, 80, 5);
   }
   else {
    tmpStr="X:NA; Y:NA; Z:NA";
    g.drawString(tmpStr, (int)((180-tmpStr.length())/2), 150);	  
   }
  }
  else 
   g.drawString("РЕЖИМ ЭМУЛЯЦИИ!", 70, 85);  
	  
}

//Устанавливаем координату Х
public void setX(long x){
 X=x; 	
}

//Устанавливаем координату Y
public void setY(long y){
 Y=y;	
}

//Устанавливаем координату Y
public void setZ(long z){
 Z=z;	
}

//Установить режим эмуляции
public void setEmul(){
 emulFlag=true;	
}

//Отключить режим эмуляции
public void unsetEmul(){
 emulFlag=false;	
}

}
