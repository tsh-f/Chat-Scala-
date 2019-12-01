import java.awt.{BorderLayout, Font}
import java.awt.event.{KeyEvent, KeyListener}

import javax.swing._

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
      override def keyTyped(e: KeyEvent): Unit = null

      override def keyPressed(e: KeyEvent): Unit = null

      override def keyReleased(e: KeyEvent): Unit = if (e.getKeyCode() == KeyEvent.VK_ENTER) submit.doClick()
    }

    message.addKeyListener(keyListener)
  }
}