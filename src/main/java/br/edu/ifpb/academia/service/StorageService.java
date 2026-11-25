package br.edu.ifpb.academia.service;

import br.edu.ifpb.academia.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final S3Client s3Client;

    @Value("${supabase.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folder) {
        if (file.isEmpty()) {
            throw new RegraDeNegocioException("Arquivo vazio.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
        String key = folder + "/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return key;
            
        } catch (IOException e) {
            throw new RegraDeNegocioException("Erro ao ler o arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new RegraDeNegocioException("Erro ao enviar arquivo para o Storage: " + e.getMessage());
        }
    }
}