import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    final static String USERPATH = "/Users/rusimac/Games/";
    final static String TYPEFILE = ".dat";
    final static String ZIPFILE = "saves.zip";

    public static void main(String[] args) {
        GameProgress save1 = new GameProgress(100, 90, 1, 10);
        GameProgress save2 = new GameProgress(87, 32, 2, 134);
        GameProgress save3 = new GameProgress(54, 75, 4, 376);

        //Создание списка сохранённых файлов
        List<String> saves = new ArrayList<>();

        saveGameProgress(save1, "save1", saves);
        saveGameProgress(save2, "save2", saves);
        saveGameProgress(save3, "save3", saves);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(USERPATH + "savegames/" + ZIPFILE))) {
            FileInputStream fileInputStream = new FileInputStream(USERPATH + saves.get(0)); //читаем в поток первый файл из списка

            //По одному файлу из списка добавляем в архив
            for (String saveFile : saves) {
                zipOutputStream.putNextEntry(new ZipEntry(saveFile));
                addToZip(fileInputStream, zipOutputStream);
                System.out.println("Файл " + saveFile + " добавлен в архив.");
            }
            // Удаление файлов вне архива
            for (String saveFile : saves) {
                File file = new File(USERPATH + saveFile);
                file.delete();
                System.out.println("Файл " + saveFile + " удалён.");
            }
        } catch (IOException ex) {
            System.err.println("Не удалось записать файл в архив!");
        }
    }

    private static void addToZip(FileInputStream fileInputStream, ZipOutputStream zipOutputStream) {
        try {
            byte[] buffer = new byte[fileInputStream.available()];
            fileInputStream.read(buffer);
            zipOutputStream.write(buffer);
            zipOutputStream.closeEntry();
        } catch (IOException ex) {
            System.err.println("Не удалось сохранить в zip-архив!");
        }
    }

    private static void saveGameProgress(GameProgress save, String nameSaveGame, List<String> saves) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(USERPATH + nameSaveGame + TYPEFILE)) {
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(save);
            saves.add(nameSaveGame + TYPEFILE);
            System.out.println("Игра сохранена " + nameSaveGame);
        } catch (IOException ex) {
            System.err.println("Не удалось сохранить игру!");
        }
    }
}
