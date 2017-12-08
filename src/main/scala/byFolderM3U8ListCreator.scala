import java.io.{File, PrintWriter}

object ByFolderMp3U3ListMakerEntry {
  def main(args: Array[String]): Unit = {


//    require(args.length == 0, "fetch directory")

    if (args.length == 0) {
      println("fetch directory")

      return
    }

    for (arg <- args) {

      val folder = new File(arg)
      val saveFileLocation = folder.getParent + "\\" + folder.getName + "PlayList" + ".m3u8"

      if (folder.exists() && folder.isDirectory) {

        val mp3u8ListBeginText = "#EXTM3U"
        val trackEntryText = "#EXTINF:"

        var fileListContent = List(mp3u8ListBeginText)

        for (file <- selectFilesWithExtension(recursiveListAllFilesInDirectory(folder), "mp3")) {

          val playlistFilePathEntry = file.getAbsolutePath.replaceAllLiterally(folder.getParent + "\\", "")

          fileListContent ++= List(trackEntryText, playlistFilePathEntry)

        }

        val inTextContent = fileListContent.mkString("\r\n")

        val writer = new PrintWriter(new File(saveFileLocation))
        writer.write(inTextContent)
        writer.close()
        println(saveFileLocation + " playlist created")

      }

    }

  }

  def recursiveListAllFilesInDirectory(f: File): Array[File] = {
    val currentLevelDirectory = f.listFiles
    currentLevelDirectory ++ currentLevelDirectory.filter(_.isDirectory).flatMap(recursiveListAllFilesInDirectory)
  }

  def selectFilesWithExtension(files: Array[File], extension: String): Array[File] = {
    files.filter(f => f.getName.endsWith(extension))
  }

}
