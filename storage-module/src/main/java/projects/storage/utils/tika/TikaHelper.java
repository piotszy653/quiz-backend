package projects.storage.utils.tika;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeType;
import org.springframework.core.io.InputStreamSource;

import java.io.IOException;

public class TikaHelper {

    public static String getExtension(InputStreamSource inputStreamSource){
        try {
        TikaConfig config = new TikaConfig();
            MediaType mediaType = config.getDetector().detect(
                    TikaInputStream.get(inputStreamSource.getInputStream()),
                    new Metadata()
            );
        MimeType mimeType = config.getMimeRepository().forName(mediaType.toString());
        return mimeType.getExtension().replace(".","");
        } catch (IOException | TikaException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
