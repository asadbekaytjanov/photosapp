package uz.aytjanov.googlephotosclone.web;

import jakarta.servlet.http.HttpSession;
import org.hibernate.annotations.NotFound;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.multipart.MultipartFile;
import uz.aytjanov.googlephotosclone.model.Photo;
import uz.aytjanov.googlephotosclone.model.User;
import uz.aytjanov.googlephotosclone.service.PhotosService;
import uz.aytjanov.googlephotosclone.service.UsersService;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@Controller
public class PhotosController {
    private final PhotosService photosService;
    private final UsersService usersService;

    public PhotosController(PhotosService photosService, UsersService usersService) {
        this.photosService = photosService;
        this.usersService = usersService;
    }
    @GetMapping("/")
    public String mainPage() {
        return "redirect:/login";
    }

    @GetMapping("/gallery")
    public String userPhotos(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        } else {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("mediaList", photosService.getMediaByUserid((Long) session.getAttribute("userId")));
            return "gallery";
        }
    }
   @GetMapping("/photos/{id}/open")
   public ResponseEntity<byte[]> openFile(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        Photo photo = photosService.getPhoto(id);
        if (photo == null || !photo.getUser().getId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                    .header("Content-Type", photo.getContentType())
                .body(photo.getData());
   }
   @GetMapping("/logout")
   public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
   }

    @GetMapping("/upload")
    public String upload(){
        return "/upload";
    }
    @PostMapping("/upload")
    public String create(@RequestParam("file") MultipartFile file, HttpSession session) throws IOException {
        if (!file.isEmpty()) {
            Photo photo = new Photo();
            photo.setUser(usersService.getUser((Long) session.getAttribute("userId")));
            photo.setData(file.getBytes());
            photo.setContentType(file.getContentType());
            photo.setFileName(file.getOriginalFilename());
            photosService.savePhoto(photo);
        }
        return "redirect:/gallery";
    }
    @GetMapping("/photos/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Photo photo = photosService.getPhoto(id);
        if (usersService.getUser(userId) == null || photosService.getPhoto(id) == null || !photo.getUser().getId().equals(userId)) {
           return "redirect:/gallery";
        }
        photosService.delete(id);
        return "redirect:/gallery";
    }
    @GetMapping("/photos/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        Photo photo = photosService.getPhoto(id);
        if (usersService.getUser(userId) == null || photosService.getPhoto(id) == null || !photo.getUser().getId().equals(userId)) return ResponseEntity.notFound().build();
        byte[] data = photo.getData();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(photo.getFileName()).build());
        headers.setContentType(MediaType.valueOf(photo.getContentType()));
        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}