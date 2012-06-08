import java.io._

object IndexHtmlGenerator {

  def main(args: Array[String]) {
    recurse(new File("m2"))
  }

  def recurse(file: File) {
    if (file isDirectory) {
      val dirs = file.listFiles.filter(_.isDirectory)
      val files = file.listFiles.filter(_.isFile).filter(_.getName != "index.html")
      writeIndexHtml(new File(file, "index.html"), dirs, files)
      
      dirs foreach { recurse(_) }
    }
  }

  def writeIndexHtml(indexHtml: File, dirs: Array[File], files: Array[File]) {
    println("Writing " + indexHtml)
    val dirNames = dirs.map(_.getName + "/").sorted.toList
    val fileNames = files.map(_.getName).sorted.toList
    using(new OutputStreamWriter(new FileOutputStream(indexHtml), "UTF-8")) { writer =>
      writer.write(
        """<!DOCTYPE html>
           |<html>
           |  <head><title>Index</title><head>
           |  <body>
           |    <ul>
           |      <li><a href="..">../</a></li>
           |""".stripMargin)
      (dirNames ::: fileNames) foreach { file =>
        writer.write("""|      <li><a href="%s">%s</a></li>""".stripMargin.format(file, file))
      }
      writer.write(
        """|  </body>
           |</html>""".stripMargin)
    }
  }

  def using(writer: Writer)(f: Writer => Unit) {
    try {
      f(writer)
    } finally {
      writer.close()
    }
  }

}

