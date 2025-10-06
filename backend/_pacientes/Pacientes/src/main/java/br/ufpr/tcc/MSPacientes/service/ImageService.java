package br.ufpr.tcc.MSPacientes.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 300;
    private static final float COMPRESSION_QUALITY = 0.8f;
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public byte[] compressImage(String base64Image) throws IOException {
        if (base64Image == null || base64Image.isEmpty()) {
            return null;
        }

        String base64Data = base64Image;
        if (base64Image.contains(",")) {
            base64Data = base64Image.split(",")[1];
        }

        // Validate base64 size before decoding (aproximação: base64 é ~33% maior que original)
        long estimatedSize = (base64Data.length() * 3L) / 4L;
        if (estimatedSize > MAX_FILE_SIZE) {
            throw new IOException("Imagem muito grande. Tamanho máximo: 5MB");
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        BufferedImage originalImage = ImageIO.read(inputStream);

        if (originalImage == null) {
            throw new IOException("Invalid image format");
        }

        BufferedImage resizedImage = resizeImage(originalImage, MAX_WIDTH, MAX_HEIGHT);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "JPEG", outputStream);
        
        return outputStream.toByteArray();
    }

    public String convertToBase64(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth = maxWidth;
        int newHeight = maxHeight;

        if (originalWidth > originalHeight) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }

        if (newWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (int) (newWidth / aspectRatio);
        }
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (newHeight * aspectRatio);
        }

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resizedImage;
    }
}