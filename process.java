import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Vector;
import javax.swing.Timer;
import java.io.*;

public class process implements Runnable{
	private int law; //Закон ПИД - регулирования
	private int elek; //Диаметр электрода
	private double Kp; //Коэффициент пропорциональности
	private double Ti; //Постоянная интегрирования
	private double Td; //Постоянная дифференцирования
	private double To; //Шаг - квантования
	private double lsp; //Расчитанная длина столба 
	private double Ua; //Напряжение на аноде
	private double Uk; //Напряжение на катоде
	private double Id; //Ток дуги
	private double Ud; //Напряжение дуги
	private double Tdug; //Температура дуги
	private double TdugNach; //Температура дуги начальная
	private double Ls;  //Длин столба дуги
	private double Ge=20.139*Math.exp(-29*Math.log(10)); //Сечение столкновения атомов с электронами
	double Ui=7.38; //Потенциал ионизации в парах металла
	private Vector sensorData; //Значения тока сварки снятый с датчика (к примеру:-)
	private boolean start; //Флаг старта процесса
	private PidGraphPainter pidGraphPainter; //Панель графика ПИД - регулятора
	private ProcGraphPainter procGraph; //Панель графика процесса
	private DefaultTableModel  modelResTable; //Модель таблицы для отображения результата
	private JTextField idTextField;   //Ток дуги
    private JTextField udTextField;   //Напряжение дуги
	private JTextField tdugTextField; //Температура дуги	

	//Конструктор
public process(){
	start=false;
 //	Пока нет дуги
	Tdug=0;   
	 
}

//Установка	закона ПИД - регулирования
 public void setLaw(int l){
  if (!start) law=l;	 
 }

//Получение закона ПИД - регулирования
public int getLaw(){
 return law;	
}
 
//Установка коэффициента пропорциональности
 public void setKp(double kp){
  if (!start) Kp=kp;	 
 }

//Получение коэффициента пропорциональности
 public double getKp(){
  return Kp;	 
 } 
 
//Установка постоянной интегрирования
public void setTi(double ti){
 if (!start) Ti=ti;	 
}

//Получение постоянной интегрирования
public double getTi(){
 return Ti;	 
}

//Установка постоянной дифференцирования
public void setTd(double td){
 if (!start) Td=td;	 
}

//Получение постоянной дифференцирования
public double getTdiff(){
 return Td;	 
}

//Установка шага квантования
public void setTo(double to){
 if (!start) To=to;	 
}

//Получение шага квантования
public double getTo(){
 return To;	 
}

//Установка напряжения на аноде
public void setUa(double ua){
 if (!start) Ua=ua;	 
}

//Получение напряжения на аноде
public double getUa(){
 return Ua;	 
}

//Установка напряжения на катоде
public void setUk(double uk){
 if (!start) Uk=uk;	 
}

//Получение напряжения на катоде
public double getUk(){
 return Uk;	 
}

//Установка напряжение дуги
public void setUd(double ud){
 if (!start) Ud=ud;	 
}

//Получение напряжение дуги
public double getUdNach(){
 return Ud;	 
}

//Установка тока дуги (нужное)
public void setId(double id){
 if (!start) Id=id;	 
}

//Получение тока дуги (нужное)
public double getId(){
 return Id;	 
}

//Установка высоты столба дуги(начальное)
public void setLs(double ls){
 if (!start) Ls=ls;	 
}

//Получение высоты столба дуги(начальное)
public double getLs(){
  return Ls;	 
}

//Получения напряжения дуги
public double getUd(){
 return Ud;	
}

//Получения температуры дуги
public double getTd(){
 return Td;	
}

//Установка диаметра электрода
public void setElek(int e){
 if (!start) elek=e;	 
}

//Получение диаметра электрода
public int getElek(){
  return elek;	 
}

//Установка температуры дуги(начальное)
public void setTdug(double td){
 if (!start) TdugNach=td;	 
}

//Получение температуры дуги(начальное)
public double getTdug(){
  return TdugNach;	 
}

//Установка значений тока сварки
public void setSensorData(Vector sd){
 if (!start) sensorData=sd;
}

//Установка панели для отображения графика ПИД - регулятора
public void setPidGraph(PidGraphPainter pgp){
 if (!start) pidGraphPainter=pgp;	
}

//Установка панели для отображения графика процесса
public void setProcGraph(ProcGraphPainter ProcGraph){
 if (!start) procGraph=ProcGraph;	
}

//Установка таблицы для отображения результатов
public void setModelResTable(DefaultTableModel ModelResTable){
 if (!start) modelResTable=ModelResTable;	
}

//Установка поля для отображения тока дуги
public void SetIdTextField (JTextField IdTextField){
 if (!start)	idTextField=IdTextField;
}

//Установка поля для отображения напряжения дуги
public void SetUdTextField (JTextField UdTextField){
 if (!start) udTextField = UdTextField;	
}

//Установка поля для отображения температуры дуги
public void SetTdugTextField (JTextField TdugTextField){
 if (!start) tdugTextField=TdugTextField; 	
}

//Просмотреть флаг Старта
public boolean getStartFlag(){
 return start;	
}

//Старт процесса
public void run(){
 int bufsize,i;
 String str;
 double ek,ek1,ek2; //Отклонения 
 double mk, mk1; //Управляющее воздействие
 double Udr; //Рассчитанное напряжение дуги
 double E; //Рассчитанная напряженность
 double q0,q1,q2; //Коэффициенты
 String resRow[]=new String[6];
 start=true;
 
 //Устанавливаем начальные данные
 ek=0; ek1=0; ek2=0;
 mk=0;mk1=0;
 lsp=Ls/10;
 pidGraphPainter.ustanovka(Ud,To);
 pidGraphPainter.updateUI();
 
 //Расчёт коэффициентов q
 q0=Kp*(1+(Td/To)); q1=-Kp*(1+2*(Td/To)-(To/Ti)); q2=Kp*(Td/To);
 //Вытаскиваем размер буффера
 bufsize=sensorData.size();
 try{
  for (i=0; i<bufsize; i++){
	//Берём ток дуги  
	str=sensorData.get(i).toString();
	Id=Double.parseDouble(str);
	//Рассчитываем напряжённость
	 E= 2*Math.exp(8*Math.log(10))*((Math.exp(2.4*Math.log(Ui))*Math.exp(0.3*Math.log(Ge)))/
			 Math.exp(0.3*Math.log(Id)));
	 //Считаем Ud  
	 Udr=Ua+Uk+E*(lsp*100)/1000;
	 //Cчитаем отклонение
	 ek=Ud-Udr;

	 //Расчёт управляющего воздействия
	 //в зависимости от закона
	 switch (law){
	  case 2: mk=mk1+Kp*ek; break; //П - регулирование
	  case 1: mk=mk1+Kp*ek+(To/Ti)*ek1; break; //ПИ - регулирование
	  case 0: mk=mk1+q0*ek+q1*ek1+q2*ek2; break; //ПИД - регулирование
	 }
	 
	 //Расчёт длины столба в процентах
	 lsp+=mk/100;
	 //Отображение всех данных в таблице
	 resRow[0]=Integer.toString(i); //Номер по порядку
	 resRow[1]=Double.toString(Id); //Ток дуги
	 resRow[2]=Double.toString(Udr); //Расчитанное напряжение дуги
	 if (lsp>0) 
	  resRow[3]=Double.toString((lsp*100)/10000); //Длина столба в метрах
	 else 
	  resRow[3]=Double.toString(-1*(lsp*100)/10000); //Длина столба в метрах
	 resRow[4]=Double.toString(ek); //Отклонение
	 resRow[5]=Double.toString(mk); //Управляющее воздействие
	 modelResTable.insertRow(i, resRow);
	 
	 //Выводим на график ПИД-регулятора
	 pidGraphPainter.addPoints(i, Udr);
	 pidGraphPainter.updateUI();
	 //Выводим на график процесса
	 procGraph.setUd(Udr);
	 procGraph.setls(Double.parseDouble(resRow[3]));
	 procGraph.updateUI();
	 //Делаем смещение
	 mk1=mk; mk=0;
	 ek2=ek1; ek1=ek;ek=0;
	 //Засыпаем
  	Thread.sleep((long)To*1000);
  }
  } catch (InterruptedException e){
	  System.out.print("Да уж.. хреново!");  
  }
  start=false; 

}

}
