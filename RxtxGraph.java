import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RxtxGraph extends JPanel{
  private boolean conFlag;
  private boolean emulFlag;
  private long rxBytes;
  private long txBytes;
 //Конструктор
 public RxtxGraph(){
  conFlag=false;
  emulFlag=false;
 }
 
 //	Отображение
 public void paint (Graphics g){
  super.paint(g);
  g.setColor(Color.black); 
  g.fillRect(10, 20, 235, 135);
  //Рисуем PC
  g.setColor(Color.green);
  g.fillRect(10, 20, 50, 135);
  g.setColor(Color.black);
  g.drawString("ПЭВМ",18,85);
  //Рисуем MPU
  g.setColor(Color.green);
  g.fillRect(195, 20, 50, 135);
  g.setColor(Color.black);
  if (!emulFlag)
   g.drawString("МПУ",205,85);
  else
   g.drawString("СУБД",200,85);	   
  //Если есть связь выводим линии RX,TX
  if (conFlag || emulFlag){
   //Rx линия
   g.setColor(Color.red);
   g.drawLine(60, 50, 110, 50);
   g.drawLine(110, 50, 140, 100);
   g.drawLine(140, 100, 195, 100);
   g.setColor(Color.green);
   if (!emulFlag){
    g.drawString("RX:"+Long.toString(rxBytes), 62, 45);
    g.drawString("TX:"+Long.toString(rxBytes), 145, 95);
   }
   //Tx линия
   g.setColor(Color.blue);
   g.drawLine(60, 100, 110, 100);
   g.drawLine(110, 100, 140, 50);
   g.drawLine(140, 50, 195, 50);
   g.setColor(Color.green);
   if (!emulFlag){
    g.drawString("RX:"+Long.toString(rxBytes), 145, 45);
    g.drawString("TX:"+Long.toString(rxBytes), 65, 95);
   }
  }
  else {
   g.setColor(Color.green);
   g.drawString("НЕТ СВЯЗИ!", 90, 85);
  }
   
 }
 
 //Метод устанавливает флаг связи
 public void connect (){
  conFlag=true;	 
 }
 
 public void emulation(){
  emulFlag=true;	 
 }

 //Метод добавляет байты передачи
 public void addTxBytes(int bytes){
  txBytes+=bytes; 	 
 }
 
//Метод добавляет байты приёма
 public void addRxBytes(int bytes){
  rxBytes+=bytes; 	 
 }
 
//Метод очищает данные (разъединение)
 public void disconnect(){
  conFlag=false;
  emulFlag=false;
  rxBytes=0;
  txBytes=0;	 
 }

//Метод возвращает статус соединения
 public boolean getConStat(){
  return conFlag;	 
 }
 
}
