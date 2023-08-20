package com.spro.pcshop.util;

import java.io.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtils {

    public static byte[] readFile(String path){
        File file = new File(path);
        byte[] bytes = new byte[(int)file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file);){
            fileInputStream.read(bytes);
        return bytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file with path " + path, e);
        }
    }
    public static byte[] readFile(File file){
        byte[] bytes = new byte[(int)file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file);){
            fileInputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file with path " + file.getPath(), e);
        }
    }

    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] tmp = new byte[3 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error during image compression", e);
        }
    }

    public static byte[] decompressImage(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            byte[] tmp = new byte[3 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
            return outputStream.toByteArray();
        } catch (IOException | DataFormatException e) {
            throw new RuntimeException("Error during image decompression", e);
        }
    }

}
