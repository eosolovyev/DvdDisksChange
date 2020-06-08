package org.project.controllers;

import org.project.domain.Disk;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ControllerUtil {

    public static void editAvatar(Disk disk,
                                  @RequestParam MultipartFile file, String uploadPath) throws IOException {
        //Если файл не пустой
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            //Создает файл, который ведет к uploadPath
            File uploadDir = new File(uploadPath);
            //Если данная папка uploadPath не существует, то создает ее
            if (!uploadDir.exists()) {
                //Создает
                uploadDir.mkdir();
            }

            //Рандомный UUID
            String originalFilename = file.getOriginalFilename();
            //Если расширение не jqp/png
            if (!(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".png"))) {
                //Выбрасывает исключение
                throw new IllegalArgumentException("this is not image!");
            }

            String uuIDFile = UUID.randomUUID().toString();
            //Итоговое имя файл, рандомный UUID + его имя
            String resultFileName = uuIDFile + file.getOriginalFilename();

            //Сохраняет данный файл в папку uploadPath
            file.transferTo(new File(uploadPath + "/" + resultFileName));

            //Вещи ставит итоговое имя файла(filename это строка, которая содержит путь к картинке)
            disk.setSkinName(resultFileName);

        }
    }
}
