import java.awt.{BorderLayout, Font}
import java.awt.event.{KeyEvent, KeyListener}
import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.Socket
import java.util.concurrent.Semaphore

import javax.swing.{JButton, JFrame, JPanel, JTextArea, JTextField, WindowConstants}

object Client {
  def main(args: Array[String]): Unit = {
    new Thread(new Client).start()
  }
}

class Client extends Runnable {
  val ui = new GUIChat
  val semaphore = new Semaphore(0)

  ui.submit.addActionListener(e => {
    semaphore.release()
  })

  def run(): Unit = {
    ui.text.setText("Введите адрес сервера")
    semaphore.acquire()
    val host = ui.message.getText()
    ui.message.setText("")
    ui.text.setText("Введите порт")
    semaphore.acquire()
    val port = Integer.parseInt(ui.message.getText())
    ui.message.setText("")
    val socket = new Socket(host, port)
    val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
    val out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))

    val sendMessages = new Thread(() => {
      ui.text.setText("Введите имя: ")
      semaphore.acquire()
      out.write(ui.message.getText() + "\n")
      out.flush()
      ui.message.setText("")
      ui.text.setText("")
      var tmp = ""
      while (!tmp.equals("стоп")) {
        semaphore.acquire()
        tmp = ui.message.getText() + "\n"
        ui.message.setText("")
        out.write(tmp)
        out.flush()
      }
    })

    val readMessages = new Thread(() => {
      while (true) {
        ui.text.append(in.readLine() + "\n")
      }
    })

    sendMessages.start()
    readMessages.start()
    sendMessages.join()
    readMessages.join()
  }

  class GUIChat {

    val frame = new JFrame("MyChat")
    val panel = new JPanel(new BorderLayout())
    val submit = new JButton("Ввод")
    val text = new JTextArea(20, 40)
    val message = new JTextField(40)

    {
      text.setEditable(false)
      text.setLineWrap(true)
      text.setWrapStyleWord(true)
      text.setFont(new Font("Arial", Font.PLAIN, 16))
      message.setFont(new Font("Arial", Font.PLAIN, 16))

      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
      panel.add(text, BorderLayout.NORTH)
      panel.add(message, BorderLayout.CENTER)
      panel.add(submit, BorderLayout.SOUTH)
      frame.add(panel)
      frame.setVisible(true)
      frame.pack()
      frame.setLocationRelativeTo(null)

      val keyListener = new KeyListener {
        override def keyTyped(e: KeyEvent): Unit = {}

        override def keyPressed(e: KeyEvent): Unit = {}

        override def keyReleased(e: KeyEvent): Unit = if (e.getKeyCode() == KeyEvent.VK_ENTER) submit.doClick()
      }

      message.addKeyListener(keyListener)
    }
  }
}