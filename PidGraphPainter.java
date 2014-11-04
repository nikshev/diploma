import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class PidGraphPainter extends JPanel {
	private Vector vX;
	private Vector vY;
	private double mashtabX,mashtabY; //Масштабы по оси Х,Y
	private double beginX,beginY; //Начало координат Х,Y
	private double UstX,UstY; //Кординаты точки установки
	private double Ud_ust; //Точка установки
	private double T;//Шаг квантования
	
	
//Конструктор
public PidGraphPainter(){
	 vX=new Vector();
	 vY=new Vector();
	 beginY=130/2; //Начало координат Y
	 beginX=28; //Начало координат Х
	 mashtabX=(512-beginX)/530;//Масштабы по оси Х
	 mashtabY=130; //Масштабы по оси Y (единица от 17.5 до 18.5)
     //Кординаты точки установки 
	 UstX=-1;
     UstY=-1;
}

//Изображение процесса
 public void paint (Graphics g){
  double temp;	
  String str;
  double X,Y;
  Double X1,Y1;
  int i,pointsize;
  //Устанавливаем чёрный цвет (фона)
  g.setColor(Color.black); 
  g.fillRect(0, 0, 525, 150); 
  //Цвет пера - зелёный
  g.setColor(Color.green);
  //Рисуем ось Y
  g.drawLine((int)beginX, 2, (int)beginX, 130); //Линия
  g.drawLine((int)beginX, 2, (int)beginX-3, 10); //Левая стрелка
  g.drawLine((int)beginX, 2, (int)beginX+3, 10); //Правая стрелка
  g.drawString("Uд,В", (int)beginX+6, 11); //Подпись оси
  //Рисуем ось X
  g.drawLine((int)beginX,130, 520, 130); //Линия
  g.drawLine(512,127, 520, 130); //Верхняя стрелка
  g.drawLine(512,133, 520, 130); //Нижняя стрелка
  g.drawString("t,c", 510, 125); //Подпись оси
  //Рисуем ось точки установки
  if (UstX!=-1 && UstY!=-1){
   g.setColor(Color.red);
   g.drawLine((int)UstX,(int)UstY, 520, (int)UstY); //Линия
   g.drawLine(512,(int)UstY-3, 520, (int)UstY); //Верхняя стрелка
   g.drawLine(512,(int)UstY+3, 520, (int)UstY); //Нижняя стрелка
   g.drawString("Uд.уст", 485, (int)UstY-5); //Подпись оси
   //Наносим насечки по оси Y
   g.setColor(Color.green);
   g.drawLine((int)UstX,(int)UstY, (int)UstX-4, (int)UstY); //Линия насечки (значение установки)
   g.drawString(Double.toString(Ud_ust), 0, (int)UstY+3); //Подпись насечки (значение установки)
   temp=mashtabY;
   g.drawLine((int)UstX,(int)temp, (int)UstX-4, (int)temp); //Линия насечки (значение установки-0.5)
   g.drawString(Double.toString(Ud_ust-0.5), 0, (int)temp+3); //Подпись насечки (значение установки-0.5)
   temp=mashtabY-122;
   g.drawLine((int)UstX,(int)temp, (int)UstX-4, (int)temp); //Линия насечки (значение установки+0.5)
   g.drawString(Double.toString(Ud_ust+0.5), 0, (int)temp+3); //Подпись насечки (значение установки+0.5)
   //Рисуем насечки на оси X
   for (i=0; i<6; i++){
	g.drawLine((int)(beginX+i*106*mashtabX), 130, (int)(beginX+i*106*mashtabX), 130+4); //Линия насечки 
	g.drawString(Double.toString(T*i*106), (int)(beginX+i*106*mashtabX)-15, 130+15); //Подпись насечки (значение установки)
   }
   //Выдаём точки
   g.setColor(Color.gray);
   pointsize=vX.size();
   
   if (pointsize>0){
   for (i=0; i<pointsize; i++){
	  X1=(Double)vX.elementAt(i);
	  Y1=(Double)vY.elementAt(i);
	  g.fillOval((int)X1.intValue(), (int)Y1.intValue(), 3, 3);
	 }
   }
  }
  
 }

//Метод добавляет точки в вектор
public void addPoints(double x,double y){
 double temp;
 Double temp1;
  temp=(mashtabX*x)+beginX; //По X вроде бы все просто
  temp1=new Double(temp);
  vX.add(temp1);

 //А вот по Y:-)
 temp=Ud_ust-y; //Смотрим отклонение от начала координат
 temp*=mashtabY; //Умножаем на масштаб Y
 temp+=beginY;//Прибавляем начало координат относительных
 temp1=new Double(temp);
 vY.add(temp1); //Получаем координаты абсолютные
}

//Метод добавляет точку установки и шаг квантования
public void ustanovka(double u, double t){
 UstX=beginX;
 UstY=beginY;
 Ud_ust=u;
 T=t;
}

//Метод очищает график ПИД - регулирования
public void clearGraph(){
	UstX=-1;
	UstY=-1;
	vX.clear();
	vY.clear();
}

}






