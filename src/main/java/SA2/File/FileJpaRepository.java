package SA2.File;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileJpaRepository implements FileRepository {
    private final FileInterfaceJpaRepository fileInterfaceJpaRepository;

    public FileJpaRepository(FileInterfaceJpaRepository fileInterfaceJpaRepository) {
        this.fileInterfaceJpaRepository = fileInterfaceJpaRepository;
    }

    @Override
    public void saveFile(File file) {
        fileInterfaceJpaRepository.saveAndFlush(file);
    }

    @Override
    public File findFileById(int id) {
        return this.fileInterfaceJpaRepository.getById(id);
    }

    @Override
    public void deleteFileById(int id) {
        this.fileInterfaceJpaRepository.deleteById(id);
    }

    @Override
    public List<File> getAllFiles() {
        return fileInterfaceJpaRepository.findAll();
    }

    @Override
    public File updateFileById(int id, File file) {
        File oldFile = this.fileInterfaceJpaRepository.getById(id);
        oldFile.setFilename(file.getFilename());
        fileInterfaceJpaRepository.saveAndFlush(oldFile);
        return oldFile;
    }
}
