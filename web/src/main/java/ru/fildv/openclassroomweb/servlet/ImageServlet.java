package ru.fildv.openclassroomweb.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.fildv.openclassroomservice.service.ImageService;

import java.io.IOException;
import java.io.InputStream;

import static ru.fildv.openclassroomutil.util.UrlPath.IMAGES;

@WebServlet(IMAGES + "/*")
public class ImageServlet extends HttpServlet {
    private final ImageService imageService = ImageService.getInstance();

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        var uri = req.getRequestURI();
        var imagePath = uri.replace(IMAGES, "");
        imageService.get(imagePath)
                .ifPresentOrElse(image -> {
                    resp.setContentType("application/octet-stream");
                    writeImage(image, resp);
                }, () -> resp.setStatus(404));
    }

    @SneakyThrows
    private void writeImage(final InputStream image,
                            final HttpServletResponse resp) {
        try (image; var outputStream = resp.getOutputStream()) {
            int cb;
            while ((cb = image.read()) != -1) {
                outputStream.write(cb);
            }
        }
    }
}
