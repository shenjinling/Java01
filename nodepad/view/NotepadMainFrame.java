package nodepad.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.PrintJob;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.undo.UndoManager;

import nodepad.util.Clock;
import nodepad.util.MQFontChooser;
import nodepad.util.SystemParam;
import nodepad.util.TestLine;

public class NotepadMainFrame extends JFrame implements ActionListener{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 8585210209467333480L;
    private JPanel contentPane;
    private JTextArea textArea;
    private JMenuItem itemOpen,itemSave;   
        
    int flag=0;//1：新建 //2：修改过 //3：保存过的   
    String currentFileName=null; //当前文件名
    String currentPath=null;//当前文件路径
    
     PrintJob  p=null;//声明一个要打印的对象
     Graphics  g=null;//要打印的对象 
        
    JColorChooser jcc1=null;//背景颜色
    Color color=Color.BLACK;
     
    int linenum = 1; //文本的行数和列数
    int columnnum = 1;
       
    public UndoManager undoMgr = new UndoManager(); //撤销管理器
 
    public Clipboard clipboard = new Clipboard("系统剪切板");  //剪贴板
    
    private JMenuItem itemSaveAs;
    private JMenuItem itemNew;
    private JMenuItem itemPage;
    private JSeparator separator;//是一个水平 或垂直  线 或一个空的空间分隔的组件
    private JMenuItem itemPrint;
    private JMenuItem itemExit;
    private JSeparator separator_1;
    private JMenu itemEdit;
    private JMenu itFormat;
    private JMenu itemCheck;
    private JMenu itemHelp;
    private JMenuItem itemSearchForHelp;
    private JMenuItem itemAboutNotepad;
    private JMenuItem itemUndo;
    private JMenuItem itemCut,itemCopy,itemPaste;
    private JMenuItem itemDelete;
    private JMenuItem itemFind,itemFindNext,itemReplace;
    private JMenuItem itemTurnTo;
    private JMenuItem itemSelectAll;
    private JMenuItem itemTime;
    private JMenuItem itemFont,itemColor,itemFontColor;
    private JCheckBoxMenuItem itemNextLine;
    private JScrollPane scrollPane;
    private JCheckBoxMenuItem itemStatement;//表示可以包含在菜单中的复选框。
    private JToolBar toolState;//JToolBar提供了可用于显示常用Actions或控件的组件。
    public static JLabel label1;
    private JLabel label2,label3;
    int length=0;
    int sum=0;    
    
    //Launch the application.启动应用程序  
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    NotepadMainFrame frame = new NotepadMainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    //GregorianCalendar 是 Calendar 的一个具体子类，提供了世界上大多数国家/地区使用的标准日历系统。 
    GregorianCalendar c=new GregorianCalendar();   
    int hour=c.get(Calendar.HOUR_OF_DAY);//时
    int min=c.get(Calendar.MINUTE);//分
    int second=c.get(Calendar.SECOND);//秒    
    private JPopupMenu popupMenu;//弹出菜单表示可以在组件内的指定位置动态弹出的菜单。
    private JMenuItem popM_Undo;
    private JMenuItem popM_Cut,popM_Copy,popM_Paste;
    private JMenuItem popM_Delete;
    private JMenuItem popM_SelectAll;
    private JMenuItem popM_toLeft;
    private JMenuItem popM_showUnicode;
    private JMenuItem popM_closeIMe;
    private JMenuItem popM_InsertUnicode;
    private JMenuItem popM_RestartSelect;
    private JSeparator separator_2,separator_3,separator_4,separator_5;
    private JMenuItem itemRedo;
    private JSeparator separator_6,separator_7,separator_8;
    private JMenuItem popM_Redo;

    //Create the frame.    
    public NotepadMainFrame() {
        try {
          // 使用UIManager.setLookAndFeel通过编程来指定界面外观
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());         
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        setTitle("无标题");    
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 521, 572);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu itemFile = new JMenu("文件(F)");
        itemFile.setMnemonic('F');
        menuBar.add(itemFile);
        
        itemNew = new JMenuItem("新建(N)",'N');
        itemNew.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
                java.awt.Event.CTRL_MASK));  
        itemNew.addActionListener(this);
        itemFile.add(itemNew);
        
        itemOpen = new JMenuItem("打开(O)",'O');
        itemOpen.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
                            java.awt.Event.CTRL_MASK));  
        itemOpen.addActionListener(this);
        itemFile.add(itemOpen);
        
        itemSave = new JMenuItem("保存(S)");
        itemSave.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.Event.CTRL_MASK));   
        itemSave.addActionListener(this);
        itemFile.add(itemSave);
        
        itemSaveAs = new JMenuItem("另存为(A)");
        itemSaveAs.addActionListener(this);
        itemFile.add(itemSaveAs);
        
        separator = new JSeparator();
        itemFile.add(separator);
        
        itemPage = new JMenuItem("页面设置(U)",'U');
        itemPage.addActionListener(this);
        itemFile.add(itemPage);
        
        itemPrint = new JMenuItem("打印(P)...",'P');
        itemPrint.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P,
                java.awt.Event.CTRL_MASK));   
        itemPrint.addActionListener(this);
        itemFile.add(itemPrint);
        
        separator_1 = new JSeparator();
        itemFile.add(separator_1);
        
        itemExit = new JMenuItem("退出(X)",'X');
        itemExit.addActionListener(this);
        itemFile.add(itemExit);
        
        itemEdit = new JMenu("编辑(E)");
        itemEdit.setMnemonic('E');
        menuBar.add(itemEdit);
        
        itemUndo = new JMenuItem("撤销(U)",'U');
        itemUndo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z,
                java.awt.Event.CTRL_MASK));
        itemUndo.addActionListener(this);
        itemEdit.add(itemUndo);
        
        itemRedo = new JMenuItem("恢复(R)");
        itemRedo.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R,
                java.awt.Event.CTRL_MASK));
        itemRedo.addActionListener(this);
        itemEdit.add(itemRedo);
        
        itemCut = new JMenuItem("剪切(T)",'T');
        itemCut.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
                java.awt.Event.CTRL_MASK));
        itemCut.addActionListener(this);
        
        separator_6 = new JSeparator();
        itemEdit.add(separator_6);
        itemEdit.add(itemCut);
        
        itemCopy = new JMenuItem("复制(C)",'C');
        itemCopy.addActionListener(this);
        itemCopy.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
                java.awt.Event.CTRL_MASK));
        itemEdit.add(itemCopy);
        
        itemPaste = new JMenuItem("粘贴(P)",'P');
        itemPaste.addActionListener(this);
        itemPaste.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
                java.awt.Event.CTRL_MASK));
        itemEdit.add(itemPaste);
        
        itemDelete = new JMenuItem("删除(L)",'L');
        itemDelete.addActionListener(this);
        itemDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,  
                InputEvent.CTRL_MASK));  
        itemEdit.add(itemDelete);
        
        separator_7 = new JSeparator();
        itemEdit.add(separator_7);
        
        itemFind = new JMenuItem("查找(F)",'F');
        itemFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                Event.CTRL_MASK));
        itemFind.addActionListener(this);
        itemEdit.add(itemFind);
        
        itemFindNext = new JMenuItem("查找下一个(N)",'N');
        itemFindNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
        itemFindNext.addActionListener(this);
        itemEdit.add(itemFindNext);
        
        itemReplace = new JMenuItem("替换(R)",'R');
        itemReplace.addActionListener(this);
        itemReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                Event.CTRL_MASK));
        itemEdit.add(itemReplace);
        
        itemTurnTo = new JMenuItem("转到(G)",'G');
        itemTurnTo.addActionListener(this);
        itemTurnTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                Event.CTRL_MASK));
        itemEdit.add(itemTurnTo);
        
        itemSelectAll = new JMenuItem("全选(A)",'A');
        itemSelectAll.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A,
                java.awt.Event.CTRL_MASK));
        itemSelectAll.addActionListener(this);
        
        separator_8 = new JSeparator();
        itemEdit.add(separator_8);
        itemEdit.add(itemSelectAll);
        
        itemTime = new JMenuItem("时间/日期(D)",'D');
        itemTime.addActionListener(this);
        itemTime.setAccelerator(KeyStroke.getKeyStroke("F5"));
        itemEdit.add(itemTime);
        
        itFormat = new JMenu("格式(O)");
        itFormat.setMnemonic('O');
        menuBar.add(itFormat);
        
        itemNextLine = new JCheckBoxMenuItem("自动换行(W)");
        itemNextLine.addActionListener(this);
        itFormat.add(itemNextLine);
        
        itemFont = new JMenuItem("字体大小(F)...");
        itemFont.addActionListener(this);
        itFormat.add(itemFont);
        
        itemColor = new JMenuItem("背景颜色(C)...");
        itemColor.addActionListener(this);
        itFormat.add(itemColor);
        
        itemFontColor = new JMenuItem("字体颜色(I)...");
        itemFontColor.addActionListener(this);
        itFormat.add(itemFontColor);
        
        itemCheck = new JMenu("查看(V)");
        itemCheck.setMnemonic('V');
        menuBar.add(itemCheck);
        
        itemStatement = new JCheckBoxMenuItem("状态栏(S)");
        itemStatement.addActionListener(this);
        itemCheck.add(itemStatement);
        
        itemHelp = new JMenu("帮助(H)");
        itemHelp.setMnemonic('H');
        menuBar.add(itemHelp);
        
        itemSearchForHelp = new JMenuItem("查看帮助(H)",'H');
        itemSearchForHelp.addActionListener(this);
        itemHelp.add(itemSearchForHelp);
        
        itemAboutNotepad = new JMenuItem("关于记事本(A)",'A');
        itemAboutNotepad.addActionListener(this);
        itemHelp.add(itemAboutNotepad);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));        
        contentPane.setLayout(new BorderLayout(0, 0));//设置边框布局
        setContentPane(contentPane);
        
        textArea = new JTextArea();
                
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                                     JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//VERTICAL垂直    HORIZONTAL水平        
        TestLine view = new TestLine();
        scrollPane.setRowHeaderView(view);
        
        popupMenu = new JPopupMenu();
//        addPopup(textArea, popupMenu);
        
        popM_Undo = new JMenuItem("撤销(U)");
        popM_Undo.addActionListener(this);
        popupMenu.add(popM_Undo);
        
        popM_Redo = new JMenuItem("恢复(R)");
        popM_Redo.addActionListener(this);
        popupMenu.add(popM_Redo);
        
        separator_2 = new JSeparator();
        popupMenu.add(separator_2);
        
        popM_Cut = new JMenuItem("剪切(T)");
        popM_Cut.addActionListener(this);
        popupMenu.add(popM_Cut);
        
        popM_Copy = new JMenuItem("复制(C)");
        popM_Copy.addActionListener(this);
        popupMenu.add(popM_Copy);
        
        popM_Paste = new JMenuItem("粘贴(P)");
        popM_Paste.addActionListener(this);
        popupMenu.add(popM_Paste);
        
        popM_Delete = new JMenuItem("删除(D)");
        popM_Delete.addActionListener(this);
        popupMenu.add(popM_Delete);
        
        separator_3 = new JSeparator();
        popupMenu.add(separator_3);
        
        popM_SelectAll = new JMenuItem("全选(A)");
        popM_SelectAll.addActionListener(this);
        popupMenu.add(popM_SelectAll);
        
        separator_4 = new JSeparator();
        popupMenu.add(separator_4);
        
        popM_toLeft = new JMenuItem("从右到左的阅读顺序(R)");
        popM_toLeft.addActionListener(this);
        popupMenu.add(popM_toLeft);
        
        popM_showUnicode = new JMenuItem("显示Unicode控制字符(S)");
        popM_showUnicode.addActionListener(this);
        popupMenu.add(popM_showUnicode);
        
        popM_InsertUnicode = new JMenuItem("插入Unicode控制字符(I)");
        popM_InsertUnicode.addActionListener(this);
        popupMenu.add(popM_InsertUnicode);
        
        separator_5 = new JSeparator();
        popupMenu.add(separator_5);
        
        popM_closeIMe = new JMenuItem("关闭IME(L)");
        popM_closeIMe.addActionListener(this);
        popupMenu.add(popM_closeIMe);
        
        popM_RestartSelect = new JMenuItem("汉字重选(R)");
        popM_RestartSelect.addActionListener(this);
        popupMenu.add(popM_RestartSelect);      
        contentPane.add(scrollPane, BorderLayout.CENTER);//添加到面板中【中间】
               
        textArea.getDocument().addUndoableEditListener(undoMgr);//添加撤销管理器               
        
        toolState = new JToolBar();
        toolState.setSize(textArea.getSize().width, 10);
        label1 = new JLabel("    当前系统时间：" + hour + ":" + min + ":" + second+" ");
        toolState.add(label1);
        toolState.addSeparator();
        label2 = new JLabel("    第 " + linenum + " 行, 第 " + columnnum+" 列  ");
        toolState.add(label2);
        toolState.addSeparator();
        
        label3 = new JLabel("    一共 " +length+" 字  ");
        toolState.add(label3);
        textArea.addCaretListener(new CaretListener() {        //记录行数和列数
            public void caretUpdate(CaretEvent e) {
                JTextArea editArea = (JTextArea)e.getSource(); 
                try {
                    int caretpos = editArea.getCaretPosition();
                    linenum = editArea.getLineOfOffset(caretpos);
                    columnnum = caretpos - textArea.getLineStartOffset(linenum);
                    linenum += 1;
                    label2.setText("    第 " + linenum + " 行, 第 " + (columnnum+1)+" 列  ");
                    length=NotepadMainFrame.this.textArea.getText().toString().length();
                    label3.setText("    一共 " +length+" 字  ");
                }
                catch(Exception ex) { }
            }});       
        contentPane.add(toolState, BorderLayout.SOUTH);
        toolState.setVisible(false);
        toolState.setFloatable(false);
        Clock clock=new Clock();
        clock.start();      
        
        // 创建弹出菜单
        final JPopupMenu jp=new JPopupMenu();    //创建弹出式菜单，下面三项是菜单项
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton()==MouseEvent.BUTTON3)//只响应鼠标右键单击事件
                {
                    jp.show(e.getComponent(),e.getX(),e.getY());//在鼠标位置显示弹出式菜单
                }
            }
        });
    }
    
    /*===================================================================*/    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==itemOpen){            //打开
            openFile();
        }else if(e.getSource()==itemSave){        //保存
            save();
        }else if(e.getSource()==itemSaveAs){    //另存为
            saveAs();
        }else if(e.getSource()==itemNew){        //新建
            newFile();
        }else if(e.getSource()==itemExit){        //退出
            exit();
        }else if(e.getSource()==itemPage){        //页面设置
            PageFormat pf = new PageFormat();
            PrinterJob.getPrinterJob().pageDialog(pf); 
        }else if(e.getSource()==itemPrint){        //打印          
//            Print();//打印机
        }else if(e.getSource()==itemUndo || e.getSource()==popM_Undo){        //撤销
            if(undoMgr.canUndo()){      //canUndo() 如果可以撤消编辑，则返回 true
                undoMgr.undo();     //undo() 撤消适当的编辑。
            }
        }else if(e.getSource()==itemRedo || e.getSource()==popM_Redo){        //恢复
            if(undoMgr.canRedo()){         //canRedo() 如果可以恢复编辑，则返回 true
                undoMgr.redo();          //redo() 恢复适当的编辑。
            }
        }else if(e.getSource()==itemCut || e.getSource()==popM_Cut){        //剪切
            cut();
        }else if(e.getSource()==itemCopy || e.getSource()==popM_Copy){        //复制
            copy();
        }else if(e.getSource()==itemPaste || e.getSource()==popM_Paste){    //粘贴
            paste();
        }else if(e.getSource()==itemDelete || e.getSource()==popM_Delete){    //删除
            String tem=textArea.getText().toString();
            textArea.setText(tem.substring(0,textArea.getSelectionStart())); 
        }else if(e.getSource()==itemFind){        //查找
//            mySearch();
        }else if(e.getSource()==itemFindNext){    //查找下一个
//            mySearch();
        }else if(e.getSource()==itemReplace){    //替换
//            mySearch();
        }else if(e.getSource()==itemTurnTo){    //转到
//            turnTo();
        }else if(e.getSource()==itemSelectAll || e.getSource()==popM_SelectAll){    //选择全部
            textArea.selectAll();
        }else if(e.getSource()==itemTime){//时间/日期
          textArea.append(hour+":"+min+" "+c.get(Calendar.YEAR)+"/"+
                          (c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH));
        }else if(e.getSource()==itemFont){//设置字体大小
          MQFontChooser fontChooser = new MQFontChooser(textArea.getFont());// 构造字体选择器，参数字体为预设值
          fontChooser.showFontDialog(this);
          Font font = fontChooser.getSelectFont();
          textArea.setFont(font);// 将字体设置到JTextArea中
        }else if(e.getSource()==itemNextLine){//设置自动换行
           //设置文本区的换行策略。如果设置为 true，则当行的长度大于所分配的宽度时，将换行。此属性默认为 false。 
          if(itemNextLine.isSelected()){
            textArea.setLineWrap(true);
          }else{
            textArea.setLineWrap(false);
          }
        }else if(e.getSource()==itemColor){//设置背景颜色
          jcc1 = new JColorChooser();
          JOptionPane.showMessageDialog(this, jcc1,"选择背景颜色颜色",-1);
          color = jcc1.getColor();
          textArea.setBackground(color);
        }else if(e.getSource()==itemFontColor){//设置字体颜色
          jcc1=new JColorChooser();
          JOptionPane.showMessageDialog(this, jcc1, "选择字体颜色", -1);
          color = jcc1.getColor();
          textArea.setForeground(color);
        }else if(e.getSource()==itemStatement){//设置状态
          if(itemStatement.isSelected()){
            toolState.setVisible(true);
          }else{
            toolState.setVisible(false);
          }
        }else if(e.getSource()==itemSearchForHelp){
          JOptionPane.showMessageDialog(this, "查询帮助","行动起来",1);
        }else if(e.getSource()==itemAboutNotepad){
          JOptionPane.showMessageDialog(this, "记事本V1.0","软件说明 ",1);
        }
    }        
    /*===============================8====================================*/
    /**
     * 退出按钮，和窗口的红叉实现一样的功能
     */
    private void exit() {
        if(flag==2 && currentPath==null){
            //这是弹出小窗口
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本",
                                                     JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.saveAs();
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else if(flag==2 && currentPath!=null){
            //这是弹出小窗口
            //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+currentPath+"?", "记事本",
                                                     JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.save();
            }else if(result==JOptionPane.NO_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }else{
            //这是弹出小窗口
            int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "确定关闭？", "系统提示",
                                                     JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(result==JOptionPane.OK_OPTION){
                NotepadMainFrame.this.dispose();
                NotepadMainFrame.this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        }
    }
    
    /*===================================================================*/   
    private void newFile(){
      if(flag==0 || flag==1){//刚启动记事本为0，刚新建文档为1
        return;
      }else if(flag==2 && this.currentPath==null){//修改后//1、（刚启动记事本为0，刚新建文档为1）条件下修改后
        //添加确认提示框，会返回一个整数
        int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", 
                                                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);       
      if(result==JOptionPane.OK_OPTION){ //如果这个整数等于JOptionPane.YES_OPTION，
        this.saveAs();//另存为
      }
      else if(result==JOptionPane.NO_OPTION){
        this.textArea.setText("");
        this.setTitle("无标题");
        flag=1;
      }
      return;
      }else if(flag==2 && this.currentPath!=null ){ //2、（保存的文件为3）条件下修改后
        int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+this.currentPath+"?", "记事本",
                                                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result==JOptionPane.OK_OPTION){
          this.save();//直接保存，有路径
        }else if(result==JOptionPane.NO_OPTION){
            this.textArea.setText("");
            this.setTitle("无标题");
            flag=1;
          }
      }
      else if(flag==3){//保存的文件
        this.textArea.setText("");
        flag=1;
        this.setTitle("无标题");
      }
    }
    
    /*===================================================================*/    
    private void openFile() {
      if(flag==2 && currentPath==null){
       //1、（刚启动记事本为0，刚新建文档为1）条件下修改后
        int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到无标题?", "记事本", 
                                                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result==JOptionPane.OK_OPTION){
          NotepadMainFrame.this.saveAs();
        }
      }else if(flag==2 && this.currentPath!=null){
        //2、（打开的文件2，保存的文件3）条件下修改
        int result=JOptionPane.showConfirmDialog(NotepadMainFrame.this, "是否将更改保存到"+this.currentPath+"?", "记事本",
                                                 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result==JOptionPane.OK_OPTION){
          NotepadMainFrame.this.save();
        }
      }        
      JFileChooser choose=new JFileChooser(); //打开文件选择框      
      int result=choose.showOpenDialog(this); //选择文件
      if(result==JFileChooser.APPROVE_OPTION){      
        File file=choose.getSelectedFile(); //取得选择的文件
        currentFileName = file.getName();
        currentPath = file.getAbsolutePath();
        flag=3;
        this.setTitle(this.currentPath);
        BufferedReader br=null;
        try {
          InputStreamReader isr=new InputStreamReader(new FileInputStream(file),"GBK");
          br=new BufferedReader(isr);//动态绑定
          StringBuffer sb=new StringBuffer();
          String line=null;
          while((line=br.readLine())!=null){
            sb.append(line+SystemParam.LINE_SEPARATOR);
          }
          textArea.setText(sb.toString());
        }catch (FileNotFoundException e1) {
          e1.printStackTrace();
        } catch (IOException e1) {
          e1.printStackTrace();
        } finally{
          try {
            if(br!=null) 
              br.close();
          } catch (Exception e1) {
            e1.printStackTrace();
          }
        }
      }
    }
    /*===================================================================*/
        
    private void saveAs() {
      JFileChooser choose=new JFileChooser();//打开保存框
      int result=choose.showSaveDialog(this);//选择文件
      if(result==JFileChooser.APPROVE_OPTION){
        File file=choose.getSelectedFile();//取得选择的文件[文件名是自己输入的]
        FileWriter fw=null;
        try {//保存
          fw=new FileWriter(file);
          fw.write(textArea.getText());
          currentFileName=file.getName();
          currentPath=file.getAbsolutePath();
          fw.flush();//如果比较少，需要写
          this.flag=3;
          this.setTitle(currentPath);
        } catch (IOException e1) {
          e1.printStackTrace();
        }finally{
          try {
            if(fw!=null) fw.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
    }    
    /*===================================================================*/
    
    private void save() {
      if(this.currentPath==null){
        this.saveAs();
        if(this.currentPath==null){
          return;
        }
      }
      FileWriter fw=null;
      try {//保存
        fw=new FileWriter(new File(currentPath));
        fw.write(textArea.getText());
        fw.flush();//如果比较少，需要写
        flag=3;
        this.setTitle(this.currentPath);
      } catch (IOException e1) {
        e1.printStackTrace();
      }finally{
        try {
          if(fw!=null) fw.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
      
    /*================================================================*/    
    public void cut(){
      copy();
      int start = this.textArea.getSelectionStart();//标记开始位置
      int end = this.textArea.getSelectionEnd();//标记结束位置
      this.textArea.replaceRange("", start, end);//删除所选段
    }
    
    public void copy(){
      String temp = this.textArea.getSelectedText();//拖动选取文本
      //把获取的内容复制到连续字符器，这个类继承了剪贴板接口
      StringSelection text = new StringSelection(temp);
      this.clipboard.setContents(text, null);//把内容放在剪贴板
    }
    
    public void paste(){
      Transferable contents = this.clipboard.getContents(this);//Transferable接口，把剪贴板的内容转换成数据
      DataFlavor flavor = DataFlavor.stringFlavor;//DataFalvor类判断是否能把剪贴板的内容转换成所需数据类型
      if(contents.isDataFlavorSupported(flavor)){//如果可以转换
        String str;
        try {//开始转换
          str=(String)contents.getTransferData(flavor);
          if(this.textArea.getSelectedText()!=null){//如果要粘贴时，鼠标已经选中了一些字符
            int start = this.textArea.getSelectionStart();//定位被选中字符的开始位置
            int end = this.textArea.getSelectionEnd();//定位被选中字符的末尾位置
            this.textArea.replaceRange(str, start, end);//把粘贴的内容替换成被选中的内容
          }else{
            int mouse = this.textArea.getCaretPosition();//获取鼠标所在TextArea的位置
            this.textArea.insert(str, mouse);//在鼠标所在的位置粘贴内容
          }
        } catch(UnsupportedFlavorException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch(IllegalArgumentException e){
          e.printStackTrace();
        }
      }
    }
    

   

}