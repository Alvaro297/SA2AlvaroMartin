package SA2;

import SA2.File.File;
import SA2.File.FileRepository;
import SA2.Storage.StorageProperties;
import SA2.Storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.websocket.server.PathParam;

import java.io.IOException;

@RestController
public class FileController {
    private final StorageService storageService;
    private final FileRepository fileRepository;

    private final StorageProperties properties;


    public FileController(StorageService storageService, FileRepository fileRepository,StorageProperties properties) {
        this.storageService = storageService;
        this.fileRepository=fileRepository;
        this.properties = properties;
    }
    @GetMapping()
    public ResponseEntity<Resource> getFileByName(@PathParam("filename") String filename){
        return getFileByNameAndReturn(filename);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable String id){
        SA2.File.File file = fileRepository.findFileById(Integer.parseInt(id));
        return getFileByNameAndReturn(file.getFilename());
    }

    @GetMapping("/setpath")
    public ResponseEntity<?> setFilePath(@PathParam("path") String path) {
        properties.setLocation(path);
        return ResponseEntity.ok().body("Path cambiado exitosamente");
    }

    private ResponseEntity<Resource> getFileByNameAndReturn(String fileName) {
        Resource fileOutput = storageService.loadAsResource(fileName);
        String fileHeader = String.format("attachment; filename=\"%s\"", fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, fileHeader).body(fileOutput);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathParam("filetype") String filetype) {


        if(!file.getContentType().equals(filetype)){
            return ResponseEntity.badRequest().body("File no especificado");
        }


        storageService.store(file);

        fileRepository.saveFile(new File(file.getOriginalFilename(), file.getContentType()));

        return ResponseEntity.ok().body("it worked");

    }
}
