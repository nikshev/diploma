import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import java.util.Vector;
import java.util.*;
import javax.swing.Timer;
import java.io.*;


public class mainWindow extends JFrame implements ActionListener {
 private JPanel  mainPanel;        //Главная панель
 private JPanel  pidPanel;         //Панель ПИД - регулятора
 private JPanel  procPanel;        //Панель процесса
 private JPanel  resultPanel;      //Панель результата
 private JPanel  buttonPanel;      //Панель кнопок
 private JPanel  pidGraph;         //График ПИД регулирования
 private PlastGraph  plastGraph;   //Визуальное отображение пластины
 private RxtxGraph   rxtxGraph;    //Визуальное отображение передачи информации
 private LayerGraph  layerGraph;   //Отображение номера слоя
 private ProcGraphPainter procGraph;  //График процесса
 private JButton startButton;      //Кнопка "СТАРТ"
 private JButton aboutButton;      //Кнопка "О программе"
 private JButton connectButton;    //Кнопка "Соединение"
 private JButton pdfButton;        //Кнопка "Экспорт в PDF" 
 private process proc;             //Класс процесса
 private JComboBox lawChoice;         //Закон регулирования 
 private JComboBox deChoice;          //Выбор диаметра электрода
 private JComboBox modeChoice;        //Выбор режима (пока будет только RTU)
 private JComboBox parityChoice;      //Выбор чётности для RS232
 private JComboBox baudChoice;        //Выбор скорости RS232
 private JComboBox portChoice;        //Выбор Com порта для RS232
 private JTextField kpTextField;   //Коэффициент пропорциональности
 private JTextField tiTextField;   //Постоянная интегрирования
 private JTextField tdTextField;   //Постоянная дифференцирования
 private JTextField toTextField;   //Шаг квантования
 private JTextField lspTextField;  //Задание высоты столба в процентах
 private JTextField uaTextField;   //Напряжение aнода
 private JTextField ukTextField;   //Напряжение катода
 private JTextField idTextField;   //Ток дуги
 private JTextField udTextField;   //Напряжение дуги
 private JTextField tdugTextField; //Температура дуги
 private JTextField adrTextField; //Адресс устройства Modbus
 private JTable     resTable;      //Таблица результатов
 private DefaultTableModel  modelResTable; //Модель по умолчанию
 private Thread procThread; //Поток для запуска процесса
 private PidGraphPainter pidGraphPainter; //Панель для рисования
 private JScrollPane  pidPane; //Полосы прокрутки
 private AboutWindow aboutWindow; //Окно "О программе"
 private Timer timer; //Таймер
 private BaseManager baseManager;
 //Концевики
 private JCheckBox bxCheckBox;   //Начало по оси Х
 private boolean   bxFlag;
 private JCheckBox exCheckBox;   //Конец по оси Х
 private boolean   exFlag;
 private JCheckBox eyCheckBox;   //Конец по оси Y
 private boolean   eyFlag;
 private JCheckBox bzCheckBox;   //Начало по оси Z
 private boolean   bzFlag;
 private JCheckBox ezCheckBox;   //Конец по оси Z
 private boolean   ezFlag;
 private JCheckBox doorCheckBox; //Дверь
 private boolean   doorFlag;
 private PopupMenu menu;
 //Идентификатор наплавки
 long idNap;
 Random randGen;
 //Флаг эмуляции
 boolean emulFlag;
 
 
 //Запуск конструктора
public static void main(String[] args){
  new mainWindow();
 }

 //Конструктор класса mainWindow
 public mainWindow(){
  super ("Атоматическое проектирвание ПИД - регулятора");
  //Устанавливаем стиль окна-Java
  try {
	   UIManager.setLookAndFeel(
	    UIManager.getCrossPlatformLookAndFeelClassName());
	  } catch (Exception e){ 
	   System.out.println("Error setting Java LAF: "+e);
	  }
  //Устанавливаем размер
  setSize(1000,700);
  setResizable(false);  
   //Устанавливаем по центру экрана
  Toolkit toolkit=getToolkit();
  Dimension scrnSize=toolkit.getScreenSize();
  Dimension frameSize=getSize();
  setLocation((scrnSize.width-frameSize.width)/2,
 		     (scrnSize.height-frameSize.height)/2);
  //Панель "ПИД - регулятор"
  pidPanel=new JPanel();
  pidPanel.setBorder(BorderFactory.createTitledBorder("ПИД-регулятор"));
  //pidPanel.setPreferredSize(new Dimension(490,190));
  pidPanel.setBounds(5, 5, 490, 220);
  //Элементы для ПИД - регулятора
  //Выбор закона
  lawChoice = new JComboBox();
  lawChoice.addItem("ПИД - закон");
  lawChoice.addItem("ПИ - закон");
  lawChoice.addItem("П - закон");
  lawChoice.setBounds(355,25, 125, 27);
  lawChoice.setFont(new Font("Arial",Font.BOLD,12));
  JLabel lawLabel=new JLabel("Выбор закона");
  lawLabel.setFont(new Font("Arial",Font.BOLD,12));
  lawLabel.setBounds(270, 22, 100, 27);
  
  //Коэффициент пропорциональности
  kpTextField=new JTextField(5);
  kpTextField.setBounds(355, 55, 125, 27);
  kpTextField.setText("1");
  JLabel kpLabel=new JLabel("Величина Кп");
  kpLabel.setFont(new Font("Arial",Font.BOLD,12));
  kpLabel.setBounds(270, 56, 130, 27);
  
  //Постоянная интегрирования
  tiTextField=new JTextField(5);
  tiTextField.setBounds(355, 85, 125, 27);
  tiTextField.setText("0.01");
  JLabel tiLabel=new JLabel("Величина Tи");
  tiLabel.setFont(new Font("Arial",Font.BOLD,12));
  tiLabel.setBounds(270, 86, 130, 30);
  
  //Постоянная дифференцирования
  tdTextField=new JTextField(5);
  tdTextField.setBounds(355, 115, 125, 27);
  tdTextField.setText("0.01");
  JLabel tdLabel=new JLabel("Величина Тд");
  tdLabel.setFont(new Font("Arial",Font.BOLD,12));
  tdLabel.setBounds(270, 116, 130, 30);
  
  //Шаг квантования
  toTextField=new JTextField(5);
  toTextField.setBounds(355, 145, 125, 27);
  toTextField.setText("0.001");
  JLabel toLabel=new JLabel("Величина Т0");
  toLabel.setFont(new Font("Arial",Font.BOLD,12));
  toLabel.setBounds(270, 146, 130, 30);
 
  //Задние величины столба в процентах
  lspTextField=new JTextField(5);
  lspTextField.setBounds(355, 175, 125, 27);
  lspTextField.setText("40");
  JLabel lspLabel=new JLabel("Высота lsp,%");
  lspLabel.setFont(new Font("Arial",Font.BOLD,12));
  lspLabel.setBounds(270, 176, 130, 30);
  
//Создаём всплывающее меню
  menu=new PopupMenu("popupMenu");
  enableEvents(AWTEvent.MOUSE_EVENT_MASK);
  MenuItem saveToFile=new MenuItem("Сохранить график в файл");
  saveToFile.addActionListener(this);
  menu.add(saveToFile);
  menu.addSeparator();
  MenuItem ExportToPDF=new MenuItem("Экспорт графика в PDF");
  ExportToPDF.addActionListener(this);
  menu.add(ExportToPDF);
  this.add(menu);  
  //Панель для рисования графика ПИД-регулятора
  pidGraphPainter=new PidGraphPainter();
  pidGraphPainter.setPreferredSize(new Dimension(525,150));
  pidGraphPainter.setBackground(Color.black);
 
          
  //Прокрутка для графика
  pidPane=new JScrollPane(pidGraphPainter);
  pidPane.setBounds(10,20,235, 145);
        
  //Панель графика ПИД регулирования
  pidGraph=new JPanel();
  pidGraph.setBorder(BorderFactory.createTitledBorder("График регулирования"));
  pidGraph.setBounds(10, 20, 255, 180);
  pidGraph.setLayout(null);
  pidGraph.add(pidPane);
   //Добавляем на панель ПИД - регулятора
  pidPanel.setLayout(null);
  pidPanel.add(pidGraph);
  pidPanel.add(lawLabel);
  pidPanel.add(lawChoice);
  pidPanel.add(kpTextField);
  pidPanel.add(kpLabel);
  pidPanel.add(tiTextField);
  pidPanel.add(tiLabel);
  pidPanel.add(tdTextField);
  pidPanel.add(tdLabel);
  pidPanel.add(toTextField);
  pidPanel.add(toLabel);
  pidPanel.add(lspTextField);
  pidPanel.add(lspLabel);
   
  //Панель "Процесс"
  procPanel=new JPanel();
  procPanel.setBorder(BorderFactory.createTitledBorder("Процесс"));
  //procPanel.setPreferredSize(new Dimension(490,220));
  procPanel.setBounds(495, 5, 490, 220);
  //Элементы для процесса
  //Выбор диаметра электрода 
  deChoice = new JComboBox();
  deChoice.addItem("d=3мм");
  deChoice.addItem("d=4мм");
  deChoice.addItem("d=5мм");
  deChoice.addItem("d=6мм");
  deChoice.setBounds(355, 25, 125, 27);
  deChoice.setFont(new Font("Arial",Font.BOLD,12));
  JLabel deLabel=new JLabel("Электрод");
  deLabel.setFont(new Font("Arial",Font.BOLD,12));
  deLabel.setBounds(270, 26, 130, 27);
  
  //Напряжение aнода
  uaTextField=new JTextField(5);
  uaTextField.setBounds(355, 55, 125, 27);
  uaTextField.setText("4");
  JLabel uaLabel=new JLabel("Величина Ua");
  uaLabel.setFont(new Font("Arial",Font.BOLD,12));
  uaLabel.setBounds(270, 56, 130, 27);
  
  //Напряжение катода
  ukTextField=new JTextField(5);
  ukTextField.setBounds(355, 85, 125, 27);
  ukTextField.setText("14");
  JLabel ukLabel=new JLabel("Величина Uk");
  ukLabel.setFont(new Font("Arial",Font.BOLD,12));
  ukLabel.setBounds(270, 86, 130, 27);
  
  //Ток дуги
  idTextField=new JTextField(5);
  idTextField.setBounds(355, 115, 125, 27);
  idTextField.setEditable(false);
  JLabel idLabel=new JLabel("Tок дуги Iд");
  idLabel.setFont(new Font("Arial",Font.BOLD,12));
  idLabel.setBounds(270, 116, 130, 30);
  
  //Напряжение дуги
  udTextField=new JTextField(5);
  udTextField.setBounds(355, 145, 125, 27);
  udTextField.setEditable(false);
  JLabel udLabel=new JLabel("Величина Uд");
  udLabel.setFont(new Font("Arial",Font.BOLD,12));
  udLabel.setBounds(270, 146, 130, 30);
  
  //Температура дуги
  tdugTextField=new JTextField(5);
  tdugTextField.setBounds(355, 175, 125, 27);
  tdugTextField.setEditable(false);
  JLabel tdugLabel=new JLabel("tдуги,C");
  tdugLabel.setFont(new Font("Arial",Font.BOLD,12));
  tdugLabel.setBounds(270, 176, 130, 30);
 
  //Панель изображения процесса
  procGraph=new ProcGraphPainter();
  procGraph.setBorder(BorderFactory.createTitledBorder("Изображение процесса"));
  procGraph.setBounds(10, 20, 255, 180);

 //Добавляем на панель процесса
  procPanel.setLayout(null);
  procPanel.add(procGraph);
  procPanel.add(deChoice);
  procPanel.add(deLabel);
  procPanel.add(uaTextField);
  procPanel.add(uaLabel);
  procPanel.add(ukTextField);
  procPanel.add(ukLabel);
  procPanel.add(idTextField);
  procPanel.add(idLabel);
  procPanel.add(udTextField);
  procPanel.add(udLabel);
  procPanel.add(tdugTextField);
  procPanel.add(tdugLabel);
  
  //Панель "Результаты"
  resultPanel=new JPanel();
  resultPanel.setBorder(BorderFactory.createTitledBorder("Результаты"));
  resultPanel.setBounds(5, 420, 980, 200);
  //Таблица результатов
  modelResTable=new DefaultTableModel();
  modelResTable.addColumn("№ п/п");
  modelResTable.addColumn("Величина Id,А");
  modelResTable.addColumn("Величина Ud,В");
  modelResTable.addColumn("Величина ls,М");
  modelResTable.addColumn("Отклонение e");
  modelResTable.addColumn("Воздействие mk,%");
  resTable=new JTable(modelResTable);
  //Устанавливаем ширину столбцов
  resTable.getColumn("№ п/п").setPreferredWidth(5);
  resTable.getColumn("Величина Id,А").setPreferredWidth(resTable.getColumn("Величина Id,А").getPreferredWidth()-15);
  resTable.getColumn("Величина Ud,В").setPreferredWidth(resTable.getColumn("Величина Ud,В").getPreferredWidth()-15);
  resTable.getColumn("Величина ls,М").setPreferredWidth(resTable.getColumn("Величина ls,М").getPreferredWidth()-15);
  resTable.setPreferredScrollableViewportSize(new Dimension(950,135));
  resTable.sizeColumnsToFit(JTable.AUTO_RESIZE_ALL_COLUMNS);
  JScrollPane resPane = new JScrollPane(resTable);
  resPane.setSize(960,195);
  resultPanel.add(resPane);
   //Панель кнопок
  buttonPanel=new JPanel();
  buttonPanel.setPreferredSize(new Dimension(580,50));
  buttonPanel.setBounds(5, 625, 970, 50);
  startButton = new JButton("Старт");
  startButton.addActionListener(this);
  aboutButton = new JButton("О программе");
  aboutButton.addActionListener(this);
  JButton saveButton=new JButton("Сохранить процесс");
  saveButton.addActionListener(this);
  connectButton=new JButton("Connect");
  connectButton.addActionListener(this);
  pdfButton=new JButton("Экспорт результатов в PDF");
  pdfButton.addActionListener(this);
  
  buttonPanel.add(connectButton);
  buttonPanel.add(startButton);
  buttonPanel.add(saveButton);
  buttonPanel.add(pdfButton);
  buttonPanel.add(aboutButton);
  
  //Панель "Камера"
  JPanel camPanel=new JPanel();
  camPanel.setBorder(BorderFactory.createTitledBorder("Камера"));
  camPanel.setBounds(5, 225, 490, 200);
  camPanel.setLayout(null);
  //Добавляем панель отображение пластины
  plastGraph=new PlastGraph();
  plastGraph.setBorder(BorderFactory.createTitledBorder("Пластина"));
  plastGraph.setBounds(10, 20, 255, 165);
  camPanel.add(plastGraph);
  //Добавляем панель отображения слоя
  layerGraph=new LayerGraph();
  layerGraph.setBorder(BorderFactory.createTitledBorder("Cлой"));
  layerGraph.setBounds(290, 110, 180, 75);
  camPanel.add(layerGraph);
   //Флажки состояния концевиков
  //Начало по оси Х
  bxCheckBox=new JCheckBox("Начало Х");
  bxCheckBox.setBounds(290, 20, 75, 30);
  bxCheckBox.addActionListener(this);
  bxFlag=false;
  //Конец по оси Х
  exCheckBox=new JCheckBox("Конец  X");
  exCheckBox.setBounds(385, 20, 75, 30);
  exCheckBox.addActionListener(this);
  exFlag=false;
  //Начало по оси Z
  bzCheckBox=new JCheckBox("Начало Z");
  bzCheckBox.setBounds(290, 50, 75, 30);
  bzCheckBox.addActionListener(this);
  bzFlag=false;
  //Конец по оси Z
  ezCheckBox=new JCheckBox("Конец  Z");
  ezCheckBox.setBounds(385, 50, 75, 30);
  ezCheckBox.addActionListener(this);
  ezFlag=false;
  //Конец по оси Y
  eyCheckBox=new JCheckBox("Конец  Y");
  eyCheckBox.setBounds(290, 80, 75, 30);
  eyCheckBox.addActionListener(this);
  eyFlag=false;
  //Дверь
  doorCheckBox=new JCheckBox("Дверь");
  doorCheckBox.setBounds(385, 80, 75, 30);
  doorCheckBox.addActionListener(this);
  doorFlag=false;
    
  camPanel.add(bxCheckBox);
  camPanel.add(exCheckBox);
  camPanel.add(bzCheckBox);
  camPanel.add(ezCheckBox);
  camPanel.add(eyCheckBox);
  camPanel.add(doorCheckBox);
  //Панель "Связь"
  JPanel conPanel=new JPanel();
  conPanel.setBorder(BorderFactory.createTitledBorder("Связь"));
  conPanel.setBounds(495, 225, 490, 200);
  conPanel.setLayout(null);
  //Добавляем панель отображения передачи данных
  rxtxGraph = new RxtxGraph();
  rxtxGraph.setBorder(BorderFactory.createTitledBorder("Данные"));
  rxtxGraph.setBounds(10, 20, 255, 165);
  conPanel.add(rxtxGraph);
  
  
  //Добавляем элементы управления протоколом связи MODBUS
  //Адресс
  adrTextField=new JTextField(5);
  adrTextField.setBounds(355, 25, 125, 27);
  adrTextField.setText("1");
  JLabel adrLabel=new JLabel("Адрес");
  adrLabel.setFont(new Font("Arial",Font.BOLD,12));
  adrLabel.setBounds(270, 26, 130, 30);
  
 //Выбор режима (пока будет только RTU)
  modeChoice=new JComboBox();
  modeChoice.addItem("RTU");
  modeChoice.setBounds(355, 57, 125, 27);
  modeChoice.setFont(new Font("Arial",Font.BOLD,12));
  JLabel modeLabel=new JLabel("Режим");
  modeLabel.setFont(new Font("Arial",Font.BOLD,12));
  modeLabel.setBounds(270, 57, 130, 30);

  
  //Выбор Com порта для RS232
  portChoice=new JComboBox();     
  portChoice.addItem("COM1");
  portChoice.addItem("COM2");
  portChoice.setBounds(355, 89, 125, 27);
  portChoice.setFont(new Font("Arial",Font.BOLD,12));
  JLabel portLabel=new JLabel("Порт");
  portLabel.setFont(new Font("Arial",Font.BOLD,12));
  portLabel.setBounds(270, 89, 130, 30);
  
  //Выбор стоповых битов для RS232
  baudChoice=new JComboBox();
  baudChoice.addItem("2400");
  baudChoice.addItem("9600");
  baudChoice.addItem("38400");
  baudChoice.addItem("115200");
  baudChoice.setBounds(355, 121, 125, 27);
  baudChoice.setFont(new Font("Arial",Font.BOLD,12));
  JLabel baudLabel=new JLabel("Скорость");
  baudLabel.setFont(new Font("Arial",Font.BOLD,12));
  baudLabel.setBounds(270, 121, 130, 30);
  
  //Выбор чётности для RS232
  parityChoice=new JComboBox();
  parityChoice.addItem("Нечётный");
  parityChoice.addItem("Чётный");
  parityChoice.setBounds(355, 153, 125, 27);
  parityChoice.setFont(new Font("Arial",Font.BOLD,12));
  JLabel parityLabel=new JLabel("Паритет");
  parityLabel.setFont(new Font("Arial",Font.BOLD,12));
  parityLabel.setBounds(270, 153, 130, 30);
        
  conPanel.add(adrTextField);
  conPanel.add(adrLabel);
  conPanel.add(modeChoice);
  conPanel.add(modeLabel);
  conPanel.add(portChoice);
  conPanel.add(portLabel);
  conPanel.add(baudChoice);
  conPanel.add(baudLabel);
  conPanel.add(parityChoice);
  conPanel.add(parityLabel);
  
  //Панель "Главная"
  mainPanel=new JPanel();
  mainPanel.setPreferredSize(new Dimension(990,600));
  mainPanel.setLayout(null);
  mainPanel.add(pidPanel);
  mainPanel.add(procPanel);
  mainPanel.add(camPanel);
  mainPanel.add(conPanel);
  mainPanel.add(resultPanel);
  mainPanel.add(buttonPanel);
  //Добавляем панели
  Container frameCont =getContentPane();
   frameCont.add(mainPanel);
  //Выводим на экран окно
  setVisible(true);

  //Выводим напряжение анода и катода
  procGraph.setUa(Double.parseDouble(uaTextField.getText()));
  procGraph.setUk(Double.parseDouble(ukTextField.getText()));
  procGraph.updateUI();
 //Создаём новый процесс сварки
  proc = new process();
 //Создаём новый поток процесса сварки
  procThread=new Thread(proc,"Process");
 //Создаём таймер для мигания и т.д.
  timer=new Timer(100,this); 
 //Создаём класс для управления базой данных
  baseManager=new BaseManager();
 //Создаём генератор случайных чисел
  idNap=0;
  randGen=new Random(10);
  //Флаг эмуляции
  emulFlag=false;
 }

 //Обработчик событий
 public void actionPerformed(ActionEvent event){
	 boolean error;
     Double vars[]=new Double[7];
  //Нажатие кнопки СТАРТ	 
  if (event.getActionCommand()=="Старт"){
   if (rxtxGraph.getConStat() || emulFlag){	  
    if (!proc.getStartFlag()){
	 //Делаем очистку графиков
	 pidGraphPainter.clearGraph();
	 procGraph.clearGraph();
	 pidGraphPainter.updateUI();
	 procGraph.updateUI();
	 //По таблице определяем делать новый поток или нет
	 if (modelResTable.getRowCount()!=0){
	  //По новой делаем указатель на трэд	 
	  procThread=new Thread(proc,"Process");
	  modelResTable.setRowCount(0);
	  this.update(this.getGraphics());
	 }
	 error=setProcVar();
	 if (!error){
	  while (!baseManager.verifyIdNap(idNap)){	 
	   randGen.setSeed(randGen.nextLong());
	   idNap=randGen.nextLong();
	  }
	  procThread.start();
	 }
	 timer.start();
    }
    else
 	 JOptionPane.showMessageDialog(this,"Процесс уже запущен! Дождитесь пожалуйста окнончания",
   	 		                           "Внимание",JOptionPane.ERROR_MESSAGE);
   }
   else
	JOptionPane.showMessageDialog(this,"Нет связи с МПУ или нет связи с базой! Повторите попытку подключения!",
                   "Внимание",JOptionPane.ERROR_MESSAGE);
  }
  //Нажатие кнопки о программе
  else if (event.getActionCommand()=="О программе"){
	if (aboutWindow==null) 
	 aboutWindow=new AboutWindow("О Программе");
	else
	 aboutWindow.show();	
  }
  
  //Нажатие кнопки Connect
  else if (event.getActionCommand()=="Connect"){
   //Прога будет работать только в режиме эмуляции (пока!!!)
   //Поэтому соединяемся только с базой
   if (!baseManager.connect())
	 JOptionPane.showMessageDialog(this,"Невозможно соединится с сервером! Повторите попытку подключения!",
               "Внимание",JOptionPane.ERROR_MESSAGE);
   else
    {
	 emulFlag=true;
	 rxtxGraph.emulation();
	 rxtxGraph.updateUI();
	 plastGraph.setEmul();
	 plastGraph.updateUI();
 	 connectButton.setText("Disconnect");
    }
  }
  
  //Нажатие кнопки "Disconnect"
  else if (event.getActionCommand()=="Disconnect"){
   if (!proc.getStartFlag()){
	baseManager.disconnect();   
	rxtxGraph.disconnect();
	rxtxGraph.updateUI();
	plastGraph.unsetEmul();
	plastGraph.updateUI();
	emulFlag=false;
    connectButton.setText("Connect");
   }
   else 
	 JOptionPane.showMessageDialog(this,"Дождитесь окончания процесса! Разъединение не возможно",
                                        "Внимание",JOptionPane.ERROR_MESSAGE);   
  }
 //Нажатие кнопки "Сохранить процесс"
  else if (event.getActionCommand()=="Сохранить процесс"){
   if (!proc.getStartFlag()){	 
	if (modelResTable.getRowCount()!=0){
	 if (emulFlag){	
	  //Должна быть проверка на двойников идентификатора наплавки
	  //Сохраняем данные о человеке запустившем процесс
	  baseManager.naplavData(idNap, 1);	
      //Сохраняем данные ПИД - регулятора
      vars[0]=(Double.parseDouble(Integer.toString(proc.getLaw())));
      vars[1]=(Double.parseDouble(Long.toString(idNap)));
      vars[2]=proc.getKp();
      vars[3]=proc.getTi();
      vars[4]=proc.getTdiff();
      vars[5]=proc.getTo();
      vars[6]=proc.getLs()*10;
      baseManager.pidPanelSave(vars);
      //Сохраняем начальные данные процесса
      vars[0]=(Double.parseDouble(Integer.toString(proc.getElek())));
      vars[1]=(Double.parseDouble(Long.toString(idNap)));
      vars[2]=proc.getUa();
      vars[3]=proc.getUk();
      vars[4]=proc.getId();
      vars[5]=proc.getUdNach();
      vars[6]=proc.getTdug();
      baseManager.procPanelSave(vars);
      //Сохраняем результаты процесса
      baseManager.procDataSave(modelResTable.getDataVector());
	 }
	 else
	  JOptionPane.showMessageDialog(this,"Нет связи с базой данных!",
	                    "Внимание",JOptionPane.ERROR_MESSAGE);	
	}
	else
	 JOptionPane.showMessageDialog(this,"Нет результатов для сохранения! Проведите процесс!",
                    "Внимание",JOptionPane.ERROR_MESSAGE);	
   }
   else
	JOptionPane.showMessageDialog(this,"Процесс запущен! Дождитесь пожалуйста окнончания",
	   	 		                           "Внимание",JOptionPane.ERROR_MESSAGE);
  }
  
 //Нажатие кнопки "Экспорт результатов в PDF"
  else if (event.getActionCommand()=="Экспорт результатов в PDF"){
	 System.out.print("Экспорт результатов в PDF");     
  }
  //Нажатия пункта "Сохранить график в файл"
  else if (event.getActionCommand()=="Сохранить график в файл"){
	System.out.print("Сохранить график в файл");   
  }
  // Нажатия пункта "Экспорт графика в PDF"
  else if (event.getActionCommand()=="Экспорт графика в PDF"){
	   System.out.print("Экспорт графика в PDF");   
  }
  else if (event.getActionCommand()=="Timer"){
   if (!proc.getStartFlag()){
    timer.stop();
    layerGraph.numVisible(true);
	layerGraph.updateUI();	
   }
   else {
	if (layerGraph.numIsVisible()){
	 layerGraph.numVisible(false);
	 layerGraph.updateUI();
	}
	else {
	 layerGraph.numVisible(true);
	 layerGraph.updateUI();	
	}
   }
  }
  
  else  { //Иначе обрабатывем событие от флажков
   bxCheckBox.setSelected(bxFlag); //Начало по оси Х  
   exCheckBox.setSelected(exFlag); //Конец по оси Х
   eyCheckBox.setSelected(eyFlag); //Конец по оси Y
   bzCheckBox.setSelected(bzFlag); //Начало по оси Z
   ezCheckBox.setSelected(ezFlag); //Конец по оси Z
   doorCheckBox.setSelected(doorFlag); //Дверь
  }	  
}	 
 
 //Устанавливаем переменные и элемент управления
 private boolean setProcVar(){
   boolean error;
   error=false;	 
   int d; //Диаметр электрода
   double Id; //Ток дуги
   double lsp,ls; //Начальное значение высоты дуги в процентах
   double Ud; //Напряжение дуги
   double Ge=20.139*Math.exp(-29*Math.log(10)); //Сечение столкновения атомов с электронами
   double Ui=7.38; //Потенциал ионизации в парах металла\
   double E; //Напряжённость поля (дуги)
   double temp;
  //Закон регулирования
  proc.setLaw(lawChoice.getSelectedIndex());
  
  //Коэффициент пропорциональности
  try {
    proc.setKp(Double.parseDouble(kpTextField.getText()));
   } catch (NumberFormatException e) {
	error=true;   
	JOptionPane.showMessageDialog(this,"Неправильно заполено поле \"Коэффициент Kп\"","Внимание",
	                              JOptionPane.ERROR_MESSAGE);     
   }

 //Постоянная интегрирования
   if (!error)
   try {
    proc.setTi(Double.parseDouble(tiTextField.getText()));
   } catch (NumberFormatException e) {
	  error=true;
	  JOptionPane.showMessageDialog(this,"Неправильно заполено поле \"Коэффициент Ти\"","Внимание",
                                     JOptionPane.ERROR_MESSAGE);     
   }  
   
 //Постоянная дифференцирования
   if (!error)
   try {
    proc.setTd(Double.parseDouble(tdTextField.getText()));
   } catch (NumberFormatException e) {
	  error=true;
	  JOptionPane.showMessageDialog(this,"Неправильно заполено поле \"Коэффициент Тд\"","Внимание",
                                     JOptionPane.ERROR_MESSAGE);     
   }  

 //Шаг квантования
   if (!error)
   try {
    proc.setTo(Double.parseDouble(toTextField.getText()));
   } catch (NumberFormatException e) {
	  error=true;
	  JOptionPane.showMessageDialog(this,"Неправильно заполено поле \"Коэффициент Т0\"","Внимание",
                                     JOptionPane.ERROR_MESSAGE);     
   }   
   
   
  //Загружаем данные процесса измеренные Id 
  if (!error)
	  proc.setSensorData(loadData(deChoice.getSelectedIndex()));
 // Напряжение Ua
   if (!error)
   try {
    proc.setUa(Double.parseDouble(uaTextField.getText()));
    procGraph.setUa(Double.parseDouble(uaTextField.getText()));
   } catch (NumberFormatException e) {
	  error=true;
	  JOptionPane.showMessageDialog(this,"Неправильно заполено поле \"Величина Ua\"","Внимание",
                                     JOptionPane.ERROR_MESSAGE);     
   }   
//Диаметр электрода
  proc.setElek(deChoice.getSelectedIndex());
  
//Напряжение Uk
   if (!error)
   try {
    proc.setUk(Double.parseDouble(ukTextField.getText()));
    procGraph.setUk(Double.parseDouble(ukTextField.getText()));
   } catch (NumberFormatException e) {
	  error=true;
	  JOptionPane.showMessageDialog(this,"Неправильно заполено поле \"Величина Uk\"","Внимание",
                                     JOptionPane.ERROR_MESSAGE);     
   }   
//Находим ток дуги
  Id=0; 
  lsp=0.10;
  if (!error){
   switch (deChoice.getSelectedIndex()){
 	  case 0: d=3; //Выбран электрод с диаметром 3мм
	          break;
	  case 1: d=4; //Выбран электрод с диаметром 4мм
           break;
	  case 2: d=5; //Выбран электрод с диаметром 5мм
           break;              
	  case 3: d=6; //Выбран электрод с диаметром 6мм
           break;   
      default:d=3; //Выбран электрод с диаметром 3мм
           break;                           
	 }  
   Id=(20+6*d)*d;
   proc.setId(Id);
   idTextField.setText(Double.toString(Id));
 //Высота столба дуги значения с какогого начинается регулирование 
   ls=Double.parseDouble(lspTextField.getText())/10;//Перейдём в мм (10 мм=100%)
   proc.setLs(ls); 
   procGraph.setls(ls/1000);//Переходим в метры
   
  }
//Напряжение дуги Uд (Расчёт и установка)
   if (!error){
	 //Считаем напряженность поля дуги
	 E= 2*Math.exp(8*Math.log(10))*((Math.exp(2.4*Math.log(Ui))*Math.exp(0.3*Math.log(Ge)))/
			 Math.exp(0.3*Math.log(Id)));
	 //Считаем Ud (установка для ПИД регулятора) и устанавливаем
	 Ud=Double.parseDouble(uaTextField.getText())+Double.parseDouble(ukTextField.getText())+E*(lsp*100)/1000;
	 //Окрунгление
	 temp=Math.round(Ud*10000);
	 temp/=10000;
	 Ud=temp;
	 proc.setUd(Ud);
	 procGraph.setUd(Ud);
	 procGraph.updateUI();
	 udTextField.setText(Double.toString(Ud));
	 //Устанавливаем температуру дуги
	 tdugTextField.setText(Double.toString(Ui*800));
	 proc.setTdug(Ui*800);
   }
   
  //Устанавливаем элементы управления
  if (!error){
      //Установка панели для отображения графика ПИД - регулятора
	  proc.setPidGraph(pidGraphPainter);
      //Установка панели для отображения графика процесса
	  proc.setProcGraph(procGraph);
	  //Установка таблицы для отображения результатов
	  proc.setModelResTable(modelResTable);
      // Установка поля для отображения тока дуги
	  proc.SetIdTextField (idTextField);
      // Установка поля для отображения напряжения дуги
	  proc.SetUdTextField (udTextField);
      // Установка поля для отображения температуры дуги
	  proc.SetTdugTextField (tdugTextField);
  }
  return error;
 }
 
 //Загрузка значений тока сварки из файлов
 private Vector loadData(int de){
	 String fileName; //Имя файла
	 String tmpString; //Временная переменная
	 Double vectorData[]; //Временная переменная для вектора
	 DataInputStream InStream;
	 int bufsize; //Размер этого буфера
	 int i;
	 //Выбираем имя файла в зависимости от электрода
	 switch (de){
	  case 0: fileName="./zamer/ztd3"; //Выбран электрод с диаметром 3мм
	          break;
	  case 1: fileName="./zamer/ztd4"; //Выбран электрод с диаметром 4мм
              break;
	  case 2: fileName="./zamer/ztd5"; //Выбран электрод с диаметром 5мм
              break;              
	  case 3: fileName="./zamer/ztd6"; //Выбран электрод с диаметром 6мм
              break;   
      default:fileName="./zamer/ztd3"; //Выбран электрод с диаметром 3мм
              break;                           
	 }
   //Загружаем данные из файла
   Vector dataVector=new Vector(); //Инициализация вектора
   try{
	InStream =new DataInputStream(new BufferedInputStream(new FileInputStream(fileName))); //Открываем файл для чтения
	bufsize=InStream.available();
	i=1;
	//Расчитываем размер массива Double
	while (bufsize!=0 ){
	 tmpString=InStream.readLine();		
	 bufsize-=tmpString.length()+2;
	 i++;
	}
	
	//Делаем перезагрузку потока и считываем строки
	InStream.close();
	InStream =new DataInputStream(new BufferedInputStream(new FileInputStream(fileName))); //Открываем файл для чтения
	bufsize=InStream.available();
	vectorData=new Double[i];
	i=0;
	while (bufsize!=0 ){
     tmpString=InStream.readLine();		
	 vectorData[i].valueOf(tmpString);
	 dataVector.add(tmpString);
	 bufsize-=tmpString.length()+2;
	 i++;
	}
	InStream.close();
   }catch (IOException e) {
	JOptionPane.showMessageDialog(this,"Ошибка открытия файла:"+e.getMessage(),"Внимание",
                                 JOptionPane.ERROR_MESSAGE);     
   }
     
   return dataVector; 
 }

//Метод ожидающий события от мышки
 public void processMouseEvent(MouseEvent event){
  if (event.isPopupTrigger()){
   if (event.getX()<490 && event.getY()<220)		 
    menu.show(this, event.getX(), event.getY());	 
  }

  super.processMouseEvent(event);
 }
 
}
