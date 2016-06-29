package com.dailytools.main;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Qxiaoi {
 private JFrame frame;
 private JPanel panel;
 private JTextArea textArea;
 private JButton buttonC, buttonS;
 private boolean isNull = true;

 public Qxiaoi() {
  frame = new JFrame();
  panel = new JPanel();
  textArea = new JTextArea(10, 10);
  buttonC = new JButton("检查");
  buttonS = new JButton("保存");
  init();
  addActionHandle();
 }

 private void init() {
  frame.add(panel);
  panel.add(textArea);
  panel.add(buttonC);
  panel.add(buttonS);

  frame.pack();
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setVisible(true);
 }
 
 private void addActionHandle() {
  buttonC.addActionListener(new ActionListener() {

   public void actionPerformed(ActionEvent e) {
    if (textArea.getText() != ""
      && textArea.getText().length() != 0) {
     JOptionPane.showMessageDialog(Qxiaoi.this.frame,
       "文本内容不为空！", "消息", JOptionPane.INFORMATION_MESSAGE);
     isNull = false;
    } else {
     JOptionPane.showMessageDialog(Qxiaoi.this.frame, "文本内容为空！",
       "消息", JOptionPane.INFORMATION_MESSAGE);
     isNull = true;
    }
   }

  });

  buttonS.addActionListener(new ActionListener() {

   public void actionPerformed(ActionEvent e) {
    if (!isNull) {
     File file=new File("C:"+ File.separator + "save.txt");
     System.out.println(textArea.getText());
     FileOutputStream fos=null;
     OutputStreamWriter osw=null;
     try {
      fos=new FileOutputStream(file);
      osw=new OutputStreamWriter(fos,"UTF8");
      osw.write(textArea.getText());
      osw.flush();
     } catch (Exception e1) {
      e1.printStackTrace();
     }finally{
      if(osw!=null)try{osw.close();}catch(IOException e2){}
      if(fos!=null)try{fos.close();}catch(IOException e3){}
     }
     JOptionPane.showMessageDialog(Qxiaoi.this.frame,
       "保存成功！", "消息", JOptionPane.INFORMATION_MESSAGE);

    } else {
     JOptionPane.showMessageDialog(Qxiaoi.this.frame, "文本内容为空！",
       "消息", JOptionPane.INFORMATION_MESSAGE);
    }
   }

  });
 }

 public static void main(String[] args) {
  new Qxiaoi();
 }
}