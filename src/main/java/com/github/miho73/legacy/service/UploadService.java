package com.github.miho73.legacy.service;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadService {
    private File createTempFile(MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString();
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    public String calculateHash(MultipartFile file) throws IOException {
        //TODO: Hash 계산
    }
}
