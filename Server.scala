import java.io.{BufferedReader, BufferedWriter, InputStreamReader, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.Scanner

object Server {
  def main(args: Array[String]): Unit = {
    new MyServer
  }
}

class MyServer() {
  var list = new util.LinkedList[Connection]()
  println("Введите порт")
  val server = new ServerSocket(new Scanner(System.in).nextInt())


  while (true) {
    val socket = server.accept()
    new Thread(new Connection(socket, list)).start()
    println(socket.getInetAddress.getHostName + " connected")
  }
}

class Connection(socket: Socket, list: util.LinkedList[Connection]) extends Runnable {
  val in = new BufferedReader(new InputStreamReader(socket.getInputStream))
  val out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream))

  def run(): Unit = {
    list.add(this)
    try {
      val name = in.readLine()
      while (true) {
        val message = in.readLine()

        list.forEach(c => {
          c.out.write("(" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ") " + name + ": " + message + "\n")
          c.out.flush()
        })
      }
    } finally {
      list.remove(this)
      this.in.close()
      this.out.close()
      this.socket.close()
    }
  }
}
