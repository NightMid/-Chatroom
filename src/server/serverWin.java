package server;

import DateUtils.DateUtil;
import DateUtils.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class serverWin extends JFrame implements ActionListener, WindowListener {
    private JLabel statusLal;
    private JTextArea textArea;
    private JTextArea infotextArea;
    private JTextField textField;
    private JButton sendBtn;
    private JScrollPane scrollPane;

    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;




    public serverWin() {
        inti();
    }

    private void inti() {
        this.setSize(650, 540);
        this.setTitle("服务端窗口");
        //退出的时候关闭程序
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(null);
        Container contentPane = this.getContentPane();
        contentPane.setBackground(Color.lightGray);

        //状态提示
        statusLal = new JLabel("等待连接");
        statusLal.setBounds(0, 0, 650, 50);
        statusLal.setHorizontalAlignment(JLabel.CENTER);
        statusLal.setVerticalAlignment(JLabel.CENTER);
        contentPane.add(statusLal);

        //聊天记录框
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.white);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(0, 50, 450, 340);
        contentPane.add(scrollPane);

        //输入框
        textField = new JTextField();
        textField.setBounds(0, 390, 450, 50);
        contentPane.add(textField);

        //消息发送按钮
        sendBtn = new JButton("发送");
        sendBtn.setBounds(370, 460, 70, 25);

        contentPane.add(sendBtn);

        //资料框
        infotextArea = new JTextArea();
        infotextArea.setBounds(450, 50, 200, 490);
        infotextArea.setText("这里是好友的资料信息");
        contentPane.add(infotextArea);

        //监听发送按钮
        sendBtn.addActionListener(this);


        new ListenThread().start();

        addWindowListener(this);

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        try{
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (serverSocket != null) {
                serverSocket .close();
            }
            if (socket != null) {
                socket .close();
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    /**
      * 内部类 线程类
       */
    public class ListenThread extends Thread{
         @Override
         public void run() {
             try {
                 //新建一个服务套接字
                 serverSocket = new ServerSocket(8080, 1000);
                 //得到socket
                 socket = serverSocket.accept();
                 inputStream = socket.getInputStream();
                 outputStream = socket.getOutputStream();

                 statusLal.setText("已连接");

                 while (true){
                     //定义一个临时缓冲区
                     byte[] buf = new byte[1024];
                     //从输入流读长度
                     int len = inputStream.read(buf);
                     //根据实际的数据长度，定义一个输出缓冲区
                     byte[] ebuf =new byte[len];
                     //拷贝实际数据
                     System.arraycopy(buf,0, ebuf,0,len);
                     //把字节转换成字符
                     String text = new String(ebuf);

                     //显示接收的文本
                     textArea.append("客户端："+ DateUtil.getnow());
                     textArea.append("\r\n");
                     textArea.append(text);
                     textArea.append("\r\n");


                 }


             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
     }



    public static void main(String[] args) {
        serverWin s = new serverWin();
        s.setVisible(true);


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //执行发送的操作
        if (e.getSource() == sendBtn) {
            //获取输入框的文本
            String sendText = textField.getText();
            //处理文本
            if(!"".equals(sendText) && sendText != null){
                //1.显示到我们自己的聊天纪录框
                textArea.append("我："+ DateUtil.getnow());
                textArea.append("\r\n");
                textArea.append(sendText);
                textArea.append("\r\n");
                textField.setText("");
                //2.发送
                byte[] sendBuf = sendText.getBytes();
                try {
                    if (outputStream != null){
                        outputStream.write(sendBuf);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }



        }
    }
}
