package org.project.controllers;

import org.project.domain.Disk;
import org.project.domain.Role;
import org.project.domain.User;
import org.project.services.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Repo repo;

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    /**
     * @param username имя пользователя, которое вводится с формы
     * @param password пароль из формы
     * @return
     */
    @PostMapping("/registration")
    public String postRegistration(
            @RequestParam String username,
            @RequestParam String password
    ) {


        //Нельзя создать пользователя с именем null
        if (username.equals("null")) {
            return "redirect:/registration";
        }

        User user = repo.findByUsername(username);

        //Если он существует, то обратно редирекктит на регистрацию
        if (user != null) {
            return "redirect:/registration";
        }

        //Создает пользователя
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setRoles(Collections.singleton(Role.ADMIN));
        newUser.setPassword(passwordEncoder.encode(password));

        //Сохраняет его
        repo.saveUser(newUser);
        //Редиректит уже на логин
        return "redirect:/login";
    }

    /**
     * @param id          переменная пути, соответствует id диска
     * @param model       модель для связи данных с ftlh страничкой
     * @param currentUser текущий пользователь из сесси
     * @return
     */
    @GetMapping("/disk/{id}")
    public String diskPage(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal User currentUser) {
        //Ищет пользователя из репозитория по айди
        Disk disk = repo.findDiskById(id);
        //Добавляет в модельку переменные по типу ключ-значение
        model.addAttribute("disk", disk);
        model.addAttribute("isOwner", disk.getUsername().equals(currentUser.getUsername()));
        model.addAttribute("noOne", disk.getUsername().equals("null"));
        return "diskPage";
    }

    /**
     * доступен только пользователям, которые админы
     * @return
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/addNewDisk")
    public String getAddDisk() {
        return "addNewDisk";
    }


    /**
     * @param model модель для связи данных с ftlh страничкой
     * @return
     */
    @GetMapping("/listFreeDisks")
    public String getAllDisks(Model model) {
        //Ищет список свободных дисков из репозитория
        List<Disk> freeDisks = repo.getFreeDisks();
        model.addAttribute("disks", freeDisks);
        model.addAttribute("isFreeDisks", freeDisks.size() != 0);
        return "freeDisks";
    }

    /**
     * доступен только пользователям, у которых есть роль админ
     * @param name        имя диска, которое вводится в форме
     * @param file        файл
     * @param currentUser ткущий пользователь
     * @return
     * @throws IOException
     */

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/addNewDisk")
    public String postAddDisk(@RequestParam String name,
                              @RequestParam MultipartFile file,
                              @AuthenticationPrincipal User currentUser) throws IOException {
        //Создает новый диск
        Disk disk = new Disk();
        //Ставит диску название
        disk.setName(name);
        //Вызывает статический метод, который сохраняет картинку диску
        ControllerUtil.editAvatar(disk, file, uploadPath);
        //Ставит имя пользователя диску
        disk.setUsername(currentUser.getUsername());
        //Сохраняет диск в репозитории, возвращает сохраненный диск с уже сгенерироанным id
        Disk savedDisk = repo.saveDisk(disk);
        //Добавляет новый диск в коллекцию
        currentUser.getCurrentDisks().add(savedDisk);
        //Сохраняет пользователя
        repo.saveUser(currentUser);
        return "redirect:/disk/" + savedDisk.getId();
    }

    /**
     * @param user текущий пользователь
     * @param id   id диска
     * @return
     */
    @GetMapping("/disk/{id}/remove")
    public String removeDiskFromUser(@AuthenticationPrincipal User user,
                                     @PathVariable Long id) {
        //Ищет пользователя из репозитория по айди, по сути ищет сам себя
        User currentUser = repo.findUserById(user.getId());
        //Ищет диск из репозитория
        Disk disk = repo.findDiskById(id);
        //Проверяет на условие, является ли данный пользователь обладателем данного диска
        if (!disk.getUsername().equals(currentUser.getUsername())) {
            return "redirect:/disk/" + id;
        }
        //Удаляет диск из коллекции текущих дисков
        currentUser.getCurrentDisks().remove(disk);
        //Добавляет элемент в коллекцию предыдущих дисков
        currentUser.getOldDisks().add(disk);
        //Ставит имя null,
        disk.setUsername("null");
        //И сохраняет в бд измененные объекты
        repo.updateDisk(disk);
        repo.saveUser(currentUser);
        return "redirect:/disk/" + disk.getId();
    }

    /**
     * @param id   переменная пути лонг, соответствует переменной
     * @param user пользователь из контекста
     * @return
     */
    @GetMapping("/disk/{id}/buy")
    public String buyDisk(@PathVariable Long id,
                          @AuthenticationPrincipal User user) {
        //Ищет пользователя из репозитория по айди, по сути ищет сам себя
        User currentUser = repo.findUserById(user.getId());
        //Ищет диск из репозитория
        Disk disk = repo.findDiskById(id);
        //Является ли данный пользователь обладателем этого диска, если да, то он не может купить сам у себя
        if (disk.getUsername().equals(currentUser.getUsername())) {
            return "redirect:/disk/" + disk.getId();
        }

        //Из БД ищу владельца диска по имены
        User oldUser = repo.findByUsername(disk.getUsername());
        //У владельца удаляю диск из числа действующих
        oldUser.getCurrentDisks().remove(disk);
        //Добавляю в коллекцию старых дисков
        oldUser.getOldDisks().add(disk);
        //Ставлю диску имя нового владельца
        disk.setUsername(currentUser.getUsername());
        //Сохраняю
        repo.updateDisk(disk);
        //Добавляю текущему пользователя новый диск
        currentUser.getCurrentDisks().add(disk);
        //Удаляю из списка старых дисков этот диск, т.к. он будет в списке текущих
        currentUser.getOldDisks().removeIf(d -> d.getId().equals(disk.getId()));
        //Сохраняю
        repo.saveUser(oldUser);
        repo.saveUser(currentUser);
        return "redirect:/user/" + currentUser.getId();
    }

    /**
     * @param user текущий пользователь
     * @param id   айди диска
     * @return
     */
    @GetMapping("/disk/{id}/addFree")
    public String addFreeDiskToUser(@AuthenticationPrincipal User user,
                                    @PathVariable Long id) {
        //Ищет пользователя из репозитория по айди, по сути ищет сам себя
        User currentUser = repo.findUserById(user.getId());
        //Ищет диск из репозитория
        Disk disk = repo.findDiskById(id);
        //Ставит диску в поле юзернейм имя текущего пользователя
        disk.setUsername(currentUser.getUsername());
        //Добавляет диск из списка текущих
        currentUser.getCurrentDisks().add(disk);
        //Удаляет диск из списка прошлых дисков
        currentUser.getOldDisks().removeIf(d -> d.getId().equals(disk.getId()));
        //Save
        repo.saveUser(currentUser);
        return "redirect:/user/" + currentUser.getId();
    }

    /**
     * @param id          пременная пути
     * @param model       модель для связи переменных в ftlh страничку
     * @param currentUser текущий пользователь из сессии
     * @return
     */
    @GetMapping("/user/{id}")
    public String userPage(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal User currentUser) {
        //Ищет пользователя по id, которая переменная пути в url
        User user = repo.findUserById(id);
        //Добавляет аттрибуты
        model.addAttribute("user", user);
        model.addAttribute("currentDisks", user.getCurrentDisks());
        model.addAttribute("oldDisks", user.getOldDisks());
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("isCurrentDisks", user.getCurrentDisks().size() != 0);
        model.addAttribute("isOldDisks", user.getOldDisks().size() != 0);
        return "userPage";
    }
}
