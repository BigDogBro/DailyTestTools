package polyu.haleyfu;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONException;

public class DialogShow extends JDialog implements ActionListener {
 
    // 文本框
    JTextField name;
    // 文本框
    JTextArea command;
    // 对话框的父窗体。
    Main parent;
    // “确定”按钮
    JButton setButton;
 
    /**
     * 构造函数，参数为父窗体和对话框的标题
     */
    DialogShow(JFrame prentFrame, String title) {
        // 调用父类的构造函数，
        // 第三个参数用false表示允许激活其他窗体。为true表示不能够激活其他窗体
        super(prentFrame, title, false);
        parent = (Main) prentFrame;
 
        // 添加Label和输入文本框
        JPanel p1 = new JPanel();
        JLabel label = new JLabel("按钮名字:");
        p1.add(label);
        name = new JTextField(20);
        name.addActionListener(this);
        p1.add(name);
        getContentPane().add("North", p1);
        
        // 添加Label和输入文本框
        JPanel p2 = new JPanel();
        JLabel label2 = new JLabel("执行命令:");
        p2.add(label2);
        command = new JTextArea(5,20);
        p2.add(command);
        getContentPane().add("Center", p2);
 
        // 添加确定和取消按钮
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("取 消");
        cancelButton.addActionListener(this);
        setButton = new JButton("确 定");
        setButton.addActionListener(this);
        p3.add(setButton);
        p3.add(cancelButton);
        getContentPane().add("South", p3);
 
        // 调整对话框布局大小
        pack();
    }
 
    /**
     * 事件处理
     */
    public void actionPerformed(ActionEvent event) {
 
        Object source = event.getSource();
        if ((source == setButton)) {
            // 如果确定按钮被按下，则将文本矿的文本添加到父窗体的文本域中
        	String buttonName=name.getText();
        	String commandText=command.getText();
        	parent.addButton(buttonName,commandText,parent.commomOpertion);
        	try {
				FileUtil.addJson(buttonName, commandText);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        // 隐藏对话框
        setVisible(false);
    }
}



