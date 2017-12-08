import java.io.{File, PrintWriter}

object ByFolderMp3U3ListMakerEntry {
  def main(args: Array[String]): Unit = {

    require(args.length > 0, "fetch directory")

    for (filePath <- args) {

      if (isFileExistAndIsDirecotry(filePath)) {

        saveM3U8List(createListContent(filePath, "mp3"), createSavePath(filePath))

      }

    }

  }

  def isFileExistAndIsDirecotry(arg: String): Boolean = {
    val folder = new File(arg)
    folder.exists() && folder.isDirectory
  }

  def saveM3U8List(content: String, fileLocation: String): Unit = {
    val writer = new PrintWriter(new File(fileLocation))
    writer.write(content)
    writer.close()
    println(fileLocation + " playlist created")
  }

  def createListContent(folderPath: String, fileExtension: String): String = {

    val folder = new File(folderPath)

    val mp3u8ListBeginText = "#EXTM3U"
    val trackEntryText = "#EXTINF:"

    var fileListContent = List(mp3u8ListBeginText)

    for (file <- selectFilesWithExtension(recursiveListAllFilesInDirectory(folder), fileExtension)) {

      val playlistFilePathEntry = file.getAbsolutePath.replaceAllLiterally(folder.getParent + "\\", "")

      fileListContent ++= List(trackEntryText, playlistFilePathEntry)

    }

    fileListContent.mkString("\r\n")
  }


  def createSavePath(filePath: String): String = {
    val folder = new File(filePath)
    folder.getParent + "\\" + folder.getName + "PlayList" + ".m3u8"
  }


  def recursiveListAllFilesInDirectory(f: File): Array[File] = {
    val currentLevelDirectory = f.listFiles
    currentLevelDirectory ++ currentLevelDirectory.filter(_.isDirectory).flatMap(recursiveListAllFilesInDirectory)
  }

  def selectFilesWithExtension(files: Array[File], extension: String): Array[File] = {
    files.filter(f => f.getName.endsWith(extension))
  }

}
